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

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.sam_chordas.android.stockhawk.StockHawk;
import com.sam_chordas.android.stockhawk.presentation.common.di.DaggerWorkerComponent;
import com.sam_chordas.android.stockhawk.presentation.common.di.WorkerComponent;

import rx.Observable;
import rx.Subscription;
import rx.subjects.ReplaySubject;

/**
 * Provides an abstract class for a so-called headless {@link Fragment}, which does not contain
 * any UI elements and is retained across configuration changes. It is useful for encapsulating
 * background tasks.
 */
public abstract class BaseRxWorker<T, S extends BaseWorkerListener> extends Fragment {

    private final ReplaySubject<T> mSubject = ReplaySubject.create();
    protected S mListener;
    private Subscription mSubscription;

    public BaseRxWorker() {
        // empty default constructor
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (S) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BaseWorkerListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);

        // inject dependencies
        final WorkerComponent component = DaggerWorkerComponent.builder()
                .applicationComponent(StockHawk.getAppComponent(getActivity()))
                .build();
        injectWorkerDependencies(component);

        final Observable<T> observable = getObservable(getArguments());
        if (observable != null) {
            mSubscription = observable.subscribe(mSubject);
        } else {
            onError();
        }
    }

    protected abstract void injectWorkerDependencies(@NonNull WorkerComponent component);

    @Override
    public void onStart() {
        super.onStart();

        setStream(mSubject.asObservable());
    }


    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Nullable
    protected abstract Observable<T> getObservable(@NonNull Bundle args);

    protected abstract void onError();

    protected abstract void setStream(@NonNull Observable<T> observable);
}
