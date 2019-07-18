package com.dan.ui.widget.grouplist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dan.ui.R;
import com.dan.ui.adapter.SimpleSpinnerTextFormatter;
import com.dan.ui.adapter.impl.SpinnerTextFormatter;
import com.dan.ui.adapter.SimpleTextAdapter;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ViewMiddle<T> extends LinearLayout implements ViewBaseAction {

    private Context context;
    /**
     * 左边
     */
    private ListView regionListView;
    /**
     * 右边
     */
    private ListView plateListView;
    /**
     * Key:显示左边,value显示右边
     */
    private Map<String, List<T>> groupMap = new LinkedHashMap<>();
    /**
     * 左边集合
     */
    private List<String> groupList = new LinkedList<String>();
    /**
     * 右边集合
     */
    private LinkedList<T> childrenItem = new LinkedList<T>();

    private SimpleTextAdapter regionListViewAdapter;

    private SimpleTextAdapter plateListViewAdapter;

    private OnSelectListener mOnSelectListener;

    private SpinnerTextFormatter regionSpinnerTextFormatter = new SimpleSpinnerTextFormatter();
    private SpinnerTextFormatter plateSpinnerTextFormatter = new SimpleSpinnerTextFormatter();

    private int lastRegionIndex = -1;
    private int lastPlateIndex = -1;
    private String showString;
    private T selectItem;

    public ViewMiddle(Context context) {
        super(context);
        init(context);
    }

    public ViewMiddle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.etv_view_region, this, true);
        regionListView = findViewById(R.id.listView);
        plateListView = findViewById(R.id.listView2);
        setBackground(getResources().getDrawable(R.drawable.etv_expand_bg_left));

    }

    /**
     * 获取右边选中的值
     *
     * @param regionIndex 左边下标
     * @param plateIndex  右边下标
     * @return 选中的对象
     */
    public T setPlateDefaultSelectAndValue(int regionIndex, int plateIndex) {
        if (regionIndex > -1 && regionIndex < groupList.size()) {
            List<T> list = groupMap.get(groupList.get(regionIndex));
            childrenItem.addAll(list);
            regionListView.setSelection(regionIndex);
            lastRegionIndex = regionIndex;
            regionListViewAdapter.setSelectedPositionNoNotify(regionIndex);
            if (list != null && plateIndex > -1 && plateIndex < list.size()) {
                plateListView.setSelection(plateIndex);
                selectItem = list.get(plateIndex);
                lastPlateIndex = plateIndex;
                showString = plateSpinnerTextFormatter.format(selectItem).toString();
                plateListViewAdapter.setSelectedPositionNoNotify(plateIndex);
                return selectItem;
            }
        }
        return null;
    }

    /**
     * 获取选择显示的值
     */
    public String getShowText() {
        return showString;
    }

    /**
     * 获取选择的那项
     */
    public T getSelectItem() {
        return selectItem;
    }

    /**
     * 获取选中的下标
     */
    public int getSelectPlateIndex() {
        return lastPlateIndex;
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public void setDataSource(Map<String, List<T>> groupMapValue) {
        if (groupMapValue == null) {
            return;
        }
        //清空旧值
        if (!groupMap.isEmpty()) {
            groupMap.clear();
        }
        if (!groupList.isEmpty()) {
            groupList.clear();
        }
        //设置新值
        groupMap.putAll(groupMapValue);
        groupList.addAll(new LinkedList<String>(groupMap.keySet()));

        regionListViewAdapter = new SimpleTextAdapter(context, groupList, R.drawable.etv_expand_item_selected, R.drawable.item_etv_choose_ear_selector);
        regionListViewAdapter.setTextSize(16);
        regionListViewAdapter.setTextFormatter(regionSpinnerTextFormatter);
        //regionListViewAdapter.setSelectedPositionNoNotify(tEaraPosition);

        regionListViewAdapter.setOnItemClickListener(new SimpleTextAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String selectedText, Object o) {
                lastRegionIndex = position;
                if (groupMap.containsKey(selectedText)) {
                    childrenItem.clear();
                    childrenItem.addAll(groupMap.get(selectedText));
                    plateListViewAdapter.notifyDataSetChanged();
                }
            }
        });
        setRegionAdapter(regionListViewAdapter);
        /*if (tEaraPosition < groupList.size()) {
            childrenItem.addAll(groupMap.get(groupList.get(tEaraPosition)));
        }*/

        plateListViewAdapter = new SimpleTextAdapter(context, childrenItem, R.drawable.etv_expand_item_right, R.drawable.item_etv_choose_plate_selector);
        plateListViewAdapter.setTextSize(14);
        plateListViewAdapter.setTextFormatter(plateSpinnerTextFormatter);
        plateListViewAdapter.setOnItemClickListener(new SimpleTextAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String selectedText, Object o) {
                selectItem = childrenItem.get(position);
                showString = plateSpinnerTextFormatter.format(selectItem).toString();
                lastPlateIndex = position;
                if (mOnSelectListener != null) {
                    mOnSelectListener.getItem(showString, selectItem);
                }
            }
        });
        setPlateAdapter(plateListViewAdapter);

    }

    /**
     * 设置右边格式
     */
    public void setPlateTextFormatter(SpinnerTextFormatter spinnerTextFormatter) {
        this.plateSpinnerTextFormatter = spinnerTextFormatter;
    }

    public void setRegionAdapter(SimpleTextAdapter regionAdapter) {
        regionListView.setAdapter(regionAdapter);
    }

    public void setPlateAdapter(SimpleTextAdapter regionAdapter) {
        plateListView.setAdapter(regionAdapter);
    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

    public interface OnSelectListener<T> {
        public void getItem(String showText, T t);
    }

}
