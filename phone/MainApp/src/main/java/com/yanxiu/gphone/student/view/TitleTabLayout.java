package com.yanxiu.gphone.student.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.utils.Util;

/**
 * Created by lidm on 2015/10/29.
 */
public class TitleTabLayout extends LinearLayout implements View.OnClickListener{

    private Context mContext;

    private LayoutInflater inflater;

    private TextView tvTabLeft, tvTabRight;

    public TitleTabLayout(Context context) {
        super(context);
        initView(context);
    }

    public TitleTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TitleTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public OnTitleTabClick getOnTitleTabClick() {
        return onTitleTabClick;
    }

    public void setOnTitleTabClick(OnTitleTabClick onTitleTabClick) {
        this.onTitleTabClick = onTitleTabClick;
    }

    private void initView(Context context){

        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.setOrientation(LinearLayout.HORIZONTAL);

        inflater.inflate(R.layout.title_tab, this);

        tvTabLeft = (TextView) this.findViewById(R.id.tv_tab_left);
        tvTabLeft.setText(mContext.getResources().getString(R.string.tab_chapter));

        tvTabRight = (TextView) this.findViewById(R.id.tv_tab_right);
        tvTabRight.setText(mContext.getResources().getString(R.string.tab_test_center));

        tvTabLeft.setOnClickListener(this);
        tvTabRight.setOnClickListener(this);
        Util.setViewTypeface(YanxiuTypefaceTextView.TypefaceType.FANGZHENG, tvTabLeft, tvTabRight);
    }


    public void setLeftSelected(){
        tvTabLeft.setTextColor(mContext.getResources().getColor(R.color.color_805500));
        setSelShadowView(tvTabLeft, mContext.getResources().getColor(R.color.color_ffff99));
        tvTabLeft.setBackgroundResource(R.drawable.slide_toggle);
        setSelShadowView(tvTabRight, mContext.getResources().getColor(R.color.color_005959));
        tvTabRight.setTextColor(mContext.getResources().getColor(R.color.color_33ffff));
        tvTabRight.setBackgroundDrawable(null);
    }

    public void setRightSelected(){
        tvTabRight.setTextColor(mContext.getResources().getColor(R.color.color_805500));
        setSelShadowView(tvTabRight, mContext.getResources().getColor(R.color.color_ffff99));
        tvTabRight.setBackgroundResource(R.drawable.slide_toggle);
        setSelShadowView(tvTabLeft, mContext.getResources().getColor(R.color.color_005959));
        tvTabLeft.setTextColor(mContext.getResources().getColor(R.color.color_33ffff));
        tvTabLeft.setBackgroundDrawable(null);
    }

    private void setSelShadowView(TextView view, int colorRes){
        view.setShadowLayer(1, 0, 2, colorRes);
    }
    @Override
    public void onClick(View v) {
        if(v == tvTabLeft){
            if(onTitleTabClick != null){
                onTitleTabClick.onLeftClick();
            }
            setLeftSelected();
        }else if(v == tvTabRight){
            setRightSelected();
            if(onTitleTabClick != null){
                onTitleTabClick.onRightClick();
            }
        }
    }


    private OnTitleTabClick onTitleTabClick;

    public interface OnTitleTabClick{
        void onLeftClick();
        void onRightClick();
    }


}
