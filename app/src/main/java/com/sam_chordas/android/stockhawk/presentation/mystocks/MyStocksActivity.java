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

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.StockHawk;
import com.sam_chordas.android.stockhawk.data.bus.LocalBroadcast;
import com.sam_chordas.android.stockhawk.data.provider.QuoteProvider;
import com.sam_chordas.android.stockhawk.data.services.UpdateStocksIntentService;
import com.sam_chordas.android.stockhawk.data.services.UpdateStocksTaskService;
import com.sam_chordas.android.stockhawk.databinding.ActivityMyStocksBinding;
import com.sam_chordas.android.stockhawk.domain.repositories.StockRepository;
import com.sam_chordas.android.stockhawk.presentation.common.BaseActivity;
import com.sam_chordas.android.stockhawk.presentation.mystocks.di.DaggerMyStocksComponent;
import com.sam_chordas.android.stockhawk.presentation.mystocks.di.MyStocksComponent;
import com.sam_chordas.android.stockhawk.presentation.mystocks.di.MyStocksViewModelModule;
import com.sam_chordas.android.stockhawk.presentation.settings.SettingsActivity;
import com.sam_chordas.android.stockhawk.presentation.settings.SettingsFragment;
import com.sam_chordas.android.stockhawk.presentation.stockdetails.StockDetailsActivity;
import com.sam_chordas.android.stockhawk.presentation.widget.QuotesWidgetProvider;
import com.sam_chordas.android.stockhawk.utils.Utils;

import javax.inject.Inject;

import rx.Single;

/**
 * Displays the user's saved stock symbols and their values in a list and allows the user to search
 * and add new symbols via a {@link FloatingActionButton}.
 */
public class MyStocksActivity extends BaseActivity<MyStocksViewModel>
        implements LoaderManager.LoaderCallbacks<Cursor>,
        MyStocksViewModel.ViewListener,
        FindStockDialogFragment.DialogListener,
        SaveStockWorkerListener {

    private static final int CURSOR_LOADER_ID = 0;
    private static final int RC_PLAY_SERVICES = 1;
    private static final int RC_SETTINGS = 2;
    private final BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, @NonNull Intent intent) {
            final String action = intent.getAction();
            if (action.equals(LocalBroadcast.ACTION_DATA_UPDATED)) {
                final boolean successful = intent.getBooleanExtra(LocalBroadcast.EXTRA_SUCCESSFUL, false);
                mViewModel.onDataUpdated(successful);
            }
        }
    };
    @Inject
    StockRepository mStockRepo;
    private ActivityMyStocksBinding mBinding;
    private MyStocksRecyclerAdapter mRecyclerAdapter;
    private MyStocksComponent mComponent;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_stocks);
        mBinding.setViewModel(mViewModel);

        checkPlayServicesAvailable();

        setupRecyclerView();
        checkAddIntent(getIntent());

        getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
    }

    private void injectDependencies(@Nullable Bundle savedInstanceState) {
        mComponent = DaggerMyStocksComponent.builder()
                .applicationComponent(StockHawk.getAppComponent(this))
                .myStocksViewModelModule(new MyStocksViewModelModule(savedInstanceState, this))
                .build();
        mComponent.inject(this);
    }

    private void checkPlayServicesAvailable() {
        final GoogleApiAvailability availability = GoogleApiAvailability.getInstance();
        final int resultCode = availability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (availability.isUserResolvableError(resultCode)) {
                // Show dialog to resolve the error.
                availability.getErrorDialog(this, resultCode, RC_PLAY_SERVICES).show();
            } else {
                showMessage(R.string.snackbar_error_google_play_services);
            }
        }
    }

    private void setupRecyclerView() {
        mRecyclerAdapter = new MyStocksRecyclerAdapter(null, mViewModel, mStockRepo);
        mBinding.rvMyStocks.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rvMyStocks.setHasFixedSize(true);
        mBinding.rvMyStocks.setAdapter(mRecyclerAdapter);
        final ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.START | ItemTouchHelper.END) {
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder source,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mRecyclerAdapter.onItemDismiss(viewHolder.getAdapterPosition());
            }
        });
        touchHelper.attachToRecyclerView(mBinding.rvMyStocks);
    }

    private void checkAddIntent(@NonNull Intent intent) {
        final boolean addQuote = intent.getBooleanExtra(QuotesWidgetProvider.EXTRA_ADD_QUOTE, false);
        if (addQuote) {
            showFindStockDialog();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkAddIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalBroadcastReceiver,
                new IntentFilter(LocalBroadcast.ACTION_DATA_UPDATED));
    }

    @Override
    protected void onStop() {
        super.onStop();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mLocalBroadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_stocks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                startSettingsScreen();
                return true;
            case R.id.action_change_units:
                mViewModel.onChangeUnitsMenuClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startSettingsScreen() {
        final Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, RC_SETTINGS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SETTINGS && resultCode == SettingsFragment.RESULT_DEFAULT_SYMBOLS_CHANGED) {
            mViewModel.onDefaultSymbolsSettingsChanged();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mViewModel.onLoadingLocalStocks();
        return mComponent.getMyStocksLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            mViewModel.setLoading(false);
        } else if (!mStockRepo.isLoadDefaultSymbolsEnabled()) {
            mViewModel.setLoading(false);
        }

        mRecyclerAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecyclerAdapter.swapCursor(null);
    }

    @Override
    protected View getSnackbarView() {
        return mBinding.rvMyStocks;
    }

    @Override
    public boolean isNetworkAvailable() {
        return Utils.isNetworkAvailable(this);
    }

    @Override
    public boolean isDataAvailable() {
        return mRecyclerAdapter.isDataAvailable();
    }

    @Override
    public void notifyItemsChanged() {
        mRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void startUpdateStocksService() {
        UpdateStocksIntentService.start(this);
    }

    @Override
    public void startPeriodicUpdateStocksService(long period) {
        UpdateStocksTaskService.startOrUpdate(this, period);
    }

    @Override
    public void loadSaveStockWorker(@NonNull String stockSymbol) {
        SaveStockWorker.attach(getSupportFragmentManager(), stockSymbol);
    }

    @Override
    public void showFindStockDialog() {
        FindStockDialogFragment.display(getSupportFragmentManager());
    }

    @Override
    public void onStockEntered(@NonNull String stockSymbol) {
        mViewModel.onStockEntered(stockSymbol);
    }

    @Override
    public void setSaveStockStream(@NonNull Single<Uri> single, @NonNull String workerTag) {
        mViewModel.setSaveStockStream(single, workerTag);
    }

    @Override
    public void showStockDetailsScreen(int position) {
        final String symbol = mRecyclerAdapter.getSymbolForPosition(position);
        final Intent intent = new Intent(this, StockDetailsActivity.class);
        intent.setData(QuoteProvider.Quotes.withSymbol(symbol));
        startActivity(intent);
    }

    @Override
    public void showProgressDialog(@StringRes int message) {
        mProgressDialog = ProgressDialog.show(this, null, getString(message), true, true);
    }

    @Override
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
}
