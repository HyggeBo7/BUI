package com.dan.ui.adapter.recycler;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * @author Bo
 * @email wubodanran@163.com
 * @desc RecyclerView帮助类
 * @date 2019/7/12 15:01
 */
public class RecyclerViewUtil {

    public static void vertical(Context context, RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }

    public static void horizontal(Context context, RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    }

    public static void grid(Context context, int spanCount, RecyclerView rv) {
        rv.setLayoutManager(new GridLayoutManager(context, spanCount, GridLayoutManager.VERTICAL, false));
    }
}
