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
package org.exbin.auxiliary.paged_data.delta.swap;

import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.auxiliary.paged_data.DataPage;

/**
 * Data page swapped to swap file.
 *
 * @version 0.2.0 2021/04/16
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class SwapedDataPage implements DataPage {

    private final SwapDataRepository repository;
    private long pageIndex;
    private int length = 0;

    public SwapedDataPage(SwapDataRepository repository, long pageIndex, int length) {
        this.repository = repository;
        this.pageIndex = pageIndex;
        this.length = length;
        repository.getSwapFilePages().addSwapMovingListener((long sourcePage, long targetPage) -> {
            if (sourcePage == pageIndex) {
                updatePageIndex(targetPage);
            }
        });
    }

    private void updatePageIndex(long pageIndex) {
        this.pageIndex = pageIndex;
    }

    @Override
    public byte[] getData() {
        return repository.getSwapFilePages().getPagePart(pageIndex, length);
    }

    @Override
    public void setData(byte[] page) {
        length = page.length;
        repository.getSwapFilePages().setPage(pageIndex, page);
    }

    @Override
    public int getDataLength() {
        return length;
    }
}
