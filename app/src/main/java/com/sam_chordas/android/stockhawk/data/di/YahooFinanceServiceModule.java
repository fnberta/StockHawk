/*
 * Copyright (c) 2016 Fabio Berta
 */

package com.sam_chordas.android.stockhawk.data.di;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.sam_chordas.android.stockhawk.data.rest.Quote;
import com.sam_chordas.android.stockhawk.data.rest.YahooFinance;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Defines how the application wide rest services are instantiated.
 */
@Module
public class YahooFinanceServiceModule {

    private static final String BASE_URL_YAHOO = "https://query.yahooapis.com/v1/public/";

    @Provides
    @Singleton
    Gson providesGson() {
        return new GsonBuilder()
                .registerTypeAdapter(new TypeToken<List<Quote>>() {

                }.getType(), new JsonDeserializer<List<Quote>>() {
                    @Override
                    public List<Quote> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        final List<Quote> quotes = new ArrayList<>();

                        if (json.isJsonArray()) {
                            for (JsonElement e : json.getAsJsonArray()) {
                                quotes.add((Quote) context.deserialize(e, Quote.class));
                            }
                        } else if (json.isJsonObject()) {
                            quotes.add((Quote) context.deserialize(json, Quote.class));
                        } else {
                            throw new RuntimeException("Unexpected JSON type: " + json.getClass());
                        }

                        return quotes;
                    }
                })
                .setDateFormat("yyyy-MM-dd")
                .create();
    }

    @Provides
    @Singleton
    YahooFinance providesYahooFinance(@NonNull Gson gson) {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_YAHOO)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(YahooFinance.class);
    }
}
