package com.yanxiu.gphone.student.fragment.question;

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
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.inter.AnswerCallback;
import com.yanxiu.gphone.student.utils.QuestionUtils;
import com.yanxiu.gphone.student.view.FillBlankAnswerView;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2016/8/23.
 */
public class NewFillBlanksFragment extends BaseQuestionFragment implements QuestionsListener, PageIndex {
    private FillBlankAnswerView answerView;
    private QuestionsListener listener;
    //本地的保存数据bean
    private AnswerBean bean;
    private int typeId;
    private AnswerCallback callback;
    private int position;
    private Fragment resolutionFragment;
    private Button addBtn;
    private boolean isWrongSetOrAnalysis = false;
    private YXiuAnserTextView tvQuestion;
    private ArrayList<String> listAnswer = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.typeId = (getArguments() != null) ? getArguments().getInt("typeId") : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_new_fill_blank_question, null);
        LinearLayout ll_answer_content = (LinearLayout) rootView.findViewById(R.id.ll_answer_content);
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
                    sb.replace(index, index + 3, "(" + blankCount + ")");
                    index = sb.indexOf("(_)");
                }
                tvQuestion.setTextHtml(sb.toString());
                answerView.setAnswerTemplate(blankCount);
                if (isWrongSetOrAnalysis) {    //是否是错题或者是解析界面,如果是，则下面答案不可编辑
                    answerView.setAnswerList(question.getAnswerBean().getFillAnswers(), false);
                } else {
                    answerView.setAnswerList(listAnswer);
                }
            }
        }
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
        if (listAnswer.size() > 0)
            answerView.setAnswerList(listAnswer);
    }

    @Override
    public void onPause() {
        super.onPause();
        saveAnswer();
    }

    private void saveAnswer() {
        if(answerView == null || questionsEntity == null)
            return;
        if (!isWrongSetOrAnalysis) {
            listAnswer = answerView.getAnswerList();
            questionsEntity.getAnswerBean().setFillAnswers(listAnswer);
            boolean flag = true;
            if(listAnswer != null && !listAnswer.isEmpty()){
                for (int i=0; i<listAnswer.size(); i++) {
                    if (listAnswer.get(i).isEmpty()) {
                        flag = false;
                    }
                }
                if (flag) {
                    questionsEntity.getAnswerBean().setIsFinish(true);
                }
                if(QuestionUtils.compare(listAnswer,questionsEntity.getAnswer()))
                    questionsEntity.getAnswerBean().setIsRight(true);
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
        saveAnswer();
    }
}
