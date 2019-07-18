package com.dan.dome;

import android.app.Application;

import com.dan.library.exception.CrashCatchHandler;
import com.xuexiang.xutil.XUtil;
import com.zhouyou.http.EasyHttp;

/**
 * Created by Bo on 2019/1/18 15:21
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        //全局异常
        CrashCatchHandler.getInstance().init(this, "dome");
    }

    private void init() {
        //默认初始化
        EasyHttp.init(this);
        XUtil.init(this);
    }

}
