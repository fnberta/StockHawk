/*
 * Copyright (c) 2015 Fabio Berta
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

package com.sam_chordas.android.stockhawk.presentation.mystocks;

import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sam_chordas.android.stockhawk.data.provider.QuoteColumns;
import com.sam_chordas.android.stockhawk.databinding.RowStocksQuoteBinding;
import com.sam_chordas.android.stockhawk.presentation.common.BaseBindingRow;
import com.sam_chordas.android.stockhawk.presentation.common.ItemTouchHelperAdapter;


/**
 * Provides the adapter for a movie poster images grid.
 */
public class MyStocksRecyclerAdapter extends RecyclerView.Adapter<MyStocksRecyclerAdapter.StockRow>
        implements ItemTouchHelperAdapter {

    private Cursor mCursor;
    private MyStocksViewModel mViewModel;
    private int mRowIdColumn;
    private boolean mDataIsValid;

    public MyStocksRecyclerAdapter(@Nullable Cursor cursor, @NonNull MyStocksViewModel viewModel) {
        mCursor = cursor;
        mDataIsValid = cursor != null;
        mRowIdColumn = mDataIsValid ? cursor.getColumnIndexOrThrow(BaseColumns._ID) : -1;
        setHasStableIds(true);

        mViewModel = viewModel;
    }

    @Override
    public StockRow onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final RowStocksQuoteBinding binding = RowStocksQuoteBinding.inflate(inflater, parent, false);
        return new StockRow(binding, mViewModel);
    }

    @Override
    public void onBindViewHolder(StockRow holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }

        final String symbol = mCursor.getString(mCursor.getColumnIndex(QuoteColumns.SYMBOL));
        final double bidPrice = mCursor.getDouble(mCursor.getColumnIndex(QuoteColumns.BID_PRICE));
        final double changeValue = mCursor.getDouble(mCursor.getColumnIndex(QuoteColumns.CHANGE));
        final String changePercent = mCursor.getString(mCursor.getColumnIndex(QuoteColumns.PERCENT_CHANGE));

        final RowStocksQuoteBinding binding = holder.getBinding();
        final StockRowViewModel viewModel = binding.getViewModel();
        if (viewModel == null) {
            binding.setViewModel(new StockRowViewModel(symbol, bidPrice, changeValue, changePercent,
                    mViewModel.isShowPercent()));
        } else {
            viewModel.setInfo(symbol, bidPrice, changeValue, changePercent, mViewModel.isShowPercent());
            viewModel.notifyChange();
        }
        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mDataIsValid ? mCursor.getCount() : 0;
    }

    @Override
    public long getItemId(int position) {
        if (mDataIsValid) {
            return mCursor.moveToPosition(position)
                    ? mCursor.getLong(mRowIdColumn)
                    : RecyclerView.NO_ID;
        } else {
            return RecyclerView.NO_ID;
        }
    }

    /**
     * Swap in a new Cursor, returning the old Cursor. The returned old Cursor is <em>not</em>
     * closed. Useful when using a loader that manages the cursor.
     *
     * @param newCursor The new cursor to be used.
     * @return the previously set Cursor, or null if there wasn't one.
     * If the given new Cursor is the same instance as the previously set
     * Cursor, null is returned.
     */
    public Cursor swapCursor(@Nullable Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }

        final int itemCount = getItemCount();
        final Cursor oldCursor = mCursor;
        mCursor = newCursor;

        if (newCursor != null) {
            mRowIdColumn = newCursor.getColumnIndexOrThrow(BaseColumns._ID);
            mDataIsValid = true;
            notifyDataSetChanged();
        } else {
            mRowIdColumn = -1;
            mDataIsValid = false;
            // notify about the lack of a data set
            notifyItemRangeRemoved(0, itemCount);
        }

        return oldCursor;
    }

    @Override
    public void onItemDismiss(int position) {
        mCursor.moveToPosition(position);
        final long rowId = mCursor.getLong(mCursor.getColumnIndex(QuoteColumns._ID));
        mViewModel.onDeleteStockItem(rowId);
    }

    @Override
    public void onItemMove(int oldPos, int newPos) {
        // TODO: implement reordering
    }

    public String getSymbolForPosition(int position) {
        mCursor.moveToPosition(position);
        return mCursor.getString(mCursor.getColumnIndex(QuoteColumns.SYMBOL));
    }

    public interface AdapterListener {
        void onStockItemClick(int position);
    }

    public static class StockRow extends BaseBindingRow<RowStocksQuoteBinding> {

        public StockRow(@NonNull RowStocksQuoteBinding binding,
                        @NonNull final AdapterListener listener) {
            super(binding);

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onStockItemClick(getAdapterPosition());
                }
            });
        }
    }
}
