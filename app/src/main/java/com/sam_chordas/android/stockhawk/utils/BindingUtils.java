/*
 * Copyright (c) 2016 Fabio Berta
 */

package com.sam_chordas.android.stockhawk.utils;

import android.databinding.BindingAdapter;
import android.support.v4.widget.SwipeRefreshLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;

/**
 * Contains generic binding adapters.
 */
public class BindingUtils {

    private BindingUtils() {
        // class cannot be instantiated
    }

    @BindingAdapter({"chartData"})
    public static void setChartData(LineChart view, LineData data) {
        if (data != null) {
            view.setData(data);
            view.animateX(1000);
        }
    }
}
