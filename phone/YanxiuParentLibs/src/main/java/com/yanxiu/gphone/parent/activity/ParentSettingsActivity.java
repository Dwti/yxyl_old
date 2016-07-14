package com.yanxiu.gphone.parent.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.activity.base.TopViewBaseActivity;

/**
 * Created by lidongming on 16/3/18.
 * 家长端-------设置界面
 */
public class ParentSettingsActivity extends TopViewBaseActivity{

    private View mView;

    private RelativeLayout rlModify;
    private RelativeLayout rlAbout;
//    private RelativeLayout rlPolicy;

    public static void launch(Activity context) {
        Intent intent = new Intent(context, ParentSettingsActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected View getContentView() {
        mView= LayoutInflater.from(this).inflate(R.layout.parent_settings_layout,null);
        initView();
        initData();
        return mView;
    }

    private void initData() {
        titleText.setText(this.getResources().getString(R.string.settings_name));
    }


    private void initView(){

        rlModify = (RelativeLayout) mView.findViewById(R.id.modify_layout);
        rlAbout = (RelativeLayout) mView.findViewById(R.id.about_layout);
//        rlPolicy = (RelativeLayout) mView.findViewById(R.id.policy_layout);


        rlModify.setOnClickListener(this);
        rlAbout.setOnClickListener(this);
//        rlPolicy.setOnClickListener(this);
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
    public void onClick(View view) {
        super.onClick(view);
        if(rlModify == view){
            ParentModifyPwdActivity.launch(this);
        }else if(rlAbout == view){
            ParentsAboutActivity.launch(this);
        }
//        else if(rlPolicy == view){
//            ParentWebViewActivity.launch(this, YanxiuParentConstants.PRIVACY_POLICY_URL);
//        }
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

    @Override
    protected boolean isAttach() {
        return false;
    }


}
