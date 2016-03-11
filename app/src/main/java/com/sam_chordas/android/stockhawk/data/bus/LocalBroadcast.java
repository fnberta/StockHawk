/*
 * Copyright (c) 2016 Fabio Berta
 */

package com.sam_chordas.android.stockhawk.data.bus;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.sam_chordas.android.stockhawk.BuildConfig;

import javax.inject.Inject;


/**
 * Provides an implementation of {@link LocalBroadcast} using the Android
 * {@link LocalBroadcastManager}.
 */
public class LocalBroadcast {

    public static final String ACTION_DATA_UPDATED = BuildConfig.APPLICATION_ID + ".intents.actions.DATA_UPDATED";
    public static final String EXTRA_SUCCESSFUL = BuildConfig.APPLICATION_ID + ".intents.extras.SUCCESSFUL";
    private final LocalBroadcastManager mBroadcastManager;

    @Inject
    public LocalBroadcast(@NonNull LocalBroadcastManager broadcastManager) {
        mBroadcastManager = broadcastManager;
    }

    public void sendDataUpdated(boolean successful) {
        final Intent intent = new Intent(ACTION_DATA_UPDATED);
        intent.putExtra(EXTRA_SUCCESSFUL, successful);
        send(intent);
    }

    private void send(@NonNull Intent intent) {
        mBroadcastManager.sendBroadcast(intent);
    }
}
