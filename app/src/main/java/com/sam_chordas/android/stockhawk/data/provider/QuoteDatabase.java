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

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Defines the table name and version of the local sqlite database.
 */
@Database(version = QuoteDatabase.VERSION)
public class QuoteDatabase {

    public static final int VERSION = 7;
    @Table(QuoteColumns.class)
    public static final String QUOTES = "quotes";

    private QuoteDatabase() {
    }
}
