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
package org.exbin.auxiliary.binary_data.delta.swap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.auxiliary.binary_data.OutOfBoundsException;
import org.exbin.auxiliary.binary_data.delta.file.FileDataSource;
import org.exbin.auxiliary.binary_data.array.paged.ByteArrayPagedData;

/**
 * Repository for swap file.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class SwapFilePages {

    private static final String DEFAULT_SWAPFILE_PREFIX = "swap";
    private static final String DEFAULT_SWAPFILE_SUFFIX = "";
    private static final int MAX_UNUSED_PAGES = 20;

    private int pageSize = ByteArrayPagedData.DEFAULT_PAGE_SIZE;

    private File swapFile = null;
    private RandomAccessFile accessFile;
    private long usedPages = 0;

    private final List<SwapMovingListener> swapMovingListeners = new ArrayList<>();
    private final int[] unusedPages = new int[MAX_UNUSED_PAGES];
    private int unusedPagesCount = 0;

    public SwapFilePages() {
        initSwapFile(DEFAULT_SWAPFILE_PREFIX, DEFAULT_SWAPFILE_SUFFIX, null);
    }

    public SwapFilePages(int pageSize) {
        this.pageSize = pageSize;
        initSwapFile(DEFAULT_SWAPFILE_PREFIX, DEFAULT_SWAPFILE_SUFFIX, null);
    }

    private void initSwapFile(String prefix, String suffix, @Nullable File directory) {
        try {
            swapFile = File.createTempFile(prefix, suffix, directory);
            accessFile = new RandomAccessFile(swapFile, FileDataSource.EditMode.READ_WRITE.getFileAccessMode());
        } catch (IOException ex) {
            Logger.getLogger(SwapFilePages.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void close() {
        try {
            accessFile.close();
        } catch (IOException ex) {
            Logger.getLogger(SwapFilePages.class.getName()).log(Level.SEVERE, null, ex);
        }
        swapFile.delete();
    }

    public long allocatePage() {
        long page;
        if (unusedPagesCount > 0) {
            unusedPagesCount--;
            page = unusedPages[unusedPagesCount];
        } else {
            page = usedPages;
            usedPages++;
        }
        return page;
    }

    public void releasePage(int pageIndex) {
        if (unusedPagesCount == MAX_UNUSED_PAGES) {
            sweepUnused();
        }

        unusedPages[unusedPagesCount] = pageIndex;
        unusedPagesCount++;
    }

    private void sweepUnused() {
        for (int i = unusedPagesCount - 1; i >= 0; i--) {
            long sourcePage = usedPages;
            long targetPage = unusedPages[i];
            copyPage(sourcePage, targetPage);
            notifyPageMoving(sourcePage, targetPage);
            usedPages--;
        }
        shrinkFile();
    }

    private void notifyPageMoving(long sourcePage, long targetPage) {
        swapMovingListeners.forEach((swapMovingListener) -> {
            swapMovingListener.pageMoved(sourcePage, targetPage);
        });
    }

    private void copyPage(long sourcePage, long targetPage) {
        try {
            byte[] page = new byte[pageSize];
            accessFile.seek(sourcePage * pageSize);
            accessFile.readFully(page, 0, pageSize);
            accessFile.seek(targetPage * pageSize);
            accessFile.write(page, 0, pageSize);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SwapFilePages.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SwapFilePages.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void shrinkFile() {
        try {
            accessFile.setLength(usedPages * pageSize);
        } catch (IOException ex) {
            Logger.getLogger(SwapFilePages.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Nonnull
    public byte[] getPage(long pageIndex) {
        byte[] page = new byte[pageSize];
        getPage(pageIndex, page);
        return page;
    }

    @Nonnull
    public byte[] getPagePart(long pageIndex, int length) {
        byte[] page = new byte[length];
        getPage(pageIndex, page);
        return page;
    }

    public long getUsedPages() {
        return usedPages;
    }

    public void getPage(long pageIndex, byte[] targetData) {
        getPage(pageIndex, targetData, 0);
    }

    public void getPage(long pageIndex, byte[] targetData, int offset) {
        if (targetData.length == 0) {
            throw new IllegalArgumentException("Reading empty data is not allowed");
        }
        if (targetData.length + offset > pageSize) {
            throw new OutOfBoundsException("Cannot read data page over it's size");
        }
        try {
            long pagePosition = pageIndex * pageSize;
            if (accessFile.length() <= pagePosition) {
                Arrays.fill(targetData, offset, targetData.length - offset, (byte) 0);
            } else {
                accessFile.seek(pagePosition);
                accessFile.readFully(targetData, offset, targetData.length - offset);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SwapFilePages.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SwapFilePages.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setPage(long pageIndex, byte[] pageData) {
        setPage(pageIndex, pageData, 0);
    }

    public void setPage(long pageIndex, byte[] pageData, int offset) {
        if (pageData.length == 0) {
            throw new IllegalArgumentException("Cannot set empty data as page");
        }
        try {
            long pagePosition = pageIndex * pageSize;
            if (pagePosition > accessFile.length()) {
                accessFile.setLength(pagePosition);
            }

            accessFile.seek(pagePosition);
            accessFile.write(pageData, offset, pageData.length);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SwapFilePages.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SwapFilePages.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addSwapMovingListener(SwapMovingListener listener) {
        swapMovingListeners.add(listener);
    }

    public void removeSwapMovingListener(SwapMovingListener listener) {
        swapMovingListeners.remove(listener);
    }

    public static interface SwapMovingListener {

        void pageMoved(long sourcePage, long targetPage);
    }
}
