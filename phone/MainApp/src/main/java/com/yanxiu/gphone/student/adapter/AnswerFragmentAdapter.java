package com.yanxiu.gphone.student.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.fragment.question.AnswerCardFragment;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.readquestion.InterViewPager;

import java.util.ArrayList;

public class AnswerFragmentAdapter extends FragmentPagerAdapter implements QuestionsListener {
	private ArrayList<Fragment> mFragments;
	private ViewPager mViewPager;

	public AnswerFragmentAdapter(FragmentManager fm) {
		super(fm);
		ArrayList<AnswerBean> list = new ArrayList<AnswerBean>();
		for(int i = 0; i < 5; i++){
			list.add(new AnswerBean());
		}
		mFragments = new ArrayList<Fragment>();

//		for(int i = 0; i < list.size(); i++){
////			mFragments.add(new ChoiceQuestionSingleFragment());
//			((QuestionsListener)mFragments.get(i)).flipNextPager(this);
//			((QuestionsListener)mFragments.get(i)).setDataSources(list.get(i));
//		}
		mFragments.add(new AnswerCardFragment());
//		((AnswerCardFragment)mFragments.get(list.size())).setDataSources(list);


	}

	public void setViewPager(ViewPager viewPager){
		mViewPager = viewPager;
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}



	@Override
	public void flipNextPager(QuestionsListener flip) {
//		LogInfo.log("geny", "anwser adapter flip");
		if((this.getCount()- 1) == mViewPager.getCurrentItem()){

			((InterViewPager)mViewPager).toParentViewPager();
//			LogInfo.log("geny", "InterViewPager last item");
		}else{
			mViewPager.setCurrentItem((mViewPager.getCurrentItem() + 1));
		}

	}

	@Override
	public void setDataSources(AnswerBean bean) {
		// do nothing
	}

	@Override
	public void initViewWithData(AnswerBean bean) {

	}

	@Override public void answerViewClick() {

	}
}
