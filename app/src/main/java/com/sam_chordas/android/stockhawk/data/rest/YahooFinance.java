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

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Single;

/**
 * Defines the network calls to the Yahoo Finance API.
 */
public interface YahooFinance {

    @GET("yql?format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=")
    Single<YahooQueryResult> getQuote(@Query("q") String selection);

    @GET("yql?format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=")
    Call<YahooQueryResult> getQuotes(@Query("q") String selection);

    @GET("yql?format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=")
    Single<YahooQueryDetailsResult> getQuoteDetails(@Query("q") String selection);
}
