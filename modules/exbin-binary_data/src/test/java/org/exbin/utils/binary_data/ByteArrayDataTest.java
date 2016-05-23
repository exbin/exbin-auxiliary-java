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
package org.exbin.utils.binary_data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 * Tests for hexadecimal component.
 *
 * @version 0.1.0 2016/05/23
 * @author ExBin Project (http://exbin.org)
 */
public class ByteArrayDataTest {

    public final static String SAMPLE_FILES_PATH = "/org/exbin/utils/binary_data/resources/";
    public final static String SAMPLE_5BYTES = SAMPLE_FILES_PATH + "5bytes.dat";
    public final static String SAMPLE_10BYTES = SAMPLE_FILES_PATH + "10bytes.dat";
    public final static String SAMPLE_ALLBYTES = SAMPLE_FILES_PATH + "allbytes.dat";

    private static byte[] sampleDataA;
    private static byte[] sampleDataB;
    private static byte[] sampleDataC;

    public ByteArrayDataTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        sampleDataA = getSampleData(SAMPLE_5BYTES);
        sampleDataB = getSampleData(SAMPLE_10BYTES);
        sampleDataC = getSampleData(SAMPLE_ALLBYTES);
    }

    @Test
    public void testGetDataSize() {
        ByteArrayData instanceA = new ByteArrayData(sampleDataA);
        assertEquals(5l, instanceA.getDataSize());

        ByteArrayData instanceB = new ByteArrayData(sampleDataB);
        assertEquals(10l, instanceB.getDataSize());

        ByteArrayData instanceC = new ByteArrayData(sampleDataC);
        assertEquals(256l, instanceC.getDataSize());
    }

    @Test
    public void testIsEmpty() {
        ByteArrayData instanceA = new ByteArrayData(sampleDataA);
        assertEquals(false, instanceA.isEmpty());

        ByteArrayData instanceB = new ByteArrayData(sampleDataB);
        assertEquals(false, instanceB.isEmpty());

        ByteArrayData instanceC = new ByteArrayData(sampleDataC);
        assertEquals(false, instanceC.isEmpty());

        ByteArrayData instanceD = new ByteArrayData();
        assertEquals(true, instanceD.isEmpty());

        ByteArrayData instanceE = new ByteArrayData(new byte[0]);
        assertEquals(true, instanceE.isEmpty());
    }

    @Test
    public void testGetByte() {
        ByteArrayData instanceA = new ByteArrayData(sampleDataA);
        assertEquals(0x12, instanceA.getByte(0));
        assertEquals(0x9a, instanceA.getByte(4) & 0xff);

        ByteArrayData instanceB = new ByteArrayData(sampleDataB);
        assertEquals(0x41, instanceB.getByte(0));
        assertEquals(0x4a, instanceB.getByte(9));

        ByteArrayData instanceC = new ByteArrayData(sampleDataC);
        assertEquals(0x0, instanceC.getByte(0));
        assertEquals(0x7f, instanceC.getByte(0x7f) & 0xff);
        assertEquals(0xff, instanceC.getByte(0xff) & 0xff);
    }

    @Test
    public void testSaveToStream() throws Exception {
    }

    @Test
    public void testCopy_0args() {
    }

    @Test
    public void testCopy_long_long() {
    }

    private static byte[] getSampleData(String dataPath) {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (InputStream stream = ByteArrayDataTest.class.getResourceAsStream(dataPath)) {
            while (stream.available() > 0) {
                int read = stream.read(buffer);
                if (read > 0) {
                    outputStream.write(buffer, 0, read);
                }
            }
            stream.close();
            outputStream.close();
            return outputStream.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(ByteArrayDataTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
