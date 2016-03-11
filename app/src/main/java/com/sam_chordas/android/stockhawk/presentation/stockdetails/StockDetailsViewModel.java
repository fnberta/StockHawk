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
