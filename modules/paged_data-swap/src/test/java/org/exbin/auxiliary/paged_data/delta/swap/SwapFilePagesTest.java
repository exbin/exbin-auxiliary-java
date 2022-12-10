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
package org.exbin.auxiliary.paged_data.delta.swap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for swap file pages handling.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class SwapFilePagesTest {

    public static final String SAMPLE_FILES_PATH = "/org/exbin/auxiliary/paged_data/delta/swap/resources/test/";
    public static final String SAMPLE_ALLBYTES = SAMPLE_FILES_PATH + "allbytes.dat";
    public static final int SAMPLE_ALLBYTES_SIZE = 256;

    public SwapFilePagesTest() {
    }

    @Test
    public void testSwapFileRepository() {
        SwapFilePages swapFilePages = new SwapFilePages();
        byte[] emptyData = new byte[swapFilePages.getPageSize()];

        long page1 = swapFilePages.allocatePage();
        byte[] page1Data = swapFilePages.getPage(page1);
        Assert.assertArrayEquals(emptyData, page1Data);

        File sampleFile = new File(SwapFilePagesTest.class.getResource(SAMPLE_ALLBYTES).getFile());
        byte[] sampleData;
        try {
            sampleData = Files.readAllBytes(sampleFile.toPath());
            System.arraycopy(sampleData, 0, page1Data, 0, SAMPLE_ALLBYTES_SIZE);
        } catch (IOException ex) {
            Logger.getLogger(SwapFilePagesTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Unable to read sample file");
        }

        swapFilePages.setPage(page1, page1Data);

        byte[] page1Modified = swapFilePages.getPage(page1);
        Assert.assertArrayEquals(page1Data, page1Modified);
        swapFilePages.close();
    }
}
