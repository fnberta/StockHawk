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
import com.sam_chordas.android.stockhawk.databinding.ItemMyStocksBinding;
import com.sam_chordas.android.stockhawk.domain.repositories.StockRepository;
import com.sam_chordas.android.stockhawk.presentation.common.BaseBindingRow;
import com.sam_chordas.android.stockhawk.presentation.common.SwipeToDismissAdapter;


/**
 * Provides the adapter for the list of stock symbols and their values.
 */
public class MyStocksRecyclerAdapter extends RecyclerView.Adapter<MyStocksRecyclerAdapter.StockRow>
        implements SwipeToDismissAdapter {

    private final MyStocksViewModel mViewModel;
    private final StockRepository mStockRepo;
    private Cursor mCursor;
    private int mRowIdColumn;
    private boolean mDataValid;

    public MyStocksRecyclerAdapter(@Nullable Cursor cursor,
                                   @NonNull MyStocksViewModel viewModel,
                                   @NonNull StockRepository stockRepository) {
        mCursor = cursor;
        mDataValid = cursor != null;
        mRowIdColumn = mDataValid ? cursor.getColumnIndexOrThrow(BaseColumns._ID) : -1;
        setHasStableIds(true);

        mViewModel = viewModel;
        mStockRepo = stockRepository;
    }

    @Override
    public StockRow onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final ItemMyStocksBinding binding = ItemMyStocksBinding.inflate(inflater, parent, false);
        return new StockRow(binding, mViewModel);
    }

    @Override
    public void onBindViewHolder(StockRow holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }

        final String symbol = mCursor.getString(mCursor.getColumnIndexOrThrow(QuoteColumns.SYMBOL));
        final double bidPrice = mCursor.getDouble(mCursor.getColumnIndexOrThrow(QuoteColumns.BID_PRICE));
        final double changeValue = mCursor.getDouble(mCursor.getColumnIndexOrThrow(QuoteColumns.CHANGE));
        final String changePercent = mCursor.getString(mCursor.getColumnIndexOrThrow(QuoteColumns.PERCENT_CHANGE));

        final ItemMyStocksBinding binding = holder.getBinding();
        final StockRowViewModel viewModel = binding.getViewModel();
        if (viewModel == null) {
            binding.setViewModel(new StockRowViewModel(symbol, bidPrice, changeValue, changePercent,
                    mStockRepo.showPercentages()));
        } else {
            viewModel.setInfo(symbol, bidPrice, changeValue, changePercent, mStockRepo.showPercentages());
            viewModel.notifyChange();
        }
        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mDataValid ? mCursor.getCount() : 0;
    }

    @Override
    public long getItemId(int position) {
        if (mDataValid) {
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
            mDataValid = true;
            notifyDataSetChanged();
        } else {
            mRowIdColumn = -1;
            mDataValid = false;
            // notify about the lack of a data set
            notifyItemRangeRemoved(0, itemCount);
        }

        return oldCursor;
    }

    @Override
    public void onItemDismiss(int position) {
        mCursor.moveToPosition(position);
        final long rowId = mCursor.getLong(mCursor.getColumnIndexOrThrow(QuoteColumns._ID));
        mViewModel.onDeleteStockItem(rowId);
    }

    /**
     * Returns whether the cursor is valid and not empty.
     *
     * @return whether the cursor is valid and not empty
     */
    public boolean isDataAvailable() {
        return mDataValid && mCursor.moveToFirst();
    }

    /**
     * Returns the symbol for the given position.
     *
     * @param position the position to return the symbol for
     * @return the symbol for the given position
     */
    public String getSymbolForPosition(int position) {
        mCursor.moveToPosition(position);
        return mCursor.getString(mCursor.getColumnIndexOrThrow(QuoteColumns.SYMBOL));
    }

    /**
     * Defines the interaction with the view.
     */
    public interface AdapterListener {
        /**
         * Launches the detail screen for the clicked stock symbol.
         *
         * @param position the position of the clicked symbol
         */
        void onStockItemClick(int position);
    }

    /**
     * Provides a bindable {@link RecyclerView} row for a stock and its value.
     */
    public static class StockRow extends BaseBindingRow<ItemMyStocksBinding> {

        public StockRow(@NonNull ItemMyStocksBinding binding,
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
