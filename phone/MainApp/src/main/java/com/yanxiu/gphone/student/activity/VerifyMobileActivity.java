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
import com.yanxiu.gphone.student.bean.RequestBean;
import com.yanxiu.gphone.student.bean.request.CheckMobileRequest;
import com.yanxiu.gphone.student.bean.request.GetVerifyCodeRequest;
import com.yanxiu.gphone.student.bean.response.BindNewMobileResponse;
import com.yanxiu.gphone.student.bean.response.GetVerifyResponse;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.requestTask.RequestProduceCodeTask;
import com.yanxiu.gphone.student.utils.ToastMaster;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.view.MyBoldTextView;
import com.yanxiu.gphone.student.view.StudentLoadingLayout;

/**
 * Created by sunpeng on 2017/4/11.
 */

public class VerifyMobileActivity extends Activity implements View.OnClickListener {
    private Context mContext;
    private MyBoldTextView textView ;
    private StudentLoadingLayout loadingLayout;
    private View backView,iv_delete;
    private EditText et_mobile_num,et_verification_code;
    private TextView tv_send_code,tv_ok;
    private int counter=45;  //计时
    private static final int COUNTER = 0x01;
    private String mobile;
    private static final String MOBILE_NUM = "mobile";
    public static final int REQUEST_VERIFY_MOBILE = 0x05;

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
    public static void launch(Activity activity,String mobileNum){
        Intent intent = new Intent(activity,VerifyMobileActivity.class);
        intent.putExtra(MOBILE_NUM,mobileNum);
        activity.startActivity(intent);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_mobile);
        mContext = this;
        initView();
        initListener();
        initData();
    }

    private void initData() {
        mobile = getIntent() ==null?null:getIntent().getStringExtra(MOBILE_NUM);
        if(TextUtils.isEmpty(mobile))
            finish();
        StringBuffer sb = new StringBuffer(mobile);
        if(sb.length() > 7){
            sb.insert(3,' ');
            sb.insert(8,' ');
        }
        et_mobile_num.setText(Html.fromHtml("<big><strong>" + sb.toString() +
                "</strong></big>"));
    }

    private void initView(){
        textView = (MyBoldTextView) findViewById(R.id.pub_top_mid);
        iv_delete = findViewById(R.id.iv_delete);
        backView = findViewById(R.id.pub_top_left);
        et_mobile_num = (EditText) findViewById(R.id.et_mobile_num);
        et_verification_code = (EditText) findViewById(R.id.et_verification_code);
        tv_send_code = (TextView) findViewById(R.id.tv_send_code);
        loadingLayout = (StudentLoadingLayout) findViewById(R.id.loading_layout);
        tv_ok = (TextView) findViewById(R.id.tv_ok);

        et_mobile_num.setEnabled(false);
        et_mobile_num.setFocusable(false);
        tv_ok.setClickable(false);
        iv_delete.setVisibility(View.INVISIBLE);
        textView.setText(R.string.verify_mobile);
        tv_send_code.setTextColor(getResources().getColor(R.color.color_805500));
    }

    private void initListener(){
        tv_ok.setOnClickListener(this);
        backView.setOnClickListener(this);
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
                    getVerificationCode(mobile);
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
                    tv_ok.setClickable(true);
                    tv_ok.setTextColor(getResources().getColor(R.color.color_805500));
                }else {
                    tv_ok.setClickable(false);
                    tv_ok.setTextColor(getResources().getColor(R.color.color_e4b62e));
                }
            }
        });
    }

    private void getVerificationCode(String mobileNum) {
        GetVerifyCodeRequest request = new GetVerifyCodeRequest();
        request.setMobile(mobileNum);
        request.setType("1");
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

    private void checkMobile(){
        CheckMobileRequest request = new CheckMobileRequest();
        request.setToken(LoginModel.getToken());
        request.setMobile(mobile);
        request.setCode(et_verification_code.getText().toString());
        showLoadingDialog();
        request.startRequest(BindNewMobileResponse.class, new HttpCallback<BindNewMobileResponse>() {
            @Override
            public void onSuccess(RequestBase request, BindNewMobileResponse response) {
                dismissLoadingDialog();
                if(response.getStatus().getCode() == 0){
                    SettingBindMobileActivity.launch(VerifyMobileActivity.this,SettingBindMobileActivity.FORM_BIND_NEW_MOBILE);
                }else {
                    Util.showUserToast(response.getStatus().getDesc(), null, null);
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                dismissLoadingDialog();
                Util.showUserToast(error.getLocalizedMessage(), null, null);
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
            case R.id.pub_top_left:
                this.finish();
                break;
            case R.id.tv_ok:
                checkMobile();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case SettingBindMobileActivity.REQUEST_BIND_MOBILE:
                if(resultCode == RESULT_OK){
                    finish();
                }
                break;
            default:
                break;
        }
    }
}
