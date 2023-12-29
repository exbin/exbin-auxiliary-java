/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.auxiliary.binary_data.ondemand;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.event.ChangeListener;

/**
 * Interface for data loaded event.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface DataLoadingProgressObserver extends DataLoadingObserver {

    /**
     * Should provide fixed length of data to load.
     *
     * @return data size
     */
    long fullDataSize();

    /**
     * Volatile changing value of how many data was loaded so far.
     *
     * @return data size
     */
    long loadedProgressSize();

    void addChangeListener(ChangeListener changeListener);

    void removeChangeListener(ChangeListener changeListener);
}
