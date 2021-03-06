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

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.databinding.DialogStockSearchBinding;

/**
 * Provides a dialog fragment that allows to user to search and save new stock symbols.
 */
public class FindStockDialogFragment extends DialogFragment {

    private static final String DIALOG_TAG = FindStockDialogFragment.class.getCanonicalName();
    private DialogListener mListener;

    /**
     * Shows a new instance of {@link FindStockDialogFragment}.
     *
     * @param fragmentManager the fragment manager to use for the transaction
     */
    public static void display(@NonNull FragmentManager fragmentManager) {
        final FindStockDialogFragment dialog = new FindStockDialogFragment();
        dialog.show(fragmentManager, DIALOG_TAG);
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (DialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final FragmentActivity activity = getActivity();
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        final DialogStockSearchBinding binding = DialogStockSearchBinding.inflate(activity.getLayoutInflater());
        dialogBuilder
                .setTitle(R.string.dialog_title_symbol_search)
                .setMessage(R.string.dialog_message_symbol_search)
                .setView(binding.getRoot())
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String stockSymbol = binding.etDialogStockSymbol.getText().toString();
                        mListener.onStockEntered(stockSymbol);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return dialogBuilder.create();
    }

    /**
     * Defines the actions to take when user clicks on one of the dialog's buttons.
     */
    public interface DialogListener {
        /**
         * Search and saves the entered symbol.
         *
         * @param stockSymbol the symbol to search and save
         */
        void onStockEntered(@NonNull String stockSymbol);
    }
}
