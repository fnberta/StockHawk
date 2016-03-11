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

package com.sam_chordas.android.stockhawk.di;

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
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Defines how the Yahoo Finance API REST service is instantiated.
 */
@Module
public class YahooFinanceServiceModule {

    private static final String BASE_URL_YAHOO = "https://query.yahooapis.com/v1/public/";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

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
                .setDateFormat(DATE_FORMAT)
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
