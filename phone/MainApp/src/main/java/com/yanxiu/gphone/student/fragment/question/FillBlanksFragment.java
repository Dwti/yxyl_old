package com.yanxiu.gphone.student.fragment.question;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.MistakeRedoActivity;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.inter.SetAnswerCallBack;
import com.yanxiu.gphone.student.utils.FragmentManagerFactory;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.fillblanks.FillBlanksFramelayout;

/**
 * Created by Administrator on 2015/7/7.
 */
public class FillBlanksFragment extends BaseQuestionFragment implements QuestionsListener, PageIndex {

    private FillBlanksFramelayout fillBlanksFramelayout;

    private AnswerBean bean;

    //    private FragmentTransaction ft;
    private Fragment resolutionFragment;
    private Button addBtn;
    private boolean isVisibleToUser = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (rootView == null) {
        rootView = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_fill_blanks, null);
        fillBlanksFramelayout = (FillBlanksFramelayout) rootView.findViewById(R.id.fb_item);
        if (answerViewTypyBean==SubjectExercisesItemBean.MISTAKEREDO) {
            fillBlanksFramelayout.setMistakeCallBack(callBack);
        }
        View top_dotted_line = rootView.findViewById(R.id.top_dotted_line);
        if (ischild)
            top_dotted_line.setVisibility(View.GONE);
        if (questionsEntity != null && questionsEntity.getStem() != null) {
            fillBlanksFramelayout.setQuestionEntity(questionsEntity);
            fillBlanksFramelayout.setAnswers(questionsEntity.getAnswer());
            fillBlanksFramelayout.setData(questionsEntity.getStem());
//            Log.d("asd", "Stem+++++" + questionsEntity.getStem());
        }

        FragmentTransaction ft = FillBlanksFragment.this.getChildFragmentManager().beginTransaction();
        ft.replace(R.id.content_problem_analysis, new Fragment()).commitAllowingStateLoss();
        selectTypeView();
//        }
        return rootView;
    }

    private void addAnalysisFragment() {
        rootView.setClickable(false);
        Bundle args = new Bundle();
        args.putSerializable("questions", questionsEntity);
        resolutionFragment = Fragment.instantiate(FillBlanksFragment.this.getActivity(), ProblemAnalysisFragment.class.getName(), args);
        FragmentTransaction ft = FillBlanksFragment.this.getChildFragmentManager().beginTransaction();
        ft.replace(R.id.content_problem_analysis, resolutionFragment).commitAllowingStateLoss();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bean == null) {
            bean = questionsEntity.getAnswerBean();
        }
        setDataSources(bean);
        LogInfo.log("geny", "onResume");
        if (!ischild && isVisibleToUser) {
            try {
                ((QuestionsListener) getActivity()).flipNextPager(null);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (fillBlanksFramelayout != null) {
            fillBlanksFramelayout.hideSoftInput();
//            answerViewClick();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (!isVisibleToUser && fillBlanksFramelayout != null) {
            fillBlanksFramelayout.hideSoftInput();
            if (bean != null) {
                LogInfo.log("king", "saveAnswers");
                fillBlanksFramelayout.saveAnswers();
            }
        }
        if (isVisibleToUser && !ischild) {
//            if (adapter!=null){
            try {
                ((QuestionsListener) getActivity()).flipNextPager(null);
            } catch (Exception e) {
            }
//            }
        }
    }

    @Override
    public void flipNextPager(QuestionsListener flip) {
    }

    @Override
    public void saveAnwser() {
        super.saveAnwser();
        answerViewClick();
    }

    @Override
    public void setDataSources(AnswerBean bean) {
        this.bean = bean;
        if (fillBlanksFramelayout != null) {
            fillBlanksFramelayout.setDataSources(bean);
        }
    }

    @Override
    public void initViewWithData(AnswerBean bean) {
        this.bean = bean;
    }

    @Override
    public void answerViewClick() {
        if (fillBlanksFramelayout != null) {
            fillBlanksFramelayout.hideSoftInput();
            if (bean != null) {
                LogInfo.log("king", "answerViewClick saveAnswers");
                fillBlanksFramelayout.saveAnswers();
            }
        }
    }

    private void selectTypeView() {
        fillBlanksFramelayout.setAnswerViewTypyBean(answerViewTypyBean);
        switch (answerViewTypyBean) {
            case SubjectExercisesItemBean.RESOLUTION:
                addAnalysisFragment();
                break;
            case SubjectExercisesItemBean.WRONG_SET:
                addBtn = (Button) rootView.findViewById(R.id.add_problem_analysis);
                addBtn.setVisibility(View.VISIBLE);
                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addBtn.setVisibility(View.GONE);
                        addAnalysisFragment();
                    }
                });
                break;
            case SubjectExercisesItemBean.MISTAKEREDO:
                if (ischild){
                    if (QuestionEntity.TYPE_SUBMIT_END.equals(questionsEntity.getType())||QuestionEntity.TYPE_DELETE_END.equals(questionsEntity.getType())){
                        fillBlanksFramelayout.setClearFoces();
//                        judgeQuestions.setDataSources(questionsEntity.getAnswerBean());
                        FragmentManagerFactory.addMistakeRedoFragment(getActivity(),getChildFragmentManager().beginTransaction(),questionsEntity,R.id.content_problem_analysis);
                    }
                    return;
                }
                FrameLayout layout = (FrameLayout) rootView.findViewById(R.id.fra_sub_or_del);
                layout.setVisibility(View.VISIBLE);
                final SubmitOrDeleteFragment fragment = new SubmitOrDeleteFragment();
                initSubOrDel(fragment);
                fragment.setEntity(questionsEntity);
                fragment.setListener(new SubmitOrDeleteFragment.OnButtonClickListener() {
                    @Override
                    public void onClick(String type) {
                        switch (type) {
                            case SubmitOrDeleteFragment.TYPE_SUBMIT:
                                questionsEntity.setType(QuestionEntity.TYPE_SUBMIT_END);
                                fillBlanksFramelayout.setClearFoces();
//                                judgeQuestions.setDataSources(questionsEntity.getAnswerBean());
                                checkTheAnswer();
                                fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_SUBMIT);
                                FragmentManagerFactory.addMistakeRedoFragment(getActivity(), getChildFragmentManager().beginTransaction(), questionsEntity, R.id.content_problem_analysis);
                                break;
                            case SubmitOrDeleteFragment.TYPE_DELETE:
                                questionsEntity.setType(QuestionEntity.TYPE_DELETE_END);
                                fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_DELETE);
                                break;
                        }
                    }
                });
                FragmentManager manager = getChildFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.fra_sub_or_del, fragment,"sub_or_del");
                transaction.show(fragment);
                transaction.commit();
                break;

        }
    }

    @Override
    public void setMistakeSubmit() {
        super.setMistakeSubmit();
        fillBlanksFramelayout.setClearFoces();
//        judgeQuestions.setDataSources(questionsEntity.getAnswerBean());
        questionsEntity.setType(QuestionEntity.TYPE_SUBMIT_END);
        FragmentManagerFactory.addMistakeRedoFragment(getActivity(),getChildFragmentManager().beginTransaction(),questionsEntity,R.id.content_problem_analysis);
    }

    @Override
    public void setMistakeDelete() {
        super.setMistakeDelete();
        questionsEntity.setType(QuestionEntity.TYPE_DELETE_END);
    }

    private SetAnswerCallBack callBack=new SetAnswerCallBack() {
        @Override
        public void callback() {
            if (ischild){
                boolean f1=getIsHavaAnswer();
                boolean f2=getTheAnswerIsRight();
                if (redoCallback!=null){
                    redoCallback.redoCallback();
                }
                return;
            }
            Fragment fragment=getChildFragmentManager().findFragmentByTag("sub_or_del");
            if (fragment==null){
                return;
            }
            SubmitOrDeleteFragment submitOrDeleteFragment= (SubmitOrDeleteFragment) fragment;
            initSubOrDel(submitOrDeleteFragment);
        }
    };

    private boolean getIsHavaAnswer(){
        boolean flag=fillBlanksFramelayout.getTheAnswerIsReady();
        questionsEntity.setIsAllBlanksFilled(flag);
        return  flag;
    }

    private void initSubOrDel(SubmitOrDeleteFragment fragment) {
        if (QuestionEntity.TYPE_SUBMIT.equals(questionsEntity.getType())) {
            if (getIsHavaAnswer()) {
                fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_NOT_SUBMIT_HASANSWER);
            } else {
                fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_NOT_SUBMIT_NOANSWER);
            }
        } else if (QuestionEntity.TYPE_SUBMIT_END.equals(questionsEntity.getType())) {
            questionsEntity.setType(QuestionEntity.TYPE_SUBMIT_END);
            fillBlanksFramelayout.setClearFoces();
//            judgeQuestions.setDataSources(questionsEntity.getAnswerBean());
            fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_SUBMIT);
            FragmentManagerFactory.addMistakeRedoFragment(getActivity(), getChildFragmentManager().beginTransaction(), questionsEntity, R.id.content_problem_analysis);
        } else if (QuestionEntity.TYPE_DELETE_END.equals(questionsEntity.getType())) {
            questionsEntity.setType(QuestionEntity.TYPE_SUBMIT_END);
            fillBlanksFramelayout.setClearFoces();
//            judgeQuestions.setDataSources(questionsEntity.getAnswerBean());
            fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_DELETE);
            FragmentManagerFactory.addMistakeRedoFragment(getActivity(), getChildFragmentManager().beginTransaction(), questionsEntity, R.id.content_problem_analysis);
        }
    }

    public boolean getTheAnswerIsRight(){
        fillBlanksFramelayout.saveAnswers();
        boolean flag=questionsEntity.getAnswerBean().isRight();
        if (flag){
            questionsEntity.setAnswerIsRight(QuestionEntity.ANSWER_RIGHT);
        }else {
            questionsEntity.setAnswerIsRight(QuestionEntity.ANSWER_FAIL);
        }
        return flag;
    }

    private void checkTheAnswer() {
        if (getTheAnswerIsRight()) {
            //回答正确
            ((MistakeRedoActivity) getActivity()).showPopup(MistakeRedoActivity.RIGHT);
        } else {
            //回答错误
            ((MistakeRedoActivity) getActivity()).showPopup(MistakeRedoActivity.FAIL);
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
        System.gc();
    }
}
