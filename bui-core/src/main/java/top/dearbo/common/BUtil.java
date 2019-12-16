package top.dearbo.common;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import top.dearbo.common.log.Logger;

/**
 * Created by Dan on 2019/7/22 16:52
 * 全局工具管理
 */
public class BUtil {

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    @SuppressLint("StaticFieldLeak")
    private static volatile BUtil sInstance;

    private static Application sApplication;

    //private ActivityLifecycleHelper mActivityLifecycleHelper;
    /**
     * 主线程Handler
     */
    private static final Handler sMainHandler = new Handler(Looper.getMainLooper());

    private BUtil() {
        // mActivityLifecycleHelper = new ActivityLifecycleHelper();
    }

    /**
     * 初始化工具
     *
     * @param application
     */
    public static void init(Application application) {
        sApplication = application;
        sContext = application.getApplicationContext();
        //application.registerActivityLifecycleCallbacks(BUtil.getInstance().getActivityLifecycleHelper());
    }

    /**
     * 初始化工具
     *
     * @param context
     */
    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }

    /**
     * 注册activity的生命回调
     *
     * @param application
     * @param lifecycleHelper
     * @return
     */
    /*public BUtil registerLifecycleCallbacks(Application application, ActivityLifecycleHelper lifecycleHelper) {
        mActivityLifecycleHelper = lifecycleHelper;
        application.registerActivityLifecycleCallbacks(mActivityLifecycleHelper);
        return this;
    }*/

    /**
     * 获取全局上下文
     *
     * @return Context全局
     */
    public static Context getContext() {
        testInitialize();
        return sContext;
    }

    /*public ActivityLifecycleHelper getActivityLifecycleHelper() {
        return mActivityLifecycleHelper;
    }*/

    private static void testInitialize() {
        if (sContext == null) {
            throw new ExceptionInInitializerError("请先在全局Application中调用 BUtil.init() 初始化！");
        }
    }

    /**
     * 设置日志记录
     */
    public static void debug(boolean isDebug) {
        if (isDebug) {
            debug(Logger.DEFAULT_LOG_TAG);
        } else {
            debug("");
        }
    }

    /**
     * 设置日志记录
     *
     * @param tag
     */
    public static void debug(String tag) {
        Logger.debug(tag);
    }

    /**
     * 获取主线程的Handler
     *
     * @return Handler
     */
    public static Handler getMainHandler() {
        return sMainHandler;
    }

    /**
     * 在主线程中执行
     *
     * @param runnable
     * @return true/false
     */
    public static boolean runOnUiThread(Runnable runnable) {
        return getMainHandler().post(runnable);
    }

    /**
     * 是否是主线程
     *
     * @return
     */
    public static boolean isMainLooper() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * 获取实例
     *
     * @return BUtil
     */
    public static BUtil getInstance() {
        if (sInstance == null) {
            synchronized (BUtil.class) {
                if (sInstance == null) {
                    sInstance = new BUtil();
                }
            }
        }
        return sInstance;
    }

    public static Application getApplication() {
        return sApplication;
    }

    private Object readResolve() {
        return sInstance;
    }
}
