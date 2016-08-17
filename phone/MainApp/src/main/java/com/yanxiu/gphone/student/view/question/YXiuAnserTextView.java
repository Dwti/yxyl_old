package com.yanxiu.gphone.student.view.question;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.DensityUtils;
import com.common.core.utils.imageloader.UilImageGetter;
import com.common.core.view.htmlview.HtmlTextView;
import com.yanxiu.gphone.student.YanxiuApplication;

import org.xml.sax.XMLReader;

/**
 * Created by Administrator on 2015/7/10.
 *
 */
public class YXiuAnserTextView extends HtmlTextView {

    public Context mCtx;
    private YanxiuApplication application;
    private UilImageGetter imageGetter;
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


    public void setTextHtml(String text) {

        mRelayout = true;
//        text="某校从参加高一年级期末考试的学生中抽出60名学生，并统计了他们的物理成绩（成绩均为整数且满分为100分）<br><img src=\"http://scc.jsyxw.cn/tizi/qf1/images/3/c/1/3c186ffab98f01fdb04067d6364dda53b1c59f14.jpg\" >";
        imageGetter = new UilImageGetter(this, mCtx, this.application);
        Spanned spanned = Html.fromHtml(text, imageGetter, null);
        this.setText(spanned);
//        setHtmlFromString();
//        this.setText(text);
    }

    public int getFontHeight(float fontSize)
    {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

    /**
     * 初始化view
     */
    protected void initView(){
        this.application = (YanxiuApplication) ((Activity)mCtx).getApplication();
        this.setGravity(Gravity.CENTER_VERTICAL);
        this.setLineSpacing(DensityUtils.px2dip(this.getContext(), 15), 1);
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, 32);
        singline_height=getFontHeight(32);
    }

    private boolean isSendheight=false;

    public void setIsSendHeight(boolean isSendheight){
        this.isSendheight=isSendheight;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (imageGetter!=null&&isSendheight){
            int linecount=getLineCount();
            int tatle_height=getTrueHeight(linecount);
            setHeight(tatle_height);
            imageGetter.setTrueHeight(tatle_height);

        }
    }

    private int getTrueHeight(int linecount){
        int tatle_height=0;
        for (int i=0;i<linecount-1;i++){
            tatle_height=tatle_height+singline_height+30;
        }
        return tatle_height;
    }

    public class MxgsaTagHandler implements Html.TagHandler {
        private int sIndex = 0;
        private  int eIndex=0;
        private final Context mContext;

        public MxgsaTagHandler(Context context){
            mContext=context;
        }

        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
            // TODO Auto-generated method stub
            if (tag.toLowerCase().equals("mxgsa")) {
                if (opening) {
                    sIndex=output.length();
                }else {
                    eIndex=output.length();
                    output.setSpan(new MxgsaSpan(), sIndex, eIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        private class MxgsaSpan extends ClickableSpan implements OnClickListener{
            @Override
            public void onClick(View widget) {
                // TODO Auto-generated method stub
                //具体代码，可以是跳转页面，可以是弹出对话框，下面是跳转页面
            }
        }

    }

}
