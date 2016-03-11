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

package com.sam_chordas.android.stockhawk.data.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.sam_chordas.android.stockhawk.StockHawk;
import com.sam_chordas.android.stockhawk.data.bus.LocalBroadcast;
import com.sam_chordas.android.stockhawk.data.services.di.DaggerUpdateStocksServiceComponent;
import com.sam_chordas.android.stockhawk.di.RepositoriesModule;
import com.sam_chordas.android.stockhawk.domain.repositories.StockRepository;

import javax.inject.Inject;

/**
 * Provides an {@link IntentService} that updates the locally stored stock values.
 */
public class UpdateStocksIntentService extends IntentService {

    @Inject
    StockRepository mStockRepo;
    @Inject
    LocalBroadcast mLocalBroadcast;

    public UpdateStocksIntentService() {
        super(UpdateStocksIntentService.class.getName());
    }

    public static void start(@NonNull Context context) {
        final Intent serviceIntent = new Intent(context, UpdateStocksIntentService.class);
        context.startService(serviceIntent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        injectDependencies();
        final boolean successful = mStockRepo.updateStocks();
        mLocalBroadcast.sendDataUpdated(successful);
    }

    private void injectDependencies() {
        DaggerUpdateStocksServiceComponent.builder()
                .applicationComponent(StockHawk.getAppComponent(this))
                .repositoriesModule(new RepositoriesModule())
                .build()
                .inject(this);
    }
}