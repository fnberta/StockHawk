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

package com.sam_chordas.android.stockhawk.domain.repositories;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.sam_chordas.android.stockhawk.data.rest.QuoteTime;

import rx.Observable;
import rx.Single;

/**
 * Handles the saving, deleting, updating and querying of saved stocks.
 */
public interface StockRepository {

    /**
     * Synchronously updates the locally stored stock values with fresh data from the internet.
     *
     * @return whether the update was successful
     */
    boolean updateStocks();

    /**
     * Asynchronously saves a stock symbol to the local data store.
     *
     * @param stockSymbol the stockSymbol to save
     * @return a {@link Single} emitting the result
     */
    Single<Uri> saveSymbol(@NonNull String stockSymbol);

    /**
     * Asynchronously deletes a stock symbol from the local data store.
     *
     * @param rowId the row id of the symbol to delete
     * @return a {@link Single} emitting the result
     */
    Single<Boolean> deleteStock(long rowId);

    /**
     * Asynchronously fetches a stock's value over the last 6 months.
     *
     * @param stockSymbol the the stock symbol to get the value for
     * @return a {@link Observable} emitting the results
     */
    Observable<QuoteTime> getStockDataOverTime(@NonNull String stockSymbol);

    /**
     * Synchronously queries for the locally saved stocks and returns them in a cursor.
     *
     * @return the locally saved stock symbols
     */
    Cursor getStocks();

    /**
     * Returns whether to show the stock value changes in percentages or in absolute values.
     *
     * @return whether to show the stock value changes in percentages or in absolute values
     */
    boolean showPercentages();

    /**
     * Toggles whether to show the stock value changes in percentages or in absolute values.
     */
    void toggleShowPercentages();

    /**
     * Returns whether to load the default stock symbols if there are no saved symbols.
     *
     * @return whether to load the default stock symbols if there are no saved symbols
     */
    boolean isLoadDefaultSymbolsEnabled();

    /**
     * Returns the period after which a sync should be performed.
     *
     * @return the period after which a sync should be performed
     */
    long getSyncPeriod();
}
