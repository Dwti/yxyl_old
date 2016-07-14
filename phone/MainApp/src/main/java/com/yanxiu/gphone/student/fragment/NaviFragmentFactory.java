package com.yanxiu.gphone.student.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.fragment.GroupFragment;
import com.yanxiu.gphone.student.fragment.HomeWorkFragment;
import com.yanxiu.gphone.student.fragment.MyFragment;


public class NaviFragmentFactory {
	private int currItem = 0;
	private HomeWorkFragment mHomeWorkFragment;
	private GroupFragment mGroupFragment;
	private MyFragment mMyFragment;
	public NaviFragmentFactory() {
	}
	public int getCurrentItem(){
		return currItem;
	}
	public int getCount() {
		return 3;
	}
	public Fragment getItem(int item){
		if(item == 0){
			return mHomeWorkFragment;
		} else if(item == 1){
			return mGroupFragment;
		}else if(item == 2){
			return mMyFragment;
		}else {
			return mHomeWorkFragment;
		}
	}
	private void hideFragment(FragmentTransaction transaction){
		if (currItem == 0 && mHomeWorkFragment != null) {
			transaction.hide(mHomeWorkFragment);
		}
		if (currItem == 1 && mGroupFragment != null) {
			transaction.hide(mGroupFragment);
		}
		if (currItem == 2 && mMyFragment != null) {
			transaction.hide(mMyFragment);
		}
	}
	public void hideAndShowFragment(FragmentManager fragmentManager, int index){
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (currItem == 0 && mHomeWorkFragment != null) {
			transaction.hide(mHomeWorkFragment);
		}
		if (currItem == 1 && mGroupFragment != null) {
			transaction.hide(mGroupFragment);
		}
		if (currItem == 2 && mMyFragment != null) {
			transaction.hide(mMyFragment);
		}
		currItem = index;
		switch (currItem) {
			case 0:
				if (mHomeWorkFragment == null) {
					mHomeWorkFragment = new HomeWorkFragment();
					transaction.add(R.id.content_main, mHomeWorkFragment);
				} else {
					transaction.show(mHomeWorkFragment);
				}
				break;
			case 1:
				if (mGroupFragment == null) {
					mGroupFragment = new GroupFragment();
					transaction.add(R.id.content_main, mGroupFragment);
				} else {
					transaction.show(mGroupFragment);
					LogInfo.log("king", "transaction.show");
					mGroupFragment.requestGroupData(true,false);
				}
				break;
			case 2:
				if (mMyFragment == null) {
					mMyFragment = new MyFragment();
					transaction.add(R.id.content_main, mMyFragment);
				} else {
					transaction.show(mMyFragment);
				}
				break;
		}
		transaction.commit();
	}
}
