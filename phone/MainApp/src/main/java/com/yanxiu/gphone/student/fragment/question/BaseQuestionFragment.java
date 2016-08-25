package com.yanxiu.gphone.student.fragment.question;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.utils.YanXiuConstant;

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

    protected boolean ischild=false;
    /**是否小题需要显示最后一个*/
    protected boolean is_reduction=false;
    protected int childPagerIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("当前",this.getClass().getName());
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
            BaseQuestionFragment fragment=this;
            int typeId = questionsEntity.getType_id();
            if(typeId == QUESTION_SUBJECTIVE.type){
                //    6
                ivTopIcon.setImageResource(R.drawable.subjective_title_bg);
            }else if(typeId == QUESTION_SINGLE_CHOICES.type) {
                //    1
                ivTopIcon.setImageResource(R.drawable.choice_single_title_bg);
            }else if(typeId == QUESTION_MULTI_CHOICES.type){
                //    2
                ivTopIcon.setImageResource(R.drawable.choice_multi_title_bg);
            }else if(typeId == QUESTION_JUDGE.type){
                //    4
                ivTopIcon.setImageResource(R.drawable.judge_title_bg);
            }else if(typeId == QUESTION_FILL_BLANKS.type){
                //    3
                ivTopIcon.setImageResource(R.drawable.fill_blanks_bg);
            }else if(typeId == QUESTION_READING.type){
                //    5
                ivTopIcon.setImageResource(R.drawable.reading_title_bg);
            }else if(typeId == QUESTION_READ_COMPLEX.type){
                //    14
                ivTopIcon.setImageResource(R.drawable.read_complex_title_bg);
            }else if(typeId == QUESTION_SOLVE_COMPLEX.type){
                //    22
                ivTopIcon.setImageResource(R.drawable.solve_complex_title_bg);
            }else if (typeId==QUESTION_CLOZE_COMPLEX.type){
                //    15
                ivTopIcon.setImageResource(R.drawable.gestalt_complex_title_bg);
            }else if (typeId==QUESTION_LISTEN_COMPLEX.type){
                //    13
                ivTopIcon.setImageResource(R.drawable.listen_complex_title_bg);
            }else if ((9<=typeId && typeId<=12) || typeId==18 || typeId==19 || typeId==21){
                ivTopIcon.setImageResource(R.drawable.listen_complex_title_bg);
            }else if (typeId== QUESTION_CONNECT.type){
                //    7
                ivTopIcon.setImageResource(R.drawable.attachment_title_bg);
            }else if (typeId== QUESTION_COMPUTE.type){
                //    8
                ivTopIcon.setImageResource(R.drawable.calculate_title_bg);
            }else if (typeId==16){
                ivTopIcon.setImageResource(R.drawable.translation_title_bg);
            }else if (typeId==17){
                ivTopIcon.setImageResource(R.drawable.subjects_title_bg);
            }else if (typeId==20){
                ivTopIcon.setImageResource(R.drawable.sorting_title_bg);
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

    public void setChildPagerIndex(int childPagerIndex){
        this.childPagerIndex=childPagerIndex;
    }

    public void setRefresh(){
        String ss="";
        ss="";
    }

    public Fragment getChildFragment(){
        return null;
    }

    public int getChildCount(){
        return 1;
    }

    public void setIsChild(boolean ischild){
        this.ischild=ischild;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        BaseQuestionFragment context=this;
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (YanXiuConstant.OnClick_TYPE == 0) {
                is_reduction = false;
            } else if (YanXiuConstant.OnClick_TYPE == 1) {
                YanXiuConstant.OnClick_TYPE = 0;
                is_reduction = true;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        questionsEntity=null;
//
//        rootView=null;
//
//        tvQuestionTitle=null;
//
//        ivTopIcon=null;
//
//        rlTopView=null;
    }


}
