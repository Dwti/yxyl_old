package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.RequestBean;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.requestTask.RequestProduceCodeTask;
import com.yanxiu.gphone.student.requestTask.RequestValidateProduceCodeTask;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.view.StudentLoadingLayout;
import com.yanxiu.gphone.student.view.YanxiuTypefaceTextView;

/**
 * Created by Administrator on 2015/7/6.
 */
public class RegisterActivity extends YanxiuBaseActivity{

    public static void launchActivity(Activity context,int type){
        Intent intent = new Intent(context,RegisterActivity.class);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }

    public static void launchActivity(Activity context,int type,String phoneNum){
        Intent intent = new Intent(context,RegisterActivity.class);
        intent.putExtra("type",type);
        intent.putExtra("phoneNum",phoneNum);
        context.startActivity(intent);
    }

    private TextView backView;

    private TextView titleView;

    private EditText userNameText;

    private EditText codeView;

    private TextView sendCodeView;

    private TextView registerNext;

    private ImageView delView;

    private String phoneNum;

    private StudentLoadingLayout loading;

    private UserTextWatcher mUserTextWatcher;

    private static int type; //0:注册  1：重置密码

    private RequestProduceCodeTask requestProduceCodeTask;

    private RequestValidateProduceCodeTask requestValidateProduceCodeTask;

    private final static int SEND_INDENTIFY_TIME = 0x01;

    private final static int SEND_INDENTIFY_TIME_DELAY = 1000;

    private static int INDENTIFY_TIME_DEFAULT = 45;

    private Handler mHandler = new Handler(){
        @Override public void handleMessage(Message msg) {
            switch (msg.what){
            case SEND_INDENTIFY_TIME:
                if(INDENTIFY_TIME_DEFAULT == 0){
                    sendCodeView.setEnabled(true);
                    sendCodeView.setText(R.string.register_send_code);
                }else{
                    sendCodeView.setEnabled(false);
                    sendCodeView.setText(String.valueOf(INDENTIFY_TIME_DEFAULT));
                    mHandler.sendEmptyMessageDelayed(SEND_INDENTIFY_TIME,
                            SEND_INDENTIFY_TIME_DELAY);
                    INDENTIFY_TIME_DEFAULT--;
                }
                break;
            }
        }
    };

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        LogInfo.log("king", "RegisterActivity onCreate");
        Intent intent = getIntent();
        if(intent != null){
            type = intent.getIntExtra("type",0);
            phoneNum = intent.getStringExtra("phoneNum");
        }
        findView();
    }

    private void findView(){
        backView = (TextView)findViewById(R.id.pub_top_left);
        titleView = (TextView)findViewById(R.id.pub_top_mid);
        userNameText = (EditText)findViewById(R.id.register_username);
        Util.setViewTypeface(YanxiuTypefaceTextView.TypefaceType.METRO_BOLD, userNameText);
        codeView = (EditText)findViewById(R.id.register_code);
        sendCodeView = (TextView)findViewById(R.id.register_send_code);
        registerNext = (TextView)findViewById(R.id.register_upload);
        Util.setViewTypeface(YanxiuTypefaceTextView.TypefaceType.FANGZHENG, registerNext);
        delView = (ImageView)findViewById(R.id.del_username);
        loading = (StudentLoadingLayout)findViewById(R.id.loading);

        if(type == 0){
            titleView.setText(R.string.register);
        }else{
            titleView.setText(R.string.login_forget_password);
        }
        backView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                CommonCoreUtil.hideSoftInput(userNameText);
                CommonCoreUtil.hideSoftInput(codeView);
                RegisterActivity.this.finish();
            }
        });
        delView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                userNameText.setText("");
            }
        });
        mUserTextWatcher = new UserTextWatcher();
        userNameText.addTextChangedListener(mUserTextWatcher);
        sendCodeView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(StringUtils.isEmpty(userNameText.getText().toString())){
                    Util.showUserToast(R.string.mobile_null, -1, -1);
                    return;
                }
                if(!CommonCoreUtil.isMobileNo(userNameText.getText().toString().replaceAll(" ", ""))){
                    Util.showUserToast(R.string.login_name_ival, -1, -1);
                    return;
                }
                getRegCode();
            }
        });

        registerNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (StringUtils.isEmpty(userNameText.getText().toString())) {
                    Util.showUserToast(R.string.mobile_null, -1, -1);
                    return;
                }
                if (!CommonCoreUtil.isMobileNo(userNameText.getText().toString().replaceAll(" ", ""))) {
                    Util.showUserToast(R.string.login_name_ival, -1, -1);
                    return;
                }
                if (StringUtils.isEmpty(codeView.getText().toString())) {
                    Util.showUserToast(R.string.register_code_null, -1, -1);
                    return;
                }
                CommonCoreUtil.hideSoftInput(userNameText);
                CommonCoreUtil.hideSoftInput(codeView);
                LoginModel.setMobile(userNameText.getText().toString().replaceAll(" ", ""));
                validateCode();
            }
        });

        if(!TextUtils.isEmpty(phoneNum)){
            userNameText.setText(phoneNum);
            userNameText.setSelection(userNameText.getText().length());
            if(userNameText.getText().toString().replaceAll(" ", "").length()==11){
                sendCodeView.setTextColor(this.getResources().getColor(R.color.color_805500));
            }else{
                sendCodeView.setTextColor(this.getResources().getColor(R.color.color_e4b62e));
            }
        }
    }
    /**
     * 获取验证码
     * */
    private void getRegCode(){
        mHandler.sendEmptyMessage(SEND_INDENTIFY_TIME);
        INDENTIFY_TIME_DEFAULT = 45;
        requestProduceCodeTask = new RequestProduceCodeTask(this,
                userNameText.getText().toString().replaceAll(" ", ""), type, new AsyncCallBack() {
            @Override public void update(YanxiuBaseBean result) {
                RequestBean requestBean = (RequestBean)result;
                if(requestBean.getStatus().getCode() != 0){
                    LogInfo.log("king","send code desc = " + requestBean.getStatus().getDesc());
                    mHandler.removeMessages(SEND_INDENTIFY_TIME);
                    INDENTIFY_TIME_DEFAULT = 0;
                    mHandler.sendEmptyMessage(SEND_INDENTIFY_TIME);
                    Util.showUserToast(requestBean.getStatus().getDesc(), null, null);
                }
            }

            @Override public void dataError(int type, String msg) {
                mHandler.removeMessages(SEND_INDENTIFY_TIME);
                INDENTIFY_TIME_DEFAULT = 0;
                mHandler.sendEmptyMessage(SEND_INDENTIFY_TIME);
                if(!StringUtils.isEmpty(msg)){
                    Util.showUserToast(msg, null, null);
                } else{
                    Util.showUserToast(R.string.net_null, -1, -1);
                }
            }
        });
        requestProduceCodeTask.start();
    }

    /**
     * 验证验证码
     * */
    private void validateCode(){
        loading.setViewType(StudentLoadingLayout.LoadingType.LAODING_COMMON);
        requestValidateProduceCodeTask = new RequestValidateProduceCodeTask(this,
                userNameText.getText().toString().replaceAll(" ", ""),
                codeView.getText().toString(), type, new AsyncCallBack() {
            @Override public void update(YanxiuBaseBean result) {
                loading.setViewGone();
                RequestBean requestBean = (RequestBean)result;
                if(requestBean.getStatus().getCode() == 0){
                    SetPasswordActivity.launchActivity(RegisterActivity.this, type);
                }else{
                    Util.showUserToast(requestBean.getStatus().getDesc(), null, null);
                }
            }

            @Override public void dataError(int type, String msg) {
                loading.setViewGone();
                if(!StringUtils.isEmpty(msg)){
                    Util.showUserToast(msg, null, null);
                }else{
                   Util.showUserToast(R.string.net_null, -1, -1);
                }
            }
        });
        requestValidateProduceCodeTask.start();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if(requestProduceCodeTask!=null){
            requestProduceCodeTask.cancel();
            requestProduceCodeTask = null;
        }
        if(requestValidateProduceCodeTask!=null){
            requestValidateProduceCodeTask.cancel();
            requestValidateProduceCodeTask = null;
        }
        CommonCoreUtil.hideSoftInput(userNameText);
        CommonCoreUtil.hideSoftInput(codeView);
    }

    private class UserTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged (CharSequence s, int start,
                                       int count, int after) {
        }

        @Override
        public void onTextChanged (CharSequence s, int start,
                                   int before, int count) {
            if (s == null || s.length() == 0 ) return;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                if (i != 3 && i != 8 && i != 13 && s.charAt(i) == ' ') {
                    continue;
                } else {
                    sb.append(s.charAt(i));
                    if ((sb.length() == 4 || sb.length() == 9 || sb.length() == 14) && sb.charAt(sb
                            .length() - 1)
                            != ' ') {
                        sb.insert(sb.length() - 1, ' ');
                    }
                }
            }
            if (!sb.toString().equals(s.toString())) {
                int index = start + 1;
                if (sb.charAt(start) == ' ') {
                    if (before == 0) {
                        index++;
                    } else {
                        index--;
                    }
                } else {
                    if (before == 1) {
                        index--;
                    }
                }
                String txtContent = sb.toString();
                userNameText.setText(Html.fromHtml("<big><strong>" + txtContent +
                        "</strong></big>"));
                userNameText.setSelection(index);
            } else if(sb.toString().replaceAll(" ", "").length() >=11){
                userNameText.removeTextChangedListener(mUserTextWatcher);
                String txtContent = sb.toString();
                userNameText.setText(Html.fromHtml("<big><strong>" + txtContent +
                        "</strong></big>"));
                userNameText.setSelection(txtContent.length());
                LogInfo.log("haitian", "sb.toString()=" + sb.toString());
                userNameText.addTextChangedListener(mUserTextWatcher);
            }

        }

        @Override
        public void afterTextChanged (final Editable s) {
            if(!TextUtils.isEmpty(s.toString()) && s.toString().replaceAll(" ", "").length()==11){
                sendCodeView.setTextColor(getResources().getColor(R.color.color_805500));
            }else{
                sendCodeView.setTextColor(getResources().getColor(R.color.color_e4b62e));
            }
//            int length = s.length();
//            if(length>0){
//                delView.setVisibility(View.VISIBLE);
//            }else{
//                delView.setVisibility(View.GONE);
//            }
        }
    }
}
