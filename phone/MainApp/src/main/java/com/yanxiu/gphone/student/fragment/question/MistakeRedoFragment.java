package com.yanxiu.gphone.student.fragment.question;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.ExtendEntity;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;
import com.yanxiu.gphone.student.view.question.subjective.SubjectiveStarLayout;

/**
 * Created by Canghaixiao.
 * Time : 2017/2/15 17:10.
 * Function :
 */

public class MistakeRedoFragment extends Fragment {

    private QuestionEntity questionsEntity;
    private YXiuAnserTextView tvReportParseStatueText;
    private SubjectiveStarLayout difficultyStart;
    private YXiuAnserTextView tvReportParseText;
    private LinearLayout hw_report_parse_layout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        questionsEntity = getArguments() != null ? (QuestionEntity) getArguments().getSerializable("questions") : null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mistakeredo, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        if (questionsEntity==null){
            return;
        }
        if (questionsEntity.getExtend() != null && questionsEntity.getExtend().getData() != null) {
            ExtendEntity.DataEntity dataEntity = questionsEntity.getExtend().getData();
            switch (questionsEntity.getTemplate()) {
                case YanXiuConstant.CLASSIFY_QUESTION://归类
                    tvReportParseStatueText.setClasfyFlag(false);
                    tvReportParseStatueText.setTextHtml(dataEntity.getAnswerCompare().replaceAll("<img", "<imgFy"));
                    break;
                default:
                    tvReportParseStatueText.setTextHtml(dataEntity.getAnswerCompare());
                    break;
            }
        }
        difficultyStart.selectStarCount(questionsEntity.getDifficulty());
        if (!TextUtils.isEmpty(questionsEntity.getAnalysis())) {
            hw_report_parse_layout.setVisibility(View.VISIBLE);
            tvReportParseText.setTextHtml(questionsEntity.getAnalysis());
        }else {
            hw_report_parse_layout.setVisibility(View.GONE);
        }
    }

    private void initView(View rootView) {
        tvReportParseStatueText = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_parse_statue_text);
        difficultyStart = (SubjectiveStarLayout) rootView.findViewById(R.id.view_sub_difficulty_star);
        tvReportParseText = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_parse_text);
        hw_report_parse_layout= (LinearLayout) rootView.findViewById(R.id.hw_report_parse_layout);
    }
}
