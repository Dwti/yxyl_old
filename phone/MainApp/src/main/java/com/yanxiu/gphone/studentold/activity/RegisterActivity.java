package com.yanxiu.gphone.studentold.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.base.YanxiuBaseActivity;
import com.yanxiu.gphone.studentold.bean.RequestBean;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.requestTask.RegisterTask;
import com.yanxiu.gphone.studentold.requestTask.RequestProduceCodeTask;
import com.yanxiu.gphone.studentold.requestTask.RequestValidateProduceCodeTask;
import com.yanxiu.gphone.studentold.utils.EditTextWatcherUtils;
import com.yanxiu.gphone.studentold.utils.Util;
import com.yanxiu.gphone.studentold.view.StudentLoadingLayout;
import com.yanxiu.gphone.studentold.view.YanxiuTypefaceTextView;

/**
 * Created by Administrator on 2015/7/6.
 */
public class RegisterActivity extends YanxiuBaseActivity{

    private EditText set_password_one;

    public static void launchActivity(Activity context, int type){
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

    private String TestMobile="";

    private final static int SEND_INDENTIFY_TIME = 0x01;
    private final static int SEND_INDENTIFY_TIMES = 0x02;
    private final static int SEND_INDENTIFY_READY=0x03;
    private final static int SEND_INDENTITY_TWO=0x04;

    private final static int SEND_INDENTIFY_TIME_DELAY = 1000;

    private static int INDENTIFY_TIME_DEFAULT = 45;

    private boolean isLock = false;

    private boolean IsMobileReady=false;

    private boolean IsCodeReady=false;

    private boolean IsPasswordReady=false;

    private Handler mHandler = new Handler(){
        @Override public void handleMessage(Message msg) {
            switch (msg.what){
            case SEND_INDENTIFY_TIME:
                if(INDENTIFY_TIME_DEFAULT == 0){
                    sendCodeView.setEnabled(true);
                    if (userNameText.getText().toString().trim().replace(" ","").equals(TestMobile)){
                        sendCodeView.setText(R.string.register_send_code_two);
                    }else {
                        sendCodeView.setText(R.string.register_send_code);
                    }
                }else{
                    sendCodeView.setEnabled(false);
                    sendCodeView.setText(String.valueOf(INDENTIFY_TIME_DEFAULT));
                    mHandler.sendEmptyMessageDelayed(SEND_INDENTIFY_TIME,
                            SEND_INDENTIFY_TIME_DELAY);
                    INDENTIFY_TIME_DEFAULT--;
                }
                break;
                case SEND_INDENTIFY_TIMES:
                    if (INDENTIFY_TIME_DEFAULT==0){
                        sendCodeView.setEnabled(true);
                        sendCodeView.setText(R.string.register_send_code);
                    }
                    break;
                case SEND_INDENTIFY_READY:
                    if (type==0) {
                        if (IsMobileReady && IsCodeReady && IsPasswordReady) {
                            registerNext.setTextColor(getResources().getColor(R.color.color_805500));
                            registerNext.setClickable(true);
                        } else {
                            registerNext.setTextColor(getResources().getColor(R.color.color_e4b62e));
                            registerNext.setClickable(false);
                        }
                    }
                    break;
                case SEND_INDENTITY_TWO:
                    sendCodeView.setText(R.string.register_send_code_two);
                    break;
            }
        }
    };

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
//        RegisterJoinGroupActivity.launchActivity(this);
//        startActivity(new Intent(this,SetPasswordActivity.class));
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
        codeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())&&s.toString().length()==4){
                    IsCodeReady=true;
                }else {
                    IsCodeReady=false;
                }
                mHandler.sendEmptyMessageDelayed(SEND_INDENTIFY_READY,10);
            }
        });
        sendCodeView = (TextView)findViewById(R.id.register_send_code);
        registerNext = (TextView)findViewById(R.id.register_upload);
        Util.setViewTypeface(YanxiuTypefaceTextView.TypefaceType.FANGZHENG, registerNext);
        if (type==0) {
            registerNext.setText(R.string.register);
        }
        LinearLayout pwd_layout_view= (LinearLayout) findViewById(R.id.pwd_layout_view);
        if (type!=0){
            pwd_layout_view.setVisibility(View.GONE);
        }
        delView = (ImageView)findViewById(R.id.del_username);
        loading = (StudentLoadingLayout)findViewById(R.id.loading);
        set_password_one= (EditText) findViewById(R.id.set_password_one);
        EditTextWatcherUtils.getInstence().setEditText(set_password_one, new EditTextWatcherUtils.OnTextChangedListener() {
            @Override
            public void onTextChanged(String text) {
                if (!TextUtils.isEmpty(text)&&text.length()>5&&text.length()<19){
                    IsPasswordReady=true;
                }else {
                    IsPasswordReady=false;
                }
                mHandler.sendEmptyMessageDelayed(SEND_INDENTIFY_READY,10);
            }
        });
        ImageView del_pwd= (ImageView) findViewById(R.id.del_pwd);
        del_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLock = !isLock;
                onCheckedChanged(v, isLock);
            }
        });
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
                if (type!=0){
                    if(StringUtils.isEmpty(userNameText.getText().toString())){
                        Util.showUserToast(R.string.mobile_null, -1, -1);
                        return;
                    }
                }else {
                    if(StringUtils.isEmpty(userNameText.getText().toString())){
//                        Util.showUserToast(R.string.mobile_null, -1, -1);
                        return;
                    }
                    if (userNameText.getText().toString().replaceAll(" ", "").length()!=11){
                        return;
                    }
                }

                if(!CommonCoreUtil.isMobileNo(userNameText.getText().toString().replaceAll(" ", ""))){
                        Util.showUserToast(R.string.login_name_ival, -1, -1);
                    return;
                }
                getRegCode();
            }
        });
        if (type==0) {
            registerNext.setTextColor(getResources().getColor(R.color.color_e4b62e));
            registerNext.setClickable(false);
        }
        registerNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                String mobile=userNameText.getText().toString().trim();
                String password=set_password_one.getText().toString().trim();
                String code=codeView.getText().toString().trim();

                if (type==0) {
                    if (StringUtils.isEmpty(password)) {
//                        Util.showUserToast(R.string.set_password_null, -1, -1);
                        return;
                    }
                    if(!CommonCoreUtil.isPasswordRight(password)){
//                        Util.showUserToast(R.string.set_password_6_8, -1, -1);
                        return;
                    }
                    if (StringUtils.isEmpty(mobile)) {
//                        Util.showUserToast(R.string.mobile_null, -1, -1);
                        return;
                    }
                    if (!CommonCoreUtil.isMobileNo(mobile.replaceAll(" ", ""))) {
                        Util.showUserToast(R.string.login_name_ival, -1, -1);
                        return;
                    }
                    if (StringUtils.isEmpty(code)) {
//                        Util.showUserToast(R.string.register_code_null, -1, -1);
                        return;
                    }
                }else {
                    if (StringUtils.isEmpty(mobile)) {
                        Util.showUserToast(R.string.mobile_null, -1, -1);
                        return;
                    }
                    if (!CommonCoreUtil.isMobileNo(mobile.replaceAll(" ", ""))) {
                        Util.showUserToast(R.string.login_name_ival, -1, -1);
                        return;
                    }
                    if (StringUtils.isEmpty(code)) {
                        Util.showUserToast(R.string.register_code_null, -1, -1);
                        return;
                    }
                }

                CommonCoreUtil.hideSoftInput(userNameText);
                CommonCoreUtil.hideSoftInput(codeView);
                LoginModel.setMobile(mobile.replaceAll(" ", ""));
                LoginModel.setPassword(password);
                validateCode(mobile.replaceAll(" ", ""),password,code);
            }
        });

        if(!TextUtils.isEmpty(phoneNum) && CommonCoreUtil.isMobileNo(phoneNum)){
            userNameText.setText(phoneNum);
            userNameText.setSelection(userNameText.getText().length());
            if(userNameText.getText().toString().replaceAll(" ", "").length()==11){
                sendCodeView.setTextColor(this.getResources().getColor(R.color.color_805500));
            }else{
                sendCodeView.setTextColor(this.getResources().getColor(R.color.color_e4b62e));
            }
        }
    }

    public void onCheckedChanged (View lockView, boolean isChecked) {
        if (isChecked) {
            //如果选中，显示密码
            lockView.setBackgroundResource(R.drawable.pwd_unlock);
            set_password_one.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            //否则隐藏密码
            lockView.setBackgroundResource(R.drawable.pwd_lock);
            set_password_one.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        if (!TextUtils.isEmpty(set_password_one.getText().toString())) {
            set_password_one.setSelection(set_password_one.getText().toString().length());
        }
    }

    /**
     * 获取验证码
     * */
    private void getRegCode(){
        TestMobile=userNameText.getText().toString().replaceAll(" ", "");
        loading.setViewType(StudentLoadingLayout.LoadingType.LAODING_COMMON);
        requestProduceCodeTask = new RequestProduceCodeTask(this,
                userNameText.getText().toString().replaceAll(" ", ""), type, new AsyncCallBack() {
            @Override public void update(YanxiuBaseBean result) {
                loading.setViewGone();
                RequestBean requestBean = (RequestBean)result;
                if(requestBean.getStatus().getCode() != 0){
                    LogInfo.log("king","send code desc = " + requestBean.getStatus().getDesc());
//                    mHandler.removeMessages(SEND_INDENTIFY_TIME);
//                    INDENTIFY_TIME_DEFAULT = 0;
                    if (!requestBean.getStatus().getDesc().equals(getResources().getString(R.string.send_code_oldover))) {
                        mHandler.sendEmptyMessage(SEND_INDENTITY_TWO);
                    }
                    Util.showUserToast(requestBean.getStatus().getDesc(), null, null);
                }else {
                    INDENTIFY_TIME_DEFAULT = 45;
                    mHandler.sendEmptyMessage(SEND_INDENTIFY_TIME);
                    Util.showUserToast("验证码已发送，请注意查收", null, null);
                }
            }

            @Override public void dataError(int type, String msg) {
                loading.setViewGone();
//                mHandler.removeMessages(SEND_INDENTIFY_TIME);
//                INDENTIFY_TIME_DEFAULT = 0;
//                mHandler.sendEmptyMessage(SEND_INDENTIFY_TIME);
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
    private void validateCode(String mobile,final String password,String code){
        loading.setViewType(StudentLoadingLayout.LoadingType.LAODING_COMMON);
        if (type==0) {
            RegisterTask registerTask = new RegisterTask(this, mobile, password, code, type, new AsyncCallBack() {
                @Override
                public void update(YanxiuBaseBean result) {
                    loading.setViewGone();
                    RequestBean requestBean = (RequestBean) result;
                    if (requestBean.getStatus().getCode() == 0) {
//                    SetPasswordActivity.launchActivity(RegisterActivity.this, type);
                        RegisterJoinGroupActivity.launchActivity(RegisterActivity.this);
                    } else {
                        Util.showUserToast(requestBean.getStatus().getDesc(), null, null);
                    }
                }

                @Override
                public void dataError(int type, String msg) {
                    loading.setViewGone();
                    if (type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                        Util.showUserToast(R.string.net_null, -1, -1);
                    } else {
                        Util.showUserToast(R.string.data_error, -1, -1);
                    }
                }
            });
            registerTask.start();
        }else {
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
                userNameText.setText(txtContent);
                userNameText.setSelection(index);
            } else if(sb.toString().replaceAll(" ", "").length() >=11){
                userNameText.removeTextChangedListener(mUserTextWatcher);
                String txtContent = sb.toString();
                userNameText.setText(txtContent);
                userNameText.setSelection(txtContent.length());
                userNameText.addTextChangedListener(mUserTextWatcher);
            }

        }

        @Override
        public void afterTextChanged (final Editable s) {
            if(!TextUtils.isEmpty(s.toString()) && s.toString().replaceAll(" ", "").length()==11){
                IsMobileReady=true;
                sendCodeView.setTextColor(getResources().getColor(R.color.color_805500));
            }else{
                IsMobileReady=false;
                sendCodeView.setTextColor(getResources().getColor(R.color.color_e4b62e));
            }
            if (!TextUtils.isEmpty(TestMobile)&&userNameText.getText().toString().trim().replace(" ","").length()==11&&!userNameText.getText().toString().trim().replace(" ","").equals(TestMobile)){
                mHandler.sendEmptyMessageDelayed(SEND_INDENTIFY_TIMES,10);
            }
            mHandler.sendEmptyMessageDelayed(SEND_INDENTIFY_READY,10);
        }
    }
}
