package com.sam_chordas.android.stockhawk.presentation.stockdetails;

import android.databinding.Bindable;
import android.support.annotation.NonNull;

import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.sam_chordas.android.stockhawk.data.rest.QuoteTime;
import com.sam_chordas.android.stockhawk.presentation.common.ViewModel;

import rx.Observable;

/**
 * Created by fabio on 08.03.16.
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

    void onDataLoaded(@NonNull Observable<QuoteTime> data);

    void onDataLoadFailed();

    interface ViewListener extends ViewModel.ViewListener {

        void setChartColors(@NonNull LineDataSet lineDataSet);
    }
}
