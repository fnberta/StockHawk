package com.sam_chordas.android.stockhawk.presentation.mystocks;

import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.sam_chordas.android.stockhawk.presentation.common.ViewModel;

/**
 * Defines an observable view model for the my stocks screen.
 */
public interface MyStocksViewModel extends ViewModel, MyStocksRecyclerAdapter.AdapterListener,
        FindStockDialogFragment.DialogListener, SaveStockWorkerListener {

    @Bindable
    boolean isLoading();

    void setLoading(boolean loading);

    @Bindable
    boolean isEmpty();

    @Bindable
    boolean isRefreshing();

    void setRefreshing(boolean refreshing);

    void onLoadingLocalStocks();

    void onDataUpdated(boolean successful);

    @Override
    void onStockItemClick(int position);

    void onDeleteStockItem(long rowId);

    @Override
    void onStockEntered(@NonNull String stockSymbol);

    SwipeRefreshLayout.OnRefreshListener getOnRefreshListener();

    void onFabClick(View view);

    void onChangeUnitsMenuClick();

    void onDefaultSymbolsSettingsChanged();

    interface ViewListener extends ViewModel.ViewListener {
        boolean isNetworkAvailable();

        boolean isDataAvailable();

        void showFindStockDialog();

        void startUpdateStocksService();

        void startPeriodicUpdateStocksService(long period);

        void loadSaveStockWorker(@NonNull String stockSymbol);

        void notifyItemsChanged();

        void showStockDetailsScreen(int position);

        void showProgressDialog(@StringRes int message);

        void hideProgressDialog();
    }
}
