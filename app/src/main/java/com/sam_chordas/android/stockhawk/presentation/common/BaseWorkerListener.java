/*
 * Copyright (c) 2016 Fabio Berta
 */

package com.sam_chordas.android.stockhawk.presentation.common;

import android.support.annotation.NonNull;

/**
 * Defines the base error handler for headless worker fragments.
 */
public interface BaseWorkerListener {

    void onWorkerError(@NonNull String workerTag);
}
