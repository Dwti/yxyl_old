package com.yanxiu.gphone.hd.student.fragment.question;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.bean.AnswerBean;
import com.yanxiu.gphone.hd.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.hd.student.view.question.QuestionsListener;
import com.yanxiu.gphone.hd.student.view.question.YXiuAnserTextView;
import com.yanxiu.gphone.hd.student.view.question.choicequestion.ChoiceQuestions;

/**
 * Created by Administrator on 2015/7/7.
 */
public class ChoiceQuestionFragment extends BaseQuestionFragment implements QuestionsListener, PageIndex {
    private static final String TAG=ChoiceQuestionFragment.class.getSimpleName();
//    private static ChoiceQuestionFragment choiceQuestionFragment;
    private ChoiceQuestions choiceQuestions;
    private QuestionsListener listener;
    //本地的保存数据bean
    private AnswerBean bean;

    private Button addBtn;

    private YXiuAnserTextView yXiuAnserTextView;


    public int typeId;

    private Fragment resolutionFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogInfo.log(TAG, "onCreate");
        this.typeId = (getArguments() != null) ? getArguments().getInt("typeId") : null;
//        rootView = BaseQuestionFragment.getViewLruCache(questionsEntity.getId());
//        if(rootView == null){
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_choices_question,null);
//        BaseQuestionFragment.addViewLruCache(questionsEntity.getId(), rootView);
//        }
        choiceQuestions = (ChoiceQuestions) rootView.findViewById(R.id.cq_item);
        choiceQuestions.flipNextPager(listener);
        yXiuAnserTextView = (YXiuAnserTextView) rootView.findViewById(R.id.yxiu_tv);
        FragmentTransaction ft = ChoiceQuestionFragment.this.getChildFragmentManager().beginTransaction();
        ft.replace(R.id.content_problem_analysis, new Fragment()).commitAllowingStateLoss();
        selectTypeView();
        if(questionsEntity != null && questionsEntity.getStem() != null){
            choiceQuestions.setAllDataSources(questionsEntity);
            yXiuAnserTextView.setTextHtml(questionsEntity.getStem());
        }
        choiceQuestions.setChoicesType(typeId);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogInfo.log(TAG,"onCreateView");
        LogInfo.log("geny", "---onCreateView-------pageIndex----" + pageIndex);
        return rootView;
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
//        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
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
        LogInfo.log(TAG, "onDestroy");
        LogInfo.log("geny", "---onDestroy-------pageIndex----" + pageIndex);
        choiceQuestions=null;
//        listener=null;
        bean=null;
        addBtn=null;
        yXiuAnserTextView=null;

        resolutionFragment=null;
    }

    @Override
    public void onDestroyView() {
        LogInfo.log(TAG, "onDestroyView");
        super.onDestroyView();
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

