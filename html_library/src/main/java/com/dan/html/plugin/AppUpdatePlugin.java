package com.dan.html.plugin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;

import com.blankj.utilcode.util.PermissionUtils;
import com.dan.html.util.JSUtils;
import com.dan.library.appupdate.model.UpdateBean;
import com.dan.library.appupdate.util.DownloadAppUtils;
import com.dan.library.customview.ConfirmDialog;
import com.dan.library.entity.AjaxResult;
import com.dan.library.util.JsonUtil;
import com.dan.library.util.PermissionsUtil;
import com.dan.library.util.ToastUtil;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;

import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.DHInterface.StandardFeature;


/**
 * Created by Bo on 2018/11/9 14:02
 */
public class AppUpdatePlugin extends StandardFeature {

    /**
     * 判断是否有权限
     *
     * @param iWebview
     * @param jsonArray
     */
    @SuppressLint("WrongConstant")
    public void checkPermissions(final IWebview iWebview, JSONArray jsonArray) {
        //回调
        final String callbackId = jsonArray.optString(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String[] permissionsList = new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES};
            PermissionsUtil permissionsUtil = new PermissionsUtil(iWebview.getActivity());
            if (!permissionsUtil.checkPermission(permissionsList)) {
                PermissionUtils.permission(permissionsList).callback(new PermissionUtils.SimpleCallback() {
                    /**
                     * 请求成功
                     */
                    @Override
                    public void onGranted() {
                        JSUtils.execCallbackOk(iWebview, callbackId, new AjaxResult(-1, "权限请求成功!"), false);
                    }

                    /**
                     * 请求失败
                     */
                    @Override
                    public void onDenied() {
                        JSUtils.execCallbackError(iWebview, callbackId, new AjaxResult(-1, "权限请求失败!"), false);
                    }
                }).request();
            }
        }
        //如果当前安卓版本大于26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //先判断是否有安装未知来源应用的权限
            boolean installPermissionFlag = getDPluginContext().getPackageManager().canRequestPackageInstalls();
            if (!installPermissionFlag) {
                //弹框提示用户手动打开
                ConfirmDialog.showAlert(iWebview.getActivity(), "安装权限", "需要打开允许来自此来源，请去设置中开启此权限", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toInstallPermissionSettingIntent(iWebview.getActivity());
                    }
                });
            }
        }
        JSUtils.execCallbackOk(iWebview, callbackId, new AjaxResult(1, "授权成功"), false);
    }

    /**
     * 开启安装未知来源权限
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void toInstallPermissionSettingIntent(Activity activity) {
        Uri packageURI = Uri.parse("package:" + getDPluginContext().getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        activity.startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {
        super.onActivityResult(i, i1, intent);
        System.out.println("onActivityResult===========安装...");
        ToastUtil.makeText(getDPluginContext(), "安装应用");
    }

    /**
     * 下载
     *
     * @param jsonArray ["回调id","下载app参数(json对象)"]
     */
    public void appUpdateAndInstall(final IWebview iWebview, final JSONArray jsonArray) {
        //0.回调
        String callbackId = jsonArray.optString(0);

        UpdateBean updateBean = null;
        if (jsonArray.length() > 1) {
            String json = jsonArray.optString(1);
            if (StringUtils.isNotBlank(json)) {
                updateBean = JsonUtil.fromJson(json, UpdateBean.class);
            }
        }
        if (null == updateBean) {
            updateBean = new UpdateBean();
        }
        //设置参数
        setUpdateBean(updateBean);
        DownloadAppUtils.startUpdate(iWebview.getActivity(), updateBean);
        JSUtils.execCallbackOk(iWebview, callbackId, new AjaxResult(1), false);
    }

    protected void setUpdateBean(UpdateBean updateBean) {

    }

}
