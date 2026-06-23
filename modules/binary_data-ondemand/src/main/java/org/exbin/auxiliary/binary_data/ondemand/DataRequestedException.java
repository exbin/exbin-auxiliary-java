/*
 * Copyright (C) ExBin Project, https://exbin.org
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

import java.util.Optional;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

/**
 * Exception for data reading when data are not available.
 */
@NullMarked
public class DataRequestedException extends RuntimeException {

    private final DataLoadingObserverProvider dataLoadingObserverProvider;

    public DataRequestedException() {
        dataLoadingObserverProvider = null;
    }

    public DataRequestedException(@Nullable DataLoadingObserverProvider dataLoadingObserverProvider) {
        this.dataLoadingObserverProvider = dataLoadingObserverProvider;
    }

    public DataRequestedException(@Nullable String message) {
        super(message);
        dataLoadingObserverProvider = null;
    }

    public DataRequestedException(@Nullable String message, @Nullable DataLoadingObserverProvider dataLoadingObserverProvider) {
        super(message);
        this.dataLoadingObserverProvider = dataLoadingObserverProvider;
    }

    public DataRequestedException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
        dataLoadingObserverProvider = null;
    }

    public DataRequestedException(@Nullable Throwable cause) {
        super(cause);
        dataLoadingObserverProvider = null;
    }

    public DataRequestedException(@Nullable String message, @Nullable Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        dataLoadingObserverProvider = null;
    }

    public Optional<DataLoadingObserverProvider> getDataLoadingObserverProvider() {
        return Optional.ofNullable(dataLoadingObserverProvider);
    }
}
