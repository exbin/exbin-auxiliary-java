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
import java.io.InputStream;
import javax.annotation.Nonnull;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests for PagedData class.
 *
 * @version 0.1.3 2017/05/26
 * @author ExBin Project (http://exbin.org)
 */
public class PagedDataTest {

    @Nonnull
    private final TestUtils testUtils = new TestUtils();

    public PagedDataTest() {
    }

    @Test
    public void testGetDataSize() {
        PagedData instanceA = new PagedData();
        instanceA.insert(0, testUtils.getSampleDataA());
        assertEquals(5l, instanceA.getDataSize());

        PagedData instanceA1 = new PagedData(8);
        instanceA1.insert(0, testUtils.getSampleDataA());
        assertEquals(5l, instanceA1.getDataSize());

        PagedData instanceB = new PagedData(8);
        instanceB.insert(0, testUtils.getSampleDataB());
        assertEquals(10l, instanceB.getDataSize());

        PagedData instanceC = new PagedData(8);
        instanceC.insert(0, testUtils.getSampleDataC());
        assertEquals(256l, instanceC.getDataSize());
    }

    @Test
    public void testIsEmpty() {
        PagedData instanceA = new PagedData(8);
        instanceA.insert(0, testUtils.getSampleDataA());
        assertEquals(false, instanceA.isEmpty());

        PagedData instanceB = new PagedData(8);
        instanceB.insert(0, testUtils.getSampleDataB());
        assertEquals(false, instanceB.isEmpty());

        PagedData instanceC = new PagedData(8);
        instanceC.insert(0, testUtils.getSampleDataC());
        assertEquals(false, instanceC.isEmpty());

        PagedData instanceD = new PagedData(8);
        assertEquals(true, instanceD.isEmpty());

        PagedData instanceE = new PagedData(8);
        instanceE.insert(0, new byte[0]);
        assertEquals(true, instanceE.isEmpty());
    }

    @Test
    public void testGetByte() {
        PagedData instanceA = new PagedData(8);
        instanceA.insert(0, testUtils.getSampleDataA());
        assertEquals(0x12, instanceA.getByte(0));
        assertEquals(0x9a, instanceA.getByte(4) & 0xff);

        PagedData instanceB = new PagedData(8);
        instanceB.insert(0, testUtils.getSampleDataB());
        assertEquals(0x41, instanceB.getByte(0));
        assertEquals(0x4a, instanceB.getByte(9));

        PagedData instanceC = new PagedData(8);
        instanceC.insert(0, testUtils.getSampleDataC());
        assertEquals(0x0, instanceC.getByte(0));
        assertEquals(0x7f, instanceC.getByte(0x7f) & 0xff);
        assertEquals(0xff, instanceC.getByte(0xff) & 0xff);
    }

    @Test
    public void testCopy() {
        PagedData instanceA = new PagedData(8);
        instanceA.insert(0, testUtils.getSampleDataA());
        BinaryData copyA = instanceA.copy();
        assertEquals(5l, copyA.getDataSize());

        PagedData instanceB = new PagedData(8);
        instanceB.insert(0, testUtils.getSampleDataB());
        BinaryData copyB = instanceB.copy();
        assertEquals(10l, copyB.getDataSize());

        PagedData instanceC = new PagedData(8);
        instanceC.insert(0, testUtils.getSampleDataC());
        BinaryData copyC = instanceC.copy();
        assertEquals(256l, copyC.getDataSize());
    }

    @Test
    public void testCopy_StartLength() {
        PagedData instanceA = new PagedData(8);
        instanceA.insert(0, testUtils.getSampleDataA());
        BinaryData copyA = instanceA.copy(1, 2);
        assertEquals(2l, copyA.getDataSize());
        assertEquals(0x34, copyA.getByte(0));

        PagedData instanceB = new PagedData(8);
        instanceB.insert(0, testUtils.getSampleDataB());
        BinaryData copyB = instanceB.copy(2, 6);
        assertEquals(6l, copyB.getDataSize());
        assertEquals(0x43, copyB.getByte(0));

        PagedData instanceC = new PagedData(8);
        instanceC.insert(0, testUtils.getSampleDataC());
        BinaryData copyC = instanceC.copy(100, 100);
        assertEquals(100l, copyC.getDataSize());
        assertEquals((byte) 100, copyC.getByte(0));

        try {
            instanceC.copy(100, 200);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testCopyToArray() {
        PagedData instanceA = new PagedData(8);
        instanceA.insert(0, testUtils.getSampleDataA());
        byte[] copyA = new byte[2];
        instanceA.copyToArray(1, copyA, 0, 2);
        assertEquals(0x34, copyA[0]);

        PagedData instanceB = new PagedData(8);
        instanceB.insert(0, testUtils.getSampleDataB());
        byte[] copyB = new byte[8];
        instanceB.copyToArray(2, copyB, 2, 6);
        assertEquals((byte) 0, copyB[0]);
        assertEquals(0x43, copyB[2]);

        PagedData instanceC = new PagedData(8);
        instanceC.insert(0, testUtils.getSampleDataC());
        byte[] copyC = new byte[100];
        instanceC.copyToArray(100, copyC, 0, 100);
        assertEquals((byte) 100, copyC[0]);
        assertEquals((byte) 199, copyC[99]);

        byte[] copyC2 = new byte[100];
        try {
            instanceC.copyToArray(100, copyC2, 50, 100);
            fail();
        } catch (OutOfBoundsException ex) {
        }

        try {
            instanceC.copyToArray(200, copyC2, 0, 100);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testSaveToStream() throws Exception {
        ByteArrayOutputStream outputA = new ByteArrayOutputStream();
        PagedData instanceA = new PagedData(8);
        instanceA.insert(0, testUtils.getSampleDataA());
        instanceA.saveToStream(outputA);
        outputA.close();
        assertArrayEquals(testUtils.getSampleDataA(), outputA.toByteArray());

        ByteArrayOutputStream outputB = new ByteArrayOutputStream();
        PagedData instanceB = new PagedData(8);
        instanceB.insert(0, testUtils.getSampleDataB());
        instanceB.saveToStream(outputB);
        outputB.close();
        assertArrayEquals(testUtils.getSampleDataB(), outputB.toByteArray());

        ByteArrayOutputStream outputC = new ByteArrayOutputStream();
        PagedData instanceC = new PagedData(8);
        instanceC.insert(0, testUtils.getSampleDataC());
        instanceC.saveToStream(outputC);
        outputC.close();
        assertArrayEquals(testUtils.getSampleDataC(), outputC.toByteArray());
    }

    @Test
    public void testSetDataSize() {
        PagedData instanceA = new PagedData(8);
        instanceA.insert(0, testUtils.getSampleDataA());
        instanceA.setDataSize(10);
        assertEquals(10l, instanceA.getDataSize());

        PagedData instanceA1 = new PagedData(8);
        instanceA1.insert(0, testUtils.getSampleDataA());
        instanceA1.setDataSize(4);
        assertEquals(4l, instanceA1.getDataSize());
    }

    @Test
    public void testSetByte() {
        PagedData instanceA = new PagedData(8);
        instanceA.insert(0, testUtils.getSampleDataA());
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
        PagedData instanceA = new PagedData(8);
        instanceA.insert(0, testUtils.getSampleDataA());
        instanceA.insertUninitialized(1, 2);
        assertEquals(7l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x34, instanceA.getByte(3));
        assertEquals((byte) 0x56, instanceA.getByte(4));

        PagedData instanceB = new PagedData(8);
        instanceB.insert(0, testUtils.getSampleDataB());
        try {
            instanceB.insertUninitialized(20, 1);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testInsertLength() {
        PagedData instanceA = new PagedData(8);
        instanceA.insert(0, testUtils.getSampleDataA());
        instanceA.insert(1, 2);
        assertEquals(7l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x0, instanceA.getByte(1));
        assertEquals((byte) 0x0, instanceA.getByte(2));
        assertEquals((byte) 0x34, instanceA.getByte(3));
        assertEquals((byte) 0x56, instanceA.getByte(4));

        PagedData instanceB = new PagedData(8);
        instanceB.insert(0, testUtils.getSampleDataB());
        try {
            instanceB.insert(20, 1);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testInsert_byteArray() {
        PagedData instanceA = new PagedData(8);
        instanceA.insert(0, testUtils.getSampleDataA());
        instanceA.insert(1, new byte[]{1, 2});
        assertEquals(7l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x1, instanceA.getByte(1));
        assertEquals((byte) 0x2, instanceA.getByte(2));
        assertEquals((byte) 0x34, instanceA.getByte(3));
        assertEquals((byte) 0x56, instanceA.getByte(4));

        PagedData instanceB = new PagedData(8);
        instanceB.insert(0, testUtils.getSampleDataB());
        try {
            instanceB.insert(20, new byte[]{1});
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testInsert_BinaryData() {
        PagedData instanceA = new PagedData(8);
        instanceA.insert(0, testUtils.getSampleDataA());
        instanceA.insert(1, new ByteArrayData(new byte[]{1, 2}));
        assertEquals(7l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x1, instanceA.getByte(1));
        assertEquals((byte) 0x2, instanceA.getByte(2));
        assertEquals((byte) 0x34, instanceA.getByte(3));
        assertEquals((byte) 0x56, instanceA.getByte(4));

        PagedData instanceB = new PagedData(8);
        instanceB.insert(0, testUtils.getSampleDataB());
        try {
            instanceB.insert(20, new ByteArrayData(new byte[]{1}));
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testInsert() {
        PagedData instanceA = new PagedData(8);
        instanceA.insert(0, testUtils.getSampleDataA());
        instanceA.insert(1, new byte[]{5, 7, 1, 2, 10}, 2, 2);
        assertEquals(7l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x1, instanceA.getByte(1));
        assertEquals((byte) 0x2, instanceA.getByte(2));
        assertEquals((byte) 0x34, instanceA.getByte(3));
        assertEquals((byte) 0x56, instanceA.getByte(4));

        PagedData instanceB = new PagedData(8);
        instanceB.insert(0, testUtils.getSampleDataB());
        try {
            instanceB.insert(20, new byte[]{5, 7, 1, 2, 10}, 2, 1);
            fail();
        } catch (OutOfBoundsException ex) {
        }

        PagedData instanceB2 = new PagedData(8);
        instanceB2.insert(0, testUtils.getSampleDataB());
        try {
            instanceB2.insert(0, new byte[]{5, 7, 1, 2, 10}, 10, 1);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testFillData() {
        PagedData instanceA = new PagedData(8);
        instanceA.insert(0, testUtils.getSampleDataA());
        instanceA.fillData(1, 2);
        assertEquals(5l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x0, instanceA.getByte(1));
        assertEquals((byte) 0x0, instanceA.getByte(2));
        assertEquals((byte) 0x78, instanceA.getByte(3));
        assertEquals((byte) 0x9a, instanceA.getByte(4));

        PagedData instanceB = new PagedData(8);
        instanceB.insert(0, testUtils.getSampleDataB());
        try {
            instanceB.fillData(20, 1);
            fail();
        } catch (OutOfBoundsException ex) {
        }

        PagedData instanceB2 = new PagedData(8);
        instanceB2.insert(0, testUtils.getSampleDataB());
        try {
            instanceB2.fillData(0, 20);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testFillData_byte() {
        PagedData instanceA = new PagedData(8);
        instanceA.insert(0, testUtils.getSampleDataA());
        instanceA.fillData(1, 2, (byte) 0x55);
        assertEquals(5l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x55, instanceA.getByte(1));
        assertEquals((byte) 0x55, instanceA.getByte(2));
        assertEquals((byte) 0x78, instanceA.getByte(3));
        assertEquals((byte) 0x9a, instanceA.getByte(4));

        PagedData instanceB = new PagedData(8);
        instanceB.insert(0, testUtils.getSampleDataB());
        try {
            instanceB.fillData(20, 1, (byte) 55);
            fail();
        } catch (OutOfBoundsException ex) {
        }

        PagedData instanceB2 = new PagedData(8);
        instanceB2.insert(0, testUtils.getSampleDataB());
        try {
            instanceB2.fillData(0, 20, (byte) 55);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testReplace_BinaryData() {
        PagedData instanceA = new PagedData(8);
        instanceA.insert(0, testUtils.getSampleDataA());
        instanceA.replace(1, new ByteArrayData(new byte[]{1, 2}));
        assertEquals(5l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x1, instanceA.getByte(1));
        assertEquals((byte) 0x2, instanceA.getByte(2));
        assertEquals((byte) 0x78, instanceA.getByte(3));
        assertEquals((byte) 0x9a, instanceA.getByte(4));

        PagedData instanceB = new PagedData(8);
        instanceB.insert(0, testUtils.getSampleDataB());
        try {
            instanceB.replace(20, new ByteArrayData(new byte[]{1}));
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testReplace_BinaryData_offset() {
        PagedData instanceA = new PagedData(8);
        instanceA.insert(0, testUtils.getSampleDataA());
        instanceA.replace(1, new ByteArrayData(new byte[]{5, 7, 1, 2, 10}), 2, 2);
        assertEquals(5l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x1, instanceA.getByte(1));
        assertEquals((byte) 0x2, instanceA.getByte(2));
        assertEquals((byte) 0x78, instanceA.getByte(3));
        assertEquals((byte) 0x9a, instanceA.getByte(4));

        PagedData instanceB = new PagedData(8);
        instanceB.insert(0, testUtils.getSampleDataB());
        try {
            instanceB.replace(20, new ByteArrayData(new byte[]{5, 7, 1, 2, 10}), 2, 2);
            fail();
        } catch (OutOfBoundsException ex) {
        }

        PagedData instanceB2 = new PagedData(8);
        instanceB2.insert(0, testUtils.getSampleDataB());
        try {
            instanceB2.replace(0, new ByteArrayData(new byte[]{5, 7, 1, 2, 10}), 10, 2);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testReplace_ByteArray() {
        PagedData instanceA = new PagedData(8);
        instanceA.insert(0, testUtils.getSampleDataA());
        instanceA.replace(1, new byte[]{1, 2});
        assertEquals(5l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x1, instanceA.getByte(1));
        assertEquals((byte) 0x2, instanceA.getByte(2));
        assertEquals((byte) 0x78, instanceA.getByte(3));
        assertEquals((byte) 0x9a, instanceA.getByte(4));

        PagedData instanceB = new PagedData(8);
        instanceB.insert(0, testUtils.getSampleDataB());
        try {
            instanceB.replace(20, new byte[]{1});
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testReplace_ByteArray_offset() {
        PagedData instanceA = new PagedData(8);
        instanceA.insert(0, testUtils.getSampleDataA());
        instanceA.replace(1, new byte[]{5, 7, 1, 2, 10}, 2, 2);
        assertEquals(5l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x1, instanceA.getByte(1));
        assertEquals((byte) 0x2, instanceA.getByte(2));
        assertEquals((byte) 0x78, instanceA.getByte(3));
        assertEquals((byte) 0x9a, instanceA.getByte(4));

        PagedData instanceB = new PagedData(8);
        instanceB.insert(0, testUtils.getSampleDataB());
        try {
            instanceB.replace(20, new byte[]{5, 7, 1, 2, 10}, 2, 2);
            fail();
        } catch (OutOfBoundsException ex) {
        }

        PagedData instanceB2 = new PagedData(8);
        instanceB2.insert(0, testUtils.getSampleDataB());
        try {
            instanceB2.replace(0, new byte[]{5, 7, 1, 2, 10}, 10, 2);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testRemove() {
        PagedData instanceA = new PagedData(8);
        instanceA.insert(0, testUtils.getSampleDataA());
        instanceA.remove(1, 2);
        assertEquals(3l, instanceA.getDataSize());
        assertEquals((byte) 0x12, instanceA.getByte(0));
        assertEquals((byte) 0x78, instanceA.getByte(1));
        assertEquals((byte) 0x9a, instanceA.getByte(2));

        PagedData instanceB = new PagedData(8);
        instanceB.insert(0, testUtils.getSampleDataB());
        try {
            instanceB.remove(20, 1);
            fail();
        } catch (OutOfBoundsException ex) {
        }

        PagedData instanceB2 = new PagedData(8);
        instanceB2.insert(0, testUtils.getSampleDataB());
        try {
            instanceB2.remove(5, 100);
            fail();
        } catch (OutOfBoundsException ex) {
        }
    }

    @Test
    public void testClear() {
        PagedData instanceA = new PagedData(8);
        instanceA.insert(0, testUtils.getSampleDataA());
        instanceA.clear();
        assertEquals(0l, instanceA.getDataSize());
    }

    @Test
    public void testLoadFromStream() throws Exception {
        PagedData instanceA = new PagedData();
        try (InputStream streamA = testUtils.getSampleDataStream(TestUtils.SAMPLE_5BYTES)) {
            instanceA.loadFromStream(streamA);
        }
        assertEquals(5l, instanceA.getDataSize());
        assertEquals(0x12, instanceA.getByte(0));
        assertEquals(0x9a, instanceA.getByte(4) & 0xff);

        PagedData instanceB = new PagedData();
        try (InputStream streamB = testUtils.getSampleDataStream(TestUtils.SAMPLE_10BYTES)) {
            instanceB.loadFromStream(streamB);
        }
        assertEquals(10l, instanceB.getDataSize());
        assertEquals(0x41, instanceB.getByte(0));
        assertEquals(0x4a, instanceB.getByte(9));

        PagedData instanceC = new PagedData();
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
        PagedData instanceA = new PagedData();
        try (InputStream streamA = testUtils.getSampleDataStream(TestUtils.SAMPLE_ALLBYTES)) {
            instanceA.insert(100, streamA, 100);
        }
        assertEquals(200l, instanceA.getDataSize());
        assertEquals((byte) 0, instanceA.getByte(100));
        assertEquals((byte) 1, instanceA.getByte(101));
        assertEquals((byte) 99, instanceA.getByte(199));
    }
}
