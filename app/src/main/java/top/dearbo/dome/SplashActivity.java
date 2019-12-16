package top.dearbo.dome;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import top.dearbo.common.log.Logger;
import top.dearbo.common.toast.ToastUtils;
import top.dearbo.common.util.JsonUtil;
import top.dearbo.common.util.StatusBarUtils;

import top.dearbo.dome.activity.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Bo on 2019/1/17 15:30
 * 启动页
 */
public class SplashActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    //两秒后进入系统
    private static final int SPLASH_DISPLAY_TIME = 2000;
    private static final boolean testFlag = false;
    private static final int RC_ALL_PERM = 1;
    private int permissionSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置状态栏全屏
        StatusBarUtils.setWindowFullScreen(this);

        setContentView(R.layout.activity_splash);

        permissions();
    }

    private void init() {
        Intent intent;
        if (testFlag) {
            intent = new Intent(getApplicationContext(), MainActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), LoginActivity.class);
        }
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }

    @AfterPermissionGranted(RC_ALL_PERM)
    public void permissions() {
        List<String> permissionList = new ArrayList<>();
        permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            permissionList.add(Manifest.permission.FOREGROUND_SERVICE);
        }
        if (!EasyPermissions.hasPermissions(SplashActivity.this, permissionList.toArray(new String[0]))) {
            permissionSize++;
            permissions(permissionList);
        } else {
            if (permissionSize < 1) {
                //停留2秒然后进主页面
                new android.os.Handler().postDelayed(this::init, SPLASH_DISPLAY_TIME);
            } else {
                init();
            }
        }
    }

    /**
     * 给没有授权的重新申请
     *
     * @param permissionList 为申请的权限
     */
    public void permissions(@NonNull List<String> permissionList) {
        EasyPermissions.requestPermissions(SplashActivity.this, "需要访问手机储存权限!", RC_ALL_PERM, permissionList.toArray(new String[0]));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //将结果转发给EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        //已授予某些权限
        Logger.d("已授予某些权限:requestCode:" + requestCode + ",perms:" + JsonUtil.toJson(perms));
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("需要权限")
                    .setRationale("没有请求权限，此应用程序可能无法正常工作，打开应用设置屏幕以修改应用权限。")
                    .build().show();
        } else {
            ToastUtils.toast("请开通相关权限，否则无法正常使用本应用！");
            permissions(perms);
        }
        //某些权限已被拒绝
        Logger.d("某些权限已被拒绝:requestCode:" + requestCode + ",perms:" + JsonUtil.toJson(perms));
    }

}
