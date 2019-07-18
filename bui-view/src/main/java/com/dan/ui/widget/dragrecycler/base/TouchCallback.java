package com.dan.ui.widget.dragrecycler.base;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * @author Bo
 * @email wubodanran@163.com
 * @desc TouchHelper参数
 * @date 2019/7/12 15:01
 */
public class TouchCallback extends ItemTouchHelper.Callback {

    /**
     * 允许滑动
     */
    private boolean isEnableSwipe;
    /**
     * 是否可以长按拖拽
     */
    private boolean isLongPressDrag;
    private OnItemDragSortListener callbackListener;//回调接口

    public TouchCallback(OnItemDragSortListener callbackListener) {
        this.callbackListener = callbackListener;
    }

    /**
     * 滑动或者拖拽的方向，上下左右
     *
     * @param recyclerView 目标RecyclerView
     * @param viewHolder   目标ViewHolder
     * @return 方向
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {// GridLayoutManager
            // flag如果值是0，相当于这个功能被关闭
            int dragFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlag = 0;
            return makeMovementFlags(dragFlag, swipeFlag);
        } else if (layoutManager instanceof LinearLayoutManager) {// linearLayoutManager
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int orientation = linearLayoutManager.getOrientation();

            int dragFlag = 0;
            int swipeFlag = 0;

            if (orientation == LinearLayoutManager.HORIZONTAL) {//横向布局
                swipeFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                dragFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            } else if (orientation == LinearLayoutManager.VERTICAL) {//纵向布局
                dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                swipeFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            }
            return makeMovementFlags(dragFlag, swipeFlag);
        }
        return 0;
    }

    /**
     * 拖拽item移动时产生回调
     *
     * @param recyclerView 目标RecyclerView
     * @param viewHolder   拖拽的item对应的viewHolder
     * @param target       拖拽目的地的ViewHOlder
     * @return 是否消费拖拽事件
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (this.callbackListener != null) {
            this.callbackListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }
        return false;
    }

    /**
     * 滑动删除时回调
     *
     * @param viewHolder 当前操作的Item对应的viewHolder
     * @param direction  方向
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (this.callbackListener != null) {
            this.callbackListener.onSwiped(viewHolder.getAdapterPosition());
        }
    }

    /**
     * 是否可以长按拖拽
     *
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return isLongPressDrag;
    }

    /**
     * 是否可以滑动删除
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return isEnableSwipe;
    }

    public void setEnableDrag(boolean isLongPressDrag) {
        this.isLongPressDrag = isLongPressDrag;
    }

    public void setEnableSwipe(boolean enableSwipe) {
        this.isEnableSwipe = enableSwipe;
    }
}
