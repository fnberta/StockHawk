package com.sam_chordas.android.stockhawk.data.provider;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;

/**
 * Created by sam_chordas on 10/5/15.
 */
public class QuoteColumns {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    @Unique
    public static final String SYMBOL = "symbol";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String PERCENT_CHANGE = "percent_change";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String CHANGE = "change";

    @DataType(DataType.Type.TEXT)
    @NotNull
    public static final String BID_PRICE = "bid_price";
}