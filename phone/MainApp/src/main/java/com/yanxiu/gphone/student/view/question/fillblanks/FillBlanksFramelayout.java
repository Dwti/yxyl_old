package com.yanxiu.gphone.student.view.question.fillblanks;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.DensityUtils;
import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.yanxiu.gphone.student.HtmlParser.MyHtml;
import com.yanxiu.gphone.student.HtmlParser.UilImageGetter;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.AnswerBean;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
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
        UilImageGetter.Callback {
    private QuestionsListener listener;
    private AnswerBean bean;
    private List<String> answers = new ArrayList<String>();
    private int mAnswerLength = 10;
    private StringBuffer mAnswerSb = new StringBuffer();
    public Context mCtx;
    private RelativeLayout rlMark;
    private YXiuAnserTextView tvFillBlank;
    private String data = "鲁迅，原名(_)字(_)，他的代表作品是小说集(_)、(_)，散文集(_)。\n" +
            "    鲁迅再《琐记》一文中，用了(_)来讥讽洋务派的办学。\n" +
            "    鲁迅写出了中国现代第一篇白话小说(_)，1918年在(_)上发表其后又发表(_)等著名小说。\n";
    private int answerViewTypyBean;
    private int textSize = 0;
    private Spanned txt;
    private boolean fromImage = false;

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
        stem = "<img src=\"http://scc.jsyxw.cn/image/20160815/147124203.png\"/><br/> <p>(_) strong</p> <p>(_) funny</p><p>(_) pretty</p><p>(_) old</p><p>(_) kind</p>";

        for (String Str : answers) {
            if (mAnswerLength < Str.length()) {
                mAnswerLength = Str.length();
            }
        }
        if (mAnswerLength > 22) {
            mAnswerLength = 22;
        }
        for (int i = 0; i < mAnswerLength; i++) {
            mAnswerSb.append("  ");
        }
        data = stem + "  \n";
        data = data + "  \n";
        //data = data.replace("(_)",mAnswerSb.toString());

        UilImageGetter getter = new UilImageGetter(tvFillBlank, mCtx);
        getter.setCallback(this);
        txt = MyHtml.fromHtml(mCtx, data, getter, null, null, null);

        tvFillBlank.setText(txt);

        ViewTreeObserver.OnGlobalLayoutListener listener = new MyOnGlobalLayoutListener();
        this.getViewTreeObserver().addOnGlobalLayoutListener(listener);

        //handler.sendEmptyMessageDelayed(3, 3000);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    addEditText(4, 6);
                    break;
                case 2:
                {
                    String targetWord = "(_)";
                    CharSequence c = tvFillBlank.getText().toString();
                    int startOffsetOfClickedText = tvFillBlank.getText().toString().indexOf(targetWord);
                    int endOffsetOfClickedText = startOffsetOfClickedText + targetWord.length();

                    addEditText(startOffsetOfClickedText, endOffsetOfClickedText);
                }
                break;
                case 3:
                {
                    String targetWord = "(_)";
                    CharSequence c = tvFillBlank.getText().toString();
                    Pattern pattern = Pattern.compile(targetWord);
                    boolean flag=true;
                    if (!StringUtils.isEmpty(data)) {
                        Matcher matcher = pattern.matcher(c);
                        int i=0;
                        while (matcher.find()) {
                            addEditText(matcher.start(), matcher.end());


                        }

                    }

                        hideSoftInput();

                }
                break;
            }
        }
    };

    private void initView() {
        LogInfo.log("geny", "framelayout_fill_blanks_question initView");
        LayoutInflater.from(mCtx).inflate(
                R.layout.framelayout_fill_blanks_question, this);
        rlMark = (RelativeLayout) this.findViewById(R.id.rl_mark);
        tvFillBlank = (YXiuAnserTextView) this.findViewById(R.id.tv_fill_blanks);
        int px = DensityUtils.dip2px(mCtx, 15);
        textSize = DensityUtils.px2sp(mCtx, px);
        tvFillBlank.setTextSize(textSize);


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
        if (rlMark != null && bean != null && bean.getFillAnswers().size() > 0) {
            if (bean.getFillAnswers().size() == rlMark.getChildCount()) {
                int fillCount = rlMark.getChildCount();
                for (int i = 0; i < fillCount; i++) {
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
            int fillCount = rlMark.getChildCount();
            int answerCount = bean.getFillAnswers().size();

            if (fillCount > 0) {
                boolean flag = true;
                for (int i = 0; i < fillCount; i++) {
                    String fillAnswer = "";
                    if (!StringUtils.isEmpty(
                            ((EditText) rlMark.getChildAt(i)).getText().toString())) {
                        fillAnswer = ((EditText) rlMark.getChildAt(i)).getText().toString();
//                    bean.setIsFinish(true);
                    } else {
                        flag = false;
                    }

                    if (answerCount == fillCount) {
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
        return CommonCoreUtil.compare(myAnswers, answers);
    }

    /**
     * textView绘制完成后匹配字符
     * 然后覆盖EditText
     */
    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

        public void onGlobalLayout() {
            FillBlanksFramelayout.this.getViewTreeObserver().removeGlobalOnLayoutListener(
                    this);
            if (fromImage) {
                String targetWord = "(_)";
                CharSequence c = tvFillBlank.getText().toString();
                Pattern pattern = Pattern.compile(targetWord);
                boolean flag = true;
                if (!StringUtils.isEmpty(data)) {
                    Matcher matcher = pattern.matcher(c);
                    int i = 0;
                    while (matcher.find()) {
                        addEditText(matcher.start(), matcher.end());
                    }

                }
                hideSoftInput();
                fromImage = false;
            }
        }

        /*
        @SuppressWarnings("deprecation")
        @Override
        public void onGlobalLayout() {


            FillBlanksFramelayout.this.getViewTreeObserver().removeGlobalOnLayoutListener(
                    this);

            Pattern pattern = Pattern.compile(mAnswerSb.toString());
            //Pattern pattern = Pattern.compile("_______________");
//            if(!StringUtils.isEmpty(data)){
//                Matcher matcher = pattern.matcher(data);
//                while (matcher.find()) {
//                    addEditText(matcher.start());
//                }
//            }
//            initViewWithData(bean);


            boolean flag=true;
            if (!StringUtils.isEmpty(data)) {
                Matcher matcher = pattern.matcher(txt);
                int i=0;
                while (matcher.find()) {
                    if (flag) {
                        flag = CheckSpaceIsSuff(i,matcher.start(), matcher.end());
                        i++;
                    }
                }
                if (flag) {
                    Matcher matcher1 = pattern.matcher(data);
                    while (matcher1.find()) {
                        //addEditText(matcher1.start());
                        addEditText(matcher1.start(), matcher1.end());
                    }
                }
            }
            if (flag) {
                initViewWithData(bean);
                try {
//                    ((EditText) rlMark.getChildAt(0)).requestFocus();
                }catch (Exception e){

                }
                hideSoftInput();
            }
        }*/
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

    /**
     * 首先找到字符所在的位置index
     * 然后计算所在的行 line
     * 算出行的y坐标 top bottom，即为EditText的高
     * 然后算出字符距离左边的x坐标即为EditText的leftMargin
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void addEditText(int start, int end) {
        Layout layout = tvFillBlank.getLayout();
//        for (int i = 0; i < 20; i++) {
//            int myline = layout.getLineForOffset(i);
//            Log.e("myline", myline+"");
//        }


        int startLine = layout.getLineForOffset(start);
        int endLine = layout.getLineForOffset(end);
        assert startLine == endLine;
        int line = startLine;

        float ttt = layout.getPrimaryHorizontal(2);

        float left = layout.getPrimaryHorizontal(start);
        float right = layout.getPrimaryHorizontal(end);
        float top = layout.getLineTop(line);
        float bottom = layout.getLineBottom(line);

        RelativeLayout.LayoutParams params;
        params = new RelativeLayout.LayoutParams((int) (right - left), (int) (bottom - top));
        params.leftMargin = (int) left;
        params.topMargin = (int) top;

        final MyEdittext et = new MyEdittext(mCtx);
        et.setPadding(10, 0, 10, 0);
        et.setSingleLine();
        et.setTextColor(mCtx.getResources().getColor(R.color.color_00b8b8));
        et.setTextSize(textSize);
        et.setBackground(mCtx.getResources().getDrawable(R.drawable.fill_blank_bg));
        et.setGravity(Gravity.CENTER);
        setEditTextCusrorDrawable(et);
        if (answerViewTypyBean == SubjectExercisesItemBean.RESOLUTION || answerViewTypyBean == SubjectExercisesItemBean.WRONG_SET) {
            et.setEnabled(false);
            et.setFocusable(false);
        }
        rlMark.addView(et, params);
    }


    private void setEditTextCusrorDrawable(EditText editText) {
        try {
            // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
            Field fCursorDrawableRes = TextView.class.getDeclaredField(
                    "mCursorDrawableRes");
            fCursorDrawableRes.setAccessible(true);
            int mCursorDrawableRes = fCursorDrawableRes.getInt(editText);
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

    @Override
    public void onFinish() {
        fromImage = true;
        ViewTreeObserver.OnGlobalLayoutListener listener = new MyOnGlobalLayoutListener();
        this.getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }
}
