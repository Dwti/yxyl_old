/** 
 * @ProjectName:CYFrameAndroid  
 * @Title: CYouBaseAdapter.java 
 * @Package com.cyou.cyframeandroid.adapter 
 * @Description: (adapter基类,将viewholder封装出去了,保证adapter的独立使用) 
 * @author liuqi qiliu_17173@cyou-inc.com   
 * @date 2013-8-6 下午1:45:08 
 * @version V1.0   
 * Copyright (c) 2013搜狐公司-版权所有
 */
package com.yanxiu.gphone.student.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @Description: (封装的自定义adapter,暴露了两个方法给外部,外部可以自己实现viewcreator中的方法)
 * @date 2013-8-6 下午1:45:08
 */
public class YXiuBaseAdapter<E> extends BaseAdapter {

	/**
	 * 
	 * @ClassName: ViewCreator
	 * @Description: (提供接口供外部使用)
	 * @author liuqi qiliu_17173@cyou-inc.com
	 * @date 2013-8-6 下午1:46:54
	 * @param <E>
	 */
	public interface ViewCreator<E> {
		/**
		 * 
		 * @Title: createView
		 * @Description: (创建adapter中item的view)
		 * @param: @param inflater
		 * @param: @param position
		 * @param: @param data
		 * @param: @return 设定文件
		 * @return: View 返回类型
		 * @date: 2013-8-6 下午1:47:18
		 */
		View createView(LayoutInflater inflater, int position, E data);

		/**
		 * 
		 * @Title: updateView
		 * @Description: (跟新viewholder中控件的数据)
		 * @param: @param view
		 * @param: @param position
		 * @param: @param data 设定文件
		 * @return: void 返回类型
		 * @date: 2013-8-6 下午1:47:58
		 */
		void updateView(View view, int position, E data);
	}

	private static class ViewHolder {
		public View view;
	}

	private List<E> mDataCache;

	private LayoutInflater mInflater;

	private ViewCreator<E> mCreator;

	public YXiuBaseAdapter(LayoutInflater inflater, ViewCreator<E> creator) {
		mInflater = inflater;
		mCreator = creator;
	}

	/**
	 * 
	 * @Title: update
	 * @Description: (跟新adpater中的数据并刷新)
	 * @param: @param data 设定文件
	 * @return: void 返回类型
	 * @date: 2013-8-6 下午1:48:35
	 */
	public void update(List<E> data) {
		mDataCache = data;
		notifyDataSetChanged();
	}

	/**
	 * 
	 * @Title: add
	 * @Description: (向adapter中添加数据并刷新)
	 * @param: @param set 设定文件
	 * @return: void 返回类型
	 * @date: 2013-8-6 下午1:49:13
	 */
	public void add(List<E> set) {
		if (null == mDataCache)
			mDataCache = new ArrayList<E>();
		if (null == set)
			return;
		mDataCache.addAll(set);
		notifyDataSetChanged();
	}

	public void clearAll() {
		if (null != mDataCache)
			mDataCache.clear();
	}

	/**
	 * 
	 * @Title: add
	 * @Description: (向adapter中添加数据并刷新)
	 * @param: @param item 设定文件
	 * @return: void 返回类型
	 * @date: 2013-8-6 下午1:49:40
	 */
	public void add(E item) {
		if (null == mDataCache)
			mDataCache = new ArrayList<E>();
		mDataCache.add(item);
		notifyDataSetChanged();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return null == mDataCache ? 0 : mDataCache.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	public E getItem(int position) {
		return null == mDataCache ? null : mDataCache.get(position);
	}

	/**
	 * 
	 * @Title: getAllItem
	 * @Description: 获取所有Item 数据
	 * @param: @return 设定文件
	 * @return: List<E> 返回类型
	 * @date: 2013-8-14 下午6:11:58
	 */
	public List<E> getAllItem() {
		return mDataCache;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mCreator.createView(mInflater, position, getItem(position));
			convertView.setTag(holder);
			holder.view = convertView;
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		mCreator.updateView(holder.view, position, getItem(position));
		return convertView;
	}
}
