package com.yanxiu.gphone.student.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.PaperTestEntity;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.fragment.question.BaseQuestionFragment;
import com.yanxiu.gphone.student.fragment.question.ChoiceQuestionFragment;
import com.yanxiu.gphone.student.fragment.question.FillBlanksFragment;
import com.yanxiu.gphone.student.fragment.question.JudgeQuestionFragment;
import com.yanxiu.gphone.student.fragment.question.QuestionFragmentFactory;
import com.yanxiu.gphone.student.fragment.question.SubjectiveQuestionFragment;
import com.yanxiu.gphone.student.inter.AnswerCallback;
import com.yanxiu.gphone.student.utils.QuestionUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.utils.YanxiuLogApiTool;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.readquestion.InterViewPager;

import java.util.ArrayList;
import java.util.List;

import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_CLOZE_COMPLEX;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_FILL_BLANKS;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_JUDGE;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_LISTEN_COMPLEX;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_MULTI_CHOICES;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_READING;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_READ_COMPLEX;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_SINGLE_CHOICES;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_SOLVE_COMPLEX;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_SUBJECTIVE;

public class AnswerAdapter extends FragmentPagerAdapter implements QuestionsListener {
	private ArrayList<Fragment> mFragments;
	private ViewPager mViewPager;
//	private SubjectExercisesItemBean subjectExercisesItemBean;
	private List<PaperTestEntity> dataList = new ArrayList<PaperTestEntity>() ;
	private int answerViewTypyBean = 0;

	private QuestionsListener flip;
	private AnswerCallback callback;


	private ArrayList<Integer> pageIndexList = new ArrayList<Integer>();
	private int comeFrom;

	public ArrayList<Fragment> getmFragments() {
		return mFragments;
	}

	public void setmFragments(ArrayList<Fragment> mFragments) {
		this.mFragments = mFragments;
	}

	public AnswerAdapter(FragmentManager fm) {
		super(fm);
		mFragments = new ArrayList<Fragment>();
	}
	public void setViewPager(ViewPager viewPager){
		mViewPager = viewPager;
	}

	public int getViewPagerCurrentItem(){
		return mViewPager.getCurrentItem();
	}

	public void setPagerLift(){
		mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1);
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

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
//		return getItem(position);
		return super.instantiateItem(container, position);
	}

	@Override
	public long getItemId(int position) {
		int hashCode = mFragments.get(position).hashCode();
		return hashCode;
	}

	public int getListCount() {
		return dataList.size();
	}
	@Override
	public Fragment getItem(int position) {
		BaseQuestionFragment fragment= (BaseQuestionFragment) mFragments.get(position);
		fragment.setRefresh();
		return fragment;
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
		SubjectExercisesItemBean beanTmp = Util.getSubjectExercisesItemBean();
//		Util.testDataStr="";
//		bean= Util.getSubjectExercisesItemBean();

		if(bean!=null && bean.getData()!= null && !bean.getData().isEmpty()){
			QuestionUtils.removeQuestions(bean);
			answerViewTypyBean = bean.getViewType();
//			isResolution = bean.getIsResolution();
//			isWrongSet = bean.isWrongSet();
			//data 中数据为数组 只是去数组中的第一个item
			dataList.addAll(bean.getData().get(0).getPaperTest());
			//dataList.addAll(beanTmp.getData().get(0).getPaperTest());
			/*for (int i=0; i<bean.getData().size(); i++) {
				dataList.addAll(bean.getData().get(i).getPaperTest());
			}*/
			int count = dataList.size();
			mFragments.clear();
			int pageIndex = 1;
			int parentIndex = -1;
			boolean isFirstSub = false;
			//dataList.get(0).getQuestions().setType_id(16);
			for(int i = 0; i < count; i++){
				if(dataList.get(i) != null && dataList.get(i).getQuestions() != null){
					parentIndex = parentIndex + 1;
					dataList.get(i).getQuestions().setParentIndex(parentIndex);
					dataList.get(i).getQuestions().setPageIndex(i);
					int typeId = dataList.get(i).getQuestions().getType_id();
					String template = dataList.get(i).getQuestions().getTemplate();
					dataList.get(i).getQuestions().setTitleName(bean.getData().get(0).getName());
					Fragment fragment = null;
					if(template.equals(YanXiuConstant.ANSWER_QUESTION)){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_SUBJECTIVE, dataList.get(i).getQuestions(), answerViewTypyBean, pageIndex);
						if(!isFirstSub){
							isFirstSub = true;
							fragment.getArguments().putBoolean("isFirstSub", isFirstSub);
						}
						pageIndexList.add(pageIndex++);
					}else if(template.equals(YanXiuConstant.SINGLE_CHOICES)) {
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_SINGLE_CHOICES, dataList.get(i).getQuestions(), answerViewTypyBean, pageIndex);
						pageIndexList.add(pageIndex++);
					}else if(template.equals(YanXiuConstant.MULTI_CHOICES)){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_MULTI_CHOICES, dataList.get(i).getQuestions(), answerViewTypyBean, pageIndex);
						pageIndexList.add(pageIndex++);
					}else if(template.equals(YanXiuConstant.JUDGE_QUESTION)){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_JUDGE, dataList.get(i).getQuestions(), answerViewTypyBean, pageIndex);
						pageIndexList.add(pageIndex++);
					}else if(template.equals(YanXiuConstant.FILL_BLANK)){
						dataList.get(i).getQuestions().getAnswerBean().setSubjectId(bean.getData().get(0).getSubjectid());
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_FILL_BLANKS, dataList.get(i).getQuestions(), answerViewTypyBean, pageIndex);
						pageIndexList.add(pageIndex++);
					}else if(template.equals(YanXiuConstant.MULTI_QUESTION) && typeId == QUESTION_READING.type && 1==2){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_READING, dataList.get(i).getQuestions(), answerViewTypyBean, pageIndex);

						if (dataList.get(i).getQuestions() != null) {
							List<PaperTestEntity> childQuestion = dataList.get(i).getQuestions().getChildren();
							if (childQuestion != null ) {
								int childCount = childQuestion.size();
								for (int j = 0; j < childCount; j++) {
									childQuestion.get(j).getQuestions().setParentIndex(parentIndex);
									childQuestion.get(j).getQuestions().setPageIndex(i);
									childQuestion.get(j).getQuestions().setChildPageIndex(j);
								}
							}
						}
						if(dataList.get(i).getQuestions() != null && dataList.get(i).getQuestions().getChildren() != null){
							pageIndexList.add(pageIndex);
							pageIndex = dataList.get(i).getQuestions().getChildren().size() + pageIndex;
						}else {
							pageIndexList.add(pageIndex++);
						}
					}else if(template.equals(YanXiuConstant.LISTEN_QUESTION)) {
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_LISTEN_COMPLEX, dataList.get(i).getQuestions(), answerViewTypyBean, pageIndex);

						if (dataList.get(i).getQuestions() != null) {
							List<PaperTestEntity> childQuestion = dataList.get(i).getQuestions().getChildren();
							if (childQuestion != null) {
								int childCount = childQuestion.size();
								for (int j = 0; j < childCount; j++) {
									childQuestion.get(j).getQuestions().setParentIndex(parentIndex);
									childQuestion.get(j).getQuestions().setPageIndex(i);
									childQuestion.get(j).getQuestions().setChildPageIndex(j);
								}
							}
						}
						if (dataList.get(i).getQuestions() != null && dataList.get(i).getQuestions().getChildren() != null) {
							pageIndexList.add(pageIndex);
							pageIndex = dataList.get(i).getQuestions().getChildren().size() + pageIndex;
						} else {
							pageIndexList.add(pageIndex++);
						}
					}else if(template.equals(YanXiuConstant.MULTI_QUESTION) && (typeId == QUESTION_READ_COMPLEX.type || typeId == QUESTION_READING.type)){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_READ_COMPLEX, dataList.get(i).getQuestions(), answerViewTypyBean, pageIndex);

						if (dataList.get(i).getQuestions() != null) {
							List<PaperTestEntity> childQuestion = dataList.get(i).getQuestions().getChildren();
							if (childQuestion != null ) {
								int childCount = childQuestion.size();
								for (int j = 0; j < childCount; j++) {
									childQuestion.get(j).getQuestions().setParentIndex(parentIndex);
									childQuestion.get(j).getQuestions().setPageIndex(i);
									childQuestion.get(j).getQuestions().setChildPageIndex(j);
								}
							}
						}
						if(dataList.get(i).getQuestions() != null && dataList.get(i).getQuestions().getChildren() != null){
							pageIndexList.add(pageIndex);
							pageIndex = dataList.get(i).getQuestions().getChildren().size() + pageIndex;
						}else {
							pageIndexList.add(pageIndex++);
						}
					}else if(template.equals(YanXiuConstant.MULTI_QUESTION) && typeId == QUESTION_SOLVE_COMPLEX.type){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_SOLVE_COMPLEX, dataList.get(i).getQuestions(), answerViewTypyBean, pageIndex);

						if (dataList.get(i).getQuestions() != null) {
							List<PaperTestEntity> childQuestion = dataList.get(i).getQuestions().getChildren();
							if (childQuestion != null ) {
								int childCount = childQuestion.size();
								for (int j = 0; j < childCount; j++) {
									childQuestion.get(j).getQuestions().setParentIndex(parentIndex);
									childQuestion.get(j).getQuestions().setPageIndex(i);
									childQuestion.get(j).getQuestions().setChildPageIndex(j);
								}
							}
						}
						pageIndexList.add(pageIndex++);
					}else if (template.equals(YanXiuConstant.CLOZE_QUESTION)){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_CLOZE_COMPLEX, dataList.get(i).getQuestions(), answerViewTypyBean, pageIndex);

						if (dataList.get(i).getQuestions() != null) {
							List<PaperTestEntity> childQuestion = dataList.get(i).getQuestions().getChildren();
							if (childQuestion != null ) {
								int childCount = childQuestion.size();
								for (int j = 0; j < childCount; j++) {
									childQuestion.get(j).getQuestions().setParentIndex(parentIndex);
									childQuestion.get(j).getQuestions().setPageIndex(i);
									childQuestion.get(j).getQuestions().setChildPageIndex(j);
								}
							}
						}
						if(dataList.get(i).getQuestions() != null && dataList.get(i).getQuestions().getChildren() != null){
							pageIndexList.add(pageIndex);
							pageIndex = dataList.get(i).getQuestions().getChildren().size() + pageIndex;
						}else {
							pageIndexList.add(pageIndex++);
						}
//						pageIndexList.add(pageIndex++);
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
//			mFragments.clear();
			for(int i = 0; i < count; i++){
				if(paperList.get(i) != null && paperList.get(i).getQuestions() != null){
					int typeId = paperList.get(i).getQuestions().getType_id();
					String template = dataList.get(i).getQuestions().getTemplate();
					Fragment fragment = null;
					LogInfo.log("geny-", "typeId------" + typeId + "----pagerIndex-----" + i);
					if(template.equals(YanXiuConstant.SINGLE_CHOICES)) {
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_SINGLE_CHOICES, paperList.get(i).getQuestions(), answerViewTypyBean, 0);
					}else if(template.equals(YanXiuConstant.MULTI_CHOICES)){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_MULTI_CHOICES, paperList.get(i).getQuestions(), answerViewTypyBean, 0);
					}else if(template .equals(YanXiuConstant.JUDGE_QUESTION)){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_JUDGE, paperList.get(i).getQuestions(), answerViewTypyBean, 0);
					}else if(template.equals(YanXiuConstant.FILL_BLANK)){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_FILL_BLANKS, paperList.get(i).getQuestions(), answerViewTypyBean, 0);
					}else if(template.equals(YanXiuConstant.MULTI_QUESTION) && typeId == QUESTION_READING.type && 1==2){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_READING, paperList.get(i).getQuestions(), answerViewTypyBean, 0);
					}else if(template.equals(YanXiuConstant.LISTEN_QUESTION)){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_LISTEN_COMPLEX, paperList.get(i).getQuestions(), answerViewTypyBean, 0);
					}else if(template.equals(YanXiuConstant.MULTI_QUESTION) && (typeId == QUESTION_READ_COMPLEX.type || typeId == QUESTION_READING.type)){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_READ_COMPLEX, paperList.get(i).getQuestions(), answerViewTypyBean, 0);
					}else if(template.equals(YanXiuConstant.MULTI_QUESTION) && typeId == QUESTION_SOLVE_COMPLEX.type){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_SOLVE_COMPLEX, paperList.get(i).getQuestions(), answerViewTypyBean, 0);
					}else if (template.equals(YanXiuConstant.CLOZE_QUESTION)){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_CLOZE_COMPLEX, paperList.get(i).getQuestions(), answerViewTypyBean, 0);
					}
					if (fragment!=null) {
						((QuestionsListener) fragment).flipNextPager(this);
						((QuestionsListener) fragment).setDataSources(
								paperList.get(i).getQuestions().getAnswerBean());
						mFragments.add(fragment);
					}
				}
			}
//			if(!isResolution && !isWrongSet){
//				mFragments.add(new AnswerCardFragment());
//				((AnswerCardFragment)mFragments.get(count)).setDataSources(subjectExercisesItemBean);
//			}
			this.notifyDataSetChanged();
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
		}else if(typeId == QUESTION_CLOZE_COMPLEX.type){
			return QUESTION_CLOZE_COMPLEX.name;
		}else if(typeId == QUESTION_LISTEN_COMPLEX.type) {
			return QUESTION_LISTEN_COMPLEX.name;
		}else if(typeId == QUESTION_READ_COMPLEX.type){
			return QUESTION_READ_COMPLEX.name;
		}else if(typeId == QUESTION_SOLVE_COMPLEX.type) {
			return QUESTION_SOLVE_COMPLEX.name;
		}
		return "";
	}

	public void setAnswerCallback(AnswerCallback callback){
		this.callback=callback;
	}

	public void addDataSourcesForReadingQuestion(List<PaperTestEntity> list){
		if(list != null){
			int count = list.size();
			List<QuestionEntity> dirtyData = new ArrayList<>();
			for(int i = 0; i < count; i++){
				if(list.get(i) !=null){
					list.get(i).getQuestions().setReadQuestion(true);
					int typeId = list.get(i).getQuestions().getType_id();

					list.get(i).getQuestions().setReadItemName(getTypeName(typeId));
					String template = list.get(i).getQuestions().getTemplate();

					Fragment fragment = null;
					if(template.equals(YanXiuConstant.SINGLE_CHOICES)) {
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_SINGLE_CHOICES, list.get(i).getQuestions(), answerViewTypyBean, list.get(i).getQuestions().getChildPageIndex());
						((ChoiceQuestionFragment)fragment).setAnswerCallback(i,callback);
						((ChoiceQuestionFragment)fragment).setIsChild(true);
					}else if(template.equals(YanXiuConstant.MULTI_CHOICES)){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_MULTI_CHOICES, list.get(i).getQuestions(), answerViewTypyBean, list.get(i).getQuestions().getChildPageIndex());
						((ChoiceQuestionFragment)fragment).setIsChild(true);
					}else if(template.equals(YanXiuConstant.JUDGE_QUESTION)){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_JUDGE, list.get(i).getQuestions(), answerViewTypyBean, list.get(i).getQuestions().getChildPageIndex());
						((JudgeQuestionFragment)fragment).setIsChild(true);
					}else if(template.equals(YanXiuConstant.FILL_BLANK)){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_FILL_BLANKS, list.get(i).getQuestions(), answerViewTypyBean, list.get(i).getQuestions().getChildPageIndex());
						((FillBlanksFragment)fragment).setIsChild(true);
					}else if(template.equals(YanXiuConstant.ANSWER_QUESTION)){
						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_SUBJECTIVE, list.get(i).getQuestions(), answerViewTypyBean, list.get(i).getQuestions().getChildPageIndex());
						((SubjectiveQuestionFragment)fragment).setIsChild(true);
					}else{
						dirtyData.add(list.get(i).getQuestions());
					}
//					else if(typeId == QUESTION_READING.type){
//						fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_READING, list.get(i), answerViewTypyBean, list.get(i).getChildPageIndex());
//					}
					if(fragment != null){
						mFragments.add(fragment);
						((QuestionsListener)fragment).flipNextPager(this);
						((QuestionsListener)fragment).setDataSources(list.get(i).getQuestions().getAnswerBean());
					}
				}
			}
			list.removeAll(dirtyData);
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
	public void initViewWithData(AnswerBean bean) {

	}

	@Override
	public void answerViewClick() {
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

	/**
	 * 设置当前题目所用的时间，单位秒，dhildPostion为子题的位置，如果不是子题，childPosition传-1
	 * @param costTime
	 * @param position
	 * @param childPosition
     */
	public void setCostTime(int costTime, int position, int childPosition) {
		if(dataList != null && !dataList.isEmpty() && dataList.get(position) != null && dataList.get(position).getQuestions() != null){
			List<PaperTestEntity> children = dataList.get(position).getQuestions().getChildren();
			AnswerBean bean;
			if(children!=null && !children.isEmpty()){
				bean = children.get(childPosition).getQuestions().getAnswerBean();
			}else{
				bean = dataList.get(position).getQuestions().getAnswerBean();
			}
			bean.setConsumeTime(bean.getConsumeTime() + costTime);
			LogInfo.log("geny", costTime + "---costTime-------viewPagerLastPosition----" + position);
		}
	}

	public void setComeFrom(int comeFrom) {
		this.comeFrom = comeFrom;
	}

}
