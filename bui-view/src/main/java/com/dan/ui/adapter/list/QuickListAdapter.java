package com.dan.ui.adapter.list;

import android.content.Context;
import android.os.Build;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.TextView;

import com.dan.ui.adapter.list.base.AbstractListAdapter;

import java.util.List;

/**
 * Created by Bo on 2019/1/21 18:14
 */
public abstract class QuickListAdapter<T> extends AbstractListAdapter<T> {

    /**
     * 布局id
     */
    private int layoutId;

    public QuickListAdapter(Context context) {
        super(context);
    }

    public QuickListAdapter(Context context, int layoutId) {
        super(context);
        this.layoutId = layoutId;
    }

    public QuickListAdapter(Context context, List<T> list, int layoutId) {
        super(context, list);
        this.layoutId = layoutId;
    }

    public void setLayoutId(int mLayoutId) {
        this.layoutId = mLayoutId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuickViewHolder holder = QuickViewHolder.get(getContext(), convertView, parent, this.layoutId);
        T item = getItem(position);
        if (item != null) {
            this.onBindViewHolder(holder, item, position);
        }
        return holder.getConvertView();
    }

    public abstract void onBindViewHolder(QuickViewHolder holder, T t, int position);

    public static class QuickViewHolder {
        private SparseArray<View> mViews;
        private View itemView;
        private Context mContext;

        public QuickViewHolder(Context context, ViewGroup parent, int layoutId) {
            this.mContext = context;
            this.mViews = new SparseArray<>();
            //this.itemView = View.inflate(context, layoutId, null);
            this.itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            this.itemView.setTag(this);
        }

        public static QuickViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId) {
            if (convertView == null) {
                return new QuickViewHolder(context, parent, layoutId);
            } else {
                return (QuickViewHolder) convertView.getTag();
            }
        }

        public View getConvertView() {
            return this.itemView;
        }

        /**
         * 寻找控件
         *
         * @param viewId
         * @param <T>
         * @return
         */
        public <T extends View> T findViewById(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = itemView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        /**
         * 寻找控件
         *
         * @param id id
         * @return View
         */
        public View findView(@IdRes int id) {
            return id == 0 ? itemView : findViewById(id);
        }

        /**
         * 设置文字
         *
         * @param id
         * @param sequence
         * @return
         */
        public QuickViewHolder setText(int id, CharSequence sequence) {
            View view = findView(id);
            if (view instanceof TextView) {
                ((TextView) view).setText(sequence);
            }
            return this;
        }

        /**
         * 设置文字
         *
         * @param id
         * @param stringRes
         * @return
         */
        public QuickViewHolder setText(@IdRes int id, @StringRes int stringRes) {
            View view = findView(id);
            if (view instanceof TextView) {
                ((TextView) view).setText(stringRes);
            }
            return this;
        }

        public QuickViewHolder setAlpha(int viewId, float value) {

            if (Build.VERSION.SDK_INT >= 11) {
                this.findViewById(viewId).setAlpha(value);
            } else {
                AlphaAnimation alpha = new AlphaAnimation(value, value);
                alpha.setDuration(0L);
                alpha.setFillAfter(true);
                this.findViewById(viewId).startAnimation(alpha);
            }

            return this;
        }

        public QuickViewHolder setVisible(int viewId, boolean visible) {
            View view = this.findViewById(viewId);

            view.setVisibility(visible ? View.VISIBLE : View.GONE);
            return this;
        }

        public QuickViewHolder setChecked(int viewId, boolean checked) {
            Checkable view = (Checkable) this.findViewById(viewId);
            view.setChecked(checked);
            return this;
        }

        public QuickViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
            View view = this.findViewById(viewId);
            view.setOnClickListener(listener);
            return this;
        }

        public QuickViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
            View view = this.findViewById(viewId);
            view.setOnTouchListener(listener);
            return this;
        }

        public QuickViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
            View view = this.findViewById(viewId);
            view.setOnLongClickListener(listener);
            return this;
        }


        /**
         * 清除控件缓存
         */
        public void clearViews() {
            if (mViews != null) {
                mViews.clear();
            }
        }
    }

}
