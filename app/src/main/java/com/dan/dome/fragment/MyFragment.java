package com.dan.dome.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dan.dome.GlideApp;
import com.dan.dome.R;
import com.dan.dome.activity.LoginActivity;
import com.dan.dome.enums.LoginKeyEnum;
import com.dan.dome.fragment.base.BaseFragment;
import com.dan.dome.util.SystemApplication;
import com.dan.library.customview.ConfirmDialog;
import com.dan.library.util.ApkUtil;
import com.dan.library.util.SharedUtil;
import com.dan.ui.widget.item.ItemView;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by Bo on 2019/2/19 14:08
 */
public class MyFragment extends BaseFragment {
    //顶部头像控件
    @BindView(R.id.h_back)
    ImageView mHBack;

    @BindView(R.id.h_head)
    ImageView mHHead;

    @BindView(R.id.user_name)
    TextView mUserName;
    //下面item控件
    @BindView(R.id.nickName)
    ItemView mNickName;

    @BindView(R.id.about)
    ItemView mAbout;
    //退出登录
    @BindView(R.id.ll_exit)
    LinearLayout llExit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_fragment, null);
        super.initCreateView(view);
        initView(view);
        return view;
    }

    private void initView(View view) {
        //顶部头像控件
        //mHBack = view.findViewById(R.id.h_back);
        //mHHead = view.findViewById(R.id.h_head);
        //mUserName = view.findViewById(R.id.user_name);

        //下面item控件
        //mNickName = view.findViewById(R.id.nickName);
        //mAbout = view.findViewById(R.id.about);

        //设置背景磨砂效果
        GlideApp.with(this).load(R.drawable.user_photo)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)//缓存
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
                .into(mHBack);
        //设置圆形图像
        GlideApp.with(this).load(R.drawable.user_photo)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)//缓存
                .into(mHHead);
        //退出登录
        //llExit = view.findViewById(R.id.ll_exit);
        initData();
    }

    private void initData() {
        mNickName.setShowRightArrow(false);
        mAbout.setShowRightArrow(false);
        mUserName.setText(SystemApplication.getUser().getName());

        mNickName.setRightDescText(SystemApplication.getUser().getName());
        mAbout.setRightDescText(ApkUtil.getVersionName(getContext()));
    }

    @OnClick(R.id.ll_exit)
    public void initListener() {
        new ConfirmDialog(
                getContext(),
                "退出提示",
                "确定退出登录?",
                "是",
                "否", position -> {
            if (position == 1) {
                SharedUtil sharedUtil = new SharedUtil(getContext());
                sharedUtil.delete(LoginKeyEnum.AUTO_LOGIN.key);
                //跳转
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        //退出登录
        /*llExit.setOnClickListener(v -> new ConfirmDialog(
                getContext(),
                "退出提示",
                "确定退出登录?",
                "是",
                "否", position -> {
            if (position == 1) {
                SharedUtil sharedUtil = new SharedUtil(getContext());
                sharedUtil.delete(LoginKeyEnum.AUTO_LOGIN.key);
                //跳转
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }));*/
    }

    @Override
    protected void setShowOrHide() {
        mainActivity.tvMainTitle.setText("我的");
    }
}
