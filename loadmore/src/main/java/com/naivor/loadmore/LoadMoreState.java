package com.naivor.loadmore;

/**
 * Created by tianlai on 17-4-6.
 */

public class LoadMoreState {
    /**
     * 初始状态
     */
    public static final int ORIGIN = 0;
    /**
     * 加载中
     */
    public static final int LOADING = 1;
    /**
     * 加载完成
     */
    public static final int COMPLETE = 2;
    /**
     * 加载失败
     */
    public static final int ERROR = 3;
    /**
     * 没有更多数据
     */
    public static final int NOMOREDATA = 4;

}
