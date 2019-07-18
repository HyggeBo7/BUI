package com.dan.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by Bo on 2019/2/25 15:17
 */
public class SwipeMenuViewHolder {
    private SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;
    private Context mContext;
    private int mLayoutId;

    public SwipeMenuViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mContext = context;
        this.mLayoutId = layoutId;
        this.mPosition = position;
        this.mViews = new SparseArray();
        this.mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        this.mConvertView.setTag(this);
    }

    public static SwipeMenuViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new SwipeMenuViewHolder(context, parent, layoutId, position);
        } else {
            SwipeMenuViewHolder holder = (SwipeMenuViewHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    public int getPosition() {
        return this.mPosition;
    }

    public int getLayoutId() {
        return this.mLayoutId;
    }

    public <T extends View> T findViewById(int viewId) {
        View view = this.mViews.get(viewId);
        if (view == null) {
            view = this.mConvertView.findViewById(viewId);
            this.mViews.put(viewId, view);
        }

        return (T) view;
    }

    public View getConvertView() {
        return this.mConvertView;
    }

    public SwipeMenuViewHolder setSelected(int viewId, boolean flag) {
        View v = this.findViewById(viewId);
        v.setSelected(flag);
        return this;
    }

    public SwipeMenuViewHolder setText(int viewId, String text) {
        TextView tv = (TextView) this.findViewById(viewId);
        tv.setText(text);
        return this;
    }

    public SwipeMenuViewHolder setImageResource(int viewId, int resId) {
        ImageView view = (ImageView) this.findViewById(viewId);
        view.setImageResource(resId);
        return this;
    }

    public SwipeMenuViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = (ImageView) this.findViewById(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public SwipeMenuViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = (ImageView) this.findViewById(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public SwipeMenuViewHolder setBackgroundColor(int viewId, int color) {
        View view = this.findViewById(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public SwipeMenuViewHolder setBackgroundRes(int viewId, int backgroundRes) {
        View view = this.findViewById(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public SwipeMenuViewHolder setTextColor(int viewId, int textColor) {
        TextView view = (TextView) this.findViewById(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public SwipeMenuViewHolder setTextColorRes(int viewId, int textColorRes) {
        TextView view = (TextView) this.findViewById(viewId);
        view.setTextColor(this.mContext.getResources().getColor(textColorRes));
        return this;
    }

    public SwipeMenuViewHolder setAlpha(int viewId, float value) {

        if (VERSION.SDK_INT >= 11) {
            this.findViewById(viewId).setAlpha(value);
        } else {
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0L);
            alpha.setFillAfter(true);
            this.findViewById(viewId).startAnimation(alpha);
        }

        return this;
    }

    public SwipeMenuViewHolder setVisible(int viewId, boolean visible) {
        View view = this.findViewById(viewId);

        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public SwipeMenuViewHolder linkify(int viewId) {
        TextView view = (TextView) this.findViewById(viewId);

        Linkify.addLinks(view, Linkify.ALL);

        return this;
    }

    public SwipeMenuViewHolder setTypeface(Typeface typeface, int... viewIds) {
        int[] var3 = viewIds;
        int var4 = viewIds.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            int viewId = var3[var5];
            TextView view = (TextView) this.findViewById(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | 128);
        }

        return this;
    }

    public SwipeMenuViewHolder setProgress(int viewId, int progress) {
        ProgressBar view = (ProgressBar) this.findViewById(viewId);
        view.setProgress(progress);
        return this;
    }

    public SwipeMenuViewHolder setProgress(int viewId, int progress, int max) {
        ProgressBar view = (ProgressBar) this.findViewById(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public SwipeMenuViewHolder setMax(int viewId, int max) {
        ProgressBar view = (ProgressBar) this.findViewById(viewId);
        view.setMax(max);
        return this;
    }

    public SwipeMenuViewHolder setRating(int viewId, float rating) {
        RatingBar view = (RatingBar) this.findViewById(viewId);
        view.setRating(rating);
        return this;
    }

    public SwipeMenuViewHolder setRating(int viewId, float rating, int max) {
        RatingBar view = (RatingBar) this.findViewById(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    public SwipeMenuViewHolder setTag(int viewId, Object tag) {
        View view = this.findViewById(viewId);
        view.setTag(tag);
        return this;
    }

    public SwipeMenuViewHolder setTag(int viewId, int key, Object tag) {
        View view = this.findViewById(viewId);
        view.setTag(key, tag);
        return this;
    }

    public SwipeMenuViewHolder setChecked(int viewId, boolean checked) {
        Checkable view = (Checkable) this.findViewById(viewId);
        view.setChecked(checked);
        return this;
    }

    public SwipeMenuViewHolder setOnClickListener(int viewId, OnClickListener listener) {
        View view = this.findViewById(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public SwipeMenuViewHolder setOnTouchListener(int viewId, OnTouchListener listener) {
        View view = this.findViewById(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public SwipeMenuViewHolder setOnLongClickListener(int viewId, OnLongClickListener listener) {
        View view = this.findViewById(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }
}
