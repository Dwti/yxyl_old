package com.yanxiu.gphone.studentold.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import com.yanxiu.gphone.studentold.fragment.question.PhotoFragmentFactory;

import java.util.ArrayList;

public class PhotoAdapter extends FragmentStatePagerAdapter {

	private ArrayList<Fragment> mFragments;
	private ArrayList<String> dataSources;

	public PhotoAdapter(FragmentManager fm) {
		super(fm);
		mFragments = new ArrayList<Fragment>();
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	@Override
	public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
	}


	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}

	public void setDataSources(ArrayList<String> dataSources) {
		this.dataSources = dataSources;
		for(String uri : dataSources){
			Fragment fragment  = PhotoFragmentFactory.getInstance().createPhotoFragment(uri);
			mFragments.add(fragment);
		}
	}



}
