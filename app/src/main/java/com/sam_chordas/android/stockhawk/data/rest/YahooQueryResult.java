package com.sam_chordas.android.stockhawk.data.rest;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the results of a query to the Yahoo Finance API for stock quotes.
 */
public class YahooQueryResult {

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
        private List<Quote> mQuotes = new ArrayList<>();

        public List<Quote> getQuotes() {
            return mQuotes;
        }

        public void setQuotes(List<Quote> quotes) {
            mQuotes = quotes;
        }
    }
}
