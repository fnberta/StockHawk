package com.sam_chordas.android.stockhawk.presentation.common;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.View;

import com.sam_chordas.android.stockhawk.R;

import javax.inject.Inject;

/**
 * Created by fabio on 09.03.16.
 */
public abstract class BaseActivity<T extends ViewModel> extends AppCompatActivity
        implements ViewModel.ViewListener, BaseWorkerListener {

    @Inject
    protected T mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mViewModel.saveState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mViewModel.onViewVisible();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mViewModel.onViewGone();
    }

    @Override
    public void showMessage(@StringRes int message) {
        showMessage(message, Snackbar.LENGTH_LONG);
    }

    @Override
    public void showMessage(@StringRes int message, int length) {
        Snackbar.make(getSnackbarView(), message, length).show();
    }

    protected abstract View getSnackbarView();

    @Override
    public void onWorkerError(@NonNull String workerTag) {
        mViewModel.onWorkerError(workerTag);
    }

    @Override
    public void removeWorker(@NonNull String workerTag) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final Fragment worker = fragmentManager.findFragmentByTag(workerTag);
        if (worker != null) {
            fragmentManager
                    .beginTransaction()
                    .remove(worker)
                    .commitAllowingStateLoss();
        }
    }
}
