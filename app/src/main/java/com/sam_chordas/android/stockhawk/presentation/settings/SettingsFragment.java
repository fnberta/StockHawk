package com.sam_chordas.android.stockhawk.presentation.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.StockHawk;

import javax.inject.Inject;

/**
 * Created by fabio on 11.03.16.
 */
public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final int RESULT_DEFAULT_SYMBOLS_CHANGED = 2;
    public static final int RESULT_SYNC_CHANGED = 3;
    public static final String PREF_DEFAULT_SYMBOLS = "pref_default_symbols";
    public static final String PREF_SYNC = "pref_sync";
    public static final String PREF_SYNC_INTERVAL = "pref_sync_interval";
    @Inject
    SharedPreferences mSharedPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPrefs = StockHawk.getAppComponent(getActivity()).getSharedPreferences();
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
        setupIntervalPref();
    }

    private void setupIntervalPref() {
        final ListPreference prefInterval = (ListPreference) findPreference(PREF_SYNC_INTERVAL);
        // Set summary to the current Entry
        prefInterval.setSummary(prefInterval.getEntry());

        // Update the summary with currently selected value
        prefInterval.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, @NonNull Object o) {
                int index = prefInterval.findIndexOfValue(o.toString());

                if (index >= 0) {
                    preference.setSummary(prefInterval.getEntries()[index]);
                } else {
                    preference.setSummary(null);
                }
                return true;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        mSharedPrefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        mSharedPrefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case PREF_DEFAULT_SYMBOLS:
                getActivity().setResult(RESULT_DEFAULT_SYMBOLS_CHANGED);
                break;
            case PREF_SYNC:
                // fall through
            case PREF_SYNC_INTERVAL:
                getActivity().setResult(RESULT_SYNC_CHANGED);
                break;
        }
    }
}
