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
package org.exbin.auxiliary.binary_data.jna;

import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nonnull;
import org.exbin.auxiliary.binary_data.OutOfBoundsException;
import org.exbin.auxiliary.binary_data.TestUtils;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests for JnaBufferEditableData class.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class JnaBufferEditableDataTest {

    @Nonnull
    private final TestUtils testUtils = new TestUtils();

    public JnaBufferEditableDataTest() {
    }

    @Test
    public void testSetDataSize() {
        JnaBufferEditableData instanceA = new JnaBufferEditableData(testUtils.getSampleDataA());
        instanceA.setDataSize(10);
        assertEquals(10l, instanceA.getDataSize());

        JnaBufferEditableData instanceA1 = new JnaBufferEditableData(testUtils.getSampleDataA());
        instanceA1.setDataSize(4);
        assertEquals(4l, instanceA1.getDataSize());
    }

    @Test
    public void testSetByte() {
        JnaBufferEditableData instanceA = new JnaBufferEditableData(testUtils.getSampleDataA());
        instanceA.setByte(0, (byte) 50);
        instanceA.setByte(1, (byte) 51);
        assertEquals(50, instanceA.getByte(0));
        assertEquals(51, instanceA.getByte(1));

        try {
            instanceA.setByte(5, (byte) 55);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testInsertUninitialized() {
        JnaBufferEditableData instanceA = new JnaBufferEditableData(testUtils.getSampleDataA());
        instanceA.insertUninitialized(1, 2);
        assertEquals(7l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x34, instanceA.getByte(3));
        assertEquals((byte) 0x56, instanceA.getByte(4));

        JnaBufferEditableData instanceB = new JnaBufferEditableData(testUtils.getSampleDataB());
        try {
            instanceB.insertUninitialized(20, 1);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testInsertLength() {
        JnaBufferEditableData instanceA = new JnaBufferEditableData(testUtils.getSampleDataA());
        instanceA.insert(1, 2);
        assertEquals(7l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x0, instanceA.getByte(1));
        assertEquals((byte) 0x0, instanceA.getByte(2));
        assertEquals((byte) 0x34, instanceA.getByte(3));
        assertEquals((byte) 0x56, instanceA.getByte(4));

        JnaBufferEditableData instanceB = new JnaBufferEditableData(testUtils.getSampleDataB());
        try {
            instanceB.insert(20, 1);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testInsert_byteArray() {
        JnaBufferEditableData instanceA = new JnaBufferEditableData(testUtils.getSampleDataA());
        instanceA.insert(1, new byte[]{1, 2});
        assertEquals(7l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x1, instanceA.getByte(1));
        assertEquals((byte) 0x2, instanceA.getByte(2));
        assertEquals((byte) 0x34, instanceA.getByte(3));
        assertEquals((byte) 0x56, instanceA.getByte(4));

        JnaBufferEditableData instanceB = new JnaBufferEditableData(testUtils.getSampleDataB());
        try {
            instanceB.insert(20, new byte[]{1});
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testInsert_BinaryData() {
        JnaBufferEditableData instanceA = new JnaBufferEditableData(testUtils.getSampleDataA());
        instanceA.insert(1, new JnaBufferData(new byte[]{1, 2}));
        assertEquals(7l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x1, instanceA.getByte(1));
        assertEquals((byte) 0x2, instanceA.getByte(2));
        assertEquals((byte) 0x34, instanceA.getByte(3));
        assertEquals((byte) 0x56, instanceA.getByte(4));

        JnaBufferEditableData instanceB = new JnaBufferEditableData(testUtils.getSampleDataB());
        try {
            instanceB.insert(20, new JnaBufferData(new byte[]{1}));
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testInsert() {
        JnaBufferEditableData instanceA = new JnaBufferEditableData(testUtils.getSampleDataA());
        instanceA.insert(1, new byte[]{5, 7, 1, 2, 10}, 2, 2);
        assertEquals(7l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x1, instanceA.getByte(1));
        assertEquals((byte) 0x2, instanceA.getByte(2));
        assertEquals((byte) 0x34, instanceA.getByte(3));
        assertEquals((byte) 0x56, instanceA.getByte(4));

        JnaBufferEditableData instanceB = new JnaBufferEditableData(testUtils.getSampleDataB());
        try {
            instanceB.insert(20, new byte[]{5, 7, 1, 2, 10}, 2, 1);
            fail();
        } catch (OutOfBoundsException ex) {
        }

        JnaBufferEditableData instanceB2 = new JnaBufferEditableData(testUtils.getSampleDataB());
        try {
            instanceB2.insert(0, new byte[]{5, 7, 1, 2, 10}, 10, 1);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testFillData() {
        JnaBufferEditableData instanceA = new JnaBufferEditableData(testUtils.getSampleDataA());
        instanceA.fillData(1, 2);
        assertEquals(5l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x0, instanceA.getByte(1));
        assertEquals((byte) 0x0, instanceA.getByte(2));
        assertEquals((byte) 0x78, instanceA.getByte(3));
        assertEquals((byte) 0x9a, instanceA.getByte(4));

        JnaBufferEditableData instanceB = new JnaBufferEditableData(testUtils.getSampleDataB());
        try {
            instanceB.fillData(20, 1);
            fail();
        } catch (OutOfBoundsException ex) {
        }

        JnaBufferEditableData instanceB2 = new JnaBufferEditableData(testUtils.getSampleDataB());
        try {
            instanceB2.fillData(0, 20);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testFillData_byte() {
        JnaBufferEditableData instanceA = new JnaBufferEditableData(testUtils.getSampleDataA());
        instanceA.fillData(1, 2, (byte) 0x55);
        assertEquals(5l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x55, instanceA.getByte(1));
        assertEquals((byte) 0x55, instanceA.getByte(2));
        assertEquals((byte) 0x78, instanceA.getByte(3));
        assertEquals((byte) 0x9a, instanceA.getByte(4));

        JnaBufferEditableData instanceB = new JnaBufferEditableData(testUtils.getSampleDataB());
        try {
            instanceB.fillData(20, 1, (byte) 55);
            fail();
        } catch (OutOfBoundsException ex) {
        }

        JnaBufferEditableData instanceB2 = new JnaBufferEditableData(testUtils.getSampleDataB());
        try {
            instanceB2.fillData(0, 20, (byte) 55);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testReplace_BinaryData() {
        JnaBufferEditableData instanceA = new JnaBufferEditableData(testUtils.getSampleDataA());
        instanceA.replace(1, new JnaBufferData(new byte[]{1, 2}));
        assertEquals(5l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x1, instanceA.getByte(1));
        assertEquals((byte) 0x2, instanceA.getByte(2));
        assertEquals((byte) 0x78, instanceA.getByte(3));
        assertEquals((byte) 0x9a, instanceA.getByte(4));

        JnaBufferEditableData instanceB = new JnaBufferEditableData(testUtils.getSampleDataB());
        try {
            instanceB.replace(20, new JnaBufferData(new byte[]{1}));
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testReplace_BinaryData_offset() {
        JnaBufferEditableData instanceA = new JnaBufferEditableData(testUtils.getSampleDataA());
        instanceA.replace(1, new JnaBufferData(new byte[]{5, 7, 1, 2, 10}), 2, 2);
        assertEquals(5l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x1, instanceA.getByte(1));
        assertEquals((byte) 0x2, instanceA.getByte(2));
        assertEquals((byte) 0x78, instanceA.getByte(3));
        assertEquals((byte) 0x9a, instanceA.getByte(4));

        JnaBufferEditableData instanceB = new JnaBufferEditableData(testUtils.getSampleDataB());
        try {
            instanceB.replace(20, new JnaBufferData(new byte[]{5, 7, 1, 2, 10}), 2, 2);
            fail();
        } catch (OutOfBoundsException ex) {
        }

        JnaBufferEditableData instanceB2 = new JnaBufferEditableData(testUtils.getSampleDataB());
        try {
            instanceB2.replace(0, new JnaBufferData(new byte[]{5, 7, 1, 2, 10}), 10, 2);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testReplace_ByteArray() {
        JnaBufferEditableData instanceA = new JnaBufferEditableData(testUtils.getSampleDataA());
        instanceA.replace(1, new byte[]{1, 2});
        assertEquals(5l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x1, instanceA.getByte(1));
        assertEquals((byte) 0x2, instanceA.getByte(2));
        assertEquals((byte) 0x78, instanceA.getByte(3));
        assertEquals((byte) 0x9a, instanceA.getByte(4));

        JnaBufferEditableData instanceB = new JnaBufferEditableData(testUtils.getSampleDataB());
        try {
            instanceB.replace(20, new byte[]{1});
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testReplace_ByteArray_offset() {
        JnaBufferEditableData instanceA = new JnaBufferEditableData(testUtils.getSampleDataA());
        instanceA.replace(1, new byte[]{5, 7, 1, 2, 10}, 2, 2);
        assertEquals(5l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x1, instanceA.getByte(1));
        assertEquals((byte) 0x2, instanceA.getByte(2));
        assertEquals((byte) 0x78, instanceA.getByte(3));
        assertEquals((byte) 0x9a, instanceA.getByte(4));

        JnaBufferEditableData instanceB = new JnaBufferEditableData(testUtils.getSampleDataB());
        try {
            instanceB.replace(20, new byte[]{5, 7, 1, 2, 10}, 2, 2);
            fail();
        } catch (OutOfBoundsException ex) {
        }

        JnaBufferEditableData instanceB2 = new JnaBufferEditableData(testUtils.getSampleDataB());
        try {
            instanceB2.replace(0, new byte[]{5, 7, 1, 2, 10}, 10, 2);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testRemove() {
        JnaBufferEditableData instanceA = new JnaBufferEditableData(testUtils.getSampleDataA());
        instanceA.remove(1, 2);
        assertEquals(3l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x78, instanceA.getByte(1));
        assertEquals((byte) 0x9a, instanceA.getByte(2));

        JnaBufferEditableData instanceB = new JnaBufferEditableData(testUtils.getSampleDataB());
        try {
            instanceB.remove(20, 1);
            fail();
        } catch (OutOfBoundsException ex) {
        }

        JnaBufferEditableData instanceB2 = new JnaBufferEditableData(testUtils.getSampleDataB());
        try {
            instanceB2.remove(5, 100);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testClear() {
        JnaBufferEditableData instanceA = new JnaBufferEditableData(testUtils.getSampleDataA());
        instanceA.clear();
        assertEquals(0l, instanceA.getDataSize());
    }

    @Test
    public void testLoadFromStream() throws Exception {
        JnaBufferEditableData instanceA = new JnaBufferEditableData();
        try (InputStream streamA = testUtils.getSampleDataStream(TestUtils.SAMPLE_5BYTES)) {
            instanceA.loadFromStream(streamA);
        }
        assertEquals(5l, instanceA.getDataSize());
        assertEquals(0x12, instanceA.getByte(0));
        assertEquals(0x9a, instanceA.getByte(4) & 0xff);

        JnaBufferEditableData instanceB = new JnaBufferEditableData();
        try (InputStream streamB = testUtils.getSampleDataStream(TestUtils.SAMPLE_10BYTES)) {
            instanceB.loadFromStream(streamB);
        }
        assertEquals(10l, instanceB.getDataSize());
        assertEquals(0x41, instanceB.getByte(0));
        assertEquals(0x4a, instanceB.getByte(9));

        JnaBufferEditableData instanceC = new JnaBufferEditableData();
        try (InputStream streamC = testUtils.getSampleDataStream(TestUtils.SAMPLE_ALLBYTES)) {
            instanceC.loadFromStream(streamC);
        }
        assertEquals(256l, instanceC.getDataSize());
        assertEquals(0x0, instanceC.getByte(0));
        assertEquals(0x7f, instanceC.getByte(0x7f) & 0xff);
        assertEquals(0xff, instanceC.getByte(0xff) & 0xff);
    }

    @Test
    public void testLoadFromNonBlockingStream() throws Exception {
        JnaBufferEditableData instanceC = new JnaBufferEditableData();
        try (InputStream streamC = testUtils.getSampleDataStream(TestUtils.SAMPLE_ALLBYTES)) {
            InputStream nonBlockingStream = new InputStream() {
                @Override
                public int read() throws IOException {
                    return streamC.read();
                }
            };

            instanceC.loadFromStream(nonBlockingStream);
        }
        assertEquals(256l, instanceC.getDataSize());
        assertEquals(0x0, instanceC.getByte(0));
        assertEquals(0x7f, instanceC.getByte(0x7f) & 0xff);
        assertEquals(0xff, instanceC.getByte(0xff) & 0xff);
    }

    @Test
    public void testLoadFromStream_offset() throws Exception {
        JnaBufferEditableData instanceA = new JnaBufferEditableData();
        try (InputStream streamA = testUtils.getSampleDataStream(TestUtils.SAMPLE_ALLBYTES)) {
            instanceA.insert(100, streamA, 100);
        }
        assertEquals(200l, instanceA.getDataSize());
        assertEquals((byte) 0, instanceA.getByte(100));
        assertEquals((byte) 1, instanceA.getByte(101));
        assertEquals((byte) 99, instanceA.getByte(199));
    }

    @Test
    public void testEquals() {
        JnaBufferEditableData instanceA = new JnaBufferEditableData(testUtils.getSampleDataA());

        JnaBufferEditableData instanceA1 = new JnaBufferEditableData(testUtils.getSampleDataA());

        assertTrue(instanceA.equals(instanceA1));

        JnaBufferEditableData instanceB = new JnaBufferEditableData(testUtils.getSampleDataB());

        assertFalse(instanceA.equals(instanceB));
    }
}
