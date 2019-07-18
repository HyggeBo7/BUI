package com.dan.ui.widget.dragrecycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dan.ui.R;
import com.dan.ui.adapter.recycler.RecyclerViewUtil;
import com.dan.ui.widget.dragrecycler.base.DragTouchHelper;
import com.dan.ui.widget.dragrecycler.base.OnItemDragSortListener;
import com.dan.ui.widget.dragrecycler.base.TouchCallback;

import java.util.Collections;

/**
 * Created by Dan on 2019/7/15 12:37
 */
public class DragSortListRecycler extends RecyclerView implements OnItemDragSortListener {

    private final TouchCallback touchCallback;
    /**
     * 拖拽操作帮助类
     */
    private DragTouchHelper dragTouchHelper;

    private OnDragSortListener onDragSortListener;

    private Context mContext;

    /**
     * 无数据是否显示空view
     */
    private boolean emptyViewShowFlag = true;

    /**
     * 无数据显示
     */
    private View emptyView;

    private AdapterDataObserver observerData = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            View thisEmptyView = getEmptyView();
            if (getAdapter().getItemCount() < 1) {
                thisEmptyView.setVisibility(VISIBLE);
                DragSortListRecycler.this.setVisibility(GONE);
            } else {
                thisEmptyView.setVisibility(GONE);
                DragSortListRecycler.this.setVisibility(VISIBLE);
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            onChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            onChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            onChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            onChanged();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            onChanged();
        }
    };

    public DragSortListRecycler(@NonNull Context context) {
        this(context, null);
    }

    public DragSortListRecycler(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragSortListRecycler(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;

        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        //局部刷新闪屏问题解决
        defaultItemAnimator.setSupportsChangeAnimations(false);
        setItemAnimator(defaultItemAnimator);

        touchCallback = new TouchCallback(this);

        dragTouchHelper = new DragTouchHelper(touchCallback);

        dragTouchHelper.attachToRecyclerView(this);

        RecyclerViewUtil.vertical(context, this);

        init();
    }

    private void init() {
        //设置默认允许长按拖动
        touchCallback.setEnableDrag(true);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof DragSortListRecyclerAdapter) {
            DragSortListRecyclerAdapter listAdapter = (DragSortListRecyclerAdapter) adapter;
            listAdapter.setOnItemDragSortListener(this);
        }
        if (emptyViewShowFlag) {
            adapter.registerAdapterDataObserver(observerData);
            observerData.onChanged();
        }
    }

    public View getEmptyView() {
        if (emptyView == null) {
            TextView textView = new TextView(mContext);
            textView.setTextSize(20);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(getResources().getColor(R.color.red));
            textView.setText("暂无数据...");
            setEmptyView(textView);
        }
        return emptyView;
    }

    /**
     * 设置空View
     *
     * @param view View
     */
    public void setEmptyView(View view) {
        this.emptyView = view;

        if (emptyViewShowFlag) {
            ((ViewGroup) this.getRootView()).addView(view);
        }
    }

    /**
     * 设置无数据显示空View
     *
     * @param emptyViewShowFlag true
     */
    public void setEmptyViewShowFlag(boolean emptyViewShowFlag) {
        this.emptyViewShowFlag = emptyViewShowFlag;
    }

    @Override
    public void onStartDrags(ViewHolder viewHolder) {
        dragTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onSwiped(int position) {
        //处理划动删除操作
        DragSortListRecyclerAdapter mAdapter = (DragSortListRecyclerAdapter) getAdapter();
        if (mAdapter != null && mAdapter.checkIndex(position)) {
            mAdapter.remove(position);
        }
    }

    @Override
    public boolean onItemMove(int srcPosition, int targetPosition) {
        //处理拖拽事件
        DragSortListRecyclerAdapter mAdapter = (DragSortListRecyclerAdapter) getAdapter();
        if (mAdapter == null || mAdapter.getDataList() == null || mAdapter.getDataList().size() == 0) {
            return false;
        }
        if (mAdapter.checkIndex(srcPosition) && mAdapter.checkIndex(targetPosition)) {
            //交换数据源两个数据的位置
            Collections.swap(mAdapter.getDataList(), srcPosition, targetPosition);
            //更新视图
            mAdapter.notifyItemMoved(srcPosition, targetPosition);
            if (onDragSortListener != null) {
                onDragSortListener.onItemMove(srcPosition, targetPosition);
            }
            //消费事件
            return true;
        }
        return false;
    }

    /**
     * 是否允许长按拖动
     *
     * @param enabledFlag true允许
     */
    public void isLongPressDrag(boolean enabledFlag) {
        touchCallback.setEnableDrag(enabledFlag);
    }

    public void setOnDragSortListener(OnDragSortListener onDragSortListener) {
        this.onDragSortListener = onDragSortListener;
    }

    public interface OnDragSortListener {
        /**
         * 移动是触发
         */
        void onItemMove(int srcPosition, int targetPosition);
    }

}
