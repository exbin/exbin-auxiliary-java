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
import java.io.OutputStream;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Output stream for binary data.
 * <p>
 * Data are expanded as needed.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class BinaryDataOutputStream extends OutputStream implements SeekableStream {

    @Nonnull
    protected final EditableBinaryData data;
    protected long position = 0;

    public BinaryDataOutputStream(EditableBinaryData data) {
        this.data = data;
    }

    @Override
    public void write(int value) throws IOException {
        long dataSize = data.getDataSize();
        if (position == dataSize) {
            dataSize++;
            data.insertUninitialized(position, 1);
        }

        data.setByte(position++, (byte) value);
    }

    @Override
    public void write(byte[] input, int off, int len) throws IOException {
        if (len == 0) {
            return;
        }

        long dataSize = data.getDataSize();
        if (position + len > dataSize) {
            long expand = position + len - dataSize;
            data.insertUninitialized(position, expand);
        }

        data.replace(position, input, off, len);
        position += len;
    }

    @Override
    public void seek(long position) throws IOException {
        if (position < 0) {
            throw new OutOfBoundsException("Position is outside of available range");
        }

        if (position > data.getDataSize()) {
            data.setDataSize(position);
        }

        this.position = position;
    }

    @Override
    public long getStreamSize() {
        return -1;
    }

    public long getProcessedSize() {
        return position;
    }

    @Override
    public void close() throws IOException {
        position = data.getDataSize();
    }
}
