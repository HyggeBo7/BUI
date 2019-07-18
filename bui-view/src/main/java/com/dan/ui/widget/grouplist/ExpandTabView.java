package com.dan.ui.widget.grouplist;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dan.ui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单控件头部，封装了下拉动画，动态生成头部按钮个数
 *
 * @author yueyueniao
 */

public class ExpandTabView extends LinearLayout implements OnDismissListener {
    private static final String TAG = "ExpandTabView";
    private ToggleButton selectedButton;
    private List<String> mTextArray = new ArrayList<String>();
    private List<RelativeLayout> mViewArray = new ArrayList<RelativeLayout>();
    private List<ToggleButton> mToggleButton = new ArrayList<ToggleButton>();
    private Context mContext;
    private final int SMALL = 0;
    private int displayWidth;
    private int displayHeight;
    private MyPopupWindow popupWindow;
    private int selectPosition;
    /**
     * false:禁用,true:不禁用【默认：true】
     */
    private boolean toggleButtonEnabledFlag = true;

    public ExpandTabView(Context context) {
        super(context);
        init(context);
    }

    public ExpandTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 根据选择的位置设置tabitem显示的值
     */
    public void setTitle(String valueText, int position) {
        if (position < mToggleButton.size()) {
            mToggleButton.get(position).setText(valueText);
        }
    }

    public void setTitle(String title) {

    }

    /**
     * 根据选择的位置获取tabitem显示的值
     */
    public String getTitle(int position) {
        if (position < mToggleButton.size() && mToggleButton.get(position).getText() != null) {
            return mToggleButton.get(position).getText().toString();
        }
        return "";
    }

    /**
     * 获取按钮
     *
     * @param index 下标
     */
    public ToggleButton getToggleButtons(int index) {
        if (index > -1 && index < mToggleButton.size()) {
            return mToggleButton.get(index);
        }
        return null;
    }

    /**
     * 获取全部按钮
     */
    public List<ToggleButton> getToggleButtons() {
        return mToggleButton;
    }

    /**
     * 设置按钮禁用
     */
    public void setCreateToggleButtonEnabled(boolean toggleButtonEnabledFlag) {
        this.toggleButtonEnabledFlag = toggleButtonEnabledFlag;
    }

    public boolean setToggleButton(int index, boolean enabled) {
        if (index > -1 && index < mToggleButton.size()) {
            mToggleButton.get(index).setEnabled(enabled);
            return true;
        }
        return false;
    }

    public boolean setToggleButton(boolean enabled) {
        if (mToggleButton.size() > 0) {
            for (ToggleButton toggleButton : mToggleButton) {
                toggleButton.setEnabled(enabled);
            }
            return true;
        }
        return false;
    }

    /**
     * 设置tabitem的个数和初始值
     */
    public void setValue(List<String> textArray, List<View> viewArray) {
        if (mContext == null) {
            return;
        }
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mTextArray = textArray;
        for (int i = 0; i < viewArray.size(); i++) {
            final RelativeLayout r = new RelativeLayout(mContext);
            int maxHeight = (int) (displayHeight * 0.7);
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, maxHeight);
            rl.leftMargin = 10;
            rl.rightMargin = 10;
            r.addView(viewArray.get(i), rl);
            mViewArray.add(r);
            r.setTag(SMALL);
            ToggleButton tButton = (ToggleButton) inflater.inflate(R.layout.toggle_button, this, false);
            addView(tButton);
            View line = new TextView(mContext);
            line.setBackgroundResource(R.drawable.etv_expand_line);
            if (i < viewArray.size() - 1) {
                LayoutParams lp = new LayoutParams(2, LayoutParams.MATCH_PARENT);
                addView(line, lp);
            }
            mToggleButton.add(tButton);
            tButton.setTag(i);
            tButton.setText(mTextArray.get(i));
            tButton.setEnabled(toggleButtonEnabledFlag);

            r.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPressBack();
                }
            });

            r.setBackgroundColor(mContext.getResources().getColor(R.color.popup_main_background));
            tButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    // initPopupWindow();
                    ToggleButton tButton = (ToggleButton) view;

                    if (selectedButton != null && selectedButton != tButton) {
                        selectedButton.setChecked(false);
                    }
                    selectedButton = tButton;
                    selectPosition = (Integer) selectedButton.getTag();
                    startAnimation();
                    if (mOnButtonClickListener != null && tButton.isChecked()) {
                        mOnButtonClickListener.onClick(selectPosition);
                    }
                }
            });
        }
    }

    private void startAnimation() {

        if (popupWindow == null) {
            popupWindow = new MyPopupWindow(mViewArray.get(selectPosition), displayWidth, displayHeight);
            popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);

            popupWindow.setFocusable(false);
            popupWindow.setOutsideTouchable(true);
        }

        if (selectedButton.isChecked()) {
            if (!popupWindow.isShowing()) {
                showPopup(selectPosition);
            } else {
                popupWindow.setOnDismissListener(this);
                popupWindow.dismiss();
                hideView();
            }
        } else {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
                hideView();
            }
        }
    }

    private void showPopup(int position) {
        View tView = mViewArray.get(selectPosition).getChildAt(0);
        if (tView instanceof ViewBaseAction) {
            ViewBaseAction f = (ViewBaseAction) tView;
            f.show();
        }
        if (popupWindow.getContentView() != mViewArray.get(position)) {
            popupWindow.setContentView(mViewArray.get(position));
        }
        popupWindow.showAsDropDown(this, 0, 0);
    }

    /**
     * 如果菜单成展开状态，则让菜单收回去
     */
    public boolean onPressBack() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            hideView();
            if (selectedButton != null) {
                selectedButton.setChecked(false);
            }
            return true;
        } else {
            return false;
        }

    }

    private void hideView() {
        View tView = mViewArray.get(selectPosition).getChildAt(0);
        if (tView instanceof ViewBaseAction) {
            ViewBaseAction f = (ViewBaseAction) tView;
            f.hide();
        }
    }

    private void init(Context context) {
        mContext = context;
        //displayWidth = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
        //displayHeight = ((Activity) mContext).getWindowManager().getDefaultDisplay().getHeight();
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        displayWidth = metrics.widthPixels;
        displayHeight = metrics.heightPixels;
        Log.i(TAG, "屏幕宽度：metrics:宽度:x=" + displayWidth + ";高:y=" + displayHeight);
        setOrientation(LinearLayout.HORIZONTAL);
    }

    @Override
    public void onDismiss() {
        showPopup(selectPosition);
        popupWindow.setOnDismissListener(null);
    }

    private OnButtonClickListener mOnButtonClickListener;

    /**
     * 设置tabitem的点击监听事件
     */
    public void setOnButtonClickListener(OnButtonClickListener l) {
        mOnButtonClickListener = l;
    }

    /**
     * 自定义tabitem点击回调接口
     */
    public interface OnButtonClickListener {
        public void onClick(int selectPosition);
    }

}
