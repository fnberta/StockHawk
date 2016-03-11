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

package com.sam_chordas.android.stockhawk.data.bus;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.sam_chordas.android.stockhawk.BuildConfig;

import javax.inject.Inject;


/**
 * Provides methods for sending local messages using {@link LocalBroadcastManager}.
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
