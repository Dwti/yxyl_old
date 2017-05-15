package com.yanxiu.gphone.studentold.exampoint.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.bean.ChartBarEntity;

import java.util.List;

public class BarChartPanel extends  View{
    private final static String TAG=BarChartPanel.class.getSimpleName();
    private List<ChartBarEntity> series;
    public final static int[] platterTable = new int[]{R.color.color_ff99bb, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN};
    private int width ;
    private int height;
    private int drawTextOffset= (int) getResources().getDimension(R.dimen.dimen_10);
    private int itemHeight;
    private Context mContext;
    private  int barWidth;
    private   int xUnit;
    private int dataSize;
    private static final float MAX=100.0f;
    private static final float MIN=0.0f;
    private static final int PERCENT=100;

    private int mPaintTimes = 0;
    private static final int TOTAL_PAINT_TIMES = 50;
    private boolean mIsAnimaionRun = false;


    public BarChartPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
        init();

    }  
      
    public BarChartPanel(Context context) {
        super(context);
        this.mContext=context;
        init();

    }  
    public BarChartPanel(Context context, AttributeSet attrs, int defStyle) {
		
		super(context, attrs, defStyle);
        this.mContext=context;
		init();

	}
    private void init(){
        barWidth = (int) mContext.getResources().getDimension(R.dimen.dimen_20);
        xUnit= (int) mContext.getResources().getDimension(R.dimen.dimen_60);
        LogInfo.log(TAG, "init");

    }

    private boolean isSupportHardAcc(){
        return CommonCoreUtil.getSDK() >= Build.VERSION_CODES.HONEYCOMB;
    }


    @Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LogInfo.log(TAG,"onLayout");
        initSetMeasure(dataSize);
    }


    private void setMeasure(int width,int height){
        setMinimumWidth(width);
        setMinimumHeight(height);
        setMeasuredDimension(width, height);
    }
    private void initSetMeasure(int dataSize){
        LogInfo.log(TAG, "HEIGHT: " + height);
        int widthOffset= CommonCoreUtil.dipToPx(mContext, 50);
        if(dataSize<=1){
            width=barWidth*dataSize+(xUnit*(dataSize))+widthOffset;
        }else{
            width=barWidth*dataSize+(xUnit*(dataSize-1))+widthOffset;
        }

        LogInfo.log(TAG, "size: " + (dataSize) + "measureWidth: " + width);
        setMeasure(width, (int) (height + getResources().getDimension(R.dimen.dimen_47)));
    }

    public void setParams(List<ChartBarEntity> series,int itemHeight,int totalValueHeight) {
        this.series = series;
        this.height=totalValueHeight;
        this.itemHeight=itemHeight;
        dataSize=series.size();
        initSetMeasure(dataSize);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        LogInfo.log(TAG,"onLayout");
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void onDraw(Canvas canvas) {
        drawChart(canvas,isSupportHardAcc());
    }

    private void drawChart(Canvas canvas,boolean isSupportHard) {
        if(isSupportHard){
            if(!mIsAnimaionRun){
                return;
            }
            mPaintTimes++;
        }
        @SuppressLint("DrawAllocation")
        Paint myPaint = new Paint();
        // draw XY Axis
        int xOffset = 0;

        if (series == null) {
            getMockUpSeries();
        }
        int xPadding = CommonCoreUtil.dipToPx(mContext, 12);
        if (series != null) {
            @SuppressLint("DrawAllocation")
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(mContext.getResources().getColor(R.color.color_805500));
            textPaint.setStrokeWidth(2);
            textPaint.setTextSize(CommonCoreUtil.dipToPx(mContext, 10));
            textPaint.setAntiAlias(true);
            textPaint.setStyle(Style.FILL);

            for (int i = 0; i < dataSize; i++) {
                String text = handlerText(series.get(i).getTitle());
                if (!StringUtils.isEmpty(text)) {
                    @SuppressLint("DrawAllocation")
                    StaticLayout layout = new StaticLayout(text, textPaint, width,
                            Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
                    canvas.save();
                    if (text.length() >= 5) {
                        canvas.translate(xOffset + xPadding / 2 - CommonCoreUtil.dipToPx(mContext, 1) + xUnit * i, height + drawTextOffset);
                    } else if (text.length() >= 4 && text.length() < 5) {
                        canvas.translate(xOffset - CommonCoreUtil.dipToPx(mContext, 2) + xPadding + xUnit * i, height + drawTextOffset);
                    } else if (text.length() >= 3 && text.length() < 4) {
                        canvas.translate(xOffset + CommonCoreUtil.dipToPx(mContext, 3) + xPadding + xUnit * i, height + drawTextOffset);
                    } else if (text.length() >= 2 && text.length() < 3) {
                        canvas.translate(xOffset + CommonCoreUtil.dipToPx(mContext, 9) + xPadding + xUnit * i, height + drawTextOffset);
                    } else {
                        canvas.translate(xOffset + CommonCoreUtil.dipToPx(mContext, 13) + xPadding + xUnit * i, height + drawTextOffset);
                    }

                    layout.draw(canvas);
                    canvas.restore();
                }

            }



            // clear the path effect
            myPaint.setColor(Color.BLACK);
            myPaint.setStyle(Style.STROKE);
            myPaint.setStrokeWidth(0);
            myPaint.setPathEffect(null);

            myPaint.setStyle(Style.FILL);
            myPaint.setStrokeWidth(0);
            myPaint.setAntiAlias(true);

            Paint  vericalPaint = new Paint();
            vericalPaint.setColor(getResources().getColor(R.color.color_f5f1e2));
            vericalPaint.setStyle(Style.FILL);
            vericalPaint.setStrokeWidth(getResources().getDimension(R.dimen.dimen_1dp));


            for (int i = 0; i < dataSize; i++) {
                int startPos;
                    if(i==0){
                        startPos = xUnit/2-barWidth/2;

                    }else{
                        startPos = xUnit/2+ xUnit * i-barWidth/2;
                    }




                float value;
                if (series.get(i).getDataElement().getValue() > MAX) {
                    value = MAX;
                } else if (series.get(i).getDataElement().getValue() < MIN) {
                    value = MIN;
                } else {
                    value = series.get(i).getDataElement().getValue();
                }
                /**
                 * height-itemHeight  除去多余高度
                 */
                float barHeight = value * (height-itemHeight) / 100;


                if (barHeight > 0) {
                    if (isSupportHard) {
                        float paintYPos = barHeight / TOTAL_PAINT_TIMES * mPaintTimes;
                        drawBar(canvas,series.get(i).getDataElement().getColor(),startPos, (int) (height - paintYPos), startPos + barWidth, height);
                    }else {
                        drawBar(canvas,series.get(i).getDataElement().getColor(),startPos,height-(int) barHeight,startPos + barWidth,height);
                    }
                    float startx= startPos + barWidth+xUnit/3;
                    float endY =getMeasuredHeight();
                    LogInfo.log(TAG,"drawVericalLine_Height: "+endY);
                    canvas.drawLine(startx,0,startx,endY,vericalPaint);

                }


            }

        }

        if(isSupportHard&&mPaintTimes < TOTAL_PAINT_TIMES ) {
            invalidate();
        }
    }


    private void drawBar(Canvas canvas,int color,int left, int top, int right, int bottom){
        int radius = (int) mContext.getResources().getDimension(R.dimen.dimen_4);
        float[] outerR = new float[]{radius, radius, radius, radius, 0, 0, 0, 0};
        @SuppressLint("DrawAllocation")
        ShapeDrawable mDrawables = new ShapeDrawable(new RoundRectShape(outerR, null, null));
        mDrawables.getPaint().setColor(getResources().getColor(color));
        mDrawables.setBounds(left,top,right,bottom);
        mDrawables.draw(canvas);
    }


    public void startCanvas() {
        mPaintTimes=0;
        mIsAnimaionRun = true;
        invalidate();
    }




    private String handlerText(String text) {
        if(StringUtils.isEmpty(text)){
            return "";
        }
        int length=text.length();
        int subLength = 10;
        if(length> subLength){
            String newText=text.substring(0, subLength-1);
            return getNewString(newText,length,subLength);
        }else{
            return getNewString(text,length,subLength);
        }
    }

    private String getNewString(String newText,int length,int subLength){
        StringBuilder stringBuilder=new StringBuilder();
        char[] array=newText.toCharArray();
        int record=1;
        for(char s:array){
            record++;
            stringBuilder.append(s);
            int rowTextNum = 5;
            if(record> rowTextNum){
                stringBuilder.append("\r\n");
                record=1;
            }
        }

        if(length>subLength){
            stringBuilder.append("...");
            return stringBuilder.toString();
        }
        return stringBuilder.toString();
    }

    private List<ChartBarEntity>  getMockUpSeries() {
        return series;
    }


    /**
     * 数值转换为百分数
     * @param value
     * @return
     */
    public  static  float convertValueToPercent(float value){
        return PERCENT*value;
    }

} 
