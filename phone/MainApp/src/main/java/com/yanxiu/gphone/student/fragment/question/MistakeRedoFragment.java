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

import org.json.JSONException;
import org.json.JSONObject;

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
        if (questionsEntity.getExtend() != null && questionsEntity.getExtend().getData() != null) {
            String answer="";
            switch (questionsEntity.getTemplate()) {
                case YanXiuConstant.SINGLE_CHOICES:
                    answer="正确答案是:";
                    if (questionsEntity.getAnswer().get(0).equals("0")){
                        answer+="A";
                    }else if (questionsEntity.getAnswer().get(0).equals("1")){
                        answer+="B";
                    }else if (questionsEntity.getAnswer().get(0).equals("2")){
                        answer+="C";
                    }else if (questionsEntity.getAnswer().get(0).equals("3")){
                        answer+="D";
                    }
                    if (questionsEntity.getAnswerIsRight()==QuestionEntity.ANSWER_RIGHT){
                        answer+=getString(R.string.answer_right);
                    }else if (questionsEntity.getAnswerIsRight()==QuestionEntity.ANSWER_FAIL){
                        answer+=getString(R.string.answer_fall);
                    }
                    tvReportParseStatueText.setTextHtml(answer);
                    break;
                case YanXiuConstant.MULTI_CHOICES:
                    answer="正确答案是:";
                    List<String> mulit_list=questionsEntity.getAnswer();
                    for (String s:mulit_list){
                        String ss=String.valueOf(numToLetter(String.valueOf(s)));
                        answer+=ss;
                    }
                    if (questionsEntity.getAnswerIsRight()==QuestionEntity.ANSWER_RIGHT){
                        answer+=getString(R.string.answer_right);
                    }else if (questionsEntity.getAnswerIsRight()==QuestionEntity.ANSWER_FAIL){
                        answer+=getString(R.string.answer_fall);
                    }
                    tvReportParseStatueText.setTextHtml(answer);
                    break;
                case YanXiuConstant.FILL_BLANK:
                    answer="正确答案是:";
                    List<String> fill_list=questionsEntity.getAnswer();
                    for (String s:fill_list){
                        answer+=s+" ";
                    }
                    if (questionsEntity.getAnswerIsRight()==QuestionEntity.ANSWER_RIGHT){
                        answer+=getString(R.string.answer_right);
                    }else if (questionsEntity.getAnswerIsRight()==QuestionEntity.ANSWER_FAIL){
                        answer+=getString(R.string.answer_fall);
                    }
                    tvReportParseStatueText.setTextHtml(answer);
                    break;
                case YanXiuConstant.JUDGE_QUESTION:
                    answer="正确答案是:";
                    if (questionsEntity.getAnswer().get(0).equals("0")){
                        answer+="错误";
                    }else if (questionsEntity.getAnswer().get(0).equals("1")){
                        answer+="正确";
                    }
                    if (questionsEntity.getAnswerIsRight()==QuestionEntity.ANSWER_RIGHT){
                        answer=answer+getString(R.string.answer_right);
                    }else if (questionsEntity.getAnswerIsRight()==QuestionEntity.ANSWER_FAIL){
                        answer=answer+getString(R.string.answer_fall);
                    }
                    tvReportParseStatueText.setTextHtml(answer);
                    break;
                case YanXiuConstant.CONNECT_QUESTION:
//                {"answer":"0,6","point":"20342"}
                    answer="正确答案是:";
                    List<String> connect_list=questionsEntity.getAnswer();
                    for (String s:connect_list){
                        try {
                            JSONObject object=new JSONObject(s);
                            String an=object.optString("answer","");
                            List<String> class_list=questionsEntity.getAnswer();
                            String string=numToString(class_list.size(),an);
                            answer+=string;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (questionsEntity.getAnswerIsRight()==QuestionEntity.ANSWER_RIGHT){
                        answer=answer+getString(R.string.answer_right);
                    }else if (questionsEntity.getAnswerIsRight()==QuestionEntity.ANSWER_FAIL){
                        answer=answer+getString(R.string.answer_fall);
                    }
                    tvReportParseStatueText.setTextHtml(answer);
                    break;
                case YanXiuConstant.CLASSIFY_QUESTION://归类
//                {"answer":"0,1,2,3","name":"hot","point":"20002"}
                    tvReportParseStatueText.setClasfyFlag(false);
                    answer="正确答案是 ";
                    List<String> class_list=questionsEntity.getAnswer();
                    for (String s:class_list){
                        try {
                            JSONObject object=new JSONObject(s);
                            String name=object.optString("name","");
                            String an=object.optString("answer","");
                            List<String> list=questionsEntity.getContent().getChoices();
                            String[] str=an.split(",");
                            answer+=name+":";
                            if (str.length>0){
                                for (String ss:str){
                                    int index=Integer.parseInt(ss);
                                    answer+=list.get(index)+" ";
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (questionsEntity.getAnswerIsRight()==QuestionEntity.ANSWER_RIGHT){
                        answer=answer+getString(R.string.answer_right);
                    }else if (questionsEntity.getAnswerIsRight()==QuestionEntity.ANSWER_FAIL){
                        answer=answer+getString(R.string.answer_fall);
                    }
                    tvReportParseStatueText.setTextHtml(answer.replaceAll("<img", "<imgFy"));
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

    public String numToString(int size,String str){
        String string="";
        String[] strings=str.split(",");
        if (strings.length>0){
//            for (String s:strings) {
//                int index=Integer.parseInt(strings[0]);
                if (Integer.parseInt(strings[0]) < size) {
                    string+="左"+(Integer.parseInt(strings[0])+1);
                    string+="连";
                    string+="右"+(Integer.parseInt(strings[1])-size+1);
                    return string+=" ";
                }

                if (Integer.parseInt(strings[0]) >= size) {
                    string+="左"+(Integer.parseInt(strings[1])+1);
                    string+="连";
                    string+="右"+(Integer.parseInt(strings[0])-size+1);
                    return string+=" ";
                }
//            }
        }
        return string;
    }


    // 将数字转换成字母
    public char[] numToLetter(String input) {
        char c[] = input.toCharArray();
        int i = 0;
        for (byte b : input.getBytes()) {
            c[i] = (char) (b + 49 - 26 - 6);
        }
        return c;
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
