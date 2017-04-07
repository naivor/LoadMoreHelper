package com.naivor.loadmorehelper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.naivor.adapter.AdapterOperator;
import com.naivor.adapter.RecyAdapter;
import com.naivor.adapter.RecyHolder;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * recyclerAdapter测试
 * <p>
 * Created by tianlai on 17-4-2.
 */

public class TestRecyAdapter extends RecyAdapter<SimpleItem> {


    public TestRecyAdapter(Context context) {
        super(context);
    }

    /**
     * 创建viewholder,需要重写
     *
     * @param view
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder createHolder(View view, int viewType) {
        return new SHolder(view);
    }

    /**
     * 获取布局资源
     *
     * @param viewType
     * @return
     */
    @Override
    public int getLayoutRes(int viewType) {
        return R.layout.list_item_txt;
    }


    static class SHolder extends RecyHolder<SimpleItem> {

        @Bind(R.id.tv_text)
        TextView tvText;

        public SHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindData(AdapterOperator<SimpleItem> operator, int position, SimpleItem itemData) {
            super.bindData(operator, position, itemData);

            tvText.setText(itemData.getContent() + "编号：" + position);
        }
    }
}
