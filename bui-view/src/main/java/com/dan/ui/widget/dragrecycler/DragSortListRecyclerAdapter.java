package com.dan.ui.widget.dragrecycler;

import android.content.Context;
import android.support.annotation.NonNull;

import com.dan.ui.adapter.recycler.base.AbstractViewHolder;
import com.dan.ui.adapter.recycler.base.QuickRecyclerAdapter;
import com.dan.ui.widget.dragrecycler.base.OnItemDragSortListener;

/**
 * Created by Dan on 2019/7/12 15:01
 */
public abstract class DragSortListRecyclerAdapter<T> extends QuickRecyclerAdapter<T> {

    private OnItemDragSortListener onItemDragSortListener;

    public DragSortListRecyclerAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void onBindViewHolder(@NonNull final AbstractViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        //重写双击事件
        /*holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemDragSortListener != null) {
                    onItemDragSortListener.onStartDrags(holder);
                }

                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(holder.itemView, getItemData(position), position);
                }
                return true;
            }
        });*/

    }

    protected void setOnItemDragSortListener(OnItemDragSortListener onItemDragSortListener) {
        this.onItemDragSortListener = onItemDragSortListener;
    }

}
