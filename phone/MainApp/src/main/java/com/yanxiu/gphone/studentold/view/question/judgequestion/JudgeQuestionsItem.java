package com.yanxiu.gphone.studentold.view.question.judgequestion;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.inter.SetAnswerCallBack;
import com.yanxiu.gphone.studentold.view.question.AbsChoiceQuestionsItem;

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

    private SetAnswerCallBack callBack;
    private JudgeQuestions.JUDGE_TYPE judgeType;

    private OnChoicesItemClickListener choicesItemClickListener;
    private ImageView iv_item_img;

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
        llParentBg = (LinearLayout) this.findViewById(R.id.ll_parent_bg);
        iv_item_img= (ImageView) this.findViewById(R.id.iv_item_img);
        tvItemTxt = (TextView) this.findViewById(R.id.tv_item_text);
    }


    public void setShadow(int textcolor,int shadowcolor){
        if (tvItemTxt!=null) {
            tvItemTxt.setTextColor(textcolor);
            tvItemTxt.setShadowLayer(1, 0, 2, shadowcolor);
        }
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

    public void setItemImg(int resId){
        if (iv_item_img!=null) {
            iv_item_img.setImageResource(resId);
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
//                llParentBg.setBackgroundResource(R.drawable.selector_judge_correct);
//                setShadow(Color.parseColor("#006666"),Color.parseColor("#33ffff"));
                llParentBg.setBackgroundResource(R.drawable.selector_judge_item);
                setShadow(Color.parseColor("#805500"),Color.parseColor("#ffff99"));
                break;
            case WRONG:
                /**
                 * 哪个货写的功能，靠，同样的代码，重复三次
                 * */
//                llParentBg.setBackgroundResource(R.drawable.selector_judge_wrong);
//                setShadow(Color.parseColor("#80334d"),Color.parseColor("#ffe5ee"));
                llParentBg.setBackgroundResource(R.drawable.selector_judge_item);
                setShadow(Color.parseColor("#805500"),Color.parseColor("#ffff99"));
                break;
        }
//        ivChoiceIcon.setVisibility(View.VISIBLE);
//        ivChoiceIcon.setBackgroundResource(R.drawable.judge_wrong);
    }


    public void setDefult(){
        llParentBg.setBackgroundResource(R.drawable.selector_judge_item);
        setShadow(Color.parseColor("#805500"),Color.parseColor("#ffff99"));
    }

    /**
     */
    public void setSelectedRight(){
        setChecked(true);
        switch (judgeType) {
            case RIGHT:
//                llParentBg.setBackgroundResource(R.drawable.selector_judge_correct);
//                setShadow(Color.parseColor("#006666"),Color.parseColor("#33ffff"));
                llParentBg.setBackgroundResource(R.drawable.selector_judge_item);
                setShadow(Color.parseColor("#805500"),Color.parseColor("#ffff99"));
                break;
            case WRONG:
                /**
                 * 需求变更
                 * */
//                llParentBg.setBackgroundResource(R.drawable.selector_judge_wrong);
//                setShadow(Color.parseColor("#80334d"),Color.parseColor("#ffe5ee"));
                llParentBg.setBackgroundResource(R.drawable.selector_judge_item);
                setShadow(Color.parseColor("#805500"),Color.parseColor("#ffff99"));
                break;
        }
//        ivChoiceIcon.setVisibility(View.VISIBLE);
//        ivChoiceIcon.setBackgroundResource(R.drawable.judge_correct);
    }

    public void setSelectedHalfRight(){
        setChecked(true);
        llParentBg.setBackgroundResource(R.drawable.selector_judge_item);
        ivChoiceIcon.setVisibility(View.VISIBLE);
        ivChoiceIcon.setBackgroundResource(R.drawable.judge_correct);
    }


    public void setselectedRights(){
        llParentBg.setBackgroundResource(R.drawable.selector_judge_correct);
        setShadow(Color.parseColor("#006666"),Color.parseColor("#33ffff"));
    }

    public void setselecteds(){
        llParentBg.setBackgroundResource(R.drawable.selector_judge_correct);
        setShadow(Color.parseColor("#006666"),Color.parseColor("#33ffff"));
    }

    public void setItemRight(){
        ivChoiceIcon.setVisibility(View.VISIBLE);
        ivChoiceIcon.setBackgroundResource(R.drawable.judge_correct);
    }

    public void setItemWronglist(){
        ivChoiceIcon.setVisibility(View.VISIBLE);
        ivChoiceIcon.setBackgroundResource(R.drawable.judge_wrong);
    }

    /**
     * 设置选中状态
     */
    public void setSelected(){
        setChecked(true);
        switch (judgeType) {
            case RIGHT:
                llParentBg.setBackgroundResource(R.drawable.selector_judge_correct);
                setShadow(Color.parseColor("#006666"),Color.parseColor("#33ffff"));
                break;
            case WRONG:
                /**2017.03.07 14:58 按产品wiki文档修改*/
                llParentBg.setBackgroundResource(R.drawable.selector_judge_correct);
                setShadow(Color.parseColor("#006666"),Color.parseColor("#33ffff"));
//                llParentBg.setBackgroundResource(R.drawable.selector_judge_wrong);
//                setShadow(Color.parseColor("#80334d"),Color.parseColor("#ffe5ee"));
                break;
        }
    }

    /**
     * 设置未选中状态
     */
    public void setUnSelected(){
        setChecked(false);
        llParentBg.setBackgroundResource(R.drawable.selector_judge_item);
        setShadow(Color.parseColor("#805500"),Color.parseColor("#ffff99"));
    }

    public void setCallBack(SetAnswerCallBack callBack){
        this.callBack=callBack;
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
            if (callBack!=null){
                callBack.callback();
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
    }
}
