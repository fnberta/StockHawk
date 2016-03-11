package com.sam_chordas.android.stockhawk.presentation.stockdetails;

import android.databinding.Bindable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.sam_chordas.android.stockhawk.BR;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.rest.QuoteTime;
import com.sam_chordas.android.stockhawk.domain.models.StockDetails;
import com.sam_chordas.android.stockhawk.presentation.common.ViewModelBaseImpl;
import com.sam_chordas.android.stockhawk.utils.Utils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Provides an implementation of the {@link StockDetailsViewModel} interface.
 */
public class StockDetailsViewModelImpl extends ViewModelBaseImpl<StockDetailsViewModel.ViewListener>
        implements StockDetailsViewModel {

    private static final String STATE_LOADING = "STATE_LOADING";
    private final StockDetails mStockDetails;
    private LineData mChartData;
    private boolean mLoading;

    public StockDetailsViewModelImpl(@Nullable Bundle savedState,
                                     @NonNull StockDetailsViewModel.ViewListener view,
                                     @NonNull String stockSymbol) {
        super(savedState, view);

        mStockDetails = new StockDetails(stockSymbol);

        if (savedState != null) {
            mLoading = savedState.getBoolean(STATE_LOADING);
        } else {
            mLoading = true;
        }
    }

    @Override
    public void saveState(@NonNull Bundle outState) {
        super.saveState(outState);

        outState.putBoolean(STATE_LOADING, mLoading);
    }

    @Bindable
    @Override
    public boolean isLoading() {
        return mLoading;
    }

    @Override
    public void setLoading(boolean loading) {
        mLoading = loading;
        notifyPropertyChanged(BR.loading);
    }

    @Bindable
    @Override
    public boolean isEmpty() {
        return mChartData == null;
    }

    @Bindable
    @Override
    public LineData getChartData() {
        return mChartData;
    }

    @Override
    public void setChartData(@NonNull LineData chartData) {
        mChartData = chartData;
        notifyPropertyChanged(BR.chartData);
    }

    @Override
    public void onDataLoaded(@Nullable Observable<QuoteTime> data) {
        if (data == null) {
            setLoading(false);
            mView.showMessage(R.string.snackbar_error_stock_details);
            return;
        }

        getSubscriptions().add(data
                .subscribe(new Subscriber<QuoteTime>() {
                    @Override
                    public void onCompleted() {
                        prepareChartData();
                        setLoading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        setLoading(false);
                        mView.showMessage(R.string.snackbar_error_stock_details);
                    }

                    @Override
                    public void onNext(QuoteTime quoteTime) {
                        mStockDetails.addQuoteTime(quoteTime);
                    }
                })
        );
    }

    private void prepareChartData() {
        final DateFormat dateFormat = Utils.getDateFormatter(true);
        final List<QuoteTime> quoteTimes = mStockDetails.getQuoteTimes();
        final int size = quoteTimes.size();
        final List<String> xVals = new ArrayList<>(size);
        final List<Entry> entries = new ArrayList<>(size);

        for (int i = size - 1; i >= 0; i--) {
            final QuoteTime quoteTime = quoteTimes.get(i);
            entries.add(new Entry((float) quoteTime.getAdjClose(), i));
            xVals.add(dateFormat.format(quoteTime.getDate()));
        }

        final LineDataSet lineDataSet = new LineDataSet(entries, mStockDetails.getSymbol());
        mView.setChartColors(lineDataSet);
        final LineData data = new LineData(xVals, lineDataSet);
        setChartData(data);
    }
}
