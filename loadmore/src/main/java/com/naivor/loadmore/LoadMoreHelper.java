package com.naivor.loadmore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.naivor.adapter.HeaderFooter;

import static com.naivor.loadmore.LoadMoreState.COMPLETE;
import static com.naivor.loadmore.LoadMoreState.ERROR;
import static com.naivor.loadmore.LoadMoreState.LOADING;
import static com.naivor.loadmore.LoadMoreState.NOMOREDATA;
import static com.naivor.loadmore.LoadMoreState.ORIGIN;

/**
 * 加载更多的辅助类
 * <p>
 * Created by tianlai on 17-4-6.
 */

public class LoadMoreHelper implements LoadMoreOperator {
    private final String TAG = this.getClass().getSimpleName();

    private static int ORIGIN_INDEX = 0;


    private ViewGroup dataView;

    private LoadMoreView moreView;

    private boolean isListType;

    private int index;

    private OnLoadMoreListener onLoadMoreListener;

    private int state;

    public LoadMoreHelper(Context context) {

        moreView = new LoadMoreView(context);
        moreView.reset();
    }

    /**
     * 初始化分页索引
     *
     * @param index
     */
    public static void initIndex(int index) {
        ORIGIN_INDEX = index;
    }

    /**
     * 设置目标view
     *
     * @param view
     */
    public void target(ViewGroup view) {
        if (view == null) {
            throw new NullPointerException("dataView  is  null");
        } else if (view instanceof ListView) {
            isListType = true;
        } else if (view instanceof RecyclerView) {
            isListType = false;
        } else {
            throw new IllegalAccessError(view.getClass().getCanonicalName() + " 不支持");
        }

        this.dataView = view;

        initView();
    }

    /**
     * 初始化
     */
    @SuppressLint("NewApi")
    private void initView() {
        if (isListType) {
            ListView listView = (ListView) this.dataView;

            listView.addFooterView(moreView);

            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


                    execLoadMore(view, getScrollY(view));
                }

                public int getScrollY(AbsListView view) {
                    View c = view.getChildAt(0);
                    if (c == null) {
                        return 0;
                    }
                    int firstVisiblePosition = view.getFirstVisiblePosition();
                    int top = c.getTop();
                    return -top + firstVisiblePosition * c.getHeight();
                }
            });

        } else {
            RecyclerView recyclerView = (RecyclerView) this.dataView;

            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (adapter == null) {
                throw new NullPointerException("please set a Adapter to RecyclerView before call target()");
            }

            if (adapter instanceof HeaderFooter) {
                ((HeaderFooter) adapter).addFooterView(moreView);
            } else {
                AdapterDelegate delegate = new AdapterDelegate(adapter);
                delegate.setFooterView(moreView);

                recyclerView.setAdapter(delegate);
            }

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView view, int dx, int dy) {
                    execLoadMore(view, dy);
                }
            });
        }

        moreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == ERROR && onLoadMoreListener != null) {
                    changeState(LOADING);
                    onLoadMoreListener.onReload(getIndex());
                }
            }
        });

        reset();
    }

    /**
     * 加载更多的处理
     *
     * @param v
     * @param scrollY
     */
    private void execLoadMore(View v, int scrollY) {
        if (isScrollBottom(v, scrollY)) {

            if (state == NOMOREDATA) {
                changeState(NOMOREDATA);

                Log.i(TAG, "没有下一页了");
            } else {
                Log.i(TAG, "加载下一页");
                changeState(LOADING);
                if (onLoadMoreListener != null) {
                    onLoadMoreListener.onLoadMore(getNext());
                }
            }

        }
    }

    /**
     * 是否滑到底部
     *
     * @param v
     * @param scrollY
     * @return
     */
    private boolean isScrollBottom(View v, int scrollY) {

        return scrollY > 0
                && moreView.getVisibility() != View.VISIBLE
                && v.getMeasuredHeight() <= scrollY + v.getHeight()
                && !canChildScrollDown(v);
    }

    /**
     * 改变加载状态
     */
    private void changeState(int newState) {
        this.state = newState;

        switch (state) {
            case ORIGIN:
                moreView.reset();
                index = ORIGIN_INDEX;
                break;
            case LOADING:
                moreView.loading();
                break;
            case ERROR:
                moreView.loadError();
                break;
            case COMPLETE:
                moreView.loadComplete();
                break;
            case NOMOREDATA:
                if (needShowNoMore()) {
                    moreView.noMoreData();
                }
                break;
        }

    }

    /**
     * 是否需要显示没有更多数据，数据不满一屏不显示
     *
     * @return
     */
    private boolean needShowNoMore() {
        if (isListType) {
            return ((ListView) dataView).getFirstVisiblePosition() > 0;
        } else {
            RecyclerView.LayoutManager layoutManager = ((RecyclerView) dataView).getLayoutManager();
            if (layoutManager != null && layoutManager instanceof LinearLayoutManager) {
                return ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition() > 0;
            }

            return false;
        }
    }

    /**
     * 滑到底部判断
     *
     * @param view
     * @return
     */
    public static boolean canChildScrollDown(View view) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (view instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) view;
                int childCount = absListView.getChildCount();

                return childCount > 1
                        && (absListView.getLastVisiblePosition() < childCount || absListView.getChildAt(childCount)
                        .getBottom() > absListView.getPaddingBottom());

            } else {
                return view.getScrollY() < view.getMeasuredHeight();
            }
        } else {
            return view.canScrollVertically(1);
        }
    }

    /**
     * dataView 的类型
     *
     * @return
     */
    private boolean isListType() {
        if (dataView == null) {
            throw new NullPointerException("dataView  is  null,you may not call traget()");
        }

        return isListType;
    }

    public int getIndex() {
        return index;
    }

    public int getNext() {
        return ++index;
    }


    /**
     * 是否正在加载中
     *
     * @return
     */
    public boolean isLoading() {
        return moreView.getVisibility() == View.VISIBLE &&
                state == LOADING;
    }

    public void setHasMore(boolean hasMore) {
        if (!hasMore) {
            changeState(NOMOREDATA);
        }
    }

    public boolean isLoadMore() {
        return index > ORIGIN_INDEX;
    }

    /**
     * 加载完成
     */
    @Override
    public void loadComplete() {
        if (state == NOMOREDATA) {
            changeState(NOMOREDATA);
        } else {
            changeState(COMPLETE);
        }
    }

    /**
     * 加载出错
     */
    @Override
    public void loadError() {
        changeState(ERROR);
    }

    /**
     * 重置
     */
    @Override
    public void reset() {
        changeState(ORIGIN);
    }

    /**
     * 执行加载
     */
    @Override
    public void loading() {
        changeState(LOADING);

    }

    /**
     * 没有更多数据
     */
    @Override
    public void noMoreData() {
        changeState(NOMOREDATA);
    }

    public String getErrorHint() {
        return moreView.getErrorHint();
    }

    public void setErrorHint(String errorHint) {
        moreView.setErrorHint(errorHint);
    }

    public String getLoadingHint() {
        return moreView.getLoadingHint();
    }

    public void setLoadingHint(String loadingHint) {
        moreView.setLoadingHint(loadingHint);
    }

    public String getNoMoreDataHint() {
        return moreView.getNoMoreDataHint();
    }

    public void setNoMoreDataHint(String noMoreDataHint) {
        moreView.setNoMoreDataHint(noMoreDataHint);
    }

    public Drawable getLoadingDrawable() {
        return moreView.getLoadingDrawable();
    }

    public void setLoadingDrawable(Drawable loadingDrawable) {
        moreView.setLoadingDrawable(loadingDrawable);
    }

    public OnLoadMoreListener getOnLoadMoreListener() {
        return onLoadMoreListener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


}
