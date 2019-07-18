package com.dan.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dan.ui.adapter.base.BaseDataAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bo on 2019/2/25 15:08
 */
public abstract class SwipeMenuAdapter<T> extends BaseAdapter implements BaseDataAdapter<T> {
    private Context mContext;
    private List<T> mList;
    private LayoutInflater mInflater;
    private int layoutId;

    public SwipeMenuAdapter(Context context, int layoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.layoutId = layoutId;
        this.mList = new ArrayList<>();
    }

    public SwipeMenuAdapter(Context context, List<T> dataList, int layoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mList = dataList;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return this.mList != null ? this.mList.size() : 0;
    }

    @Override
    public T getItem(int position) {
        if (this.mList != null && position > -1 && position < this.mList.size()) {
            return this.mList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return this.mList != null ? (long) position : -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SwipeMenuViewHolder holder = SwipeMenuViewHolder.get(this.mContext, convertView, parent, this.layoutId, position);
        this.onBindViewHolder(holder, this.getItem(position), position);
        return holder.getConvertView();
    }

    public abstract void onBindViewHolder(SwipeMenuViewHolder viewHolder, T t, int position);

    public boolean remove(List<T> list) {
        if (this.mList != null && this.mList.size() > 0) {
            this.mList.clear();
            return true;
        }
        return false;
    }

    @Override
    public void addFirst(T item) {
        if (item != null) {
            if (this.mList.size() == 0) {
                this.mList.add(item);
            } else {
                this.mList.add(0, item);
            }
            notifyDataChanged();
        }
    }

    @Override
    public void addFirst(List<T> collection) {
        if (collection != null) {
            if (this.mList.size() == 0) {
                this.mList.addAll(collection);
            } else {
                this.mList.addAll(0, collection);
            }
            notifyDataChanged();
        }
    }

    @Override
    public void add(T item) {
        if (item != null) {
            this.mList.add(item);
            notifyDataChanged();
        }
    }

    @Override
    public void add(List<T> collection) {
        if (collection != null) {
            this.mList.addAll(collection);
            notifyDataChanged();
        }
    }

    @Override
    public boolean update(T item, int index) {
        if (checkIndex(index) && item != null) {
            this.mList.set(index, item);
            return notifyDataChanged();
        }
        return false;
    }

    @Override
    public boolean remove(int index) {
        if (checkIndex(index)) {
            this.mList.remove(index);
            return notifyDataChanged();
        }
        return false;
    }

    @Override
    public boolean remove() {
        if (this.mList != null && this.mList.size() > 0) {
            if (remove(this.mList)) {
                return notifyDataChanged();
            }
        }
        return false;
    }

    @Override
    public T getItemData(int index) {
        return getItem(index);
    }

    @Override
    public void setData(List<T> collection) {
        if (this.mList == null) {
            this.mList = new ArrayList<>();
        }
        if (this.mList.size() > 0) {
            this.mList.clear();
        }
        if (collection != null && collection.size() > 0) {
            this.mList.addAll(collection);
        }
        notifyDataChanged();
    }

    @Override
    public void clear() {
        if (this.mList != null) {
            this.mList.clear();
            notifyDataChanged();
        }
    }

    @Override
    public List<T> getDataList() {
        return this.mList;
    }

    private boolean checkIndex(int index) {

        return this.mList != null && index > -1 && index < this.mList.size();
    }

    private boolean notifyDataChanged() {
        this.notifyDataSetChanged();
        return true;
    }

}