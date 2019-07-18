package com.dan.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dan.ui.R;
import com.dan.ui.adapter.impl.SpinnerTextFormatter;

import java.util.Arrays;
import java.util.List;


public class SimpleTextAdapter<T> extends ArrayAdapter<T> {

    private Context mContext;
    private List<T> mListData;
    private int selectedPos = -1;
    private String selectedText = "";
    private T selectItem;
    private int normalDrawableId;
    private Drawable selectedDrawable;
    private float textSize;
    private OnClickListener onClickListener;
    private OnItemClickListener mOnItemClickListener;
    private SpinnerTextFormatter spinnerTextFormatter = new SimpleSpinnerTextFormatter();

    public SimpleTextAdapter(Context context, T[] arrayData, int sId, int nId) {
        this(context, Arrays.asList(arrayData), sId, nId);
    }

    @SuppressLint("ResourceType")
    public SimpleTextAdapter(Context context, List<T> listData, int sId, int nId) {
        super(context, R.string.no_data, listData);
        mContext = context;
        mListData = listData;
        selectedDrawable = mContext.getResources().getDrawable(sId);
        normalDrawableId = nId;
        init();
    }

    /**
     * 设置显示字符串格式化
     */
    public void setTextFormatter(SpinnerTextFormatter spinnerTextFormatter) {
        this.spinnerTextFormatter = spinnerTextFormatter;
    }

    private void init() {
        onClickListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPos = (Integer) view.getTag();
                setSelectedPosition(selectedPos);
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, selectedPos, selectedText, selectItem);
                }
            }
        };
    }

    private void setSelectedPositionNoNotifyFlag(int pos, boolean changedFlag) {
        T t = null;
        if (mListData != null && pos > -1 && pos < mListData.size()) {
            t = mListData.get(pos);
        }
        if (t != null) {
            selectedPos = pos;
            selectItem = t;
            selectedText = spinnerTextFormatter.format(t).toString();
            if (changedFlag) {
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 设置选中的position,并通知列表刷新
     */
    public void setSelectedPosition(int pos) {
        setSelectedPositionNoNotifyFlag(pos, true);
    }

    /**
     * 设置选中的position,但不通知刷新
     */
    public void setSelectedPositionNoNotify(int pos) {
        setSelectedPositionNoNotifyFlag(pos, false);
    }

    /**
     * 获取选中的position
     */
    public int getSelectedPosition() {
        if (mListData != null && selectedPos < mListData.size()) {
            return selectedPos;
        }

        return -1;
    }

    public T getSelectItem() {
        return selectItem;
    }

    /**
     * 设置列表字体大小
     */
    public void setTextSize(float tSize) {
        textSize = tSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view;
        if (convertView == null) {
            view = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_etv_choose, parent, false);
        } else {
            view = (TextView) convertView;
        }
        view.setTag(position);
        String mString = "";
        if (mListData != null && position > -1 && position < mListData.size()) {
            mString = spinnerTextFormatter.format(mListData.get(position)).toString();
        }
        view.setText(mString);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        if (selectedText != null && selectedText.equals(mString)) {
            //设置选中的背景图片
            view.setBackground(selectedDrawable);
            //view.setBackgroundDrawable(selectedDrawable);
        } else {
            view.setBackground(mContext.getResources().getDrawable(normalDrawableId));//设置未选中状态背景图片
        }
        view.setPadding(20, 0, 0, 0);
        view.setOnClickListener(onClickListener);
        return view;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    /**
     * 重新定义菜单选项单击接口
     */
    public interface OnItemClickListener<T> {
        public void onItemClick(View view, int position, String selectedText, T o);
    }

}
