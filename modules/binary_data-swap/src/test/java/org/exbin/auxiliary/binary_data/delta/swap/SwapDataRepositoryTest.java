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

import javax.annotation.ParametersAreNonnullByDefault;
import junit.framework.Assert;
import org.exbin.auxiliary.binary_data.delta.DeltaDocument;
import org.junit.Test;

/**
 * Tests for swap paging repository.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class SwapDataRepositoryTest {

    public static final String SAMPLE_FILES_PATH = "/org/exbin/auxiliary/binary_data/delta/swap/resources/test/";
    public static final String SAMPLE_ALLBYTES = SAMPLE_FILES_PATH + "allbytes.dat";
    public static final int SAMPLE_ALLBYTES_SIZE = 256;

    public SwapDataRepositoryTest() {
    }

    @Test
    public void testSwapping() {
        SwapDataRepository swapDataRepository = new SwapDataRepository();
        swapDataRepository.setMaximumMemoryUsage(0);
        DeltaDocument testDocument = swapDataRepository.getSegmentsRepository().createDocument();
        testDocument.insert(0, 8096);
        long usedPages = swapDataRepository.getSwapFilePages().getUsedPages();
        Assert.assertNotSame(usedPages, 0l);
    }
}
