package com.sam_chordas.android.stockhawk.presentation.mystocks;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.sam_chordas.android.stockhawk.presentation.common.BaseWorkerListener;

import rx.Single;

/**
 * Created by fabio on 08.03.16.
 */
public interface SaveStockWorkerListener extends BaseWorkerListener {

    void setSaveStockStream(@NonNull Single<Uri> single, @NonNull String workerTag);
}
