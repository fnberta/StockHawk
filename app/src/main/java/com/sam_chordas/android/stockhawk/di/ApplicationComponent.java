/*
 * Copyright (c) 2016 Fabio Berta.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sam_chordas.android.stockhawk.di;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;

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

    LocalBroadcastManager getLocalBroadcastManager();

    AppWidgetManager getAppWidgetManager();

    YahooFinance getYahooFinance();
}
