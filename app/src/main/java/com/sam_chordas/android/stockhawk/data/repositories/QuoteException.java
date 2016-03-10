package com.sam_chordas.android.stockhawk.data.repositories;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by fabio on 08.03.16.
 */
public class QuoteException extends RuntimeException {

    private int mCode;

    public QuoteException(@Code int code) {
        mCode = code;
    }

    public QuoteException(@Code int code, @NonNull String message) {
        super(message);
        mCode = code;
    }

    public int getCode() {
        return mCode;
    }

    @IntDef({Code.ALREADY_SAVED, Code.SYMBOL_NOT_FOUND})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Code {
        int ALREADY_SAVED = 1;
        int SYMBOL_NOT_FOUND = 2;
    }
}
