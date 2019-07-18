package com.dan.ui.widget.grouplist;

import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by Bo on 2019/2/25 9:58
 * PopupWindow在7.0的手机上弹出位置异常
 */
public class MyPopupWindow extends PopupWindow {

    public MyPopupWindow(View contentView, int width, int height) {
        this(contentView, width, height, false);
    }

    public MyPopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    /**
     * 重写 showAsDropDown方法
     */
    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor, xoff, yoff);
    }
}
