package com.dan.dome.fragment.base;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.view.View;

import com.dan.dome.MainActivity;
import com.dan.library.util.ActivityUtil;
import com.dan.library.util.DialogUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Bo on 2019/2/19 14:08
 */
public abstract class BaseFragment extends Fragment {

    public Dialog mLoading;

    public MainActivity mainActivity;

    protected Unbinder unbinder;

    @Override
    public void onStart() {
        super.onStart();
        mLoading = DialogUtils.createLoadingDialog(getContext());
        mainActivity = (MainActivity) getActivity();
        setShowOrHide();
        ActivityUtil.getInstance().addActivity(getActivity());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setShowOrHide();
        }
    }

    /**
     * onDestroyView中进行解绑操作
     */
    @Override
    public void onDestroyView() {
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
        super.onDestroyView();
        ActivityUtil.getInstance().addActivity(getActivity());
    }

    /**
     * 子Fragment创建CreateView时调用此方法
     */
    protected boolean initCreateView(View view) {
        unbinder = ButterKnife.bind(this, view);
        return null == unbinder;
    }

    protected abstract void setShowOrHide();
}
