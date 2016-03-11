package com.sam_chordas.android.stockhawk.presentation.widget;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.provider.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.provider.QuoteProvider;
import com.sam_chordas.android.stockhawk.domain.repositories.StockRepository;
import com.sam_chordas.android.stockhawk.utils.Utils;

import java.text.NumberFormat;

import javax.inject.Inject;

/**
 * Provides the data for the my stocks widget.
 */
public class QuotesWidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final NumberFormat mNumberFormat = Utils.getValueFormatter();
    private final Context mContext;
    private final StockRepository mStockRepo;
    private Cursor mCursor;
    private int mRowIdColumn;

    @Inject
    public QuotesWidgetViewsFactory(@NonNull Application application,
                                    @NonNull StockRepository stockRepo) {
        mContext = application;
        mStockRepo = stockRepo;
    }

    @Override
    public void onCreate() {
        // do nothing
    }

    @Override
    public void onDataSetChanged() {
        if (mCursor != null) {
            mCursor.close();
        }

        // This method is called by the app hosting the widget (e.g., the launcher)
        // However, our ContentProvider is not exported so it doesn't have access to the
        // data. Therefore we need to clear (and finally restore) the calling identity so
        // that calls use our process and permission
        final long identityToken = Binder.clearCallingIdentity();
        try {
            mCursor = mStockRepo.getStocks();
        } finally {
            Binder.restoreCallingIdentity(identityToken);
        }

        mRowIdColumn = mCursor != null ? mCursor.getColumnIndexOrThrow(BaseColumns._ID) : -1;
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
    }

    @Override
    public int getCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                mCursor == null || !mCursor.moveToPosition(position)) {
            return null;
        }

        final RemoteViews rv = new RemoteViews(mContext.getPackageName(),
                R.layout.item_widget_my_stocks);

        final String symbol = mCursor.getString(mCursor.getColumnIndexOrThrow(QuoteColumns.SYMBOL));
        final double bidPrice = mCursor.getDouble(mCursor.getColumnIndexOrThrow(QuoteColumns.BID_PRICE));
        final String changePercent = mCursor.getString(mCursor.getColumnIndexOrThrow(QuoteColumns.PERCENT_CHANGE));
        final double changeValue = mCursor.getDouble(mCursor.getColumnIndexOrThrow(QuoteColumns.CHANGE));
        final String change = mStockRepo.showPercentages() ? changePercent : mNumberFormat.format(changeValue);
        final int changeBackground = Utils.isNegative(changeValue)
                ? R.drawable.percent_change_pill_red
                : R.drawable.percent_change_pill_green;

        rv.setTextViewText(R.id.widget_stock_symbol, symbol);
        rv.setTextViewText(R.id.widget_stock_bid_price, mNumberFormat.format(bidPrice));
        rv.setTextViewText(R.id.widget_stock_change, change);
        rv.setInt(R.id.widget_stock_change, "setBackgroundResource", changeBackground);

        final Intent fillInIntent = new Intent();
        fillInIntent.setData(QuoteProvider.Quotes.withSymbol(symbol));
        rv.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        // use default loading view
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        if (mCursor != null) {
            return mCursor.moveToPosition(position) ? mCursor.getLong(mRowIdColumn) : ListView.NO_ID;
        } else {
            return ListView.NO_ID;
        }
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
