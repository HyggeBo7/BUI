package com.dan.dome;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.dan.dome.activity.LoginActivity;
import com.dan.library.util.PermissionsUtil;
import com.dan.library.util.StatusBarUtils;
import com.dan.library.util.ToastUtil;

/**
 * Created by Bo on 2019/1/17 15:30
 * 启动页
 */
public class SplashActivity extends AppCompatActivity {

    //两秒后进入系统
    private final int SPLASH_DISPLAY_TIME = 2000;
    private boolean testFlag = false;
    private int permissionSize = 0;
    private int maxPermissionSize = 7;

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

    public void permissions() {
        String[] permissionsList = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        PermissionsUtil permissionsUtil = new PermissionsUtil(this);
        if (!permissionsUtil.checkPermission(permissionsList)) {
            permissionSize++;
            permissionsUtil.getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, permissionsList);
        } else {
            //停留2秒然后进主页面
            new android.os.Handler().postDelayed(this::init, SPLASH_DISPLAY_TIME);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            init();
        } else {
            ToastUtil.makeText(getApplicationContext(), "请开通相关权限，否则无法正常使用本应用！");
            if (permissionSize < maxPermissionSize) {
                permissions();
            }
        }
    }

}
