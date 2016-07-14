package com.yanxiu.gphone.parent.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.ColorInt;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import com.yanxiu.gphone.parent.R;

/**
 * colorful arc progress bar
 * Created by shinelw on 12/4/15.
 */
public class ColorArcProgressBar extends View{

    //直径
    private float diameter = 240;
    //判断int的个数
    private final int[] sizeTable = { 9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE };
    //圆心
    private float centerX;
    private float centerY;

    //背景圆圈的paint
    private Paint allArcPaint;
    //进度条的paint
    private Paint progressPaint;
    private Paint vTextPaint;
    private Paint percentPaint;
    private Paint hintPaint;
    private Paint degreePaint;
    private Paint curSpeedPaint;
    private Paint signPaint;
    private int titleColor;

    //当前view的宽度
    private int width;
    //是否view渲染完
    private boolean isFinishLayout = false;
    //是否是根据布局的宽度来设置直径
    private boolean isNeedDiameter = false;

    private RectF bgRect;

    private ValueAnimator progressAnimator;
    private PaintFlagsDrawFilter mDrawFilter;
    private SweepGradient sweepGradient;
    private Matrix rotateMatrix;

    private float startAngle = 270;
    private float sweepAngle = 270;
    private float currentAngle = 0;
    private float lastAngle;
    private int[] colors = new int[]{Color.GREEN, Color.YELLOW, Color.RED, Color.RED};
    private float maxValues = 60;
    private float curValues = 0;
    private String curString = "";
    private float bgArcWidth = dipToPx(2);
    private float progressWidth = dipToPx(0);
    private float textSize;
    private float percentSize;
    private float hintSize = dipToPx(12);
    private float curSpeedSize = dipToPx(13);
    private int aniSpeed = 1000;
    private float longdegree = dipToPx(13);
    private final int DEGREE_PROGRESS_DISTANCE = dipToPx(0);

    private int hintColor;
    private String longDegreeColor = "#111111";
    private int bgArcColorInt = Color.BLACK;
    private int bgTextColorInt = Color.BLACK;
    private int signTextColorInt = Color.BLACK;
    private String titleString;
    private String hintString;

    private boolean isNeedUnit;
    private boolean isNeedContent;
    private boolean isNeedPercent;

    private boolean isAnimationEnd = false;

    private boolean isSign;
    private String signStr;

    private float k;
    private boolean isAnim = false;

    public ColorArcProgressBar(Context context) {
        super(context, null);
        initView();
    }

    public ColorArcProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initCofig(context, attrs);
        initView();
    }

    public ColorArcProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCofig(context, attrs);
        initView();
    }

    /**
     * 初始化布局配置
     * @param context
     * @param attrs
     */
    private void initCofig(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorArcProgressBar);
        bgTextColorInt = a.getColor(R.styleable.ColorArcProgressBar_front_color1, Color.BLACK);
        int color1 = a.getColor(R.styleable.ColorArcProgressBar_front_color1, Color.GREEN);
        int color2 = a.getColor(R.styleable.ColorArcProgressBar_front_color2, color1);
        int color3 = a.getColor(R.styleable.ColorArcProgressBar_front_color3, color1);
        colors = new int[]{color1, color2, color3, color3};


        bgArcColorInt = a.getColor(R.styleable.ColorArcProgressBar_back_color, Color.BLACK);

        sweepAngle = a.getInteger(R.styleable.ColorArcProgressBar_total_engle, 270);
        bgArcWidth = a.getDimension(R.styleable.ColorArcProgressBar_back_width, dipToPx(2));
        progressWidth = a.getDimension(R.styleable.ColorArcProgressBar_front_width, dipToPx(10));
        isNeedContent = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_content, false);
        isNeedDiameter = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_diameter, false);
        isSign = a.getBoolean(R.styleable.ColorArcProgressBar_is_sign, false);
        signStr = a.getString(R.styleable.ColorArcProgressBar_sign_str);
        signTextColorInt = a.getColor(R.styleable.ColorArcProgressBar_sign_color, Color.GREEN);
        isNeedPercent = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_percent, true);
        isNeedUnit = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_unit, false);
        diameter = a.getDimension(R.styleable.ColorArcProgressBar_diameter, dipToPx(200));
        textSize = a.getDimension(R.styleable.ColorArcProgressBar_text_size, dipToPx(28));
        hintSize = a.getDimension(R.styleable.ColorArcProgressBar_hint_size, dipToPx(9));
        hintColor = a.getColor(R.styleable.ColorArcProgressBar_hint_color, Color.GREEN);
        titleColor = a.getColor(R.styleable.ColorArcProgressBar_title_color, -1);
        percentSize = textSize / 2;
        hintString = a.getString(R.styleable.ColorArcProgressBar_string_unit);
        titleString = a.getString(R.styleable.ColorArcProgressBar_string_title);
        curValues = a.getFloat(R.styleable.ColorArcProgressBar_current_value, 0);
        maxValues = a.getFloat(R.styleable.ColorArcProgressBar_max_value, 60);
        setMaxValues(maxValues);
        a.recycle();

    }
    public void setFontColor(@ColorInt int colorFont){
        bgTextColorInt = colorFont;
        colors[0] = colorFont;
        colors[1] = colorFont;
        colors[2] = colorFont;
        colors[3] = colorFont;
        sweepGradient = null;
        sweepGradient = new SweepGradient(centerX, centerY, colors, null);
        //内容显示文字
        if(vTextPaint != null) {
            vTextPaint.setColor(bgTextColorInt);
        }

        //百分比的文字
        if(percentPaint != null) {
            percentPaint.setColor(bgTextColorInt);
        }
        //百分比前面的+-号
        if(signPaint != null) {
            signPaint.setColor(signTextColorInt);
        }
        invalidate();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(isNeedDiameter){
            int width = (int) (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE);
            int height= (int) (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE);
            setMeasuredDimension(width, height);
        }else{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setHintString(String hintString) {
        this.hintString = hintString;
    }

    private void initView() {

        if(isNeedDiameter){

            initPaint();

        }else{
            this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout () {
                    ColorArcProgressBar.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    width = ColorArcProgressBar.this.getWidth();
                    isFinishLayout = true;

                    diameter = (float) (width * 0.8);

                    initPaint();

                    ColorArcProgressBar.this.invalidate();
                }
            });
        }

    }

    public void setSignTextProcess(boolean isSign, int signTextColorInt, String signStr){
        this.isSign = isSign;
        this.signTextColorInt = signTextColorInt;
        this.signStr = signStr;

    }
    private void initPaint(){
        bgRect = new RectF();
        bgRect.top = longdegree + progressWidth/2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.left = longdegree + progressWidth/2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.right = diameter + (longdegree + progressWidth/2 + DEGREE_PROGRESS_DISTANCE);
        bgRect.bottom = diameter + (longdegree + progressWidth/2 + DEGREE_PROGRESS_DISTANCE);

        //圆心
        centerX = (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE)/2;
        centerY = (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE)/2;

        //外部刻度线
        degreePaint = new Paint();
        degreePaint.setColor(Color.parseColor(longDegreeColor));

        //整个弧形
        allArcPaint = new Paint();
        allArcPaint.setAntiAlias(true);
        allArcPaint.setStyle(Paint.Style.STROKE);
        allArcPaint.setStrokeWidth(bgArcWidth);
        allArcPaint.setColor(bgArcColorInt);
        allArcPaint.setStrokeCap(Paint.Cap.ROUND);

        //当前进度的弧形
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setColor(Color.GREEN);

        //内容显示文字
        vTextPaint = new Paint();
        vTextPaint.setTextSize(textSize);
        vTextPaint.setColor(bgTextColorInt);
        vTextPaint.setTextAlign(Paint.Align.CENTER);

        //内容显示符号
        signPaint = new Paint();
        signPaint.setTextSize(textSize);
        signPaint.setColor(signTextColorInt);
        signPaint.setTextAlign(Paint.Align.CENTER);

        //百分比的文字
        percentPaint = new Paint();
        percentPaint.setTextSize(percentSize);
        percentPaint.setColor(bgTextColorInt);
        percentPaint.setTextAlign(Paint.Align.CENTER);

        //显示单位文字
        hintPaint = new Paint();
        hintPaint.setTextSize(hintSize);
        hintPaint.setColor(hintColor);
        hintPaint.setTextAlign(Paint.Align.CENTER);

        //显示标题文字
        curSpeedPaint = new Paint();
        curSpeedPaint.setTextSize(curSpeedSize);
        curSpeedPaint.setColor(hintColor);
        curSpeedPaint.setTextAlign(Paint.Align.CENTER);

        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        sweepGradient = new SweepGradient(centerX, centerY, colors, null);
        rotateMatrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if(isFinishLayout || isNeedDiameter){
            //抗锯齿
            canvas.setDrawFilter(mDrawFilter);

            //整个弧
            canvas.drawArc(bgRect, startAngle, sweepAngle, false, allArcPaint);

            //设置渐变色
            rotateMatrix.setRotate(130, centerX, centerY);
            sweepGradient.setLocalMatrix(rotateMatrix);
            progressPaint.setShader(sweepGradient);

            float unitHeight = 0;
            if (isNeedUnit) {
                //画半圆的缓冲
                if(!TextUtils.isEmpty(hintString)){
                    canvas.drawText(hintString, centerX, (float) (centerY + (2.5 * textSize / 3)), hintPaint);
                }
                unitHeight = textSize / 3;
            }

            if (isNeedContent) {

                if(titleColor != -1){
                    vTextPaint.setColor(titleColor);
                }

                if(isSign && !TextUtils.isEmpty(signStr)){
                    //百分比的负号
                    canvas.drawText(signStr, centerX - ((sizeOfInt((int) curValues) * (textSize / 4)) + (textSize / 3) / 2 + percentSize / 4), centerY + textSize / 3 - unitHeight, signPaint);
                }

                if(isNeedPercent){
                    //百分比的数字
                    canvas.drawText(String.format("%.0f", curValues), centerX, centerY + textSize / 3 - unitHeight, vTextPaint);

                }else{
                    //写入文字
                    canvas.drawText(curString, centerX, centerY + textSize / 3 - unitHeight, vTextPaint);
                }
            }

            if (isNeedPercent) {
                //百分比的百分号 %
                canvas.drawText("%", centerX + (sizeOfInt((int) curValues) * (textSize / 4)) + (textSize / 3) / 2 + percentSize / 4, centerY - (textSize - textSize / 3 - percentSize - percentSize / 4) - unitHeight, percentPaint);
                //当前进度
                canvas.drawArc(bgRect, startAngle, currentAngle, false, progressPaint);

            }else{
                //没有百分比的时候直接是圆圈
                canvas.drawArc(bgRect, 0, 360, false, progressPaint);
            }

            if(!isAnimationEnd){
                invalidate();
            }
        }

    }


    private int sizeOfInt(int x) {
        for (int i = 0;; i++)
            if (x <= sizeTable[i])
                return i + 1;
    }

    /**
     * 设置最大值
     * @param maxValues
     */
    public void setMaxValues(float maxValues) {
        this.maxValues = maxValues;
        k = sweepAngle/maxValues;
    }

    /**
     * 设置当前值
     * @param currentValues
     */
    public void setCurrentValues(float currentValues) {
        if (currentValues > maxValues) {
            currentValues = maxValues;
        }
        if (currentValues < 0) {
            currentValues = 0;
        }
        this.curValues = currentValues;
        lastAngle = currentAngle;
        if(isAnim){
            setAnimation(lastAngle, currentValues * k, aniSpeed);
        }else{
            currentAngle = currentValues * k;
            isAnimationEnd = true;
            this.invalidate();
        }
    }



    /**
     * 设置写入的文字
     * @param values
     */
    public void setCurrentString(String values){
        curString = values;
        this.invalidate();
        isAnimationEnd = true;
    }


    public void setIsAnim(boolean isAnim) {
        this.isAnim = isAnim;
    }

    /**
     * 为进度设置动画
     * @param last
     * @param current
     */
    private void setAnimation(float last, float current, int length) {
        progressAnimator = ValueAnimator.ofFloat(last, current);
        progressAnimator.setDuration(length);
        progressAnimator.setTarget(currentAngle);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentAngle = (float) animation.getAnimatedValue();
                curValues = currentAngle / k;
            }
        });
        progressAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isAnimationEnd = true;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        progressAnimator.start();
    }

    /**
     * dip 转换成px
     * @param dip
     * @return
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int)(dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }
}
