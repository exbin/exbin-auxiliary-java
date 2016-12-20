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

import java.io.InputStream;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests for ByteArrayEditableData class.
 *
 * @version 0.1.1 2016/11/02
 * @author ExBin Project (http://exbin.org)
 */
public class ByteArrayEditableDataTest {

    private final TestUtils testUtils = new TestUtils();

    public ByteArrayEditableDataTest() {
    }

    @Test
    public void testSetDataSize() {
        ByteArrayEditableData instanceA = new ByteArrayEditableData(testUtils.getSampleDataA());
        instanceA.setDataSize(10);
        assertEquals(10l, instanceA.getDataSize());

        ByteArrayEditableData instanceA1 = new ByteArrayEditableData(testUtils.getSampleDataA());
        instanceA1.setDataSize(4);
        assertEquals(4l, instanceA1.getDataSize());
    }

    @Test
    public void testSetByte() {
        ByteArrayEditableData instanceA = new ByteArrayEditableData(testUtils.getSampleDataA());
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
        ByteArrayEditableData instanceA = new ByteArrayEditableData(testUtils.getSampleDataA());
        instanceA.insertUninitialized(1, 2);
        assertEquals(7l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x34, instanceA.getByte(3));
        assertEquals((byte) 0x56, instanceA.getByte(4));

        ByteArrayEditableData instanceB = new ByteArrayEditableData(testUtils.getSampleDataB());
        try {
            instanceB.insertUninitialized(20, 1);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testInsertLength() {
        ByteArrayEditableData instanceA = new ByteArrayEditableData(testUtils.getSampleDataA());
        instanceA.insert(1, 2);
        assertEquals(7l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x0, instanceA.getByte(1));
        assertEquals((byte) 0x0, instanceA.getByte(2));
        assertEquals((byte) 0x34, instanceA.getByte(3));
        assertEquals((byte) 0x56, instanceA.getByte(4));

        ByteArrayEditableData instanceB = new ByteArrayEditableData(testUtils.getSampleDataB());
        try {
            instanceB.insert(20, 1);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testInsert_byteArray() {
        ByteArrayEditableData instanceA = new ByteArrayEditableData(testUtils.getSampleDataA());
        instanceA.insert(1, new byte[]{1, 2});
        assertEquals(7l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x1, instanceA.getByte(1));
        assertEquals((byte) 0x2, instanceA.getByte(2));
        assertEquals((byte) 0x34, instanceA.getByte(3));
        assertEquals((byte) 0x56, instanceA.getByte(4));

        ByteArrayEditableData instanceB = new ByteArrayEditableData(testUtils.getSampleDataB());
        try {
            instanceB.insert(20, new byte[]{1});
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testInsert_BinaryData() {
        ByteArrayEditableData instanceA = new ByteArrayEditableData(testUtils.getSampleDataA());
        instanceA.insert(1, new ByteArrayData(new byte[]{1, 2}));
        assertEquals(7l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x1, instanceA.getByte(1));
        assertEquals((byte) 0x2, instanceA.getByte(2));
        assertEquals((byte) 0x34, instanceA.getByte(3));
        assertEquals((byte) 0x56, instanceA.getByte(4));

        ByteArrayEditableData instanceB = new ByteArrayEditableData(testUtils.getSampleDataB());
        try {
            instanceB.insert(20, new ByteArrayData(new byte[]{1}));
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testInsert() {
        ByteArrayEditableData instanceA = new ByteArrayEditableData(testUtils.getSampleDataA());
        instanceA.insert(1, new byte[]{5, 7, 1, 2, 10}, 2, 2);
        assertEquals(7l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x1, instanceA.getByte(1));
        assertEquals((byte) 0x2, instanceA.getByte(2));
        assertEquals((byte) 0x34, instanceA.getByte(3));
        assertEquals((byte) 0x56, instanceA.getByte(4));

        ByteArrayEditableData instanceB = new ByteArrayEditableData(testUtils.getSampleDataB());
        try {
            instanceB.insert(20, new byte[]{5, 7, 1, 2, 10}, 2, 1);
            fail();
        } catch (OutOfBoundsException ex) {
        }

        ByteArrayEditableData instanceB2 = new ByteArrayEditableData(testUtils.getSampleDataB());
        try {
            instanceB2.insert(0, new byte[]{5, 7, 1, 2, 10}, 10, 1);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testFillData() {
        ByteArrayEditableData instanceA = new ByteArrayEditableData(testUtils.getSampleDataA());
        instanceA.fillData(1, 2);
        assertEquals(5l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x0, instanceA.getByte(1));
        assertEquals((byte) 0x0, instanceA.getByte(2));
        assertEquals((byte) 0x78, instanceA.getByte(3));
        assertEquals((byte) 0x9a, instanceA.getByte(4));

        ByteArrayEditableData instanceB = new ByteArrayEditableData(testUtils.getSampleDataB());
        try {
            instanceB.fillData(20, 1);
            fail();
        } catch (OutOfBoundsException ex) {
        }

        ByteArrayEditableData instanceB2 = new ByteArrayEditableData(testUtils.getSampleDataB());
        try {
            instanceB2.fillData(0, 20);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testFillData_byte() {
        ByteArrayEditableData instanceA = new ByteArrayEditableData(testUtils.getSampleDataA());
        instanceA.fillData(1, 2, (byte) 0x55);
        assertEquals(5l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x55, instanceA.getByte(1));
        assertEquals((byte) 0x55, instanceA.getByte(2));
        assertEquals((byte) 0x78, instanceA.getByte(3));
        assertEquals((byte) 0x9a, instanceA.getByte(4));

        ByteArrayEditableData instanceB = new ByteArrayEditableData(testUtils.getSampleDataB());
        try {
            instanceB.fillData(20, 1, (byte) 55);
            fail();
        } catch (OutOfBoundsException ex) {
        }

        ByteArrayEditableData instanceB2 = new ByteArrayEditableData(testUtils.getSampleDataB());
        try {
            instanceB2.fillData(0, 20, (byte) 55);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testReplace_BinaryData() {
        ByteArrayEditableData instanceA = new ByteArrayEditableData(testUtils.getSampleDataA());
        instanceA.replace(1, new ByteArrayData(new byte[]{1, 2}));
        assertEquals(5l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x1, instanceA.getByte(1));
        assertEquals((byte) 0x2, instanceA.getByte(2));
        assertEquals((byte) 0x78, instanceA.getByte(3));
        assertEquals((byte) 0x9a, instanceA.getByte(4));

        ByteArrayEditableData instanceB = new ByteArrayEditableData(testUtils.getSampleDataB());
        try {
            instanceB.replace(20, new ByteArrayData(new byte[]{1}));
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testReplace_BinaryData_offset() {
        ByteArrayEditableData instanceA = new ByteArrayEditableData(testUtils.getSampleDataA());
        instanceA.replace(1, new ByteArrayData(new byte[]{5, 7, 1, 2, 10}), 2, 2);
        assertEquals(5l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x1, instanceA.getByte(1));
        assertEquals((byte) 0x2, instanceA.getByte(2));
        assertEquals((byte) 0x78, instanceA.getByte(3));
        assertEquals((byte) 0x9a, instanceA.getByte(4));

        ByteArrayEditableData instanceB = new ByteArrayEditableData(testUtils.getSampleDataB());
        try {
            instanceB.replace(20, new ByteArrayData(new byte[]{5, 7, 1, 2, 10}), 2, 2);
            fail();
        } catch (OutOfBoundsException ex) {
        }

        ByteArrayEditableData instanceB2 = new ByteArrayEditableData(testUtils.getSampleDataB());
        try {
            instanceB2.replace(0, new ByteArrayData(new byte[]{5, 7, 1, 2, 10}), 10, 2);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testReplace_ByteArray() {
        ByteArrayEditableData instanceA = new ByteArrayEditableData(testUtils.getSampleDataA());
        instanceA.replace(1, new byte[]{1, 2});
        assertEquals(5l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x1, instanceA.getByte(1));
        assertEquals((byte) 0x2, instanceA.getByte(2));
        assertEquals((byte) 0x78, instanceA.getByte(3));
        assertEquals((byte) 0x9a, instanceA.getByte(4));

        ByteArrayEditableData instanceB = new ByteArrayEditableData(testUtils.getSampleDataB());
        try {
            instanceB.replace(20, new byte[]{1});
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testReplace_ByteArray_offset() {
        ByteArrayEditableData instanceA = new ByteArrayEditableData(testUtils.getSampleDataA());
        instanceA.replace(1, new byte[]{5, 7, 1, 2, 10}, 2, 2);
        assertEquals(5l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x1, instanceA.getByte(1));
        assertEquals((byte) 0x2, instanceA.getByte(2));
        assertEquals((byte) 0x78, instanceA.getByte(3));
        assertEquals((byte) 0x9a, instanceA.getByte(4));

        ByteArrayEditableData instanceB = new ByteArrayEditableData(testUtils.getSampleDataB());
        try {
            instanceB.replace(20, new byte[]{5, 7, 1, 2, 10}, 2, 2);
            fail();
        } catch (OutOfBoundsException ex) {
        }

        ByteArrayEditableData instanceB2 = new ByteArrayEditableData(testUtils.getSampleDataB());
        try {
            instanceB2.replace(0, new byte[]{5, 7, 1, 2, 10}, 10, 2);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testRemove() {
        ByteArrayEditableData instanceA = new ByteArrayEditableData(testUtils.getSampleDataA());
        instanceA.remove(1, 2);
        assertEquals(3l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x78, instanceA.getByte(1));
        assertEquals((byte) 0x9a, instanceA.getByte(2));

        ByteArrayEditableData instanceB = new ByteArrayEditableData(testUtils.getSampleDataB());
        try {
            instanceB.remove(20, 1);
            fail();
        } catch (OutOfBoundsException ex) {
        }

        ByteArrayEditableData instanceB2 = new ByteArrayEditableData(testUtils.getSampleDataB());
        try {
            instanceB2.remove(5, 100);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testClear() {
        ByteArrayEditableData instanceA = new ByteArrayEditableData(testUtils.getSampleDataA());
        instanceA.clear();
        assertEquals(0l, instanceA.getDataSize());
    }

    @Test
    public void testLoadFromStream() throws Exception {
        ByteArrayEditableData instanceA = new ByteArrayEditableData();
        try (InputStream streamA = testUtils.getSampleDataStream(TestUtils.SAMPLE_5BYTES)) {
            instanceA.loadFromStream(streamA);
        }
        assertEquals(5l, instanceA.getDataSize());
        assertEquals(0x12, instanceA.getByte(0));
        assertEquals(0x9a, instanceA.getByte(4) & 0xff);

        ByteArrayEditableData instanceB = new ByteArrayEditableData();
        try (InputStream streamB = testUtils.getSampleDataStream(TestUtils.SAMPLE_10BYTES)) {
            instanceB.loadFromStream(streamB);
        }
        assertEquals(10l, instanceB.getDataSize());
        assertEquals(0x41, instanceB.getByte(0));
        assertEquals(0x4a, instanceB.getByte(9));

        ByteArrayEditableData instanceC = new ByteArrayEditableData();
        try (InputStream streamC = testUtils.getSampleDataStream(TestUtils.SAMPLE_ALLBYTES)) {
            instanceC.loadFromStream(streamC);
        }
        assertEquals(256l, instanceC.getDataSize());
        assertEquals(0x0, instanceC.getByte(0));
        assertEquals(0x7f, instanceC.getByte(0x7f) & 0xff);
        assertEquals(0xff, instanceC.getByte(0xff) & 0xff);
    }

    @Test
    public void testLoadFromStream_offset() throws Exception {
        ByteArrayEditableData instanceA = new ByteArrayEditableData();
        try (InputStream streamA = testUtils.getSampleDataStream(TestUtils.SAMPLE_ALLBYTES)) {
            instanceA.insert(100, streamA, 100);
        }
        assertEquals(200l, instanceA.getDataSize());
        assertEquals((byte) 0, instanceA.getByte(100));
        assertEquals((byte) 1, instanceA.getByte(101));
        assertEquals((byte) 99, instanceA.getByte(199));
    }
}
