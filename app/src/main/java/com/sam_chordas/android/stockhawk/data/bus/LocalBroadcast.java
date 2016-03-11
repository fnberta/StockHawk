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

    public static final String ACTION_DATA_UPDATED = BuildConfig.APPLICATION_ID + ".intents.DATA_UPDATED";
    private final LocalBroadcastManager mBroadcastManager;

    @Inject
    public LocalBroadcast(@NonNull LocalBroadcastManager broadcastManager) {
        mBroadcastManager = broadcastManager;
    }

    public void sendDataUpdated() {
        final Intent intent = new Intent(ACTION_DATA_UPDATED);
        send(intent);
    }

    private void send(@NonNull Intent intent) {
        mBroadcastManager.sendBroadcast(intent);
    }
}
