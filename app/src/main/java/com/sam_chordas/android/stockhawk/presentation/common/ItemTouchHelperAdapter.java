package com.sam_chordas.android.stockhawk.presentation.common;

/**
 * Created by sam_chordas on 10/6/15.
 * credit to Paul Burke (ipaulpro)
 * Interface to enable swipe to delete
 */
public interface ItemTouchHelperAdapter {

    void onItemDismiss(int position);

    void onItemMove(int oldPos, int newPos);
}
