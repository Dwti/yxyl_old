package com.yanxiu.gphone.studentold.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.StringUtils;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.base.YanxiuBaseActivity;
import com.yanxiu.gphone.studentold.bean.RequestBean;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.requestTask.RequestResetPwdTask;
import com.yanxiu.gphone.studentold.utils.EditTextWatcherUtils;
import com.yanxiu.gphone.studentold.utils.Util;
import com.yanxiu.gphone.studentold.view.StudentLoadingLayout;
import com.yanxiu.gphone.studentold.view.YanxiuTypefaceTextView;

/**
 * Created by Administrator on 2015/7/6.
 */
public class SetPasswordActivity extends YanxiuBaseActivity{

    public static void launchActivity(Activity context,int type){
        Intent intent = new Intent(context,SetPasswordActivity.class);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }

    private TextView backView;

    private TextView titleView;

    private TextView tipView;

    private EditText pwdText;

    private EditText pwdAgainText;

    private TextView nextView;

    private StudentLoadingLayout loading;

    private ImageView delPwd, delPwdAgain;

    private static int type; //0：注册  ，1:重置密码

    private RequestResetPwdTask requestResetPwdTask;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_password_layout);
        Intent intent = getIntent();
        if(intent != null){
            type = intent.getIntExtra("type",0);
        }
        findView();
    }

    private void findView(){
        backView = (TextView)findViewById(R.id.pub_top_left);
        delPwd = (ImageView)findViewById(R.id.del_pwd);
        delPwdAgain = (ImageView)findViewById(R.id.del_pwd_again);
        titleView = (TextView)findViewById(R.id.pub_top_mid);
        tipView = (TextView)findViewById(R.id.register_tip);
        pwdText = (EditText)findViewById(R.id.set_password_one);
        EditTextWatcherUtils.getInstence().setEditText(pwdText,null);
        pwdAgainText = (EditText)findViewById(R.id.set_password_again);
        EditTextWatcherUtils.getInstence().setEditText(pwdAgainText,null);
        nextView = (TextView)findViewById(R.id.register_next);
        Util.setViewTypeface(YanxiuTypefaceTextView.TypefaceType.FANGZHENG, nextView);
        loading = (StudentLoadingLayout)findViewById(R.id.loading);
        delPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                pwdText.setText("");
            }
        });
        delPwdAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                pwdAgainText.setText("");
            }
        });
        if(type == 0){
            titleView.setText(R.string.set_password);
            nextView.setText(R.string.set_password_next);
        }else{
            titleView.setText(R.string.reset_password);
            nextView.setText(R.string.reset_password_next);
            tipView.setVisibility(View.INVISIBLE);
        }
        backView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                CommonCoreUtil.hideSoftInput(pwdText);
                CommonCoreUtil.hideSoftInput(pwdAgainText);
                SetPasswordActivity.this.finish();
            }
        });

        nextView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (StringUtils.isEmpty(pwdText.getText().toString())) {
                    Util.showUserToast(R.string.set_password_null, -1, -1);
                    return;
                }
                if (!pwdText.getText().toString()
                        .equals(pwdAgainText.getText().toString())) {
                    Util.showUserToast(R.string.set_password_not_same, -1, -1);
                    return;
                }
                String password = pwdText.getText().toString();
                if(!CommonCoreUtil.isPasswordRight(password)){
                    Util.showUserToast(R.string.set_password_6_8, -1, -1);
                    return;
                }
                CommonCoreUtil.hideSoftInput(pwdText);
                CommonCoreUtil.hideSoftInput(pwdAgainText);
                LoginModel.setPassword(password);
                if (type == 0) {
//                    UserInfoActivity.launchActivity(SetPasswordActivity.this);
                    RegisterJoinGroupActivity.launchActivity(SetPasswordActivity.this);
                } else {
                    resetPwd();
                }
            }
        });

//        pwdText.addTextChangedListener(new MyTextWatcher());
//        pwdAgainText.addTextChangedListener(new MyTextWatcher());
    }

    private void resetPwd(){
        loading.setViewType(StudentLoadingLayout.LoadingType.LAODING_COMMON);
        requestResetPwdTask = new RequestResetPwdTask(this, LoginModel.getMobile(),
                LoginModel.getPassword(), new AsyncCallBack() {
            @Override public void update(YanxiuBaseBean result) {
                loading.setVisibility(View.GONE);
                RequestBean requestBean = (RequestBean)result;
                if(requestBean.getStatus().getCode() == 0){
                   Util.showUserToast(R.string.reset_password_success1, R.string.reset_password_success2, -1);
                    LoginActivity.launcherActivity(SetPasswordActivity.this,0);
                }else{
                    Util.showUserToast(requestBean.getStatus().getDesc(), null, null);
                }
            }

            @Override public void dataError(int type, String msg) {
                loading.setVisibility(View.GONE);
                if(!StringUtils.isEmpty(msg)){
                    Util.showUserToast(msg, null, null);
                } else{
                    Util.showUserToast(R.string.net_null, -1, -1);
                }
            }
        });
        requestResetPwdTask.start();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if(requestResetPwdTask!=null){
            requestResetPwdTask.cancel();
            requestResetPwdTask = null;
        }
        CommonCoreUtil.hideSoftInput(pwdText);
        CommonCoreUtil.hideSoftInput(pwdAgainText);
    }

}
