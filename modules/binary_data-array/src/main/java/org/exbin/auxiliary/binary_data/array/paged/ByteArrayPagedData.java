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
package org.exbin.auxiliary.binary_data.array.paged;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.auxiliary.binary_data.BinaryData;
import org.exbin.auxiliary.binary_data.array.ByteArrayData;
import org.exbin.auxiliary.binary_data.DataOverflowException;
import org.exbin.auxiliary.binary_data.EditableBinaryData;
import org.exbin.auxiliary.binary_data.OutOfBoundsException;
import org.exbin.auxiliary.binary_data.paged.PagedData;
import org.exbin.auxiliary.binary_data.paged.DataPageCreator;

/**
 * Encapsulation class for binary data blob.
 * <p>
 * Data are stored using paging. Last page might be shorter than page size, but
 * not empty.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class ByteArrayPagedData implements PagedData {

    public static final int DEFAULT_PAGE_SIZE = 4096;
    public static final long MAX_DATA_SIZE = Long.MAX_VALUE;

    private int pageSize = DEFAULT_PAGE_SIZE;
    @Nonnull
    private final List<ByteArrayData> data = new ArrayList<>();

    @Nullable
    private DataPageCreator dataPageCreator = null;

    public ByteArrayPagedData() {
    }

    public ByteArrayPagedData(DataPageCreator dataPageCreator) {
        this.dataPageCreator = dataPageCreator;
    }

    public ByteArrayPagedData(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public long getDataSize() {
        return (data.size() > 1 ? (data.size() - 1) * pageSize : 0) + (!data.isEmpty() ? data.get(data.size() - 1).getDataSize() : 0);
    }

    @Override
    public void setDataSize(long size) {
        if (size < 0) {
            throw new InvalidParameterException("Size cannot be negative");
        }

        long dataSize = getDataSize();
        if (size > dataSize) {
            int lastPage = (int) (dataSize / pageSize);
            int lastPageSize = (int) (dataSize % pageSize);
            long remaining = size - dataSize;
            // extend last page
            if (lastPageSize > 0) {
                byte[] page = getPageData(lastPage);
                int nextPageSize = remaining + lastPageSize > pageSize ? pageSize : (int) remaining + lastPageSize;
                byte[] newPage = new byte[nextPageSize];
                System.arraycopy(page, 0, newPage, 0, lastPageSize);
                setPage(lastPage, createNewPage(newPage));
                remaining -= (nextPageSize - lastPageSize);
                lastPage++;
            }

            while (remaining > 0) {
                int nextPageSize = remaining > pageSize ? pageSize : (int) remaining;
                data.add(createNewPage(new byte[nextPageSize]));
                remaining -= nextPageSize;
            }
        } else if (size < dataSize) {
            int lastPage = (int) (size / pageSize);
            int lastPageSize = (int) (size % pageSize);
            // shrink last page
            if (lastPageSize > 0) {
                byte[] page = getPageData(lastPage);
                byte[] newPage = new byte[lastPageSize];
                System.arraycopy(page, 0, newPage, 0, lastPageSize);
                setPage(lastPage, createNewPage(newPage));
                lastPage++;
            }

            for (int pageIndex = data.size() - 1; pageIndex >= lastPage; pageIndex--) {
                data.remove(pageIndex);
            }
        }
    }

    @Override
    public byte getByte(long position) {
        byte[] page = getPageData((int) (position / pageSize));
        try {
            return page[(int) (position % pageSize)];
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new OutOfBoundsException(ex);
        }
    }

    @Override
    public void setByte(long position, byte value) {
        byte[] page = getPageData((int) (position / pageSize));
        try {
            page[(int) (position % pageSize)] = value;
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new OutOfBoundsException(ex);
        }
    }

    @Override
    public void insertUninitialized(long startFrom, long length) {
        if (length < 0) {
            throw new IllegalArgumentException("Length of inserted block must be nonnegative");
        }
        if (startFrom < 0) {
            throw new IllegalArgumentException("Position of inserted block must be nonnegative");
        }
        long dataSize = getDataSize();
        if (startFrom > dataSize) {
            throw new OutOfBoundsException("Inserted block must be inside or directly after existing data");
        }
        if (length > MAX_DATA_SIZE - getDataSize()) {
            throw new DataOverflowException("Maximum array size overflow");
        }

        if (length == 0) {
            return;
        }

        long copyLength = dataSize - startFrom;
        dataSize = dataSize + length;
        setDataSize(dataSize);
        long sourceEnd = dataSize - length;
        long targetEnd = dataSize;
        // Backward copy
        while (copyLength > 0) {
            byte[] sourcePage = getPageData((int) (sourceEnd / pageSize));
            int sourceOffset = (int) (sourceEnd % pageSize);
            if (sourceOffset == 0) {
                sourcePage = getPageData((int) ((sourceEnd - 1) / pageSize));
                sourceOffset = sourcePage.length;
            }

            byte[] targetPage = getPageData((int) (targetEnd / pageSize));
            int targetOffset = (int) (targetEnd % pageSize);
            if (targetOffset == 0) {
                targetPage = getPageData((int) ((targetEnd - 1) / pageSize));
                targetOffset = targetPage.length;
            }

            int copySize = Math.min(sourceOffset, targetOffset);
            if (copySize > copyLength) {
                copySize = (int) copyLength;
            }

            System.arraycopy(sourcePage, sourceOffset - copySize, targetPage, targetOffset - copySize, copySize);
            copyLength -= copySize;
            sourceEnd -= copySize;
            targetEnd -= copySize;
        }
    }

    @Override
    public void insert(long startFrom, long length) {
        insertUninitialized(startFrom, length);
        fillData(startFrom, length);
    }

    @Override
    public void insert(long startFrom, BinaryData insertedData) {
        long length = insertedData.getDataSize();
        insertUninitialized(startFrom, length);
        replace(startFrom, insertedData, 0, length);
    }

    @Override
    public void insert(long startFrom, BinaryData insertedData, long insertedDataOffset, long insertedDataLength) {
        insertUninitialized(startFrom, insertedDataLength);
        replace(startFrom, insertedData, insertedDataOffset, insertedDataLength);
    }

    @Override
    public void insert(long startFrom, byte[] insertedData) {
        insert(startFrom, insertedData, 0, insertedData.length);
    }

    @Override
    public void insert(long startFrom, byte[] insertedData, int insertedDataOffset, int insertedDataLength) {
        if (insertedDataLength <= 0) {
            return;
        }

        insertUninitialized(startFrom, insertedDataLength);

        while (insertedDataLength > 0) {
            byte[] targetPage = getPageData((int) (startFrom / pageSize));
            int targetOffset = (int) (startFrom % pageSize);
            int blockLength = pageSize - targetOffset;
            if (blockLength > insertedDataLength) {
                blockLength = insertedDataLength;
            }

            try {
                System.arraycopy(insertedData, insertedDataOffset, targetPage, targetOffset, blockLength);
            } catch (ArrayIndexOutOfBoundsException ex) {
                throw new OutOfBoundsException(ex);
            }
            insertedDataOffset += blockLength;
            insertedDataLength -= blockLength;
            startFrom += blockLength;
        }
    }

    @Override
    public long insert(long startFrom, InputStream inputStream, long maximumDataSize) throws IOException {
        if (maximumDataSize > MAX_DATA_SIZE - getDataSize()) {
            throw new DataOverflowException("Maximum array size overflow");
        }

        if (startFrom > getDataSize()) {
            setDataSize(startFrom);
        }

        long loadedData = 0;
        int pageOffset = (int) (startFrom % pageSize);
        byte[] buffer = new byte[pageSize];
        while (maximumDataSize == -1 || maximumDataSize > 0) {
            int dataToRead = pageSize - pageOffset;
            if (maximumDataSize >= 0 && maximumDataSize < dataToRead) {
                dataToRead = (int) maximumDataSize;
            }
            if (pageOffset > 0 && dataToRead > pageOffset) {
                // Align to data pages
                dataToRead = pageOffset;
            }

            int readLength = 0;
            while (dataToRead > 0) {
                int read = inputStream.read(buffer, readLength, dataToRead);
                if (read == -1) {
                    break;
                }

                readLength += read;
                dataToRead -= read;
            }

            insert(startFrom, buffer, 0, readLength);
            startFrom += readLength;
            if (maximumDataSize >= 0) {
                maximumDataSize -= readLength;
            }
            loadedData += readLength;
            pageOffset = 0;
        }
        return loadedData;
    }

    @Override
    public void fillData(long startFrom, long length) {
        fillData(startFrom, length, (byte) 0);
    }

    @Override
    public void fillData(long startFrom, long length, byte fill) {
        if (length < 0) {
            throw new IllegalArgumentException("Length of filled block must be non-negative");
        }
        if (startFrom < 0) {
            throw new IllegalArgumentException("Position of filler block must be non-negative");
        }
        if (startFrom + length > getDataSize()) {
            throw new OutOfBoundsException("Filled block must be inside existing data");
        }

        while (length > 0) {
            byte[] page = getPageData((int) (startFrom / pageSize));
            int pageOffset = (int) (startFrom % pageSize);
            int fillSize = page.length - pageOffset;
            if (fillSize > length) {
                fillSize = (int) length;
            }
            Arrays.fill(page, pageOffset, pageOffset + fillSize, fill);
            length -= fillSize;
            startFrom += fillSize;
        }
    }

    @Nonnull
    @Override
    public ByteArrayPagedData copy() {
        ByteArrayPagedData targetData = new ByteArrayPagedData();
        targetData.insert(0, this);
        return targetData;
    }

    @Nonnull
    @Override
    public ByteArrayPagedData copy(long startFrom, long length) {
        ByteArrayPagedData targetData = new ByteArrayPagedData();
        targetData.insertUninitialized(0, length);
        targetData.replace(0, this, startFrom, length);
        return targetData;
    }

    @Override
    public void copyToArray(long startFrom, byte[] target, int offset, int length) {
        while (length > 0) {
            byte[] page = getPageData((int) (startFrom / pageSize));
            int pageOffset = (int) (startFrom % pageSize);
            int copySize = pageSize - pageOffset;
            if (copySize > length) {
                copySize = length;
            }

            try {
                System.arraycopy(page, pageOffset, target, offset, copySize);
            } catch (ArrayIndexOutOfBoundsException ex) {
                throw new OutOfBoundsException(ex);
            }
            length -= copySize;
            offset += copySize;
            startFrom += copySize;
        }
    }

    @Override
    public void remove(long startFrom, long length) {
        if (length < 0) {
            throw new IllegalArgumentException("Length of removed block must be non-negative");
        }
        if (startFrom < 0) {
            throw new IllegalArgumentException("Position of removed block must be non-negative");
        }
        if (startFrom + length > getDataSize()) {
            throw new OutOfBoundsException("Removed block must be inside existing data");
        }

        if (length > 0) {
            replace(startFrom, this, startFrom + length, getDataSize() - startFrom - length);
            setDataSize(getDataSize() - length);
        }
    }

    @Override
    public void clear() {
        data.clear();
    }

    /**
     * Returns number of pages currently used.
     *
     * @return count of pages
     */
    @Override
    public int getPagesCount() {
        return data.size();
    }

    /**
     * Returns currently used page size.
     *
     * @return page size in bytes
     */
    @Override
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Gets data page allowing direct access to it.
     *
     * @param pageIndex page index
     * @return data page
     */
    @Nonnull
    @Override
    public ByteArrayData getPage(int pageIndex) {
        try {
            return data.get(pageIndex);
        } catch (IndexOutOfBoundsException ex) {
            throw new OutOfBoundsException(ex);
        }
    }

    private byte[] getPageData(int pageIndex) {
        return getPage(pageIndex).getData();
    }

    /**
     * Sets data page replacing existing page by reference.
     *
     * @param pageIndex page index
     * @param dataPage data page
     */
    @Override
    public void setPage(int pageIndex, BinaryData dataPage) {
        if (!(dataPage instanceof ByteArrayData)) {
            throw new UnsupportedOperationException("Unsupported data page type");
        }
        try {
            data.set(pageIndex, (ByteArrayData) dataPage);
        } catch (IndexOutOfBoundsException ex) {
            throw new OutOfBoundsException(ex);
        }
    }

    @Override
    public void replace(long targetPosition, BinaryData replacingData) {
        replace(targetPosition, replacingData, 0, replacingData.getDataSize());
    }

    @Override
    public void replace(long targetPosition, BinaryData replacingData, long startFrom, long length) {
        if (targetPosition + length > getDataSize()) {
            throw new OutOfBoundsException("Data can be replaced only inside or at the end");
        }

        if (replacingData instanceof ByteArrayPagedData) {
            if (replacingData != this || (startFrom > targetPosition) || (startFrom + length < targetPosition)) {
                while (length > 0) {
                    byte[] page = getPageData((int) (targetPosition / pageSize));
                    int offset = (int) (targetPosition % pageSize);

                    byte[] sourcePage = ((ByteArrayPagedData) replacingData).getPageData((int) (startFrom / ((ByteArrayPagedData) replacingData).getPageSize()));
                    int sourceOffset = (int) (startFrom % ((ByteArrayPagedData) replacingData).getPageSize());

                    int copySize = pageSize - offset;
                    if (copySize > ((ByteArrayPagedData) replacingData).getPageSize() - sourceOffset) {
                        copySize = ((ByteArrayPagedData) replacingData).getPageSize() - sourceOffset;
                    }
                    if (copySize > length) {
                        copySize = (int) length;
                    }

                    try {
                        System.arraycopy(sourcePage, sourceOffset, page, offset, copySize);
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        throw new OutOfBoundsException(ex);
                    }
                    length -= copySize;
                    targetPosition += copySize;
                    startFrom += copySize;
                }
            } else {
                targetPosition += length - 1;
                startFrom += length - 1;
                while (length > 0) {
                    byte[] page = getPageData((int) (targetPosition / pageSize));
                    int upTo = (int) (targetPosition % pageSize) + 1;

                    byte[] sourcePage = ((ByteArrayPagedData) replacingData).getPageData((int) (startFrom / ((ByteArrayPagedData) replacingData).getPageSize()));
                    int sourceUpTo = (int) (startFrom % ((ByteArrayPagedData) replacingData).getPageSize()) + 1;

                    int copySize = upTo;
                    if (copySize > sourceUpTo) {
                        copySize = sourceUpTo;
                    }
                    if (copySize > length) {
                        copySize = (int) length;
                    }
                    int offset = upTo - copySize;
                    int sourceOffset = sourceUpTo - copySize;

                    System.arraycopy(sourcePage, sourceOffset, page, offset, copySize);
                    length -= copySize;
                    targetPosition -= copySize;
                    startFrom -= copySize;
                }
            }
        } else {
            while (length > 0) {
                byte[] page = getPageData((int) (targetPosition / pageSize));
                int offset = (int) (targetPosition % pageSize);

                int copySize = pageSize - offset;
                if (copySize > length) {
                    copySize = (int) length;
                }

                replacingData.copyToArray(startFrom, page, offset, copySize);

                length -= copySize;
                targetPosition += copySize;
                startFrom += copySize;
            }
        }
    }

    @Override
    public void replace(long targetPosition, byte[] replacingData) {
        replace(targetPosition, replacingData, 0, replacingData.length);
    }

    @Override
    public void replace(long targetPosition, byte[] replacingData, int replacingDataOffset, int length) {
        if (targetPosition + length > getDataSize()) {
            throw new OutOfBoundsException("Data can be replaced only inside or at the end");
        }

        while (length > 0) {
            byte[] page = getPageData((int) (targetPosition / pageSize));
            int offset = (int) (targetPosition % pageSize);

            int copySize = pageSize - offset;
            if (copySize > length) {
                copySize = length;
            }

            try {
                System.arraycopy(replacingData, replacingDataOffset, page, offset, copySize);
            } catch (ArrayIndexOutOfBoundsException ex) {
                throw new OutOfBoundsException(ex);
            }

            length -= copySize;
            targetPosition += copySize;
            replacingDataOffset += copySize;
        }
    }

    @Override
    public void loadFromStream(InputStream inputStream) throws IOException {
        data.clear();
        byte[] buffer = new byte[pageSize];
        int cnt;
        int offset = 0;
        while ((cnt = inputStream.read(buffer, offset, buffer.length - offset)) > 0) {
            if (cnt + offset < pageSize) {
                offset = offset + cnt;
            } else {
                data.add(createNewPage(buffer));
                buffer = new byte[pageSize];
                offset = 0;
            }
        }

        if (offset > 0) {
            byte[] tail = new byte[offset];
            System.arraycopy(buffer, 0, tail, 0, offset);
            data.add(createNewPage(tail));
        }
    }

    @Override
    public void saveToStream(OutputStream outputStream) throws IOException {
        for (ByteArrayData dataPage : data) {
            outputStream.write(dataPage.getData());
        }
    }

    @Nonnull
    @Override
    public OutputStream getDataOutputStream() {
        return new ByteArrayPagedDataOutputStream(this);
    }

    @Nonnull
    @Override
    public InputStream getDataInputStream() {
        return new ByteArrayPagedDataInputStream(this);
    }

    @Nonnull
    private ByteArrayData createNewPage(byte[] pageData) {
        if (dataPageCreator != null) {
            EditableBinaryData page = dataPageCreator.createPage(pageData.length);
            page.replace(0, pageData);
            return (ByteArrayData) page;
        }

        return new ByteArrayData(pageData);
    }

    @Nullable
    public DataPageCreator getDataPageCreator() {
        return dataPageCreator;
    }

    public void setDataPageCreator(@Nullable DataPageCreator dataPageCreator) {
        this.dataPageCreator = dataPageCreator;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            if (obj instanceof BinaryData) {
                BinaryData other = (BinaryData) obj;
                long dataSize = getDataSize();
                if (other.getDataSize() != dataSize) {
                    return false;
                }

                int pageIndex = 0;
                int bufferSize = dataSize > pageSize ? pageSize : (int) dataSize;
                byte[] buffer = new byte[bufferSize];
                int offset = 0;
                int remain = (int) dataSize;
                while (remain > 0) {
                    int length = remain > bufferSize ? bufferSize : remain;
                    other.copyToArray(offset, buffer, 0, length);

                    // In Java 9+
                    // if (!Arrays.equals(data.get(pageIndex).getData(), 0, length, buffer, 0, length)) {
                    //    return false;
                    // }
                    {
                        byte[] pageData = data.get(pageIndex).getData();
                        for (int i = 0; i < length; i++) {
                            if (pageData[i] != buffer[i]) {
                                return false;
                            }
                        }
                    }

                    offset += length;
                    remain -= length;
                    pageIndex++;
                }

                return true;
            }

            return false;
        }

        final ByteArrayPagedData other = (ByteArrayPagedData) obj;
        long dataSize = getDataSize();
        if (other.getDataSize() != dataSize) {
            return false;
        }

        int pageIndex = 0;
        int otherPageIndex = 0;
        long offset = 0;
        long remain = dataSize;
        while (remain > 0) {
            int pageOffset = (int) (offset % pageSize);
            int otherPageOffset = (int) (offset % other.pageSize);

            int length = remain > pageSize - pageOffset ? pageSize - pageOffset : (int) remain;
            if (length > other.pageSize - otherPageOffset) {
                length = other.pageSize - otherPageOffset;
            }

            // In Java 9+
            // if (!Arrays.equals(data.get(pageIndex).getData(), pageOffset, pageOffset + length, other.data.get(otherPageIndex).getData(), otherPageOffset, otherPageOffset + length)) {
            //     return false;
            // }
            {
                byte[] pageData = data.get(pageIndex).getData();
                byte[] otherPageData = other.data.get(otherPageIndex).getData();
                int pageTestPos = pageOffset;
                int otherPageTestPos = otherPageOffset;
                for (int i = 0; i < length; i++) {
                    if (pageData[pageTestPos] != otherPageData[otherPageTestPos]) {
                        return false;
                    }
                    pageTestPos++;
                    otherPageTestPos++;
                }
            }

            offset += length;
            remain -= length;
            pageIndex++;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDataSize());
    }

    @Override
    public void dispose() {
    }
}
