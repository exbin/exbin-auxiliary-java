/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.auxiliary.paged_data;

import java.util.concurrent.Future;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Interface for binary data.
 *
 * @version 0.1.3 2019/10/08
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
    Future retrieveData(long startFrom, long length);
}
