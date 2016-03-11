/*
 * Copyright (c) 2016 Fabio Berta.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
