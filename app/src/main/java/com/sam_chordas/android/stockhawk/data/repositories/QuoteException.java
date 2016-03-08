package com.sam_chordas.android.stockhawk.data.repositories;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by fabio on 08.03.16.
 */
public class QuoteException extends RuntimeException {

    private int mCode;

    public QuoteException(@Code int code) {
        super();
        mCode = code;
    }

    public int getCode() {
        return mCode;
    }

    @IntDef({Code.GENERIC, Code.ALREADY_SAVED, Code.SYMBOL_NOT_FOUND})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Code {
        int GENERIC = 0;
        int ALREADY_SAVED = 1;
        int SYMBOL_NOT_FOUND = 2;
    }
}
