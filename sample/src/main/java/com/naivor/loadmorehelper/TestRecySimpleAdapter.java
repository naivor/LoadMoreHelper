package com.naivor.loadmorehelper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * recyclerAdapter测试
 * <p>
 * Created by tianlai on 17-4-2.
 */

public class TestRecySimpleAdapter extends RecyclerView.Adapter<TestRecySimpleAdapter.SHolder> {

    private LayoutInflater inflater;

    private List<SimpleItem> itemDatas;

    public TestRecySimpleAdapter(Context context) {
        inflater = LayoutInflater.from(context);

        itemDatas = new ArrayList<>();
    }


    @Override
    public SHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SHolder(inflater.inflate(R.layout.list_item_txt, parent, false));
    }


    @Override
    public void onBindViewHolder(SHolder holder, int position) {
        holder.tvText.setText(itemDatas.get(position).getContent() + "编号：" + position);
    }


    @Override
    public int getItemCount() {
        return itemDatas.size();
    }

    public void setItems(List<SimpleItem> list) {
        this.itemDatas.clear();
        if(list != null) {
            this.itemDatas.addAll(list);
        }

        this.notifyDataSetChanged();
    }

    public void addItems(List<SimpleItem> list) {
        if(list != null) {
            this.itemDatas.addAll(list);
        }

        this.notifyDataSetChanged();
    }


    static class SHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_text)
        TextView tvText;

        public SHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

    }
}
