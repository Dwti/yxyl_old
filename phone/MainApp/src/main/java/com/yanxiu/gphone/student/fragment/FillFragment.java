package com.yanxiu.gphone.student.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.MistakeRedoActivity;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.fragment.question.BaseQuestionFragment;
import com.yanxiu.gphone.student.fragment.question.PageIndex;
import com.yanxiu.gphone.student.fragment.question.ProblemAnalysisFragment;
import com.yanxiu.gphone.student.fragment.question.SubmitOrDeleteFragment;
import com.yanxiu.gphone.student.inter.SetAnswerCallBack;
import com.yanxiu.gphone.student.utils.FragmentManagerFactory;
import com.yanxiu.gphone.student.utils.QuestionUtils;
import com.yanxiu.gphone.student.view.spanreplaceabletextview.FillBlankTextView;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.fillblanks.FillBlanksFramelayout;
import com.yanxiu.gphone.student.view.spanreplaceabletextview.FilledContentChangeListener;

import java.util.List;

/**
 * Created by Administrator on 2015/7/7.
 */
public class FillFragment extends BaseQuestionFragment implements QuestionsListener, PageIndex,FilledContentChangeListener {

    private FillBlanksFramelayout fillBlanksFramelayout;
    private FillBlankTextView mTextView;
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
        rootView = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_fill_blanks, null);
        fillBlanksFramelayout = (FillBlanksFramelayout) rootView.findViewById(R.id.fb_item);
        mTextView = (FillBlankTextView) rootView.findViewById(R.id.fill_blank_text_view);
        if (answerViewTypyBean == SubjectExercisesItemBean.MISTAKEREDO) {
            mTextView.setFilledContentChangeListener(this);
        }
        View top_dotted_line = rootView.findViewById(R.id.top_dotted_line);
        if (ischild)
            top_dotted_line.setVisibility(View.GONE);
        if (questionsEntity != null && questionsEntity.getStem() != null) {
            mTextView.setText(initStem(questionsEntity.getStem()),questionsEntity.getAnswer());
            mTextView.setFilledContent(questionsEntity.getAnswerBean().getFillAnswers());
        }

        FragmentTransaction ft = FillFragment.this.getChildFragmentManager().beginTransaction();
        ft.replace(R.id.content_problem_analysis, new Fragment()).commitAllowingStateLoss();
        selectTypeView();
        return rootView;
    }

    private String initStem(String stem){
        if(stem == null)
            return null;
        return stem.replaceAll("\\(_\\)", "<Blank>empty</Blank>");
    }
    private void addAnalysisFragment() {
        rootView.setClickable(false);
        Bundle args = new Bundle();
        args.putSerializable("questions", questionsEntity);
        resolutionFragment = Fragment.instantiate(FillFragment.this.getActivity(), ProblemAnalysisFragment.class.getName(), args);
        FragmentTransaction ft = FillFragment.this.getChildFragmentManager().beginTransaction();
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

    public void saveAndJudgeAnswers() {
        if (bean == null || mTextView == null)
            return;
        List<String> filledContent = mTextView.getFilledContent();
        bean.getFillAnswers().clear();
        bean.getFillAnswers().addAll(filledContent);
        List<String> answers = questionsEntity.getAnswer();
        boolean isRight = QuestionUtils.compareListByOrder(StringUtils.full2half(filledContent), StringUtils.full2half(answers));
        bean.setIsRight(isRight);
        bean.setIsFinish(false);
        for (String str : filledContent) {
            if (!TextUtils.isEmpty(str)) {
                bean.setIsFinish(true);
                break;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CommonCoreUtil.hideSoftInput(mTextView);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (!isVisibleToUser) {
            CommonCoreUtil.hideSoftInput(mTextView);
            saveAndJudgeAnswers();
        }
        if (isVisibleToUser && !ischild) {
            try {
                ((QuestionsListener) getActivity()).flipNextPager(null);
            } catch (Exception e) {
            }
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
    }

    @Override
    public void initViewWithData(AnswerBean bean) {
        this.bean = bean;
    }

    @Override
    public void answerViewClick() {
        CommonCoreUtil.hideSoftInput(mTextView);
        saveAndJudgeAnswers();
    }

    private void selectTypeView() {
        if (answerViewTypyBean == SubjectExercisesItemBean.RESOLUTION || answerViewTypyBean == SubjectExercisesItemBean.WRONG_SET || (answerViewTypyBean == SubjectExercisesItemBean.MISTAKEREDO && !"0".equals(questionsEntity.getType())))
            mTextView.setEditable(false);
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
                if (ischild) {
                    if (QuestionEntity.TYPE_SUBMIT_END.equals(questionsEntity.getType()) || QuestionEntity.TYPE_DELETE_END.equals(questionsEntity.getType())) {
                        FragmentManagerFactory.addMistakeRedoFragment(getActivity(), getChildFragmentManager().beginTransaction(), questionsEntity, R.id.content_problem_analysis);
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
                transaction.add(R.id.fra_sub_or_del, fragment, "sub_or_del");
                transaction.show(fragment);
                transaction.commit();
                break;

        }
    }

    @Override
    public void setMistakeSubmit() {
        super.setMistakeSubmit();
        questionsEntity.setType(QuestionEntity.TYPE_SUBMIT_END);
        FragmentManagerFactory.addMistakeRedoFragment(getActivity(), getChildFragmentManager().beginTransaction(), questionsEntity, R.id.content_problem_analysis);
    }

    @Override
    public void setMistakeDelete() {
        super.setMistakeDelete();
        questionsEntity.setType(QuestionEntity.TYPE_DELETE_END);
    }

    private SetAnswerCallBack callBack = new SetAnswerCallBack() {
        @Override
        public void callback() {

        }
    };

    private boolean isAllBlanksFilled(List<String> filledContents) {
        boolean flag = true;
        for(String str : filledContents){
            if(TextUtils.isEmpty(str)){
                flag = false;
                break;
            }
        }
        return flag;
    }

    private void initSubOrDel(SubmitOrDeleteFragment fragment) {
        if (QuestionEntity.TYPE_SUBMIT.equals(questionsEntity.getType())) {
            if (isAllBlanksFilled(mTextView.getFilledContent())) {
                fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_NOT_SUBMIT_HASANSWER);
            } else {
                fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_NOT_SUBMIT_NOANSWER);
            }
        } else if (QuestionEntity.TYPE_SUBMIT_END.equals(questionsEntity.getType())) {
            questionsEntity.setType(QuestionEntity.TYPE_SUBMIT_END);
            fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_SUBMIT);
            FragmentManagerFactory.addMistakeRedoFragment(getActivity(), getChildFragmentManager().beginTransaction(), questionsEntity, R.id.content_problem_analysis);
        } else if (QuestionEntity.TYPE_DELETE_END.equals(questionsEntity.getType())) {
            questionsEntity.setType(QuestionEntity.TYPE_SUBMIT_END);
            fragment.setQuestionType(SubmitOrDeleteFragment.QUESTION_DELETE);
            FragmentManagerFactory.addMistakeRedoFragment(getActivity(), getChildFragmentManager().beginTransaction(), questionsEntity, R.id.content_problem_analysis);
        }
    }

    public boolean getTheAnswerIsRight() {
        saveAndJudgeAnswers();
        boolean flag = questionsEntity.getAnswerBean().isRight();
        if (flag) {
            questionsEntity.setAnswerIsRight(QuestionEntity.ANSWER_RIGHT);
        } else {
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

    @Override
    public void filledContentChanged(List<String> filledContent) {
        if (ischild){
            questionsEntity.setIsAllBlanksFilled(isAllBlanksFilled(filledContent));
            getTheAnswerIsRight();
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
}
