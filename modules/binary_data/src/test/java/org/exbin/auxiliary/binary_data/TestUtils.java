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
package org.exbin.auxiliary.binary_data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Tests for ByteArrayData class.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class TestUtils {

    public final static String SAMPLE_FILES_PATH = "/org/exbin/auxiliary/binary_data/resources/";
    public final static String SAMPLE_5BYTES = SAMPLE_FILES_PATH + "5bytes.dat";
    public final static String SAMPLE_10BYTES = SAMPLE_FILES_PATH + "10bytes.dat";
    public final static String SAMPLE_ALLBYTES = SAMPLE_FILES_PATH + "allbytes.dat";

    @Nonnull
    private final byte[] sampleDataA;
    @Nonnull
    private final byte[] sampleDataB;
    @Nonnull
    private final byte[] sampleDataC;

    public TestUtils() {
        sampleDataA = getSampleData(SAMPLE_5BYTES);
        sampleDataB = getSampleData(SAMPLE_10BYTES);
        sampleDataC = getSampleData(SAMPLE_ALLBYTES);
    }

    @Nonnull
    public byte[] getSampleDataA() {
        return sampleDataA;
    }

    @Nonnull
    public byte[] getSampleDataB() {
        return sampleDataB;
    }

    @Nonnull
    public byte[] getSampleDataC() {
        return sampleDataC;
    }

    @Nullable
    public InputStream getSampleDataStream(String sampleDataPath) {
        return TestUtils.class.getResourceAsStream(sampleDataPath);
    }

    @Nullable
    protected byte[] getSampleData(String dataPath) {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (InputStream stream = getSampleDataStream(dataPath)) {
            int read;
            do {
                read = stream.read(buffer);
                if (read > 0) {
                    outputStream.write(buffer, 0, read);
                }
            } while (read > 0);
            stream.close();
            outputStream.close();
            return outputStream.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(TestUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
