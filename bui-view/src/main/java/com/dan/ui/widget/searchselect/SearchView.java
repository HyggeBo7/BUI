package com.dan.ui.widget.searchselect;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dan.ui.R;


/**
 * Created by dan on 2019/02/28.
 */
public class SearchView extends LinearLayout implements View.OnClickListener {

    private final static String TAG = "SearchView";
    /**
     * 空字符串
     */
    public final static String STRING_EMPTY = "";
    /**
     * 输入框
     */
    private EditText etInput;

    /**
     * 删除键
     */
    private ImageView ivDelete;

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 搜索回调接口
     */
    private OnSearchViewListener mListener;

    /**
     * 设置搜索内容
     */
    public void setSearchText(String searchText) {
        if (etInput != null) {

            etInput.setText(searchText);
        }
    }

    /**
     * 设置搜索回调接口
     *
     * @param listener 监听者
     */
    public void setSearchViewListener(OnSearchViewListener listener) {
        mListener = listener;
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.item_view_searcht, this);
        initViews();
    }

    private void initViews() {
        etInput = (EditText) findViewById(R.id.et_search_text);
        ivDelete = (ImageView) findViewById(R.id.imb_search_clear);
        ivDelete.setOnClickListener(this);
        etInput.addTextChangedListener(new EditChangedListener());
        etInput.setOnClickListener(this);

    }

    private class EditChangedListener implements TextWatcher {

        /**
         * 修改之前的文字
         */
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            //Log.i(TAG, "beforeTextChanged===" + charSequence.toString());
        }

        /**
         * 改变后的字符串
         */
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            //Log.i(TAG, "onTextChanged===" + charSequence.toString());
            String value = charSequence.toString();
            if (!STRING_EMPTY.equals(value)) {
                ivDelete.setVisibility(VISIBLE);
            } else {
                ivDelete.setVisibility(GONE);
            }
            //更新autoComplete数据
            if (mListener != null) {
                mListener.onQueryTextChange(value);
            }
        }

        /**
         * 修改后的文字
         */
        @Override
        public void afterTextChanged(Editable editable) {
            //Log.i(TAG, "afterTextChanged===" + editable.toString());
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imb_search_clear) {
            etInput.setText(STRING_EMPTY);
            if (mListener != null) {
                mListener.onQueryTextChange(STRING_EMPTY);
            }
            ivDelete.setVisibility(GONE);
        }
    }

    /**
     * search view回调方法
     */
    public interface OnSearchViewListener {
        void onQueryTextChange(String text);
    }
}  