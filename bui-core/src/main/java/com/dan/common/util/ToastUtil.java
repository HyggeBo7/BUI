package com.dan.common.util;

import android.content.Context;
import android.widget.Toast;

import com.dan.common.BUtil;

/**
 * Created by Bo on 2018/10/17 15:07
 */
public class ToastUtil {
    private static final String TAG = "ToastUtil";
    private static Toast mToast = null;

    public static void makeText(String msg) {
        makeText(BUtil.getContext(), msg, Toast.LENGTH_SHORT);
    }

    public static void makeText(Context context, String msg) {
        makeText(context, msg, Toast.LENGTH_SHORT);
    }

    public static void makeText(final Context context, final String msg, final int duration) {
        if (BUtil.isMainLooper()) {
            showToast(context, msg, duration);
        } else {
            BUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showToast(context, msg, duration);
                }
            });
        }
    }

    private static void showToast(Context context, String msg, int duration) {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        if (null == mToast) {
            mToast = Toast.makeText(context, "", duration);
        }
        mToast.setText(msg);
        mToast.show();
    }

}
