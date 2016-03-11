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
