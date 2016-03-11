/*
 * Copyright (c) 2016 Fabio Berta
 */

package com.sam_chordas.android.stockhawk.di;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.sam_chordas.android.stockhawk.data.repositories.StockRepositoryImpl;
import com.sam_chordas.android.stockhawk.data.rest.YahooFinance;
import com.sam_chordas.android.stockhawk.domain.repositories.StockRepository;
import com.sam_chordas.android.stockhawk.presentation.widget.QuotesWidgetProvider;

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
    StockRepository providesStockRepository(@NonNull ContentResolver contentResolver,
                                            @NonNull SharedPreferences sharedPreferences,
                                            @NonNull AppWidgetManager appWidgetManager,
                                            @NonNull ComponentName widgetProviderComponent,
                                            @NonNull YahooFinance yahooFinance) {
        return new StockRepositoryImpl(contentResolver, appWidgetManager, widgetProviderComponent,
                sharedPreferences, yahooFinance);
    }

    @Provides
    ComponentName providesWidgetProviderComponent(@NonNull Application application) {
        return new ComponentName(application, QuotesWidgetProvider.class);
    }
}
