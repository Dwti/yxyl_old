package com.yanxiu.gphone.parent.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.yanxiu.gphone.parent.R;


public class ParentNavFragmentFactory {
	private int currItem = 0;
	private ParentHomeFragment mHomeFragment;
	private ParentWeekReportFragment mWeekReportFragment;
	private ParentHonorFragment mHonorFragment;
	private ParentMyFragment mMyFragment;
	public ParentNavFragmentFactory () {
	}
	public int getCurrentItem(){
		return currItem;
	}
	public int getCount() {
		return 4;
	}
	public Fragment getItem(int item){
		if(item == 0){
			return mHomeFragment;
		} else if(item == 1){
			return mWeekReportFragment;
		}else if(item == 2){
			return mHonorFragment;
		}else if(item == 3){
			return mMyFragment;
		}else {
			return mHomeFragment;
		}
	}
	private void hideFragment(FragmentTransaction transaction){
		if (currItem == 0 && mHomeFragment != null) {
			transaction.hide(mHomeFragment);
		}
		if (currItem == 1 && mWeekReportFragment != null) {
			transaction.hide(mWeekReportFragment);
		}
		if (currItem == 2 && mHonorFragment != null) {
			transaction.hide(mHonorFragment);
		}
		if (currItem == 3 && mMyFragment != null) {
			transaction.hide(mMyFragment);
		}
	}
	public void hideAndShowFragment(FragmentManager fragmentManager, int index){
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		hideFragment(transaction);
		currItem = index;
		switch (currItem) {
			case 0:
				if (mHomeFragment == null) {
					mHomeFragment = new ParentHomeFragment();
					transaction.add(R.id.content_parent_main, mHomeFragment);
				} else {
					transaction.show(mHomeFragment);
				}
				break;
			case 1:
				if (mWeekReportFragment == null) {
					mWeekReportFragment = new ParentWeekReportFragment();
					transaction.add(R.id.content_parent_main, mWeekReportFragment);
				} else {
					transaction.show(mWeekReportFragment);
				}
				break;
			case 2:
				if (mHonorFragment == null) {
					mHonorFragment = new ParentHonorFragment();
					transaction.add(R.id.content_parent_main, mHonorFragment);
				} else {
					transaction.show(mHonorFragment);
				}
				break;
			case 3:
				if (mMyFragment == null) {
					mMyFragment = new ParentMyFragment();
					transaction.add(R.id.content_parent_main, mMyFragment);
				} else {
					transaction.show(mMyFragment);
				}
				break;
		}
		transaction.commit();
	}
}
