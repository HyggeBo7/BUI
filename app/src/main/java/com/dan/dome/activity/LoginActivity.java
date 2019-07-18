package com.dan.dome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.dan.dome.MainActivity;
import com.dan.dome.R;
import com.dan.dome.activity.base.BaseActivity;
import com.dan.dome.networks.HttpConfig;
import com.dan.dome.enums.LoginKeyEnum;
import com.dan.dome.util.SystemApplication;
import com.dan.library.networks.HttpStatusCode;
import com.dan.library.entity.AjaxResult;
import com.dan.library.util.JsonUtil;
import com.dan.library.util.SharedUtil;
import com.dan.library.util.StatusBarUtils;
import com.dan.library.util.ToastUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpParams;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Bo on 2018/10/17 12:00
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.btn_login_submit)
    Button btnSubmit;

    @BindView(R.id.et_login_username)
    EditText etUserName;

    @BindView(R.id.et_login_pwd)
    EditText etPwd;

    /**
     * 是否记住密码
     */
    @BindView(R.id.cb_login_remember_pwd)
    CheckBox cbRememberPwd;

    /**
     * 是否自动登录
     */
    @BindView(R.id.cb_login_auto_login)
    CheckBox cbAutoLogin;

    private SharedUtil sharedUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置状态栏透明
        StatusBarUtils.setWindowStatusBarColor(this);
        setContentView(R.layout.login_activity);
        sharedUtil = new SharedUtil(LoginActivity.this);
        String account = sharedUtil.getStringValue(LoginKeyEnum.USER_NAME.key);
        String password = sharedUtil.getStringValue(LoginKeyEnum.PWD.key);
        //自动登录
        Boolean autoLoginFlag = sharedUtil.getBooleanValue(LoginKeyEnum.AUTO_LOGIN.key);
        //记住密码
        Boolean rememberPwdFlag = sharedUtil.getBooleanValue(LoginKeyEnum.REMEMBER_PWD.key);
        if (rememberPwdFlag || autoLoginFlag) {
            etUserName.setText(account);
            etPwd.setText(password);
            cbRememberPwd.setChecked(rememberPwdFlag);
            cbAutoLogin.setChecked(autoLoginFlag);
            if (autoLoginFlag) {
                login(account, password);
            }
        }
    }

    /**
     * 点击登录
     */
    @OnClick(R.id.btn_login_submit)
    public void btnLoginClick(View v) {
        final String account = etUserName.getText().toString();
        final String password = etPwd.getText().toString();
        if (StringUtils.isBlank(account)) {
            ToastUtil.makeText(LoginActivity.this, "账号不能为空!");
            return;
        }
        if (StringUtils.isBlank(password)) {
            ToastUtil.makeText(LoginActivity.this, "密码不能为空!");
            return;
        }
        login(account, password);

        //ToastUtil.makeText(LoginActivity.this, "登录成功!userName:" + userName + ",pwd:" + pwd);
    }

    private void login(final String account, final String password) {
        HttpParams params = new HttpParams();
        params.put("account", account);
        params.put("password", password);
        EasyHttp.post(HttpConfig.getLoginUrl())
                .params(params)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        mLoading.dismiss();
                        if (e.getCode() >= ApiException.UNKNOWN) {
                            ToastUtil.makeText(LoginActivity.this, e.getMessage());
                        } else {
                            ToastUtil.makeText(LoginActivity.this, HttpStatusCode.getHttpStatusMsg(e.getCode()));
                        }
                        btnSubmit.setBackgroundResource(android.R.color.holo_blue_dark);
                        btnSubmit.setEnabled(true);
                        btnSubmit.setText("登录");
                    }

                    @Override
                    public void onSuccess(String json) {
                        btnSubmit.setBackgroundResource(android.R.color.holo_purple);
                        btnSubmit.setText("登录");
                        btnSubmit.setEnabled(true);
                        mLoading.dismiss();
                        AjaxResult result = JsonUtil.fromJson(json, AjaxResult.class);
                        if (result != null && result.getCode().equals(1)) {
                            if (cbRememberPwd.isChecked() || cbAutoLogin.isChecked()) {
                                sharedUtil.save(LoginKeyEnum.USER_NAME.key, account);
                                sharedUtil.save(LoginKeyEnum.PWD.key, password);
                            } else {
                                sharedUtil.delete(LoginKeyEnum.USER_NAME.key);
                                sharedUtil.delete(LoginKeyEnum.PWD.key);
                            }
                            sharedUtil.save(LoginKeyEnum.REMEMBER_PWD.key, cbRememberPwd.isChecked());
                            sharedUtil.save(LoginKeyEnum.AUTO_LOGIN.key, cbAutoLogin.isChecked());
                            SystemApplication.setDataToken(result.getData().toString());
                            SystemApplication.setUserAccount(account);
                            SystemApplication.setUserPassword(password);
                            ToastUtil.makeText(LoginActivity.this, "登录成功!" + JsonUtil.toJson(result));
                            //跳转
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            ToastUtil.makeText(LoginActivity.this, result.getMsg() == null ? "登录失败!" : result.getMsg());
                        }
                    }

                    @Override
                    public void onStart() {
                        mLoading.show();
                        btnSubmit.setBackgroundResource(android.R.color.darker_gray);
                        btnSubmit.setEnabled(false);
                        btnSubmit.setText("登录中...");
                        //睡眠一秒钟
                        //SystemClock.sleep(1000 * 2);
                    }
                });
    }
}
