package com.yanxiu.gphone.studentold.activity;

import android.view.View;

import com.yanxiu.gphone.studentold.R;

/**
 * Created by Administrator on 2016/5/3.
 */
public class NoGroupAddTipsActivity extends  TopViewBaseActivity {


    @Override
    protected void setTopView() {
        bottomView.setVisibility(View.VISIBLE);
        titleText.setText(getResources().getString(R.string.group_add_tips_title));
    }

    @Override
    protected void setContentContainerView() {
        contentContainer.setPadding(getResources().getDimensionPixelOffset(R.dimen.dimen_20),getResources().getDimensionPixelOffset(R.dimen.dimen_25),getResources().getDimensionPixelOffset(R.dimen.dimen_20),0);
        contentContainer.setBackgroundResource(0);
        contentContainer.setBackgroundColor(getResources().getColor(R.color.color_fff0b2));
    }


    @Override
    protected void setRootView() {
        rootView.setBackgroundResource(0);
    }

    @Override
    protected boolean isAttach() {
        return true;
    }

    @Override
    protected View getContentView() {

        return getAttachView(R.layout.no_group_add_tips_activity);
    }

    @Override
    protected void setContentListener() {

    }

    @Override
    protected void destoryData() {

    }

    @Override
    protected void initLaunchIntentData() {

    }
}
