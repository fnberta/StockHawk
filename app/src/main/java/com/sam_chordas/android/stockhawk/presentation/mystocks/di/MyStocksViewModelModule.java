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

package com.sam_chordas.android.stockhawk.presentation.mystocks.di;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sam_chordas.android.stockhawk.di.scopes.PerActivity;
import com.sam_chordas.android.stockhawk.domain.repositories.StockRepository;
import com.sam_chordas.android.stockhawk.presentation.common.di.BaseViewModelModule;
import com.sam_chordas.android.stockhawk.presentation.mystocks.MyStocksViewModel;
import com.sam_chordas.android.stockhawk.presentation.mystocks.MyStocksViewModelImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Defines which implementation to use for the paid compensations view model and how to instantiate
 * it.
 */
@Module
public class MyStocksViewModelModule extends BaseViewModelModule<MyStocksViewModel.ViewListener> {


    public MyStocksViewModelModule(@Nullable Bundle savedState,
                                   @NonNull MyStocksViewModel.ViewListener view) {
        super(savedState, view);
    }

    @PerActivity
    @Provides
    MyStocksViewModel providesMyStocksViewModel(@NonNull StockRepository stockRepository) {
        return new MyStocksViewModelImpl(mSavedState, mView, stockRepository);
    }
}
