package com.yanxiu.gphone.student.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.utils.Util;

/**
 * Created by Administrator on 2015/10/31.
 */
public class ChapterTabTitleLayout extends FrameLayout implements View.OnClickListener{

    private Context mContext;


    private LayoutInflater inflater;
    private LinearLayout llRightBack;
//    private ImageView ivBack;
    private ImageView ivRight;
    private TextView intellVolumeTxt;
    private TextView intellVolumeTxtTest;

    private TextView tvLeftTitle;
    private TextView tvCenterTitle;

    private RelativeLayout rlRight;
    private View rlRightTestView;

    public ChapterTabTitleLayout(Context context) {
        super(context);
        initView(context);
    }

    public ChapterTabTitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ChapterTabTitleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context){
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        inflater.inflate(R.layout.chapter_tab_title_layout, this);

//        ivBack = (ImageView) findViewById(R.id.iv_top_back);
        ivRight = (ImageView) findViewById(R.id.iv_right);
        rlRightTestView = findViewById(R.id.rl_right_test);
        rlRight = (RelativeLayout) findViewById(R.id.rl_right);
        tvLeftTitle = (TextView) findViewById(R.id.tv_left_title);
        tvCenterTitle = (TextView) findViewById(R.id.tv_center_title);

        intellVolumeTxt = (TextView) this.findViewById(R.id.intell_volume_txt);
        intellVolumeTxtTest = (TextView) this.findViewById(R.id.intell_volume_txt_test);
        Util.setViewTypeface(YanxiuTypefaceTextView.TypefaceType.FANGZHENG, intellVolumeTxtTest);
        llRightBack = (LinearLayout) findViewById(R.id.ll_top_back);
        llRightBack.setOnClickListener(this);
//        ivBack.setOnClickListener(this);
    }
    public void setLeftTitle(String title){
        tvLeftTitle.setText(title);
    }

    public void setCenterTitle(String title){
        tvCenterTitle.setText(title);
    }

    public RelativeLayout getRlRight() {
        return rlRight;
    }


    public void setIntellVolumeTxt(String volumeName){
        intellVolumeTxt.setText(volumeName);
    }

    public void setIntellVolumeTxtSize(int textSize){
//        intellVolumeTxt.setTextSize(Util.dipToPx(textSize));
        intellVolumeTxt.setTextSize(textSize);
    }

    public void setIntellVolumeTxtMargin(int margin){
//        intellVolumeTxt.setTextSize(Util.dipToPx(textSize));
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) intellVolumeTxt.getLayoutParams();
        lp.rightMargin = margin;
        intellVolumeTxt.setLayoutParams(lp);
//        intellVolumeTxt.setTextSize(textSize);
    }

    public void setRightImageViewDisp(boolean isChapter){
        if(isChapter){
            rlRight.setVisibility(VISIBLE);
            rlRightTestView.setVisibility(GONE);
        } else {
            rlRightTestView.setVisibility(VISIBLE);
            rlRight.setVisibility(GONE);
        }
    }


    @Override
    public void onClick(View v) {
        if (v == llRightBack) {
            ((Activity)mContext).finish();
        }
    }

}
