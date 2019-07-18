package com.dan.ui.widget.dragrecycler.base;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * @author Bo
 * @email wubodanran@163.com
 * @desc RecyclerView拖动帮助类
 * @date 2018-05-25 11:18
 */
public class DragTouchHelper extends ItemTouchHelper {

    private TouchCallback callback;

    public DragTouchHelper(TouchCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void startDrag(RecyclerView.ViewHolder viewHolder) {
        super.startDrag(viewHolder);
    }

    public void setEnableDrag(boolean enableDrag) {
        callback.setEnableDrag(enableDrag);
    }

    public void setEnableSwipe(boolean enableSwipe) {
        callback.setEnableSwipe(enableSwipe);
    }
}
