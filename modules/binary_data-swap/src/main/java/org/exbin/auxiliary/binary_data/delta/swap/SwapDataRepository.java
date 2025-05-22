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
package org.exbin.auxiliary.binary_data.delta.swap;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.auxiliary.binary_data.array.ByteArrayEditableData;
import org.exbin.auxiliary.binary_data.EditableBinaryData;
import org.exbin.auxiliary.binary_data.delta.MemorySegmentCreator;
import org.exbin.auxiliary.binary_data.delta.SegmentsRepository;

/**
 * TODO: Repository for data paging with support for swapping.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class SwapDataRepository {

    /**
     * Maximum memory allowed to be used before swapping will be enforced.
     */
    private long maximumMemoryUsage = 0;

    private final SwapFilePages swapFilePages = new SwapFilePages();
    private final MemorySegmentCreator memorySegmentCreator;
    private final SegmentsRepository segmentsRepository;

    public SwapDataRepository() {
        memorySegmentCreator = new SwapDataSegmentCreator();
        segmentsRepository = new SegmentsRepository(memorySegmentCreator);
    }

    @Nonnull
    public MemorySegmentCreator getMemorySegmentCreator() {
        return memorySegmentCreator;
    }

    @Nonnull
    public SwapFilePages getSwapFilePages() {
        return swapFilePages;
    }

    @Nonnull
    public SegmentsRepository getSegmentsRepository() {
        return segmentsRepository;
    }

    public long getMaximumMemoryUsage() {
        return maximumMemoryUsage;
    }

    public void setMaximumMemoryUsage(long maximumMemoryUsage) {
        this.maximumMemoryUsage = maximumMemoryUsage;
        // TODO
    }

    @ParametersAreNonnullByDefault
    private class SwapDataSegmentCreator implements MemorySegmentCreator {

        @Override
        public EditableBinaryData createSegment() {
            return new ByteArrayEditableData();
            /*
            if ((maximumMemoryUsage == -1) || (maximumMemoryUsage > sourceData.length)) {
                EditableBinaryData dataPage = new ByteArrayEditableData(sourceData);
                maximumMemoryUsage -= sourceData.length;
                return dataPage;
            }

            long allocatedPage = swapFilePages.allocatePage();
            swapFilePages.setPage(allocatedPage, sourceData);
            return new SwappedDataPage(SwapDataRepository.this, allocatedPage, sourceData.length); */
        }
    }
}
