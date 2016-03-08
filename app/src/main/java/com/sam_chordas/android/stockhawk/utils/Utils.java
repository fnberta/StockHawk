package com.sam_chordas.android.stockhawk.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by sam_chordas on 10/8/15.
 */
public class Utils {

    /**
     * Returns whether there is an active network connection or not.
     * <p/>
     * Note that an active network connection does not guarantee that there is a connection to the
     * internet.
     *
     * @param context the context to use to get the {@link ConnectivityManager}
     * @return whether there is an active network connection or not
     */
    public static boolean isNetworkAvailable(@NonNull Context context) {
        final ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Returns a {@link NumberFormat} instance that formats the value with the default number of
     * decimal digits.
     *
     * @return a {@link NumberFormat} instance that formats an exchange rate
     */
    public static NumberFormat getValueFormatter() {
        final NumberFormat moneyFormatter = NumberFormat.getInstance();
        moneyFormatter.setMinimumFractionDigits(2);
        moneyFormatter.setMaximumFractionDigits(2);

        return moneyFormatter;
    }

    /**
     * Returns whether the number is negative.
     *
     * @param d the number to check
     * @return whether the number is negative
     */
    public static boolean isNegative(double d) {
        return Double.doubleToRawLongBits(d) < 0;
    }

    public static ArrayList quoteJsonToContentVals(String JSON) {
        return null;
    }
}
