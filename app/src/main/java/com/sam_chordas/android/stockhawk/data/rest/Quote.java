package com.sam_chordas.android.stockhawk.data.rest;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.sam_chordas.android.stockhawk.data.provider.QuoteColumns;

import java.util.Locale;

public class Quote {

    @SerializedName("symbol")
    private String mSymbol;
    @SerializedName("Bid")
    private double mBid;
    @SerializedName("Change")
    private double mChange;
    @SerializedName("ChangeinPercent")
    private String mChangeInPercent;

    public String getSymbol() {
        return mSymbol;
    }

    public void setSymbol(@NonNull String symbol) {
        mSymbol = symbol;
    }

    public double getBid() {
        return mBid;
    }

    public void setBid(double bid) {
        mBid = bid;
    }

    public double getChange() {
        return mChange;
    }

    public void setChange(double change) {
        mChange = change;
    }

    public String getChangeInPercent() {
        return mChangeInPercent;
    }

    public void setChangeInPercent(@NonNull String changeInPercent) {
        mChangeInPercent = changeInPercent;
    }

    public ContentValues getContentValuesEntry() {
        final ContentValues cv = new ContentValues();
        cv.put(QuoteColumns.SYMBOL, mSymbol);
        cv.put(QuoteColumns.BID_PRICE, mBid);
        cv.put(QuoteColumns.CHANGE, mChange);
        cv.put(QuoteColumns.PERCENT_CHANGE, getChangeInPercentTruncated());
        return cv;
    }

    private String getChangeInPercentTruncated() {
        String truncated = mChangeInPercent;
        if (TextUtils.isEmpty(truncated)) {
            return "0.00%";
        }

        final String weight = truncated.substring(0, 1);
        final String ampersand = truncated.substring(truncated.length() - 1, truncated.length());
        truncated = truncated.substring(0, truncated.length() - 1);
        truncated = truncated.substring(1, truncated.length());
        final double round = (double) Math.round(Double.parseDouble(truncated) * 100) / 100;
        truncated = String.format(Locale.getDefault(), "%.2f", round);
        final StringBuilder stringBuilder = new StringBuilder(truncated);
        stringBuilder.insert(0, weight);
        stringBuilder.append(ampersand);
        return stringBuilder.toString();
    }
}
