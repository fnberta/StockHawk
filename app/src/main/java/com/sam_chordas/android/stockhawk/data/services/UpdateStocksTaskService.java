package com.sam_chordas.android.stockhawk.data.services;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.sam_chordas.android.stockhawk.StockHawk;
import com.sam_chordas.android.stockhawk.data.services.di.DaggerUpdateStocksServiceComponent;
import com.sam_chordas.android.stockhawk.di.RepositoriesModule;
import com.sam_chordas.android.stockhawk.domain.repositories.StockRepository;

import javax.inject.Inject;

/**
 * Created by sam_chordas on 9/30/15.
 * This GCMTask service pulls and updates stocks once every hour after the app has been opened.
 * This is so Widget data stays up to date.
 */
public class UpdateStocksTaskService extends GcmTaskService {

    @Inject
    StockRepository mStockRepo;

    @Override
    public int onRunTask(TaskParams params) {
        injectDependencies();
        return mStockRepo.updateStocks()
                ? GcmNetworkManager.RESULT_SUCCESS
                : GcmNetworkManager.RESULT_RESCHEDULE;
    }

    private void injectDependencies() {
        DaggerUpdateStocksServiceComponent.builder()
                .applicationComponent(StockHawk.getAppComponent(this))
                .repositoriesModule(new RepositoriesModule())
                .build()
                .inject(this);
    }
}
