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

import org.exbin.auxiliary.binary_data.BinaryData;

import java.util.concurrent.Future;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Interface for binary data with support for on demand retrieval.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface OnDemandBinaryData {

    /**
     * Returns true if data are currently available.
     *
     * @param startFrom position to start copy from
     * @param length length of area
     *
     * @return true if data available
     */
    boolean isAvailable(long startFrom, long length);

    /**
     * Requests retrieval of data.
     *
     * @param startFrom position to start copy from
     * @param length length of area
     * @return retrieval future object
     */
    @Nonnull
    Future<BinaryData> retrieveData(long startFrom, long length);
}
