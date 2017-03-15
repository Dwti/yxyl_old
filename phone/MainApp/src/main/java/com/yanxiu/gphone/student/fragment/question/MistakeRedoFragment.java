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
import com.yanxiu.gphone.student.activity.MistakeRedoActivity;
import com.yanxiu.gphone.student.activity.NoteEditActivity;
import com.yanxiu.gphone.student.adapter.NoteImageGridAdapter;
import com.yanxiu.gphone.student.bean.ExtendEntity;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;
import com.yanxiu.gphone.student.view.question.subjective.SubjectiveStarLayout;

import java.util.ArrayList;
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
    private LinearLayout ll_hw_report_parse;
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
        String wqid="";
        String qid="";
        try {
            wqid=((MistakeRedoActivity)getActivity()).getWqid();
            qid=((MistakeRedoActivity)getActivity()).getQid();
        }catch (Exception e){
            wqid="";
            qid="";
        }

        questionsEntity.setWqid(wqid);
        questionsEntity.setQid(qid);
        if (questionsEntity.getExtend() != null && questionsEntity.getExtend().getData() != null) {
            ExtendEntity.DataEntity dataEntity = questionsEntity.getExtend().getData();
            String answer=dataEntity.getAnswerCompare().substring(0,dataEntity.getAnswerCompare().lastIndexOf(","));
            if (questionsEntity.getAnswerIsRight()==QuestionEntity.ANSWER_RIGHT){
                answer=answer+getString(R.string.answer_right);
            }else if (questionsEntity.getAnswerIsRight()==QuestionEntity.ANSWER_FAIL){
                answer=answer+getString(R.string.answer_fall);
            }
            switch (questionsEntity.getTemplate()) {
                case YanXiuConstant.CLASSIFY_QUESTION://归类
                    tvReportParseStatueText.setClasfyFlag(false);
                    tvReportParseStatueText.setTextHtml(answer.replaceAll("<img", "<imgFy"));
                    break;
                default:
                    tvReportParseStatueText.setTextHtml(answer);
                    break;
            }
        }
        difficultyStart.selectStarCount(questionsEntity.getDifficulty());
        if (!TextUtils.isEmpty(questionsEntity.getAnalysis())) {
            ll_hw_report_parse.setVisibility(View.VISIBLE);
            tvReportParseText.setTextHtml(questionsEntity.getAnalysis());
        }else {
            ll_hw_report_parse.setVisibility(View.GONE);
        }
        noteAdapter.setData(questionsEntity.getJsonNote().getImages());
        tv_note.setText(questionsEntity.getJsonNote().getText());
        setNoteContentVisible(tv_note.getText().toString(),noteAdapter.getData());
    }

    private void initView(View rootView) {
        tvReportParseStatueText = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_parse_statue_text);
        difficultyStart = (SubjectiveStarLayout) rootView.findViewById(R.id.view_sub_difficulty_star);
        tvReportParseText = (YXiuAnserTextView) rootView.findViewById(R.id.hw_report_parse_text);
        ll_hw_report_parse = (LinearLayout) rootView.findViewById(R.id.ll_hw_report_parse);
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
                Bundle args = new Bundle();
                args.putString(NoteEditActivity.NOTE_CONTENT,tv_note.getText().toString());
                args.putStringArrayList(NoteEditActivity.PHOTO_PATH,noteAdapter.getData());
                args.putString(NoteEditActivity.WQID,questionsEntity.getWqid());
                args.putString(NoteEditActivity.QID,questionsEntity.getQid());
                NoteEditActivity.launch(MistakeRedoFragment.this,args);
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
                    String text = data.getStringExtra(NoteEditActivity.NOTE_CONTENT);
                    ArrayList<String> images = data.getStringArrayListExtra(NoteEditActivity.PHOTO_PATH);
                    tv_note.setText(text);
                    noteAdapter.setData(images);
                    questionsEntity.getJsonNote().setText(text);
                    questionsEntity.getJsonNote().setImages(images);
                    setNoteContentVisible(tv_note.getText().toString(),noteAdapter.getData());
                }
                break;
            default:
                break;
        }
    }

}
