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
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.auxiliary.binary_data.FinishableStream;
import org.exbin.auxiliary.binary_data.OutOfBoundsException;
import org.exbin.auxiliary.binary_data.SeekableStream;

/**
 * Input stream for paged data.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class ByteArrayPagedDataInputStream extends InputStream implements SeekableStream, FinishableStream {

    @Nonnull
    private final ByteArrayPagedData data;
    protected final long startPosition;
    protected final long length;
    protected long position = 0;
    protected long mark = 0;

    public ByteArrayPagedDataInputStream(ByteArrayPagedData data) {
        this.data = data;
        this.startPosition = 0;
        this.position = 0;
        this.length = data.getDataSize();
    }

    public ByteArrayPagedDataInputStream(ByteArrayPagedData data, long startPosition, long length) {
        if (startPosition < 0) {
            throw new IllegalArgumentException("Negative position not allowed");
        }
        if (length < 0) {
            throw new IllegalArgumentException("Negative length not allowed");
        }
        long sourceDataSize = data.getDataSize();
        if (startPosition + length > sourceDataSize) {
            throw new OutOfBoundsException("Target area is outside of available data");
        }

        this.data = data;
        this.startPosition = startPosition;
        this.position = startPosition;
        this.length = length;
    }

    @Override
    public int read() throws IOException {
        if (position > startPosition + length) {
            return -1;
        }

        try {
            return data.getByte(position++);
        } catch (ArrayIndexOutOfBoundsException ex) {
            return -1;
        }
    }

    @Override
    public int read(byte[] output, int off, int len) throws IOException {
        if (output.length == 0 || len == 0) {
            return 0;
        }

        if (position > startPosition + length - len) {
            if (position >= startPosition + length) {
                return -1;
            }
            len = (int) (startPosition + length - position);
        }

        int remaining = len;
        int offset = off;
        while (remaining > 0) {
            int pageIndex = (int) (position / data.getPageSize());
            if (pageIndex >= data.getPagesCount()) {
                return offset == off ? -1 : offset - off;
            }

            byte[] page = data.getPage(pageIndex).getData();
            int srcPos = (int) (position % data.getPageSize());
            int copyLength = page.length - srcPos;
            if (copyLength > remaining) {
                copyLength = remaining;
            }

            if (copyLength == 0) {
                return len == remaining ? -1 : len - remaining;
            }

            System.arraycopy(page, srcPos, output, offset, copyLength);
            remaining -= copyLength;
            position += copyLength;
            offset += copyLength;
        }

        return len;
    }

    @Override
    public void close() throws IOException {
        finish();
    }

    @Override
    public int available() throws IOException {
        return (int) ((startPosition + length) - position);
    }

    @Override
    public void seek(long position) throws IOException {
        if (position < 0 || position > length) {
            throw new OutOfBoundsException("Position is outside of available range");
        }

        this.position = startPosition + position;
    }

    @Override
    public long finish() throws IOException {
        position = startPosition + length;
        return length;
    }

    @Override
    public long getProcessedSize() {
        return position - startPosition;
    }

    @Override
    public long getStreamSize() {
        return length;
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public synchronized void reset() throws IOException {
        position = mark;
    }

    @Override
    public synchronized void mark(int readlimit) {
        mark = position;
    }

    @Override
    public long skip(long n) throws IOException {
        if (position - startPosition + n < length) {
            position += n;
            return n;
        }

        long skipped = startPosition + length - position;
        position = startPosition + length;
        return skipped;
    }
}
