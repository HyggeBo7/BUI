package top.dearbo.ui.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import top.dearbo.ui.R;


/**
 * Created by Bo on 2017/7/18.
 */

public class QuickDialog extends Dialog {
    private final static String TAG = "DragDialog";
    private Builder builder;

    private QuickDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    /**
     * 设置 Dialog的大小
     *
     * @param x 宽比例
     * @param y 高比例
     */
    public void setDialogWindowAttr(double x, double y, Activity activity) {
        if (x < 0 || x > 1 || y < 0 || y > 1) {
            return;
        }
        Window window = this.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            WindowManager manager = activity.getWindowManager();
            DisplayMetrics outMetrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(outMetrics);
            int width = outMetrics.widthPixels;
            int height = outMetrics.heightPixels;
            lp.gravity = Gravity.CENTER;
            lp.width = (int) (width * x);
            lp.height = (int) (height * y);
            window.setAttributes(lp);
        }
    }

    private void setBuilder(Builder builder) {
        this.builder = builder;
    }

    public Builder getBuilder() {
        return this.builder;
    }

    public static class Builder {

        //private View layout;
        private Context mContext;
        /**
         * 弹出框
         */
        private QuickDialog searchSelectDialog;
        private QuickDialogView dialogView;


        public Builder(QuickDialogView dialogView) {
            //这里传入自定义的style，直接影响此Dialog的显示效果。style具体实现见style.xml
            this.mContext = dialogView.getContext();
            searchSelectDialog = new QuickDialog(mContext, R.style.selectDialog);
            //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //layout = inflater.inflate(R.layout.item_dialog_drag_list, null);
            dialogView.onBindView(dialogView.getContext(), dialogView.getLayoutView());
            searchSelectDialog.addContentView(dialogView.getLayoutView(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            this.dialogView = dialogView;
        }

        /**
         * 调用此方法完成构建
         * 单按钮对话框和双按钮对话框的公共部分在这里设置
         */
        public QuickDialog builder() {
            searchSelectDialog.setContentView(dialogView.getLayoutView());
            //用户可以点击手机Back键取消对话框显示
            searchSelectDialog.setCancelable(true);
            //用户不能通过点击对话框之外的地方取消对话框显示
            searchSelectDialog.setCanceledOnTouchOutside(false);

            searchSelectDialog.setBuilder(this);
            return searchSelectDialog;
        }

        //用户设置

        /**
         * 设置标题
         */
        public Builder setTitle(String title) {
            dialogView.setTitle(title);
            return this;
        }

    }

}
