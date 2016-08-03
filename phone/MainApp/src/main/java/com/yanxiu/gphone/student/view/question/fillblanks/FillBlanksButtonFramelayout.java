package com.yanxiu.gphone.student.view.question.fillblanks;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
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

    private QuestionsListener listener;
    private QuestionPositionSelectListener selectListener;
    protected QuestionEntity questionsEntity;
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
    private int textSize = 0;

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
//        data = stem + "  \n";
        data = data + "  \n";
//        data = data.replace("(_)", "________________");
        data = data.replace("(_)", "                  ");
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
        int px = DensityUtils.dip2px(mCtx, 15);
        textSize = DensityUtils.px2sp(mCtx, px);
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
//        initViewWithData(bean);
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
                text=(int)textView.getTag()+1+".A";
                break;
            case "1":
                text=(int)textView.getTag()+1+".B";
                break;
            case "2":
                text=(int)textView.getTag()+1+".C";
                break;
            case "3":
                text=(int)textView.getTag()+1+".D";
                break;
            default:
                text=(int)textView.getTag()+1+"";
                break;
        }
        textView.setText(text);
    }

    @Override
    public void answerViewClick() {
    }

    public void setQuestionsEntity(QuestionEntity questionsEntity){
        this.questionsEntity=questionsEntity;
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
            List<QuestionEntity> list=questionsEntity.getChildren();
            for (int i = 0; i < fillCount; i++) {
                String fillAnswer = "";
                if (list!=null) {
                    if (!StringUtils.isEmpty(list.get(i).getAnswerBean().getSelectType())) {
                        fillAnswer = list.get(i).getAnswerBean().getSelectType();
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
        int position= (int) v.getTag();
        if (selectListener!=null){
            selectListener.QuestionPosition(position);
        }
        setTextViewSelect(position);
    }


    public void setTextViewSelect(int position){
        for (int i=0;i<list_textview.size();i++){
            TextView textView=list_textview.get(i);
            if ((int)textView.getTag()==position){
                textView.setBackgroundResource(R.drawable.gestalt_button_nowanswer);
                textView.setTextColor(mCtx.getResources().getColor(R.color.color_805500));
            }else {
                List<QuestionEntity> list=questionsEntity.getChildren();
                if (list!=null&&list.get(i).getAnswerBean()!=null&&list.get(i).getAnswerBean().getSelectType()!=null){
                    String answer=list.get(i).getAnswerBean().getSelectType();
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
            FillBlanksButtonFramelayout.this.getViewTreeObserver().removeGlobalOnLayoutListener(
                    this);
            Pattern pattern = Pattern.compile("                  ");
//            Pattern pttern1 = Pattern.compile("                  ");
            if (!StringUtils.isEmpty(data)) {
                Matcher matcher = pattern.matcher(data);
                while (matcher.find()) {
                    addTextView(matcher.start());
//                    setAnswers_cache();
                }
            }
//            initViewWithData(bean);
            setData();
        }
    }

    private void setData(){
        List<QuestionEntity> list=questionsEntity.getChildren();
        if (list!=null) {
            for (int i = 0; i < list.size(); i++) {
                TextView textView = (TextView) rlMark.getChildAt(i);
                if (!StringUtils.isEmpty(list.get(i).getAnswerBean().getSelectType())) {
                    String answer = list.get(i).getAnswerBean().getSelectType();
                    setText(textView, answer);
                    setTextColor(textView,answer);
                }else {
                    int answer_id=(int)textView.getTag()+1;
                    textView.setText(answer_id+"");
                }
            }
        }
        /**设置默认选中第一个*/
        TextView textView_first = (TextView) rlMark.getChildAt(0);
        textView_first.setBackgroundResource(R.drawable.gestalt_button_nowanswer);
    }

    private void setAnswers_cache(){
//        answers_cache.add("");
        bean.getFillAnswers().add("");
    }

    /**
     * 首先找到字符所在的位置index
     * 然后计算所在的行 line
     * 算出行的y坐标 top bottom，即为TextView的高
     * 然后算出字符距离左边的x坐标即为TextView的leftMargin
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void addTextView(int index) {
        Layout layout = tvFillBlank.getLayout();
        Rect bound = new Rect();
        int line = layout.getLineForOffset(index);
        layout.getLineBounds(line, bound);
        LogInfo.log("king", "line = " + line);

        int yAxisTop = bound.top;//字符顶部y坐标
        int yAxisBottom = bound.bottom;//字符底部y坐标

        float xAxisLeft = layout.getPrimaryHorizontal(index);//字符左边x坐标
//        float xAxisRight =  layout.getSecondaryHorizontal(index);//字符右边x坐标  (yAxisBottom - 5 - yAxisTop + tvFillBlank.getTextSize() / 2)
        LogInfo.log("king", "yAxisTop = " + yAxisTop + " , yAxisBottom = " + yAxisBottom);
        RelativeLayout.LayoutParams params;
        if (bean != null && String.valueOf(YanXiuConstant.SUBJECT.YINYU).equals(bean.getSubjectId())) {
            if (CommonCoreUtil.getSDK() >= 21) {
                params = new RelativeLayout.LayoutParams((int) ((tvFillBlank.getTextSize() / 2) * 8), (int) (yAxisBottom - yAxisTop + tvFillBlank.getTextSize()));
            } else {
                params = new RelativeLayout.LayoutParams((int) ((tvFillBlank.getTextSize() / 2) * 8), (int) (yAxisBottom - yAxisTop + tvFillBlank.getTextSize() * 4 / 5));
            }
        } else {
            if (CommonCoreUtil.getSDK() >= 21) {
                params = new RelativeLayout.LayoutParams((int) ((tvFillBlank.getTextSize() / 2) * 8), (int) (yAxisBottom - yAxisTop + tvFillBlank.getTextSize()));
            } else {
                params = new RelativeLayout.LayoutParams((int) ((tvFillBlank.getTextSize() / 2) * 8), (int) (yAxisBottom - yAxisTop + tvFillBlank.getTextSize() / 3));
            }
        }
        params.leftMargin = (int) xAxisLeft;
        params.topMargin = (int) (yAxisTop - tvFillBlank.getTextSize() / 2)+ Util.dipToPx(10)+4;
        TextView tv = new TextView(mCtx);
        tv.setSingleLine();
        tv.setTextColor(mCtx.getResources().getColor(R.color.color_805500));
        tv.setTextSize(textSize);
        tv.setBackgroundResource(R.drawable.gestalt_button_noanswer);
        tv.setOnClickListener(this);
        tv.setTag(list_textview.size());
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


}
