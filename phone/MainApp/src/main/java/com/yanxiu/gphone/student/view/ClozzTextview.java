package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.TextView;


import com.yanxiu.gphone.student.HtmlParser.Interface.ImageSpanOnclickListener;
import com.yanxiu.gphone.student.HtmlParser.MyHtml;
import com.yanxiu.gphone.student.HtmlParser.MyImageSpanLinkMovementMethod;
import com.yanxiu.gphone.student.HtmlParser.UilImageGetter;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.view.question.QuestionsListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public class ClozzTextview extends TextView implements ImageSpanOnclickListener ,QuestionsListener {

    private Context context;
    private float textsize=20;
    private int linespacing=10;
    private List<Buttonbean> list=new ArrayList<Buttonbean>();
    protected QuestionEntity questionsEntity;
    private int position_index;
    private int question_position;
    private List<String> answers = new ArrayList<String>();

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
        int position=text.split("<clozz>").length;
        for (int i=0;i<position-1;i++){
            Buttonbean buttonbean=new Buttonbean();
            buttonbean.setId(i);
            buttonbean.setQuestion_id(i);
            buttonbean.setText("");
            buttonbean.setTextsize(textsize);
            list.add(buttonbean);
        }
        UilImageGetter imageGetter = new UilImageGetter(this, context);
        Spanned spanned = MyHtml.fromHtml(context,text,imageGetter,null,list,this);
        this.setText(spanned);
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

    @Override
    public void onClick(Object flag) {
        Buttonbean buttonbean= (Buttonbean) flag;
        buttonbean.setText("便");
        for (Buttonbean bean:list){
            if (bean.getId()==buttonbean.getId()){
                bean.setSelect(true);
            }else {
                bean.setSelect(false);
            }
        }
        invalidate();
    }

    @Override
    public void flipNextPager(QuestionsListener listener) {

    }

    @Override
    public void setDataSources(AnswerBean bean) {

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
