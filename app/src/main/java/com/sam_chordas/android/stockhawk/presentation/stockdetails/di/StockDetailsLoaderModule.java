/*
 * Copyright (c) 2016 Fabio Berta
 */

package com.sam_chordas.android.stockhawk.presentation.stockdetails.di;

import android.app.Application;
import android.support.annotation.NonNull;

import com.sam_chordas.android.stockhawk.di.scopes.PerActivity;
import com.sam_chordas.android.stockhawk.domain.repositories.StockRepository;
import com.sam_chordas.android.stockhawk.presentation.stockdetails.StockDetailsLoader;

import dagger.Module;
import dagger.Provides;

/**
 * Defines which implementation to use for the paid compensations view model and how to instantiate
 * it.
 */
@Module
public class StockDetailsLoaderModule {

    private String mStockSymbol;

    public StockDetailsLoaderModule(@NonNull String stockSymbol) {
        mStockSymbol = stockSymbol;
    }

    @PerActivity
    @Provides
    StockDetailsLoader providesStockDetailsLoader(@NonNull Application application,
                                                  @NonNull StockRepository stockRepository) {
        return new StockDetailsLoader(application, stockRepository, mStockSymbol);
    }
}
