package com.yanxiu.gphone.student.fragment.question;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< e60ec203cf7d072dc6a4f9f7fccd91d098cf34cf
import android.widget.LinearLayout;
=======
import android.widget.ImageView;
>>>>>>> 添加解析错题界面里面笔记编辑入口

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.NoteEditActivity;
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
    private ImageView iv_edit_note;

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
        initListener();
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
        iv_edit_note = (ImageView) rootView.findViewById(R.id.iv_edit_note);

    }

    private void initListener() {
        iv_edit_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoteEditActivity.class);
                startActivity(intent);
            }
        });
    }

}
