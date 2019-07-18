package com.dan.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dan.ui.R;
import com.dan.ui.adapter.impl.SpinnerTextFormatter;

import java.util.List;


public class SearchSelectAdapter<T> extends BaseAdapter {
    private List<T> mList;
    private Context context;
    private LayoutInflater inflater;
    private SpinnerTextFormatter spinnerTextFormatter;

    public SearchSelectAdapter(Context ctx) {
        this(ctx, null, new SimpleSpinnerTextFormatter());
    }

    public SearchSelectAdapter(Context ctx, List<T> mList) {
        this(ctx, mList, new SimpleSpinnerTextFormatter());
    }

    public SearchSelectAdapter(Context ctx, List<T> mList, SpinnerTextFormatter spinnerTextFormatter) {
        this.context = ctx;
        this.mList = mList;
        this.inflater = LayoutInflater.from(ctx);
        this.spinnerTextFormatter = spinnerTextFormatter;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.item_list_select_single, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (mList != null && mList.size() > 0) {
            holder.info.setText(spinnerTextFormatter.format(mList.get(i)).toString());
        }
        return view;
    }

    public List<T> getItemAll() {
        return this.mList;
    }

    public void setData(List<T> collection) {
        if (this.mList == null) {
            this.mList = collection;
        } else {
            this.mList.clear();
            if (collection != null) {
                this.mList.addAll(collection);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 添加一个元素
     */
    public void add(T item) {
        if (item != null) {
            this.mList.add(item);
        }
        notifyDataSetChanged();
    }

    /**
     * 添加多个元素
     */
    public void add(List<T> collection) {
        if (collection != null) {
            this.mList.addAll(collection);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        if (this.mList != null) {
            this.mList.clear();
            notifyDataSetChanged();
        }
    }

    static class ViewHolder {
        TextView info;

        public ViewHolder(View view) {
            info = view.findViewById(R.id.tv_select_info);
        }
    }

}
