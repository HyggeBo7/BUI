package com.dan.dome.util;

import android.app.Application;

import com.dan.dome.entity.User;
import com.dan.library.util.JsonUtil;

/**
 * Created by Johnny on 2017/9/27.
 */

public class SystemApplication extends Application {

    public static String userAccount;
    public static String dataToken;
    public static String userPassword;
    public static User user;
    public static String url;

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        SystemApplication.url = url;
    }

    public static User getUser() {
        return user;
    }

    public static String getUserPassword() {
        return userPassword;
    }

    public static void setUserPassword(String userPassword) {
        SystemApplication.userPassword = userPassword;
    }

    public static String getUserAccount() {
        return userAccount;
    }

    public static void setUserAccount(String userAccount) {
        SystemApplication.userAccount = userAccount;
    }

    public static String getDataToken() {
        return dataToken;
    }

    public static void setDataToken(final String dataToken) {
        SystemApplication.dataToken = dataToken;
    }

    public static String toPrint() {
        return "SystemApplication{userAccount:" + userAccount + ",dataToken:" + dataToken + ",user:" + JsonUtil.toJson(user) + "}";
    }
}
