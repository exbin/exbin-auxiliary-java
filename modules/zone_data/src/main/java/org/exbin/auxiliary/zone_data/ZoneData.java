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
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import org.exbin.auxiliary.binary_data.DataRange;

/**
 * Zone types for editation of the binary data.
 *
 * @author ExBin Project (https://exbin.org)
 */
@Immutable
public class ZoneData extends DataRange {

    private final ZoneType zoneType;

    public ZoneData(long startPosition, long endPosition) {
        this(null, startPosition, endPosition);
    }

    public ZoneData(@Nullable ZoneType zoneType, long startPosition, long endPosition) {
        super(startPosition, endPosition);
        this.zoneType = zoneType;
    }

    @Nonnull
    public Optional<ZoneType> getZoneType() {
        return Optional.ofNullable(zoneType);
    }
}
