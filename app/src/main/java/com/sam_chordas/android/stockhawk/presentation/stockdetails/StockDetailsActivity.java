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

package com.sam_chordas.android.stockhawk.presentation.stockdetails;

import android.content.Intent;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.github.mikephil.charting.data.LineDataSet;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.StockHawk;
import com.sam_chordas.android.stockhawk.data.rest.QuoteTime;
import com.sam_chordas.android.stockhawk.databinding.ActivityStockDetailsBinding;
import com.sam_chordas.android.stockhawk.domain.repositories.StockRepository;
import com.sam_chordas.android.stockhawk.presentation.common.BaseActivity;
import com.sam_chordas.android.stockhawk.presentation.stockdetails.di.DaggerStockDetailsComponent;
import com.sam_chordas.android.stockhawk.presentation.stockdetails.di.StockDetailsComponent;
import com.sam_chordas.android.stockhawk.presentation.stockdetails.di.StockDetailsLoaderModule;
import com.sam_chordas.android.stockhawk.presentation.stockdetails.di.StockDetailsViewModelModule;
import com.sam_chordas.android.stockhawk.utils.Utils;

import javax.inject.Inject;

import rx.Observable;

/**
 * Displays a stock's value over the last 6 months in a line chart.
 */
public class StockDetailsActivity extends BaseActivity<StockDetailsViewModel>
        implements StockDetailsViewModel.ViewListener,
        LoaderManager.LoaderCallbacks<Observable<QuoteTime>> {

    private static final int DETAILS_LOADER_ID = 0;
    @Inject
    StockRepository mStockRepo;
    private ActivityStockDetailsBinding mBinding;
    private StockDetailsComponent mComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String stockSymbol = getStockSymbol();
        injectDependencies(savedInstanceState, stockSymbol);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(stockSymbol);
        }

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_stock_details);
        mBinding.setViewModel(mViewModel);
        setChartStyle();

        if (Utils.isNetworkAvailable(this)) {
            getSupportLoaderManager().initLoader(DETAILS_LOADER_ID, null, this);
        } else {
            mViewModel.setLoading(false);
            showMessage(R.string.snackbar_error_no_network);
        }
    }

    private String getStockSymbol() {
        final Intent intent = getIntent();
        return intent.getData().getLastPathSegment();
    }

    private void injectDependencies(@Nullable Bundle savedInstanceState, @NonNull String stockSymbol) {
        mComponent = DaggerStockDetailsComponent.builder()
                .applicationComponent(StockHawk.getAppComponent(this))
                .stockDetailsViewModelModule(new StockDetailsViewModelModule(savedInstanceState, this, stockSymbol))
                .stockDetailsLoaderModule(new StockDetailsLoaderModule(stockSymbol))
                .build();
        mComponent.inject(this);
    }

    private void setChartStyle() {
        final int textColor = getTextColorPrimary();
        mBinding.lcStockDetails.setNoDataText("");
        mBinding.lcStockDetails.setDescription(getString(R.string.desc_stock_value_time));
        mBinding.lcStockDetails.setDescriptionColor(textColor);
        mBinding.lcStockDetails.setAutoScaleMinMaxEnabled(true);
        mBinding.lcStockDetails.getXAxis().setTextColor(textColor);
        mBinding.lcStockDetails.getAxisLeft().setTextColor(textColor);
        mBinding.lcStockDetails.getAxisRight().setTextColor(textColor);
        mBinding.lcStockDetails.getLegend().setTextColor(textColor);
    }

    private int getTextColorPrimary() {
        final int[] attrs = new int[]{android.R.attr.textColorPrimary};
        final TypedArray typedArray = obtainStyledAttributes(attrs);
        final int textColorId = typedArray.getResourceId(0, 0);
        final int textColor = ContextCompat.getColor(this, textColorId);
        typedArray.recycle();
        return textColor;
    }

    @Override
    public Loader<Observable<QuoteTime>> onCreateLoader(int id, Bundle args) {
        return mComponent.getStockDetailsLoader();
    }

    @Override
    public void onLoadFinished(Loader<Observable<QuoteTime>> loader, Observable<QuoteTime> data) {
        mViewModel.onDataLoaded(data);
    }

    @Override
    public void onLoaderReset(Loader<Observable<QuoteTime>> loader) {
        // do nothing
    }

    @Override
    protected View getSnackbarView() {
        return mBinding.lcStockDetails;
    }

    @Override
    public void setChartColors(@NonNull LineDataSet lineDataSet) {
        final int textColor = getTextColorPrimary();
        lineDataSet.setValueTextColor(textColor);
    }
}
