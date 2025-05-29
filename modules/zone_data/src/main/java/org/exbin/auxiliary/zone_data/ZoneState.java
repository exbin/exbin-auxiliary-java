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

/**
 * Interface for binary data zone state.
 *
 * @author ExBin Project (https://exbin.org)
 */
public interface ZoneState {

    /**
     * Returns zone type for specific data position.
     *
     * @param dataPosition data position
     * @return zone type
     */
    @Nonnull
    Optional<ZoneType> getZoneType(long dataPosition);

    /**
     * Sets zone type.
     *
     * @param zoneType zone type
     */
    void setZoneType(ZoneType zoneType);

    /**
     * Returns zone start position.
     *
     * @return data position
     */
    long getStartPosition();

    /**
     * Sets zone start position.
     *
     * @param dataPosition data position
     */
    void setStartPosition(long dataPosition);

    /**
     * Returns zone end position.
     *
     * @return data position
     */
    long getEndPosition();

    /**
     * Sets zone end position.
     *
     * @param dataPosition data position
     */
    void setEndPosition(long dataPosition);
}
