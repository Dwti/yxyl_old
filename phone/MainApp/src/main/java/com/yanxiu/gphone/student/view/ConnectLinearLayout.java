package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.yanxiu.gphone.student.R;

/**
 * Created by Administrator on 2016/9/12.
 */
public class ConnectLinearLayout extends LinearLayout{

    private Context context;
    private OnLayoutSuccessListener successListener;
    private ConnectTextView textview_left;
    private ConnectTextView textview_right;
    private ConnectLinesLinearLayout.BaseBean bean_left;
    private ConnectLinesLinearLayout.BaseBean bean_right;

    public ConnectLinearLayout(Context context) {
        this(context,null);
    }

    public ConnectLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ConnectLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context=context;
        LayoutInflater.from(context).inflate(R.layout.connect_linearlayout,this);
        textview_left= (ConnectTextView) this.findViewById(R.id.textview_left);
        textview_right= (ConnectTextView) this.findViewById(R.id.textview_right);
    }

    public void setData_left(String text_left, ConnectLinesLinearLayout.BaseBean bean_left){
        this.bean_left=bean_left;
        bean_left.setTextView(textview_left);
        textview_left.setHtmlText(text_left);
        textview_left.setBaseBean(bean_left);
    }

    public void setData_right(String text_right, ConnectLinesLinearLayout.BaseBean bean_right){
        this.bean_right=bean_right;
        bean_right.setTextView(textview_right);
        textview_right.setHtmlText(text_right);
        textview_right.setBaseBean(bean_right);
    }

    public void setCheckListener(ConnectTextView.OnCheckListener listener){
        textview_left.setCheckListener(listener);
        textview_right.setCheckListener(listener);
    }

    public void setBackgroud(int color){
        textview_left.setBackgroundResource(color);
        textview_right.setBackgroundResource(color);
    }

    public void setOnLayoutSuccessListener(OnLayoutSuccessListener successListener){
        this.successListener=successListener;
    }

    public interface OnLayoutSuccessListener{
        void OnLayoutSuccessListener();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int y=(top+bottom)/2;
        if (bean_left!=null&&bean_right!=null) {
            boolean b=false;
            if (y != bean_left.getY()||y != bean_right.getY()) {
                b=true;
            }
            bean_left.setY(y);
            bean_right.setY(y);
            if (b) {
                successListener.OnLayoutSuccessListener();
            }
        }
    }
}
