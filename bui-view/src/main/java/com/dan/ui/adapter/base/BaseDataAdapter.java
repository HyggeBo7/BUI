package com.dan.ui.adapter.base;

import java.util.List;

/**
 * Created by Bo on 2019/7/12 15:28
 */
public interface BaseDataAdapter<T> {

    /**
     * 添加到元素第一个
     */
    void addFirst(T item);

    /**
     * 添加多个元素到第一个
     *
     * @param collection 多个
     */
    void addFirst(List<T> collection);

    /**
     * 添加一个元素
     */
    void add(T item);

    /**
     * 添加多个元素
     */
    void add(List<T> collection);

    /**
     * 指定下标修改元素
     *
     * @param item  修改的元素
     * @param index 下标
     * @return true/false
     */
    boolean update(T item, int index);

    /**
     * 删除指定下标元素
     *
     * @param index 下标
     * @return true/false
     */
    boolean remove(int index);

    /**
     * 删除全部元素-默认清空全部,可以通过重写customizeRemove方法实现自定义条件删除
     *
     * @return true/false
     */
    boolean remove();

    /**
     * 根据下标获取元素
     *
     * @param index 下标
     * @return T
     */
    T getItemData(int index);

    /**
     * 获取全部数据
     *
     * @return list
     */
    List<T> getDataList();

    /**
     * 设置数据:
     * 1.有数据就清空,然后添加.
     * 2.传null，会自动new ArrayList一个
     *
     * @param collection 数据
     */
    void setData(List<T> collection);

    /**
     * 清空所有元素
     */
    void clear();

}
