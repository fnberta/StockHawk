package com.sam_chordas.android.stockhawk.presentation.stockdetails;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sam_chordas.android.stockhawk.data.rest.QuoteTime;
import com.sam_chordas.android.stockhawk.domain.repositories.StockRepository;
import com.sam_chordas.android.stockhawk.presentation.common.BaseRxLoader;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by fabio on 08.03.16.
 */
public class StockDetailsLoader extends BaseRxLoader<QuoteTime> {

    private StockRepository mStockRepo;
    private String mStockSymbol;

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
