package com.sam_chordas.android.stockhawk.data.rest;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by fabio on 08.03.16.
 */
public class QuoteTime {

    @SerializedName("Symbol")
    private String mSymbol;
    @SerializedName("Date")
    private Date mDate;
    @SerializedName("Adj_Close")
    private double mAdjClose;

    public String getSymbol() {
        return mSymbol;
    }

    public void setSymbol(String symbol) {
        mSymbol = symbol;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public double getAdjClose() {
        return mAdjClose;
    }

    public void setAdjClose(double adjClose) {
        mAdjClose = adjClose;
    }
}
