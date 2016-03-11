package com.sam_chordas.android.stockhawk.data.services;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.StockHawk;
import com.sam_chordas.android.stockhawk.data.services.di.DaggerQuotesWidgetComponent;

/**
 * Provides a service that instantiates a view factory to fill the my stocks widget with content.
 */
public class QuotesWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return DaggerQuotesWidgetComponent.builder()
                .applicationComponent(StockHawk.getAppComponent(this))
                .build()
                .getQuotesViewsFactory();
    }
}
