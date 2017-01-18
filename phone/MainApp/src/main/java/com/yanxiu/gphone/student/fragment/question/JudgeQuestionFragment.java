package com.yanxiu.gphone.student.fragment.question;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;
import com.yanxiu.gphone.student.view.question.judgequestion.JudgeQuestions;

/**
 * Created by lidm on 2015/7/7.
 *  判断题的fragment
 */
public class JudgeQuestionFragment extends BaseQuestionFragment implements QuestionsListener, PageIndex  {
    private JudgeQuestions judgeQuestions;
    private static QuestionsListener listener;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        switch (answerViewTypyBean){
            case SubjectExercisesItemBean.ANSWER_QUESTION:
                rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_judge_question,null);
                break;
            default:
                rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_judge_question_analysis,null);
                break;
        }

        judgeQuestions = (JudgeQuestions) rootView.findViewById(R.id.jq_item);
        yXiuAnserTextView = (YXiuAnserTextView) rootView.findViewById(R.id.yxiu_tv);
        View top_dotted_line = rootView.findViewById(R.id.top_dotted_line);
        if(ischild)
            top_dotted_line.setVisibility(View.GONE);
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser&&!ischild){
//            if (adapter!=null){
            try {
                ((QuestionsListener)getActivity()).flipNextPager(null);
            }catch (Exception e){

            }

//            }
        }
        if(isVisibleToUser && isResumed())
        Log.i("life","onVisible");
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
        Log.i("life","onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("life","onPause");
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
