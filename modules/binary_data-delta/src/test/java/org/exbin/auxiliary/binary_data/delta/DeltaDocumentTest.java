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
package org.exbin.auxiliary.binary_data.delta;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.auxiliary.binary_data.BinaryData;
import org.exbin.auxiliary.binary_data.array.paged.ByteArrayPagedData;
import org.exbin.auxiliary.binary_data.delta.file.FileDataSource;
import org.exbin.auxiliary.binary_data.delta.list.DefaultDoublyLinkedList;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for delta document.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class DeltaDocumentTest {

    public static final String SAMPLE_FILES_PATH = "/org/exbin/auxiliary/binary_data/delta/resources/test/";
    public static final String SAMPLE_ALLBYTES = SAMPLE_FILES_PATH + "allbytes.dat";
    public static final int SAMPLE_ALLBYTES_SIZE = 256;

    public DeltaDocumentTest() {
    }

    @Test
    public void testOpenDocument() {
        DeltaDocument document = openDeltaDocument();
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE, document.getDataSize());

        DefaultDoublyLinkedList<DataSegment> segments = document.getSegments();
        Assert.assertEquals(1, segments.size());

        DataSegment segment0 = segments.first();
        Assert.assertTrue(segment0 instanceof SourceSegment);
        Assert.assertEquals(0, ((SourceSegment) segment0).getStartPosition());
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE, segment0.getLength());

        document.validate();
        document.clear();
        Assert.assertEquals(0, document.getSegments().size());
        document.dispose();
    }

    @Test
    public void testSetBytes() {
        DeltaDocument document = openDeltaDocument();
        document.setByte(10, (byte) 0);
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE, document.getDataSize());

        DefaultDoublyLinkedList<DataSegment> segments = document.getSegments();
        Assert.assertEquals(3, segments.size());

        DataSegment segment0 = segments.first();
        Assert.assertTrue(segment0 instanceof SourceSegment);
        Assert.assertEquals(0, ((SourceSegment) segment0).getStartPosition());
        Assert.assertEquals(10, segment0.getLength());

        DataSegment segment1 = segments.get(1);
        Assert.assertTrue(segment1 instanceof MemorySegment);
        Assert.assertEquals(0, ((MemorySegment) segment1).getStartPosition());
        Assert.assertEquals(1, segment1.getLength());

        DataSegment segment2 = segments.get(2);
        Assert.assertTrue(segment2 instanceof SourceSegment);
        Assert.assertEquals(11, ((SourceSegment) segment2).getStartPosition());
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE - 11, segment2.getLength());

        document.validatePointerPosition();
        document.clear();
        Assert.assertEquals(0, document.getSegments().size());
        document.dispose();
    }

    @Test
    public void testInsertBinaryData() {
        DeltaDocument document = openDeltaDocument();
        ByteArrayPagedData memoryData = new ByteArrayPagedData();
        memoryData.insert(0, new byte[] {0});
        BinaryData data = new MemoryDataSource(memoryData);
        document.insert(10, data);
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE + 1, document.getDataSize());

        DefaultDoublyLinkedList<DataSegment> segments = document.getSegments();
        Assert.assertEquals(3, segments.size());

        DataSegment segment0 = segments.first();
        Assert.assertTrue(segment0 instanceof SourceSegment);
        Assert.assertEquals(0, ((SourceSegment) segment0).getStartPosition());
        Assert.assertEquals(10, segment0.getLength());

        DataSegment segment1 = segments.get(1);
        Assert.assertTrue(segment1 instanceof MemorySegment);
        Assert.assertEquals(0, ((MemorySegment) segment1).getStartPosition());
        Assert.assertEquals(1, segment1.getLength());

        DataSegment segment2 = segments.get(2);
        Assert.assertTrue(segment2 instanceof SourceSegment);
        Assert.assertEquals(10, ((SourceSegment) segment2).getStartPosition());
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE - 10, segment2.getLength());

        document.validatePointerPosition();
        document.clear();
        Assert.assertEquals(0, document.getSegments().size());
    }

    @Nonnull
    public static DeltaDocument openDeltaDocument() {
        SegmentsRepository segmentsRepository = new SegmentsRepository(() -> new ByteArrayPagedData());
        try {
            FileDataSource dataSource = new FileDataSource(new File(DeltaDocumentTest.class.getResource(SAMPLE_ALLBYTES).getFile()));
            segmentsRepository.addDataSource(dataSource);
            return segmentsRepository.createDocument(dataSource);
        } catch (IOException ex) {
            Logger.getLogger(DeltaDocumentTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail();
            throw new RuntimeException(ex);
        }
    }
}
