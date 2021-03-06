package com.yanxiu.gphone.studentold.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.login.LoginModel;
import com.igexin.sdk.PushManager;
import com.jakewharton.scalpel.ScalpelFrameLayout;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.base.YanxiuBaseActivity;
import com.yanxiu.gphone.studentold.preference.PreferencesManager;
import com.yanxiu.gphone.studentold.upgrade.UpgradeUtils;
import com.yanxiu.gphone.studentold.utils.Util;
import com.yanxiu.gphone.studentold.view.YanxiuTypefaceTextView;

/**
 * Created by Administrator on 2015/7/13.
 */
public class UserSettingActivity extends YanxiuBaseActivity implements View.OnClickListener{
    private View topView,bindMobileView, modifyPwdView, updateView, aboutUsView, logOutView,
            pwdDividerLine,bindMobileLine;
    private View backView;
    private TextView topTitle,bindMobileTV, modifyTV, updateTv, aboutUsTv;
    private ScalpelFrameLayout mScalpelView;
    public static void launchActivity(Activity context) {
        Intent intent = new Intent(context, UserSettingActivity.class);
        context.startActivity(intent);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_user_layout);
        findView();
    }
    private void findView(){
        topView = findViewById(R.id.top_layout);
        backView = topView.findViewById(R.id.pub_top_left);
        topTitle = (TextView) topView.findViewById(R.id.pub_top_mid);
        topTitle.setText(R.string.my_setting_name);
        bindMobileView = findViewById(R.id.setting_modify_bind_mobile_layout);
        bindMobileLine = findViewById(R.id.setting_bind_mobile_dividerline);
        modifyPwdView = findViewById(R.id.setting_modify_pwd_layout);
        updateView = findViewById(R.id.setting_update_layout);
        aboutUsView = findViewById(R.id.setting_about_us_layout);
        logOutView = findViewById(R.id.login_out_txt);
        pwdDividerLine = findViewById(R.id.setting_modify_pwd_dividerline);
        Util.setViewTypeface(YanxiuTypefaceTextView.TypefaceType.FANGZHENG, (TextView) logOutView);

        bindMobileTV = (TextView) bindMobileView.findViewById(R.id.name);
        modifyTV = (TextView) modifyPwdView.findViewById(R.id.name);

        updateTv = (TextView) updateView.findViewById(R.id.name);
        aboutUsTv = (TextView) aboutUsView.findViewById(R.id.name);

        ImageView bindMobileLeftIcon = (ImageView) bindMobileView.findViewById(R.id.left_icon);
        bindMobileLeftIcon.setVisibility(View.GONE);
        ImageView bindMobileRightIcon = (ImageView) bindMobileView.findViewById(R.id.right_arrow);
        bindMobileRightIcon.setVisibility(View.VISIBLE);

        ImageView modifyLeftIcon=(ImageView)modifyPwdView.findViewById(R.id.left_icon);
        modifyLeftIcon.setVisibility(View.GONE);
        ImageView modifyRightIcon=(ImageView)modifyPwdView.findViewById(R.id.right_arrow);
        modifyRightIcon.setVisibility(View.VISIBLE);

        ImageView updateLeftIcon=(ImageView)updateView.findViewById(R.id.left_icon);
        updateLeftIcon.setVisibility(View.GONE);
        ImageView updaterRightIcon=(ImageView)updateView.findViewById(R.id.right_arrow);
        updaterRightIcon.setVisibility(View.VISIBLE);

        ImageView aboutUsLeftIcon=(ImageView)aboutUsView.findViewById(R.id.left_icon);
        aboutUsLeftIcon.setVisibility(View.GONE);
        ImageView aboutUsRightIcon=(ImageView)aboutUsView.findViewById(R.id.right_arrow);
        aboutUsRightIcon.setVisibility(View.VISIBLE);

        bindMobileTV.setText(R.string.bind_mobile);
        modifyTV.setText(R.string.modify_pwd_txt);
        updateTv.setText(R.string.update_version_txt);
        aboutUsTv.setText(R.string.about_us_txt);


        backView.setOnClickListener(this);
        bindMobileView.setOnClickListener(this);
        modifyPwdView.setOnClickListener(this);

        updateView.setOnClickListener(this);
        aboutUsView.setOnClickListener(this);
        logOutView.setOnClickListener(this);
        Util.setViewTypeface(YanxiuTypefaceTextView.TypefaceType.FANGZHENG, ((TextView) logOutView));

        if(PreferencesManager.getInstance().getIsThirdLogIn()){
            bindMobileView.setVisibility(View.GONE);
            bindMobileLine.setVisibility(View.GONE);

            modifyPwdView.setVisibility(View.GONE);
            pwdDividerLine.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        UpgradeUtils.cancelUpgrade();
    }

    @Override
    public void onClick(View v) {
        if(v == backView){
            finish();
        } else if(v == bindMobileView){
            if(TextUtils.isEmpty(LoginModel.getBindMobile())){
                SettingBindMobileActivity.launch(this,SettingBindMobileActivity.FORM_OTHERS);
            }else{
//                VerifyMobileActivity.launch(this,LoginModel.getBindMobile());
                ModifyMobileActivity.launch(this,LoginModel.getBindMobile());
            }
        }else if(v == modifyPwdView){
            SettingModifyPWDActivity.launchActivity(this);
        }else if(v == updateView){
            UpgradeUtils.requestInitialize(true, this);
        }else if(v == aboutUsView){
            UserAboutUsActivity.launch(this);
        }else if(v == logOutView){
            PushManager.getInstance().unBindAlias(this.getApplicationContext(), String.valueOf(LoginModel.getUid()), true);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
            LoginModel.loginOut();
        }
    }
}
