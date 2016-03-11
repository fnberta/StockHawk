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

package com.sam_chordas.android.stockhawk.presentation.common;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

/**
 * Provides an abstract base class for a {@link RecyclerView} row that uses the Android data
 * binding framework to bind its views.
 * <p/>
 * Subclass {@link RecyclerView.ViewHolder}.
 */
public abstract class BaseBindingRow<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

    private final T mBinding;

    /**
     * Constructs a new {@link BaseBindingRow}.
     *
     * @param binding the binding to use
     */
    public BaseBindingRow(@NonNull T binding) {
        super(binding.getRoot());

        mBinding = binding;
    }

    public T getBinding() {
        return mBinding;
    }
}
