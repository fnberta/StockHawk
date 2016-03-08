package com.sam_chordas.android.stockhawk.data.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.sam_chordas.android.stockhawk.StockHawk;
import com.sam_chordas.android.stockhawk.data.di.RepositoriesModule;
import com.sam_chordas.android.stockhawk.data.services.di.DaggerUpdateStocksServiceComponent;
import com.sam_chordas.android.stockhawk.domain.repositories.StockRepository;

import javax.inject.Inject;

/**
 * Created by sam_chordas on 10/1/15.
 */
public class UpdateStocksIntentService extends IntentService {

    @Inject
    StockRepository mStockRepo;

    public UpdateStocksIntentService() {
        super(UpdateStocksIntentService.class.getName());
    }

    public static void start(@NonNull Context context) {
        final Intent serviceIntent = new Intent(context, UpdateStocksIntentService.class);
        context.startService(serviceIntent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        injectDependencies();

        mStockRepo.updateStocks();
    }

    private void injectDependencies() {
        DaggerUpdateStocksServiceComponent.builder()
                .applicationComponent(StockHawk.getAppComponent(this))
                .repositoriesModule(new RepositoriesModule())
                .build()
                .inject(this);
    }
}