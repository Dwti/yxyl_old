package com.yanxiu.gphone.hd.student.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.hd.student.bean.AnswerBean;
import com.yanxiu.gphone.hd.student.bean.PaperTestEntity;
import com.yanxiu.gphone.hd.student.bean.QuestionEntity;
import com.yanxiu.gphone.hd.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.hd.student.fragment.question.QuestionFragmentFactory;
import com.yanxiu.gphone.hd.student.view.question.QuestionsListener;
import com.yanxiu.gphone.hd.student.view.question.readquestion.InterViewPager;

import java.util.ArrayList;
import java.util.List;

import static com.yanxiu.gphone.hd.student.utils.YanXiuConstant.QUESTION_TYP.*;


public class AnswerAdapter extends FragmentStatePagerAdapter implements QuestionsListener {
	private ArrayList<Fragment> mFragments;
	private ViewPager mViewPager;
//	private SubjectExercisesItemBean subjectExercisesItemBean;
	private List<PaperTestEntity> dataList = new ArrayList<>() ;
	private int answerViewTypyBean = 0;

	private QuestionsListener flip;


	private ArrayList<Integer> pageIndexList = new ArrayList<>();
	private int comeFrom;

	public ArrayList<Fragment> getmFragments() {
		return mFragments;
	}

	public void setmFragments(ArrayList<Fragment> mFragments) {
		this.mFragments = mFragments;
	}

	public AnswerAdapter(FragmentManager fm) {
		super(fm);
		mFragments = new ArrayList<>();
	}
	public void setViewPager(ViewPager viewPager){
		mViewPager = viewPager;
	}

	public int getTotalCount(){
		if(!pageIndexList.isEmpty()){
			int size = pageIndexList.size();
			return pageIndexList.get(size - 1);
		}
		return 0;
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	public int getListCount() {
		return dataList.size();
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}


	public int getIndexPage(int position){
//		if(getItem(position) instanceof ReadingQuestionsFragment){
//			return pageIndexList.get(position) + ((ReadingQuestionsFragment)getItem(position)).getPagerIndex();
//		}else{
//		}
		return pageIndexList.get(position);
	}

	public int getAnswerViewTypyBean() {
		return answerViewTypyBean;
	}

	public void setAnswerViewTypyBean(int answerViewTypyBean) {
		this.answerViewTypyBean = answerViewTypyBean;
	}

	public void setFlip(QuestionsListener flip) {
		this.flip = flip;
	}

	@Override
	public void flipNextPager(QuestionsListener flip) {
		LogInfo.log("king", "anwser adapter flip");
		if(mViewPager instanceof InterViewPager){
			LogInfo.log("king", mViewPager.getCurrentItem() + "--------" + this.getCount());
			if((this.getCount()- 1) == mViewPager.getCurrentItem()){
				((InterViewPager)mViewPager).toParentViewPager();
			}else{
				mViewPager.setCurrentItem((mViewPager.getCurrentItem() + 1));
			}
		}else{
			this.flip.flipNextPager(null);
			mViewPager.setCurrentItem((mViewPager.getCurrentItem() + 1));
//			Fragment lastFragment = getItem(mViewPager.getAdapter().getCount() - 1);
//				if(lastFragment != null && lastFragment instanceof AnswerCardFragment){
//					((AnswerCardFragment)lastFragment).refreshAnswerCard();
//				}
		}
	}


	public void addDataSources(SubjectExercisesItemBean bean){
//		subjectExercisesItemBean = bean;
		if(bean!=null && bean.getData()!= null && !bean.getData().isEmpty()){
			answerViewTypyBean = bean.getViewType();
//			isResolution = bean.getIsResolution();
//			isWrongSet = bean.isWrongSet();
			//data 中数据为数组 只是去数组中的第一个item
			//dataList.addAll(bean.getData().get(0).getPaperTest());
			for (int i=0; i<bean.getData().size(); i++) {
				dataList.addAll(bean.getData().get(i).getPaperTest());
			}
			int count = dataList.size();
			mFragments.clear();
			int pageIndex = 1;
			boolean isFirstSub = false;
			for(int i = 0; i < count; i++){
				if(dataList.get(i) != null && dataList.get(i).getQuestions() != null){
					dataList.get(i).getQuestions().setPageIndex(i);
					//int typeId = bean.getData().get(0).getPaperTest().get(i).getQuestions().getType_id();
					int typeId = dataList.get(i).getQuestions().getType_id();
//					typeId = 4;
					dataList.get(i).getQuestions().setTitleName(bean.getData().get(0).getName());
					Fragment fragment = null;
//					fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_FILL_BLANKS, dataList.get(i).getQuestions(), answerViewTypyBean, pageIndex);
//					pageIndexList.add(pageIndex++);
//					typeId = 4;
					if(typeId == QUESTION_SUBJECTIVE.type){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_SUBJECTIVE, dataList.get(i).getQuestions(), answerViewTypyBean, pageIndex);
						if(!isFirstSub){
							isFirstSub = true;
							fragment.getArguments().putBoolean("isFirstSub", isFirstSub);
						}
						pageIndexList.add(pageIndex++);
					}else if(typeId == QUESTION_SINGLE_CHOICES.type) {
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_SINGLE_CHOICES, dataList.get(i).getQuestions(), answerViewTypyBean, pageIndex);
						pageIndexList.add(pageIndex++);
					}else if(typeId == QUESTION_MULTI_CHOICES.type){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_MULTI_CHOICES, dataList.get(i).getQuestions(), answerViewTypyBean, pageIndex);
						pageIndexList.add(pageIndex++);
					}else if(typeId == QUESTION_JUDGE.type){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_JUDGE, dataList.get(i).getQuestions(), answerViewTypyBean, pageIndex);
						pageIndexList.add(pageIndex++);
					}else if(typeId == QUESTION_FILL_BLANKS.type){
						dataList.get(i).getQuestions().getAnswerBean().setSubjectId(bean.getData().get(0).getSubjectid());
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_FILL_BLANKS, dataList.get(i).getQuestions(), answerViewTypyBean, pageIndex);
						pageIndexList.add(pageIndex++);
					}else if(typeId == QUESTION_READING.type){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_READING, dataList.get(i).getQuestions(), answerViewTypyBean, pageIndex);

						if (dataList.get(i).getQuestions() != null) {
							List<QuestionEntity> childQuestion = dataList.get(i).getQuestions().getChildren();
							int childCount = childQuestion.size();
							for (int j = 0; j < childCount; j++) {
								childQuestion.get(j).setPageIndex(i);
								childQuestion.get(j).setChildPageIndex(j);
							}
						}
						if(dataList.get(i).getQuestions() != null && dataList.get(i).getQuestions().getChildren() != null){
							pageIndexList.add(pageIndex);
							pageIndex = dataList.get(i).getQuestions().getChildren().size() + pageIndex;
						}else {
							pageIndexList.add(pageIndex++);
						}
					}
					if(fragment != null){
						((QuestionsListener)fragment).flipNextPager(this);
						((QuestionsListener)fragment).setDataSources(dataList.get(i).getQuestions().getAnswerBean());
						mFragments.add(fragment);
					}
				}
			}
			//最后一个记录整个题目的总数量
			pageIndexList.add(pageIndex - 1);
//			LogInfo.log("geny-", "childIndex------" + pageIndexList.size() + "----pagerIndex-----" + 1);
//			if(answerViewTypyBean == SubjectExercisesItemBean.ANSWER_QUESTION){
//				LogInfo.log("geny", "AnswerAdapter comeFrome------" + comeFrom);
//				Fragment fragment = QuestionFragmentFactory.getInstance().createAnswerCardFragment(bean, comeFrom);
//				mFragments.add(fragment);
//			}
		}
	}
	public void addDataSourcesMore(List<PaperTestEntity> paperList){
		if(paperList!=null && paperList.size() > 0){
			//data 中数据为数组 只是去数组中的第一个item
			int count = paperList.size();
			dataList.addAll(paperList);
			for(int i = 0; i < count; i++){
				if(paperList.get(i) != null && paperList.get(i).getQuestions() != null){
					int typeId = paperList.get(i).getQuestions().getType_id();
					Fragment fragment = null;
					LogInfo.log("geny-", "typeId------" + typeId + "----pagerIndex-----" + i);
					if(typeId == QUESTION_SINGLE_CHOICES.type) {
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_SINGLE_CHOICES, paperList.get(i).getQuestions(), answerViewTypyBean, 0);
					}else if(typeId == QUESTION_MULTI_CHOICES.type){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_MULTI_CHOICES, paperList.get(i).getQuestions(), answerViewTypyBean, 0);
					}else if(typeId == QUESTION_JUDGE.type){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_JUDGE, paperList.get(i).getQuestions(), answerViewTypyBean, 0);
					}else if(typeId == QUESTION_FILL_BLANKS.type){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_FILL_BLANKS, paperList.get(i).getQuestions(), answerViewTypyBean, 0);
					}else if(typeId == QUESTION_READING.type){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_READING, paperList.get(i).getQuestions(), answerViewTypyBean, 0);
					}
					((QuestionsListener)fragment).flipNextPager(this);
					((QuestionsListener)fragment).setDataSources(
							paperList.get(i).getQuestions().getAnswerBean());
					mFragments.add(fragment);
				}
			}
//			if(!isResolution && !isWrongSet){
//				mFragments.add(new AnswerCardFragment());
//				((AnswerCardFragment)mFragments.get(count)).setDataSources(subjectExercisesItemBean);
//			}
			notifyDataSetChanged();
		}
	}

	public String getTypeName(int typeId){
		if(typeId == QUESTION_SINGLE_CHOICES.type) {
			return QUESTION_SINGLE_CHOICES.name;
		}else if(typeId == QUESTION_MULTI_CHOICES.type){
			return QUESTION_MULTI_CHOICES.name;
		}else if(typeId == QUESTION_JUDGE.type){
			return QUESTION_JUDGE.name;
		}else if(typeId == QUESTION_FILL_BLANKS.type){
			return QUESTION_FILL_BLANKS.name;
		}else if(typeId == QUESTION_READING.type){
			return QUESTION_READING.name;
		}else if(typeId == QUESTION_SUBJECTIVE.type){
			return QUESTION_SUBJECTIVE.name;
		}
		return "";
	}

	public void addDataSourcesForReadingQuestion(List<QuestionEntity> dataList){
		if(dataList != null){
			int count = dataList.size();
			List<QuestionEntity> dirtyData = new ArrayList<>();
			for(int i = 0; i < count; i++){
				if(dataList.get(i) !=null){
					dataList.get(i).setReadQuestion(true);
					int typeId = dataList.get(i).getType_id();

					dataList.get(i).setReadItemName(getTypeName(typeId));
					Fragment fragment = null;
					if(typeId == QUESTION_SINGLE_CHOICES.type) {
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_SINGLE_CHOICES, dataList.get(i), answerViewTypyBean, dataList.get(i).getChildPageIndex());
					}else if(typeId == QUESTION_MULTI_CHOICES.type){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_MULTI_CHOICES, dataList.get(i), answerViewTypyBean, dataList.get(i).getChildPageIndex());
					}else if(typeId == QUESTION_JUDGE.type){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_JUDGE, dataList.get(i), answerViewTypyBean, dataList.get(i).getChildPageIndex());
					}else if(typeId == QUESTION_FILL_BLANKS.type){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_FILL_BLANKS, dataList.get(i), answerViewTypyBean, dataList.get(i).getChildPageIndex());
					}else{
						dirtyData.add(dataList.get(i));
					}
//					else if(typeId == QUESTION_READING.type){
//						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_READING, dataList.get(i), answerViewTypyBean, dataList.get(i).getChildPageIndex());
//					}
					if(fragment != null){
						mFragments.add(fragment);
						((QuestionsListener)fragment).flipNextPager(this);
						((QuestionsListener)fragment).setDataSources(dataList.get(i).getAnswerBean());
					}
				}
			}
			dataList.removeAll(dirtyData);
		}
	}


	public void deleteFragment(int index){
		if(mFragments != null && mFragments.size() > index){
			mFragments.remove(index);
			dataList.remove(index);
		}
		notifyDataSetChanged();
	}
	@Override
	public void setDataSources(AnswerBean bean) {
		// do nothing
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {//销毁没有用的page页面======================================
		((ViewPager) arg0).removeView((View) arg2);
		super.destroyItem(arg0, arg1, arg2);
	}


	@Override
	public void initViewWithData(AnswerBean bean) {

	}

	@Override public void answerViewClick() {
		int count = mFragments.size();
		for(int i=0;i<count;i++)
		{
			Fragment fragment = mFragments.get(i);
			if(fragment instanceof QuestionsListener){
				((QuestionsListener)fragment).answerViewClick();
			}
		}
	}

	@Override
	public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
	}

	public void setCostTime(int costTime, int position) {
		if(dataList != null && dataList.get(position) != null && dataList.get(position).getQuestions() != null){
			AnswerBean bean = dataList.get(position).getQuestions().getAnswerBean();
			bean.setConsumeTime(bean.getConsumeTime() + costTime);
			LogInfo.log("geny", costTime + "---costTime-------viewPagerLastPosition----" + position);
		}
	}

	public void setComeFrom(int comeFrom) {
		this.comeFrom = comeFrom;
	}


	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
	}
	public void clearData(){
		mFragments.clear();
		mFragments=null;
		 mViewPager=null;
		dataList.clear();
		dataList=null;
		flip=null;
	}

}
