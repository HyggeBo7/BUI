package com.dan.ui.adapter.recycler.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bo
 * @email wubodanran@163.com
 * @desc 通用Recycler适配器
 * @date 2019/7/12 14:31
 */
public abstract class QuickRecyclerAdapter<T> extends AbstractRecyclerView<T> {

    private Context context;
    private int layoutId;
    //事件
    private OnItemClickListener<T> onItemClickListener;
    private OnItemLongClickListener<T> onItemLongClickListener;

    public QuickRecyclerAdapter(Context context) {
        this.context = context;
        this.mList = new ArrayList<>();
    }

    public QuickRecyclerAdapter(Context context, int layoutId) {
        this.context = context;
        this.layoutId = layoutId;
        this.mList = new ArrayList<>();
    }

    public QuickRecyclerAdapter(Context context, int layoutId, List<T> mList) {
        this.context = context;
        this.layoutId = layoutId;
        this.mList = mList;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public QuickViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //R.layout.recycler_item_book_shelf
        QuickViewHolder viewHolder = QuickViewHolder.get(context, parent, layoutId);
        this.onCreateView(viewHolder, viewHolder.mConvertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        QuickViewHolder quickViewHolder = (QuickViewHolder) holder;
        final T itemData = getItemData(position);
        this.onBindViewHolder(quickViewHolder, itemData, position);
        if (onItemClickListener != null) {
            quickViewHolder.mConvertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, itemData, position);
                }
            });
        }
        if (onItemLongClickListener != null) {
            quickViewHolder.mConvertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClickListener.onItemLongClick(v, itemData, position);
                    //表示此事件已经消费，不会触发单击事件
                    return true;
                }
            });
        }
    }

    /**
     * 设置单击事件
     */
    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 设置双击事件
     */
    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    /**
     * onCreateView 创建执行
     */
    public void onCreateView(QuickViewHolder viewHolder, View view) {

    }

    /**
     * 绑定事件执行
     */
    public abstract void onBindViewHolder(QuickViewHolder viewHolder, T t, int position);

    /**
     * 单击事件
     */
    public interface OnItemClickListener<T> {
        void onItemClick(View view, T t, int position);

    }

    /**
     * 双击事件
     */
    public interface OnItemLongClickListener<T> {

        void onItemLongClick(View view, T t, int position);
    }

    public static class QuickViewHolder extends AbstractViewHolder {
        private SparseArray<View> mViews;
        private View mConvertView;

        public QuickViewHolder(View itemView) {
            super(itemView);
            this.mConvertView = itemView;
            this.mViews = new SparseArray<>();
        }

        public static QuickViewHolder get(Context context, ViewGroup parent, int layoutId) {
            View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
            return new QuickViewHolder(view);
        }

        public <T extends View> T findViewById(int id) {
            View v = mViews.get(id);
            if (v == null) {
                v = mConvertView.findViewById(id);
                mViews.put(id, v);
            }
            return (T) v;
        }

        public QuickViewHolder setText(int viewId, String text) {
            TextView tv = this.findViewById(viewId);
            tv.setText(text);
            return this;
        }

        public QuickViewHolder setVisible(int viewId, boolean visible) {
            View view = this.findViewById(viewId);
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
            return this;
        }

    }

}
