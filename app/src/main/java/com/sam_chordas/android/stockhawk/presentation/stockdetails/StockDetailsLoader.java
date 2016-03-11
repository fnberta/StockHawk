package com.sam_chordas.android.stockhawk.presentation.stockdetails;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sam_chordas.android.stockhawk.data.rest.QuoteTime;
import com.sam_chordas.android.stockhawk.domain.repositories.StockRepository;
import com.sam_chordas.android.stockhawk.presentation.common.BaseRxLoader;

import rx.Observable;

/**
 * Loads the stock's value over the last 6 months from the internet.
 */
public class StockDetailsLoader extends BaseRxLoader<QuoteTime> {

    private final StockRepository mStockRepo;
    private final String mStockSymbol;

    public StockDetailsLoader(@NonNull Application context, @NonNull StockRepository stockRepo,
                              @NonNull String stockSymbol) {
        super(context);

        mStockSymbol = stockSymbol;
        mStockRepo = stockRepo;
    }

    @Nullable
    @Override
    protected Observable<QuoteTime> getObservable() {
        return mStockRepo.getStockDataOverTime(mStockSymbol);
    }
}
