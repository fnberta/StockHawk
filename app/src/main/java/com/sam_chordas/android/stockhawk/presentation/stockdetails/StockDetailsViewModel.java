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

package com.sam_chordas.android.stockhawk.presentation.stockdetails;

import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.sam_chordas.android.stockhawk.data.rest.QuoteTime;
import com.sam_chordas.android.stockhawk.presentation.common.ViewModel;

import rx.Observable;

/**
 * Defines an observable view model for the stock details screen.
 */
public interface StockDetailsViewModel extends ViewModel {

    @Bindable
    boolean isLoading();

    void setLoading(boolean loading);

    @Bindable
    boolean isEmpty();

    @Bindable
    LineData getChartData();

    void setChartData(@NonNull LineData chartData);

    void onDataLoaded(@Nullable Observable<QuoteTime> data);

    interface ViewListener extends ViewModel.ViewListener {

        void setChartColors(@NonNull LineDataSet lineDataSet);
    }
}
