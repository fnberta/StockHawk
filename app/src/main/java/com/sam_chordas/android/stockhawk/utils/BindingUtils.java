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

    @BindingAdapter({"colorScheme"})
    public static void setColorScheme(SwipeRefreshLayout view, int[] colorScheme) {
        view.setColorSchemeColors(colorScheme);
    }

    @BindingAdapter({"chartData"})
    public static void setChartData(LineChart view, LineData data) {
        if (data != null) {
            view.setData(data);
            view.animateX(500);
        }
    }
}
