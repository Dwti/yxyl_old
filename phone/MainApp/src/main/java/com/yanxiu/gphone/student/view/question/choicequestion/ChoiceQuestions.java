package com.yanxiu.gphone.student.view.question.choicequestion;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.inter.AnswerCallback;
import com.yanxiu.gphone.student.inter.SetAnswerCallBack;
import com.yanxiu.gphone.student.utils.QuestionUtils;
import com.yanxiu.gphone.student.view.question.QuestionsListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/10.
 * 选择题 View
 */
public class ChoiceQuestions extends LinearLayout implements ChoiceQuestionsItem.OnChoicesItemClickListener,QuestionsListener {

    private Context mContext;

    private boolean isSingleChoice;

    public QuestionsListener listener;

    public AnswerCallback callback;
    public int position;


    //本地答案
    private AnswerBean bean;

    //正确答案集
    private List<String> answer;

    private boolean isResolution = false;

    private boolean isClick = true;

    private boolean isWrongSet = false;

    private String selectType;
    private List<String> mixList;
    private List<String> halfList;
    private List<String> wrongList;

    private SetAnswerCallBack answerCallBack;

    public void setIsWrongSet(boolean isWrongSet) {
        this.isWrongSet = isWrongSet;
    }

    //单选
    public static final int CHOICE_SINGLE_TYPE = 1;
    //多选
    public static final int CHOICE_MULTI_TYPE = 2;



//    private ArrayList<ChoiceQuestionsItem> viewList;
//    public class ChoiceType implements Serializable {
//
//
//    }

    public ChoiceQuestions(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public ChoiceQuestions(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public ChoiceQuestions(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    public void initView(){
        this.setOrientation(VERTICAL);
    }

    public boolean isClick() {
        return isClick;
    }

    public void setIsClick(boolean isClick) {
        this.isClick = isClick;
    }

    public void setCallback(SetAnswerCallBack answerCallBack){
        this.answerCallBack=answerCallBack;
    }

    /**
     * 设置数据源
     */
    public void setAllDataSources(QuestionEntity entity){
        if(entity != null && entity.getContent() != null && entity.getContent().getChoices() != null){
            answer = entity.getAnswer();
            int cotentCount = entity.getContent().getChoices().size();
            for(int i = 0; i < cotentCount; i++){
                final ChoiceQuestionsItem item = new ChoiceQuestionsItem(mContext);
                item.setChoicesItemClickListener(this);
                item.setCallback(answerCallBack);
                item.setItemId(i);
                item.setOnItemClick(isClick());
                item.setItemContentText(entity.getContent().getChoices().get(i));
                this.addView(item);


                //由于5.0以下的。9图改变监听不到填充区域，所以设置高度为屏幕的高度
                item.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        item.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        item.setLineHeight();

                    }
                });
            }
        }
    }

    public void setclearFocuse(){
        int count=this.getChildCount();
        for (int i=0;i<count;i++){
            if (this.getChildAt(i) instanceof ChoiceQuestionsItem){
                ((ChoiceQuestionsItem)this.getChildAt(i)).setOnItemClick(isClick());
            }
        }
    }

    /**
     * test log list
     * @param list
     * @param flag
     */
    private void logList(List<String> list, String flag){
        if(list != null){
            int count = list.size();
            for(int i = 0; i < count; i++){
                LogInfo.log("geny", "answer----" + flag + list.get(i));
            }
        }
    }

    /**
     * 是否是解析界面
     * @param isResolution
     */
    public void setIsResolution(boolean isResolution) {
        this.isResolution = isResolution;
    }

    @Override
    public void flipNextPager(QuestionsListener listener) {
        this.listener = listener;
        LogInfo.log("geny", "ChoiceQuestions flipNextPager");
    }

    public void setAnswerCallback(int position,AnswerCallback callback){
        this.position=position;
        this.callback=callback;
    }

    /**
     * 设置数据源
     * @param bean
     */
    @Override
    public void setDataSources(AnswerBean bean) {
        this.bean = bean;
        if(!isWrongSet){
            initViewWithData(bean);
        }
    }

    /**
     * 每次 fragment onresume时保存添加答案
     * @param bean
     */
    @Override
    public void initViewWithData(AnswerBean bean) {
        if(isSingleChoice){
            selectType = bean.getSelectType();

        }else{
            mixList = mixList(answer, bean.getMultiSelect());
            halfList = removeMixList(answer, mixList);
            wrongList = removeMixList(bean.getMultiSelect(), mixList);
        }
        int count = this.getChildCount();
        for(int i = 0; i < count; i++){
            View view = getChildAt(i);
            //单选题时
            if (isSingleChoice) {
                //取本地答案 做对比
                if(!TextUtils.isEmpty(selectType) && selectType.equals(((ChoiceQuestionsItem) view).getSelectType())){
                    //是题目解析时  画对错界面  答题界面画选中状态
                    if(isResolution){
                        if(!bean.isRight()){
                            ((ChoiceQuestionsItem) view).setSelectedWrong();
                        }else{
                            ((ChoiceQuestionsItem) view).setSelectedRight();
                        }
                    }else{
                        if(!isWrongSet){
                            ((ChoiceQuestionsItem) view).setSelected();
                        }
                    }
                }
                //解析的时候画出正确答案  //取本正确 做对比
                if(isResolution){
                    if(answer != null && !answer.isEmpty()){
                        int singleCount = answer.size();
                        for(int k = 0; k < singleCount; k++){
                            if(answer.get(k).equals(((ChoiceQuestionsItem) view).getSelectType())){
                                if(!bean.isRight()){
                                    //没作答案 颜色是半绿的
                                    ((ChoiceQuestionsItem) view).setSelectedHalfRight();
                                }else{
                                    //完成了 就是绿色的
                                    ((ChoiceQuestionsItem) view).setSelectedRight();
                                }
                            }
                        }
                    }
                }else if(isWrongSet){
                    if(answer != null && !answer.isEmpty()){
                        int singleCount = answer.size();
                        for(int k = 0; k < singleCount; k++){
                            if(answer.get(k).equals(((ChoiceQuestionsItem) view).getSelectType())){
                                ((ChoiceQuestionsItem) view).setSelectedRight();
                            }
                            if (bean.getFillAnswers()!=null&&bean.getFillAnswers().size()>0) {
                                if (bean.getFillAnswers().get(0).equals(((ChoiceQuestionsItem) view).getSelectType())) {
                                    if (!bean.getFillAnswers().get(0).equals(answer.get(k))) {
                                        ((ChoiceQuestionsItem) view).setSelectedWrong();
                                    }
                                }
                            }
                        }
                    }
                }
            }else{
                if(bean.getMultiSelect() != null){
                    if(isResolution){
                        if(answer != null && !answer.isEmpty()){

                            /**
                             * 正确答案没有选择 是半绿颜色
                             * list1  a b c 正确答案
                             * list2  b c d 本地答案
                             * b c 对
                             * a 半对
                             * d 错
                             */
                            logList(mixList, "交集答案");
                            int multiCount = mixList.size();
                            for(int j = 0; j < multiCount; j++){
                                if(mixList.get(j).equals(((ChoiceQuestionsItem) view).getSelectType())){
                                    ((ChoiceQuestionsItem) view).setSelectedRight();
                                }
                            }


                            logList(halfList, "halfList");
                            multiCount = halfList.size();
                            for(int j = 0; j < multiCount; j++){
                                if(halfList.get(j).equals(((ChoiceQuestionsItem) view).getSelectType())) {
                                    ((ChoiceQuestionsItem) view).setSelectedHalfRight();
                                }
                            }


                            logList(wrongList, "wrongList");
                            multiCount = wrongList.size();
                            for(int j = 0; j < multiCount; j++){
                                if(wrongList.get(j).equals(((ChoiceQuestionsItem) view).getSelectType())){
                                    ((ChoiceQuestionsItem) view).setSelectedWrong();
                                }
                            }


                        }
                    }else if(isWrongSet){
                        int multiCount = answer.size();
                        for(int j = 0; j < multiCount; j++){
                            if(answer.get(j).equals(((ChoiceQuestionsItem) view).getSelectType())){
                                ((ChoiceQuestionsItem) view).setSelectedRight();
                            }
                        }
                    }else{
                        if(bean.getMultiSelect().contains(((ChoiceQuestionsItem) view).getSelectType())){
                            ((ChoiceQuestionsItem) view).setSelected();
                        }
                    }
                }else {
                    LogInfo.log("geny", "mutil answer---- null");
                }
            }
        }
    }

    @Override public void answerViewClick() {

    }

    /**
     * 两个list的 交集 画绿色
     * @param list1 a b c
     * @param list2 b c d
     * @return b c
     */
    private List<String> mixList(List<String> list1, List<String> list2){
        List<String> tempList1 = new ArrayList<String>();
        tempList1.addAll(list1);
        List<String> tempList2 = new ArrayList<String>();
        tempList2.addAll(list2);

        tempList2.retainAll(tempList1);
        return tempList2;
    }


    /**
     * 正确答案没有选择 是半绿颜色
     * @param list1  a b c 正确答案
     * @param list2  b c d 本地答案
     * @return  a
     */
    private List<String> removeMixList(List<String> list1, List<String> list2){
        List<String> tempList1 = new ArrayList<String>();
        tempList1.addAll(list1);
        List<String> tempList2 = new ArrayList<String>();
        tempList2.addAll(list2);

        tempList1.removeAll(tempList2);
        return tempList1;
    }

//    /**
//     * 本地答案选择非正确答案 是红色
//     * @param list1  a b c
//     * @param list2  b c d
//     * @return  d
//     */
//    private List<String> getRightList(List<String> list1, List<String> list2){
//        List<String> tempList1 = new ArrayList<String>();
//        tempList1.addAll(list1);
//        List<String> tempList2 = new ArrayList<String>();
//        tempList2.addAll(list2);
//
//        tempList2.remove(tempList1);
//        return tempList2;
//    }

//    /**
//     * list 并集
//     * @param list1 a b c
//     * @param list2 b c d
//     * @return a b c d
//     */
//    private List<String> unionList(List<String> list1, List<String> list2){
//        List<String> tempList1 = new ArrayList<String>();
//        tempList1.addAll(list1);
//        List<String> tempList2 = new ArrayList<String>();
//        tempList2.addAll(list2);
//
//        tempList1.removeAll(tempList2);
//        tempList2.addAll(tempList1);
//        return tempList2;
//    }


    /**
     * 设置选择类型
     * @param type 多选 单选
     */
    public void setChoicesType(int type){
        switch (type) {
            case CHOICE_SINGLE_TYPE:
                initSingle();
                break;
            case CHOICE_MULTI_TYPE:
                initMulti();
                break;
        }
    }


    @Override
    public void choicesItemClickListener(ChoiceQuestionsItem choiceQuestionsItem) {


        int count = this.getChildCount();
        for(int i = 0; i < count; i++){
            View view = getChildAt(i);
            //单选题
            if(view != null && view instanceof ChoiceQuestionsItem && view != null && isSingleChoice){
                //点击选中的item
                if(((ChoiceQuestionsItem) view).isChecked() && view == choiceQuestionsItem){
                    bean.setSelectType(((ChoiceQuestionsItem) view).getSelectType());
                    if (callback!=null){
                        callback.answercallback(position,((ChoiceQuestionsItem) view).getSelectType());
                    }
                    if(listener != null){
                        listener.flipNextPager(listener);
                    }
                }else{
                    //其他的item置为 未选中
                    ((ChoiceQuestionsItem) view).setUnSelected();
                    if (view == choiceQuestionsItem&&callback!=null){
                        callback.answercallback(position,"-1");
                    }
                }
            }

            if(view == choiceQuestionsItem && bean.getMultiSelect() != null){
                if(((ChoiceQuestionsItem) view).isChecked()){
                    //选中的item 本地里没有就加入 未选中有就移除
                    if(!bean.getMultiSelect().contains(((ChoiceQuestionsItem) view).getSelectType())){
                        bean.getMultiSelect().add(((ChoiceQuestionsItem) view).getSelectType());
                    }
                }else{
                    if (bean.getMultiSelect().contains(((ChoiceQuestionsItem) view).getSelectType())) {
                        bean.getMultiSelect().remove(((ChoiceQuestionsItem) view).getSelectType());
                    }
                }
            }

        }
        //单选题中处理 本地答案
        if (isSingleChoice) {
            if (choiceQuestionsItem.isChecked()) {
                bean.setSelectType(choiceQuestionsItem.getSelectType());
                bean.setIsFinish(true);
                if(answer != null && !answer.isEmpty()){
//                    Util.showToast("选择选项-----" + choiceQuestionsItem.getSelectType());
                    if(answer.get(0).contains(choiceQuestionsItem.getSelectType())){
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
        }else{
            //多选题中有一道是错误的 为错
            if (bean.getMultiSelect() != null && bean.getMultiSelect().size() != 0){
                bean.setIsFinish(true);
                boolean isRight = true;
                if(answer != null && !answer.isEmpty()){
                    isRight = QuestionUtils.compareCollection(bean.getMultiSelect(), answer);
                    bean.setIsRight(isRight);
                }
            }else{
                bean.setIsRight(false);
                bean.setIsFinish(false);
            }
        }

    }



    /**
     * 多选的初始化
     */
    private void initMulti() {
        isSingleChoice = false;
    }


    /**
     * 单选的初始化
     */
    private void initSingle() {
        isSingleChoice = true;
    }
}
