package com.dan.library.customview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.dan.library.R;

/**
 * Created by Bo on 2018/11/9 14:44
 */
public class ConfirmDialog extends Dialog {

    public ConfirmDialog(Context context, String title, String msg, String positiveBtnTxt, String negativeBtnTxt, final Callback callback, int iconId) {
        super(context, R.style.AppTheme);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveBtnTxt, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.callback(1);
                ConfirmDialog.this.cancel();
            }
        });
        builder.setNegativeButton(negativeBtnTxt, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.callback(0);
                ConfirmDialog.this.cancel();
            }
        });
        if (iconId > -1) {
            builder.setIcon(iconId);
        }
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public ConfirmDialog(Context context, String title, String msg, String positiveBtnTxt, String negativeBtnTxt, final Callback callback) {
        this(context, title, msg, positiveBtnTxt, negativeBtnTxt, callback, -1);
    }

    public ConfirmDialog(Context context, String title, String msg, final Callback callback) {
        this(context, title, msg, "确定", "取消", callback, -1);
    }

    public static void showAlert(Context context, String title, String message, OnClickListener listener) {
        showAlert(context, title, message, "设置", false, -1, listener);
    }

    public static void showAlert(Context context, String title, String message, String positiveBtnTxt, boolean cancelableFlag, OnClickListener listener) {
        showAlert(context, title, message, positiveBtnTxt, cancelableFlag, -1, listener);
    }

    public static void showAlert(Context context, String title, String message, String positiveBtnTxt, boolean cancelableFlag, int iconId, OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveBtnTxt, listener);
        builder.setCancelable(cancelableFlag);
        if (iconId > -1) {
            builder.setIcon(iconId);
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public interface Callback {
        void callback(int position);
    }
}
