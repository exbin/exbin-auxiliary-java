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
package org.exbin.auxiliary.binary_data.delta;

import java.io.IOException;
import java.io.InputStream;
import javax.annotation.ParametersAreNonnullByDefault;
import org.junit.Assert;

/**
 * Tests utilities.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class TestUtils {

    private TestUtils() {
    }

    /**
     * Loads data from two streams and test them for exact match.
     *
     * @param expectedStream stream to be considered as template
     * @param stream stream for matching
     */
    public static void assertEqualsInputStream(InputStream expectedStream, InputStream stream) {
        try {
            byte[] dataBlob = new byte[2];
            int position = 0;
            int readStat;
            do {
                readStat = expectedStream.read(dataBlob, 0, 1);
                int readStat2 = stream.read(dataBlob, 1, 1);
                if (readStat < 0) {
                    if (readStat2 > 0) {
                        Assert.fail("Unable to read expected stream on position " + position);
                    }
                    break;
                }
                if (readStat2 < 0) {
                    Assert.fail("Unable to read compared stream on position " + position);
                }

                Assert.assertEquals("Issue on position " + position, dataBlob[0], dataBlob[1]);
                position++;
            } while (readStat > 0);

            Assert.assertTrue(stream.available() == 0);
        } catch (IOException ex) {
            Assert.fail("IOException " + ex.getMessage());
        }
    }
}
