package com.sam_chordas.android.stockhawk.presentation.common;

import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

/**
 * Defines an observable view model.
 */
public interface ViewModel extends Observable, BaseWorkerListener {

    /**
     * Saves the state of the view model in a bundle before recreation.
     *
     * @param outState the bundle to save the state in
     */
    void saveState(@NonNull Bundle outState);

    /**
     * Called when the view becomes visible to the user.
     */
    void onViewVisible();

    /**
     * Cleans up any long living tasks, e.g. RxJava subscriptions, in order to allow the view model
     * and the view it references to be garbage collected.
     */
    void onViewGone();

    interface ViewListener {

        void showMessage(@StringRes int message);

        void removeWorker(@NonNull String workerTag);
    }
}
