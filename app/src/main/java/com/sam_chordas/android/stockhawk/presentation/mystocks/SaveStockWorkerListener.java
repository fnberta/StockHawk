package com.sam_chordas.android.stockhawk.presentation.mystocks;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.sam_chordas.android.stockhawk.presentation.common.BaseWorkerListener;

import rx.Single;

/**
 * Defines the interaction of the {@link SaveStockWorker} worker fragment with other components.
 */
public interface SaveStockWorkerListener extends BaseWorkerListener {

    void setSaveStockStream(@NonNull Single<Uri> single, @NonNull String workerTag);
}
