package com.sam_chordas.android.stockhawk.presentation.common;

import android.databinding.BaseObservable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by fabio on 07.03.16.
 */
public class ViewModelBaseImpl extends BaseObservable implements ViewModel {

    private CompositeSubscription mSubscriptions = new CompositeSubscription();

    public ViewModelBaseImpl(@Nullable Bundle savedState) {
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

    protected CompositeSubscription getSubscriptions() {
        if (mSubscriptions == null || mSubscriptions.isUnsubscribed()) {
            mSubscriptions = new CompositeSubscription();
        }

        return mSubscriptions;
    }
}
