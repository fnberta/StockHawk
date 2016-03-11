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

package com.sam_chordas.android.stockhawk.presentation.common;

import android.databinding.BaseObservable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sam_chordas.android.stockhawk.R;

import rx.subscriptions.CompositeSubscription;

/**
 * Provides an abstract base implementation of the {@link ViewModel} interface.
 */
public abstract class ViewModelBaseImpl<T extends ViewModel.ViewListener> extends BaseObservable
        implements ViewModel {

    protected final T mView;
    private CompositeSubscription mSubscriptions = new CompositeSubscription();

    public ViewModelBaseImpl(@Nullable Bundle savedState, T view) {
        mView = view;
    }

    @Override
    @CallSuper
    public void saveState(@NonNull Bundle outState) {
        // Empty default implementation
    }

    @Override
    public void onViewVisible() {
        // empty default implementation
    }

    @Override
    public void onViewGone() {
        if (mSubscriptions.hasSubscriptions()) {
            mSubscriptions.unsubscribe();
        }
    }

    @Override
    public void onWorkerError(@NonNull String workerTag) {
        mView.removeWorker(workerTag);
        mView.showMessage(R.string.snackbar_error_unknown);
    }

    protected CompositeSubscription getSubscriptions() {
        if (mSubscriptions == null || mSubscriptions.isUnsubscribed()) {
            mSubscriptions = new CompositeSubscription();
        }

        return mSubscriptions;
    }
}
