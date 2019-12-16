package top.dearbo.common.toast.style;

import android.content.Context;

/**
 *    author : Bo
 *    time   : 2018/09/01
 *    desc   : 默认黑色样式实现
 */
public class ToastBlackStyle extends BaseToastStyle {

    public ToastBlackStyle(Context context) {
        super(context);
    }

    @Override
    public int getCornerRadius() {
        return dp2px(5);
    }

    @Override
    public int getBackgroundColor() {
        return 0X88000000;
    }

    @Override
    public int getTextColor() {
        return 0XEEFFFFFF;
    }

    @Override
    public float getTextSize() {
        return sp2px(16);
    }

    @Override
    public int getPaddingStart() {
        return dp2px(16);
    }

    @Override
    public int getPaddingTop() {
        return dp2px(10);
    }
}