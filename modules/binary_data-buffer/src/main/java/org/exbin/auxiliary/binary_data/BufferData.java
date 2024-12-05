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
package org.exbin.auxiliary.binary_data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * TODO: Basic implementation of binary data interface using byte array.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class BufferData implements BinaryData {

    @Nonnull
    protected ByteBuffer data;

    public BufferData() {
        this(null);
    }

    public BufferData(@Nullable ByteBuffer data) {
        this.data = data != null ? data : ByteBuffer.allocate(0);
    }

    /**
     * Returns internal data.
     *
     * @return byte array
     */
    @Nonnull
    public ByteBuffer getData() {
        return data;
    }

    @Override
    public boolean isEmpty() {
        return data.capacity() == 0;
    }

    @Override
    public long getDataSize() {
        return data.capacity();
    }

    @Override
    public byte getByte(long position) {
        try {
            return data.get((int) position);
        } catch (IndexOutOfBoundsException ex) {
            throw new OutOfBoundsException(ex);
        }
    }

    @Nonnull
    @Override
    public BinaryData copy() {
        return new BufferData(data.duplicate());
    }

    @Nonnull
    @Override
    public BinaryData copy(long startFrom, long length) {
        throw new UnsupportedOperationException("Not supported yet.");
/*        if (startFrom + length > data.length) {
            throw new OutOfBoundsException("Attemt to copy outside of data");
        }

        byte[] copy = Arrays.copyOfRange(data, (int) startFrom, (int) (startFrom + length));
        return new BufferData(copy); */
    }

    @Override
    public void copyToArray(long startFrom, byte[] target, int offset, int length) {
        throw new UnsupportedOperationException("Not supported yet.");
/*        try {
            System.arraycopy(data, (int) startFrom, target, offset, length);
        } catch (IndexOutOfBoundsException ex) {
            throw new OutOfBoundsException(ex);
        } */
    }

    @Override
    public void saveToStream(OutputStream outputStream) throws IOException {
        // outputStream.write(data);
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Nonnull
    @Override
    public InputStream getDataInputStream() {
        return new BinaryDataInputStream(this);
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("Not supported yet.");
        // return Arrays.hashCode(data);
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
                return compareTo(other);
            }

            return false;
        }

        final BufferData other = (BufferData) obj;
        // return Arrays.equals(this.data, other.data);
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean compareTo(BinaryData other) {
        long dataSize = getDataSize();
        if (other.getDataSize() != dataSize) {
            return false;
        }

        throw new UnsupportedOperationException("Not supported yet.");
/*        int bufferSize = dataSize > BufferEditableData.BUFFER_SIZE ? BufferEditableData.BUFFER_SIZE : (int) dataSize;
        byte[] buffer = new byte[bufferSize];
        int offset = 0;
        int remain = (int) dataSize;
        while (remain > 0) {
            int length = remain > bufferSize ? bufferSize : remain;
            other.copyToArray(offset, buffer, 0, length);
            for (int i = 0; i < length; i++) {
                if (data[offset + i] != buffer[i]) {
                    return false;
                }
            }

            offset += length;
            remain -= length;
        }

        return true; */
    }

    @Override
    public void dispose() {
    }
}
