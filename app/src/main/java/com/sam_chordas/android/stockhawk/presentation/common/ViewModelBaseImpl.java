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
