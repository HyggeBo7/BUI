package com.dan.common.util;

import android.app.Activity;

import com.dan.common.entity.BaseSerializable;
import com.dan.common.log.Logger;

import java.util.Iterator;
import java.util.Stack;

/**
 * <pre>
 *     desc   : activity生命周期管理
 *     author : Bo
 *     time   : 2019年7月22日17:09:04
 * </pre>
 */
public class ActivityHelper extends BaseSerializable {

    private static volatile ActivityHelper INSTANCE;

    private Stack<Activity> activityList;

    public static ActivityHelper getInstance() {
        if (INSTANCE == null) {
            synchronized (ActivityHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ActivityHelper();
                }
            }
        }
        return INSTANCE;
    }

    private ActivityHelper() {
        activityList = new Stack<>();
    }

    public Stack<Activity> getActivityAll() {
        return activityList;
    }

    /**
     * 添加Activity到堆栈
     */
    private void addActivity(Activity activity) {
        if (activityList == null) {
            activityList = new Stack<>();
        }
        activityList.add(activity);
    }

    /**
     * 将Activity移出堆栈
     *
     * @param activity
     */
    private void removeActivity(Activity activity) {
        if (activity != null) {
            activityList.remove(activity);
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity getCurrentActivity() {
        return activityList.lastElement();
    }

    /**
     * 获取上一个Activity
     *
     * @return
     */
    public Activity getPreActivity() {
        int size = activityList.size();
        if (size < 2) {
            return null;
        }
        return activityList.elementAt(size - 2);
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishCurrentActivity() {
        finishActivity(getCurrentActivity());
    }

    /**
     * 结束上一个Activity
     */
    public void finishPreActivity() {
        finishActivity(getPreActivity());
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityList.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定的Activity
     *
     * @param clazz activity的类
     */
    public void finishActivity(Class<? extends Activity> clazz) {
        if (activityList != null) {
            Iterator<Activity> it = activityList.iterator();
            synchronized (it) {
                while (it.hasNext()) {
                    Activity activity = it.next();
                    if (clazz.getCanonicalName().equals(activity.getClass().getCanonicalName())) {
                        if (!activity.isFinishing()) {
                            it.remove();
                            activity.finish();
                        }
                    }
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityList != null && activityList.size() > 0) {
            for (Activity activity : activityList) {
                if (activity != null) {
                    if (!activity.isFinishing()) {
                        Logger.d("[FinishActivity]:" + getActivityName(activity));
                        activity.finish();
                    }
                }
            }
            activityList.clear();
        }
    }

    /**
     * 退出
     */
    public void exit() {
        finishAllActivity();
    }

    private String getActivityName(Object object) {
        return object != null ? object.getClass().getName() : "NULL";
    }
}
