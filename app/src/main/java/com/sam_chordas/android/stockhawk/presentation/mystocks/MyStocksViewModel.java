package com.sam_chordas.android.stockhawk.presentation.mystocks;

import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.CursorLoader;
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

    boolean isShowPercent();

    void onLoadingLocalStocks();

    @Override
    void onStockItemClick(int position);

    void onDeleteStockItem(long rowId);

    @Override
    void onStockEntered(@NonNull String stockSymbol);

    void onFabClick(View view);

    void onChangeUnitsMenuClick();

    interface ViewListener {
        boolean isNetworkAvailable();

        void showMessage(int message, int length);

        void removeWorker(@NonNull String workerTag);

        void showFindStockDialog();

        void loadUpdateStocksService();

        void loadSaveStockWorker(@NonNull String stockSymbol);

        void notifyItemsChanged();
    }
}
