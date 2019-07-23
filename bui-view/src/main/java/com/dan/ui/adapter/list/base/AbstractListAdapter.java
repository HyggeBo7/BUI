package com.dan.ui.adapter.list.base;

import android.content.Context;
import android.widget.BaseAdapter;

import com.dan.ui.adapter.base.BaseDataAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2019/7/23 15:43
 */
public abstract class AbstractListAdapter<T> extends BaseAdapter implements BaseDataAdapter<T> {

    private Context mContext;

    /**
     * 数据
     */
    private List<T> mList;

    public AbstractListAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<>();
    }

    public AbstractListAdapter(Context context, List<T> data) {
        mContext = context;
        setData(data);
    }

    @Override
    public int getCount() {
        return this.mList != null ? this.mList.size() : 0;
    }

    @Override
    public T getItem(int position) {
        if (checkIndex(position)) {
            return this.mList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return this.mList != null ? (long) position : -1;
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
        if (checkIndex(index)) {
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
        if (checkNotEmpty()) {
            if (remove(this.mList)) {
                return notifyDataChanged();
            }
        }
        return false;
    }

    public boolean remove(List<T> list) {
        if (checkNotEmpty(list)) {
            list.clear();
            return true;
        }
        return false;
    }

    @Override
    public T getItemData(int index) {
        return getItem(index);
    }

    @Override
    public List<T> getDataList() {
        return this.mList;
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
        if (checkNotEmpty()) {
            this.mList.clear();
            notifyDataChanged();
        }
    }

    private boolean checkNotEmpty() {
        return checkNotEmpty(this.mList);
    }

    private boolean checkNotEmpty(List<T> list) {
        return list != null && list.size() > 0;
    }

    private boolean checkIndex(int index) {

        return this.mList != null && index > -1 && index < this.mList.size();
    }

    private boolean notifyDataChanged() {
        this.notifyDataSetChanged();
        return true;
    }

    public Context getContext() {
        return mContext;
    }
}
