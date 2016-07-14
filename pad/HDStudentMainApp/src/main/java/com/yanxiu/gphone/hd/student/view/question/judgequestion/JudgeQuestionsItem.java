package com.yanxiu.gphone.hd.student.view.question.judgequestion;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.view.question.AbsChoiceQuestionsItem;

/**
 * Created by Administrator on 2015/7/10.
 * 判断题 item
 */
public class JudgeQuestionsItem extends AbsChoiceQuestionsItem implements View.OnClickListener{

    private ImageView ivChoiceIcon;

//    private FrameLayout flItemBg;

    private TextView tvItemTxt;

    private LinearLayout llParentBg;

    private int drawableSelectId = -1;

    private int drawableNormalId = -1;

    private JudgeQuestions.JUDGE_TYPE judgeType;

    private OnChoicesItemClickListener choicesItemClickListener;

    public JudgeQuestionsItem(Context context) {
        super(context);
    }

    public JudgeQuestionsItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JudgeQuestionsItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView() {
        this.setOnClickListener(this);
        ivChoiceIcon = (ImageView) this.findViewById(R.id.iv_choice_icon_item);
//        flItemBg = (FrameLayout) this.findViewById(R.id.fl_choice_text_item_bg);
        llParentBg = (LinearLayout) this.findViewById(R.id.ll_parent_bg);
        tvItemTxt = (TextView) this.findViewById(R.id.tv_item_text);
    }


    public int getDrawableNormalId() {
        return drawableNormalId;
    }

    public void setDrawableNormalId(int drawableNormalId) {
        this.drawableNormalId = drawableNormalId;
    }

    public int getDrawableSelectId() {
        return drawableSelectId;
    }

    public void setDrawableSelectId(int drawableSelectId) {
        this.drawableSelectId = drawableSelectId;
    }

    @Override
    public View getView() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.judge_question_item;
    }


    public void setItemText(String str){
        if(!TextUtils.isEmpty(str)){
            tvItemTxt.setText(str);
        }
    }

    /**
     */
    public void setSelectedBg(int id){
        llParentBg.setBackgroundResource(id);
    }


    /**
     */
    public void setSelectedWrong(){
        setChecked(true);
        switch (judgeType) {
            case RIGHT:
                llParentBg.setBackgroundResource(R.drawable.selector_judge_correct);
                break;
            case WRONG:
                llParentBg.setBackgroundResource(R.drawable.selector_judge_wrong);
                break;
        }
        ivChoiceIcon.setVisibility(View.VISIBLE);
        ivChoiceIcon.setBackgroundResource(R.drawable.judge_wrong);
    }

    /**
     */
    public void setSelectedRight(){
        setChecked(true);
        switch (judgeType) {
            case RIGHT:
                llParentBg.setBackgroundResource(R.drawable.selector_judge_correct);
                break;
            case WRONG:
                llParentBg.setBackgroundResource(R.drawable.selector_judge_wrong);
                break;
        }
        ivChoiceIcon.setVisibility(View.VISIBLE);
        ivChoiceIcon.setBackgroundResource(R.drawable.judge_correct);
    }

    public void setSelectedHalfRight(){
        setChecked(true);
        llParentBg.setBackgroundResource(R.drawable.selector_judge_item);
        ivChoiceIcon.setVisibility(View.VISIBLE);
        ivChoiceIcon.setBackgroundResource(R.drawable.judge_correct);
    }


    /**
     * 设置选中状态
     */
    public void setSelected(){
        setChecked(true);
//        flItemBg.setBackgroundResource(R.drawable.choice_question_select);
//        ivChoiceIcon.setImageResource(drawableSelectId);
        switch (judgeType) {
            case RIGHT:
                llParentBg.setBackgroundResource(R.drawable.selector_judge_correct);
                break;
            case WRONG:
                llParentBg.setBackgroundResource(R.drawable.selector_judge_wrong);
                break;
        }
    }

    /**
     * 设置未选中状态
     */
    public void setUnSelected(){
        setChecked(false);
//        flItemBg.setBackgroundResource(R.drawable.choice_question_unselect);
//        ivChoiceIcon.setImageResource(drawableNormalId);
        llParentBg.setBackgroundResource(R.drawable.selector_judge_item);
//        switch (judgeType) {
//            case RIGHT:
//                llParentBg.setBackgroundResource(R.drawable.judge_question_right_bg_normal);
//                break;
//            case WRONG:
//                llParentBg.setBackgroundResource(R.drawable.judge_question_left_bg_normal);
//                break;
//        }
    }


    /**
     * 选择题每个item的点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(isClick()){
            refreshDrawableState();
            if(isChecked()){
                setUnSelected();
            }else{
                setSelected();
            }
            if(choicesItemClickListener != null){
                choicesItemClickListener.choicesItemClickListener(this);
            }
        }
    }

    public OnChoicesItemClickListener getChoicesItemClickListener() {
        return choicesItemClickListener;
    }

    public void setChoicesItemClickListener(OnChoicesItemClickListener choicesItemClickListener) {
        this.choicesItemClickListener = choicesItemClickListener;
    }

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {

    }

    public interface OnChoicesItemClickListener{
        void choicesItemClickListener(JudgeQuestionsItem judgeQuestionsItem);
    }


    @Override
    protected boolean isClick() {
        return isClick;
    }

    @Override
    protected String getSelectType() {
        return selectTpye;
    }

    @Override
    protected void setSelectType(String type) {
        selectTpye = type;
    }

    @Override
    protected void setItemContentText(String str) {

    }

    public JudgeQuestions.JUDGE_TYPE getJudgeType() {
        return judgeType;
    }

    public void setJudgeType(JudgeQuestions.JUDGE_TYPE judgeType) {
        this.judgeType = judgeType;
//        RelativeLayout.LayoutParams params;
//        params = (RelativeLayout.LayoutParams) flItemBg.getLayoutParams();
//        switch (judgeType) {
//            case RIGHT:
//                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//                break;
//            case WRONG:
//                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//                break;
//        }
//        flItemBg.setLayoutParams(params);
    }
}
