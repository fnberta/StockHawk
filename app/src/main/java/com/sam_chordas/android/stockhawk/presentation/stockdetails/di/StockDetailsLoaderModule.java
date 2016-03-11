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

package com.sam_chordas.android.stockhawk.presentation.stockdetails.di;

import android.app.Application;
import android.support.annotation.NonNull;

import com.sam_chordas.android.stockhawk.di.scopes.PerActivity;
import com.sam_chordas.android.stockhawk.domain.repositories.StockRepository;
import com.sam_chordas.android.stockhawk.presentation.stockdetails.StockDetailsLoader;

import dagger.Module;
import dagger.Provides;

/**
 * Defines how the stock details loader is constructed.
 */
@Module
public class StockDetailsLoaderModule {

    private final String mStockSymbol;

    public StockDetailsLoaderModule(@NonNull String stockSymbol) {
        mStockSymbol = stockSymbol;
    }

    @PerActivity
    @Provides
    StockDetailsLoader providesStockDetailsLoader(@NonNull Application application,
                                                  @NonNull StockRepository stockRepository) {
        return new StockDetailsLoader(application, stockRepository, mStockSymbol);
    }
}
