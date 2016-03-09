/*
 * Copyright (c) 2016 Fabio Berta
 */

package com.sam_chordas.android.stockhawk.presentation.mystocks.di;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sam_chordas.android.stockhawk.data.di.scopes.PerActivity;
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
