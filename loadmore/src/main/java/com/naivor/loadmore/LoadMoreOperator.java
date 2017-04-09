package com.naivor.loadmore;

/**
 * 加载更多的接口
 * <p>
 * Created by tianlai on 17-4-5.
 */

public interface LoadMoreOperator {

    /**
     * 加载完成
     */
    void loadComplete();

    /**
     * 加载出错
     */
    void loadError();

    /**
     * 重置
     */
    void reset();

    /**
     * 执行加载
     */
    void loading();

    /**
     * 没有更多数据
     */
    void noMoreData();

}
