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

package com.sam_chordas.android.stockhawk.data.rest;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Represents a value in time of a stock quote.
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
