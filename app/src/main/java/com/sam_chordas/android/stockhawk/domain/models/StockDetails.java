package com.sam_chordas.android.stockhawk.domain.models;

import android.support.annotation.NonNull;

import com.sam_chordas.android.stockhawk.data.rest.QuoteTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents one stock symbol and its value over time.
 */
public class StockDetails {

    private String mSymbol;
    private List<QuoteTime> mQuoteTimes;

    public StockDetails(@NonNull String symbol) {
        mSymbol = symbol;
        mQuoteTimes = new ArrayList<>();
    }

    public StockDetails(@NonNull String symbol, @NonNull List<QuoteTime> quoteTimes) {
        mSymbol = symbol;
        mQuoteTimes = quoteTimes;
    }

    public String getSymbol() {
        return mSymbol;
    }

    public void setSymbol(@NonNull String symbol) {
        mSymbol = symbol;
    }

    public List<QuoteTime> getQuoteTimes() {
        return mQuoteTimes;
    }

    public void setQuoteTimes(@NonNull List<QuoteTime> quoteTimes) {
        mQuoteTimes = quoteTimes;
    }

    public void addQuoteTime(@NonNull QuoteTime quoteTime) {
        mQuoteTimes.add(quoteTime);
    }
}
