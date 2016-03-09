/*
 * Copyright (c) 2016 Fabio Berta
 */

package com.sam_chordas.android.stockhawk.presentation.stockdetails.di;

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sam_chordas.android.stockhawk.data.di.scopes.PerActivity;
import com.sam_chordas.android.stockhawk.domain.repositories.StockRepository;
import com.sam_chordas.android.stockhawk.presentation.common.di.BaseViewModelModule;
import com.sam_chordas.android.stockhawk.presentation.stockdetails.StockDetailsLoader;
import com.sam_chordas.android.stockhawk.presentation.stockdetails.StockDetailsViewModel;
import com.sam_chordas.android.stockhawk.presentation.stockdetails.StockDetailsViewModelImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Defines which implementation to use for the paid compensations view model and how to instantiate
 * it.
 */
@Module
public class StockDetailsViewModelModule extends BaseViewModelModule<StockDetailsViewModel.ViewListener> {

    private String mStockSymbol;

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

    @PerActivity
    @Provides
    StockDetailsLoader providesStockDetailsLoader(@NonNull Application application,
                                                  @NonNull StockRepository stockRepository) {
        return new StockDetailsLoader(application, stockRepository, mStockSymbol);
    }
}
