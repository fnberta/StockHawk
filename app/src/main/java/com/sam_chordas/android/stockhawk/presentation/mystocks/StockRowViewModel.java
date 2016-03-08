package com.sam_chordas.android.stockhawk.presentation.mystocks;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import com.sam_chordas.android.stockhawk.utils.Utils;

import java.text.NumberFormat;

/**
 * Created by fabio on 07.03.16.
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
