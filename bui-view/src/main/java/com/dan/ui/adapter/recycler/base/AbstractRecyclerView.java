package com.dan.ui.adapter.recycler.base;

import android.support.v7.widget.RecyclerView;

import com.dan.ui.adapter.base.BaseDataAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bo
 * @email wubodanran@163.com
 * @desc Recycler适配器-实现对数据操作封装
 * @date 2019/7/12 14:31
 */
public abstract class AbstractRecyclerView<T> extends RecyclerView.Adapter<AbstractViewHolder> implements BaseDataAdapter<T> {

    protected List<T> mList;

    @Override
    public int getItemCount() {
        return this.mList != null ? this.mList.size() : 0;
    }

    @Override
    public void addFirst(T item) {
        if (item != null) {
            if (this.mList.size() == 0) {
                this.mList.add(item);
            } else {
                this.mList.add(0, item);
            }
            ////更新数据集不是用adapter.notifyDataSetChanged()而是notifyItemInserted(position)与notifyItemRemoved(position) 否则没有动画效果。
            notifyItemInserted(0);
            //notifyItemRangeChanged(0, 1);
        }
    }

    @Override
    public void addFirst(List<T> collection) {
        if (collection != null && collection.size() > 0) {
            if (this.mList.size() == 0) {
                this.mList.addAll(collection);
            } else {
                this.mList.addAll(0, collection);
            }
            notifyItemRangeInserted(0, collection.size());
            //notifyDataChanged();
        }
    }

    @Override
    public void add(T item) {
        if (item != null) {
            this.mList.add(item);
            notifyItemInserted(this.mList.size() - 1);
            //notifyDataChanged();
        }
    }

    @Override
    public void add(List<T> collection) {
        if (collection != null && collection.size() > 0) {
            int oldIndex = this.mList.size();
            this.mList.addAll(collection);
            notifyItemRangeInserted(oldIndex, collection.size());
            //notifyDataChanged();
        }
    }

    @Override
    public boolean update(T item, int index) {
        if (checkIndex(index) && item != null) {
            this.mList.set(index, item);
            notifyItemChanged(index);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(int index) {
        if (checkIndex(index)) {
            this.mList.remove(index);
            notifyItemRemoved(index);
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    @Override
    public boolean remove() {
        if (this.mList != null && this.mList.size() > 0) {
            if (customizeRemove(this.mList)) {
                notifyDataSetChanged();
                return true;
            }
        }
        return false;
    }

    @Override
    public T getItemData(int index) {
        if (checkIndex(index)) {
            return mList.get(index);
        }
        return null;
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
            int previousSize = this.mList.size();
            this.mList.clear();
            notifyItemRangeRemoved(0, previousSize);
        }
        if (collection != null && collection.size() > 0) {
            this.mList.addAll(collection);
            notifyItemRangeInserted(0, collection.size());
        }
        //notifyDataChanged();
    }

    @Override
    public void clear() {
        if (checkNotEmpty()) {
            int previousSize = this.mList.size();
            this.mList.clear();
            notifyItemRangeRemoved(0, previousSize);
        }
    }

    /**
     * 用于自定义条件删除,调用时通过重写
     */
    public boolean customizeRemove(List<T> list) {
        if (checkNotEmpty()) {
            int previousSize = this.mList.size();
            this.mList.clear();
            notifyItemRangeRemoved(0, previousSize);
            return true;
        }
        return false;
    }

    public boolean checkIndex(int index) {

        return this.mList != null && index > -1 && index < this.mList.size();
    }

    private boolean checkNotEmpty() {
        return checkNotEmpty(this.mList);
    }

    private boolean checkNotEmpty(List<T> mList) {
        return mList != null && mList.size() > 0;
    }

    private boolean checkNotNull() {
        return this.mList != null;
    }

    private boolean checkNotNull(List<T> list) {
        return this.mList != null && list != null;
    }

    private boolean notifyDataChanged() {
        this.notifyDataSetChanged();
        return true;
    }
}
