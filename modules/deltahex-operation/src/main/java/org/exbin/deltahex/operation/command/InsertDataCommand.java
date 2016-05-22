/*
 * Copyright (C) ExBin Project
 *
 * This application or library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This application or library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along this application.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.exbin.deltahex.operation.command;

import org.exbin.deltahex.data.HexadecimalData;
import org.exbin.deltahex.Hexadecimal;
import org.exbin.deltahex.operation.InsertDataOperation;

/**
 * Command for inserting data.
 *
 * @version 0.1.0 2016/05/14
 * @author ExBin Project (http://exbin.org)
 */
public class InsertDataCommand extends OpHexCommand {

    private long position;
    private long dataLength;

    public InsertDataCommand(Hexadecimal hexadecimal, long position, HexadecimalData data) {
        super(hexadecimal);
        this.position = position;
        dataLength = data.getDataSize();
        super.setOperation(new InsertDataOperation(hexadecimal, position, hexadecimal.getCaretPosition().isLowerHalf(), data));
    }

    @Override
    public HexCommandType getType() {
        return HexCommandType.DATA_INSERTED;
    }

    @Override
    public void redo() throws Exception {
        super.redo();
        hexadecimal.getCaretPosition().setDataPosition(position + dataLength);
    }

    @Override
    public void undo() throws Exception {
        super.undo();
        hexadecimal.getCaretPosition().setDataPosition(position);
    }
}
