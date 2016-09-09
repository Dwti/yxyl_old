package com.yanxiu.gphone.student.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.core.utils.imageloader.UilImageGetter;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.YanxiuApplication;
import com.yanxiu.gphone.student.utils.ConnectImageGetter;

/**
 * Created by Administrator on 2016/9/1.
 */
public class ConnectTextView extends TextView implements View.OnClickListener{

    private Context context;
    private YanxiuApplication application;
    private OnCheckListener listener;
    private ConnectLinesLinearLayout.BaseBean bean;
    private ConnectImageGetter imageGetter;
    private OnLayoutSuccessListener successListener;

    public ConnectTextView(Context context) {
        this(context,null);
    }

    public ConnectTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ConnectTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context=context;
        this.application = (YanxiuApplication) ((Activity)context).getApplication();
        this.setOnClickListener(this);
        this.setTextColor(context.getResources().getColor(R.color.color_333333));
        this.setTextSize(24);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,10,0,10);
        this.setLayoutParams(params);
        this.setGravity(Gravity.CENTER);
    }

    public void setBaseBean(ConnectLinesLinearLayout.BaseBean bean){
        this.bean=bean;
    }

    public void setHtmlText(String text){
        imageGetter = new ConnectImageGetter(this, context);
        Spanned spanned = Html.fromHtml(text, imageGetter, null);
        this.setText(spanned);
    }

    public interface OnCheckListener{
        void OnCheckListener(ConnectLinesLinearLayout.BaseBean bean);
    }

    public interface OnLayoutSuccessListener{
        void OnLayoutSuccessListener();
    }

    public void setOnLayoutSuccessListener(OnLayoutSuccessListener successListener){
        this.successListener=successListener;
    }

    public void setCheckListener(OnCheckListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {
        listener.OnCheckListener(bean);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int y=(top+bottom)/2;
        if (bean!=null) {
            boolean b=false;
            if (y != bean.getY()) {
                b=true;
            }
            bean.setY(y);
            if (b) {
                successListener.OnLayoutSuccessListener();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
