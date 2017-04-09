package com.naivor.loadmore;

/**
 * 加载更多的监听
 *
 * Created by tianlai on 17-4-7.
 */

public  interface OnLoadMoreListener {
    /**
     * 加载下一页
     *
     * @param next
     */
    void onLoadMore(int next);

}