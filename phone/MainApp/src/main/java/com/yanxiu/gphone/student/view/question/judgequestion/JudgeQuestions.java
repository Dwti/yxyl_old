package com.yanxiu.gphone.student.view.question.judgequestion;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.inter.SetAnswerCallBack;
import com.yanxiu.gphone.student.view.question.QuestionsListener;

import java.util.List;

/**
 * Created by Administrator on 2015/7/10.
 * 判断题 View
 */
public class JudgeQuestions extends LinearLayout implements JudgeQuestionsItem.OnChoicesItemClickListener,QuestionsListener {
    public static final int NO_0 = 0;
    public static final int NO_1 = 1;
    public static final int NO_2 = 2;

    private Context mContext;

    private SetAnswerCallBack callBack;
    private QuestionsListener listener;
    private AnswerBean bean;

    private List<String> answer;

    private boolean isResolution = false;

    private boolean isWrongSet = false;

    private boolean isClick = true;

    public enum JUDGE_TYPE{

        RIGHT(1),
        WRONG(0);

        private int key;

        JUDGE_TYPE(int key) {
            this.key = key;
        }
    }


    public JudgeQuestions(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public JudgeQuestions(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public JudgeQuestions(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    public void initView(){
        this.setOrientation(HORIZONTAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }


    public void setCallBack(SetAnswerCallBack callBack){
        this.callBack=callBack;
    }

    /**
     * 设置数据源
     */
    public void setDataSources(){

        for(int i = 0; i < NO_2; i++){
            JudgeQuestionsItem item = new JudgeQuestionsItem(mContext);
            item.setChoicesItemClickListener(this);
            if(i == NO_0){
                item.setDrawableNormalId(R.drawable.judge_correct);
                item.setDrawableSelectId(R.drawable.judge_correct);
//                item.setSelectedBg(R.drawable.selector_judge_correct);
                item.setItemText(mContext.getResources().getString(R.string.correct));
                item.setJudgeType(JUDGE_TYPE.RIGHT);
//                item.setOnItemClick(isClick());
                item.setSelectType(String.valueOf(JUDGE_TYPE.RIGHT.key));
            }else if(i == NO_1){
                item.setDrawableNormalId(R.drawable.judge_wrong);
                item.setDrawableSelectId(R.drawable.judge_wrong);
//                item.setSelectedBg(R.drawable.selector_judge_wrong);
                item.setItemText(mContext.getResources().getString(R.string.wrong));
                item.setJudgeType(JUDGE_TYPE.WRONG);
//                item.setOnItemClick(isClick());
                item.setSelectType(String.valueOf(JUDGE_TYPE.WRONG.key));
            }
            item.setCallBack(callBack);
            item.setClickable(isClick());
            item.setUnSelected();
            this.addView(item);
            setChildLayoutParams(item);
            setLayoutParams(item);
        }
    }

    public void setFoucesClear(){
        int count=this.getChildCount();
        for (int i=0;i<count;i++){
            if (this.getChildAt(i) instanceof JudgeQuestionsItem) {
                this.getChildAt(i).setClickable(isClick());
            }
        }
    }

    private void setChildLayoutParams(JudgeQuestionsItem item){
        LinearLayout.LayoutParams params = (LayoutParams) item.getLayoutParams();
        params.weight = NO_1;
        params.width = LayoutParams.MATCH_PARENT;
        item.setLayoutParams(params);
    }

    public void setIsResolution(boolean isResolution) {
        this.isResolution = isResolution;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setIsClick(boolean isClick) {
        this.isClick = isClick;
    }

    private void setLayoutParams(JudgeQuestionsItem item){
        LinearLayout.LayoutParams params = (LayoutParams) item.getLayoutParams();
        params.weight = NO_1;
        params.width = LayoutParams.MATCH_PARENT;
        params.height = LayoutParams.MATCH_PARENT;
        item.setLayoutParams(params);
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }

    @Override
    public void choicesItemClickListener(JudgeQuestionsItem judgeQuestionsItem) {
        int count = this.getChildCount();
        for(int i = 0; i < count; i++){
            View view = getChildAt(i);
            if(view != null && view instanceof JudgeQuestionsItem && judgeQuestionsItem != null){
                if(((JudgeQuestionsItem) view).isChecked() && view == judgeQuestionsItem){
                    if(listener != null){
                        listener.flipNextPager(listener);
                    }
                }else{
                    ((JudgeQuestionsItem) view).setUnSelected();
                }
            }
        }
        if (judgeQuestionsItem.isChecked()) {
            bean.setSelectType(judgeQuestionsItem.getSelectType());
//            Util.showToast("选择选项-----" + judgeQuestionsItem.getSelectType());
            bean.setIsFinish(true);
            if(answer != null && !answer.isEmpty()){
                if(answer.get(0).contains(judgeQuestionsItem.getSelectType())){
                    bean.setIsRight(true);
                }else{
                    bean.setIsRight(false);
                }
            }
        } else {
            bean.setIsRight(false);
            bean.setSelectType(null);
            bean.setIsFinish(false);
        }
    }

    @Override
    public void flipNextPager(QuestionsListener listener) {
        this.listener = listener;
    }

    @Override
    public void setDataSources(AnswerBean bean) {
        this.bean = bean;
        if(!isWrongSet){
            initViewWithData(bean);
        }
    }

    public boolean isWrongSet() {
        return isWrongSet;
    }

    public void setIsWrongSet(boolean isWrongSet) {
        this.isWrongSet = isWrongSet;
    }

    @Override
    public void initViewWithData(AnswerBean bean) {
        String selectType = bean.getSelectType();
//            Util.showToast("选择选项-----" + selectType);
        int count = this.getChildCount();
        for(int i = 0; i < count; i++) {
            View view = getChildAt(i);
            JUDGE_TYPE judgeType = ((JudgeQuestionsItem) view).getJudgeType();
            boolean isAnswerDone = false;
            // 判断用户是否做题 没做过直接画出正确答案 做过了在此item上做判断
            switch (judgeType) {
                case RIGHT:
                    isAnswerDone = setCorrectAnswer(selectType, JUDGE_TYPE.RIGHT.key, view);
                    break;
                case WRONG:
                    isAnswerDone = setCorrectAnswer(selectType, JUDGE_TYPE.WRONG.key, view);
                    break;
                default:
                    ((JudgeQuestionsItem) view).setUnSelected();
                    break;
            }
//            if(isAnswerDone){
//                return;
//            }

        }
        if (isResolution||isWrongSet) {
//            if (false) {
//            if (!isClick) {
                for (int i = 0; i < count; i++) {
                    View view = getChildAt(i);
                    JUDGE_TYPE judgeType = ((JudgeQuestionsItem) view).getJudgeType();
                    // 判断用户是否做题 没做过直接画出正确答案 做过了在此item上做判断
                    String an = "";
                    if (answer != null && answer.size() > 0) {
                        an = answer.get(0);
                    }
                    switch (judgeType) {
                        case RIGHT:
                            if (!TextUtils.isEmpty(selectType) && selectType.equals(JUDGE_TYPE.RIGHT.key + "")) {
                                setAnsRight(view, an, JUDGE_TYPE.RIGHT.key + "");
                            }else if (TextUtils.isEmpty(selectType)){
                                setAnsRight(view, an, JUDGE_TYPE.RIGHT.key + "");
                            }
//                    setHalfCorrectAnswer(JUDGE_TYPE.RIGHT.key, view);
                            break;
                        case WRONG:
                            if (!TextUtils.isEmpty(selectType) && selectType.equals(JUDGE_TYPE.WRONG.key + "")){
                                setAnsRight(view, an, JUDGE_TYPE.WRONG.key + "");
                            }
//                    setHalfCorrectAnswer(JUDGE_TYPE.WRONG.key, view);
                            break;
                    }
//                }
            }
        }
    }

    private void setAnsRight(View view,String ans,String ss){
        if (ss.equals(ans)) {
            ((JudgeQuestionsItem) view).setItemRight();
        }else {
            ((JudgeQuestionsItem) view).setItemWronglist();
        }
    }
    private void setAnsWrong(){

    }


    /**
     * 是否画出判断题的正确答案
     * @param selectType
     * @param key
     * @param view
     */
    private boolean setCorrectAnswer(String selectType, int key, View view){
        if(!TextUtils.isEmpty(selectType) && selectType.equals(String.valueOf(key))){
//            if(isResolution){
//                if(bean.isRight()){
//                    ((JudgeQuestionsItem) view).setSelectedRight();
//                }else{
//                    ((JudgeQuestionsItem) view).setSelectedWrong();
//                }
//                return true;
//            }else{
//                ((JudgeQuestionsItem) view).setSelected();
//            }
//            if (key==1){
//                ((JudgeQuestionsItem) view).setselectedRights();
//            }else {
//                ((JudgeQuestionsItem) view).setselectedWrongs();
//            }
            ((JudgeQuestionsItem) view).setselecteds();
        }else {
            if (key==1){
                ((JudgeQuestionsItem) view).setDefult();
            }else {
                ((JudgeQuestionsItem) view).setDefult();
            }
        }
//        else if(isWrongSet && answer != null && !answer.isEmpty() && answer.get(0).contains(String.valueOf(key))){
//            ((JudgeQuestionsItem) view).setSelectedRight();
//        }
        return false;
    }


    /**
     * 画出用户没有做题，但是正确的答案
     * @param key
     * @param view
     */
    private void setHalfCorrectAnswer(int key, View view){
        if(isResolution && answer != null && !answer.isEmpty() && answer.get(0).contains(String.valueOf(key))){
            if(bean.isRight()){
                ((JudgeQuestionsItem) view).setSelectedRight();
            }else{
                ((JudgeQuestionsItem) view).setSelectedHalfRight();
            }
        }
    }


    @Override public void answerViewClick() {

    }
}
