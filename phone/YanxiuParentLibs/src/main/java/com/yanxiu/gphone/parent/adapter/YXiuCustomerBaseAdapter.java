package com.yanxiu.gphone.parent.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public abstract class YXiuCustomerBaseAdapter<T> extends BaseAdapter {

	protected List<T> mList;
	protected Activity mContext;
	protected ListView mListView;

	public YXiuCustomerBaseAdapter(Activity context) {
		this.mContext = context;
	}

	@Override
	public int getCount() {
		if (mList != null)
			return mList.size();
		else
			return 0;
	}

	@Override
	public T getItem(int position) {
		return mList == null ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	abstract public View getView(int position, View convertView, ViewGroup parent);

	public void setList(List<T> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	public List<T> getList() {
		return mList;
	}

	public void addMoreData(List<T> list) {
		if (mList != null) {
			this.mList.addAll(list);
		}else{
			mList = list;
		}
		notifyDataSetChanged();
	}

	public void setList(T[] list) {
		List<T> List = new ArrayList<T>(list.length);
		for (T t : list) {
			List.add(t);
		}
		setList(List);
	}

	public ListView getListView() {
		return mListView;
	}

	public void setListView(ListView listView) {
		mListView = listView;
	}

	public abstract void clearDataSrouces();
}
