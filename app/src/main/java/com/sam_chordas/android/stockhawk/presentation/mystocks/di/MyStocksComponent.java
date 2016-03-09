/*
 * Copyright (c) 2016 Fabio Berta
 */

package com.sam_chordas.android.stockhawk.presentation.mystocks.di;

import android.support.v4.content.CursorLoader;

import com.sam_chordas.android.stockhawk.data.di.ApplicationComponent;
import com.sam_chordas.android.stockhawk.data.di.RepositoriesModule;
import com.sam_chordas.android.stockhawk.data.di.scopes.PerActivity;
import com.sam_chordas.android.stockhawk.presentation.mystocks.MyStocksActivity;

import dagger.Component;

/**
 * Provides the dependencies for the my stocks screen.
 */
@PerActivity
@Component(dependencies = {ApplicationComponent.class},
        modules = {MyStocksViewModelModule.class, RepositoriesModule.class})
public interface MyStocksComponent {

    void inject(MyStocksActivity myStocksActivity);

    CursorLoader getMyStocksLoader();
}
