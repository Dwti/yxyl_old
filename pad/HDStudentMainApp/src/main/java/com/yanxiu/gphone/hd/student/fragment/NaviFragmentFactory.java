package com.yanxiu.gphone.hd.student.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;


public class NaviFragmentFactory {
    private final static String TAG=NaviFragmentFactory.class.getSimpleName();
	private YanXiuConstant.TITLE_ENUM currItem= YanXiuConstant.TITLE_ENUM.EXIST_ENUM ;
	private HomePageFragment mHomePageFragment;//首页
	private GroupInfoContainerFragment mGroupContainerFragment; //作业
	private SetContainerFragment mSetFragment; //设置
	private ExercisesContainerHisFragment mExerciseHisFg;//练习历史
	private MyCollectContainerFragment mMyCollectFg; //收藏
	private MyErrorRecordContainerFragment mMyErrorRecordFg; //错题
	private RankingContainerFragment mRankingFg; //排行榜
	private MyUserInfoContainerFragment mMyUserInfoFg; //个人信息

	public NaviFragmentFactory() {
	}
	public YanXiuConstant.TITLE_ENUM getCurrentItem(){
		return currItem;
	}

	public void resetFragmentContainer(){

		mHomePageFragment = null;
		mGroupContainerFragment = null;
		mSetFragment = null;
		mExerciseHisFg = null;
		mMyCollectFg = null;
		mMyErrorRecordFg = null;
		mRankingFg = null;
		mMyUserInfoFg = null;
	}
	public Fragment getCurrentItemFg(){
		return getItem(currItem);
	}


	public Fragment getItem(YanXiuConstant.TITLE_ENUM item){
		switch (item){
			case MYUSERINFO_ENUM:
				return mMyUserInfoFg;
			case EXIST_ENUM:
				return  mHomePageFragment;
			case HOMEWORK_ENUM:
				return mGroupContainerFragment;
			case RANKING_ENUM:
				return mRankingFg;
			//case COLLECT_ENUM:
				//return mMyCollectFg;
			case ERROR_ENUM:
				return mMyErrorRecordFg;
			case HIS_ENUM:
				return mExerciseHisFg;
			case SET_ENUM:
				return mSetFragment;
			default:
				return mHomePageFragment;
		}

	}



	public void hideAndShowFragment(FragmentManager fragmentManager, YanXiuConstant.TITLE_ENUM index){
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.addToBackStack(null);
		currItem = index;
		if ( mHomePageFragment != null) {
			LogInfo.log(TAG,"mHomePageFragment transaction.hide");
			transaction.hide(mHomePageFragment);
		}

		if ( mSetFragment != null) {
			LogInfo.log(TAG,"SetFragment transaction.hide");
			transaction.hide(mSetFragment);
		}

		if ( mMyUserInfoFg != null) {
			LogInfo.log(TAG,"mMyUserInfoFg transaction.hide");
			transaction.hide(mMyUserInfoFg);
		}
		if ( mGroupContainerFragment != null) {
			LogInfo.log(TAG,"mGroupContainerFragment transaction.hide");
			transaction.hide(mGroupContainerFragment);
		}

		if ( mRankingFg != null) {
			LogInfo.log(TAG,"mRankingFg transaction.hide");
			transaction.hide(mRankingFg);
		}
		if ( mMyCollectFg != null) {
			LogInfo.log(TAG,"mMyCollectFg transaction.hide");
			transaction.hide(mMyCollectFg);
		}
		if ( mMyErrorRecordFg != null) {
			LogInfo.log(TAG,"mMyErrorRecordFg transaction.hide");
			transaction.hide(mMyErrorRecordFg);
		}

		if ( mExerciseHisFg != null) {
			LogInfo.log(TAG,"mExerciseHisFg transaction.hide");
			transaction.hide(mExerciseHisFg);
		}
		LogInfo.log(TAG,"CurrItem: "+currItem);
		switch (currItem) {
			case MYUSERINFO_ENUM:
				if (mMyUserInfoFg == null) {
					mMyUserInfoFg = new MyUserInfoContainerFragment();
					transaction.add(R.id.content_main,mMyUserInfoFg);
				}else{
					transaction.show(mMyUserInfoFg);
				}
				break;
			case EXIST_ENUM:
				if (mHomePageFragment == null) {
					mHomePageFragment = new HomePageFragment();
					transaction.add(R.id.content_main, mHomePageFragment);
				} else {
					transaction.show(mHomePageFragment);
				}
				break;
			case HOMEWORK_ENUM:
				if (mGroupContainerFragment == null) {
					mGroupContainerFragment = new GroupInfoContainerFragment();
					transaction.add(R.id.content_main,mGroupContainerFragment);
					LogInfo.log(TAG, "mGroupContainerFragment:transaction.add ");
				}else{
					transaction.show(mGroupContainerFragment);
					LogInfo.log(TAG, "mGroupContainerFragment:transaction.show");
				}
				break;

			case RANKING_ENUM:
				if (mRankingFg == null) {
					mRankingFg = new RankingContainerFragment();
					transaction.add(R.id.content_main,mRankingFg);
					LogInfo.log(TAG, "mRankingFg:transaction.add ");
				}else{
					transaction.show(mRankingFg);
					LogInfo.log(TAG, "mRankingFg:transaction.show ");
				}

				break;
			/*case COLLECT_ENUM:
				if (mMyCollectFg == null) {
					mMyCollectFg = new MyCollectContainerFragment();
					transaction.add(R.id.content_main, mMyCollectFg);
					LogInfo.log(TAG, "mMyCollectFg:transaction.add ");
				}else{
					transaction.show(mMyCollectFg);
					LogInfo.log(TAG, "mMyCollectFg:transaction.show ");
				}

				break;*/

			case ERROR_ENUM :
				if (mMyErrorRecordFg == null) {
					mMyErrorRecordFg = new MyErrorRecordContainerFragment();
					transaction.add(R.id.content_main, mMyErrorRecordFg);
					LogInfo.log(TAG, "mMyErrorRecordFg:transaction.add ");
				}else{
					transaction.show(mMyErrorRecordFg);
					LogInfo.log(TAG, "mMyErrorRecordFg:transaction.show ");
				}
				break;
			case HIS_ENUM :
				if (mExerciseHisFg == null) {
					mExerciseHisFg = new ExercisesContainerHisFragment();
					transaction.add(R.id.content_main, mExerciseHisFg);
					LogInfo.log(TAG, "mExerciseHisFg:transaction.add ");
				}else{
					transaction.show(mExerciseHisFg);
					LogInfo.log(TAG, "mExerciseHisFg:transaction.show ");
				}
				break;
			case SET_ENUM:
				if (mSetFragment == null) {
					mSetFragment = new SetContainerFragment();
					transaction.add(R.id.content_main, mSetFragment);
					LogInfo.log(TAG, "Set:transaction.add " );
				}else{
					transaction.show(mSetFragment);
					LogInfo.log(TAG, "Set:transaction.show ");
				}

				break;
		}
		transaction.commit();
	}



}
