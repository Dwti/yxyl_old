package com.yanxiu.gphone.student.view.question;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.DensityUtils;
import com.common.core.utils.imageloader.UilImageGetter;
import com.common.core.view.htmlview.HtmlTextView;
import com.yanxiu.gphone.student.HtmlParser.MyHtml;
import com.yanxiu.gphone.student.YanxiuApplication;
import com.yanxiu.gphone.student.utils.ClassfyImageGetter;
import com.yanxiu.gphone.student.utils.SpanCenterUtils;
import com.yanxiu.gphone.student.utils.Util;

import org.xml.sax.XMLReader;

/**
 * Created by Administrator on 2015/7/10.
 *
 */
public class YXiuAnserTextView extends HtmlTextView {

    public Context mCtx;
    private YanxiuApplication application;
    private UilImageGetter imageGetter;
    private ClassfyImageGetter classfyImageGetter;
    private int singline_height;

    public YXiuAnserTextView(Context context) {
        super(context);
        mCtx = context;
        initView();
    }

    public YXiuAnserTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCtx = context;
        initView();
    }

    public YXiuAnserTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCtx = context;
        initView();
    }


    private boolean mRelayout;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // If no change, measure and return
        if (!mRelayout || getVisibility() == View.GONE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        // Measure
        super.onMeasure(widthMeasureSpec, this.getMeasuredHeight());



    }
    private boolean htmlFlag=true;
    public void setHtmlFlag(boolean htmlFlag){
        this.htmlFlag=htmlFlag;
    }

    private boolean clasfyFlag=true;
    public void setClasfyFlag(boolean clasfyFlag){
        this.clasfyFlag=clasfyFlag;
    }

    public void setTextHtml(String text) {

        mRelayout = true;
        /**
         * 因原始框架问题，为暂时避免bug，先用以下方式处理，以后重构时修复
         * */

//        if (htmlFlag){
//            String[] message=text.split("<br/>");
//            text=message[0];
//        }
//        text="某校从参加高一年级期末考试的学生中抽出60名学生，并统计了他们的物理成绩（成绩均为整数且满分为100分）<br><img src=\"http://scc.jsyxw.cn/tizi/qf1/images/3/c/1/3c186ffab98f01fdb04067d6364dda53b1c59f14.jpg\" >";

        if (clasfyFlag) {
            imageGetter = new UilImageGetter(this, mCtx, this.application);
            Spanned spanned = Html.fromHtml(text, imageGetter, null);
            spanned= SpanCenterUtils.getInstence().getSpan(spanned);
            this.setText(spanned);
        } else {
            classfyImageGetter = new ClassfyImageGetter(this, mCtx);
            Spanned spanned = MyHtml.fromHtml(mCtx, text, classfyImageGetter, null, null, null);
            this.setText(spanned);
        }

//        setHtmlFromString();
//        this.setText(text);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (clasfyFlag) {
            return super.onTouchEvent(event);
        }else {
            return false;
        }
    }

    private int height=0;

    public int getClassfyHeight(){
        return height;
    }

    public void setClassfyheight(int height){
        this.height=height;
    }

    public int getFontHeight()
    {
        Paint.FontMetrics fm = getPaint().getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

    /**
     * 初始化view
     */

    protected void initView(){
        this.application = (YanxiuApplication) ((Activity)mCtx).getApplication();
        this.setGravity(Gravity.CENTER_VERTICAL);
        this.setLineSpacing(DensityUtils.px2dip(this.getContext(), 15), 1);
//        int textsize= Util.getTextPX((Activity)mCtx);
        this.setTextSize(15);//默认的为sp
        singline_height=getFontHeight();//32
    }


    public void setLineSpace(int px){
        this.setLineSpacing(DensityUtils.px2dip(this.getContext(), px), 1);
    }


    private boolean isSendheight=false;

    public void setIsSendHeight(boolean isSendheight){
        this.isSendheight=isSendheight;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (isSendheight) {
            int linecount = getLineCount();
            int tatle_height = getTrueHeight(linecount);
            setHeight(tatle_height);
            if (imageGetter!=null&&isSendheight){
                imageGetter.setTrueHeight(tatle_height);
            }
            /*if (classfyImageGetter!=null&&isSendheight){
                classfyImageGetter.setTrueHeight(tatle_height);
            }*/
        }
    }

    private int getTrueHeight(int linecount){
        int tatle_height=0;
        for (int i=0;i<linecount-1;i++){
            tatle_height=tatle_height+singline_height+30;
        }
        return tatle_height;
    }
}
