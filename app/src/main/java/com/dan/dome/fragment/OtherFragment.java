package com.dan.dome.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dan.dome.R;
import com.dan.dome.activity.CameraActivity;
import com.dan.dome.entity.City;
import com.dan.dome.entity.Material;
import com.dan.dome.fragment.base.BaseFragment;
import com.dan.library.util.JsonUtil;
import com.dan.ui.adapter.SimpleSpinnerTextFormatter;
import com.dan.ui.widget.grouplist.ExpandTabView;
import com.dan.ui.widget.grouplist.ViewMiddle;
import com.dan.ui.widget.searchselect.SearchSelectDialog;
import com.xuexiang.xutil.tip.ToastUtils;

import org.angmarch.views.NiceSpinner;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Bo on 2019/2/19 14:08
 */
public class OtherFragment extends BaseFragment {
    private static final String TAG = "OtherFragment";
    //下拉列表
    @BindView(R.id.nice_spinner)
    NiceSpinner niceSpinner;

    //ExpandTabView 点击按首字母筛选
    @BindView(R.id.etv_list)
    ExpandTabView expandTabView;

    private List<View> mViewArray = new ArrayList<View>();
    private ViewMiddle viewMiddle;
    private Map<String, List<Material>> mapList = new LinkedHashMap<>();

    //弹框搜索
    @BindView(R.id.btn_open_search_dialog)
    Button btnOpenSearchDialog;

    @BindView(R.id.tv_search_result)
    TextView textSearchResultView;

    //打开相机
    @BindView(R.id.btn_open_camera)
    Button btnOpenCamera;

    private List<City> mDataList;
    private SearchSelectDialog searchSelectDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.other_fragment, null);
        super.initCreateView(view);
        initView(view);
        initListener();
        return view;
    }

    private void initView(View view) {
        //niceSpinner = view.findViewById(R.id.nice_spinner);
        //expandTabView = view.findViewById(R.id.etv_list);

        viewMiddle = new ViewMiddle(getContext());
        mViewArray.add(viewMiddle);
        //设置创建时按钮禁用
        expandTabView.setCreateToggleButtonEnabled(false);
        expandTabView.setValue(Arrays.asList("原料加载中..."), mViewArray);
        //设置右边显示数据,格式化
        viewMiddle.setPlateTextFormatter(new SimpleSpinnerTextFormatter() {
            @Override
            public Spannable format(Object item) {
                String value;
                if (item instanceof Material) {
                    value = ((Material) item).getMaterialName();
                } else {
                    value = item.toString();
                }
                return new SpannableString(value);
            }
        });

        //弹出搜索
        //btnOpenSearchDialog = view.findViewById(R.id.btn_open_search_dialog);
        //textSearchResultView = view.findViewById(R.id.tv_search_result);
        setData();
        createSearchSelectDialog();
        //设置数据
        searchSelectDialog.getBuilder().setDataSource(mDataList);
    }

    private void initListener() {
        //设置下拉框监听
        viewMiddle.setOnSelectListener((showText, o) -> {
            expandTabView.onPressBack();
            //就新增了一个选择原料，所以直接设置0
            int index = 0;
            if (!expandTabView.getTitle(index).equals(showText)) {
                expandTabView.setTitle(showText, index);
                if (o instanceof Material) {
                    Material material = (Material) o;
                    Log.i(TAG, "选择material:" + JsonUtil.toJson(material));
                    ToastUtils.toast("选择material:" + JsonUtil.toJson(material));
                }
            }
        });
        //设置弹出按钮
        btnOpenSearchDialog.setOnClickListener(v -> {
            //调用弹出窗口
            searchSelectDialog.show();
        });
    }

    private void createSearchSelectDialog() {
        SearchSelectDialog.Builder alert = new SearchSelectDialog.Builder(getContext());
        searchSelectDialog = alert.setSpinnerTextFormatter(new SimpleSpinnerTextFormatter() {
            @Override
            public Spannable format(Object item) {
                String value;
                if (item instanceof City) {
                    City item1 = (City) item;
                    value = item1.getName() + "=new=" + item1.getId();
                } else {
                    value = item.toString();
                }
                return new SpannableString(value);
            }
        }).setItemSelectedListener(new SearchSelectDialog.OnSelectedListener() {
            @Override
            public void onSelected(String showText, int position, Object t) {
                textSearchResultView.setText(showText);
                ToastUtils.toast("showText:" + showText);
                Log.i(TAG, "showText:" + showText + ";t:" + JsonUtil.toJson(t));
            }

        }).setTitle("自定义标题").setReservedFlag(true).builder();
        //设置Dialog 尺寸
        searchSelectDialog.setDialogWindowAttr(0.9, 0.9, getActivity());
    }

    private void setData() {
        List<String> dataset = new ArrayList<>(Arrays.asList("One", "Two", "Three", "Four", "Five"));
        niceSpinner.attachDataSource(dataset);

        List<Material> materialList = null;
        Material user = null;
        long id = 0;
        for (int i = 1; i <= 27; i++) {
            materialList = new ArrayList<>();
            for (int j = 1; j <= 50; j++) {
                user = new Material();
                id = i + j;
                user.setId(id);
                user.setMaterialName("MaterialName:i->" + i + "===j->" + j);
                user.setMaterialCode("MaterialCode:i->" + i + "===j->" + j);
                materialList.add(user);
            }
            mapList.put(i + "Key", materialList);
        }
        viewMiddle.setDataSource(mapList);

        //设置选中第一个
        Material material = (Material) viewMiddle.setPlateDefaultSelectAndValue(0, 0);
        if (material == null) {
            return;
        }
        //有数据时,取消禁用
        expandTabView.setToggleButton(true);
        Log.i(TAG, "设置默认选中:material:" + JsonUtil.toJson(material));
        ToastUtils.toast("设置默认选中:material:" + JsonUtil.toJson(material));
        if (StringUtils.isNotBlank(viewMiddle.getShowText())) {
            expandTabView.setTitle(viewMiddle.getShowText(), 0);
        }


        //设置弹出框搜索数据
        mDataList = new ArrayList<>();
        String[] citys = {"武汉", "北京", "上海", "深圳", "兰州", "成都", "天津"};
        City city;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < citys.length; j++) {
                city = new City();
                city.setId((i + j) + 10);
                city.setCode("Code:" + citys[j] + i);
                city.setName(citys[j] + i);
                mDataList.add(city);
            }
        }
    }

    @OnClick({R.id.btn_open_camera})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open_camera:
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                startActivityForResult(intent, 77);
                break;
            default:
                break;
        }
    }

    @Override
    protected void setShowOrHide() {

        mainActivity.tvMainTitle.setText("其他");
    }
}
