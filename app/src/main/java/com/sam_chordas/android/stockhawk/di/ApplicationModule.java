/*
 * Copyright (c) 2016 Fabio Berta
 */

package com.sam_chordas.android.stockhawk.di;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Defines how the application wide dependencies are instantiated.
 */
@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(@NonNull Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    ContentResolver providesContentResolver(@NonNull Application application) {
        return application.getContentResolver();
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(@NonNull Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    LocalBroadcastManager providesLocalBroadcastManager(@NonNull Application application) {
        return LocalBroadcastManager.getInstance(application);
    }

    @Provides
    @Singleton
    AppWidgetManager providesAppWidgetManager(@NonNull Application application) {
        return AppWidgetManager.getInstance(application);
    }
}
