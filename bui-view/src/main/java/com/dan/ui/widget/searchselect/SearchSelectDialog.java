package com.dan.ui.widget.searchselect;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.dan.ui.R;
import com.dan.ui.adapter.SearchSelectAdapter;
import com.dan.ui.adapter.SimpleSpinnerTextFormatter;
import com.dan.ui.adapter.impl.SpinnerTextFormatter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Bo on 2017/7/18.
 */

public class SearchSelectDialog extends Dialog {
    private final static String TAG = "SearchSelectDialog";
    private Builder builder;

    private SearchSelectDialog(Context context, int themeResId) {
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
        WindowManager.LayoutParams lp = window.getAttributes();
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        lp.gravity = Gravity.CENTER;
        lp.width = (int) (width * x);
        lp.height = (int) (height * y);
        this.getWindow().setAttributes(lp);
    }

    private void setBuilder(Builder builder) {
        this.builder = builder;
    }

    public Builder getBuilder() {
        return this.builder;
    }

    public static class Builder<T> {
        /**
         * 标题内容
         */
        private String title;
        //private View contentView;
        //private String positiveButtonText;
        //private String negativeButtonText;
        //private String singleButtonText;
        /**
         * 全部数据
         */
        private List<T> mListDataAll;
        /**
         * 搜索内容存放
         */
        private List<T> mSearchList;
        //private View.OnClickListener positiveButtonClickListener;
        //private View.OnClickListener negativeButtonClickListener;
        //private View.OnClickListener singleButtonClickListener;

        private View layout;
        private Context mContext;
        /**
         * 弹出框
         */
        private SearchSelectDialog searchSelectDialog;
        /**
         * 重写ListView,点击事件
         */
        private OnSelectedListener selectedListener;
        /**
         * 格式化列表显示内容->自定义设置重写 format方法,使用setSpinnerTextFormatter设置
         */
        private SpinnerTextFormatter spinnerTextFormatter;

        /**
         * 列表-显示内容
         */
        private ListView listView;
        /**
         * 搜索的文本框UI
         */
        private SearchView searchView;
        /**
         * 搜索按钮开关
         */
        private ImageButton searchBtn;
        /**
         * 清空内容
         */
        private ImageButton closeBtn;
        /**
         * 标题
         */
        private TextView titleView;

        /**
         * 按钮状态->false:隐藏,true:显示
         */
        private boolean searchViewStatusFlag = false;

        /**
         * 最后一次选择的数据
         */
        private T lastItem;
        /**
         * 最后一次搜索的文本内容
         */
        private String lastSearchText = null;
        /**
         * 是否保留上次搜索列表->默认false不保留
         */
        private boolean reservedFlag = false;

        /**
         * Adapter
         */
        private SearchSelectAdapter<T> mSelectAdapter;

        public Builder(Context context) {
            //这里传入自定义的style，直接影响此Dialog的显示效果。style具体实现见style.xml
            this.mContext = context;
            searchSelectDialog = new SearchSelectDialog(context, R.style.selectDialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.item_dialog_select_search, null);
            listView = (ListView) layout.findViewById(R.id.listview);
            searchView = (SearchView) layout.findViewById(R.id.searchView);
            searchBtn = (ImageButton) layout.findViewById(R.id.btn_dialog_select_search);
            closeBtn = (ImageButton) layout.findViewById(R.id.imb_dialog_select_close);
            titleView = (TextView) layout.findViewById(R.id.tv_dialog_select_title);
            searchSelectDialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            //设置所有事件
            initListener();
        }

        /**
         * 设置监听事件
         */
        private void initListener() {
            //点击搜索按钮,隐藏搜索框或者显示
            searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!searchViewStatusFlag) {
                        searchView.setVisibility(View.VISIBLE);
                        searchViewStatusFlag = true;
                    } else {
                        searchView.setVisibility(View.GONE);
                        searchViewStatusFlag = false;
                    }
                }
            });
            //设置搜索文本内容监听
            searchView.setSearchViewListener(new SearchView.OnSearchViewListener() {
                @Override
                public void onQueryTextChange(String text) {
                    //获取内容,更新列表
                    updateLayout(searchItem(text));
                    //保存最后一次搜索文本
                    lastSearchText = text;
                }
            });
            //关闭弹出框
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchSelectDialog.dismiss();
                }
            });
            //弹框关闭执行
            searchSelectDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    //关闭时还原创建时的状态
                    closeClear();
                    //Log.i(TAG, "searchSelectDialog==onDismiss:");
                }
            });
        }

        public SearchSelectDialog builder() {
            return builder(null);
        }

        /**
         * 调用此方法完成构建
         * 单按钮对话框和双按钮对话框的公共部分在这里设置
         */
        public SearchSelectDialog builder(List<T> dataList) {
            if (dataList != null) {
                setDataSource(dataList);
            }
            titleView.setText(title);
            //来刷新View的
            //listView.invalidate();

            searchSelectDialog.setContentView(layout);
            //用户可以点击手机Back键取消对话框显示
            searchSelectDialog.setCancelable(true);
            //用户不能通过点击对话框之外的地方取消对话框显示
            searchSelectDialog.setCanceledOnTouchOutside(false);

            searchSelectDialog.setBuilder(this);
            return searchSelectDialog;
        }

        /*public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }*/

        /*public Builder setPositiveButton(String positiveButtonText, View.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }*/

        /*public Builder setNegativeButton(String negativeButtonText, View.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }*/

        //私有化

        /**
         * 设置Adapter以及点击事件
         */
        private void setListViewListener(final SearchSelectAdapter<T> selectAdapter) {
            this.listView.setAdapter(selectAdapter);
            this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    lastItem = selectAdapter.getItem(position);
                    selectedListener.onSelected(getFormatShowText().format(lastItem).toString(), position, lastItem);
                    searchSelectDialog.dismiss();
                }
            });
        }

        /**
         * 获取格式化类
         */
        private SpinnerTextFormatter getFormatShowText() {
            return spinnerTextFormatter == null ? new SimpleSpinnerTextFormatter() : spinnerTextFormatter;
        }

        /**
         * 根据内容匹配
         *
         * @param name 筛选内容
         * @return 结果
         */
        private List<T> searchItem(String name) {
            //Log.i(TAG, "searchItem==name:" + name);
            if (mListDataAll == null) {
                return null;
            }
            if (name != null && !"".equals(name)) {
                //保证筛选时 mSearchList不为null,并且是空的
                if (mSearchList == null) {
                    mSearchList = new ArrayList<T>();
                } else if (mSearchList.size() > 0) {
                    mSearchList.clear();
                }
                for (T t : mListDataAll) {
                    int index = getFormatShowText().format(t).toString().indexOf(name);
                    // 存在匹配的数据
                    if (index != -1) {
                        mSearchList.add(t);
                    }
                }
                return mSearchList;
            }
            return mListDataAll;
        }

        /**
         * 更新ListView列表数据
         *
         * @param newList 数据
         */
        private void updateLayout(List<T> newList) {
            if (mSelectAdapter != null) {
                if (newList == null) {
                    mSelectAdapter.clear();
                } else {
                    mSelectAdapter.setData(newList);
                }
            } else {
                Log.e(TAG, "Adapter IS NULL...请设置Adapter！");
            }
        }

        /**
         * 关闭弹框后操作
         */
        private void closeClear() {
            //搜索仓储的集合
            if (this.mSearchList != null) {
                this.mSearchList.clear();
                this.mSearchList = null;
            }
            //保留上次搜索记录
            if (reservedFlag) {
                updateLayout(searchItem(lastSearchText));
            } else {
                //Adapter清空数据,以及还原数据
                updateLayout(searchItem(null));
                //搜索框,显示或者隐藏
                if (searchViewStatusFlag) {
                    searchView.setVisibility(View.GONE);
                    searchView.setSearchText(SearchView.STRING_EMPTY);
                    searchViewStatusFlag = false;
                }
            }
            //Log.i(TAG, "closeClear===");
        }

        //用户设置

        /**
         * 设置标题
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * 设置是否保留上次搜索结果
         *
         * @param reservedFlag 默认false不保留
         * @return this
         */
        public Builder setReservedFlag(boolean reservedFlag) {
            this.reservedFlag = reservedFlag;
            return this;
        }

        /**
         * 设置格式化显示内容
         */
        public Builder setSpinnerTextFormatter(SpinnerTextFormatter spinnerTextFormatter) {
            this.spinnerTextFormatter = spinnerTextFormatter;
            return this;
        }

        /**
         * 设置Adapter
         */
        public Builder setmSelectAdapter(SearchSelectAdapter selectAdapter) {
            this.mSelectAdapter = selectAdapter;
            setListViewListener(mSelectAdapter);
            return this;
        }

        /**
         * 设置点击选中监听
         */
        public Builder setItemSelectedListener(OnSelectedListener selectedListener) {
            this.selectedListener = selectedListener;
            return this;
        }

        /**
         * 设置数据源
         * 注:调用此方法前可以先设置格式化内容
         */
        public void setDataSource(List<T> dataSource) {
            if (dataSource == null) {
                Log.e(TAG, "setDataSource()方法,dataSource不能为null!");
            } else {
                this.mListDataAll = dataSource;
                this.mSelectAdapter = new SearchSelectAdapter(mContext, new ArrayList(dataSource), getFormatShowText());
                setListViewListener(mSelectAdapter);
            }
        }

        /**
         * 重新设置/更新-列表数据->调用此方法前,必须保证已经设置了Adapter
         */
        public void setListData(List<T> collection) {
            if (this.mListDataAll == null) {
                this.mListDataAll = collection;
            } else {
                this.mListDataAll.clear();
                if (collection != null) {
                    this.mListDataAll.addAll(collection);
                }
            }
            if (this.mListDataAll != null) {
                this.mSelectAdapter.setData(new ArrayList<T>(mListDataAll));
            }
        }

        /**
         * 获取最后一次搜索记录
         */
        public String getLastSearchText() {
            return lastSearchText;
        }

        /**
         * 获取最后一次选择的值
         */
        public T getLastItem() {
            return lastItem;
        }

        /**
         * 添加一个元素
         */
        /*public void add(T item) {
            if (item != null) {
                this.mListDataAll.add(item);
                this.mSelectAdapter.add(item);
            }
        }*/

        /**
         * 添加多个元素
         */
        /*public void add(List<T> collection) {
            if (collection != null) {
                this.mListDataAll.addAll(collection);
                this.mSelectAdapter.add(collection);
            }
        }*/

        /**
         * 获取所有数据
         *
         * @return 存入的所有结果
         */
        public List<T> getData() {
            return this.mListDataAll;
        }

        /**
         * 获取列表上的数据
         *
         * @return ListView列表上的内容
         */
        public List<T> getAdapterItemAll() {
            return this.mSelectAdapter.getItemAll();
        }
    }

    public static abstract class OnSelectedListener {
        public abstract void onSelected(String showText, int position, Object t);
    }
}
