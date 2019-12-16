package com.dan.html.util;

import com.dan.common.entity.AjaxResult;
import com.dan.common.util.JsonUtil;

import java.util.Collection;

import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.util.JSUtil;

/**
 * @fileName: JSUtils.java
 * @author: Bo
 * @createDate: 2018/9/12 15:57
 * @description: 扩展原生的 JSUtil
 */
public class JSUtils extends JSUtil {

    public static String wrapJsVarData(Object data) {
        return wrapJsVar(new AjaxResult(data));
    }

    public static String wrapJsVar(AjaxResult ajaxResult) {
        return JSUtil.wrapJsVar(ajaxResult == null ? "" : ajaxResult.toString());
    }

    public static String wrapJsVar(Collection value) {
        return wrapJsVar(JsonUtil.toJson(value), false);
    }

    public static String wrapJsVar(int value) {
        return wrapJsVar(String.valueOf(value), false);
    }

    ///////////异步回调/////////////

    public static void execCallbackOk(IWebview iWebview, String callbackId, String ajaxResult, boolean b) {
        execCallbackOk(iWebview, callbackId, new AjaxResult(ajaxResult), b);
    }

    public static void execCallbackOkData(IWebview iWebview, String callbackId, Object data) {
        execCallbackOkData(iWebview, callbackId, data, false);
    }

    public static void execCallbackOkData(IWebview iWebview, String callbackId, Object data, boolean b) {
        execCallback(iWebview, callbackId, JSUtils.toJsResponseText(new AjaxResult(data).toString()), JSUtil.OK, b);
    }

    public static void execCallbackOk(IWebview iWebview, String callbackId, AjaxResult ajaxResult) {
        execCallback(iWebview, callbackId, JSUtils.toJsResponseText(ajaxResult.toString()), JSUtil.OK, false);
    }

    public static void execCallbackOk(IWebview iWebview, String callbackId, AjaxResult ajaxResult, boolean b) {
        execCallback(iWebview, callbackId, JSUtils.toJsResponseText(ajaxResult.toString()), JSUtil.OK, b);
    }

    public static void execCallbackError(IWebview iWebview, String callbackId, String ajaxResult, boolean b) {
        execCallbackError(iWebview, callbackId, new AjaxResult(ajaxResult), b);
    }

    public static void execCallbackError(IWebview iWebview, String callbackId, AjaxResult ajaxResult, boolean b) {
        execCallback(iWebview, callbackId, JSUtils.toJsResponseText(JsonUtil.toJson(ajaxResult)), JSUtil.ERROR, b);
    }
}
