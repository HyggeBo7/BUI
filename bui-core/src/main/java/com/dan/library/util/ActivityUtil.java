package com.dan.library.util;

import android.app.Activity;

import com.dan.library.entity.BaseSerializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bo on 2018/10/17.
 */

public class ActivityUtil extends BaseSerializable {

    private static ActivityUtil INSTANCE;

    private List<Activity> activityList;

    private ActivityUtil() {
        activityList = new ArrayList<>();
    }

    public static ActivityUtil getInstance() {
        if (null == INSTANCE) {
            synchronized (ActivityUtil.class) {
                if (null == INSTANCE) {
                    INSTANCE = new ActivityUtil();
                }
            }
        }
        return INSTANCE;
    }

    public List<Activity> getActivityList() {
        return activityList;
    }

    public void addActivity(Activity activity) {
        if (activityList.size() > 0) {
            if (!activityList.contains(activity)) {
                activityList.add(activity);
            }
        } else {
            activityList.add(activity);
        }
    }

    public void removeActivity(Activity activity) {
        if (activityList.size() > 0) {
            activityList.remove(activity);
        }
    }

    public void finishAll() {
        if (activityList.size() > 0) {
            for (Activity activity : activityList) {
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }
        }
    }
}
