package com.sam_chordas.android.stockhawk.domain.models;

import android.support.annotation.NonNull;

/**
 * Created by fabio on 07.03.16.
 */
public class Stock {

    private String mSymbol;

    public Stock(@NonNull String symbol) {
        mSymbol = symbol;
    }

    public String getSymbol() {
        return mSymbol;
    }

    public void setSymbol(@NonNull String symbol) {
        mSymbol = symbol;
    }
}
