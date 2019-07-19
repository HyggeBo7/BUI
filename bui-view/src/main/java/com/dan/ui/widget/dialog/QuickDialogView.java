package com.dan.ui.widget.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Dan on 2019/7/16 11:21
 */
public abstract class QuickDialogView {

    private View layoutView;
    private Context mContext;

    public QuickDialogView(Context context, int layoutId) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutView = inflater.inflate(layoutId, null);
        this.mContext = context;
    }

    public View getLayoutView() {
        return layoutView;
    }

    public Context getContext() {
        return mContext;
    }

    public void setTitle(String title) {

    }

    public abstract void onBindView(Context context, View layoutView);

}
