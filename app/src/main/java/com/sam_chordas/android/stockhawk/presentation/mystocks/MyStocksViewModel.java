package com.sam_chordas.android.stockhawk.presentation.mystocks;

import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.sam_chordas.android.stockhawk.presentation.common.ViewModel;
import com.sam_chordas.android.stockhawk.utils.MessageAction;

/**
 * Created by fabio on 07.03.16.
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

    @Override
    void onStockItemClick(int position);

    void onDeleteStockItem(long rowId);

    @Override
    void onStockEntered(@NonNull String stockSymbol);

    SwipeRefreshLayout.OnRefreshListener getOnRefreshListener();

    void onFabClick(View view);

    void onChangeUnitsMenuClick();

    void onDefaultSymbolsSettingsChanged();

    void onSyncSettingsChanged();

    interface ViewListener extends ViewModel.ViewListener {
        boolean isNetworkAvailable();

        boolean isDataAvailable();

        void showFindStockDialog();

        void loadUpdateStocksService();

        void loadPeriodicUpdateStocksService(long period);

        void cancelPeriodicUpdateStocksService();

        void loadSaveStockWorker(@NonNull String stockSymbol);

        void notifyItemsChanged();

        void showStockDetailsScreen(int position);

        void showProgressDialog(@StringRes int message);

        void hideProgressDialog();
    }
}
