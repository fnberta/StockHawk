/*
 * Copyright (c) 2016 Fabio Berta
 */

package com.sam_chordas.android.stockhawk.presentation.common.di;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sam_chordas.android.stockhawk.presentation.common.ViewModel;

/**
 * Provides an abstract base class for modules defining the instantiation of view models.
 */
public abstract class BaseViewModelModule<T extends ViewModel.ViewListener> {

    protected final Bundle mSavedState;
    protected final T mView;

    public BaseViewModelModule(@Nullable Bundle savedState, @NonNull T view) {
        mSavedState = savedState;
        mView = view;
    }
}
