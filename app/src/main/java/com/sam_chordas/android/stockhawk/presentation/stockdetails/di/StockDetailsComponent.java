/*
 * Copyright (c) 2016 Fabio Berta
 */

package com.sam_chordas.android.stockhawk.presentation.stockdetails.di;

import com.sam_chordas.android.stockhawk.data.di.ApplicationComponent;
import com.sam_chordas.android.stockhawk.data.di.RepositoriesModule;
import com.sam_chordas.android.stockhawk.data.di.scopes.PerActivity;
import com.sam_chordas.android.stockhawk.presentation.stockdetails.StockDetailsActivity;
import com.sam_chordas.android.stockhawk.presentation.stockdetails.StockDetailsLoader;

import dagger.Component;

/**
 * Provides the dependencies for the stock details screen.
 */
@PerActivity
@Component(dependencies = {ApplicationComponent.class},
        modules = {StockDetailsViewModelModule.class, StockDetailsLoaderModule.class, RepositoriesModule.class})
public interface StockDetailsComponent {

    void inject(StockDetailsActivity stockDetailsActivity);

    StockDetailsLoader getStockDetailsLoader();
}
