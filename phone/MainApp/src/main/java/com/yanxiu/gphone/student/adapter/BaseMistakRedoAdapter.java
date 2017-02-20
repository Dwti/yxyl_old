package com.yanxiu.gphone.student.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

abstract class BaseMistakRedoAdapter<T> extends FragmentStatePagerAdapter {

    private Map<String, WeakReference<BaseQuestionFragment>> map = new HashMap<>();
    private List<T> mDatas = new ArrayList<>();
    public static final int ITEM_LEFT = 0;
    public static int ITEM_RIGHT = 0;

    BaseMistakRedoAdapter(FragmentManager fm) {
        super(fm);
    }

    BaseMistakRedoAdapter(FragmentManager fm, List<T> datas) {
        super(fm);
        if (datas != null) {
            this.mDatas.addAll(datas);
        }
    }

    public void setData(List<T> datas) {
        this.mDatas.clear();
        this.mDatas.addAll(datas);
    }

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
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {
        BaseQuestionFragment fragment = null;
        ViewHolder holder = null;
        T t = mDatas.get(position);
        String key = makeItemName(getItemId(position));
        if (map != null) {
            WeakReference<BaseQuestionFragment> weak = map.get(key);
            if (weak != null) {
                fragment = weak.get();
                if (fragment != null) {
                    Object o = fragment.getTagMessage();
                    if (o != null) {
                        holder = (ViewHolder) o;
                    } else {
                        holder = new ViewHolder();
                    }
                }
            }
        }
        if (fragment == null) {
            fragment = (BaseQuestionFragment) CreatItemFragment(t, position);
            holder = new ViewHolder();
        }
        if (fragment == null) {
            throw new NullPointerException("The fragment cannot be NULL.");
        }
        holder.position = position;
        holder.t = t;
        fragment.setTagMessage(holder);
        if (map == null) {
            map = new HashMap<>();
        }
        map.put(key, new WeakReference<>(fragment));
        return fragment;
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
            if (t != null && holder.t != null && t.hashCode() == holder.t.hashCode()) {
                return POSITION_UNCHANGED;
            } else if ((t != null && holder.t == null) || (t == null && holder.t != null)) {
                return POSITION_NONE;
            } else {
                return POSITION_UNCHANGED;
            }
        }
        return POSITION_NONE;
    }

    protected abstract Fragment CreatItemFragment(T t, int position);

    private long getItemId(int position) {
        T t = this.mDatas.get(position);
        if (t != null) {
            return t.hashCode();
        } else {
            return position;
        }
    }

    private static String makeItemName(long id) {
        return "CwqLibrary:" + "BaseFragmentStatePagerAdapter" + ":" + id;
    }

    private class ViewHolder {
        int position;
        T t;
    }
}


