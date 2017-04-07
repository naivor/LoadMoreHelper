package com.naivor.loadmore;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 加载更多的view
 * <p>
 * Created by tianlai on 17-4-5.
 */

public class LoadMoreView extends FrameLayout implements LoadMoreOperator {

    private String errorHint;
    private String loadingHint;
    private String noMoreDataHint;

    private Drawable loadingDrawable;

    private LayoutInflater inflater;

    private TextView tvContent;

    private ProgressBar pbAnimate;

    public LoadMoreView(Context context) {
        this(context, null);
    }

    public LoadMoreView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LoadMoreView, defStyleAttr, 0);
        int attrCount = a.getIndexCount();
        for (int i = 0; i < attrCount; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.LoadMoreView_errorHint) {
                errorHint = a.getString(attr);

            } else if (attr == R.styleable.LoadMoreView_loadingHint) {
                loadingHint = a.getString(attr);

            } else if (attr == R.styleable.LoadMoreView_noMoreDataHint) {
                noMoreDataHint = a.getString(attr);

            } else if (attr == R.styleable.LoadMoreView_loadDrawable) {
                loadingDrawable = a.getDrawable(attr);

            }
        }

        a.recycle();

        initView(context);
    }

    /**
     * 初始化
     */
    private void initView(Context context) {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(context, 60));
        params.gravity = Gravity.CENTER;
        setLayoutParams(params);

        inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.load_more_layout, this, false);

        tvContent = (TextView) view.findViewById(R.id.more_tv_content);
        pbAnimate = (ProgressBar) view.findViewById(R.id.more_pb_anim);

        addView(view);

        if (TextUtils.isEmpty(errorHint)) {
            errorHint = context.getString(R.string.loading_error);
        }

        if (TextUtils.isEmpty(loadingHint)) {
            loadingHint = context.getString(R.string.loading_message);
        }

        if (TextUtils.isEmpty(noMoreDataHint)) {
            noMoreDataHint = context.getString(R.string.no_more_data);
        }

        if (loadingDrawable == null) {
            loadingDrawable = context.getResources().getDrawable(R.drawable.progress_dialog);
        }
    }


    public String getErrorHint() {
        return errorHint;
    }

    public void setErrorHint(String errorHint) {
        this.errorHint = errorHint;
    }

    public String getLoadingHint() {
        return loadingHint;
    }

    public void setLoadingHint(String loadingHint) {
        this.loadingHint = loadingHint;
    }

    public String getNoMoreDataHint() {
        return noMoreDataHint;
    }

    public void setNoMoreDataHint(String noMoreDataHint) {
        this.noMoreDataHint = noMoreDataHint;
    }

    public Drawable getLoadingDrawable() {
        return loadingDrawable;
    }

    public void setLoadingDrawable(Drawable loadingDrawable) {
        this.loadingDrawable = loadingDrawable;
    }

    /**
     * 获取屏幕像素密度相对于标准屏幕(160dpi)倍数
     *
     * @param context 当前上下文对象
     * @return float 屏幕像素密度
     */
    @SuppressWarnings("deprecation")
    public float getScreenDensity(Context context) {

        return context.getResources().getDisplayMetrics().density;
    }


    /**
     * 将dp转换成px
     *
     * @param context 当前上下文对象
     * @param dp      dp值
     * @return px的值
     */
    public int dp2px(Context context, float dp) {
        return (int) (getScreenDensity(context) * dp + 0.5f);
    }

    /**
     * 加载完成
     */
    @Override
    public void loadComplete() {
        setVisibility(GONE);
    }

    /**
     * 加载出错
     */
    @Override
    public void loadError() {
        setVisibility(VISIBLE);
        pbAnimate.setVisibility(GONE);
        tvContent.setText(errorHint);
        setEnabled(true);
    }

    /**
     * 重置
     */
    @Override
    public void reset() {
        setVisibility(GONE);
    }

    /**
     * 执行加载
     */
    @Override
    public void loading() {
        setVisibility(VISIBLE);
        pbAnimate.setVisibility(VISIBLE);
        tvContent.setText(loadingHint);
        setEnabled(false);
    }

    /**
     * 没有更多数据
     */
    @Override
    public void noMoreData() {

        setVisibility(VISIBLE);
        pbAnimate.setVisibility(GONE);
        tvContent.setText(noMoreDataHint);
        setEnabled(false);
    }
}
