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
package org.exbin.auxiliary.paged_data.delta;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.auxiliary.paged_data.BinaryData;
import org.exbin.auxiliary.paged_data.EditableBinaryData;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for delta document.
 *
 * @version 0.2.0 2018/04/27
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class DeltaDocumentSaveTest {

    public static final String SAMPLE_FILES_PATH = "/org/exbin/auxiliary/paged_data/delta/resources/test/";
    public static final String SAMPLE_ALLBYTES = SAMPLE_FILES_PATH + "allbytes.dat";
    public static final String SAMPLE_INSERTED_BEGIN = SAMPLE_FILES_PATH + "inserted_begin.dat";
    public static final String SAMPLE_INSERTED_MIDDLE = SAMPLE_FILES_PATH + "inserted_middle.dat";
    public static final String SAMPLE_INSERTED_END = SAMPLE_FILES_PATH + "inserted_end.dat";
    public static final String SAMPLE_OVERWRITTEN_BEGIN = SAMPLE_FILES_PATH + "overwritten_begin.dat";
    public static final String SAMPLE_OVERWRITTEN_MIDDLE = SAMPLE_FILES_PATH + "overwritten_middle.dat";
    public static final String SAMPLE_OVERWRITTEN_END = SAMPLE_FILES_PATH + "overwritten_end.dat";
    public static final String SAMPLE_REMOVED_BEGIN = SAMPLE_FILES_PATH + "removed_begin.dat";
    public static final String SAMPLE_REMOVED_MIDDLE = SAMPLE_FILES_PATH + "removed_middle.dat";
    public static final String SAMPLE_REMOVED_END = SAMPLE_FILES_PATH + "removed_end.dat";
    public static final String SAMPLE_SWAP_HALF = SAMPLE_FILES_PATH + "swaped_half.dat";
    public static final String SAMPLE_SWAP_MIDDLE = SAMPLE_FILES_PATH + "swaped_middle.dat";
    public static final String SAMPLE_SWAP_DOUBLE = SAMPLE_FILES_PATH + "swaped_double.dat";
    public static final String SAMPLE_SWAP_FIRST_QUARTER = SAMPLE_FILES_PATH + "swaped_first_quarter.dat";
    public static final String SAMPLE_SWAP_LAST_QUARTER = SAMPLE_FILES_PATH + "swaped_last_quarter.dat";
    public static final String SAMPLE_REVERSE = SAMPLE_FILES_PATH + "reversed.dat";
    public static final int SAMPLE_ALLBYTES_SIZE = 256;

    public DeltaDocumentSaveTest() {
    }

    @Test
    public void testSaveDocument() {
        DeltaDocument document = openTempDeltaDocument();
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE, document.getDataSize());

        try {
            document.save();
        } catch (IOException ex) {
            Logger.getLogger(DeltaDocumentSaveTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Exception: " + ex.getMessage());
        }

        document.clear();
        Assert.assertEquals(0, document.getSegments().size());
        closeTempDeltaDocument(document);
    }

    @Test
    public void testInsertBeginSaveDocument() {
        DeltaDocument document = openTempDeltaDocument();
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE, document.getDataSize());
        document.insert(0, new byte[]{0x40, 0x41});
        document.validatePointerPosition();

        try {
            document.save();

            InputStream comparisionFile;
            try (InputStream dataInputStream = document.getDataInputStream()) {
                comparisionFile = new FileInputStream(DeltaDocumentSaveTest.class.getResource(SAMPLE_INSERTED_BEGIN).getFile());
                TestUtils.assertEqualsInputStream(comparisionFile, dataInputStream);
            }
            comparisionFile.close();
        } catch (IOException ex) {
            Logger.getLogger(DeltaDocumentSaveTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Exception: " + ex.getMessage());
        }

        document.validatePointerPosition();
        document.clear();
        Assert.assertEquals(0, document.getSegments().size());
        closeTempDeltaDocument(document);
    }

    @Test
    public void testInsertMiddleSaveDocument() {
        DeltaDocument document = openTempDeltaDocument();
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE, document.getDataSize());
        document.insert(120, new byte[]{0x40, 0x41});
        document.validatePointerPosition();

        try {
            document.save();

            InputStream comparisionFile;
            try (InputStream dataInputStream = document.getDataInputStream()) {
                comparisionFile = new FileInputStream(DeltaDocumentSaveTest.class.getResource(SAMPLE_INSERTED_MIDDLE).getFile());
                TestUtils.assertEqualsInputStream(comparisionFile, dataInputStream);
            }
            comparisionFile.close();
        } catch (IOException ex) {
            Logger.getLogger(DeltaDocumentSaveTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Exception: " + ex.getMessage());
        }

        document.validatePointerPosition();
        document.clear();
        Assert.assertEquals(0, document.getSegments().size());
        closeTempDeltaDocument(document);
    }

    @Test
    public void testInsertEndSaveDocument() {
        DeltaDocument document = openTempDeltaDocument();
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE, document.getDataSize());
        document.insert(256, new byte[]{0x40, 0x41});
        document.validatePointerPosition();

        try {
            document.save();

            InputStream comparisionFile;
            try (InputStream dataInputStream = document.getDataInputStream()) {
                comparisionFile = new FileInputStream(DeltaDocumentSaveTest.class.getResource(SAMPLE_INSERTED_END).getFile());
                TestUtils.assertEqualsInputStream(comparisionFile, dataInputStream);
            }
            comparisionFile.close();
        } catch (IOException ex) {
            Logger.getLogger(DeltaDocumentSaveTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Exception: " + ex.getMessage());
        }

        document.validatePointerPosition();
        document.clear();
        Assert.assertEquals(0, document.getSegments().size());
        closeTempDeltaDocument(document);
    }

    @Test
    public void testInsertCopyBeginSaveDocument() {
        DeltaDocument document = openTempDeltaDocument();
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE, document.getDataSize());
        BinaryData copy = document.copy(0x40, 2);
        document.insert(0, copy);
        document.validatePointerPosition();
        copy.dispose();

        try {
            document.save();

            InputStream comparisionFile;
            try (InputStream dataInputStream = document.getDataInputStream()) {
                comparisionFile = new FileInputStream(DeltaDocumentSaveTest.class.getResource(SAMPLE_INSERTED_BEGIN).getFile());
                TestUtils.assertEqualsInputStream(comparisionFile, dataInputStream);
            }
            comparisionFile.close();
        } catch (IOException ex) {
            Logger.getLogger(DeltaDocumentSaveTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Exception: " + ex.getMessage());
        }

        document.validatePointerPosition();
        document.clear();
        Assert.assertEquals(0, document.getSegments().size());
        closeTempDeltaDocument(document);
    }

    @Test
    public void testInsertCopyMiddleSaveDocument() {
        DeltaDocument document = openTempDeltaDocument();
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE, document.getDataSize());
        BinaryData copy = document.copy(0x40, 2);
        document.insert(120, copy);
        document.validatePointerPosition();
        copy.dispose();

        try {
            document.save();

            InputStream comparisionFile;
            try (InputStream dataInputStream = document.getDataInputStream()) {
                comparisionFile = new FileInputStream(DeltaDocumentSaveTest.class.getResource(SAMPLE_INSERTED_MIDDLE).getFile());
                TestUtils.assertEqualsInputStream(comparisionFile, dataInputStream);
            }
            comparisionFile.close();
        } catch (IOException ex) {
            Logger.getLogger(DeltaDocumentSaveTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Exception: " + ex.getMessage());
        }

        document.validatePointerPosition();
        document.clear();
        Assert.assertEquals(0, document.getSegments().size());
        closeTempDeltaDocument(document);
    }

    @Test
    public void testInsertCopyEndSaveDocument() {
        DeltaDocument document = openTempDeltaDocument();
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE, document.getDataSize());
        BinaryData copy = document.copy(0x40, 2);
        document.insert(256, copy);
        document.validatePointerPosition();
        copy.dispose();

        try {
            document.save();

            InputStream comparisionFile;
            try (InputStream dataInputStream = document.getDataInputStream()) {
                comparisionFile = new FileInputStream(DeltaDocumentSaveTest.class.getResource(SAMPLE_INSERTED_END).getFile());
                TestUtils.assertEqualsInputStream(comparisionFile, dataInputStream);
            }
            comparisionFile.close();
        } catch (IOException ex) {
            Logger.getLogger(DeltaDocumentSaveTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Exception: " + ex.getMessage());
        }

        document.validatePointerPosition();
        document.clear();
        Assert.assertEquals(0, document.getSegments().size());
        closeTempDeltaDocument(document);
    }

    @Test
    public void testOverwriteBeginSaveDocument() {
        DeltaDocument document = openTempDeltaDocument();
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE, document.getDataSize());
        document.replace(0, new byte[]{0x40, 0x41});
        document.validatePointerPosition();

        try {
            document.save();

            InputStream comparisionFile;
            try (InputStream dataInputStream = document.getDataInputStream()) {
                comparisionFile = new FileInputStream(DeltaDocumentSaveTest.class.getResource(SAMPLE_OVERWRITTEN_BEGIN).getFile());
                TestUtils.assertEqualsInputStream(comparisionFile, dataInputStream);
            }
            comparisionFile.close();
        } catch (IOException ex) {
            Logger.getLogger(DeltaDocumentSaveTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Exception: " + ex.getMessage());
        }

        document.validatePointerPosition();
        document.clear();
        Assert.assertEquals(0, document.getSegments().size());
        closeTempDeltaDocument(document);
    }

    @Test
    public void testOverwriteMiddleSaveDocument() {
        DeltaDocument document = openTempDeltaDocument();
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE, document.getDataSize());
        document.replace(120, new byte[]{0x40, 0x41});
        document.validatePointerPosition();

        try {
            document.save();

            InputStream comparisionFile;
            try (InputStream dataInputStream = document.getDataInputStream()) {
                comparisionFile = new FileInputStream(DeltaDocumentSaveTest.class.getResource(SAMPLE_OVERWRITTEN_MIDDLE).getFile());
                TestUtils.assertEqualsInputStream(comparisionFile, dataInputStream);
            }
            comparisionFile.close();
        } catch (IOException ex) {
            Logger.getLogger(DeltaDocumentSaveTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Exception: " + ex.getMessage());
        }

        document.validatePointerPosition();
        document.clear();
        Assert.assertEquals(0, document.getSegments().size());
        closeTempDeltaDocument(document);
    }

    @Test
    public void testOverwriteEndSaveDocument() {
        DeltaDocument document = openTempDeltaDocument();
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE, document.getDataSize());
        document.replace(254, new byte[]{0x40, 0x41});
        document.validatePointerPosition();

        try {
            document.save();

            InputStream comparisionFile;
            try (InputStream dataInputStream = document.getDataInputStream()) {
                comparisionFile = new FileInputStream(DeltaDocumentSaveTest.class.getResource(SAMPLE_OVERWRITTEN_END).getFile());
                TestUtils.assertEqualsInputStream(comparisionFile, dataInputStream);
            }
            comparisionFile.close();
        } catch (IOException ex) {
            Logger.getLogger(DeltaDocumentSaveTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Exception: " + ex.getMessage());
        }

        document.validatePointerPosition();
        document.clear();
        Assert.assertEquals(0, document.getSegments().size());
        closeTempDeltaDocument(document);
    }

    @Test
    public void testOverwriteCopyBeginSaveDocument() {
        DeltaDocument document = openTempDeltaDocument();
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE, document.getDataSize());
        BinaryData copy = document.copy(0x40, 2);
        document.replace(0, copy);
        document.validatePointerPosition();
        copy.dispose();

        try {
            document.save();

            InputStream comparisionFile;
            try (InputStream dataInputStream = document.getDataInputStream()) {
                comparisionFile = new FileInputStream(DeltaDocumentSaveTest.class.getResource(SAMPLE_OVERWRITTEN_BEGIN).getFile());
                TestUtils.assertEqualsInputStream(comparisionFile, dataInputStream);
            }
            comparisionFile.close();
        } catch (IOException ex) {
            Logger.getLogger(DeltaDocumentSaveTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Exception: " + ex.getMessage());
        }

        document.validatePointerPosition();
        document.clear();
        Assert.assertEquals(0, document.getSegments().size());
        closeTempDeltaDocument(document);
    }

    @Test
    public void testOverwriteCopyMiddleSaveDocument() {
        DeltaDocument document = openTempDeltaDocument();
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE, document.getDataSize());
        BinaryData copy = document.copy(0x40, 2);
        document.replace(120, copy);
        document.validatePointerPosition();
        copy.dispose();

        try {
            document.save();

            InputStream comparisionFile;
            try (InputStream dataInputStream = document.getDataInputStream()) {
                comparisionFile = new FileInputStream(DeltaDocumentSaveTest.class.getResource(SAMPLE_OVERWRITTEN_MIDDLE).getFile());
                TestUtils.assertEqualsInputStream(comparisionFile, dataInputStream);
            }
            comparisionFile.close();
        } catch (IOException ex) {
            Logger.getLogger(DeltaDocumentSaveTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Exception: " + ex.getMessage());
        }

        document.validatePointerPosition();
        document.clear();
        Assert.assertEquals(0, document.getSegments().size());
        closeTempDeltaDocument(document);
    }

    @Test
    public void testOverwriteCopyEndSaveDocument() {
        DeltaDocument document = openTempDeltaDocument();
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE, document.getDataSize());
        BinaryData copy = document.copy(0x40, 2);
        document.replace(254, copy);
        document.validatePointerPosition();
        copy.dispose();

        try {
            document.save();

            InputStream comparisionFile;
            try (InputStream dataInputStream = document.getDataInputStream()) {
                comparisionFile = new FileInputStream(DeltaDocumentSaveTest.class.getResource(SAMPLE_OVERWRITTEN_END).getFile());
                TestUtils.assertEqualsInputStream(comparisionFile, dataInputStream);
            }
            comparisionFile.close();
        } catch (IOException ex) {
            Logger.getLogger(DeltaDocumentSaveTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Exception: " + ex.getMessage());
        }

        document.validatePointerPosition();
        document.clear();
        Assert.assertEquals(0, document.getSegments().size());
        closeTempDeltaDocument(document);
    }

    @Test
    public void testRemoveBeginSaveDocument() {
        DeltaDocument document = openTempDeltaDocument();
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE, document.getDataSize());
        document.remove(0, 2);
        document.validatePointerPosition();

        try {
            document.save();

            InputStream comparisionFile;
            try (InputStream dataInputStream = document.getDataInputStream()) {
                comparisionFile = new FileInputStream(DeltaDocumentSaveTest.class.getResource(SAMPLE_REMOVED_BEGIN).getFile());
                TestUtils.assertEqualsInputStream(comparisionFile, dataInputStream);
            }
            comparisionFile.close();
        } catch (IOException ex) {
            Logger.getLogger(DeltaDocumentSaveTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Exception: " + ex.getMessage());
        }

        document.validatePointerPosition();
        document.clear();
        Assert.assertEquals(0, document.getSegments().size());
        closeTempDeltaDocument(document);
    }

    @Test
    public void testRemoveMiddleSaveDocument() {
        DeltaDocument document = openTempDeltaDocument();
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE, document.getDataSize());
        document.remove(120, 2);
        document.validatePointerPosition();

        try {
            document.save();

            InputStream comparisionFile;
            try (InputStream dataInputStream = document.getDataInputStream()) {
                comparisionFile = new FileInputStream(DeltaDocumentSaveTest.class.getResource(SAMPLE_REMOVED_MIDDLE).getFile());
                TestUtils.assertEqualsInputStream(comparisionFile, dataInputStream);
            }
            comparisionFile.close();
        } catch (IOException ex) {
            Logger.getLogger(DeltaDocumentSaveTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Exception: " + ex.getMessage());
        }

        document.validatePointerPosition();
        document.clear();
        Assert.assertEquals(0, document.getSegments().size());
        closeTempDeltaDocument(document);
    }

    @Test
    public void testRemoveEndSaveDocument() {
        DeltaDocument document = openTempDeltaDocument();
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE, document.getDataSize());
        document.remove(254, 2);
        document.validatePointerPosition();

        try {
            document.save();

            InputStream comparisionFile;
            try (InputStream dataInputStream = document.getDataInputStream()) {
                comparisionFile = new FileInputStream(DeltaDocumentSaveTest.class.getResource(SAMPLE_REMOVED_END).getFile());
                TestUtils.assertEqualsInputStream(comparisionFile, dataInputStream);
            }
            comparisionFile.close();
        } catch (IOException ex) {
            Logger.getLogger(DeltaDocumentSaveTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Exception: " + ex.getMessage());
        }

        document.validatePointerPosition();
        document.clear();
        Assert.assertEquals(0, document.getSegments().size());
        closeTempDeltaDocument(document);
    }

    @Test
    public void testSwapHalfSaveDocument() {
        DeltaDocument document = openTempDeltaDocument();
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE, document.getDataSize());
        EditableBinaryData halfCopy = (EditableBinaryData) document.copy(0, 128);
        document.remove(0, 128);
        document.validatePointerPosition();
        document.insert(128, halfCopy);
        document.validatePointerPosition();
        halfCopy.dispose();

        try {
            document.save();

            InputStream comparisionFile;
            try (InputStream dataInputStream = document.getDataInputStream()) {
                comparisionFile = new FileInputStream(DeltaDocumentSaveTest.class.getResource(SAMPLE_SWAP_HALF).getFile());
                TestUtils.assertEqualsInputStream(comparisionFile, dataInputStream);
            }
            comparisionFile.close();
        } catch (IOException ex) {
            Logger.getLogger(DeltaDocumentSaveTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Exception: " + ex.getMessage());
        }

        document.validatePointerPosition();
        document.clear();
        Assert.assertEquals(0, document.getSegments().size());
        closeTempDeltaDocument(document);
    }

    @Test
    public void testSwapMiddleSaveDocument() {
        DeltaDocument document = openTempDeltaDocument();
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE, document.getDataSize());
        EditableBinaryData quarterCopy = (EditableBinaryData) document.copy(64, 64);
        document.remove(64, 64);
        document.validatePointerPosition();
        document.insert(128, quarterCopy);
        document.validatePointerPosition();
        quarterCopy.dispose();

        try {
            document.save();

            InputStream comparisionFile;
            try (InputStream dataInputStream = document.getDataInputStream()) {
                comparisionFile = new FileInputStream(DeltaDocumentSaveTest.class.getResource(SAMPLE_SWAP_MIDDLE).getFile());
                TestUtils.assertEqualsInputStream(comparisionFile, dataInputStream);
            }
            comparisionFile.close();
        } catch (IOException ex) {
            Logger.getLogger(DeltaDocumentSaveTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Exception: " + ex.getMessage());
        }

        document.validatePointerPosition();
        document.clear();
        Assert.assertEquals(0, document.getSegments().size());
        closeTempDeltaDocument(document);
    }

    @Test
    public void testSwapDoubleSaveDocument() {
        DeltaDocument document = openTempDeltaDocument();
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE, document.getDataSize());
        EditableBinaryData quarterCopy = (EditableBinaryData) document.copy(0, 64);
        document.remove(0, 64);
        document.validatePointerPosition();
        document.insert(64, quarterCopy);
        document.validatePointerPosition();
        quarterCopy.dispose();
        EditableBinaryData quarterCopy2 = (EditableBinaryData) document.copy(128, 64);
        document.remove(128, 64);
        document.validatePointerPosition();
        document.insert(192, quarterCopy2);
        document.validatePointerPosition();
        quarterCopy2.dispose();

        try {
            document.save();

            InputStream comparisionFile;
            try (InputStream dataInputStream = document.getDataInputStream()) {
                comparisionFile = new FileInputStream(DeltaDocumentSaveTest.class.getResource(SAMPLE_SWAP_DOUBLE).getFile());
                TestUtils.assertEqualsInputStream(comparisionFile, dataInputStream);
            }
            comparisionFile.close();
        } catch (IOException ex) {
            Logger.getLogger(DeltaDocumentSaveTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Exception: " + ex.getMessage());
        }

        document.validatePointerPosition();
        document.clear();
        Assert.assertEquals(0, document.getSegments().size());
        closeTempDeltaDocument(document);
    }

    @Test
    public void testSwapFirstQuarterSaveDocument() {
        DeltaDocument document = openTempDeltaDocument();
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE, document.getDataSize());
        EditableBinaryData quarterCopy = (EditableBinaryData) document.copy(0, 64);
        document.remove(0, 64);
        document.validatePointerPosition();
        document.insert(192, quarterCopy);
        document.validatePointerPosition();
        quarterCopy.dispose();

        try {
            document.save();

            InputStream comparisionFile;
            try (InputStream dataInputStream = document.getDataInputStream()) {
                comparisionFile = new FileInputStream(DeltaDocumentSaveTest.class.getResource(SAMPLE_SWAP_FIRST_QUARTER).getFile());
                TestUtils.assertEqualsInputStream(comparisionFile, dataInputStream);
            }
            comparisionFile.close();
        } catch (IOException ex) {
            Logger.getLogger(DeltaDocumentSaveTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Exception: " + ex.getMessage());
        }

        document.validatePointerPosition();
        document.clear();
        Assert.assertEquals(0, document.getSegments().size());
        closeTempDeltaDocument(document);
    }

    @Test
    public void testSwapLastQuarterSaveDocument() {
        DeltaDocument document = openTempDeltaDocument();
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE, document.getDataSize());
        EditableBinaryData quarterCopy = (EditableBinaryData) document.copy(192, 64);
        document.remove(192, 64);
        document.validatePointerPosition();
        document.insert(0, quarterCopy);
        document.validatePointerPosition();
        quarterCopy.dispose();

        try {
            document.save();

            InputStream comparisionFile;
            try (InputStream dataInputStream = document.getDataInputStream()) {
                comparisionFile = new FileInputStream(DeltaDocumentSaveTest.class.getResource(SAMPLE_SWAP_LAST_QUARTER).getFile());
                TestUtils.assertEqualsInputStream(comparisionFile, dataInputStream);
            }
            comparisionFile.close();
        } catch (IOException ex) {
            Logger.getLogger(DeltaDocumentSaveTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Exception: " + ex.getMessage());
        }

        document.validatePointerPosition();
        document.clear();
        Assert.assertEquals(0, document.getSegments().size());
        closeTempDeltaDocument(document);
    }

    @Test
    public void testReverseSaveDocument() {
        DeltaDocument document = openTempDeltaDocument();
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE, document.getDataSize());
        for (int i = 0; i < 128; i++) {
            byte buf = document.getByte(i);
            document.setByte(i, document.getByte(255 - i));
            document.validatePointerPosition();
            document.setByte(255 - i, buf);
            document.validatePointerPosition();
        }

        try {
            document.save();

            InputStream comparisionFile;
            try (InputStream dataInputStream = document.getDataInputStream()) {
                comparisionFile = new FileInputStream(DeltaDocumentSaveTest.class.getResource(SAMPLE_REVERSE).getFile());
                TestUtils.assertEqualsInputStream(comparisionFile, dataInputStream);
            }
            comparisionFile.close();
        } catch (IOException ex) {
            Logger.getLogger(DeltaDocumentSaveTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Exception: " + ex.getMessage());
        }

        document.validatePointerPosition();
        document.clear();
        Assert.assertEquals(0, document.getSegments().size());
        closeTempDeltaDocument(document);
    }

    @Test
    public void testSwapReverseSaveDocument() {
        DeltaDocument document = openTempDeltaDocument();
        Assert.assertEquals(SAMPLE_ALLBYTES_SIZE, document.getDataSize());
        for (int i = 0; i < 128; i++) {
            BinaryData copy1 = document.copy(i, 1);
            BinaryData copy2 = document.copy(255 - i, 1);
            document.replace(i, copy2);
            document.validatePointerPosition();
            document.replace(255 - i, copy1);
            document.validatePointerPosition();
            copy1.dispose();
            copy2.dispose();
        }

        try {
            document.save();

            InputStream comparisionFile;
            try (InputStream dataInputStream = document.getDataInputStream()) {
                comparisionFile = new FileInputStream(DeltaDocumentSaveTest.class.getResource(SAMPLE_REVERSE).getFile());
                TestUtils.assertEqualsInputStream(comparisionFile, dataInputStream);
            }
            comparisionFile.close();
        } catch (IOException ex) {
            Logger.getLogger(DeltaDocumentSaveTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail("Exception: " + ex.getMessage());
        }

        document.validatePointerPosition();
        document.clear();
        Assert.assertEquals(0, document.getSegments().size());
        closeTempDeltaDocument(document);
    }

    @Nonnull
    public static DeltaDocument openTempDeltaDocument() {
        SegmentsRepository segmentsRepository = new SegmentsRepository();

        File sampleFile = new File(DeltaDocumentSaveTest.class.getResource(SAMPLE_ALLBYTES).getFile());
        try {
            File tempFile = File.createTempFile("bined-example", ".tmp");

            try (FileInputStream fileInput = new FileInputStream(sampleFile); FileOutputStream fileOutput = new FileOutputStream(tempFile)) {
                // Copy file
                byte[] buffer = new byte[4096];
                int read;
                do {
                    read = fileInput.read(buffer);
                    if (read < 0) {
                        break;
                    }
                    fileOutput.write(buffer, 0, read);

                } while (read > 0);
            }

            FileDataSource fileSource = segmentsRepository.openFileSource(tempFile);
            return segmentsRepository.createDocument(fileSource);
        } catch (IOException ex) {
            Logger.getLogger(DeltaDocumentSaveTest.class.getName()).log(Level.SEVERE, null, ex);
            Assert.fail();
            throw new RuntimeException(ex);
        }
    }

    public static void closeTempDeltaDocument(DeltaDocument document) {
        document.dispose();
        FileDataSource fileSource = Objects.requireNonNull(document.getFileSource());
        fileSource.close();
        File tempFile = fileSource.getFile();
        tempFile.delete();
    }
}
