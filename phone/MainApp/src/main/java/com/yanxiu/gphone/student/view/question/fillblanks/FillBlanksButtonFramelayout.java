package com.yanxiu.gphone.student.view.question.fillblanks;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.DensityUtils;
import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.PaperTestEntity;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/7/27.
 */
public class FillBlanksButtonFramelayout extends FrameLayout implements
        QuestionsListener, View.OnClickListener {

    private int NUMBERPAGER=R.id.TAG_NUMBERPAGER;
    private int PAGER=R.id.TAG_PAGER;

    private QuestionsListener listener;
    private QuestionPositionSelectListener selectListener;
    protected QuestionEntity questionsEntity;
    private int position_index;
    private AnswerBean bean;
    private List<String> answers = new ArrayList<String>();
    private List<TextView> list_textview = new ArrayList<TextView>();
    private List<String> answers_cache = new ArrayList<String>();
    public Context mCtx;
    private RelativeLayout rlMark;
    private YXiuAnserTextView tvFillBlank;
    private String data = "鲁迅，原名(_)字，他的代表作品是小说集，散文集。\n" +
            "    鲁迅再《琐记》一文中，用了来讥讽洋务(_)派的办学。\n" +
            "    鲁迅写出了中国现代第一篇白话小说(_)，1918年在上发表其后又发表等著名小说。\n";
    private int answerViewTypyBean;
    private float textSize = 32;
    private int yAxisHeight;
    private float offset_width;
    private float textview_width;
    private int question_position;
    private int select_position;

    public FillBlanksButtonFramelayout(Context context) {
        super(context);
        mCtx = context;
        initView();

    }

    public FillBlanksButtonFramelayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCtx = context;
        initView();
    }

    public FillBlanksButtonFramelayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCtx = context;
        initView();
    }

    /**
     * 设置标准答案
     */
    public void setAnswers(List<String> answerData) {
        if (answerData != null) {
            answers.addAll(answerData);
        }
    }

    /**
     * 设置数据源，替换字符
     */
    public void setData(String stem) {
        data = stem + "  \n";
//        data = data + "  \n";
//        data = data.replace("(_)", "________________");_____
        data = data.replace("(_)", "(                 )");
//        data = data.replace("_____", "_______________");
//        tvFillBlank.setTextHtml(data);
        tvFillBlank.setText(data);
//        Log.d("asd","data++++"+data);
        ViewTreeObserver.OnGlobalLayoutListener listener = new MyOnGlobalLayoutListener();
        this.getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }

    private void initView() {
        LogInfo.log("geny", "framelayout_fill_blanks_question initView");
        LayoutInflater.from(mCtx).inflate(
                R.layout.framelayout_fill_blanks_question, this);
        rlMark = (RelativeLayout) this.findViewById(R.id.rl_mark);
        tvFillBlank = (YXiuAnserTextView) this.findViewById(R.id.tv_fill_blanks);
//        int px = DensityUtils.dip2px(mCtx, 15);
//        textSize = DensityUtils.px2sp(mCtx, px);
        tvFillBlank.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        yAxisHeight=getFontHeight(textSize);
        textview_width=(textSize / 2) * 10;
    }

    public void setAnswerViewTypyBean(int answerViewTypyBean) {
        this.answerViewTypyBean = answerViewTypyBean;
    }

    @Override
    public void flipNextPager(QuestionsListener listener) {
        this.listener = listener;
    }

    @Override
    public void setDataSources(AnswerBean bean) {
        this.bean = bean;
        initViewWithData(bean);
    }

    /**
     * 当用户已经做过这道题再次返回来的时候
     * 设置TextView中的答案
     */
    @Override
    public void initViewWithData(AnswerBean bean) {
        if (rlMark != null && bean != null && bean.getFillAnswers().size() > 0) {
            if (bean.getFillAnswers().size() == rlMark.getChildCount()) {
                int fillCount = rlMark.getChildCount();
                for (int i = 0; i < fillCount; i++) {
                    TextView textView=(TextView) rlMark.getChildAt(i);
                    String answer=bean.getFillAnswers().get(i);
                    setText(textView,answer);
                    setTextColor(textView,answer);
                }
            }
        }
    }

    private void setText(TextView textView,String answer){
        String text="";
        switch (answer){
            case "0":
                text=(int)textView.getTag(PAGER)+1+".A";
                break;
            case "1":
                text=(int)textView.getTag(PAGER)+1+".B";
                break;
            case "2":
                text=(int)textView.getTag(PAGER)+1+".C";
                break;
            case "3":
                text=(int)textView.getTag(PAGER)+1+".D";
                break;
            default:
                text=(int)textView.getTag(PAGER)+1+"";
                break;
        }
        textView.setText(text);
    }

    @Override
    public void answerViewClick() {
    }

    public void setQuestionsEntity(QuestionEntity questionsEntity,int position_index){
        this.questionsEntity=questionsEntity;
        this.position_index=position_index;
        if (position_index==-1){
            question_position=questionsEntity.getChildren().get(0).getQuestions().getPositionForCard();
        }else {
            question_position=position_index;
        }
    }

    /**
     * 存储答案
     * 按照TextView的个数挨个存储答案
     * 答案为字符串  如果没有填写答案则为"" ，add进答案list
     * 当用户返回来继续做题时，由于答案list里已经存在了，所以直接替换
     * 如果其中一个TextView的答案不为null，则认为用户已经答了这道题
     */
    public void saveAnswers() {
        if (rlMark != null && rlMark.getChildCount() > 0) {
            int fillCount = rlMark.getChildCount();
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

    public void setAnswersToPosition(int position,String answer){
        TextView textView=(TextView) rlMark.getChildAt(position);
        ArrayList<String> answer_list=bean.getFillAnswers();
        answer_list.add(position,answer);
//        asd
        setText(textView,answer);
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInput() {
        if (rlMark != null) {
            int count = rlMark.getChildCount();
            for (int i = 0; i < count; i++) {
                TextView TextView = (TextView) rlMark.getChildAt(i);
                CommonCoreUtil.hideSoftInput(TextView);
            }
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

    @Override
    public void onClick(View v) {
        int position= (int) v.getTag(NUMBERPAGER);
        if (selectListener!=null){
            selectListener.QuestionPosition(position);
        }
        setTextViewSelect(position);
    }

    public void setTextViewSelect(int position){
        select_position=position;
        for (int i=0;i<list_textview.size();i++){
            TextView textView=list_textview.get(i);
            if ((int)textView.getTag(NUMBERPAGER)==position){
                textView.setBackgroundResource(R.drawable.gestalt_button_nowanswer);
                textView.setTextColor(mCtx.getResources().getColor(R.color.color_805500));
            }else {
//                List<PaperTestEntity> list=questionsEntity.getChildren();
                List<String> list=bean.getFillAnswers();
                if (list!=null&&list.size()>=i){
                    String answer=list.get(i);
                    setTextColor(textView,answer);
                }else {
                    setTextColor(textView,null);
                }
            }
        }
    }

    private void setTextColor(TextView textView,String answer){
        if (TextUtils.isEmpty(answer)){
            textView.setBackgroundResource(R.drawable.gestalt_button_noanswer);
            textView.setTextColor(mCtx.getResources().getColor(R.color.color_805500));
        }else {
            textView.setBackgroundResource(R.drawable.gestalt_button_answer);
            textView.setTextColor(mCtx.getResources().getColor(R.color.color_black));
        }
    }

    public interface QuestionPositionSelectListener{
        void QuestionPosition(int position);
    }

    public void setListener(QuestionPositionSelectListener listener){
        this.selectListener=listener;
    }

    /**
     * textView绘制完成后匹配字符
     * 然后覆盖TextView
     */
    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        @SuppressWarnings("deprecation")
        @Override
        public void onGlobalLayout() {
            FillBlanksButtonFramelayout.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//            Pattern pattern = Pattern.compile("_______________");
            Pattern pattern = Pattern.compile("\\(                 \\)");
            boolean flag=true;
            if (!StringUtils.isEmpty(data)) {
                Matcher matcher = pattern.matcher(data);
                int i=0;
                while (matcher.find()) {
                    if (flag) {
                        flag = CheckSpaceIsSuff(i,matcher.start(), matcher.end());
                        i++;
                    }
                }
                if (flag) {
                    i=0;
                    Matcher matcher1 = pattern.matcher(data);
                    while (matcher1.find()) {
                        addTextView(matcher1.start(), matcher1.end());
                        setAnswers_cache(i);
                        i++;
                    }
                }
            }
            if (flag) {
//                setData();
                initViewWithData(bean);
                /**设置默认选中第一个*/
                TextView textView_first = (TextView) rlMark.getChildAt(select_position);
                if (textView_first != null) {
                    textView_first.setBackgroundResource(R.drawable.gestalt_button_nowanswer);
                }
            }
        }
    }
    private boolean CheckSpaceIsSuff(int index,int index_start,int index_end){
        Layout layout = tvFillBlank.getLayout();
//        int line_start = layout.getLineForOffset(index_start);
//        int line_end = layout.getLineForOffset(index_end);
//        if (line_start!=line_end){
            float width=tvFillBlank.getWidth();
            float marginleft = layout.getPrimaryHorizontal(index_start);
            if (width-marginleft-20>textview_width+16){
                return true;
            }else {
                setNewText(index);
                ViewTreeObserver.OnGlobalLayoutListener listener = new MyOnGlobalLayoutListener();
                this.getViewTreeObserver().addOnGlobalLayoutListener(listener);
                return false;
            }
//        }else {
//            return true;
//        }
    }

    private void setNewText(int index){
//        String[] strings=data.split("_______________");
        String[] strings=data.split("\\(                 \\)");
        String mData="";
        for (int i=0;i<strings.length;i++){
            mData=mData+strings[i];
            if (index==i){
                mData=mData+"\n";
            }
            if (i!=strings.length-1) {
//                mData = mData + "_______________";
                mData = mData + "(                 )";
            }
        }
        data=mData;
        tvFillBlank.setText(data);
    }

    private void setData(){
//        int ss=bean.getFillAnswers().size();
//        List<PaperTestEntity> list=questionsEntity.getChildren();
        List<String> list=bean.getFillAnswers();
        if (list!=null) {
            for (int i = 0; i < list.size(); i++) {
                TextView textView = (TextView) rlMark.getChildAt(i);
                if (!StringUtils.isEmpty(list.get(i))) {
                    String answer = list.get(i);
                    setText(textView, answer);
                    setTextColor(textView,answer);
                }else {
                    int answer_id=(int)textView.getTag(PAGER)+1;
                    textView.setText(answer_id+"");
                }
            }
        }
    }

    private void setAnswers_cache(int i){
//        answers_cache.add("");
        if (bean.getFillAnswers().size()>i){

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

    /**
     * 首先找到字符所在的位置index
     * 然后计算所在的行 line
     * 算出行的y坐标 top bottom，即为TextView的高
     * 然后算出字符距离左边的x坐标即为TextView的leftMargin
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void addTextView(int index_start,int index_end) {
        Layout layout = tvFillBlank.getLayout();

        Rect bound = new Rect();
        int line = layout.getLineForOffset(index_start);
        layout.getLineBounds(line, bound);

        int yAxisTop = bound.top;//字符顶部y坐标
        int yAxisBottom = bound.bottom;//字符底部y坐标

        float xAxisLeft = layout.getPrimaryHorizontal(index_start);//字符左边x坐标
        float xAxisRight = layout.getPrimaryHorizontal(index_end);//字符左边x坐标

        RelativeLayout.LayoutParams params;
        float offset_heigh=0;
//        float textview_width=(textSize / 2) * 8;
        if (offset_width==0) {
            float xAxisWidth = xAxisRight - xAxisLeft;
            offset_width = xAxisWidth - textview_width;
        }

        if (bean != null && String.valueOf(YanXiuConstant.SUBJECT.YINYU).equals(bean.getSubjectId())) {
            if (CommonCoreUtil.getSDK() >= 21) {
                offset_heigh=textSize/3;
                params = new RelativeLayout.LayoutParams((int) textview_width, (int) (yAxisHeight + offset_heigh));
            } else {
                offset_heigh=textSize/3;
                params = new RelativeLayout.LayoutParams((int) textview_width, (int) (yAxisHeight + offset_heigh));
            }
        } else {
            if (CommonCoreUtil.getSDK() >= 21) {
                offset_heigh=textSize/3;
                params = new RelativeLayout.LayoutParams((int) textview_width, (int) (yAxisHeight + offset_heigh));
            } else {
                offset_heigh=textSize / 3;
                params = new RelativeLayout.LayoutParams((int) textview_width, (int) (yAxisHeight + offset_heigh));
            }
        }
        params.leftMargin = (int) (xAxisLeft+offset_width/2);
        params.leftMargin = (int)xAxisLeft;
//        params.topMargin = (int) (yAxisTop - textSize / 2)+ Util.dipToPx(10)+4;
        params.topMargin = (int) (yAxisTop+ Util.dipToPx(10)-offset_heigh/2);
        TextView tv = new TextView(mCtx);
        tv.setSingleLine();
        tv.setTextColor(mCtx.getResources().getColor(R.color.color_805500));
        tvFillBlank.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        tv.setBackgroundResource(R.drawable.gestalt_button_noanswer);
        tv.setOnClickListener(this);
//        tv.setTag(question_position);

        tv.setTag(NUMBERPAGER,list_textview.size());
        tv.setTag(PAGER,question_position);
        question_position++;
//        tv.setText(list_textview.size()+1+"");
        tv.setGravity(Gravity.CENTER);
//        tv.setBackgroundColor(Color.parseColor(Color_00FF00));
        list_textview.add(tv);
        setTextViewCusrorDrawable(tv);
        if (answerViewTypyBean == SubjectExercisesItemBean.RESOLUTION || answerViewTypyBean == SubjectExercisesItemBean.WRONG_SET) {
            tv.setEnabled(false);
            tv.setFocusable(false);
        }
        rlMark.addView(tv, params);
    }

    private void setTextViewCusrorDrawable(TextView bt) {
        try {
            // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
            Field fCursorDrawableRes = TextView.class.getDeclaredField(
                    "mCursorDrawableRes");
            fCursorDrawableRes.setAccessible(true);
            int mCursorDrawableRes = fCursorDrawableRes.getInt(bt);
            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(bt);
            Class<?> clazz = editor.getClass();
            Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);
            Drawable[] drawables = new Drawable[2];
            drawables[0] = bt.getContext().getResources().getDrawable(mCursorDrawableRes);
            drawables[1] = bt.getContext().getResources().getDrawable(mCursorDrawableRes);
            int color = mCtx.getResources().getColor(R.color.color_black);
            drawables[0].setColorFilter(color, PorterDuff.Mode.SRC_OVER);
            drawables[1].setColorFilter(color, PorterDuff.Mode.SRC_OVER);
            fCursorDrawable.set(editor, drawables);
        } catch (Exception ignored) {
        }
    }

    public int getFontHeight(float fontSize){
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

}
