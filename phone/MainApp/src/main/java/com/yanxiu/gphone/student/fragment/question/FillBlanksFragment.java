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
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.fillblanks.FillBlanksFramelayout;

/**
 * Created by Administrator on 2015/7/7.
 */
public class FillBlanksFragment extends BaseQuestionFragment implements QuestionsListener , PageIndex {

    private FillBlanksFramelayout fillBlanksFramelayout;

    private AnswerBean bean;

//    private FragmentTransaction ft;
    private Fragment resolutionFragment;
    private Button addBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_fill_blanks, null);
        fillBlanksFramelayout = (FillBlanksFramelayout)rootView.findViewById(R.id.fb_item);
        if(questionsEntity != null && questionsEntity.getStem() != null){
            fillBlanksFramelayout.setData(questionsEntity.getStem());
            Log.d("asd","Stem+++++"+questionsEntity.getStem());
            fillBlanksFramelayout.setAnswers(questionsEntity.getAnswer());
        }


        FragmentTransaction ft = FillBlanksFragment.this.getChildFragmentManager().beginTransaction();
        ft.replace(R.id.content_problem_analysis, new Fragment()).commitAllowingStateLoss();
        selectTypeView();
        return rootView;
    }

    private void addAnalysisFragment(){
        rootView.setClickable(false);
        //            addBtn.setVisibility(View.VISIBLE);
//        ft = FillBlanksFragment.this.getChildFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putSerializable("questions", questionsEntity);
        resolutionFragment = Fragment.instantiate(
                FillBlanksFragment.this.getActivity(),
                ProblemAnalysisFragment.class.getName(), args);
        FragmentTransaction ft = FillBlanksFragment.this.getChildFragmentManager().beginTransaction();
        ft.replace(R.id.content_problem_analysis, resolutionFragment).commitAllowingStateLoss();
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

    @Override public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!isVisibleToUser && fillBlanksFramelayout!=null){
            fillBlanksFramelayout.hideSoftInput();
            if(bean!=null){
                LogInfo.log("king","saveAnswers");
                fillBlanksFramelayout.saveAnswers();
            }
        }
        if (isVisibleToUser&&!ischild){
//            if (adapter!=null){
                ((QuestionsListener)getActivity()).flipNextPager(null);
//            }
        }
    }

    @Override
    public void flipNextPager(QuestionsListener flip) {
    }

    @Override
    public void setDataSources(AnswerBean bean) {
        this.bean = bean;
        if(fillBlanksFramelayout!=null){
            fillBlanksFramelayout.setDataSources(bean);
        }
    }

    @Override
    public void initViewWithData(AnswerBean bean) {
        this.bean = bean;
    }

    @Override public void answerViewClick() {
        if(fillBlanksFramelayout!=null){
            fillBlanksFramelayout.hideSoftInput();
            if(bean!=null){
                LogInfo.log("king","answerViewClick saveAnswers");
                fillBlanksFramelayout.saveAnswers();
            }
        }
    }

    private void selectTypeView(){
        fillBlanksFramelayout.setAnswerViewTypyBean(answerViewTypyBean);
        switch (answerViewTypyBean){
            case SubjectExercisesItemBean.RESOLUTION:
                addAnalysisFragment();
                break;
            case SubjectExercisesItemBean.WRONG_SET:
                addBtn = (Button) rootView.findViewById(R.id.add_problem_analysis);
                addBtn.setVisibility(View.VISIBLE);
                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        addBtn.setVisibility(View.GONE);
                        addAnalysisFragment();
                    }
                });
                break;
        }
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
    public void onDestroy() {
        super.onDestroy();
        rootView=null;
        fillBlanksFramelayout=null;
        bean=null;
        resolutionFragment=null;
        addBtn=null;
        rootView=null;
        fillBlanksFramelayout=null;
        bean=null;
        resolutionFragment=null;
        addBtn=null;

        System.gc();
    }
}
