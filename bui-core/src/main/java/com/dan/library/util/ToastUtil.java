package com.dan.library.util;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Bo on 2018/10/17 15:07
 */
@Deprecated
public class ToastUtil {
    private static final String TAG = "ToastUtil";

    public static void makeText(Context context, String msg) {
        makeText(context, msg, Toast.LENGTH_SHORT);
    }

    public static void makeText(Context context, String msg, int duration) {
        Toast mToast = null;
        try {
            if (null == mToast) {
                mToast = Toast.makeText(context, "", duration);
            }
            mToast.setText(msg);
            mToast.show();
            //Toast.makeText(context, msg, duration).show();
        } catch (Exception e) {
            Log.e(TAG, "makeText:", e);
            try {
                //解决在子线程中调用Toast的异常情况处理
                Looper.prepare();
                if (null == mToast) {
                    mToast = Toast.makeText(context, "", duration);
                }
                mToast.setText(msg);
                mToast.show();
                Looper.loop();
            } catch (Exception e1) {
                Log.e(TAG, "makeText-Looper.prepare:", e1);
            }
        }
    }
}
