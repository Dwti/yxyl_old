package com.yanxiu.gphone.hd.student.fragment.question;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.bean.AnswerBean;
import com.yanxiu.gphone.hd.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.hd.student.view.question.QuestionsListener;
import com.yanxiu.gphone.hd.student.view.question.YXiuAnserTextView;
import com.yanxiu.gphone.hd.student.view.question.judgequestion.JudgeQuestions;

/**
 * Created by lidm on 2015/7/7.
 *  判断题的fragment
 */
public class JudgeQuestionFragment extends BaseQuestionFragment implements QuestionsListener, PageIndex  {
    private JudgeQuestions judgeQuestions;
    private QuestionsListener listener;
    private YXiuAnserTextView yXiuAnserTextView;
    private AnswerBean bean;

    //是否是题目解析
//    private boolean isResolution;
    private FragmentTransaction ft;
    private Fragment resolutionFragment;
    //是否是错题集
//    private boolean isWrongSet;
    private Button addBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (answerViewTypyBean){
            case SubjectExercisesItemBean.ANSWER_QUESTION:
                rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_judge_question,null);
                break;
            default:
                rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_judge_question_analysis,null);
                break;
        }
//        rootView = BaseQuestionFragment.getViewLruCache(questionsEntity.getId());
//        if(rootView == null){
//            switch (answerViewTypyBean){
//                case SubjectExercisesItemBean.ANSWER_QUESTION:
//                    rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_judge_question,null);
//                    break;
//                default:
//                    rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_judge_question_analysis,null);
//                    break;
//            }
//            BaseQuestionFragment.addViewLruCache(questionsEntity.getId(), rootView);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        judgeQuestions = (JudgeQuestions) rootView.findViewById(R.id.jq_item);
        yXiuAnserTextView = (YXiuAnserTextView) rootView.findViewById(R.id.yxiu_tv);
        if(questionsEntity != null){
            judgeQuestions.setAnswer(questionsEntity.getAnswer());
            if(questionsEntity.getStem() != null){
//            String tet = "萝卜白菜的ss博客<img src='https://www.baidu.com/img/baidu_jgylogo3.gif'/>自定义";
                yXiuAnserTextView.setTextHtml(questionsEntity.getStem());
//            yXiuAnserTextView.setTextHtml(tet);
            }
        }
        FragmentTransaction ft = JudgeQuestionFragment.this.getChildFragmentManager().beginTransaction();
        ft.replace(R.id.content_problem_analysis, new Fragment()).commitAllowingStateLoss();
        selectTypeView();
//        if(isWrongSet){
//
//
//        }else if(isResolution){
////            addBtn.setVisibility(View.VISIBLE);
//
//        }

        judgeQuestions.setDataSources();
        judgeQuestions.flipNextPager(listener);

        return rootView;
    }

    private void selectTypeView(){
        switch (answerViewTypyBean){
            case SubjectExercisesItemBean.RESOLUTION:
                judgeQuestions.setIsResolution(true);
                judgeQuestions.setIsClick(false);
                addAnalysisFragment();
                break;
            case SubjectExercisesItemBean.WRONG_SET:
                addBtn = (Button) rootView.findViewById(R.id.add_problem_analysis);
                addBtn.setVisibility(View.VISIBLE);
                judgeQuestions.setIsWrongSet(true);
                judgeQuestions.setIsClick(false);
                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addBtn.setVisibility(View.GONE);
                        addAnalysisFragment();
                        judgeQuestions.initViewWithData(bean);
                    }
                });
                break;
        }
    }


    private void addAnalysisFragment(){

        Bundle args = new Bundle();
        args.putSerializable("questions", questionsEntity);
        resolutionFragment = Fragment.instantiate(JudgeQuestionFragment.this.getActivity(), ProblemAnalysisFragment.class.getName(), args);
        FragmentTransaction ft = JudgeQuestionFragment.this.getChildFragmentManager().beginTransaction();
        ft.replace(R.id.content_problem_analysis, resolutionFragment).commitAllowingStateLoss();
    }
    @Override
    public void onResume() {
        super.onResume();
        if(bean == null){
            bean = questionsEntity.getAnswerBean();
        }
        judgeQuestions.setDataSources(bean);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootView = null;
        resolutionFragment = null;
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        rootView=null;
        judgeQuestions=null;
//        listener=null;
        yXiuAnserTextView=null;
        bean=null;
        ft=null;
        resolutionFragment=null;
        addBtn=null;
        System.gc();
    }
}
