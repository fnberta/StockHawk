package com.sam_chordas.android.stockhawk.presentation.mystocks;

import android.databinding.Bindable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;

import com.sam_chordas.android.stockhawk.BR;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.repositories.AlreadySavedException;
import com.sam_chordas.android.stockhawk.domain.repositories.StockRepository;
import com.sam_chordas.android.stockhawk.presentation.common.ViewModelBaseImpl;

import rx.Single;
import rx.SingleSubscriber;

/**
 * Created by fabio on 07.03.16.
 */
public class MyStocksViewModelImpl extends ViewModelBaseImpl<MyStocksViewModel.ViewListener> implements MyStocksViewModel {

    private static final String STATE_LOADING = "STATE_LOADING";
    private static final String STATE_SHOW_PERCENT = "STATE_SHOW_PERCENT";
    private StockRepository mStockRepo;
    private boolean mLoading;
    private boolean mShowPercent;

    public MyStocksViewModelImpl(@Nullable Bundle savedState,
                                 @NonNull MyStocksViewModel.ViewListener view,
                                 @NonNull StockRepository stockRepo) {
        super(savedState, view);

        mStockRepo = stockRepo;

        if (savedState != null) {
            mLoading = savedState.getBoolean(STATE_LOADING);
            mShowPercent = savedState.getBoolean(STATE_SHOW_PERCENT);
        } else {
            mLoading = true;
            mShowPercent = true;
        }
    }

    @Override
    public void saveState(@NonNull Bundle outState) {
        super.saveState(outState);

        outState.putBoolean(STATE_LOADING, mLoading);
        outState.putBoolean(STATE_SHOW_PERCENT, mShowPercent);
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
    public boolean isShowPercent() {
        return mShowPercent;
    }

    @Override
    public void onLoadingLocalStocks() {
        if (mView.isNetworkAvailable()) {
            mView.loadUpdateStocksService();
        } else {
            mView.showMessage(R.string.snackbar_no_network, Snackbar.LENGTH_INDEFINITE);
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
    public void onChangeUnitsMenuClick() {
        mShowPercent = !mShowPercent;
        mView.notifyItemsChanged();
    }

    @Override
    public void onStockEntered(@NonNull final String stockSymbol) {
        if (!TextUtils.isEmpty(stockSymbol)) {
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
                        mView.showMessage(R.string.snackbar_stock_added);
                    }

                    @Override
                    public void onError(Throwable error) {
                        mView.removeWorker(workerTag);
                        if (error instanceof AlreadySavedException) {
                            mView.showMessage(R.string.snackbar_error_add_stock_already_saved);
                        } else {
                            mView.showMessage(R.string.snackbar_error_add_stock_not_found);
                        }
                    }
                })
        );
    }

    @Override
    public void onFabClick(View view) {
        if (mView.isNetworkAvailable()) {
            mView.showFindStockDialog();
        } else {
            mView.showMessage(R.string.snackbar_no_network);
        }
    }
}
