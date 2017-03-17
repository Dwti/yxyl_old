package com.yanxiu.gphone.student.fragment.question;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.QuestionEntity;

/**
 * Created by Canghaixiao.
 * Time : 2017/2/22 10:48.
 * Function :
 */

public class SubmitOrDeleteFragment extends Fragment {

    public static final String TYPE_SUBMIT="submit";
    public static final String TYPE_DELETE="delete";

    public static final String QUESTION_NOT_SUBMIT_HASANSWER="0";
    public static final String QUESTION_NOT_SUBMIT_NOANSWER="1";
    public static final String QUESTION_SUBMIT="2";
    public static final String QUESTION_DELETE="3";

    private static final float UN_CLICK=0.6f;
    private static final float CLICK=1.0f;

    private QuestionEntity entity;

    private OnButtonClickListener listener;
    private Button button;
    private String questionType="";
    private boolean isReady=false;

    public interface OnButtonClickListener{
        void onClick(String type);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_submitordelete,container,false);
            initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        isReady=true;
        initQuestionType();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearAlpha();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        clearAlpha();
    }

    private void clearAlpha(){
        if (button!=null){
            button.setAlpha(0);
//            button=null;
        }
    }

    private void initView(View view) {
        button= (Button) view.findViewById(R.id.add_problem_mistakeredo);
        button.setAlpha(UN_CLICK);
        button.setVisibility(View.VISIBLE);
        button.setText(R.string.question_submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    if (entity.getType().equals("0")) {
                        listener.onClick(TYPE_SUBMIT);
                    }else if (entity.getType().equals("1")){
                        listener.onClick(TYPE_DELETE);
                    }
                }
            }
        });
    }

    public void setListener(OnButtonClickListener listener) {
        this.listener = listener;
    }

    public void setEntity(QuestionEntity entity) {
        this.entity = entity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null){
            questionType=savedInstanceState.getString("questionType","");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("questionType", questionType);
        super.onSaveInstanceState(outState);
    }

    private void initQuestionType(){
        switch (questionType){
            case QUESTION_NOT_SUBMIT_NOANSWER:
                button.setClickable(false);
//                button.setBackgroundResource(R.drawable.judge_item_pre);
                button.setText(R.string.submit_txt);
                button.setAlpha(UN_CLICK);
                button.invalidate();
//                button.getPaint().setAlpha(0);
//                button.getPaint().setAlpha(150);
                break;
            case QUESTION_NOT_SUBMIT_HASANSWER:
                button.setClickable(true);
//                button.setBackgroundResource(R.drawable.judge_item_nor);
                button.setText(R.string.submit_txt);
                button.setAlpha(CLICK);
                button.invalidate();
//                button.getPaint().setAlpha(0);
//                button.getPaint().setAlpha(150);
                break;
            case QUESTION_SUBMIT:
                button.setClickable(true);
//                button.setBackgroundResource(R.drawable.judge_item_nor);
                button.setText(R.string.delete_question);
                button.setAlpha(CLICK);
                button.invalidate();
//                button.getPaint().setAlpha(0);
//                button.getPaint().setAlpha(150);
                break;
            case QUESTION_DELETE:
                button.setClickable(false);
//                button.setBackgroundResource(R.drawable.judge_item_pre);
                button.setText(R.string.delete_question_end);
                button.setAlpha(UN_CLICK);
                button.invalidate();
//                button.getPaint().setAlpha(0);
//                button.getPaint().setAlpha(150);
                break;
        }
    }

    public void setQuestionType(String questionType){
        this.questionType=questionType;
        if (isReady){
            initQuestionType();
        }
    }
}
