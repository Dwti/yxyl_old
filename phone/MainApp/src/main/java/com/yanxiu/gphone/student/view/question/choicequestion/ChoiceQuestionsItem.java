package com.yanxiu.gphone.student.view.question.choicequestion;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.yanxiu.gphone.student.R;
import com.common.core.utils.CommonCoreUtil;
import com.yanxiu.gphone.student.inter.SetAnswerCallBack;
import com.yanxiu.gphone.student.view.YanxiuTypefaceTextView;
import com.yanxiu.gphone.student.view.question.AbsChoiceQuestionsItem;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;

/**
 * Created by Administrator on 2015/7/10.
 * 选择题 item
 */
public class ChoiceQuestionsItem extends AbsChoiceQuestionsItem implements View.OnClickListener{

    private YanxiuTypefaceTextView tvItemText;
    private YXiuAnserTextView tvItemContentText;

    private FrameLayout flItemBg;
    private FrameLayout flItemMiddleBg;


    private ImageView ivMiddleIcon;

    private ImageView ivChoiceIconItem;

    private int itemId;

    private OnChoicesItemClickListener choicesItemClickListener;

    private SetAnswerCallBack answerCallBack;

    public ChoiceQuestionsItem(Context context) {
        super(context);
    }

    public ChoiceQuestionsItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChoiceQuestionsItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCallback(SetAnswerCallBack answerCallBack){
        this.answerCallBack=answerCallBack;
    }

    @Override
    public void initView() {
        this.setOnClickListener(this);
        tvItemText = (YanxiuTypefaceTextView) this.findViewById(R.id.tv_choice_text_item);
        tvItemText.setTypefaceName(YanxiuTypefaceTextView.TypefaceType.METRO_BOLD);
        flItemBg = (FrameLayout) this.findViewById(R.id.fl_choice_text_item_bg);
        flItemMiddleBg = (FrameLayout) this.findViewById(R.id.view_middle_content);
        tvItemContentText = (YXiuAnserTextView) this.findViewById(R.id.tv_item_content_text);
        ivChoiceIconItem = (ImageView) this.findViewById(R.id.iv_choice_icon_item);
        ivMiddleIcon = (ImageView) this.findViewById(R.id.iv_middle_icon);
    }

    @Override
    public View getView() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.choice_question_item;
    }

    /**
     */
    public void setSelectedWrong(){
        setChecked(true);
        ivChoiceIconItem.setVisibility(View.VISIBLE);
        ivChoiceIconItem.setImageResource(R.drawable.answer_wrong);
        flItemBg.setBackgroundResource(R.drawable.answer_left_bg_pre);
        flItemMiddleBg.setBackgroundResource(R.drawable.answer_middle_bg_pre);
        tvItemText.setTextColor(mCtx.getResources().getColor(R.color.color_white));
    }

    /**
     */
    public void setSelectedRight(){
        setChecked(true);
        ivChoiceIconItem.setVisibility(View.VISIBLE);
        ivChoiceIconItem.setImageResource(R.drawable.answer_correct);
        flItemBg.setBackgroundResource(R.drawable.answer_left_bg_pre);
        flItemMiddleBg.setBackgroundResource(R.drawable.answer_middle_bg_pre);
        tvItemText.setTextColor(mCtx.getResources().getColor(R.color.color_white));
    }


    public void setSelectedHalfRight(){
        setChecked(true);
        ivChoiceIconItem.setVisibility(View.VISIBLE);
        ivChoiceIconItem.setImageResource(R.drawable.answer_correct);
        flItemBg.setBackgroundResource(R.drawable.answer_left_bg_nor);
        flItemMiddleBg.setBackgroundResource(R.drawable.answer_middle_bg_nor);
        tvItemText.setTextColor(mCtx.getResources().getColor(R.color.color_00cccc));
    }

    /**
     * 设置选中状态
     */
    public void setSelected(){
        setChecked(true);
        flItemBg.setBackgroundResource(R.drawable.answer_left_bg_pre);
        flItemMiddleBg.setBackgroundResource(R.drawable.answer_middle_bg_pre);
        tvItemText.setTextColor(mCtx.getResources().getColor(R.color.color_white));
    }

    /**
     * 设置未选中状态
     */
    public void setUnSelected(){
        setChecked(false);
        flItemBg.setBackgroundResource(R.drawable.answer_left_bg_nor);
        flItemMiddleBg.setBackgroundResource(R.drawable.answer_middle_bg_nor);
        tvItemText.setTextColor(mCtx.getResources().getColor(R.color.color_00cccc));
    }


    /**
     * 选择题每个item的点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        if(isClick()){
            if(isChecked()){
                setUnSelected();
            }else{
                setSelected();
            }
            refreshDrawableState();
            if(choicesItemClickListener != null){
                choicesItemClickListener.choicesItemClickListener(this);
            }
            if (answerCallBack!=null){
                answerCallBack.callback();
            }
        }
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

    public void setLineHeight() {
        FrameLayout.LayoutParams ivLp = (FrameLayout.LayoutParams) ivMiddleIcon.getLayoutParams();
        ivLp.height = CommonCoreUtil.getScreenHeight();;
        ivMiddleIcon.requestLayout();
    }

    public interface OnChoicesItemClickListener{
        void choicesItemClickListener(ChoiceQuestionsItem choiceQuestionsItem);
    }
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
        selectTpye = String.valueOf(itemId);
        tvItemText.setText(String.valueOf(numToLetter(String.valueOf(itemId))));
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


    @Override
    protected boolean isClick() {
        return isClick;
    }

    @Override
    protected String  getSelectType() {
        return selectTpye;
    }

    @Override
    protected void setSelectType(String type) {
        selectTpye = type;
    }

    @Override
    protected void setItemContentText(String str) {
        if(!TextUtils.isEmpty(str)){
            tvItemContentText.setTextHtml(str);
        }
    }
}
