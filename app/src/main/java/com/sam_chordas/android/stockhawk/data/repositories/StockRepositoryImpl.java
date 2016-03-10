package com.sam_chordas.android.stockhawk.data.repositories;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.provider.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.provider.QuoteProvider;
import com.sam_chordas.android.stockhawk.data.repositories.QuoteException.Code;
import com.sam_chordas.android.stockhawk.data.rest.Quote;
import com.sam_chordas.android.stockhawk.data.rest.QuoteTime;
import com.sam_chordas.android.stockhawk.data.rest.YahooFinance;
import com.sam_chordas.android.stockhawk.data.rest.YahooQueryDetailsResult;
import com.sam_chordas.android.stockhawk.data.rest.YahooQueryResult;
import com.sam_chordas.android.stockhawk.domain.repositories.StockRepository;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by fabio on 07.03.16.
 */
public class StockRepositoryImpl implements StockRepository {

    private static final String SHOW_PERCENTAGE = "SHOW_PERCENTAGE";
    private static final String INITIAL_SYMBOLS = "(\"YHOO\",\"AAPL\",\"GOOG\",\"MSFT\")";
    private ContentResolver mContentResolver;
    private AppWidgetManager mAppWidgetManager;
    private ComponentName mWidgetProviderComp;
    private SharedPreferences mSharedPrefs;
    private YahooFinance mYahooFinance;

    public StockRepositoryImpl(@NonNull ContentResolver contentResolver,
                               @NonNull AppWidgetManager appWidgetManager,
                               @NonNull ComponentName widgetProviderComp,
                               @NonNull SharedPreferences sharedPrefs,
                               @NonNull YahooFinance yahooFinance) {
        mContentResolver = contentResolver;
        mAppWidgetManager = appWidgetManager;
        mWidgetProviderComp = widgetProviderComp;
        mSharedPrefs = sharedPrefs;
        mYahooFinance = yahooFinance;
    }

    @Override
    public boolean updateStocks() {
        boolean successful;

        final Cursor cursor = mContentResolver.query(
                QuoteProvider.Quotes.CONTENT_URI,
                new String[]{QuoteColumns.SYMBOL},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            final StringBuilder selection = getSelection(cursor);
            cursor.close();
            successful = updateExisting(selection.toString());
        } else {
            successful = insertInitial();
        }

        if (successful) {
            updateWidget();
        }

        return successful;
    }

    @NonNull
    private StringBuilder getSelection(@NonNull Cursor cursor) {
        final StringBuilder selection = new StringBuilder();
        selection.append("select symbol, Bid, Change, ChangeinPercent from yahoo.finance.quotes where symbol in (");
        do {
            selection
                    .append("\"")
                    .append(cursor.getString(cursor.getColumnIndex(QuoteColumns.SYMBOL)))
                    .append("\",");
        } while (cursor.moveToNext());

        // replace last , with )
        final int length = selection.length();
        selection.replace(length - 1, length, ")");

        return selection;
    }

    private boolean updateExisting(@NonNull String symbols) {
        final Call<YahooQueryResult> request = mYahooFinance.getQuotes(symbols);
        final Response<YahooQueryResult> response;
        try {
            response = request.execute();
        } catch (IOException e) {
            return false;
        }
        final YahooQueryResult yahooQueryResult = response.body();
        if (yahooQueryResult == null) {
            return false;
        }

        final ArrayList<ContentProviderOperation> ops = getOperations(yahooQueryResult, true);
        try {
            mContentResolver.applyBatch(QuoteProvider.CONTENT_AUTHORITY, ops);
        } catch (Throwable t) {
            return false;
        }

        return true;
    }

    @NonNull
    private ArrayList<ContentProviderOperation> getOperations(@NonNull YahooQueryResult yahooQueryResult,
                                                              boolean update) {
        final ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        final YahooQueryResult.Query query = yahooQueryResult.getQuery();
        final YahooQueryResult.Results results = query.getResults();
        final List<Quote> quotes = results.getQuotes();
        if (!quotes.isEmpty()) {
            for (Quote quote : quotes) {
                if (update) {
                    ops.add(buildUpdateBatchOperation(quote));
                } else {
                    ops.add(buildInsertBatchOperation(quote));
                }
            }
        }

        return ops;
    }

    private ContentProviderOperation buildUpdateBatchOperation(@NonNull Quote quote) {
        return ContentProviderOperation.newUpdate(QuoteProvider.Quotes.withSymbol(quote.getSymbol()))
                .withValues(quote.getContentValuesEntry())
                .build();
    }


    private boolean insertInitial() {
        final String selection = ("select symbol, Bid, Change, ChangeinPercent " +
                "from yahoo.finance.quotes where symbol in " + INITIAL_SYMBOLS);
        final Call<YahooQueryResult> request = mYahooFinance.getQuotes(selection);
        final Response<YahooQueryResult> response;
        try {
            response = request.execute();
        } catch (IOException e) {
            return false;
        }
        final YahooQueryResult yahooQueryResult = response.body();
        if (yahooQueryResult == null) {
            return false;
        }

        final ArrayList<ContentProviderOperation> ops = getOperations(yahooQueryResult, false);
        try {
            mContentResolver.applyBatch(QuoteProvider.CONTENT_AUTHORITY, ops);
        } catch (Throwable t) {
            return false;
        }

        return true;
    }

    @NonNull
    private ContentProviderOperation buildInsertBatchOperation(@NonNull Quote quote) {
        return ContentProviderOperation.newInsert(QuoteProvider.Quotes.CONTENT_URI)
                .withValues(quote.getContentValuesEntry())
                .build();
    }

    private void updateWidget() {
        final int[] appWidgetIds = mAppWidgetManager.getAppWidgetIds(mWidgetProviderComp);
        mAppWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_widget);
    }

    @Override
    public Single<Uri> saveSymbol(@NonNull final String stockSymbol) {
        return Single.just(stockSymbol)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, Cursor>() {
                    @Override
                    public Cursor call(String s) {
                        return mContentResolver.query(
                                QuoteProvider.Quotes.CONTENT_URI,
                                new String[]{QuoteColumns.SYMBOL},
                                QuoteColumns.SYMBOL + "= ?",
                                new String[]{stockSymbol},
                                null
                        );
                    }
                })
                .map(new Func1<Cursor, Boolean>() {
                    @Override
                    public Boolean call(Cursor cursor) {
                        return cursor.moveToFirst();
                    }
                })
                .flatMap(new Func1<Boolean, Single<YahooQueryResult>>() {
                    @Override
                    public Single<YahooQueryResult> call(Boolean exists) {
                        if (exists) {
                            return Single.error(new QuoteException(Code.ALREADY_SAVED));
                        }

                        final String selection = ("select symbol, Bid, Change, ChangeinPercent " +
                                "from yahoo.finance.quotes where symbol in " +
                                "(\"" + stockSymbol + "\")");
                        return mYahooFinance.getQuote(selection);
                    }
                })
                .map(new Func1<YahooQueryResult, Quote>() {
                    @Override
                    public Quote call(YahooQueryResult yahooQueryResult) {
                        final YahooQueryResult.Query query = yahooQueryResult.getQuery();
                        final YahooQueryResult.Results results = query.getResults();
                        final List<Quote> quotes = results.getQuotes();
                        return quotes.get(0);
                    }
                })
                .flatMap(new Func1<Quote, Single<? extends Uri>>() {
                    @Override
                    public Single<? extends Uri> call(Quote quote) {
                        if (quote.getBid() == 0) {
                            return Single.error(new QuoteException(Code.SYMBOL_NOT_FOUND));
                        }

                        return Single.just(mContentResolver.insert(
                                QuoteProvider.Quotes.CONTENT_URI,
                                quote.getContentValuesEntry()));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(new Action1<Uri>() {
                    @Override
                    public void call(Uri uri) {
                        updateWidget();
                    }
                });
    }

    @Override
    public Single<Boolean> deleteStock(final long rowId) {
        return Single.just(rowId)
                .subscribeOn(Schedulers.io())
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long rowId) {
                        return mContentResolver.delete(
                                QuoteProvider.Quotes.withId(rowId),
                                null,
                                null
                        );
                    }
                })
                .map(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return getStocksCount() == 0;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean isEmpty) {
                        updateWidget();
                    }
                });
    }

    private int getStocksCount() {
        final Cursor cursor = mContentResolver.query(
                QuoteProvider.Quotes.CONTENT_URI,
                new String[]{},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            final int count = cursor.getCount();
            cursor.close();
            return count;
        }

        return 0;
    }

    @Override
    public Observable<QuoteTime> getStockDataOverTime(@NonNull String stockSymbol) {
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        final Calendar cal = Calendar.getInstance();
        final Date today = cal.getTime();
        cal.add(Calendar.MONTH, -6);
        final Date sixMonthBack = cal.getTime();

        final String selection = ("select Symbol, Date, Adj_Close " +
                "from yahoo.finance.historicaldata where symbol in " +
                "(\"" + stockSymbol + "\") and startDate = \"" + formatter.format(sixMonthBack) +
                "\" and endDate = \"" + formatter.format(today) + "\"");
        return mYahooFinance.getQuoteDetails(selection)
                .subscribeOn(Schedulers.io())
                .flatMapObservable(new Func1<YahooQueryDetailsResult, Observable<? extends QuoteTime>>() {
                    @Override
                    public Observable<? extends QuoteTime> call(YahooQueryDetailsResult yahooQueryDetailsResult) {
                        final YahooQueryDetailsResult.Query query = yahooQueryDetailsResult.getQuery();
                        final YahooQueryDetailsResult.Results results = query.getResults();
                        return Observable.from(results.getQuoteTimes());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Cursor getStocks() {
        return mContentResolver.query(
                QuoteProvider.Quotes.CONTENT_URI,
                new String[]{
                        QuoteColumns._ID,
                        QuoteColumns.SYMBOL,
                        QuoteColumns.BID_PRICE,
                        QuoteColumns.PERCENT_CHANGE,
                        QuoteColumns.CHANGE},
                null,
                null,
                null
        );
    }

    @Override
    public boolean showPercentages() {
        return mSharedPrefs.getBoolean(SHOW_PERCENTAGE, true);
    }

    @Override
    public void toggleShowPercentages() {
        final boolean showPercentages = showPercentages();
        mSharedPrefs.edit()
                .putBoolean(SHOW_PERCENTAGE, !showPercentages)
                .apply();
        updateWidget();
    }
}
