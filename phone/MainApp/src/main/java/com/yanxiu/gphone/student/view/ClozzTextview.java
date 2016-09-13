package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;


import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.StringUtils;
import com.yanxiu.gphone.student.HtmlParser.Interface.ImageSpanOnclickListener;
import com.yanxiu.gphone.student.HtmlParser.MyHtml;
import com.yanxiu.gphone.student.HtmlParser.MyImageSpanLinkMovementMethod;
import com.yanxiu.gphone.student.HtmlParser.UilImageGetter;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.PaperTestEntity;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.view.question.QuestionsListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public class ClozzTextview extends TextView implements ImageSpanOnclickListener ,QuestionsListener {

    private Context context;
    private float textsize=16;
    private int linespacing=10;
    private List<Buttonbean> list=new ArrayList<Buttonbean>();
    protected QuestionEntity questionsEntity;
    private int position_index;
    private int question_position;
    private List<String> answers = new ArrayList<String>();
    private QuestionPositionSelectListener selectListener;
    private AnswerBean bean;

    public ClozzTextview(Context context) {
        this(context,null);
    }

    public ClozzTextview(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClozzTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context=context;
        this.setTextSize(textsize);
        this.setLineSpacing(linespacing,1);
        this.setMovementMethod(MyImageSpanLinkMovementMethod.getInstance());
        this.setHighlightColor(context.getResources().getColor(android.R.color.transparent));
    }

    public void setData(String text){
        text=text.replaceAll("\\(_\\)"," <clozz>kaf</clozz> ");
//        text=text.replaceAll("\\(_\\)","");
        int position=text.split("<clozz>").length;
        for (int i=0;i<position-1;i++){
            Buttonbean buttonbean=new Buttonbean();
            buttonbean.setId(i);
            question_position++;
            buttonbean.setQuestion_id(question_position);
            buttonbean.setText("");
            buttonbean.setTextsize(textsize);
            if (i==0){
                buttonbean.setSelect(true);
            }
            list.add(buttonbean);
            setAnswers_cache(buttonbean,i);
        }
        UilImageGetter imageGetter = new UilImageGetter(this, context);
        Spanned spanned = MyHtml.fromHtml(context,text,imageGetter,null,list,this);
        this.setText(spanned);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void setAnswers_cache(Buttonbean buttonbean, int i){
        if (bean.getFillAnswers().size()>i){
            String answer=bean.getFillAnswers().get(i);
            setText(buttonbean,answer);
        }else {
            if (position_index==0){
                bean.getFillAnswers().add("");
            }else {
                List<PaperTestEntity> list=questionsEntity.getChildren();
                String select=list.get(i).getQuestions().getAnswerBean().getSelectType();
                if (!TextUtils.isEmpty(select)){
                    bean.getFillAnswers().add(select);
                }else {
                    bean.getFillAnswers().add("");
                }
            }

        }
    }

    public void setQuestionsEntity(QuestionEntity questionsEntity, int position_index){
        this.questionsEntity=questionsEntity;
        this.position_index=position_index;
        if (position_index==-1){
            question_position=questionsEntity.getChildren().get(0).getQuestions().getPositionForCard();
        }else {
            question_position=position_index;
        }
    }

    /**
     * 设置标准答案
     */
    public void setAnswers(List<String> answerData) {
        if (answerData != null) {
            answers.addAll(answerData);
        }
    }

    public void setAnswersToPosition(int position,String answer){
        Buttonbean buttonbean=list.get(position);
        ArrayList<String> answer_list=bean.getFillAnswers();
        if (answer_list.size()<position){

        }else {
            answer_list.remove(position);
            answer_list.add(position,answer);
            setText(buttonbean,answer);
            invalidate();
        }
    }

    private void setText(Buttonbean buttonbean,String answer){
//        String text="";
        switch (answer){
            case "0":
//                text=(int)textView.getTag(PAGER)+1+".A";
                buttonbean.setText("A");
                break;
            case "1":
//                text=(int)textView.getTag(PAGER)+1+".B";
                buttonbean.setText("B");
                break;
            case "2":
//                text=(int)textView.getTag(PAGER)+1+".C";
                buttonbean.setText("C");
                break;
            case "3":
//                text=(int)textView.getTag(PAGER)+1+".D";
                buttonbean.setText("D");
                break;
            default:
//                text=(int)textView.getTag(PAGER)+1+"";
                buttonbean.setText("");
                break;
        }
//        textView.setText(text);
    }

    public void setTextViewSelect(int position){
        Buttonbean buttonbean= list.get(position);
//        buttonbean.setText("便");
        for (Buttonbean bean:list){
            if (bean.getId()==buttonbean.getId()){
                bean.setSelect(true);
            }else {
                bean.setSelect(false);
            }
        }
        invalidate();
    }

    /**
     * 存储答案
     * 按照TextView的个数挨个存储答案
     * 答案为字符串  如果没有填写答案则为"" ，add进答案list
     * 当用户返回来继续做题时，由于答案list里已经存在了，所以直接替换
     * 如果其中一个TextView的答案不为null，则认为用户已经答了这道题
     */
    public void saveAnswers() {
        if (list!=null&&list.size()>0){
            int fillCount = list.size();
            int answerCount = bean.getFillAnswers().size();
            bean.setIsFinish(false);
            List<PaperTestEntity> list=questionsEntity.getChildren();
            for (int i = 0; i < fillCount; i++) {
                String fillAnswer = "";
                if (list!=null) {
                    if (!StringUtils.isEmpty(list.get(i).getQuestions().getAnswerBean().getSelectType())) {
                        fillAnswer = list.get(i).getQuestions().getAnswerBean().getSelectType();
                        bean.setIsFinish(true);
                    }
                }
                if (answerCount == fillCount) {
                    bean.getFillAnswers().set(i, fillAnswer);
                } else {
                    bean.getFillAnswers().add(fillAnswer);
                }
            }
            bean.setIsRight(judgeAnswerIsRight());
        }
    }

    /**
     * 判断答案是否正确
     * 因为存储的时候已经按顺序存储了
     * 所以只需要挨个比较标准答案及用户填写的答案即可
     */
    private boolean judgeAnswerIsRight() {
        ArrayList<String> myAnswers = bean.getFillAnswers();
        return CommonCoreUtil.compare(myAnswers, answers);
    }

    public interface QuestionPositionSelectListener{
        void QuestionPosition(Buttonbean buttonbean);
    }

    public void setListener(QuestionPositionSelectListener listener){
        this.selectListener=listener;
    }

    @Override
    public void onClick(Object flag) {
        Buttonbean buttonbean= (Buttonbean) flag;
//        buttonbean.setText("便");
        for (Buttonbean bean:list){
            if (bean.getId()==buttonbean.getId()){
                bean.setSelect(true);
            }else {
                bean.setSelect(false);
            }
        }
        invalidate();
        if (selectListener!=null){
            selectListener.QuestionPosition(buttonbean);
        }
    }

    @Override
    public void flipNextPager(QuestionsListener listener) {

    }

    @Override
    public void setDataSources(AnswerBean bean) {
        this.bean = bean;
    }

    @Override
    public void initViewWithData(AnswerBean bean) {

    }

    @Override
    public void answerViewClick() {

    }

    public class Buttonbean{
        private int id;
        private int question_id;
        private String text;
        private float textsize;
        private boolean select;

        public float getTextsize() {
            return textsize;
        }

        public void setTextsize(float textsize) {
            this.textsize = textsize;
        }

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getQuestion_id() {
            return question_id;
        }

        public void setQuestion_id(int question_id) {
            this.question_id = question_id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
