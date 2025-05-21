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

import java.io.ByteArrayOutputStream;
import javax.annotation.Nonnull;
import org.exbin.auxiliary.binary_data.BinaryData;
import org.exbin.auxiliary.binary_data.OutOfBoundsException;
import org.exbin.auxiliary.binary_data.TestUtils;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests for JnaBufferData class.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class JnaBufferDataTest {

    @Nonnull
    private final TestUtils testUtils = new TestUtils();

    public JnaBufferDataTest() {
    }

    @Test
    public void testGetDataSize() {
        JnaBufferData instanceA = new JnaBufferData(testUtils.getSampleDataA());
        assertEquals(5l, instanceA.getDataSize());

        JnaBufferData instanceB = new JnaBufferData(testUtils.getSampleDataB());
        assertEquals(10l, instanceB.getDataSize());

        JnaBufferData instanceC = new JnaBufferData(testUtils.getSampleDataC());
        assertEquals(256l, instanceC.getDataSize());
    }

    @Test
    public void testIsEmpty() {
        JnaBufferData instanceA = new JnaBufferData(testUtils.getSampleDataA());
        assertEquals(false, instanceA.isEmpty());

        JnaBufferData instanceB = new JnaBufferData(testUtils.getSampleDataB());
        assertEquals(false, instanceB.isEmpty());

        JnaBufferData instanceC = new JnaBufferData(testUtils.getSampleDataC());
        assertEquals(false, instanceC.isEmpty());

        JnaBufferData instanceD = new JnaBufferData();
        assertEquals(true, instanceD.isEmpty());

        JnaBufferData instanceE = new JnaBufferData(new byte[0]);
        assertEquals(true, instanceE.isEmpty());
    }

    @Test
    public void testGetByte() {
        JnaBufferData instanceA = new JnaBufferData(testUtils.getSampleDataA());
        assertEquals(0x12, instanceA.getByte(0));
        assertEquals(0x9a, instanceA.getByte(4) & 0xff);

        JnaBufferData instanceB = new JnaBufferData(testUtils.getSampleDataB());
        assertEquals(0x41, instanceB.getByte(0));
        assertEquals(0x4a, instanceB.getByte(9));

        JnaBufferData instanceC = new JnaBufferData(testUtils.getSampleDataC());
        assertEquals(0x0, instanceC.getByte(0));
        assertEquals(0x7f, instanceC.getByte(0x7f) & 0xff);
        assertEquals(0xff, instanceC.getByte(0xff) & 0xff);
    }

    @Test
    public void testCopy_0args() {
        JnaBufferData instanceA = new JnaBufferData(testUtils.getSampleDataA());
        BinaryData copyA = instanceA.copy();
        assertEquals(5l, copyA.getDataSize());

        JnaBufferData instanceB = new JnaBufferData(testUtils.getSampleDataB());
        BinaryData copyB = instanceB.copy();
        assertEquals(10l, copyB.getDataSize());

        JnaBufferData instanceC = new JnaBufferData(testUtils.getSampleDataC());
        BinaryData copyC = instanceC.copy();
        assertEquals(256l, copyC.getDataSize());
    }

    @Test
    public void testCopy() {
        JnaBufferData instanceA = new JnaBufferData(testUtils.getSampleDataA());
        BinaryData copyA = instanceA.copy(1, 2);
        assertEquals(2l, copyA.getDataSize());
        assertEquals(0x34, copyA.getByte(0));

        JnaBufferData instanceB = new JnaBufferData(testUtils.getSampleDataB());
        BinaryData copyB = instanceB.copy(2, 6);
        assertEquals(6l, copyB.getDataSize());
        assertEquals(0x43, copyB.getByte(0));

        JnaBufferData instanceC = new JnaBufferData(testUtils.getSampleDataC());
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
        JnaBufferData instanceA = new JnaBufferData(testUtils.getSampleDataA());
        byte[] copyA = new byte[2];
        instanceA.copyToArray(1, copyA, 0, 2);
        assertEquals(0x34, copyA[0]);

        JnaBufferData instanceB = new JnaBufferData(testUtils.getSampleDataB());
        byte[] copyB = new byte[8];
        instanceB.copyToArray(2, copyB, 2, 6);
        assertEquals((byte) 0, copyB[0]);
        assertEquals(0x43, copyB[2]);

        JnaBufferData instanceC = new JnaBufferData(testUtils.getSampleDataC());
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
        JnaBufferData instanceA = new JnaBufferData(testUtils.getSampleDataA());
        instanceA.saveToStream(outputA);
        outputA.close();
        assertArrayEquals(testUtils.getSampleDataA(), outputA.toByteArray());

        ByteArrayOutputStream outputB = new ByteArrayOutputStream();
        JnaBufferData instanceB = new JnaBufferData(testUtils.getSampleDataB());
        instanceB.saveToStream(outputB);
        outputB.close();
        assertArrayEquals(testUtils.getSampleDataB(), outputB.toByteArray());

        ByteArrayOutputStream outputC = new ByteArrayOutputStream();
        JnaBufferData instanceC = new JnaBufferData(testUtils.getSampleDataC());
        instanceC.saveToStream(outputC);
        outputC.close();
        assertArrayEquals(testUtils.getSampleDataC(), outputC.toByteArray());
    }

    @Test
    public void testEquals() {
        JnaBufferData instanceA = new JnaBufferData(testUtils.getSampleDataA());

        JnaBufferData instanceA1 = new JnaBufferData(testUtils.getSampleDataA());

        assertTrue(instanceA.equals(instanceA1));

        JnaBufferData instanceB = new JnaBufferData(testUtils.getSampleDataB());

        assertFalse(instanceA.equals(instanceB));
    }
}
