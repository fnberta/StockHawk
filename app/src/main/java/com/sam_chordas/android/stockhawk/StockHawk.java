package com.sam_chordas.android.stockhawk;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.sam_chordas.android.stockhawk.data.di.ApplicationComponent;
import com.sam_chordas.android.stockhawk.data.di.ApplicationModule;
import com.sam_chordas.android.stockhawk.data.di.DaggerApplicationComponent;
import com.sam_chordas.android.stockhawk.data.di.YahooFinanceServiceModule;

/**
 * Created by fabio on 07.03.16.
 */
public class StockHawk extends Application {

    private ApplicationComponent mAppComponent;

    public static ApplicationComponent getAppComponent(@NonNull Context context) {
        return ((StockHawk) context.getApplicationContext()).getAppComponent();
    }

    private ApplicationComponent getAppComponent() {
        return mAppComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        buildAppComponent();
    }

    private void buildAppComponent() {
        mAppComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .yahooFinanceServiceModule(new YahooFinanceServiceModule())
                .build();
    }
}
