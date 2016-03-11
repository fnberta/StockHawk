/*
 * Copyright (c) 2016 Fabio Berta
 */

package com.sam_chordas.android.stockhawk.presentation.stockdetails.di;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sam_chordas.android.stockhawk.di.scopes.PerActivity;
import com.sam_chordas.android.stockhawk.presentation.common.di.BaseViewModelModule;
import com.sam_chordas.android.stockhawk.presentation.stockdetails.StockDetailsViewModel;
import com.sam_chordas.android.stockhawk.presentation.stockdetails.StockDetailsViewModelImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Defines which implementation to use for the stock details view model and how to instantiate it.
 */
@Module
public class StockDetailsViewModelModule extends BaseViewModelModule<StockDetailsViewModel.ViewListener> {

    private final String mStockSymbol;

    public StockDetailsViewModelModule(@Nullable Bundle savedState,
                                       @NonNull StockDetailsViewModel.ViewListener view,
                                       @NonNull String stockSymbol) {
        super(savedState, view);

        mStockSymbol = stockSymbol;
    }

    @PerActivity
    @Provides
    StockDetailsViewModel providesStockDetailsViewModel() {
        return new StockDetailsViewModelImpl(mSavedState, mView, mStockSymbol);
    }
}
