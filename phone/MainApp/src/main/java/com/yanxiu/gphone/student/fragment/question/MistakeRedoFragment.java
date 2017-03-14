package com.yanxiu.gphone.student.fragment.question;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.ImagePreviewActivity;
import com.yanxiu.gphone.student.activity.NoteEditActivity;
import com.yanxiu.gphone.student.adapter.NoteImageGridAdapter;
import com.yanxiu.gphone.student.bean.ExtendEntity;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;
import com.yanxiu.gphone.student.view.question.subjective.SubjectiveStarLayout;

import java.util.List;

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
    private TextView tv_note;
    private GridView grid_note_image;
    private NoteImageGridAdapter noteAdapter;
    private View ll_note_content;

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
        setNoteContentVisible(tv_note.getText().toString(),noteAdapter.getData());
    }

    private void initView(View rootView) {
        tvReportParseStatueText = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_parse_statue_text);
        difficultyStart = (SubjectiveStarLayout) rootView.findViewById(R.id.view_sub_difficulty_star);
        tvReportParseText = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_parse_text);
        hw_report_parse_layout= (LinearLayout) rootView.findViewById(R.id.hw_report_parse_layout);
        iv_edit_note = (ImageView) rootView.findViewById(R.id.iv_edit_note);
        ll_note_content= rootView.findViewById(R.id.ll_note_content);
        tv_note = (TextView) rootView.findViewById(R.id.tv_note);
        grid_note_image = (GridView) rootView.findViewById(R.id.grid_note_image);
        noteAdapter = new NoteImageGridAdapter(getActivity());
        grid_note_image.setAdapter(noteAdapter);
    }

    private void initListener() {
        iv_edit_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteEditActivity.launch(MistakeRedoFragment.this,tv_note.getText().toString(),noteAdapter.getData());
            }
        });

        grid_note_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImagePreviewActivity.lanuch(MistakeRedoFragment.this,noteAdapter.getData(),position,false);
            }
        });
    }

    private void setNoteContentVisible(String note,List<String> imagePath){
        if(TextUtils.isEmpty(note) && (imagePath == null || imagePath.size() ==0)){
            ll_note_content.setVisibility(View.GONE);
        }else {
            ll_note_content.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case NoteEditActivity.REQUEST_NOTE_EDIT:
                if(resultCode == getActivity().RESULT_OK){
                    tv_note.setText(data.getStringExtra(NoteEditActivity.NOTE_CONTENT));
                    noteAdapter.setData(data.getStringArrayListExtra(NoteEditActivity.PHOTO_PATH));
                    setNoteContentVisible(tv_note.getText().toString(),noteAdapter.getData());
                }
                break;
            default:
                break;
        }
    }

}
