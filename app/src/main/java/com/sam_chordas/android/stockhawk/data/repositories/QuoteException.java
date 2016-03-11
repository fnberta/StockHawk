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

package com.sam_chordas.android.stockhawk.data.repositories;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Defines an exception with two different codes, in order to display different error messages.
 */
public class QuoteException extends RuntimeException {

    private final int mCode;

    public QuoteException(@Code int code) {
        mCode = code;
    }

    public QuoteException(@Code int code, @NonNull String message) {
        super(message);
        mCode = code;
    }

    @Code
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
