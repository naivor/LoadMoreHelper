package com.naivor.loadmorehelper;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.naivor.adapter.AdapterOperator;
import com.naivor.adapter.ListAdapter;
import com.naivor.adapter.ListHolder;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by naivor on 16-5-21.
 */
public class TestListAdapter extends ListAdapter<SimpleItem> {

    public TestListAdapter(Context context) {
        super(context);
    }

    /**
     * 创建viewholder
     *
     * @param view
     * @param viewType
     * @return
     */
    @Override
    public ListHolder<SimpleItem> onCreateViewHolder(View view, int viewType) {
        return new HomeViewHolder(view);
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

    /**
     * Created by naivor on 16-5-21.
     */
    static class HomeViewHolder extends ListHolder<SimpleItem> {

        @Bind(R.id.tv_text)
        TextView tvText;

        public HomeViewHolder(View convertView) {
            super(convertView);

            ButterKnife.bind(this, convertView);

        }

        @Override
        public void bindData(AdapterOperator<SimpleItem> operator, int position, SimpleItem itemData) {
            super.bindData(operator, position, itemData);

            tvText.setText(itemData.getContent() + "编号：" + position);
        }
    }

}
