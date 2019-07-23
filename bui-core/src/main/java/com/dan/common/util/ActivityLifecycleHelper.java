package com.dan.common.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;

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
public class ActivityLifecycleHelper extends BaseSerializable implements Application.ActivityLifecycleCallbacks {

    private Stack<Activity> activityList;

    public ActivityLifecycleHelper() {
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
    public void finishActivity(@NonNull Class<? extends Activity> clazz) {
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
        if (activityList != null) {
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

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Logger.v("[onActivityCreated]:" + getActivityName(activity));
        addActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Logger.v("[onActivityStarted]:" + getActivityName(activity));
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Logger.v("[onActivityResumed]:" + getActivityName(activity));
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Logger.v("[onActivityPaused]:" + getActivityName(activity));
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Logger.v("[onActivityStopped]:" + getActivityName(activity));
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Logger.v("[onActivitySaveInstanceState]:" + getActivityName(activity));
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Logger.v("[onActivityDestroyed]:" + getActivityName(activity));
        removeActivity(activity);
    }

    private String getActivityName(Object object) {
        return object != null ? object.getClass().getName() : "NULL";
    }
}
