package com.yanxiu.gphone.student.fragment.question;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.view.question.QuestionsListener;

/**
 * Created by Administrator on 2016/7/28.
 */
public class GestaltFillBlanksQuestionFragment extends BaseQuestionFragment implements QuestionsListener, PageIndex{

    private AnswerBean bean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_gestaltfillblanks,null);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int getPageIndex() {
        return pageIndex;
    }

    @Override
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    @Override
    public void flipNextPager(QuestionsListener listener) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if(bean == null){
            bean = questionsEntity.getAnswerBean();
        }
        setDataSources(bean);
        LogInfo.log("geny", "onResume");
        //        LogInfo.log("geny", paperTestEntity.getQuestions().getStem());
    }

    @Override
    public void setDataSources(AnswerBean bean) {
        this.bean = bean;
//        if(fillBlanksFramelayout!=null){
//            fillBlanksFramelayout.setDataSources(bean);
//        }
    }

    @Override
    public void initViewWithData(AnswerBean bean) {
        this.bean = bean;
    }

    @Override
    public void answerViewClick() {
//        if(fillBlanksFramelayout!=null){
//            fillBlanksFramelayout.hideSoftInput();
//            if(bean!=null){
//                LogInfo.log("king","answerViewClick saveAnswers");
//                fillBlanksFramelayout.saveAnswers();
//            }
//        }
    }
}
