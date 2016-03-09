package com.sam_chordas.android.stockhawk.data.repositories;

import android.app.Application;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.sam_chordas.android.stockhawk.data.provider.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.provider.QuoteProvider;
import com.sam_chordas.android.stockhawk.data.rest.Quote;
import com.sam_chordas.android.stockhawk.data.rest.QuoteTime;
import com.sam_chordas.android.stockhawk.data.rest.YahooQueryDetailsResult;
import com.sam_chordas.android.stockhawk.data.rest.YahooFinance;
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
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by fabio on 07.03.16.
 */
public class StockRepositoryImpl implements StockRepository {

    private Context mAppContext;
    private YahooFinance mYahooFinance;

    public StockRepositoryImpl(@NonNull Application appContext, @NonNull YahooFinance yahooFinance) {
        mAppContext = appContext;
        mYahooFinance = yahooFinance;
    }

    @Override
    public boolean updateStocks() {
        final Cursor cursor = mAppContext.getContentResolver()
                .query(QuoteProvider.Quotes.CONTENT_URI,
                        new String[]{QuoteColumns.SYMBOL},
                        null,
                        null,
                        null
                );

        if (cursor != null && cursor.moveToFirst()) {
            final StringBuilder selection = getSelection(cursor);
            cursor.close();
            return updateExisting(selection.toString());
        }

        return insertInitial();
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
        final ContentResolver contentResolver = mAppContext.getApplicationContext().getContentResolver();
        try {
            contentResolver.applyBatch(QuoteProvider.CONTENT_AUTHORITY, ops);
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
        final String selection = ("select symbol, Bid, Change, ChangeinPercent from yahoo.finance.quotes where symbol in " +
                "(\"YHOO\",\"AAPL\",\"GOOG\",\"MSFT\")");
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
        final ContentResolver contentResolver = mAppContext.getApplicationContext().getContentResolver();
        try {
            contentResolver.applyBatch(QuoteProvider.CONTENT_AUTHORITY, ops);
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

    @Override
    public Single<Uri> saveSymbol(@NonNull final String stockSymbol) {
        return Single.just(stockSymbol)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, Cursor>() {
                    @Override
                    public Cursor call(String stockSymbol) {
                        return mAppContext.getContentResolver().query(
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
                            return Single.error(new AlreadySavedException());
                        }

                        final String selection = ("select symbol, Bid, Change, ChangeinPercent from yahoo.finance.quotes where symbol in " +
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
                .map(new Func1<Quote, Uri>() {
                    @Override
                    public Uri call(Quote quote) {
                        final ContentResolver contentResolver = mAppContext.getApplicationContext().getContentResolver();
                        return contentResolver.insert(QuoteProvider.Quotes.CONTENT_URI, quote.getContentValuesEntry());
                    }
                });
    }

    @Override
    public Single<Integer> deleteStock(final long rowId) {
        return Single.just(rowId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long rowId) {
                        return mAppContext.getContentResolver().delete(
                                QuoteProvider.Quotes.withId(rowId),
                                null,
                                null
                        );
                    }
                });
    }

    @Override
    public Observable<QuoteTime> getStockDataOverTime(@NonNull String stockSymbol) {
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        final Calendar cal = Calendar.getInstance();
        final Date today = cal.getTime();
        cal.add(Calendar.MONTH, -6);
        final Date sixMonthBack = cal.getTime();

        final String selection = ("select Symbol, Date, Adj_Close from yahoo.finance.historicaldata where symbol in " +
                "(\"" + stockSymbol + "\") and startDate = \"" + formatter.format(sixMonthBack) +
                "\" and endDate = \"" + formatter.format(today) + "\"");
        return mYahooFinance.getQuoteDetails(selection)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapObservable(new Func1<YahooQueryDetailsResult, Observable<? extends QuoteTime>>() {
                    @Override
                    public Observable<? extends QuoteTime> call(YahooQueryDetailsResult yahooQueryDetailsResult) {
                        return Observable.from(yahooQueryDetailsResult.getQuery().getResults().getQuoteTimes());
                    }
                });
    }
}
