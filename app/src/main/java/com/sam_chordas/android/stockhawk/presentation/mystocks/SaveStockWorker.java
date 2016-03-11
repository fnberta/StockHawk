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

package com.sam_chordas.android.stockhawk.presentation.mystocks;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.sam_chordas.android.stockhawk.domain.repositories.StockRepository;
import com.sam_chordas.android.stockhawk.presentation.common.BaseRxWorker;
import com.sam_chordas.android.stockhawk.presentation.common.di.WorkerComponent;

import javax.inject.Inject;

import rx.Observable;

/**
 * Hosts the {@link Observable} that fetches the data for a newly entered stock symbol and saves it
 * to the local data store.
 */
public class SaveStockWorker extends BaseRxWorker<Uri, SaveStockWorkerListener> {

    private static final String WORKER_TAG = SaveStockWorker.class.getCanonicalName();
    private static final String KEY_STOCK_SYMBOL = "STOCK_SYMBOL";

    @Inject
    StockRepository mStockRepo;

    public static SaveStockWorker attach(@NonNull FragmentManager fm, @NonNull String stockSymbol) {
        SaveStockWorker worker = (SaveStockWorker) fm.findFragmentByTag(WORKER_TAG);
        if (worker == null) {
            worker = new SaveStockWorker();
            final Bundle args = new Bundle();
            args.putString(KEY_STOCK_SYMBOL, stockSymbol);
            worker.setArguments(args);

            fm.beginTransaction()
                    .add(worker, WORKER_TAG)
                    .commit();
        }

        return worker;
    }

    @Override
    protected void injectWorkerDependencies(@NonNull WorkerComponent component) {
        component.inject(this);
    }

    @Nullable
    @Override
    protected Observable<Uri> getObservable(@NonNull Bundle args) {
        final String stockSymbol = args.getString(KEY_STOCK_SYMBOL);
        if (!TextUtils.isEmpty(stockSymbol)) {
            return mStockRepo.saveSymbol(stockSymbol).toObservable();
        }

        return null;
    }

    @Override
    protected void onError() {
        mListener.onWorkerError(WORKER_TAG);
    }

    @Override
    protected void setStream(@NonNull Observable<Uri> observable) {
        mListener.setSaveStockStream(observable.toSingle(), WORKER_TAG);
    }
}
