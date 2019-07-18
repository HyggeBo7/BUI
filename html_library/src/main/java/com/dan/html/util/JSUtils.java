package com.dan.html.util;

import com.dan.library.entity.AjaxResult;
import com.dan.library.util.JsonUtil;

import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.util.JSUtil;

/**
 * @fileName: JSUtils.java
 * @author: Bo
 * @createDate: 2018/9/12 15:57
 * @description: 扩展原生的 JSUtil
 */
public class JSUtils extends JSUtil {

    public static void execCallbackOk(IWebview iWebview, String callbackId, String ajaxResult, boolean b) {
        execCallbackOk(iWebview, callbackId, new AjaxResult(ajaxResult), b);
    }

    public static void execCallbackOk(IWebview iWebview, String callbackId, Object ajaxResult, boolean b) {
        execCallback(iWebview, callbackId, JSUtils.toJsResponseText(JsonUtil.toJson(ajaxResult)), JSUtil.OK, b);
    }

    public static void execCallbackError(IWebview iWebview, String callbackId, String ajaxResult, boolean b) {
        execCallbackError(iWebview, callbackId, new AjaxResult(ajaxResult), b);
    }

    public static void execCallbackError(IWebview iWebview, String callbackId, AjaxResult ajaxResult, boolean b) {
        execCallback(iWebview, callbackId, JSUtils.toJsResponseText(JsonUtil.toJson(ajaxResult)), JSUtil.ERROR, b);
    }
}
