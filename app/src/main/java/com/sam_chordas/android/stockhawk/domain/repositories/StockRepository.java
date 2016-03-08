package com.sam_chordas.android.stockhawk.domain.repositories;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;

import com.sam_chordas.android.stockhawk.domain.models.Stock;

import rx.Completable;
import rx.Single;

/**
 * Created by fabio on 07.03.16.
 */
public interface StockRepository {

    CursorLoader getMyStocksLoader();

    boolean updateStocks();

    Single<Uri> saveSymbol(@NonNull String symbol);

    Single<Integer> deleteStock(long rowId);
}
