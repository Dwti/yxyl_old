package com.yanxiu.gphone.hd.student.view.question;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.LruCache;
import android.view.Gravity;
import android.view.View;
import com.common.core.utils.DensityUtils;
import com.common.core.utils.imageloader.UilImageGetter;
import com.common.core.view.htmlview.HtmlTextView;
import com.yanxiu.gphone.hd.student.YanxiuApplication;

/**
 * Created by Administrator on 2015/7/10.
 *
 */
public class YXiuAnserTextView extends HtmlTextView {

    public Context mCtx;
    private YanxiuApplication application;
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

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
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

    /**
     * 缓存json数据
     */
    private static LruCache<String, Spanned> mSpannedCache;



    public void setTextHtml(String text) {
        mRelayout = true;

        UilImageGetter imageGetter = new UilImageGetter(this, mCtx, this.application);

        Spanned spanned = Html.fromHtml(text, imageGetter, null);

        this.setText(spanned);
    }

    /**
     * 初始化view
     */
    protected void initView(){
        this.application = (YanxiuApplication) ((Activity)mCtx).getApplication();
        this.setGravity(Gravity.CENTER_VERTICAL);
        this.setLineSpacing(DensityUtils.px2dip(this.getContext(), 15), 1);
    }


}
