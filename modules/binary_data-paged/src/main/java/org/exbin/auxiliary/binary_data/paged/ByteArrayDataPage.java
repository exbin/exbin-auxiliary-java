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
package org.exbin.auxiliary.binary_data.paged;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.auxiliary.binary_data.ByteArrayData;

/**
 * Basic implementation of binary data interface using byte array.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class ByteArrayDataPage extends ByteArrayData implements DataPage {

    public ByteArrayDataPage() {
        super(null);
    }

    public ByteArrayDataPage(@Nullable byte[] data) {
        super(data);
    }

    /**
     * Returns internal data.
     *
     * @return byte array
     */
    @Nonnull
    @Override
    public byte[] getData() {
        return data;
    }

    /**
     * Sets internal data.
     *
     * @param data byte array
     */
    @Override
    public void setData(byte[] data) {
        this.data = Objects.requireNonNull(data);
    }

    /**
     * Returns internal data length.
     *
     * @return data length
     */
    @Override
    public int getDataLength() {
        return data.length;
    }
}
