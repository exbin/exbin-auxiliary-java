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
package org.exbin.auxiliary.zone_data;

import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.auxiliary.binary_data.DataRange;

/**
 * Interface for binary data zone source.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface ZoneDataSource {

    /**
     * Returns zone for given index.
     *
     * @param zoneIndex zone index
     * @return zone data
     */
    @Nonnull
    ZoneData getZone(long zoneIndex);

    /**
     * Returns count of zones.
     *
     * @return count
     */
    long getZonesCount();

    /**
     * Updates zone state.
     *
     * @param zoneState target zone state
     * @param zoneIndex zone index
     */
    void updateZoneState(ZoneState zoneState, long zoneIndex);

    /**
     * Returns zone type for specific data position.
     *
     * @param dataPosition data position
     * @return zone type
     */
    @Nonnull
    Optional<ZoneType> findZoneType(long dataPosition);

    /**
     * Returns zone for specific data position.
     *
     * @param dataPosition data position
     * @return zone
     */
    @Nonnull
    ZoneType findZone(long dataPosition);

    /**
     * Returns zone range for specific data position.
     *
     * @param dataPosition data position
     * @return data range
     */
    @Nonnull
    DataRange findZoneRange(long dataPosition);

    /**
     * Returns start position of the zone on the given data position.
     *
     * @param dataPosition data position
     * @return start data position
     */
    long findZoneStart(long dataPosition);

    /**
     * Returns end position of the zone on the given data position.
     *
     * @param dataPosition data position
     * @return end data position
     */
    long findZoneEnd(long dataPosition);

    /**
     * Updates zone state for given data position.
     *
     * @param zoneState target zone state
     * @param dataPosition data position
     */
    void findZoneState(ZoneState zoneState, long dataPosition);
}
