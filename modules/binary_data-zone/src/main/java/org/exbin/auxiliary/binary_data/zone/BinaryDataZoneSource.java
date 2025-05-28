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
package org.exbin.auxiliary.binary_data.zone;

import java.util.Optional;
import javax.annotation.Nonnull;
import org.exbin.auxiliary.binary_data.DataRange;

/**
 * Interface for binary data zone source.
 *
 * @author ExBin Project (https://exbin.org)
 */
public interface BinaryDataZoneSource {

    /**
     * Returns zone type for specific data position.
     *
     * @param dataPosition data position
     * @return zone type
     */
    @Nonnull
    Optional<ZoneType> getZoneType(long dataPosition);

    /**
     * Returns zone for specific data position.
     *
     * @param dataPosition data position
     * @return zone
     */
    @Nonnull
    ZoneType getZone(long dataPosition);

    /**
     * Returns zone range for specific data position.
     *
     * @param dataPosition data position
     * @return data range
     */
    @Nonnull
    DataRange getZoneRange(long dataPosition);

    /**
     * Returns start position of the zone on the given data position.
     *
     * @param dataPosition data position
     * @return start data position
     */
    long getZoneStart(long dataPosition);

    /**
     * Returns end position of the zone on the given data position.
     *
     * @param dataPosition data position
     * @return end data position
     */
    long getZoneEnd(long dataPosition);
}
