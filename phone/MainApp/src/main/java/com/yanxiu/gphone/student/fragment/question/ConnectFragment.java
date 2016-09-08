package com.yanxiu.gphone.student.fragment.question;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.view.ConnectLinesLinearLayout;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;

/**
 * Created by Administrator on 2016/9/7.
 */
public class ConnectFragment extends BaseQuestionFragment implements QuestionsListener, PageIndex{

    private QuestionsListener listener;
    public int typeId;
    //本地的保存数据bean
    private AnswerBean bean;
    private boolean isVisibleToUser;
    private ConnectLinesLinearLayout connect_lineslinearlayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.typeId = (getArguments() != null) ? getArguments().getInt("typeId") : null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView =inflater.inflate(R.layout.fragment_connect,null);
        initview(rootView);
        selectTypeView();
        return rootView;
    }

    private void initview(View rootView) {
        YXiuAnserTextView connect_question= (YXiuAnserTextView) rootView.findViewById(R.id.connect_question);
        connect_lineslinearlayout= (ConnectLinesLinearLayout) rootView.findViewById(R.id.connect_lineslinearlayout);
        if (questionsEntity!=null) {
            connect_question.setTextHtml(questionsEntity.getStem());
            connect_lineslinearlayout.setAnswers(questionsEntity.getAnswer());
            if (bean!=null) {
                connect_lineslinearlayout.setAnswerBean(bean);
            }
            connect_lineslinearlayout.setDatas(questionsEntity.getContent().getChoices());
        }
    }

    private void selectTypeView(){

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser=isVisibleToUser;
        if (!isVisibleToUser && connect_lineslinearlayout != null) {
            if (bean != null) {
                connect_lineslinearlayout.saveAnswers();
            }
        }
        if (isVisibleToUser) {
            if (!ischild){
                ((QuestionsListener) getActivity()).flipNextPager(null);
            }
            if (connect_lineslinearlayout!=null) {
                connect_lineslinearlayout.setDefault();
            }
        }
    }

    @Override
    public int getPageIndex() {
        return this.pageIndex;
    }

    @Override
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    @Override
    public void flipNextPager(QuestionsListener listener) {
        this.listener = listener;
    }

    @Override
    public void setDataSources(AnswerBean bean) {
        this.bean = bean;
        if (connect_lineslinearlayout!=null){
            connect_lineslinearlayout.setAnswerBean(bean);
        }
    }

    @Override
    public void initViewWithData(AnswerBean bean) {
        this.bean = bean;
        if (connect_lineslinearlayout!=null){
            connect_lineslinearlayout.setAnswerBean(bean);
        }
    }

    @Override
    public void answerViewClick() {
        if (connect_lineslinearlayout != null) {
            if (bean != null) {
                connect_lineslinearlayout.saveAnswers();
            }
        }
    }
}
