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
package org.exbin.auxiliary.binary_data.delta.swap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.auxiliary.binary_data.BinaryData;
import org.exbin.auxiliary.binary_data.EditableBinaryData;

/**
 * Data page swapped to swap file.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class SwappedDataPage implements EditableBinaryData {

    private final SwapDataRepository repository;
    private long pageIndex;
    private int length = 0;

    public SwappedDataPage(SwapDataRepository repository, long pageIndex, int length) {
        this.repository = repository;
        this.pageIndex = pageIndex;
        this.length = length;
        repository.getSwapFilePages().addSwapMovingListener((long sourcePage, long targetPage) -> {
            if (sourcePage == pageIndex) {
                updatePageIndex(targetPage);
            }
        });
    }

    private void updatePageIndex(long pageIndex) {
        this.pageIndex = pageIndex;
    }
/*
    @Nonnull
    @Override
    public byte[] getData() {
        return repository.getSwapFilePages().getPagePart(pageIndex, length);
    }

    @Override
    public void setData(byte[] page) {
        length = page.length;
        repository.getSwapFilePages().setPage(pageIndex, page);
    }

    @Override
    public int getDataLength() {
        return length;
    } */

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getDataSize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte getByte(long position) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Nonnull
    @Override
    public BinaryData copy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Nonnull
    @Override
    public BinaryData copy(long startFrom, long length) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void copyToArray(long startFrom, byte[] target, int offset, int length) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void saveToStream(OutputStream outputStream) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Nonnull
    @Override
    public InputStream getDataInputStream() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void dispose() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setDataSize(long size) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setByte(long position, byte value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void insertUninitialized(long startFrom, long length) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void insert(long startFrom, long length) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void insert(long startFrom, byte[] insertedData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void insert(long startFrom, byte[] insertedData, int insertedDataOffset, int insertedDataLength) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void insert(long startFrom, BinaryData insertedData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void insert(long startFrom, BinaryData insertedData, long insertedDataOffset, long insertedDataLength) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long insert(long startFrom, InputStream inputStream, long maximumDataSize) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void replace(long targetPosition, BinaryData replacingData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void replace(long targetPosition, BinaryData replacingData, long startFrom, long length) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void replace(long targetPosition, byte[] replacingData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void replace(long targetPosition, byte[] replacingData, int replacingDataOffset, int length) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fillData(long startFrom, long length) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fillData(long startFrom, long length, byte fill) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove(long startFrom, long length) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void loadFromStream(InputStream inputStream) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public OutputStream getDataOutputStream() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
