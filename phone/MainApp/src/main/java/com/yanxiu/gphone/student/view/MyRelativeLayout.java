package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2016/8/26.
 */
public class MyRelativeLayout extends RelativeLayout{

    public MyRelativeLayout(Context context) {
        this(context,null,0);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
//        if (heightMeasureSpec>260){
//            setLayoutParams(new LayoutParams(widthMeasureSpec,260));
//        }
        this.post(new Runnable() {
            @Override
            public void run() {
                int height=getHeight();
                int width=getWidth();
                if (height>260){
                    LayoutParams params=new LayoutParams(width,260);
                    setLayoutParams(params);
                }else {
                    LayoutParams params=new LayoutParams(width,height);
                    setLayoutParams(params);
                }
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (b-t>260){
//            LayoutParams params=new LayoutParams(r-l,260);
//            params.

        }else {

        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
