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
