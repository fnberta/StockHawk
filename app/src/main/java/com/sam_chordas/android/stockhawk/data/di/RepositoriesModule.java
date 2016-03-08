/*
 * Copyright (c) 2016 Fabio Berta
 */

package com.sam_chordas.android.stockhawk.data.di;

import android.app.Application;
import android.support.annotation.NonNull;

import com.sam_chordas.android.stockhawk.data.repositories.StockRepositoryImpl;
import com.sam_chordas.android.stockhawk.data.rest.YahooFinance;
import com.sam_chordas.android.stockhawk.domain.repositories.StockRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Defines which implementations to use for the repository interfaces and how to instantiate them.
 */
@Module
public class RepositoriesModule {

    public RepositoriesModule() {
    }

    @Provides
    StockRepository providesStockRepository(@NonNull Application application,
                                            @NonNull YahooFinance yahooFinance) {
        return new StockRepositoryImpl(application, yahooFinance);
    }
}
