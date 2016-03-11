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

package com.sam_chordas.android.stockhawk;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.sam_chordas.android.stockhawk.di.ApplicationComponent;
import com.sam_chordas.android.stockhawk.di.ApplicationModule;
import com.sam_chordas.android.stockhawk.di.DaggerApplicationComponent;

/**
 * Instantiates application wide components.
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
                .build();
    }
}
