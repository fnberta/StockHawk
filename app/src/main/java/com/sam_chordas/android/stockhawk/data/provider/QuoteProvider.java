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

package com.sam_chordas.android.stockhawk.data.provider;

import android.content.ContentResolver;
import android.net.Uri;

import com.sam_chordas.android.stockhawk.BuildConfig;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Provides a content provider to access the local sqlite database.
 */
@ContentProvider(authority = QuoteProvider.CONTENT_AUTHORITY, database = QuoteDatabase.class)
public class QuoteProvider {

    public static final String CONTENT_AUTHORITY = BuildConfig.APPLICATION_ID;
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String CONTENT_TYPE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
            CONTENT_AUTHORITY + "/" + QuoteDatabase.QUOTES;
    public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
            CONTENT_AUTHORITY + "/" + QuoteDatabase.QUOTES;

    private static Uri buildUri(String... paths) {
        final Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = QuoteDatabase.QUOTES)
    public static class Quotes {
        @ContentUri(
                path = QuoteDatabase.QUOTES,
                type = CONTENT_TYPE_DIR,
                defaultSort = QuoteColumns.SYMBOL + " ASC"
        )
        public static final Uri CONTENT_URI = buildUri(QuoteDatabase.QUOTES);

        @InexactContentUri(
                name = "QUOTE_ID",
                path = QuoteDatabase.QUOTES + "/#",
                type = CONTENT_TYPE_ITEM,
                whereColumn = QuoteColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(QuoteDatabase.QUOTES, String.valueOf(id));
        }

        @InexactContentUri(
                name = "QUOTE_SYMBOL",
                path = QuoteDatabase.QUOTES + "/" + QuoteColumns.SYMBOL + "/*",
                type = CONTENT_TYPE_ITEM,
                whereColumn = QuoteColumns.SYMBOL,
                pathSegment = 2
        )
        public static Uri withSymbol(String symbol) {
            return buildUri(QuoteDatabase.QUOTES, QuoteColumns.SYMBOL, symbol);
        }
    }
}
