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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sam_chordas.android.stockhawk.di.scopes.PerActivity;
import com.sam_chordas.android.stockhawk.presentation.common.di.BaseViewModelModule;
import com.sam_chordas.android.stockhawk.presentation.stockdetails.StockDetailsViewModel;
import com.sam_chordas.android.stockhawk.presentation.stockdetails.StockDetailsViewModelImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Defines which implementation to use for the stock details view model and how to instantiate it.
 */
@Module
public class StockDetailsViewModelModule extends BaseViewModelModule<StockDetailsViewModel.ViewListener> {

    private final String mStockSymbol;

    public StockDetailsViewModelModule(@Nullable Bundle savedState,
                                       @NonNull StockDetailsViewModel.ViewListener view,
                                       @NonNull String stockSymbol) {
        super(savedState, view);

        mStockSymbol = stockSymbol;
    }

    @PerActivity
    @Provides
    StockDetailsViewModel providesStockDetailsViewModel() {
        return new StockDetailsViewModelImpl(mSavedState, mView, mStockSymbol);
    }
}
