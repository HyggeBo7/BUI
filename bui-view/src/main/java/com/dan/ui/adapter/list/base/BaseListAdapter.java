package com.dan.ui.adapter.list.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Dan on 2019/7/23 16:21
 */
public abstract class BaseListAdapter<T, H> extends AbstractListAdapter<T> {

    public BaseListAdapter(Context context) {
        super(context);
    }

    public BaseListAdapter(Context context, List<T> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        H holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), getLayoutId(), null);
            holder = newViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (H) convertView.getTag();
        }
        T item = getItem(position);
        if (item != null) {
            convert(holder, item, position);
        }
        return convertView;
    }

    /**
     * 创建ViewHolder
     *
     * @param convertView
     * @return
     */
    protected abstract H newViewHolder(View convertView);

    /**
     * 获取适配的布局ID
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 转换布局
     *
     * @param holder
     * @param item
     * @param position
     */
    protected abstract void convert(H holder, T item, int position);
}
