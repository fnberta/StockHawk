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
