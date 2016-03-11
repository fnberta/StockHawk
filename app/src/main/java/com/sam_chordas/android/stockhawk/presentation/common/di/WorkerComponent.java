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

package com.sam_chordas.android.stockhawk.presentation.common.di;

import com.sam_chordas.android.stockhawk.di.ApplicationComponent;
import com.sam_chordas.android.stockhawk.di.RepositoriesModule;
import com.sam_chordas.android.stockhawk.di.scopes.PerFragment;
import com.sam_chordas.android.stockhawk.presentation.mystocks.SaveStockWorker;

import dagger.Component;

/**
 * Defines the dependencies for headless worker fragments.
 */
@PerFragment
@Component(dependencies = {ApplicationComponent.class},
        modules = {RepositoriesModule.class})
public interface WorkerComponent {

    void inject(SaveStockWorker saveStockWorker);
}
