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

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;

import rx.Observable;
import rx.Subscription;
import rx.subjects.ReplaySubject;

/**
 * Provides an abstract base class for a loader that returns a RxJava {@link Observable} as a
 * result.
 */
public abstract class BaseRxLoader<T> extends Loader<Observable<T>> {

    private Subscription mSubscription;
    private ReplaySubject<T> mSubject = ReplaySubject.create();

    public BaseRxLoader(@NonNull Application context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        if (takeContentChanged() || !mSubject.hasAnyValue()) {
            forceLoad();
        } else {
            deliverResult(mSubject.asObservable());
        }
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();

        final Observable<T> observable = getObservable();
        if (observable != null) {
            mSubscription = observable.subscribe(mSubject);
            deliverResult(mSubject.asObservable());
        } else {
            deliverResult(null);
        }
    }

    @Nullable
    protected abstract Observable<T> getObservable();

    @Override
    public void deliverResult(Observable<T> observable) {
        if (isStarted()) {
            super.deliverResult(observable);
        }
    }

    @Override
    protected boolean onCancelLoad() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
            return true;
        }

        return false;
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();

        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();

        onStopLoading();

        if (mSubject != null) {
            mSubject = ReplaySubject.create();
        }
    }
}
