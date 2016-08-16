package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;

/**
 * Created by sunpeng on 2016/7/26.
 */
public class TitleView extends FrameLayout {

    private ImageView iv_title;
    public TitleView(Context context) {
        super(context);
        initView(context);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.titleview,this,true);
        iv_title = (ImageView) view.findViewById(R.id.iv_title);
    }

    public void setTitleImage(int resId){
        if(iv_title!=null){
            iv_title.setImageResource(resId);
        }
    }

}
