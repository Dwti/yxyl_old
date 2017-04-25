package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Context;
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
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.common.login.LoginModel;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.RequestBean;
import com.yanxiu.gphone.student.bean.request.BindNewMobileRequest;
import com.yanxiu.gphone.student.bean.request.GetVerifyCodeRequest;
import com.yanxiu.gphone.student.bean.response.BindNewMobileResponse;
import com.yanxiu.gphone.student.bean.response.GetVerifyResponse;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.requestTask.RequestProduceCodeTask;
import com.yanxiu.gphone.student.utils.ToastMaster;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.view.MyBoldTextView;
import com.yanxiu.gphone.student.view.StudentLoadingLayout;
import com.yanxiu.gphone.student.view.YanxiuTypefaceTextView;

/**
 * Created by sunpeng on 2017/4/6.
 */

public class SettingBindMobileActivity extends Activity implements View.OnClickListener {
    private Context mContext;
    private StudentLoadingLayout loadingLayout;
    private MyBoldTextView textView ;
    private View backView,iv_delete,tv_bottom_tips;
    private EditText et_mobile_num,et_verification_code;
    private TextView tv_send_code,tv_ok;
    private String temMobileNum;
    private int counter=45;  //计时
    private static final int COUNTER = 0x01;
    private boolean isMobileNumReady,isVerificationCodeReady;
    private MobileNumTextWatcher mobileNumTextWatcher;
    private int comeForm = 0;
    private static final String COME_FORM = "comeForm";
    public static final String MOBILE_NUM = "mobileNum";
    public static final int FORM_BIND_NEW_MOBILE = 0x01;
    public static final int FORM_OTHERS = 0x02;
    public static final int REQUEST_BIND_MOBILE = 0x03;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case COUNTER:
                    if(counter > 0){
                        tv_send_code.setText(String.valueOf(counter));
                        counter--;
                        mHandler.sendEmptyMessageDelayed(COUNTER,1000);
                    }else {
                        tv_send_code.setText(R.string.register_send_code_two);
                        tv_send_code.setClickable(true);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public static void launch(Activity activity, int comeForm){
        Intent intent = new Intent(activity,SettingBindMobileActivity.class);
        intent.putExtra(COME_FORM,comeForm);
        activity.startActivityForResult(intent,REQUEST_BIND_MOBILE);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_mobile);
        mContext = this;
        initData();
        initView();
        initListener();
    }

    private void initData() {
        comeForm = getIntent().getIntExtra(COME_FORM,0);
    }

    private void initView(){
        textView = (MyBoldTextView) findViewById(R.id.pub_top_mid);
        iv_delete = findViewById(R.id.iv_delete);
        backView = findViewById(R.id.pub_top_left);
        et_mobile_num = (EditText) findViewById(R.id.et_mobile_num);
        et_verification_code = (EditText) findViewById(R.id.et_verification_code);
        tv_send_code = (TextView) findViewById(R.id.tv_send_code);
        tv_bottom_tips = findViewById(R.id.tv_bottom_tips);
        loadingLayout = (StudentLoadingLayout) findViewById(R.id.loading_layout);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        Util.setViewTypeface(YanxiuTypefaceTextView.TypefaceType.METRO_BOLD, et_mobile_num);

        tv_send_code.setClickable(false);
        tv_ok.setClickable(false);
        tv_bottom_tips.setVisibility(View.GONE);

        if(comeForm == FORM_BIND_NEW_MOBILE){
            textView.setText(R.string.bind_new_mobile);
        }else{
            textView.setText(R.string.bind_mobile);
        }
    }

    private void initListener(){
        backView.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        tv_ok.setOnClickListener(this);
        mobileNumTextWatcher = new MobileNumTextWatcher();
        et_mobile_num.addTextChangedListener(mobileNumTextWatcher);
        tv_send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(et_mobile_num.getText().toString())){
                    ToastMaster.showShortToast(mContext,R.string.mobile_null);
                    return;
                }else if(!CommonCoreUtil.isMobileNo(et_mobile_num.getText().toString().replaceAll(" ",""))){
                    ToastMaster.showShortToast(mContext,R.string.login_name_ival);
                    return;
                }else {
                    temMobileNum = et_mobile_num.getText().toString().replaceAll(" ","");
                    getVerificationCode(temMobileNum);
                }
                tv_send_code.setClickable(false);
            }
        });
        et_verification_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(s.toString()) && s.length() ==4){
                    isVerificationCodeReady = true;
                    if(isMobileNumReady){
                        tv_ok.setClickable(true);
                        tv_ok.setTextColor(getResources().getColor(R.color.color_805500));
                    }
                }else {
                    isVerificationCodeReady = false;
                    tv_ok.setClickable(false);
                    tv_ok.setTextColor(getResources().getColor(R.color.color_e4b62e));
                }
            }
        });
    }

    private void bindMobile(){
        temMobileNum = et_mobile_num.getText().toString().replaceAll(" ","");
        BindNewMobileRequest request = new BindNewMobileRequest();
        request.setToken(LoginModel.getToken());
        request.setNewMobile(temMobileNum);
        request.setCode(et_verification_code.getText().toString());
        showLoadingDialog();
        request.startRequest(BindNewMobileResponse.class, new HttpCallback<BindNewMobileResponse>() {
            @Override
            public void onSuccess(RequestBase request, BindNewMobileResponse response) {
                dismissLoadingDialog();
                if(response.getStatus().getCode() == 0){
                    Util.showUserToast("绑定成功",null,null);
                    LoginModel.setBindMobile(temMobileNum);
                    LoginModel.savaCacheData();
                    ModifyMobileActivity.launch(SettingBindMobileActivity.this,LoginModel.getBindMobile());
                    setResult(RESULT_OK);
                    finish();
                }else {
                    Util.showUserToast(response.getStatus().getDesc(),null,null);
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                dismissLoadingDialog();
                Util.showUserToast(error.getLocalizedMessage(),null,null);
            }
        });
    }
    private void showLoadingDialog(){
        loadingLayout.setViewType(StudentLoadingLayout.LoadingType.LAODING_COMMON);
    }

    private void dismissLoadingDialog(){
        loadingLayout.setViewGone();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_delete:
                et_mobile_num.setText("");
                break;
            case R.id.pub_top_left:
                this.finish();
                break;
            case R.id.tv_ok:
                bindMobile();
                break;
            default:
                break;
        }
    }

    private void getVerificationCode(String mobileNum) {
        GetVerifyCodeRequest request = new GetVerifyCodeRequest();
        request.setMobile(mobileNum);
        request.setType("0");
        showLoadingDialog();
        request.startRequest(GetVerifyResponse.class, new HttpCallback<GetVerifyResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetVerifyResponse response) {
                dismissLoadingDialog();
                if(response.getStatus().getCode() != 0){
                    if (!response.getStatus().getDesc().equals(getResources().getString(R.string.send_code_oldover))) {
                        tv_send_code.setText(R.string.register_send_code_two);
                    }
                    tv_send_code.setClickable(true);
                    Util.showUserToast(response.getStatus().getDesc(), null, null);
                }else {
                    //请求获取验证码成功之后
                    tv_send_code.setText(R.string.register_send_code_two);
                    mHandler.sendEmptyMessage(COUNTER);
                    counter = 45;
                    Util.showUserToast("验证码已发送，请注意查收", null, null);
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                dismissLoadingDialog();
                String msg = error.getLocalizedMessage();
                if(!StringUtils.isEmpty(msg)){
                    Util.showUserToast(msg, null, null);
                } else{
                    Util.showUserToast(R.string.net_null, -1, -1);
                }
            }
        });
    }

    private class MobileNumTextWatcher implements TextWatcher {
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
                et_mobile_num.setText(txtContent);
                et_mobile_num.setSelection(index);
            } else if(sb.toString().replaceAll(" ", "").length() >=11){
                et_mobile_num.removeTextChangedListener(mobileNumTextWatcher);
                String txtContent = sb.toString();
                et_mobile_num.setText(txtContent);
                et_mobile_num.setSelection(txtContent.length());
                et_mobile_num.addTextChangedListener(mobileNumTextWatcher);
            }

        }

        @Override
        public void afterTextChanged (final Editable s) {
            if(!TextUtils.isEmpty(s.toString()) && s.toString().replaceAll(" ","").length()==11){
                isMobileNumReady = true;
                if(isVerificationCodeReady){
                    tv_ok.setClickable(true);
                    tv_ok.setTextColor(getResources().getColor(R.color.color_805500));
                }
                if(counter < 1){
                    tv_send_code.setClickable(true);
                    tv_send_code.setTextColor(getResources().getColor(R.color.color_805500));
                }
            }else{
                isMobileNumReady = false;
                tv_send_code.setClickable(false);
                tv_send_code.setTextColor(getResources().getColor(R.color.color_e4b62e));

                tv_ok.setClickable(false);
                tv_ok.setTextColor(getResources().getColor(R.color.color_e4b62e));
            }
        }
    }
}
