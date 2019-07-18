package com.dan.dome.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dan.dome.R;
import com.dan.dome.entity.Material;
import com.dan.dome.fragment.base.BaseFragment;
import com.dan.library.util.JsonUtil;
import com.dan.library.util.ToastUtil;
import com.dan.ui.adapter.SwipeMenuAdapter;
import com.dan.ui.adapter.SwipeMenuViewHolder;
import com.dan.ui.widget.swipelist.SwipeMenuLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Bo on 2019/2/19 14:08
 */
public class IndexFragment extends BaseFragment {

    private static final String TAG = "IndexFragment";

    @BindView(R.id.li_listView)
    ListView listView;

    private List<Material> materialList = new ArrayList<>();

    private Integer dataSize = 100;

    private SwipeMenuAdapter<Material> materialSwipeMenuAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, null);
        super.initCreateView(view);
        initView(view);
        return view;
    }

    private void initView(View view) {
        //listView = view.findViewById(R.id.li_listView);
        listView.setAdapter(materialSwipeMenuAdapter = new SwipeMenuAdapter<Material>(getContext(), R.layout.item_cst_swipe) {
            @Override
            public void onBindViewHolder(SwipeMenuViewHolder holder, Material model, int position) {
                //((SwipeMenuLayout)holder.getConvertView()).setIos(false);//这句话关掉IOS阻塞式交互效果
                SwipeMenuLayout convertView = (SwipeMenuLayout) holder.getConvertView();
                if (model.getTempFlag() == null || !model.getTempFlag()) {
                    holder.setText(R.id.txt_code, "编码:");
                    holder.setText(R.id.txt_code_value, model.getMaterialCode());
                    //holder.tvDelete.setVisibility(View.GONE);
                    //holder.tvUpdate.setVisibility(View.GONE);
                    holder.findViewById(R.id.txt_weight).setVisibility(View.GONE);
                    holder.findViewById(R.id.txt_weight_value).setVisibility(View.GONE);
                    convertView.setSwipeEnable(false);
                    convertView.setSwipeClickEnable(false);
                } else {
                    holder.setText(R.id.txt_code, "批次:");
                    holder.setText(R.id.txt_code_value, model.getMaterialBatch());
                    holder.setText(R.id.txt_weight_value, model.getPackageCount().toString());
                    //holder.tvDelete.setVisibility(View.VISIBLE);
                    //holder.tvUpdate.setVisibility(View.VISIBLE);
                    holder.findViewById(R.id.txt_weight).setVisibility(View.VISIBLE);
                    holder.findViewById(R.id.txt_weight_value).setVisibility(View.VISIBLE);
                    convertView.setSwipeEnable(true);
                }
                holder.setText(R.id.txt_name_value, model.getMaterialName());
                String toJson = JsonUtil.toJson(model);
                holder.setOnClickListener(R.id.btnDelete, v -> {
                    ToastUtil.makeText(getActivity(), "删除:position:" + position + ",Material->" + toJson);
                    boolean removeFlag = this.remove(position);
                    if (removeFlag) {
                        ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                        Log.i(TAG, "删除:removeFlag:" + removeFlag + ",position:" + position + ",Material->" + toJson);
                    }
                });
                holder.setOnClickListener(R.id.btnUpdate, v -> {
                    ToastUtil.makeText(getActivity(), "修改:position:" + position + ",Material->" + toJson);
                    Log.i(TAG, "修改:position:" + position + ",Material->" + toJson);
                });
            }

            /**
             * 更改删除逻辑
             */
            @Override
            public boolean remove(List<Material> list) {
                if (list != null && list.size() > 0) {
                    boolean deleteFlag = false;
                    Iterator<Material> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Material next = iterator.next();
                        if (next.getTempFlag() != null && next.getTempFlag()) {
                            iterator.remove();
                            if (!deleteFlag) {
                                deleteFlag = true;
                            }
                        }
                    }
                    return deleteFlag;
                }
                return false;
            }
        });
        initData();
    }

    private void initData() {
        Material material = null;
        for (int i = 1; i <= dataSize; i++) {
            material = new Material();
            if (i % 3 == 0) {
                material.setTempFlag(true);
            }
            material.setMaterialBatch("20190219-" + i);
            material.setMaterialCode("编码Code-" + i);
            material.setMaterialName("名称Name-" + i);
            material.setPackageCount(i + 10);
            materialList.add(material);
        }
        materialSwipeMenuAdapter.setData(materialList);
    }

    @Override
    protected void setShowOrHide() {
        mainActivity.tvMainTitle.setText("首页");
    }
}
