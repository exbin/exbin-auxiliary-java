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
package org.exbin.utils.data.paged;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface for editable binary data.
 *
 * @version 0.1.0 2016/05/23
 * @author ExBin Project (http://exbin.org)
 */
interface EditableBinaryData extends BinaryData {

    /**
     * Sets data size.
     *
     * If size is bigger than current size, it will fill it with zeros,
     * otherwise it will shrink current data.
     *
     * @param size target size
     */
    void setDataSize(long size);

    /**
     * Sets byte to given position.
     *
     * @param position position
     * @param value byte value to be set
     */
    void setByte(long position, byte value);

    /**
     * Inserts empty data of given length to given position.
     *
     * @param startFrom position to insert to
     * @param length length of data
     */
    void insert(long startFrom, long length);

    /**
     * Inserts data space of given length to given position without setting any
     * data to it.
     *
     * @param startFrom position to insert to
     * @param length length of data
     */
    void insertUninitialized(long startFrom, long length);

    /**
     * Inserts given data to given position.
     *
     * @param startFrom position to insert to
     * @param insertedData data to insert
     */
    void insert(long startFrom, byte[] insertedData);

    /**
     * Inserts given data to given position.
     *
     * @param startFrom position to insert to
     * @param insertedData data to insert
     */
    void insert(long startFrom, BinaryData insertedData);

    /**
     * Inserts given data to given position.
     *
     * @param startFrom position to insert to
     * @param insertedData data to insert
     * @param insertedDataOffset inserted data offset
     * @param insertedDataLength inserted data length
     */
    void insert(long startFrom, byte[] insertedData, int insertedDataOffset, int insertedDataLength);

    /**
     * Replaces data in given area to target data.
     *
     * Replace must support replacing segment from itself (last-first
     * direction).
     *
     * @param targetPosition target position to write to
     * @param sourceData data to copy from
     */
    void replace(long targetPosition, BinaryData sourceData);

    /**
     * Replaces data in given area to target data.
     *
     * Replace must support replacing segment from itself (last-first
     * direction).
     *
     * @param targetPosition target position to write to
     * @param sourceData data to copy from
     * @param startFrom position to start copy from
     * @param length length of data to copy
     */
    void replace(long targetPosition, BinaryData sourceData, long startFrom, long length);

    /**
     * Fills given area with empty data.
     *
     * @param startFrom position to fill data to
     * @param length length of area
     */
    void fillData(long startFrom, long length);

    /**
     * Fills given area with bytes of given value.
     *
     * @param startFrom position to fill data to
     * @param length length of area
     * @param fill value to fill with
     */
    void fillData(long startFrom, long length, byte fill);

    /**
     * Removes area of data.
     *
     * @param startFrom position to start removal from
     * @param length length of area
     */
    void remove(long startFrom, long length);

    /**
     * Removes all existing data.
     *
     * Simply releases all references to data pages.
     */
    public void clear();

    /**
     * Loads data from given stream.
     *
     * @param inputStream input stream
     * @throws java.io.IOException if input/output error
     */
    void loadFromStream(InputStream inputStream) throws IOException;

    /**
     * Loads data from given stream expecting given size.
     *
     * @param inputStream input stream
     * @param startFrom start position to load data to
     * @param dataSize data size
     * @throws java.io.IOException if input/output error
     */
    public void loadFromStream(InputStream inputStream, long startFrom, long dataSize) throws IOException;
}
