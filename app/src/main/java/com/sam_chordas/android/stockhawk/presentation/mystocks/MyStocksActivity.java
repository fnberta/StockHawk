package com.sam_chordas.android.stockhawk.presentation.mystocks;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.StockHawk;
import com.sam_chordas.android.stockhawk.data.di.RepositoriesModule;
import com.sam_chordas.android.stockhawk.data.services.UpdateStocksIntentService;
import com.sam_chordas.android.stockhawk.data.services.UpdateStocksTaskService;
import com.sam_chordas.android.stockhawk.databinding.ActivityMyStocksBinding;
import com.sam_chordas.android.stockhawk.domain.repositories.StockRepository;
import com.sam_chordas.android.stockhawk.presentation.mystocks.di.DaggerMyStocksComponent;
import com.sam_chordas.android.stockhawk.presentation.mystocks.di.MyStocksViewModelModule;
import com.sam_chordas.android.stockhawk.utils.Utils;

import javax.inject.Inject;

import rx.Single;

public class MyStocksActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>,
        MyStocksViewModel.ViewListener,
        FindStockDialogFragment.DialogListener,
        SaveStockWorkerListener {

    private static final int CURSOR_LOADER_ID = 0;
    private static final String PERIODIC_UPDATE_SERVICE = "PERIODIC_UPDATE_SERVICE";
    @Inject
    MyStocksViewModel mViewModel;
    @Inject
    StockRepository mStockRepo;
    private ActivityMyStocksBinding mBinding;
    private MyStocksRecyclerAdapter mRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_stocks);
        mBinding.setViewModel(mViewModel);
        setupRecyclerView();

        getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        startPeriodicQueryService();
    }

    private void injectDependencies(@Nullable Bundle savedInstanceState) {
        DaggerMyStocksComponent.builder()
                .applicationComponent(StockHawk.getAppComponent(this))
                .myStocksViewModelModule(new MyStocksViewModelModule(savedInstanceState, this))
                .repositoriesModule(new RepositoriesModule())
                .build()
                .inject(this);
    }

    private void setupRecyclerView() {
        mRecyclerAdapter = new MyStocksRecyclerAdapter(null, mViewModel);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setHasFixedSize(true);
        mBinding.recyclerView.setAdapter(mRecyclerAdapter);
        final ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.START | ItemTouchHelper.END) {
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder source,
                                  @NonNull RecyclerView.ViewHolder target) {
                if (source.getItemViewType() != target.getItemViewType()) {
                    return false;
                }

                mRecyclerAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mRecyclerAdapter.onItemDismiss(viewHolder.getAdapterPosition());
            }
        });
        touchHelper.attachToRecyclerView(mBinding.recyclerView);
    }

    private void startPeriodicQueryService() {
        final long period = 3600L;
        final long flex = 10L;

        final PeriodicTask periodicTask = new PeriodicTask.Builder()
                .setService(UpdateStocksTaskService.class)
                .setTag(PERIODIC_UPDATE_SERVICE)
                .setPeriod(period)
                .setFlex(flex)
                .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                .setRequiresCharging(false)
                .build();
        GcmNetworkManager.getInstance(this).schedule(periodicTask);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mViewModel.saveState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mViewModel.onViewVisible();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mViewModel.onViewGone();
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
                // TODO: load settings if there are any
                return true;
            case R.id.action_change_units:
                // this is for changing stock changes from percent value to dollar value
                mViewModel.onChangeUnitsMenuClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mViewModel.onLoadingLocalStocks();
        return mStockRepo.getMyStocksLoader();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            mViewModel.setLoading(false);
        }
        mRecyclerAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecyclerAdapter.swapCursor(null);
    }

    @Override
    public boolean isNetworkAvailable() {
        return Utils.isNetworkAvailable(this);
    }

    @Override
    public void showMessage(int message, int length) {
        Snackbar.make(mBinding.recyclerView, message, length).show();
    }

    @Override
    public void removeWorker(@NonNull String workerTag) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final Fragment worker = fragmentManager.findFragmentByTag(workerTag);
        if (worker != null) {
            fragmentManager
                    .beginTransaction()
                    .remove(worker)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void notifyItemsChanged() {
        mRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadUpdateStocksService() {
        UpdateStocksIntentService.start(this);
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
    public void onWorkerError(@NonNull String workerTag) {
        mViewModel.onWorkerError(workerTag);
    }
}
