package com.dan.dome.activity.base;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.dan.dome.R;
import com.dan.library.util.ActivityUtil;
import com.dan.library.util.DialogUtils;
import com.dan.library.util.StatusBarUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Bo on 2018/10/17 13:06
 */
public class BaseActivity extends Activity {

    public Dialog mLoading;
    protected Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.head_background_back_all);
        ActivityUtil.getInstance().addActivity(this);

        mLoading = DialogUtils.createLoadingDialog(this);
        //软件盘自动打开
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    /**
     * setContentView 这个方法后执行此方法
     */
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        //绑定当前视图
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
        ActivityUtil.getInstance().removeActivity(this);
    }

}
