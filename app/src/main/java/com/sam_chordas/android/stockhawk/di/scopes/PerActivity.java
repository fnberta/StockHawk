/*
 * Copyright (c) 2016 Fabio Berta
 */

package com.sam_chordas.android.stockhawk.di.scopes;

import javax.inject.Scope;

/**
 * Defines a custom di scope indicating that the injected modules should live as long as the
 * activity that contains them.
 */
@Scope
public @interface PerActivity {
}