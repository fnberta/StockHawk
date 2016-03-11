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

package com.sam_chordas.android.stockhawk.presentation.mystocks.di;

import android.support.v4.content.CursorLoader;

import com.sam_chordas.android.stockhawk.di.ApplicationComponent;
import com.sam_chordas.android.stockhawk.di.RepositoriesModule;
import com.sam_chordas.android.stockhawk.di.scopes.PerActivity;
import com.sam_chordas.android.stockhawk.presentation.mystocks.MyStocksActivity;

import dagger.Component;

/**
 * Provides the dependencies for the my stocks screen.
 */
@PerActivity
@Component(dependencies = {ApplicationComponent.class},
        modules = {MyStocksViewModelModule.class, MyStocksLoaderModule.class, RepositoriesModule.class})
public interface MyStocksComponent {

    void inject(MyStocksActivity myStocksActivity);

    CursorLoader getMyStocksLoader();
}
