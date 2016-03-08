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
import com.sam_chordas.android.stockhawk.data.repositories.QuoteException;
import com.sam_chordas.android.stockhawk.domain.repositories.StockRepository;
import com.sam_chordas.android.stockhawk.presentation.common.ViewModelBaseImpl;

import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by fabio on 07.03.16.
 */
public class MyStocksViewModelImpl extends ViewModelBaseImpl implements MyStocksViewModel {

    private static final String STATE_LOADING = "STATE_LOADING";
    private static final String STATE_SHOW_PERCENT = "STATE_SHOW_PERCENT";
    private MyStocksViewModel.ViewListener mView;
    private StockRepository mStockRepo;
    private boolean mLoading;
    private boolean mShowPercent;

    public MyStocksViewModelImpl(@Nullable Bundle savedState, @NonNull ViewListener view,
                                 @NonNull StockRepository stockRepo) {
        super(savedState);
        mView = view;
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
        // TODO: load details screen
    }

    @Override
    public void onDeleteStockItem(long rowId) {
        getSubscriptions().add(mStockRepo.deleteStock(rowId)
                .subscribe(new SingleSubscriber<Integer>() {
                    @Override
                    public void onSuccess(Integer value) {
                        mView.showMessage(R.string.snackbar_stock_removed, Snackbar.LENGTH_LONG);
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
                        mView.showMessage(R.string.snackbar_stock_added, Snackbar.LENGTH_LONG);
                    }

                    @Override
                    public void onError(Throwable error) {
                        mView.removeWorker(workerTag);

                        try {
                            showAppropriateErrorMessage((QuoteException) error);
                        } catch (ClassCastException e) {
                            mView.showMessage(R.string.snackbar_error_add_stock, Snackbar.LENGTH_LONG);
                        }

                    }
                })
        );
    }

    private void showAppropriateErrorMessage(QuoteException exception) {
        final int code = exception.getCode();
        switch (code) {
            case QuoteException.Code.ALREADY_SAVED:
                mView.showMessage(R.string.snackbar_error_add_stock_already_saved, Snackbar.LENGTH_LONG);
                break;
            case QuoteException.Code.SYMBOL_NOT_FOUND:
                mView.showMessage(R.string.snackbar_error_add_stock_not_found, Snackbar.LENGTH_LONG);
                break;
            default:
                mView.showMessage(R.string.snackbar_error_add_stock, Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onWorkerError(@NonNull String workerTag) {
        mView.removeWorker(workerTag);
        mView.showMessage(R.string.snackbar_error_unknown, Snackbar.LENGTH_LONG);
    }

    @Override
    public void onFabClick(View view) {
        if (mView.isNetworkAvailable()) {
            mView.showFindStockDialog();
        } else {
            mView.showMessage(R.string.snackbar_no_network, Snackbar.LENGTH_LONG);
        }
    }
}
