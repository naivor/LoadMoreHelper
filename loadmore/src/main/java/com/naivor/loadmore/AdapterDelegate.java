package com.naivor.loadmore;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * recycler adapter 的代理类，方便给recycler 添加 footer
 * <p>
 * Created by tianlai on 17-4-7.
 */

public class AdapterDelegate extends RecyclerView.Adapter {

    private static final int TYPE_FOOTER = Integer.MAX_VALUE;

    private View footerView;

    private RecyclerView.Adapter realAdapter;

    private RecyclerView.AdapterDataObserver dataObserver = new RecyclerView.AdapterDataObserver() {

        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            notifyItemRangeChanged(fromPosition, toPosition, itemCount);
        }
    };

    public AdapterDelegate(RecyclerView.Adapter adapter) {

        if (adapter == null) {
            throw new NullPointerException("Adapter can't be null");
        }

        if (realAdapter != null) {
            realAdapter.unregisterAdapterDataObserver(dataObserver);
        }

        realAdapter = adapter;

        realAdapter.registerAdapterDataObserver(dataObserver);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            return new ViewHolder(footerView);
        }

        return realAdapter.onCreateViewHolder(parent, viewType);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isFooterView(position)) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();

            if (layoutParams == null) {
                RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.bottomMargin = 1;
                params.topMargin = 1;
                holder.itemView.setLayoutParams(params);
            }
        } else {
            realAdapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isFooterView(position)) {
            return TYPE_FOOTER;
        }
        return realAdapter.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return realAdapter.getItemCount() + 1;
    }

    public void setFooterView(View footerView) {

        this.footerView = footerView;
    }

    public boolean isFooterView(int position) {
        return (position == getItemCount() - 1) && position >= 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }


}
