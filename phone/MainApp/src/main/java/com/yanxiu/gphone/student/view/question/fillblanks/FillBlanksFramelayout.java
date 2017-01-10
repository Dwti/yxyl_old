package com.yanxiu.gphone.student.view.question.fillblanks;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.DensityUtils;
import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.yanxiu.gphone.student.HtmlParser.FillBlankImageGetterTrick;
import com.yanxiu.gphone.student.HtmlParser.MyHtml;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.view.MyEdittext;
import com.yanxiu.gphone.student.view.question.QuestionsListener;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/7/16.
 */
public class FillBlanksFramelayout extends FrameLayout implements
        QuestionsListener,
        FillBlankImageGetterTrick.Callback {
    private QuestionsListener listener;
    private AnswerBean bean;
    private List<String> answers = new ArrayList<String>();
    private int mAnswerLength = 10;
    private StringBuffer mAnswerSb = new StringBuffer();
    public Context mCtx;
    private RelativeLayout rlMark;
    private YXiuAnserTextView tvFillBlank;
    private YXiuAnserTextView trickTextView;
    private String data = "鲁迅，原名(_)字(_)，他的代表作品是小说集(_)、(_)，散文集(_)。\n" +
            "    鲁迅再《琐记》一文中，用了(_)来讥讽洋务派的办学。\n" +
            "    鲁迅写出了中国现代第一篇白话小说(_)，1918年在(_)上发表其后又发表(_)等著名小说。\n";
    private int answerViewTypyBean;
    private int textSize = 0;
    private Spanned txt;
    private MyEdittext trickBottomEtView;

    public FillBlanksFramelayout(Context context) {
        super(context);
        mCtx = context;
        initView();

    }

    public FillBlanksFramelayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCtx = context;
        initView();
    }

    public FillBlanksFramelayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
        mAnswerLength = 0;

        for (String Str : answers) {
            if (mAnswerLength < Str.length()) {
                mAnswerLength = Str.length();
            }
        }

        // 10 - 18 之间
        int maxWidth = 18;
        int minWidth = 10;
        WindowManager wm = (WindowManager) mCtx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;

        if (screenWidth <= 480) {
            // 要保证占位符号能在一行内显示
            minWidth = 6;
            maxWidth = 12;
        }

        mAnswerLength = Math.max(Math.min(maxWidth, mAnswerLength), minWidth);

        // 这里不能使用space空格，因为html text里会省略连续空格
        mAnswerSb.append("o");
        for (int i = 0; i < mAnswerLength - 1; i++) {
            mAnswerSb.append("o");
        }
        StringBuffer replacementString = new StringBuffer(mAnswerSb);
        replacementString.append("o.");
        mAnswerSb.append("o\\.");

        data = stem + "  \n";
        data = data + "  \n";
        data = data + "<p><br/></p>";

        String desReplaceString = " &nbsp " + replacementString + " &nbsp "; /* 防止在(---)之间换行 */
        data = data.replace("(_)",desReplaceString);

        FillBlankImageGetterTrick getter = new FillBlankImageGetterTrick(tvFillBlank, mCtx);
        getter.setCallback(this);
        txt = MyHtml.fromHtml(mCtx, data, getter, null, null, null);
        tvFillBlank.setText(txt);
        //if (!hasImageTagInHtmlText(data)) {
            ViewTreeObserver.OnGlobalLayoutListener listener = new MyOnGlobalLayoutListener();
            this.getViewTreeObserver().addOnGlobalLayoutListener(listener);
        //}
    }

    private void initView() {
        LogInfo.log("geny", "framelayout_fill_blanks_question initView");
        LayoutInflater.from(mCtx).inflate(
                R.layout.framelayout_fill_blanks_question, this);
        rlMark = (RelativeLayout) this.findViewById(R.id.rl_mark);
        tvFillBlank = (YXiuAnserTextView) this.findViewById(R.id.tv_fill_blanks);
        int px = DensityUtils.dip2px(mCtx, 15);
        textSize = DensityUtils.px2sp(mCtx, px);
        tvFillBlank.setTextSize(textSize);
        tvFillBlank.setLineSpacing(DensityUtils.px2dip(this.getContext(), 30), 1);

        trickTextView = (YXiuAnserTextView) this.findViewById(R.id.tv_fill_blanks_trick);
        trickTextView.setTextSize(textSize);
        trickTextView.setVisibility(View.INVISIBLE);
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
     * 设置EditText中的答案
     */
    @Override
    public void initViewWithData(AnswerBean bean) {
        int actualEditTextCount = rlMark.getChildCount() - 1;
        if (rlMark != null && bean != null && bean.getFillAnswers().size() > 0) {
            if (bean.getFillAnswers().size() == actualEditTextCount) {
                for (int i = 0; i < actualEditTextCount; i++) {
                    ((EditText) rlMark.getChildAt(i)).setText(bean.getFillAnswers().get(i));
                }
            }
        }
    }

    @Override
    public void answerViewClick() {

    }

    /**
     * 存储答案
     * 按照EditText的个数挨个存储答案
     * 答案为字符串  如果没有填写答案则为"" ，add进答案list
     * 当用户返回来继续做题时，由于答案list里已经存在了，所以直接替换
     * 如果其中一个EditText的答案不为null，则认为用户已经答了这道题
     */
    public void saveAnswers() {
        if (rlMark != null && rlMark.getChildCount() > 0) {
            bean.getFillAnswers().clear();

            int actualEditTextCount = rlMark.getChildCount() - 1;
            int answerCount = bean.getFillAnswers().size();

            if (actualEditTextCount > 0) {
                boolean flag = true;
                for (int i = 0; i < actualEditTextCount; i++) {
                    String fillAnswer = "";
                    if (!StringUtils.isEmpty(
                            ((EditText) rlMark.getChildAt(i)).getText().toString())) {
                        fillAnswer = ((EditText) rlMark.getChildAt(i)).getText().toString();
//                    bean.setIsFinish(true);
                    } else {
                        flag = false;
                    }

                    if (answerCount == actualEditTextCount) {
                        bean.getFillAnswers().set(i, fillAnswer);
                    } else {
                        bean.getFillAnswers().add(fillAnswer);
                    }
                }
                bean.setIsFinish(flag);
            }
            bean.setIsRight(judgeAnswerIsRight());
        }
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInput() {
        if (rlMark != null) {
            int count = rlMark.getChildCount();
            for (int i = 0; i < count; i++) {
                EditText editText = (EditText) rlMark.getChildAt(i);
                CommonCoreUtil.hideSoftInput(editText);
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
        ArrayList<String> myCodeChangedAnswers = new ArrayList<String>();
        ArrayList<String> codeChangedAnswers = new ArrayList<String>();
        for (String s : answers) {
            codeChangedAnswers.add(full2half(s));
        }
        for (String s : myAnswers) {
            myCodeChangedAnswers.add(full2half(s));
        }

        return CommonCoreUtil.compare(myCodeChangedAnswers, codeChangedAnswers);
    }

    private static final char SBC_CHAR_START = 65281;
    private static final char SBC_CHAR_END = 65374;
    private static final int CONVERT_STEP = 65248;
    private static final char SBC_SPACE = 12288;
    private static final char DBC_SPACE = ' ';

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

    /**
     * textView绘制完成后匹配字符
     * 然后覆盖EditText
     */
    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        public void onGlobalLayout() {
            FillBlanksFramelayout.this.getViewTreeObserver().removeGlobalOnLayoutListener(
                    this);
            // 防止加入重复EditText
            if (mAnswerSb!=null&&tvFillBlank!=null&&rlMark!=null) {
                String targetWord = mAnswerSb.toString();
                CharSequence c = tvFillBlank.getText().toString();
                Pattern pattern = Pattern.compile(targetWord);
                if (!StringUtils.isEmpty(data)) {
                    Matcher matcher = pattern.matcher(c);
                    while (matcher.find()) {
                        MyEdittext existEtView = (MyEdittext) rlMark.findViewWithTag(matcher.start());
                        if (existEtView != null) {
                            changeEditText(matcher.start(), matcher.end(), c.length() - 1, existEtView);
                        } else {
                            addEditText(matcher.start(), matcher.end(), c.length() - 1);
                        }
                    }
                }
                hideSoftInput();

                initViewWithData(bean);
            }
        }
    }

    private boolean CheckSpaceIsSuff(int index, int index_start, int index_end) {
        Layout layout = tvFillBlank.getLayout();
        int line_start = layout.getLineForOffset(index_start);
        int line_end = layout.getLineForOffset(index_end);
        if (line_start != line_end) {
            setNewText(index);
            ViewTreeObserver.OnGlobalLayoutListener listener = new MyOnGlobalLayoutListener();
            //this.getViewTreeObserver().addOnGlobalLayoutListener(listener);
            return false;
        } else {
            return true;
        }
    }

    private void setNewText(int index) {
        String[] strings = data.split(mAnswerSb.toString());
        //String[] strings=data.split("\\(________________\\)");
//        String[] strings=data.split("\\(");
        String mData = "";
        for (int i = 0; i < strings.length; i++) {
            mData = mData + strings[i];
            if (index == i) {
                mData = mData + "\n";
            }
            if (i != strings.length - 1) {
                //mData = mData + "("+mAnswerSb.toString()+")";
                mData = mData + mAnswerSb.toString();
                //mData = mData + "(________)";
            }
        }
        data = mData;
        //tvFillBlank.setText(data);
        //tvFillBlank.setText(Html.fromHtml(data));
    }

    private void setEditTextCusrorDrawable(EditText editText) {
        try {
            // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
            Field fCursorDrawableRes = TextView.class.getDeclaredField(
                    "mCursorDrawableRes");
            fCursorDrawableRes.setAccessible(true);
            @DrawableRes int mCursorDrawableRes = fCursorDrawableRes.getInt(editText);
            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(editText);
            Class<?> clazz = editor.getClass();
            Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);
            Drawable[] drawables = new Drawable[2];
            drawables[0] = editText.getContext().getResources().getDrawable(mCursorDrawableRes);
            drawables[1] = editText.getContext().getResources().getDrawable(mCursorDrawableRes);
            int color = mCtx.getResources().getColor(R.color.color_black);
            drawables[0].setColorFilter(color, PorterDuff.Mode.SRC_OVER);
            drawables[1].setColorFilter(color, PorterDuff.Mode.SRC_OVER);
            fCursorDrawable.set(editor, drawables);
        } catch (Exception ignored) {
        }
    }


    // 在网络图片加载成功、失败后被调用
    @Override
    public void onFinish() {
        ViewTreeObserver.OnGlobalLayoutListener listener = new MyOnGlobalLayoutListener();
        this.getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }

    // 检查html格式文本中是否有<img>标签
    private boolean hasImageTagInHtmlText(String htmlText) {
        Pattern pattern = Pattern.compile("<img");
        Matcher matcher = pattern.matcher(htmlText);
        return matcher.find();
    }

    // 把需要扣掉的部分换成 EditText
    // 这个方法目前有点问题，用padding和用margin时居然不一致，不懂为什么，暂时只能2套方案取相对合适的
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void addEditText(int start, int end, int last) {
        Layout layout = tvFillBlank.getLayout();
        if (layout==null){
            return;
        }
        int line = layout.getLineForOffset((int)((start + end) * 0.5));
        float left = layout.getSecondaryHorizontal(start);
        float right = layout.getPrimaryHorizontal(end) + 4;

        // 用trickTextView来计算高度
        trickTextView.setText("(-)");
        Layout layout3 = trickTextView.getLayout();
        if (layout3==null){
            return;
        }
        float height = layout3.getLineBottom(0) - layout3.getLineTop(0);
        float ascent = Math.abs(layout3.getLineAscent(0));
        float top = layout.getLineBaseline(line) - ascent + 2;
        float bottom = top + height - 2;

        RelativeLayout.LayoutParams params;
        params = new RelativeLayout.LayoutParams((int) (right - left), (int) (bottom - top));
        params.leftMargin = (int) left;
        params.topMargin = (int) top;

        final MyEdittext et = new MyEdittext(mCtx);
        et.setTag(start);
        et.setPadding(10, 0, 10, 0);
        et.setSingleLine();
//        et.setTextColor(mCtx.getResources().getColor(R.color.color_333333));
        et.setTextSize(textSize);
//        et.setBackground(mCtx.getResources().getDrawable(R.drawable.fill_blank_bg));
        et.setBackground(mCtx.getResources().getDrawable(R.drawable.fill_full_bg));
//        et.setGravity(Gravity.CENTER);
        et.setFocusable(false);
        setEditTextCusrorDrawable(et);
        if (answerViewTypyBean == SubjectExercisesItemBean.RESOLUTION || answerViewTypyBean == SubjectExercisesItemBean.WRONG_SET) {
            et.setEnabled(false);
            et.setFocusable(false);
        }
        rlMark.addView(et, params);

        // 用trickBottomEtView来保证rlMark能到最底
        if (trickBottomEtView != null) {
            rlMark.removeView(trickBottomEtView);
        }
        final MyEdittext et2 = new MyEdittext(mCtx);
        et2.setVisibility(View.INVISIBLE);
        int lastLine = layout.getLineForOffset(last);
        bottom = layout.getLineBottom(lastLine);
        RelativeLayout.LayoutParams params2;
        params2 = new RelativeLayout.LayoutParams((int) (right - left), (int) (bottom - top));
        params2.leftMargin = (int) left;
        params2.topMargin = (int) top;

        rlMark.addView(et2, params2);
        trickBottomEtView = et2;
    }

    // 为了解决闪烁，只能分成两个方法，重构的时候需要思考这边root cause以及解决方案
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void changeEditText(int start, int end, int last, MyEdittext et) {
        Layout layout = tvFillBlank.getLayout();
        if (layout==null){
            return;
        }
        int line = layout.getLineForOffset((int)((start + end) * 0.5));
        float left = layout.getSecondaryHorizontal(start);
        float right = layout.getPrimaryHorizontal(end) + 4;

        // 用trickTextView来计算高度
        trickTextView.setText("(-)");
        Layout layout3 = trickTextView.getLayout();
        if (layout3==null){
            return;
        }
        float height = layout3.getLineBottom(0) - layout3.getLineTop(0);
        float ascent = Math.abs(layout3.getLineAscent(0));
        float top = layout.getLineBaseline(line) - ascent + 2;
        float bottom = top + height - 2;

        RelativeLayout.LayoutParams params;
        params = new RelativeLayout.LayoutParams((int) (right - left), (int) (bottom - top));
        params.leftMargin = (int) left;
        params.topMargin = (int) top;

        et.setLayoutParams(params);

        if (trickBottomEtView != null) {
            rlMark.removeView(trickBottomEtView);
        }
        final MyEdittext et2 = new MyEdittext(mCtx);
        et2.setVisibility(View.INVISIBLE);
        int lastLine = layout.getLineForOffset(last);
        bottom = layout.getLineBottom(lastLine);
        RelativeLayout.LayoutParams params2;
        params2 = new RelativeLayout.LayoutParams((int) (right - left), (int) (bottom - top));
        params2.leftMargin = (int) left;
        params2.topMargin = (int) top;

        rlMark.addView(et2, params2);
        trickBottomEtView = et2;
    }
}
