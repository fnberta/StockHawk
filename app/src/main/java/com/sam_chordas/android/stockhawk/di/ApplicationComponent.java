/*
 * Copyright (c) 2016 Fabio Berta
 */

package com.sam_chordas.android.stockhawk.di;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.SharedPreferences;

import com.sam_chordas.android.stockhawk.StockHawk;
import com.sam_chordas.android.stockhawk.data.rest.YahooFinance;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Provides application wide dependencies as singletons.
 *
 * @see {@link StockHawk}
 */
@Singleton
@Component(modules = {ApplicationModule.class, YahooFinanceServiceModule.class})
public interface ApplicationComponent {

    Application getApplication();

    ContentResolver getContentResolver();

    SharedPreferences getSharedPreferences();

    AppWidgetManager getAppWidgetManager();

    YahooFinance getYahooFinance();
}
