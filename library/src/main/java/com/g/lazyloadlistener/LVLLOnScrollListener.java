/*
 * Copyright 2017 Vasyl Glodan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.g.lazyloadlistener;

import android.widget.AbsListView;

public abstract class LVLLOnScrollListener implements AbsListView.OnScrollListener {

    private int bunchSize;
    private int startLoadOffset;

    private boolean isLoading, isLastCallFailed;
    private int loadedInLastBunch;

    private int loadedCount;

    public LVLLOnScrollListener(int bunchSize) {
        this.bunchSize = bunchSize;
    }

    public LVLLOnScrollListener(int bunchSize, int startLoadOffset) {
        this.bunchSize = bunchSize;
        this.startLoadOffset = startLoadOffset;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (totalItemCount != 0 && loadedInLastBunch < bunchSize) {
            return;
        }

        boolean offsetReached = totalItemCount - (firstVisibleItem + visibleItemCount) <= startLoadOffset;

        if (totalItemCount > 0 && offsetReached && !isLoading && !isLastCallFailed) {
            loadedCount += bunchSize;
            isLoading = true;
            onLoad(loadedCount);
        }
    }

    /**
     * This method will be triggered when user reaches end of list / startLoadOffset
     * @param offset Number of last loaded item
     */
    public abstract void onLoad(int offset);

    /**
     * Call this method when getting new items was failed
     */
    public void notifyFail() {
        isLoading = false;
        isLastCallFailed = true;
    }

    /**
     * Call this method when getting new items was successful
     * @param lastBunchSize Size of last bunch to detect end of list
     */
    public void notifyFinish(int lastBunchSize) {
        isLoading = isLastCallFailed = false;
        loadedInLastBunch = lastBunchSize;
    }

    /**
     * Resets listener
     */
    public void reset() {
        loadedInLastBunch = loadedCount = 0;
        isLoading = isLastCallFailed = false;
    }

    /**
     * Returns count of loaded items
     * @return Count of loaded items
     */
    public int getLoadedCount() {
        return loadedCount;
    }
}
