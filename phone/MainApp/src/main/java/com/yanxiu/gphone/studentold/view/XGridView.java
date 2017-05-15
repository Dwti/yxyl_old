package com.yanxiu.gphone.studentold.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.yanxiu.gphone.studentold.R;

/**
 * Created by sunpeng on 2016/8/11.
 */
public class XGridView extends FrameLayout {
    private View mView;
    private TitleView titleView;
    private GridView gridView;
    public XGridView(Context context) {
        super(context);
    }

    public XGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context){
        mView= LayoutInflater.from(context).inflate(R.layout.xgridview,this,true);
        titleView = (TitleView) mView.findViewById(R.id.titleView);
        gridView = (GridView) mView.findViewById(R.id.gridView);
    }
}
