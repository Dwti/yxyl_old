package com.yanxiu.gphone.student.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.student.fragment.question.BaseQuestionFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Canghaixiao.
 * Time : 2016/12/30 16:00.
 * Function :自定义继承于FragmentStatePagerAdapter的基类，主要用于大量item或者可能频繁操作等消耗内存较多的情况
 */

abstract class BaseMistakRedoAdapter<T> extends FragmentPagerAdapter {

    /**
     * 数据集
     */
    private List<T> mDatas = new ArrayList<>();
    /**
     * item第一个位置
     */
    public static final int ITEM_LEFT = 0;
    /**
     * item最后一个位置
     */
    public int ITEM_RIGHT = 0;

    public BaseMistakRedoAdapter(FragmentManager fm) {
        super(fm);
    }

    public BaseMistakRedoAdapter(FragmentManager fm,List<T> datas){
        super(fm);
        if (datas!=null) {
            this.mDatas.addAll(datas);
        }
    }

    /**
     * 初始化数据集
     *
     * @param datas 数据集合
     * @throws NullPointerException list不能为null
     */
    public void setData(List<T> datas) {
        this.mDatas.addAll(datas);
    }

    /**
     * 获得数据集合
     *
     * @return 返回当前数据集合
     */
    public List<T> getDatas() {
        return mDatas;
    }

    @Override
    public int getCount() {
        ITEM_RIGHT = mDatas == null ? 0 : mDatas.size();
        return ITEM_RIGHT;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        return fragment;
    }

    @Override
    public long getItemId(int position) {
        T t=mDatas.get(position);
        if (t!=null) {
            int hashcode = t.hashCode();
            return hashcode;
        }else {
            return position;
        }
    }

    @Override
    public Fragment getItem(int position) {
        T t = mDatas.get(position);
        Fragment fragment = CreatItemFragment(t, position);
        BaseQuestionFragment basePerfectFragment = (BaseQuestionFragment) fragment;
        ViewHolder holder = new ViewHolder();
        holder.position = position;
        holder.t = t;
        basePerfectFragment.setTagMessage(holder);
        return basePerfectFragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((Fragment) object).getView();
    }

    @Override
    public int getItemPosition(Object object) {
        ViewHolder holder = (ViewHolder) ((BaseQuestionFragment) object).getTagMessage();
        if (holder != null && holder.position < mDatas.size()) {
            T t = mDatas.get(holder.position);
            if (t!=null) {
                if (t.hashCode() == holder.t.hashCode()) {
                    return POSITION_UNCHANGED;
                }
            }else {
                return POSITION_NONE;
            }
        }
        return POSITION_NONE;
    }

    /**
     * 创建fragment
     *
     * @param t        数据
     * @param position 即将创建的item在viewpager里面的位置
     */
    protected abstract Fragment CreatItemFragment(T t, int position);

    /**
     * 标识，用于判断item是否需要重建
     */
    private class ViewHolder {
        int position;
        T t;
    }

}


