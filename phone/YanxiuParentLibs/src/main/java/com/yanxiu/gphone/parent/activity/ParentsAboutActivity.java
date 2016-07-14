package com.yanxiu.gphone.parent.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.activity.base.TopViewBaseActivity;
import com.yanxiu.gphone.parent.contants.YanxiuParentConstants;

/**
 * Created by lidongming on 16/3/21.
 * 关于界面
 */
public class ParentsAboutActivity extends TopViewBaseActivity {

    private View mView;
    
    private View cluasePolicyLayout;


    public static void launch(Activity context) {
        Intent intent = new Intent(context, ParentsAboutActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLaunchIntentData() {

    }

    @Override
    protected boolean isAttach() {
        return false;
    }

    @Override
    protected View getContentView() {
        mView= LayoutInflater.from(this).inflate(R.layout.parent_about_layout,null);
        initView();
        initData();
        return mView;
    }

    private void initData() {
        titleText.setText(this.getResources().getString(R.string.parent_about_us_txt));
    }

    @Override
    protected void setRootView() {
        super.setRootView();
        rootView.setBackgroundResource(R.drawable.login_in_common_bg);
    }



    @Override
    protected void setTopView() {
        super.setTopView();
        topRootView.setBackgroundColor(getResources().getColor(android.R.color.white));
    }

    @Override
    protected void setContentListener() {

    }

    @Override
    protected void destoryData() {

    }

    private void initView(){
        cluasePolicyLayout = mView.findViewById(R.id.cluase_policy_layout);

        cluasePolicyLayout.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view == cluasePolicyLayout){
            ParentWebViewActivity.launch(this, YanxiuParentConstants.PRIVACY_POLICY_URL);
        }

    }



}
