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
