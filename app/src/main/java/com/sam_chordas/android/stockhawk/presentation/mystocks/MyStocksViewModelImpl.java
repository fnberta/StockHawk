package com.sam_chordas.android.stockhawk.presentation.mystocks;

import android.databinding.Bindable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;

import com.sam_chordas.android.stockhawk.BR;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.repositories.QuoteException;
import com.sam_chordas.android.stockhawk.data.repositories.QuoteException.Code;
import com.sam_chordas.android.stockhawk.domain.repositories.StockRepository;
import com.sam_chordas.android.stockhawk.presentation.common.ViewModelBaseImpl;
import com.sam_chordas.android.stockhawk.utils.Utils;

import rx.Single;
import rx.SingleSubscriber;

/**
 * Created by fabio on 07.03.16.
 */
public class MyStocksViewModelImpl extends ViewModelBaseImpl<MyStocksViewModel.ViewListener>
        implements MyStocksViewModel {

    private static final String STATE_LOADING = "STATE_LOADING";
    private static final String STATE_REFRESHING = "STATE_REFRESHING";
    private StockRepository mStockRepo;
    private boolean mLoading;
    private boolean mRefreshing;

    public MyStocksViewModelImpl(@Nullable Bundle savedState,
                                 @NonNull MyStocksViewModel.ViewListener view,
                                 @NonNull StockRepository stockRepo) {
        super(savedState, view);

        mStockRepo = stockRepo;

        if (savedState != null) {
            mLoading = savedState.getBoolean(STATE_LOADING);
            mRefreshing = savedState.getBoolean(STATE_REFRESHING);
        } else {
            mLoading = true;
        }
    }

    @Override
    public void saveState(@NonNull Bundle outState) {
        super.saveState(outState);

        outState.putBoolean(STATE_LOADING, mLoading);
        outState.putBoolean(STATE_REFRESHING, mRefreshing);
    }

    @Bindable
    @Override
    public boolean isLoading() {
        return mLoading;
    }

    @Override
    public void setLoading(boolean loading) {
        mLoading = loading;
        notifyPropertyChanged(BR.loading);
    }

    @Override
    @Bindable
    public boolean isRefreshing() {
        return mRefreshing;
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        mRefreshing = refreshing;
        notifyPropertyChanged(BR.refreshing);
    }

    @Override
    public void onViewVisible() {
        super.onViewVisible();

        if (!mView.isNetworkAvailable()) {
            mView.showMessage(R.string.snackbar_no_network);
        }
    }

    @Override
    public void onLoadingLocalStocks() {
        mView.loadPeriodicQueryService();

        if (mView.isNetworkAvailable()) {
            mView.loadUpdateStocksService();
        }
    }

    @Override
    public void onStockItemClick(int position) {
        mView.showStockDetailsScreen(position);
    }

    @Override
    public void onDeleteStockItem(long rowId) {
        getSubscriptions().add(mStockRepo.deleteStock(rowId)
                .subscribe(new SingleSubscriber<Integer>() {
                    @Override
                    public void onSuccess(Integer value) {
                        // do nothing
                    }

                    @Override
                    public void onError(Throwable error) {
                        // TODO: handle error
                    }
                })
        );
    }

    @Override
    public void onStockEntered(@NonNull final String stockSymbol) {
        if (!TextUtils.isEmpty(stockSymbol)) {
            mView.showProgressDialog(R.string.progress_saving_symbol);
            mView.loadSaveStockWorker(stockSymbol);
        }
    }

    @Override
    public void setSaveStockStream(@NonNull Single<Uri> single, @NonNull final String workerTag) {
        getSubscriptions().add(single
                .subscribe(new SingleSubscriber<Uri>() {
                    @Override
                    public void onSuccess(Uri value) {
                        mView.removeWorker(workerTag);
                        mView.hideProgressDialog();
                        mView.showMessage(R.string.snackbar_stock_added);
                    }

                    @Override
                    public void onError(Throwable error) {
                        mView.removeWorker(workerTag);
                        mView.hideProgressDialog();

                        if (error instanceof QuoteException) {
                            @Code final int code = ((QuoteException) error).getCode();
                            switch (code) {
                                case Code.ALREADY_SAVED:
                                    mView.showMessage(R.string.snackbar_error_add_stock_already_saved);
                                    break;
                                case Code.SYMBOL_NOT_FOUND:
                                    mView.showMessage(R.string.snackbar_error_add_stock_not_found);
                                    break;
                            }
                        } else {
                            mView.showMessage(R.string.snackbar_error_add_stock);
                        }
                    }
                })
        );
    }

    @Override
    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mView.loadUpdateStocksService();
            }
        };
    }

    @Override
    public void onFabClick(View view) {
        if (mView.isNetworkAvailable()) {
            mView.showFindStockDialog();
        } else {
            mView.showMessage(R.string.snackbar_no_network);
        }
    }

    @Override
    public void onChangeUnitsMenuClick() {
        mStockRepo.toggleShowPercentages();
        mView.notifyItemsChanged();
    }
}
