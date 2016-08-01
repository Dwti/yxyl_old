package com.yanxiu.gphone.student.fragment.question;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.inter.AnswerCallback;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;
import com.yanxiu.gphone.student.view.question.choicequestion.ChoiceQuestions;

/**
 * Created by Administrator on 2015/7/7.
 */
public class ChoiceQuestionFragment extends BaseQuestionFragment implements QuestionsListener, PageIndex {

    private View rootView;
    private ChoiceQuestions choiceQuestions;
    private static QuestionsListener listener;
    //本地的保存数据bean
    private AnswerBean bean;

    private Button addBtn;

    private YXiuAnserTextView yXiuAnserTextView;


    public int typeId;

    private Fragment resolutionFragment;
    private static AnswerCallback callback;
    private int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.typeId = (getArguments() != null) ? getArguments().getInt("typeId") : null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_choices_question,null);
        choiceQuestions = (ChoiceQuestions) rootView.findViewById(R.id.cq_item);
        choiceQuestions.flipNextPager(listener);
        if (callback!=null) {
            choiceQuestions.setAnswerCallback(position, callback);
        }
        yXiuAnserTextView = (YXiuAnserTextView) rootView.findViewById(R.id.yxiu_tv);
        FragmentTransaction ft = ChoiceQuestionFragment.this.getChildFragmentManager().beginTransaction();
        ft.replace(R.id.content_problem_analysis, new Fragment()).commitAllowingStateLoss();
        selectTypeView();
        if(questionsEntity != null && questionsEntity.getStem() != null){
            choiceQuestions.setAllDataSources(questionsEntity);
            yXiuAnserTextView.setTextHtml(questionsEntity.getStem());
//            Log.d("asd",questionsEntity.getStem().toString());
        }
        choiceQuestions.setChoicesType(typeId);

        LogInfo.log("geny", "---onCreateView-------pageIndex----" + pageIndex);
        return rootView;
    }

    public void setAnswerCallback(int position,AnswerCallback callback){
        this.callback=callback;
        this.position=position;
        if (choiceQuestions!=null) {
            choiceQuestions.setAnswerCallback(position, callback);
        }
    }


    private void selectTypeView(){
        switch (answerViewTypyBean){
            case SubjectExercisesItemBean.RESOLUTION:
                choiceQuestions.setIsResolution(true);
                choiceQuestions.setIsClick(false);
                addAnalysisFragment();
                break;
            case SubjectExercisesItemBean.WRONG_SET:
                choiceQuestions.setIsWrongSet(true);
                choiceQuestions.setIsClick(false);
                addBtn = (Button) rootView.findViewById(R.id.add_problem_analysis);
                addBtn.setVisibility(View.VISIBLE);
                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addBtn.setVisibility(View.GONE);
                        choiceQuestions.initViewWithData(bean);
                        addAnalysisFragment();
                    }
                });
                break;
        }
    }

    private void addAnalysisFragment(){
        Bundle args = new Bundle();
        args.putSerializable("questions", questionsEntity);
        resolutionFragment = Fragment.instantiate(ChoiceQuestionFragment.this.getActivity(), ProblemAnalysisFragment.class.getName(), args);
        FragmentTransaction ft = ChoiceQuestionFragment.this.getChildFragmentManager().beginTransaction();
       //ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
//         标准动画
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.replace(R.id.content_problem_analysis, resolutionFragment).commitAllowingStateLoss();
    }


    @Override
    public void onStart() {
        super.onStart();
        LogInfo.log("geny", "---onStart-------pageIndex----" + pageIndex);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogInfo.log("geny", "---onResume-------pageIndex----" + pageIndex);
        if(bean == null){
            bean = questionsEntity.getAnswerBean();
        }
        choiceQuestions.setDataSources(bean);
    }


    @Override
    public void onPause() {
        super.onPause();
        LogInfo.log("geny", "---onPause-------pageIndex----" + pageIndex);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogInfo.log("geny", "---onDestroy-------pageIndex----" + pageIndex);
        choiceQuestions=null;
//        listener=null;
        bean=null;
        addBtn=null;
        yXiuAnserTextView=null;

        resolutionFragment=null;
        System.gc();
    }

    @Override
    public void flipNextPager(QuestionsListener listener) {
        this.listener = listener;
    }

    @Override
    public void setDataSources(AnswerBean bean) {
        this.bean = bean;
    }

    @Override
    public void initViewWithData(AnswerBean bean) {
        this.bean = bean;
    }

    @Override public void answerViewClick() {

    }

    @Override
    public int getPageIndex() {
        return pageIndex;
    }

    @Override
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
}

