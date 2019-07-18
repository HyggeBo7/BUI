package com.dan.dome.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dan.dome.R;
import com.dan.dome.entity.User;
import com.dan.dome.fragment.base.BaseFragment;
import com.dan.ui.adapter.recycler.base.QuickRecyclerAdapter;
import com.dan.ui.widget.dragrecycler.DragSortListRecycler;
import com.dan.ui.widget.dragrecycler.DragSortListRecyclerAdapter;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Bo on 2019/2/19 14:08
 */
public class OtherListFragment extends BaseFragment {

    private static final String TAG = "OtherListFragment";

    @BindView(R.id.recycler_view)
    DragSortListRecycler dragSortListRecycler;

    @BindView(R.id.btn_add_all)
    Button btn_add_all;
    @BindView(R.id.btn_add_alls)
    Button btn_add_alls;

    @BindView(R.id.btn_clear)
    Button btn_clear;

    private DragSortListRecyclerAdapter<User> sortListRecyclerAdapter;

    private int valueIndex = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.other_list_fragment, null);
        super.initCreateView(view);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initView(View view) {
        //添加分割线
        dragSortListRecycler.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));

        sortListRecyclerAdapter = new DragSortListRecyclerAdapter<User>(getContext(), R.layout.recycler_item_book_shelf) {

            @Override
            public void onBindViewHolder(QuickViewHolder viewHolder, User user, int position) {
                viewHolder.setText(R.id.tv_name_value, user.getName());
                viewHolder.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        User user1 = getUser();
                        sortListRecyclerAdapter.add(user1);
                        dragSortListRecycler.getLayoutManager().scrollToPosition(sortListRecyclerAdapter.getItemCount() - 1);
                    }
                });
                viewHolder.findViewById(R.id.btn_add_s).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        List<User> userList = getUserListByNum(2);
                        sortListRecyclerAdapter.add(userList);
                        dragSortListRecycler.getLayoutManager().scrollToPosition(sortListRecyclerAdapter.getItemCount() - 1);
                    }
                });
                viewHolder.findViewById(R.id.btn_add_first).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        User user1 = getUser();
                        sortListRecyclerAdapter.addFirst(user1);
                        dragSortListRecycler.getLayoutManager().scrollToPosition(0);
                    }
                });
                viewHolder.findViewById(R.id.btn_add_first_s).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        List<User> userList = getUserListByNum(2);
                        sortListRecyclerAdapter.addFirst(userList);
                        dragSortListRecycler.getLayoutManager().scrollToPosition(0);
                    }
                });
                viewHolder.findViewById(R.id.img_delete_x).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sortListRecyclerAdapter.remove(position);
                    }
                });
            }
        };
        dragSortListRecycler.setAdapter(sortListRecyclerAdapter);

    }

    private void initData() {
        List<User> userList = new ArrayList<>();
        User user;
        for (int i = 1; i <= 20; i++) {
            user = new User();
            user.setName("名称：" + i);
            userList.add(user);
            valueIndex++;
        }
        sortListRecyclerAdapter.setData(userList);
    }

    private List<User> getUserListByNum(int num) {
        int thisNum = ++valueIndex;
        List<User> userList = new ArrayList<>();
        User user;
        for (int i = valueIndex; i <= (thisNum + num); i++) {
            user = new User();
            user.setName("名称：" + i);
            userList.add(user);
        }
        valueIndex += num;
        return userList;
    }

    private User getUser() {
        valueIndex++;
        User user = new User();
        user.setName("名称:" + valueIndex);
        return user;
    }

    private void initListener() {
        sortListRecyclerAdapter.setOnItemClickListener(new QuickRecyclerAdapter.OnItemClickListener<User>() {
            @Override
            public void onItemClick(View view, User user, int position) {
                System.out.println("setOnItemClickListener===>" + user.getName());
                ToastUtils.toast(user.getName());
            }
        });
        sortListRecyclerAdapter.setOnItemLongClickListener(new QuickRecyclerAdapter.OnItemLongClickListener<User>() {
            @Override
            public void onItemLongClick(View view, User user, int position) {
                System.out.println("setOnItemLongClickListener===>" + user.getName());
            }
        });
        //添加
        btn_add_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user1 = getUser();
                sortListRecyclerAdapter.add(user1);
                dragSortListRecycler.getLayoutManager().scrollToPosition(sortListRecyclerAdapter.getItemCount() - 1);
            }
        });
        //添加多个
        btn_add_alls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<User> userList = getUserListByNum(2);
                sortListRecyclerAdapter.add(userList);
                dragSortListRecycler.getLayoutManager().scrollToPosition(sortListRecyclerAdapter.getItemCount() - 1);
            }
        });
        //清空
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortListRecyclerAdapter.clear();
            }
        });
    }


    @Override
    protected void setShowOrHide() {
        mainActivity.tvMainTitle.setText("其他列表");
    }
}
