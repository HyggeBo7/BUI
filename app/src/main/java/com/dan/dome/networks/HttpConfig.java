package com.dan.dome.networks;

import com.dan.dome.util.SystemApplication;

/**
 * Created by Bo on 2019/1/17 11:43
 */
public class HttpConfig {

    //服务器api
    public static String REST_URL = "http://119.37.194.4:5555";

    //api
    public static String REST_API = "/xtp-api";

    public static String getRestUrl() {
        return SystemApplication.getUrl() == null ? REST_URL : SystemApplication.getUrl();
    }

    public static void setRestUrl(String restUrl) {
        REST_URL = restUrl;
    }

    /**
     * 登录验证  参数:userName,pwd
     */
    public static String getLoginUrl() {
        return getRestUrl() + REST_API + "/user/loginUser";
    }

    public static String getUser() {
        return getRestUrl() + REST_API + "/user/getUser";
    }

}
