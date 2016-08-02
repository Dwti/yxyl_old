package com.yanxiu.gphone.student.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.fragment.question.ChoiceQuestionFragment;
import com.yanxiu.gphone.student.fragment.question.QuestionFragmentFactory;
import com.yanxiu.gphone.student.inter.AnswerCallback;
import com.yanxiu.gphone.student.view.question.QuestionsListener;

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

/**
 * Created by Yangjj on 2016/8/2.
 */
public class ChildAnswerAdapter extends FragmentPagerAdapter implements QuestionsListener {
    private ArrayList<Fragment> mFragments;
    private ViewPager mViewPager;
    private int answerViewTypyBean = 0;

    private AnswerCallback callback;

    public ChildAnswerAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<Fragment>();
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    public void setViewPager(ViewPager viewPager){
        mViewPager = viewPager;
    }

    public void setAnswerCallback(AnswerCallback callback){
        this.callback=callback;
    }

    public int getAnswerViewTypyBean() {
        return answerViewTypyBean;
    }

    public void setAnswerViewTypyBean(int answerViewTypyBean) {
        this.answerViewTypyBean = answerViewTypyBean;
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
                        ((ChoiceQuestionFragment)fragment).setAnswerCallback(i,callback);
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

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public void flipNextPager(QuestionsListener listener) {

    }

    @Override
    public void setDataSources(AnswerBean bean) {

    }

    @Override
    public void initViewWithData(AnswerBean bean) {

    }

    @Override
    public void answerViewClick() {

    }
}
