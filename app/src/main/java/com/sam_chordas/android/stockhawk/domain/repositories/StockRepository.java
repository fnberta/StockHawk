package com.sam_chordas.android.stockhawk.domain.repositories;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;

import com.sam_chordas.android.stockhawk.data.rest.QuoteTime;

import rx.Observable;
import rx.Single;

/**
 * Created by fabio on 07.03.16.
 */
public interface StockRepository {

    boolean updateStocks();

    Single<Uri> saveSymbol(@NonNull String symbol);

    Single<Boolean> deleteStock(long rowId);

    Observable<QuoteTime> getStockDataOverTime(@NonNull String stockSymbol);

    Cursor getStocks();

    boolean showPercentages();

    void toggleShowPercentages();
}
