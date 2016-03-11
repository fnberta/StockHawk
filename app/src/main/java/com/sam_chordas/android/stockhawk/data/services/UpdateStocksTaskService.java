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

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;
import com.sam_chordas.android.stockhawk.StockHawk;
import com.sam_chordas.android.stockhawk.data.services.di.DaggerUpdateStocksServiceComponent;
import com.sam_chordas.android.stockhawk.di.RepositoriesModule;
import com.sam_chordas.android.stockhawk.domain.repositories.StockRepository;

import javax.inject.Inject;

/**
 * Created by sam_chordas on 9/30/15.
 * This GCMTask service pulls and updates stocks once every hour after the app has been opened.
 * This is so Widget data stays up to date.
 */
public class UpdateStocksTaskService extends GcmTaskService {

    private static final String SERVICE_TAG = UpdateStocksTaskService.class.getCanonicalName();
    @Inject
    StockRepository mStockRepo;

    /**
     * Starts or updates this periodic task service.
     *
     * @param context the context to use for the operation
     * @param period  the refresh period
     */
    public static void startOrUpdate(@NonNull Context context, long period) {
        final PeriodicTask periodicTask = new PeriodicTask.Builder()
                .setService(UpdateStocksTaskService.class)
                .setTag(SERVICE_TAG)
                .setPeriod(period)
                .setFlex(10L)
                .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                .setRequiresCharging(false)
                .setUpdateCurrent(true)
                .build();
        GcmNetworkManager.getInstance(context).schedule(periodicTask);
    }

    /**
     * Cancels this periodic task service.
     *
     * @param context the context to use for the operation
     */
    public static void cancel(@NonNull Context context) {
        GcmNetworkManager.getInstance(context).cancelTask(SERVICE_TAG, UpdateStocksTaskService.class);
    }

    @Override
    public int onRunTask(TaskParams params) {
        injectDependencies();
        return mStockRepo.updateStocks()
                ? GcmNetworkManager.RESULT_SUCCESS
                : GcmNetworkManager.RESULT_RESCHEDULE;
    }

    private void injectDependencies() {
        DaggerUpdateStocksServiceComponent.builder()
                .applicationComponent(StockHawk.getAppComponent(this))
                .repositoriesModule(new RepositoriesModule())
                .build()
                .inject(this);
    }
}
