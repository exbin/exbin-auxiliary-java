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
package org.exbin.auxiliary.paged_data;

import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Input stream for byte array data.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class ByteArrayDataInputStream extends InputStream implements SeekableStream, FinishableStream {

    @Nonnull
    private final ByteArrayData data;
    private long position = 0;

    public ByteArrayDataInputStream(ByteArrayData data) {
        this.data = data;
    }

    @Override
    public int read() throws IOException {
        try {
            return data.getByte(position++);
        } catch (ArrayIndexOutOfBoundsException ex) {
            return -1;
        }
    }

    @Override
    public void close() throws IOException {
        finish();
    }

    @Override
    public int available() throws IOException {
        return (int) (data.getDataSize() - position);
    }

    @Override
    public int read(byte[] output, int off, int len) throws IOException {
        if (output.length == 0 || len == 0) {
            return 0;
        }

        byte[] byteArray = data.getData();
        if (position > byteArray.length - len) {
            if (position >= byteArray.length) {
                return -1;
            }
            len = (int) (byteArray.length - position);
        }

        System.arraycopy(byteArray, (int) position, output, off, len);
        position += len;
        return len;
    }

    @Override
    public void seek(long position) throws IOException {
        this.position = position;
    }

    @Override
    public long finish() throws IOException {
        position = data.getDataSize();
        return position;
    }

    @Override
    public long getProcessedSize() {
        return position;
    }

    @Override
    public long getStreamSize() {
        return data.getDataSize();
    }
}
