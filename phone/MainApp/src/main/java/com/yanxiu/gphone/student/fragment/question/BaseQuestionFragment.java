package com.yanxiu.gphone.student.fragment.question;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.QuestionEntity;

import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.*;

/**
 * Created by Administrator on 2015/12/17.
 */
public class BaseQuestionFragment extends Fragment {
    protected QuestionEntity questionsEntity;

    protected int answerViewTypyBean;

    protected int pageIndex;

    protected View rootView;

    private TextView tvQuestionTitle;
    private String questionTitle;

    private ImageView ivTopIcon;

    private RelativeLayout rlTopView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("asd",this.getClass().getSimpleName());
        this.questionsEntity = (getArguments() != null) ? (QuestionEntity) getArguments().getSerializable("questions") : null;
        this.answerViewTypyBean = (getArguments() != null) ? getArguments().getInt("answerViewTypyBean") : null;
        this.pageIndex = (getArguments() != null) ? getArguments().getInt("pageIndex") : 0;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvQuestionTitle = (TextView) view.findViewById(R.id.tv_title_name);
        rlTopView = (RelativeLayout) view.findViewById(R.id.rl_top);
        ivTopIcon = (ImageView) view.findViewById(R.id.iv_answer_top_icon);
        if(questionsEntity != null){

            int typeId = questionsEntity.getType_id();
            if(typeId == QUESTION_SUBJECTIVE.type){
                ivTopIcon.setImageResource(R.drawable.subjective_title_bg);
            }else if(typeId == QUESTION_SINGLE_CHOICES.type) {
                ivTopIcon.setImageResource(R.drawable.choice_single_title_bg);
            }else if(typeId == QUESTION_MULTI_CHOICES.type){
                ivTopIcon.setImageResource(R.drawable.choice_multi_title_bg);
            }else if(typeId == QUESTION_JUDGE.type){
                ivTopIcon.setImageResource(R.drawable.judge_title_bg);
            }else if(typeId == QUESTION_FILL_BLANKS.type){
                ivTopIcon.setImageResource(R.drawable.fill_blanks_bg);
            }else if(typeId == QUESTION_READING.type){
                ivTopIcon.setImageResource(R.drawable.reading_title_bg);
            }
            if(questionsEntity.isReadQuestion()){
                rlTopView.setVisibility(View.GONE);
            }else{
                questionTitle = questionsEntity.getTitleName();
                if(!TextUtils.isEmpty(questionTitle)){
                    tvQuestionTitle.setText(questionTitle);
                }
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        questionsEntity=null;

        rootView=null;

        tvQuestionTitle=null;

        ivTopIcon=null;

        rlTopView=null;
    }


}
