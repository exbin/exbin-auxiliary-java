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
package org.exbin.auxiliary.binary_data.buffer;

import java.io.ByteArrayOutputStream;
import javax.annotation.Nonnull;
import org.exbin.auxiliary.binary_data.BinaryData;
import org.exbin.auxiliary.binary_data.OutOfBoundsException;
import org.exbin.auxiliary.binary_data.TestUtils;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests for BufferData class.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class BufferDataTest {

    @Nonnull
    private final TestUtils testUtils = new TestUtils();

    public BufferDataTest() {
    }

    @Test
    public void testGetDataSize() {
        BufferData instanceA = new BufferData(testUtils.getSampleDataA());
        assertEquals(5l, instanceA.getDataSize());

        BufferData instanceB = new BufferData(testUtils.getSampleDataB());
        assertEquals(10l, instanceB.getDataSize());

        BufferData instanceC = new BufferData(testUtils.getSampleDataC());
        assertEquals(256l, instanceC.getDataSize());
    }

    @Test
    public void testIsEmpty() {
        BufferData instanceA = new BufferData(testUtils.getSampleDataA());
        assertEquals(false, instanceA.isEmpty());

        BufferData instanceB = new BufferData(testUtils.getSampleDataB());
        assertEquals(false, instanceB.isEmpty());

        BufferData instanceC = new BufferData(testUtils.getSampleDataC());
        assertEquals(false, instanceC.isEmpty());

        BufferData instanceD = new BufferData();
        assertEquals(true, instanceD.isEmpty());

        BufferData instanceE = new BufferData(new byte[0]);
        assertEquals(true, instanceE.isEmpty());
    }

    @Test
    public void testGetByte() {
        BufferData instanceA = new BufferData(testUtils.getSampleDataA());
        assertEquals(0x12, instanceA.getByte(0));
        assertEquals(0x9a, instanceA.getByte(4) & 0xff);

        BufferData instanceB = new BufferData(testUtils.getSampleDataB());
        assertEquals(0x41, instanceB.getByte(0));
        assertEquals(0x4a, instanceB.getByte(9));

        BufferData instanceC = new BufferData(testUtils.getSampleDataC());
        assertEquals(0x0, instanceC.getByte(0));
        assertEquals(0x7f, instanceC.getByte(0x7f) & 0xff);
        assertEquals(0xff, instanceC.getByte(0xff) & 0xff);
    }

    @Test
    public void testCopy_0args() {
        BufferData instanceA = new BufferData(testUtils.getSampleDataA());
        BinaryData copyA = instanceA.copy();
        assertEquals(5l, copyA.getDataSize());

        BufferData instanceB = new BufferData(testUtils.getSampleDataB());
        BinaryData copyB = instanceB.copy();
        assertEquals(10l, copyB.getDataSize());

        BufferData instanceC = new BufferData(testUtils.getSampleDataC());
        BinaryData copyC = instanceC.copy();
        assertEquals(256l, copyC.getDataSize());
    }

    @Test
    public void testCopy() {
        BufferData instanceA = new BufferData(testUtils.getSampleDataA());
        BinaryData copyA = instanceA.copy(1, 2);
        assertEquals(2l, copyA.getDataSize());
        assertEquals(0x34, copyA.getByte(0));

        BufferData instanceB = new BufferData(testUtils.getSampleDataB());
        BinaryData copyB = instanceB.copy(2, 6);
        assertEquals(6l, copyB.getDataSize());
        assertEquals(0x43, copyB.getByte(0));

        BufferData instanceC = new BufferData(testUtils.getSampleDataC());
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
        BufferData instanceA = new BufferData(testUtils.getSampleDataA());
        byte[] copyA = new byte[2];
        instanceA.copyToArray(1, copyA, 0, 2);
        assertEquals(0x34, copyA[0]);

        BufferData instanceB = new BufferData(testUtils.getSampleDataB());
        byte[] copyB = new byte[8];
        instanceB.copyToArray(2, copyB, 2, 6);
        assertEquals((byte) 0, copyB[0]);
        assertEquals(0x43, copyB[2]);

        BufferData instanceC = new BufferData(testUtils.getSampleDataC());
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
        BufferData instanceA = new BufferData(testUtils.getSampleDataA());
        instanceA.saveToStream(outputA);
        outputA.close();
        assertArrayEquals(testUtils.getSampleDataA(), outputA.toByteArray());

        ByteArrayOutputStream outputB = new ByteArrayOutputStream();
        BufferData instanceB = new BufferData(testUtils.getSampleDataB());
        instanceB.saveToStream(outputB);
        outputB.close();
        assertArrayEquals(testUtils.getSampleDataB(), outputB.toByteArray());

        ByteArrayOutputStream outputC = new ByteArrayOutputStream();
        BufferData instanceC = new BufferData(testUtils.getSampleDataC());
        instanceC.saveToStream(outputC);
        outputC.close();
        assertArrayEquals(testUtils.getSampleDataC(), outputC.toByteArray());
    }

    @Test
    public void testEquals() {
        BufferData instanceA = new BufferData(testUtils.getSampleDataA());

        BufferData instanceA1 = new BufferData(testUtils.getSampleDataA());

        assertTrue(instanceA.equals(instanceA1));

        BufferData instanceB = new BufferData(testUtils.getSampleDataB());

        assertFalse(instanceA.equals(instanceB));
    }
}
