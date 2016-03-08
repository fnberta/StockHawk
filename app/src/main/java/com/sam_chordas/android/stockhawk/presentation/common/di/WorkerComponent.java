/*
 * Copyright (c) 2016 Fabio Berta
 */

package com.sam_chordas.android.stockhawk.presentation.common.di;

import com.sam_chordas.android.stockhawk.data.di.ApplicationComponent;
import com.sam_chordas.android.stockhawk.data.di.RepositoriesModule;
import com.sam_chordas.android.stockhawk.data.di.scopes.PerFragment;
import com.sam_chordas.android.stockhawk.presentation.mystocks.SaveStockWorker;

import dagger.Component;

/**
 * Defines the dependencies for headless worker fragments.
 */
@PerFragment
@Component(dependencies = {ApplicationComponent.class},
        modules = {RepositoriesModule.class})
public interface WorkerComponent {

    void inject(SaveStockWorker saveStockWorker);
}
