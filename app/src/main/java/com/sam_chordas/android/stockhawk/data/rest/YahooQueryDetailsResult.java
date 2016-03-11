package com.sam_chordas.android.stockhawk.data.rest;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the results of a query to the Yahoo Finance API for stock values over time.
 */
public class YahooQueryDetailsResult {

    @SerializedName("query")
    private Query mQuery;

    public Query getQuery() {
        return mQuery;
    }

    public void setQuery(Query query) {
        mQuery = query;
    }

    public static class Query {

        @SerializedName("count")
        private int mCount;
        @SerializedName("results")
        private Results mResults;

        public int getCount() {
            return mCount;
        }

        public void setCount(int count) {
            mCount = count;
        }

        public Results getResults() {
            return mResults;
        }

        public void setResults(Results results) {
            mResults = results;
        }
    }

    public static class Results {

        @SerializedName("quote")
        private List<QuoteTime> mQuoteTimes = new ArrayList<>();

        public List<QuoteTime> getQuoteTimes() {
            return mQuoteTimes;
        }

        public void setQuoteTimes(List<QuoteTime> quoteTimes) {
            mQuoteTimes = quoteTimes;
        }
    }
}
