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
package org.exbin.auxiliary.binary_data;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Exception type for out of bounds issues with binary data manipulation
 * operations.
 *
 * @version 0.1.3 2019/01/05
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class OutOfBoundsException extends RuntimeException {

    public OutOfBoundsException() {
    }

    public OutOfBoundsException(@Nullable String message) {
        super(message);
    }

    public OutOfBoundsException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    public OutOfBoundsException(@Nullable Throwable cause) {
        super(cause);
    }

    public OutOfBoundsException(@Nullable String message, @Nullable Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
