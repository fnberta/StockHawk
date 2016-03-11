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

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import com.sam_chordas.android.stockhawk.utils.Utils;

import java.text.NumberFormat;

/**
 * Provides a view model for one row in the my stocks list.
 */
public class StockRowViewModel extends BaseObservable {

    private String mStockSymbol;
    private String mStockBidPrice;
    private String mStockChange;
    private String mStockChangePercent;
    private boolean mStockUp;
    private boolean mShowPercentage;

    public StockRowViewModel(@NonNull String stockSymbol, double stockBidPrice, double stockChange,
                             @NonNull String stockChangePercent, boolean showPercentage) {
        setInfo(stockSymbol, stockBidPrice, stockChange, stockChangePercent, showPercentage);
    }

    public void setInfo(@NonNull String stockSymbol, double stockBidPrice, double stockChange,
                        @NonNull String stockChangePercent, boolean showPercentage) {
        final NumberFormat numberFormat = Utils.getValueFormatter();
        mStockSymbol = stockSymbol;
        mStockBidPrice = numberFormat.format(stockBidPrice);
        mStockChange = numberFormat.format(stockChange);
        mStockChangePercent = stockChangePercent;
        mShowPercentage = showPercentage;
        mStockUp = !Utils.isNegative(stockChange);
    }

    @Bindable
    public String getStockSymbol() {
        return mStockSymbol;
    }

    @Bindable
    public String getStockBidPrice() {
        return mStockBidPrice;
    }

    @Bindable
    public String getStockChange() {
        return mShowPercentage ? mStockChangePercent : mStockChange;
    }

    @Bindable
    public boolean isStockUp() {
        return mStockUp;
    }
}
