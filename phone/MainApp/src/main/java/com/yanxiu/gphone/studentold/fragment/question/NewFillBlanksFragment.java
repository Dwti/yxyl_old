package com.yanxiu.gphone.studentold.fragment.question;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.common.core.utils.StringUtils;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.bean.AnswerBean;
import com.yanxiu.gphone.studentold.bean.QuestionEntity;
import com.yanxiu.gphone.studentold.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.studentold.inter.AnswerCallback;
import com.yanxiu.gphone.studentold.utils.FragmentManagerFactory;
import com.yanxiu.gphone.studentold.utils.QuestionUtils;
import com.yanxiu.gphone.studentold.view.FillBlankAnswerView;
import com.yanxiu.gphone.studentold.view.question.QuestionsListener;
import com.yanxiu.gphone.studentold.view.question.YXiuAnserTextView;

import java.util.ArrayList;

/**
 * Created by sunpeng on 2016/8/23.
 */
public class NewFillBlanksFragment extends BaseQuestionFragment implements QuestionsListener, PageIndex {
    private FillBlankAnswerView answerView;
    private View line;
    private QuestionsListener listener;
    private Boolean isKeyBoardActive = false;   //键盘是否弹起
    //本地的保存数据bean
    private AnswerBean bean;
    private int typeId;
    private AnswerCallback callback;
    private int position;
    private Fragment resolutionFragment;
    private Button addBtn;
    private boolean isWrongSetOrAnalysis = false;
    private YXiuAnserTextView tvQuestion;
    //    private ArrayList<String> listAnswer = new ArrayList<>();

    private Context mContext;
    private LinearLayout ll_answer_content;
    private View focusedView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.typeId = (getArguments() != null) ? getArguments().getInt("typeId") : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mContext = getActivity();
            rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_new_fill_blank_question, null);
            line = rootView.findViewById(R.id.view_line_ccc4a3_2);
            ll_answer_content = (LinearLayout) rootView.findViewById(R.id.ll_answer_content);
            View view_line_ccc4a3_2 = rootView.findViewById(R.id.view_line_ccc4a3_2);
            answerView = (FillBlankAnswerView) rootView.findViewById(R.id.cq_item);
            if (callback != null) {
                ll_answer_content.setVisibility(View.GONE);
                view_line_ccc4a3_2.setVisibility(View.GONE);
            }
            tvQuestion = (YXiuAnserTextView) rootView.findViewById(R.id.yxiu_tv);
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.replace(R.id.content_problem_analysis, new Fragment()).commitAllowingStateLoss();
            selectTypeView();
            setStemAndAnswerTemplate(questionsEntity);
            rootView.getRootView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (bottom < oldBottom) {
                        isKeyBoardActive = true;
                    } else {
                        isKeyBoardActive = false;
                    }
                }
            });
        return rootView;
    }

    private void setStemAndAnswerTemplate(QuestionEntity question) {
        if (question != null) {
            if (!StringUtils.isEmpty(question.getStem())) {
                StringBuilder sb = new StringBuilder(question.getStem());
                sb.append("  \n  \n");
                int blankCount = 0;
                int index = sb.indexOf("(_)");
                while (index != -1) {
                    blankCount++;
                    sb.replace(index, index + 3, "(" + blankCount + ")____");
                    index = sb.indexOf("(_)");
                }
                tvQuestion.setTextHtml(sb.toString());
                answerView.setAnswerTemplate(blankCount);
                if (isWrongSetOrAnalysis) {    //是否是错题或者是解析界面,如果是，则下面答案不可编辑
                    answerView.setAnswerList(question.getAnswerBean().getFillAnswers(), false);
                } else {
                    answerView.setAnswerList(question.getAnswerBean().getFillAnswers());
                }
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !ischild) {
            try {
                ((QuestionsListener) getActivity()).flipNextPager(null);
            }catch (Exception e){}
        }
        saveAnwser();
    }

    private void selectTypeView() {
        switch (answerViewTypyBean) {
            case SubjectExercisesItemBean.RESOLUTION:
                isWrongSetOrAnalysis = true;
                addAnalysisFragment();
                break;
            case SubjectExercisesItemBean.WRONG_SET:
                isWrongSetOrAnalysis = true;
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
                isWrongSetOrAnalysis = true;
                FragmentManagerFactory.addMistakeRedoFragment(getActivity(),getChildFragmentManager().beginTransaction(),questionsEntity,R.id.content_problem_analysis);
                break;
            default:
                isWrongSetOrAnalysis = false;
                break;
        }
    }

    private void addAnalysisFragment() {
        rootView.setClickable(false);
        Bundle args = new Bundle();
        args.putSerializable("questions", questionsEntity);
        resolutionFragment = Fragment.instantiate(getActivity(), ProblemAnalysisFragment.class.getName(), args);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.content_problem_analysis, resolutionFragment).commitAllowingStateLoss();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (questionsEntity.getAnswerBean().getFillAnswers() != null && questionsEntity.getAnswerBean().getFillAnswers().size() > 0) {
            if (isWrongSetOrAnalysis) {    //是否是错题或者是解析界面,如果是，则下面答案不可编辑
                answerView.setAnswerList(questionsEntity.getAnswerBean().getFillAnswers(), false);
            } else {
                answerView.setAnswerList(questionsEntity.getAnswerBean().getFillAnswers());
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (imm != null && !isKeyBoardActive) {
            line.requestFocus();
        }
        saveAnwser();
    }

    @Override
    public void saveAnwser() {
        hideSoftInput();
        if (answerView == null || questionsEntity == null)
            return;
        questionsEntity.getAnswerBean().setType(1);
        if (!isWrongSetOrAnalysis) {
            ArrayList<String> listAnswer;
            listAnswer = answerView.getAnswerList();
            questionsEntity.getAnswerBean().setFillAnswers(listAnswer);
            boolean isFinish = true;
            if (listAnswer != null && !listAnswer.isEmpty()) {
                if (listAnswer.size() == answerView.getChildCount()) {
                    for (int i = 0; i < listAnswer.size(); i++) {
                        if (TextUtils.isEmpty(listAnswer.get(i))) {
                            isFinish = false;
                            break;
                        }
                    }
                } else {
                    isFinish = false;
                }
                if (isFinish) {
                    questionsEntity.getAnswerBean().setIsFinish(true);
                    if (QuestionUtils.compareListByOrder(listAnswer, questionsEntity.getAnswer())){
                        questionsEntity.getAnswerBean().setIsRight(true);
                        questionsEntity.getAnswerBean().setStatus(AnswerBean.ANSER_RIGHT);
                    }else {
                        questionsEntity.getAnswerBean().setIsRight(false);
                        questionsEntity.getAnswerBean().setStatus(AnswerBean.ANSER_WRONG);
                    }
                }else{
                    questionsEntity.getAnswerBean().setStatus(AnswerBean.ANSER_UNFINISH);
                    questionsEntity.getAnswerBean().setIsFinish(false);
                }

            }else {
                questionsEntity.getAnswerBean().setStatus(AnswerBean.ANSER_UNFINISH);
                questionsEntity.getAnswerBean().setIsFinish(false);
            }
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

    @Override
    public void answerViewClick() {
        saveAnwser();
    }

//    public void hideSoftInput() {
//        if (imm != null) {
//            imm.hideSoftInputFromWindow(ll_answer_content.getWindowToken(), 0);
//            line.requestFocus();
//        }
//    }
}
