package com.dan.library.appupdate.model;

import com.dan.library.R;
import com.dan.library.appupdate.util.DownloadAppUtils;

import java.io.Serializable;

/**
 * Created by Bo on 2018/11/9 14:54
 */
public class UpdateBean implements Serializable {

    private static final long serialVersionUID = -2784353684531894001L;
    /**
     * 下载方式：默认app下载
     */
    private int downloadCode = DownloadAppUtils.DOWNLOAD_BROWSER;
    /**
     * apk 下载地址
     */
    private String apkPath = "http://119.37.194.4:15555/imm-api/upload/imm-1.0.5.apk";
    /**
     * apk名称
     */
    private String apkName = "down.apk";
    /**
     * 更新说明
     */
    private String updateInfo = "";
    /**
     * 是否强制更新
     */
    private Boolean isForce = false;
    /**
     * 服务器上版本名
     */
    private String serverVersionName = "1.0";
    /**
     * 服务器上版本号
     */
    private Integer serverVersionCode;
    /**
     * 是否在通知栏显示
     */
    private boolean showNotification = true;
    /**
     * 是否显示版本提示
     */
    private boolean versionFlag = true;

    /**
     * 设置按钮的颜色
     */
    private String dialogButtonColor = "#E743DA";

    /**
     * 设置按钮的文字颜色
     */
    private String dialogButtonTextColor = "#FFFFFF";

    /**
     * apk 大小提示
     */
    private String apkSize = "";

    /**
     * 不是wifi状态是否提示
     */
    private boolean wifiFlag = false;

    /**
     * 通知栏图标id
     */
    private int smallIconId = R.mipmap.update_small_icon;

    public int getSmallIconId() {
        return smallIconId;
    }

    public void setSmallIconId(int smallIconId) {
        this.smallIconId = smallIconId;
    }

    public boolean isWifiFlag() {
        return wifiFlag;
    }

    public void setWifiFlag(boolean wifiFlag) {
        this.wifiFlag = wifiFlag;
    }

    public String getDialogButtonColor() {
        return dialogButtonColor;
    }

    public void setDialogButtonColor(String dialogButtonColor) {
        this.dialogButtonColor = dialogButtonColor;
    }

    public String getDialogButtonTextColor() {
        return dialogButtonTextColor;
    }

    public void setDialogButtonTextColor(String dialogButtonTextColor) {
        this.dialogButtonTextColor = dialogButtonTextColor;
    }

    public String getApkSize() {
        return apkSize;
    }

    public void setApkSize(String apkSize) {
        this.apkSize = apkSize;
    }

    public int getDownloadCode() {
        return downloadCode;
    }

    public void setDownloadCode(int downloadCode) {
        this.downloadCode = downloadCode;
    }

    public String getApkPath() {
        return apkPath;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }

    public Boolean getForce() {
        return isForce;
    }

    public void setForce(Boolean force) {
        isForce = force;
    }

    public String getServerVersionName() {
        return serverVersionName;
    }

    public void setServerVersionName(String serverVersionName) {
        this.serverVersionName = serverVersionName;
    }

    public Integer getServerVersionCode() {
        return serverVersionCode;
    }

    public void setServerVersionCode(Integer serverVersionCode) {
        this.serverVersionCode = serverVersionCode;
    }

    public boolean isShowNotification() {
        return showNotification;
    }

    public void setShowNotification(boolean showNotification) {
        this.showNotification = showNotification;
    }

    public boolean isVersionFlag() {
        return versionFlag;
    }

    public void setVersionFlag(boolean versionFlag) {
        this.versionFlag = versionFlag;
    }
}
