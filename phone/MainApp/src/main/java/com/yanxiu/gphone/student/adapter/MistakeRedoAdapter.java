package com.yanxiu.gphone.student.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.PaperTestEntity;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.fragment.DefaultLoadFragment;
import com.yanxiu.gphone.student.fragment.question.QuestionFragmentFactory;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.MyViewPager;
import com.yanxiu.gphone.student.view.question.QuestionsListener;

import java.util.ArrayList;
import java.util.List;

import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_CLASSFY;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_CLOZE_COMPLEX;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_COMPUTE;
import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_CONNECT;
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
 * Created by Canghaixiao.
 * Time : 2017/2/13 10:53.
 * Function :
  */

public class MistakeRedoAdapter extends BaseMistakRedoAdapter<PaperTestEntity> implements QuestionsListener {

    private SubjectExercisesItemBean dataSources;
    private MyViewPager vpAnswer;
    private String name = "";
    int parentIndex = -1;
    int pageIndex = 1;
    int wrongCount=0;
    boolean isFirstSub = false;
    private int answerViewTypyBean;
    private String subject_id;

    public MistakeRedoAdapter(FragmentManager fm) {
        super(fm);
    }

    public MistakeRedoAdapter(FragmentManager fm, List<PaperTestEntity> datas) {
        super(fm, datas);
    }

    @Override
    protected Fragment CreatItemFragment(PaperTestEntity paperTestEntity, int position) {
        return getFragment(paperTestEntity, position);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

    public void setViewPager(MyViewPager vpAnswer) {
        this.vpAnswer = vpAnswer;
    }

    public void setDataSourcesFirst(SubjectExercisesItemBean dataSources,int number,int old_page,int now_page){
        this.dataSources = dataSources;
        this.name = dataSources.getData().get(0).getName();
        this.wrongCount=number;
        answerViewTypyBean = dataSources.getViewType();
        answerViewTypyBean=4;
        subject_id = dataSources.getData().get(0).getSubjectid();
        ArrayList<PaperTestEntity> list=getList(number);
        this.setData(list);
//        addDataSources(dataSources,old_page,now_page);
    }

    public void addDataSources(SubjectExercisesItemBean dataSources,int old_page,int now_page) {
        ArrayList<PaperTestEntity> list= (ArrayList<PaperTestEntity>) getDatas();
        List<PaperTestEntity> data=dataSources.getData().get(0).getPaperTest();
        int k=0;
        for (int i=old_page;i<now_page;i++){
            list.add(old_page,data.get(k));
            k++;
        }
    }

    /**
     * 这样的逻辑效率极其低下，强烈建议更改产品设计
     * */
    private ArrayList<PaperTestEntity> getList(int num){
        ArrayList<PaperTestEntity> list=new ArrayList<>();
        for (int i=0;i<num;i++){
            list.add(null);
        }
        return list;
    }

    private Fragment getFragment(PaperTestEntity paperTestEntity, int i) {
        Fragment fragment = null;
        if (paperTestEntity != null && paperTestEntity.getQuestions() != null) {
            parentIndex = parentIndex + 1;
            paperTestEntity.getQuestions().setParentIndex(parentIndex);
            paperTestEntity.getQuestions().setPageIndex(i);
            int typeId = paperTestEntity.getQuestions().getType_id();
            String template = paperTestEntity.getQuestions().getTemplate();
            paperTestEntity.getQuestions().setTitleName(name);
            if (template.equals(YanXiuConstant.ANSWER_QUESTION)) {
                fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_SUBJECTIVE, paperTestEntity.getQuestions(), answerViewTypyBean, pageIndex, i+1, wrongCount);
                if (!isFirstSub) {
                    isFirstSub = true;
                    fragment.getArguments().putBoolean("isFirstSub", isFirstSub);
                }
                pageIndex++;
            } else if (template.equals(YanXiuConstant.SINGLE_CHOICES)) {
                fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_SINGLE_CHOICES, paperTestEntity.getQuestions(), answerViewTypyBean, pageIndex, i+1, wrongCount);
                pageIndex++;
            } else if (template.equals(YanXiuConstant.MULTI_CHOICES)) {
                fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_MULTI_CHOICES, paperTestEntity.getQuestions(), answerViewTypyBean, pageIndex, i+1, wrongCount);
                pageIndex++;
            } else if (template.equals(YanXiuConstant.JUDGE_QUESTION)) {
                fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_JUDGE, paperTestEntity.getQuestions(), answerViewTypyBean, pageIndex, i+1, wrongCount);
                pageIndex++;
            } else if (template.equals(YanXiuConstant.FILL_BLANK)) {
                paperTestEntity.getQuestions().getAnswerBean().setSubjectId(subject_id);
                fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_FILL_BLANKS, paperTestEntity.getQuestions(), answerViewTypyBean, pageIndex, i+1, wrongCount);
                pageIndex++;
            } else if (template.equals(YanXiuConstant.CLASSIFY_QUESTION)) {
                fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_CLASSFY, paperTestEntity.getQuestions(), answerViewTypyBean, pageIndex, i+1, wrongCount);
                pageIndex++;
            } else if (template.equals(YanXiuConstant.MULTI_QUESTION) && typeId == QUESTION_READING.type && 1 == 2) {
                fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_READING, paperTestEntity.getQuestions(), answerViewTypyBean, pageIndex, i+1, wrongCount);
                if (paperTestEntity.getQuestions() != null) {
                    List<PaperTestEntity> childQuestion = paperTestEntity.getQuestions().getChildren();
                    if (childQuestion != null) {
                        int childCount = childQuestion.size();
                        for (int j = 0; j < childCount; j++) {
                            childQuestion.get(j).getQuestions().setParentIndex(parentIndex);
                            childQuestion.get(j).getQuestions().setPageIndex(i);
                            childQuestion.get(j).getQuestions().setChildPageIndex(j);
                            childQuestion.get(j).getQuestions().setChildPageNumber(pageIndex);
                        }
                    }
                }
                if (paperTestEntity.getQuestions() != null && paperTestEntity.getQuestions().getChildren() != null) {
                    pageIndex = paperTestEntity.getQuestions().getChildren().size() + pageIndex;
                } else {
                    pageIndex++;
                }
            } else if (template.equals(YanXiuConstant.LISTEN_QUESTION)) {
                fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_LISTEN_COMPLEX, paperTestEntity.getQuestions(), answerViewTypyBean, pageIndex, i+1, wrongCount);
                if (paperTestEntity.getQuestions() != null) {
                    List<PaperTestEntity> childQuestion = paperTestEntity.getQuestions().getChildren();
                    if (childQuestion != null) {
                        int childCount = childQuestion.size();
                        for (int j = 0; j < childCount; j++) {
                            childQuestion.get(j).getQuestions().setParentIndex(parentIndex);
                            childQuestion.get(j).getQuestions().setPageIndex(i);
                            childQuestion.get(j).getQuestions().setChildPageIndex(j);
                            childQuestion.get(j).getQuestions().setChildPageNumber(pageIndex);
                        }
                    }
                }
                if (paperTestEntity.getQuestions() != null && paperTestEntity.getQuestions().getChildren() != null) {
                    pageIndex = paperTestEntity.getQuestions().getChildren().size() + pageIndex;
                } else {
                    pageIndex++;
                }
            } else if (template.equals(YanXiuConstant.MULTI_QUESTION) && (typeId == QUESTION_READ_COMPLEX.type || typeId == QUESTION_READING.type)) {
                fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_READ_COMPLEX, paperTestEntity.getQuestions(), answerViewTypyBean, pageIndex, i+1, wrongCount);
                if (paperTestEntity.getQuestions() != null) {
                    List<PaperTestEntity> childQuestion = paperTestEntity.getQuestions().getChildren();
                    if (childQuestion != null) {
                        int childCount = childQuestion.size();
                        for (int j = 0; j < childCount; j++) {
                            childQuestion.get(j).getQuestions().setParentIndex(parentIndex);
                            childQuestion.get(j).getQuestions().setPageIndex(i);
                            childQuestion.get(j).getQuestions().setChildPageIndex(j);
                            childQuestion.get(j).getQuestions().setChildPageNumber(pageIndex);
                        }
                    }
                }
                if (paperTestEntity.getQuestions() != null && paperTestEntity.getQuestions().getChildren() != null) {
                    pageIndex = paperTestEntity.getQuestions().getChildren().size() + pageIndex;
                } else {
                    pageIndex++;
                }
            } else if (template.equals(YanXiuConstant.MULTI_QUESTION) && (typeId == QUESTION_SOLVE_COMPLEX.type || typeId == QUESTION_COMPUTE.type)) {
                fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_SOLVE_COMPLEX, paperTestEntity.getQuestions(), answerViewTypyBean, pageIndex, i+1, wrongCount);
                if (paperTestEntity.getQuestions() != null) {
                    List<PaperTestEntity> childQuestion = paperTestEntity.getQuestions().getChildren();
                    if (childQuestion != null) {
                        int childCount = childQuestion.size();
                        for (int j = 0; j < childCount; j++) {
                            childQuestion.get(j).getQuestions().setParentIndex(parentIndex);
                            childQuestion.get(j).getQuestions().setPageIndex(i);
                            childQuestion.get(j).getQuestions().setChildPageIndex(j);
                            childQuestion.get(j).getQuestions().setChildPageNumber(pageIndex);
                        }
                    }
                }
                pageIndex++;
            } else if (template.equals(YanXiuConstant.CLOZE_QUESTION)) {
                fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_CLOZE_COMPLEX, paperTestEntity.getQuestions(), answerViewTypyBean, pageIndex, i+1, wrongCount);
                if (paperTestEntity.getQuestions() != null) {
                    List<PaperTestEntity> childQuestion = paperTestEntity.getQuestions().getChildren();
                    if (childQuestion != null) {
                        int childCount = childQuestion.size();
                        for (int j = 0; j < childCount; j++) {
                            childQuestion.get(j).getQuestions().setParentIndex(parentIndex);
                            childQuestion.get(j).getQuestions().setPageIndex(i);
                            childQuestion.get(j).getQuestions().setChildPageIndex(j);
                            childQuestion.get(j).getQuestions().setChildPageNumber(pageIndex);
                        }
                    }
                }
                if (paperTestEntity.getQuestions() != null && paperTestEntity.getQuestions().getChildren() != null) {
                    pageIndex = paperTestEntity.getQuestions().getChildren().size() + pageIndex;
                } else {
                    pageIndex++;
                }
            } else if (template.equals(YanXiuConstant.CONNECT_QUESTION)) {
                fragment = QuestionFragmentFactory.getInstance().createQuestionFragment(QUESTION_CONNECT, paperTestEntity.getQuestions(), answerViewTypyBean, pageIndex, i+1, wrongCount);
                if (paperTestEntity.getQuestions() != null) {
                    List<PaperTestEntity> childQuestion = paperTestEntity.getQuestions().getChildren();
                    if (childQuestion != null) {
                        int childCount = childQuestion.size();
                        for (int j = 0; j < childCount; j++) {
                            childQuestion.get(j).getQuestions().setParentIndex(parentIndex);
                            childQuestion.get(j).getQuestions().setPageIndex(i);
                            childQuestion.get(j).getQuestions().setChildPageIndex(j);
                            childQuestion.get(j).getQuestions().setChildPageNumber(pageIndex);
                        }
                    }
                }
                pageIndex++;
            }
            ((QuestionsListener) fragment).flipNextPager(this);
            ((QuestionsListener) fragment).setDataSources(paperTestEntity.getQuestions().getAnswerBean());
        }else {
            fragment=new DefaultLoadFragment();
            Bundle args=new Bundle();
            args.putInt("answerViewTypyBean", answerViewTypyBean);
            args.putInt("pageIndex", pageIndex);
            args.putSerializable("questions", new QuestionEntity());
            args.putInt("wrong",i+1);
            args.putInt("wrongCount",wrongCount);
            fragment.setArguments(args);
            pageIndex++;
        }
        return fragment;
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
