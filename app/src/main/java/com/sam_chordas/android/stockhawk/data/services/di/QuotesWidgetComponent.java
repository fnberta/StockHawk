/*
 * Copyright (c) 2016 Fabio Berta
 */

package com.sam_chordas.android.stockhawk.data.services.di;

import com.sam_chordas.android.stockhawk.di.ApplicationComponent;
import com.sam_chordas.android.stockhawk.di.RepositoriesModule;
import com.sam_chordas.android.stockhawk.di.scopes.PerActivity;
import com.sam_chordas.android.stockhawk.presentation.widget.QuotesWidgetViewsFactory;

import dagger.Component;

/**
 * Provides the dependencies for the my stocks widget.
 */
@PerActivity
@Component(dependencies = {ApplicationComponent.class},
        modules = {RepositoriesModule.class})
public interface QuotesWidgetComponent {

    QuotesWidgetViewsFactory getQuotesViewsFactory();
}
