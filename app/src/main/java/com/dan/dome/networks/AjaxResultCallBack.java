package com.dan.dome.networks;

import android.util.Log;

import com.dan.library.entity.AjaxResult;
import com.dan.library.networks.HttpStatusCode;
import com.dan.library.util.JsonUtil;
import com.xuexiang.xutil.tip.ToastUtils;
import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.exception.ApiException;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Bo on 2019/3/29 9:21
 */
public abstract class AjaxResultCallBack<T> extends CallBack<String> {

    private static final String TAG = "AjaxResultCallBack";

    @Override
    public void onStart() {
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(ApiException e) {
        if (e.getCode() < ApiException.UNKNOWN) {
            e.setDisplayMessage(HttpStatusCode.getHttpStatusMsg(e.getCode()));
        } else {
            e.setDisplayMessage(e.getMessage());
        }
        onResult(false);
        onErrorResult(e);
    }

    @Override
    public void onSuccess(String json) {
        AjaxResult ajaxResult = null;
        boolean successFlag = true;
        ApiException apiException = null;
        if (StringUtils.isNotBlank(json)) {
            try {
                ajaxResult = JsonUtil.fromJson(json, AjaxResult.class);
            } catch (Exception e) {
                successFlag = false;
                apiException = ApiException.handleException(e);
                apiException.setDisplayMessage("数据解析失败!");
                Log.i(TAG, "onSuccess:json:" + json + ",解析异常:" + e.getMessage());
            }
        }
        onResult(successFlag);
        if (successFlag) {
            onSuccessResult(ajaxResult, json);
        } else {
            onErrorResult(apiException);
        }
    }

    protected void onResult(boolean successFlag) {

    }

    public void onErrorResult(ApiException e) {
        ToastUtils.toast(e.getDisplayMessage());
    }

    public abstract void onSuccessResult(AjaxResult result, String str);
}
