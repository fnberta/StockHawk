/*
 * Copyright (c) 2016 Fabio Berta.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sam_chordas.android.stockhawk.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Provides static utility methods.
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
        return activeNetwork != null && activeNetwork.isConnected();
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
     * Returns a date formatter that formats date to the specified format.
     *
     * @param shortFormat whether to format in short format
     * @return a properly configured {@link DateFormat} instance
     */
    public static DateFormat getDateFormatter(boolean shortFormat) {
        return DateFormat.getDateInstance(shortFormat ? DateFormat.SHORT : DateFormat.LONG,
                Locale.getDefault());
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
}
