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
package org.exbin.deltahex.operation;

import org.exbin.deltahex.Hexadecimal;

/**
 * Abstract operation for editing data.
 *
 * @version 0.1.0 2015/05/16
 * @author ExBin Project (http://exbin.org)
 */
public abstract class CharEditDataOperation extends HexOperation {

    public CharEditDataOperation(Hexadecimal hexadecimal) {
        super(hexadecimal);
    }

    /**
     * Appends next character value in editing action sequence.
     *
     * @param value character
     */
    public abstract void appendEdit(char value);

    /**
     * Generates undo operation for combined editing action.
     *
     * @return hexadecimal operation
     */
    public abstract HexOperation[] generateUndo();
}
