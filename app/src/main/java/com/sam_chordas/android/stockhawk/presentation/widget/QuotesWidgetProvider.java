package com.sam_chordas.android.stockhawk.presentation.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.BuildConfig;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.services.QuotesWidgetService;
import com.sam_chordas.android.stockhawk.presentation.mystocks.MyStocksActivity;
import com.sam_chordas.android.stockhawk.presentation.stockdetails.StockDetailsActivity;

/**
 * Provides a provider for the my stocks widget.
 */
public class QuotesWidgetProvider extends AppWidgetProvider {

    public static final String EXTRA_ADD_QUOTE = BuildConfig.APPLICATION_ID + ".intents.EXTRA_ADD_QUOTE";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (final int appWidgetId : appWidgetIds) {
            final RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_my_stocks);

            // Create an Intent to launch MyStocksActivity
            final Intent headerIntent = new Intent(context, MyStocksActivity.class);
            final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, headerIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setOnClickPendingIntent(R.id.fl_widget_title_bar, pendingIntent);

            // Create an Intent to launch MyStocksActivity with add dialog
            final Intent headerAddIntent = new Intent(context, MyStocksActivity.class);
            headerAddIntent.putExtra(EXTRA_ADD_QUOTE, true);
            final PendingIntent pendingAddIntent = PendingIntent.getActivity(context, 1, headerAddIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setOnClickPendingIntent(R.id.iv_widget_title_add, pendingAddIntent);

            // Set remote adapter and empty view
            final Intent intent = new Intent(context, QuotesWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            rv.setRemoteAdapter(R.id.lv_widget, intent);
            rv.setEmptyView(R.id.lv_widget, R.id.tv_widget_empty_view);

            // Set click template
            final Intent detailsIntent = new Intent(context, StockDetailsActivity.class);
            final PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(detailsIntent)
                    .getPendingIntent(2, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.lv_widget, clickPendingIntentTemplate);

            // Update
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
    }
}
