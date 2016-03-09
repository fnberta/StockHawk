/*
 * Copyright (c) 2016 Fabio Berta
 */

package com.sam_chordas.android.stockhawk.presentation.mystocks.di;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;

import com.sam_chordas.android.stockhawk.data.di.scopes.PerActivity;
import com.sam_chordas.android.stockhawk.data.provider.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.provider.QuoteProvider;

import dagger.Module;
import dagger.Provides;

/**
 * Defines which implementation to use for the paid compensations view model and how to instantiate
 * it.
 */
@Module
public class MyStocksLoaderModule {

    @PerActivity
    @Provides
    CursorLoader providesMyStocksLoader(@NonNull Application application) {
        return new CursorLoader(
                application,
                QuoteProvider.Quotes.CONTENT_URI,
                new String[]{
                        QuoteColumns._ID,
                        QuoteColumns.SYMBOL,
                        QuoteColumns.BID_PRICE,
                        QuoteColumns.PERCENT_CHANGE,
                        QuoteColumns.CHANGE},
                null,
                null,
                null
        );
    }
}
