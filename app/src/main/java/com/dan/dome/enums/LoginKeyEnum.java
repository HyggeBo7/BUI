package com.dan.dome.enums;

/**
 * Created by Bo on 2019/2/19 14:57
 */
public enum LoginKeyEnum {

    USER_NAME("userNameKey", "用户名"),
    PWD("pwdKey", "密码"),
    REMEMBER_PWD("rememberPwdKey", "记住密码"),
    AUTO_LOGIN("autoLoginKey", "自动登录");

    LoginKeyEnum(String key, String msg) {
        this.key = key;
        this.msg = msg;
    }

    public String key;
    public String msg;

}
