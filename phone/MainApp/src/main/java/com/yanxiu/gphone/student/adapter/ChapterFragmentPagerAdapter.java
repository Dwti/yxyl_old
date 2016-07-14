package com.yanxiu.gphone.student.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import com.yanxiu.gphone.student.bean.SubjectVersionBean;
import com.yanxiu.gphone.student.fragment.ChapterFragment;
import com.yanxiu.gphone.student.fragment.TestCenterFragment;

public class ChapterFragmentPagerAdapter extends FragmentStatePagerAdapter {

	private FragmentManager fm ;

	private static final int COUNT = 2;

	private Fragment[] fragments;


	public ChapterFragmentPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		this.fm = fm ;
		fragments = new Fragment[COUNT];
	}

	public void setDataSources(SubjectVersionBean.DataEntity dataEntity, String volume, String volumeName){
		for(int pageId = 0; pageId < COUNT; pageId++){
			switch (pageId) {
				case 0:
					if (fragments[pageId] == null) {
						fragments[pageId] = new ChapterFragment();
					}
				case 1:
					if (fragments[pageId] == null) {
						fragments[pageId] = new TestCenterFragment();
					}
			}
			Bundle args = new Bundle();
			args.putSerializable("dataEntity", dataEntity);
			args.putString("volume", volume);
			args.putString("volumeName", volumeName);
			fragments[pageId].setArguments(args);
		}
	}


	public void setFilterSources(SubjectVersionBean.DataEntity dataEntity, String volume, String volumeName){
		int pageId = 0;
		fragments[pageId] = new ChapterFragment();
		Bundle args = new Bundle();
		args.putSerializable("dataEntity", dataEntity);
		args.putString("volume", volume);
		args.putString("volumeName", volumeName);
		fragments[pageId].setArguments(args);
	}


	@Override
	public Fragment getItem(int pos) {
		return fragments[pos];
	}


	public void format(){
	}

	@Override
	public int getCount() {
		return fragments.length;
	}

	@Override
	public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
	}

}
