package com.dan.library.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by Bo on 2018/10/15 14:21
 */
public class PermissionsUtil implements ActivityCompat.OnRequestPermissionsResultCallback {

    private Activity activity;

    private int requestCode = 1;

    public PermissionsUtil(Activity activity) {
        this.activity = activity;
    }

    /**
     * 检查某个权限是否已经获得
     */
    public boolean checkPermission(String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
                if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    // 只要有一个权限没有被授予, 则直接返回 false
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    /**
     * 获取权限
     */
    public void getPermission(String permission, String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //申请权限
            ActivityCompat.requestPermissions(activity, permissions, requestCode);

            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                Toast.makeText(activity, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.println("PermissionsUtil==========requestCode:" + requestCode);
    }

}