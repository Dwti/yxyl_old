package com.yanxiu.gphone.student.fragment.question;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.core.view.LineGridView;
import com.common.core.view.UnMoveGridView;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.ClassfyAnswerAdapter;
import com.yanxiu.gphone.student.adapter.ClassfyQuestionAdapter;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;
import com.yanxiu.gphone.student.view.question.classfy.ClassfyAnswers;

/**
 * Created by Yangjj on 2016/8/30.
 */
public class ClassfyQuestionFragment extends BaseQuestionFragment implements QuestionsListener, PageIndex {
    private QuestionsListener listener;
    //本地的保存数据bean
    private AnswerBean bean;

    public int typeId;

    private YXiuAnserTextView tvYanxiu;
    private UnMoveGridView gvClassfyQuestion;
    private ClassfyAnswers vgClassfyAnswers;
    private UnMoveGridView lgClassfyAnswers;

    private ClassfyQuestionAdapter classfyQuestionAdapter;
    private ClassfyAnswerAdapter classfyAnswerAdapter;

    private static final String IMG_SRC = "<img src=";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.typeId = (getArguments() != null) ? getArguments().getInt("typeId") : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_classfy_question, null);
        }
        initView();
        initData();
        return rootView;
    }

    private void initView() {
        tvYanxiu = (YXiuAnserTextView) rootView.findViewById(R.id.yxiu_tv);
        gvClassfyQuestion = (UnMoveGridView) rootView.findViewById(R.id.classfy_question_item);
        classfyQuestionAdapter = new ClassfyQuestionAdapter(getActivity());
        gvClassfyQuestion.setAdapter(classfyQuestionAdapter);
        vgClassfyAnswers = (ClassfyAnswers) rootView.findViewById(R.id.classfy_text_item);
        lgClassfyAnswers = (UnMoveGridView) rootView.findViewById(R.id.classfy_icon_item);
        classfyAnswerAdapter = new ClassfyAnswerAdapter(getActivity());
        lgClassfyAnswers.setAdapter(classfyAnswerAdapter);
    }

    private void initData() {
        if (questionsEntity != null && questionsEntity.getStem() != null) {
            tvYanxiu.setTextHtml(questionsEntity.getStem());
            if (questionsEntity.getPoint() != null) {
                classfyQuestionAdapter.setList(questionsEntity.getPoint());
            }
            if (questionsEntity.getContent() != null && questionsEntity.getContent().getChoices() != null
                    && questionsEntity.getContent().getChoices().size() > 0) {
                if (questionsEntity.getContent().getChoices().get(0).contains(IMG_SRC+"ttt")) {
                    classfyAnswerAdapter.setData(questionsEntity.getContent().getChoices());
                    lgClassfyAnswers.setVisibility(View.VISIBLE);
                    vgClassfyAnswers.setVisibility(View.GONE);
                } else {
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    for (int i=0; i<questionsEntity.getContent().getChoices().size(); i++) {
                        TextView view = (TextView) inflater.inflate(R.layout.layout_textview, null);
                        view.setText(questionsEntity.getContent().getChoices().get(i));
                        view.getLayoutParams();
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(8, 8, 8, 8);
                        view.setLayoutParams(lp);
                        vgClassfyAnswers.addView(view);
                    }
                    lgClassfyAnswers.setVisibility(View.GONE);
                    vgClassfyAnswers.setVisibility(View.VISIBLE);
                }

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

    }
}
