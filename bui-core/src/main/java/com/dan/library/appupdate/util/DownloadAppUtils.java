package com.dan.library.appupdate.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;

import com.azhon.appupdate.config.UpdateConfiguration;
import com.azhon.appupdate.manager.DownloadManager;
import com.azhon.appupdate.utils.ApkUtil;
import com.dan.library.appupdate.model.UpdateBean;
import com.dan.library.customview.ConfirmDialog;

/**
 * Created by Bo on 2018/11/16 10:25
 */
public class DownloadAppUtils {

    /**
     * 浏览器下载
     */
    public static final int DOWNLOAD_BROWSER = 1004;
    /**
     * 使用自己的对话框更新
     */
    public static final int DOWNLOAD_App_By1 = 2001;
    /**
     * 简单使用,直接下载
     */
    public static final int DOWNLOAD_App_By2 = 2003;
    /**
     * 版本库内置的对话框更新
     */
    public static final int DOWNLOAD_App_By3 = 2005;

    @SuppressLint("StaticFieldLeak")
    private static DownloadManager manager = null;

    /**
     * 通过浏览器下载APK包
     */
    public static void downloadForWebView(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startUpdate(final Activity context, UpdateBean updateBean) {
        if (null == updateBean) {
            updateBean = new UpdateBean();
        }
        //判断不是wifi时是否提示
        if (updateBean.isWifiFlag() && !isWifiConnected(context)) {
            final UpdateBean finalUpdateBean = updateBean;
            new ConfirmDialog(context, "提示", "目前手机不是WiFi状态\n确认是否继续下载更新?", new ConfirmDialog.Callback() {
                @Override
                public void callback(int position) {
                    if (position == 1) {
                        downApK(context, finalUpdateBean);
                    }
                }
            });
        } else {
            downApK(context, updateBean);
        }
    }

    /**
     * 下载-判断
     */
    private static void downApK(Activity context, UpdateBean updateBean) {
        if (manager != null) {
            //释放资源
            manager.release();
        }
        switch (updateBean.getDownloadCode()) {
            case DOWNLOAD_BROWSER:
                startBrowser(context, updateBean);
                break;
            case DOWNLOAD_App_By1:
                startUpdate1(context, updateBean.getApkName(), updateBean.getApkPath(), updateBean.getUpdateInfo(), updateBean.getSmallIconId());
                break;
            case DOWNLOAD_App_By2:
                startUpdate2(context, updateBean.getApkName(), updateBean.getApkPath(), updateBean.getSmallIconId());
                break;
            case DOWNLOAD_App_By3:
                startUpdate3(context, updateBean);
                break;
            default:
                startBrowser(context, updateBean);
                break;
        }
    }

    /**
     * 浏览器下载
     */
    private static void startBrowser(final Activity context, final UpdateBean updateBean) {
        if (updateBean.isVersionFlag()) {
            ConfirmDialog.showAlert(context, "发现新版本", updateBean.getUpdateInfo(), "升级", true, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    downloadForWebView(context, updateBean.getApkPath());
                }
            });
        } else {
            downloadForWebView(context, updateBean.getApkPath());
        }
    }

    private static void startUpdate1(final Activity context, final String apkName, final String apkPath, String updateInfo, final int smallIconId) {
        ConfirmDialog.showAlert(context, "发现新版本", updateInfo, "升级", true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                manager = DownloadManager.getInstance(context);
                manager.setApkName(apkName)
                        .setApkUrl(apkPath)
                        .setDownloadPath(Environment.getExternalStorageDirectory() + "/AppUpdate")
                        .setSmallIcon(smallIconId)
                        .download();
            }
        });
    }

    private static void startUpdate2(Activity context, String apkName, String apkPath, int smallIconId) {
        manager = DownloadManager.getInstance(context);
        manager.setApkName(apkName)
                .setApkUrl(apkPath)
                .setDownloadPath(Environment.getExternalStorageDirectory() + "/AppUpdate")
                .setSmallIcon(smallIconId)
                .download();
    }

    private static void startUpdate3(final Activity context, UpdateBean updateBean) {
        /*
         * 整个库允许配置的内容
         * 非必选
         */
        UpdateConfiguration configuration = new UpdateConfiguration()
                //输出错误日志
                .setEnableLog(true)
                //设置自定义的下载
                //.setHttpManager()
                //下载完成自动跳动安装页面
                .setJumpInstallPage(true)
                //设置对话框背景图片 (图片规范参照demo中的示例图)
                //.setDialogImage(R.drawable.ic_dialog)
                //设置按钮的颜色
                .setDialogButtonColor(Color.parseColor(updateBean.getDialogButtonColor()))
                //设置按钮的文字颜色
                .setDialogButtonTextColor(Color.parseColor(updateBean.getDialogButtonTextColor()))
                //支持断点下载
                .setBreakpointDownload(true)
                //设置是否显示通知栏进度
                .setShowNotification(updateBean.isShowNotification())
                //设置强制更新
                .setForcedUpgrade(updateBean.getForce())
                //设置对话框按钮的点击监听
                /*.setButtonClickListener(new OnButtonClickListener() {
                    @Override
                    public void onButtonClick(int id) {
                        Log.e("TAG", String.valueOf(id));
                    }
                })*/
                //设置下载过程的监听
                /*.setOnDownloadListener(new OnDownloadListener() {
                    @Override
                    public void start() {

                    }

                    @Override
                    public void downloading(int max, int progress) {
                        System.out.println("max:" + max + ",progress:" + progress);
                    }

                    @Override
                    public void done(File apk) {

                    }

                    @Override
                    public void error(Exception e) {

                    }
                })*/;
        manager = DownloadManager.getInstance(context);
        DownloadManager downloadManager = manager.setApkName(updateBean.getApkName())
                .setApkUrl(updateBean.getApkPath())
                .setSmallIcon(updateBean.getSmallIconId())
                .setShowNewerToast(true)
                .setConfiguration(configuration)
                .setDownloadPath(Environment.getExternalStorageDirectory() + "/AppUpdate");

        if (updateBean.isVersionFlag()) {
            downloadManager.setApkVersionCode(updateBean.getServerVersionCode() == null ? ApkUtil.getVersionCode(context) + 1 : updateBean.getServerVersionCode())
                    .setApkVersionName(updateBean.getServerVersionName())
                    .setApkSize(updateBean.getApkSize())
                    .setAuthorities(context.getPackageName())
                    .setApkDescription(updateBean.getUpdateInfo());
        }
        downloadManager.download();
    }

    /**
     * 检测wifi是否连接
     */
    private static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

}