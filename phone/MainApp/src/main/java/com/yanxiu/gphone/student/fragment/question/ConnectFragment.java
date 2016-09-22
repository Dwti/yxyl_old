package com.yanxiu.gphone.student.fragment.question;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.view.ConnectLinesLinearLayout;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/7.
 */
public class ConnectFragment extends BaseQuestionFragment implements PageIndex{

    private QuestionsListener listener;
    public int typeId;
    //本地的保存数据bean
    private AnswerBean bean;
    private boolean isVisibleToUser;
    private ConnectLinesLinearLayout connect_lineslinearlayout;
    private Fragment resolutionFragment;
    private Button addBtn;
    private FrameLayout ll_answer;

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

    @Override
    public void onResume() {
        super.onResume();
        if (ischild){
            if (questionsEntity!=null&&TextUtils.isEmpty(questionsEntity.getStem())){
                ll_answer.setVisibility(View.GONE);
            }else {
                ll_answer.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initview(View rootView) {
        ll_answer= (FrameLayout) rootView.findViewById(R.id.ll_answer);
        YXiuAnserTextView connect_question= (YXiuAnserTextView) rootView.findViewById(R.id.connect_question);
        connect_lineslinearlayout= (ConnectLinesLinearLayout) rootView.findViewById(R.id.connect_lineslinearlayout);
        if (questionsEntity!=null) {
            connect_question.setTextHtml(questionsEntity.getStem());
            connect_lineslinearlayout.setAnswers(questionsEntity.getAnswer());
            if (bean!=null) {
                connect_lineslinearlayout.setAnswerBean(bean);
            }
            connect_lineslinearlayout.setDatas(questionsEntity.getContent().getChoices());
//            connect_lineslinearlayout.setDefault();
        }
    }

    private void selectTypeView(){
        switch (answerViewTypyBean){
            case SubjectExercisesItemBean.RESOLUTION:
                connect_lineslinearlayout.setIsResolution(true);
                connect_lineslinearlayout.setIsClick(false);
                addAnalysisFragment();
                break;
            case SubjectExercisesItemBean.WRONG_SET:
                connect_lineslinearlayout.setIsWrongSet(true);
                connect_lineslinearlayout.setIsClick(false);
                addBtn = (Button) rootView.findViewById(R.id.add_problem_analysis);
                addBtn.setVisibility(View.VISIBLE);
                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addBtn.setVisibility(View.GONE);
//                        try {
//                            ArrayList<String> answer_list=bean.getFillAnswers();
//                            answer_list.clear();
//                            String jsonanswer=questionsEntity.getPad().getJsonAnswer();
//                            JSONArray array=new JSONArray(jsonanswer);
//                            for (int i=0;i<array.length();i++){
//                                String answer=array.getString(i);
//                                answer_list.add(answer);
//                            }
//                            if (answer_list.size()==1){
//                                bean.setSelectType(answer_list.get(0));
//                            }
//                        }catch (Exception e){
//
//                        }
//                        choiceQuestions.initViewWithData(bean);
                        addAnalysisFragment();
                    }
                });
                break;
        }
    }

    private void addAnalysisFragment(){
        Bundle args = new Bundle();
        args.putSerializable("questions", questionsEntity);
        resolutionFragment = Fragment.instantiate(ConnectFragment.this.getActivity(), ProblemAnalysisFragment.class.getName(), args);
        FragmentTransaction ft = ConnectFragment.this.getChildFragmentManager().beginTransaction();
//         标准动画
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);

        ft.replace(R.id.content_problem_analysis, resolutionFragment).commitAllowingStateLoss();
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
        super.answerViewClick();
        if (connect_lineslinearlayout != null) {
            if (bean != null) {
                connect_lineslinearlayout.saveAnswers();
            }
        }
    }

    @Override
    public void saveAnwser() {
        super.saveAnwser();
        answerViewClick();
    }
}
