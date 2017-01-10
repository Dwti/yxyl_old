package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.speech.RecognitionListener;
import android.util.AttributeSet;
import android.view.View;

import com.yanxiu.gphone.student.R;


/**
 * 视频录制开始 暂停 按钮
 *
 * @author fengrongcheng
 *         created at 16-8-8 下午6:07
 */

public class RecordVideoStatueCircle extends View {
    /**
     * 内部圆的画笔
     */
    private Paint mCirclePaint;
    /**
     * 外部圆环的画笔
     */
    private Paint mRingPaint;
    /**
     * 内部正方形的画笔
     */
    private Paint mRectPaint;

    /**
     * 内部圆形半径
     */
    private float mRadius;
    /**
     * 内部圆形的颜色
     */
    private int mCircleColor;
    /**
     * 外部圆环半径
     */
    private float mRingWidth;
    /**
     * 外部圆环宽度
     */
    private float mStorkeWidth;
    /**
     * 外部圆环颜色
     */
    private int mRingColor;
    /**
     * 中间正方形的宽
     */
    private float mRectWidth;
    /**
     * 中间正方形的颜色
     */
    private int mRectColor;
    /**
     * 中间正方形圆角半径
     */
    private float mRectRoundW;
    //private RECORD_STATUE statue = RECORD_STATUE.STOP;

    public RecordVideoStatueCircle(Context context) {
        this(context, null);
    }

    public RecordVideoStatueCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordVideoStatueCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initVariable();

    }


    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RecordVideoStatueCircle, 0, 0);
        mRadius = typedArray.getDimension(R.styleable.RecordVideoStatueCircle_statueCircleWidth, 150);
        mRingWidth = typedArray.getDimension(R.styleable.RecordVideoStatueCircle_ringWidth, 200);
        mStorkeWidth = typedArray.getDimension(R.styleable.RecordVideoStatueCircle_ringStrokeWidth, 25);
        mCircleColor = typedArray.getColor(R.styleable.RecordVideoStatueCircle_statueCircleColor, 0xffffff);
        mRingColor = typedArray.getColor(R.styleable.RecordVideoStatueCircle_ringCol, 0xffffff);
        mRectWidth = typedArray.getDimension(R.styleable.RecordVideoStatueCircle_rectW, 100);
        mRectColor = typedArray.getColor(R.styleable.RecordVideoStatueCircle_rectCol, 0xffffff);
        mRectRoundW = typedArray.getDimension(R.styleable.RecordVideoStatueCircle_rectRoundW, 10);
        typedArray.recycle();
    }

    private void initVariable() {
        /**中间正方形的画笔**/
        mRectPaint = new Paint();
        mRectPaint.setAntiAlias(true);
        mRectPaint.setColor(mRectColor);
        mRectPaint.setStyle(Paint.Style.FILL);
        /**内部圆形的画笔**/
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.FILL);
        /**外部圆环的画笔**/
        mRingPaint = new Paint();
        mRingPaint.setColor(mRingColor);
        mRingPaint.setAntiAlias(true);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(mStorkeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float mXCenter = getWidth() / 2;
        /**画外部的圆环**/

        canvas.drawCircle(mXCenter, mXCenter, mRingWidth - mStorkeWidth / 2, mRingPaint);
        //if (statue == RECORD_STATUE.STOP) {
            canvas.drawCircle(mXCenter, mXCenter, mRadius, mCirclePaint);
        /*} else {
            RectF rectF = new RectF(mXCenter - mRectWidth / 2, mXCenter - mRectWidth / 2, mXCenter + mRectWidth / 2, mXCenter + mRectWidth / 2);
            canvas.drawRoundRect(rectF, mRectRoundW, mRectWidth, mRectPaint);

        }*/

    }


    /**
     * 设置显示的状态
     *
     * @param statue
     */
    /*public void setCircleStatue(RECORD_STATUE statue) {
        this.statue = statue;

        invalidate();
    }*/
}
