package com.dan.common.toast;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dan.common.BUtil;
import com.dan.common.log.Logger;
import com.dan.common.toast.style.ToastBlackStyle;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Dan on 2019/7/22 17:52
 */
public final class ToastUtils {

    private static Toast mToast = null;
    private static Toast customizeToast = null;
    private static IToastStyle toastStyleGlobal;

    /**
     * 原始-Toast
     */
    private final static int TOAST_TYPE = 0;
    /**
     * 自定义样式-Toast
     */
    private final static int CUSTOMIZE_TOAST_TYPE = 1;

    static {
        if (toastStyleGlobal == null) {
            toastStyleGlobal = new ToastBlackStyle(BUtil.getContext());
            Logger.d("ToastUtils========static========toastStyleGlobal");
        }
        Logger.d("ToastUtils========static========init");
    }

    /**
     * 没次弹出是否都创建新的Toast:[默认:true]
     */
    private static boolean toastCreateNewFlag = true;

    /**
     * 是否设置最大行数:[默认:false]
     */
    private static boolean maxLineFlag;

    /**
     * 是否设置重心:[默认:false]
     */
    private static boolean gravityFlag;

    /**
     * 根据文本来获取吐司的显示时长
     */
    private static int getToastDuration(CharSequence text) {
        // 如果显示的文字超过了10个就显示长吐司，否则显示短吐司
        return StringUtils.isNotBlank(text) && text.length() > 20 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;
    }

    /**
     * 智能获取用于显示消息的 TextView
     */
    private static TextView getMessageView(View view) {
        if (view instanceof TextView) {
            return (TextView) view;
        } else if (view.findViewById(android.R.id.message) instanceof TextView) {
            return ((TextView) view.findViewById(android.R.id.message));
        } else if (view instanceof ViewGroup) {
            TextView textView = findViewGroup((ViewGroup) view, TextView.class);
            if (textView != null) {
                return textView;
            }
        }
        // 如果设置的布局没有包含一个 TextView 则抛出异常，必须要包含一个 TextView 作为 MessageView
        throw new IllegalArgumentException("The layout must contain a TextView");
    }

    /**
     * 递归获取 ViewGroup 中的 TextView 对象
     */
    private static <T extends View> T findViewGroup(ViewGroup group, Class<T> t) {
        for (int i = 0; i < group.getChildCount(); i++) {
            View view = group.getChildAt(i);
            if (view.getClass().equals(t)) {
                return (T) view;
            } else if (view instanceof ViewGroup) {
                T findView = findViewGroup((ViewGroup) view, t);
                if (findView != null) {
                    return findView;
                }
            }

            /*if (view.getClass().equals(mClass)) {
                return view;
            }*/
            /*if ((view instanceof mClass)) {
                return (TextView) view;
            } else if (view instanceof ViewGroup) {
                TextView textView = findTextView((ViewGroup) view);
                if (textView != null) {
                    return textView;
                }
            }*/
        }
        return null;
    }

    /**
     * 显示toast-主线程显示
     */
    private static void showToast(final String text, final int duration, final IToastStyle toastStyle, final int toastType) {
        if (BUtil.isMainLooper()) {
            Toast toast = buildToast(text, duration, toastStyle, toastType);
            toast.show();
        } else {
            BUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast toast = buildToast(text, duration, toastStyle, toastType);
                    toast.show();
                }
            });
        }
    }

    /**
     * 构造toast
     *
     * @param text       内容
     * @param duration   长度
     * @param toastStyle 样式
     * @param toastType  类型
     * @return Toast
     */
    private static Toast buildToast(String text, int duration, IToastStyle toastStyle, int toastType) {
        //自定义样式
        if (toastType == CUSTOMIZE_TOAST_TYPE) {
            if (toastCreateNewFlag && customizeToast != null) {
                //注销之前显示的那条信息
                customizeToast.cancel();
                customizeToast = null;
            }
            if (customizeToast == null) {
                customizeToast = initToast(BUtil.getContext(), text, duration, toastStyle);
            } else {
                TextView tv = createTextView(BUtil.getContext(), toastStyle);
                tv.setText(text);
                customizeToast.setView(tv);
            }
            return customizeToast;
        } else {
            //TOAST_TYPE 或者 不存在的类型

            if (toastCreateNewFlag && mToast != null) {
                mToast.cancel();
                mToast = null;
            }
            if (mToast == null) {
                mToast = initToast(BUtil.getContext(), text, duration, toastStyle);
            } else {
                TextView tv = getMessageView(mToast.getView());
                if (tv != null) {
                    tv.setText(text);
                } else {
                    Logger.e("showToast===>View not TextView...");
                }
            }
        }
        return mToast;
    }

    /**
     * 构建Toast
     *
     * @param context  Context
     * @param msg      内容
     * @param duration 长度
     * @return Toast
     */
    private static Toast initToast(Context context, String msg, int duration, IToastStyle toastStyle) {
        //如果样式为null,就使用默认样式
        if (toastStyle == null) {
            toastStyle = toastStyleGlobal;
        }
        Toast toast = new Toast(context);
        TextView tv = createTextView(context, toastStyle);
        toast.setView(tv);
        tv.setText(msg);
        toast.setDuration(duration);
        if (gravityFlag) {
            setGravity(toast, toastStyle.getGravity(), toastStyle.getXOffset(), toastStyle.getYOffset());
        }
        return toast;
    }

    /**
     * 取消toast显示
     */
    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
        if (customizeToast != null) {
            customizeToast.cancel();
        }
    }

    /**
     * 生成默认的 TextView 对象
     */
    private static TextView createTextView(Context context, IToastStyle toastStyle) {

        GradientDrawable drawable = new GradientDrawable();
        // 设置背景色
        drawable.setColor(toastStyle.getBackgroundColor());
        // 设置圆角大小
        drawable.setCornerRadius(toastStyle.getCornerRadius());

        TextView textView = new TextView(context);
        textView.setId(android.R.id.message);
        textView.setTextColor(toastStyle.getTextColor());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, toastStyle.getTextSize());

        // 适配布局反方向
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textView.setPaddingRelative(toastStyle.getPaddingStart(), toastStyle.getPaddingTop(), toastStyle.getPaddingEnd(), toastStyle.getPaddingBottom());
        } else {
            textView.setPadding(toastStyle.getPaddingStart(), toastStyle.getPaddingTop(), toastStyle.getPaddingEnd(), toastStyle.getPaddingBottom());
        }

        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // setBackground API 版本兼容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            textView.setBackground(drawable);
        } else {
            textView.setBackgroundDrawable(drawable);
        }

        // 设置 Z 轴阴影
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textView.setZ(toastStyleGlobal.getZ());
        }

        // 设置最大显示行数
        if (maxLineFlag && toastStyleGlobal.getMaxLines() > 0) {
            textView.setMaxLines(toastStyleGlobal.getMaxLines());
        }

        return textView;
    }

    /**
     * 显示toast
     *
     * @param text 内容
     */
    public static void toast(String text) {
        toast(text, getToastDuration(text));
    }

    /**
     * 显示toast在主线程中
     *
     * @param text     提示信息
     * @param duration 提示长度
     */
    public static void toast(final String text, final int duration) {
        showToast(text, duration, toastStyleGlobal, TOAST_TYPE);
    }

    /**
     * 显示toast在主线程中
     *
     * @param resId String资源id或者int类型
     */
    public static void toast(int resId) {
        String msg;
        try {
            msg = BUtil.getContext().getResources().getString(resId);
        } catch (Resources.NotFoundException ignored) {
            msg = String.valueOf(resId);
        }
        toast(msg);
    }

    /**
     * 自定义样式显示-短时间显示
     *
     * @param text       内容
     * @param toastStyle 样式
     */
    public static Toast toast(String text, IToastStyle toastStyle) {
        return toast(text, getToastDuration(text), toastStyle);
    }

    /**
     * 自定义样式显示
     *
     * @param text       内容
     * @param duration   自定义长时间还是短时间
     * @param toastStyle 样式
     */
    public static Toast toast(String text, int duration, IToastStyle toastStyle) {
        showToast(text, duration, toastStyle, CUSTOMIZE_TOAST_TYPE);
        return customizeToast;
    }

    /*public static void toastIcon(String text, int imageRes) {
        toastIcon(text, imageRes, toastStyleGlobal);
    }

    public static void toastIcon(String text, int imageRes, IToastStyle toastStyle) {
        Context context = BUtil.getContext();
        if (imgToast == null) {
            imgToast = new Toast(BUtil.getContext());

            LinearLayout toastView = new LinearLayout(context);
            toastView.setOrientation(LinearLayout.HORIZONTAL);
            toastView.setLayoutParams(new ViewGroup.LayoutParams(35, 35));
            //toastView.setGravity(Gravity.CENTER_HORIZONTAL);

            ImageView imageCodeProject = new ImageView(context);
            imageCodeProject.setImageResource(imageRes);

            TextView tv = createTextView(context, toastStyle);
            tv.setGravity(Gravity.CENTER);
            //tv.setSingleLine(false);
            tv.setText(text);

            toastView.addView(imageCodeProject);
            toastView.addView(tv);

            imgToast.setView(toastView);
        } else {
            View view = imgToast.getView();
            if (view instanceof ViewGroup) {
                ImageView imageView = findViewGroup((ViewGroup) view, ImageView.class);
                if (imageView != null) {
                    imageView.setImageResource(imageRes);
                }

                TextView messageView = getMessageView(view);
                if (messageView != null) {
                    messageView.setText(text);
                }

            }
        }


        //Toast toast = toast.setView(toastView);

        showToast(imgToast);
    }*/

    /**
     * 设置吐司的位置
     *
     * @param gravity 重心
     * @param xOffset x轴偏移
     * @param yOffset y轴偏移
     */
    public static void setGravity(Toast toast, int gravity, int xOffset, int yOffset) {
        if (toast != null) {
            // 适配 Android 4.2 新特性，布局反方向（开发者选项 - 强制使用从右到左的布局方向）
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                gravity = Gravity.getAbsoluteGravity(gravity, toast.getView().getResources().getConfiguration().getLayoutDirection());
            }
            toast.setGravity(gravity, xOffset, yOffset);
        }
    }

    public static void setMaxLineFlag(boolean maxLineFlag) {
        ToastUtils.maxLineFlag = maxLineFlag;
    }

    public static void setGravityFlag(boolean gravityFlag) {
        ToastUtils.gravityFlag = gravityFlag;
    }

    public static void setToastStyle(IToastStyle sStyle) {
        ToastUtils.toastStyleGlobal = sStyle;
    }

    public static void setToastCreateNewFlag(boolean toastCreateNewFlag) {
        ToastUtils.toastCreateNewFlag = toastCreateNewFlag;
    }
}
