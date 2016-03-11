/*
 * Copyright (c) 2016 Fabio Berta
 */

package com.sam_chordas.android.stockhawk.data.services.di;

import com.sam_chordas.android.stockhawk.data.services.UpdateStocksIntentService;
import com.sam_chordas.android.stockhawk.data.services.UpdateStocksTaskService;
import com.sam_chordas.android.stockhawk.di.ApplicationComponent;
import com.sam_chordas.android.stockhawk.di.RepositoriesModule;
import com.sam_chordas.android.stockhawk.di.scopes.PerService;

import dagger.Component;

/**
 * Provides the dependencies for the update stocks services.
 *
 * @see {@link UpdateStocksIntentService}
 */
@PerService
@Component(dependencies = {ApplicationComponent.class},
        modules = {RepositoriesModule.class})
public interface UpdateStocksServiceComponent {

    void inject(UpdateStocksIntentService updateStocksIntentService);

    void inject(UpdateStocksTaskService updateStocksTaskService);
}
