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

package com.sam_chordas.android.stockhawk.presentation.stockdetails;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sam_chordas.android.stockhawk.data.rest.QuoteTime;
import com.sam_chordas.android.stockhawk.domain.repositories.StockRepository;
import com.sam_chordas.android.stockhawk.presentation.common.BaseRxLoader;

import rx.Observable;

/**
 * Loads the stock's value over the last 6 months from the internet.
 */
public class StockDetailsLoader extends BaseRxLoader<QuoteTime> {

    private final StockRepository mStockRepo;
    private final String mStockSymbol;

    public StockDetailsLoader(@NonNull Application context, @NonNull StockRepository stockRepo,
                              @NonNull String stockSymbol) {
        super(context);

        mStockSymbol = stockSymbol;
        mStockRepo = stockRepo;
    }

    @Nullable
    @Override
    protected Observable<QuoteTime> getObservable() {
        return mStockRepo.getStockDataOverTime(mStockSymbol);
    }
}
