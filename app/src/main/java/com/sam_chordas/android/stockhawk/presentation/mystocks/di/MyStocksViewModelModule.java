/*
 * Copyright (c) 2016 Fabio Berta
 */

package com.sam_chordas.android.stockhawk.presentation.mystocks.di;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sam_chordas.android.stockhawk.data.di.scopes.PerActivity;
import com.sam_chordas.android.stockhawk.domain.repositories.StockRepository;
import com.sam_chordas.android.stockhawk.presentation.mystocks.MyStocksViewModel;
import com.sam_chordas.android.stockhawk.presentation.mystocks.MyStocksViewModelImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Defines which implementation to use for the paid compensations view model and how to instantiate
 * it.
 */
@Module
public class MyStocksViewModelModule {

    private Bundle mSavedState;
    private MyStocksViewModel.ViewListener mView;

    public MyStocksViewModelModule(@Nullable Bundle savedState,
                                   @NonNull MyStocksViewModel.ViewListener view) {
        mSavedState = savedState;
        mView = view;
    }

    @PerActivity
    @Provides
    MyStocksViewModel providesMyStocksViewModel(@NonNull StockRepository stockRepository) {
        return new MyStocksViewModelImpl(mSavedState, mView, stockRepository);
    }
}
