package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
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

    private static final char SBC_CHAR_START = 65281;
    private static final char SBC_CHAR_END = 65374;
    private static final int CONVERT_STEP = 65248;
    private static final char SBC_SPACE = 12288;
    private static final char DBC_SPACE = ' ';

    private Context context;
    private float textsize=32;
    private int linespacing=10;

    public List<Buttonbean> getList() {
        return list;
    }

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
//        this.setTextSize(textsize);
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, textsize);
        this.setLineSpacing(linespacing,1);
        this.setMovementMethod(MyImageSpanLinkMovementMethod.getInstance());
        this.setHighlightColor(context.getResources().getColor(android.R.color.transparent));
    }

    public void setData(String text){
        text=full2half(text);
//        text=text.replaceAll(" ","&nbsp;");
        text=text.replaceAll("&nbsp;"," ");
        text=text.replaceAll("\\(_\\)","<clozz>k</clozz>");
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

    private String full2half(String src) {
        if (src == null) {
            return src;
        }
        StringBuilder buf = new StringBuilder(src.length());
        char[] ca = src.toCharArray();
        for (int i = 0; i < src.length(); i++) {
            if (ca[i] >= SBC_CHAR_START && ca[i] <= SBC_CHAR_END) {
                buf.append((char) (ca[i] - CONVERT_STEP));
            } else if (ca[i] == SBC_SPACE) {
                buf.append(DBC_SPACE);
            } else {
                buf.append(ca[i]);
            }
        }
        return buf.toString();
    }

    public interface onDrawSucessListener{
        void onsucess();
    }
    private onDrawSucessListener sucessListener;
    public void setOnDrawSucessListener(onDrawSucessListener sucessListener){
        this.sucessListener=sucessListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (sucessListener!=null) {
            sucessListener.onsucess();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setTextColor(){
//        if (answers.size()!=list.size()){
//            return;
//        }
        List<PaperTestEntity> children_list=questionsEntity.getChildren();
        for (int i=0;i<children_list.size();i++){
            String ans=children_list.get(i).getQuestions().getAnswer().get(0);
            Buttonbean bean=list.get(i);
//            if (!TextUtils.isEmpty(bean.getText())) {
                if (bean.getText().equals(ans)) {
                    bean.setTextcolor(Buttonbean.COLOR_CORRECT);
                } else {
                    bean.setTextcolor(Buttonbean.COLOR_ERROR);
                }
//            }
        }
        this.invalidate();
    }

    private void setAnswers_cache(Buttonbean buttonbean, int i){
        if (bean.getFillAnswers().size()>i){
            String answer=bean.getFillAnswers().get(i);
            setText(buttonbean,answer);
        }else {
            if (position_index==0){
                bean.getFillAnswers().add("");
                buttonbean.setText("");
            }else {
                List<PaperTestEntity> list=questionsEntity.getChildren();
                String select=list.get(i).getQuestions().getAnswerBean().getSelectType();
                if (!TextUtils.isEmpty(select)){
                    bean.getFillAnswers().add(select);
                    setText(buttonbean,select);
                }else {
                    bean.getFillAnswers().add("");
                    setText(buttonbean,"");
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

    @Override
    public void invalidate() {
        super.invalidate();
    }

    private void setText(Buttonbean buttonbean, String answer){
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
        //形势所致，只能改为这样
        List<PaperTestEntity> children_list=questionsEntity.getChildren();
        answers.clear();
        for (int i=0;i<children_list.size();i++){
            String ans=children_list.get(i).getQuestions().getAnswer().get(0);
            answers.add(ans);
        }
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

        /**
         * 正确时的错题颜色
         * */
        public static final String COLOR_CORRECT="#00cccc";
        /**
         * 错误时的错题颜色
         * */
        public static final String COLOR_ERROR="#ff80aa";

        private int id;
        private int question_id;
        private String text="";
        private String textcolor="#000000";
        private float textsize;
        private boolean select;
        private int y;

        public String getTextcolor() {
            return textcolor;
        }

        public void setTextcolor(String textcolor) {
            this.textcolor = textcolor;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

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
