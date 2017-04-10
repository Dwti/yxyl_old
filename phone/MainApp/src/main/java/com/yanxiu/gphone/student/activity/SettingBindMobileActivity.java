package com.yanxiu.gphone.student.activity;

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
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.utils.ToastMaster;
import com.yanxiu.gphone.student.view.MyBoldTextView;

/**
 * Created by sunpeng on 2017/4/6.
 */

public class SettingBindMobileActivity extends YanxiuBaseActivity implements View.OnClickListener {
    private Context mContext;
    private MyBoldTextView textView ;
    private View backView,iv_delete;
    private EditText et_mobile_num,et_verification_code;
    private TextView tv_send_code,tv_ok;
    private String temMobileNum;
    private int counter=45;  //计时
    private static final int COUNTER = 0x01;
    private boolean isMobileNumReady,isVerificationCodeReady;
    private MobileNumTextWatcher mobileNumTextWatcher;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case COUNTER:
                    if(counter > 0){
                        counter--;
                        tv_send_code.setText(String.valueOf(counter));
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
    public static void launch(Context context){
        Intent intent = new Intent(context,SettingBindMobileActivity.class);
        context.startActivity(intent);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_mobile);
        mContext = this;
        initView();
        initListener();
    }

    private void initView(){
        textView = (MyBoldTextView) findViewById(R.id.pub_top_mid);
        iv_delete = findViewById(R.id.iv_delete);
        backView = findViewById(R.id.pub_top_left);
        et_mobile_num = (EditText) findViewById(R.id.et_mobile_num);
        et_verification_code = (EditText) findViewById(R.id.et_verification_code);
        tv_send_code = (TextView) findViewById(R.id.tv_send_code);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        tv_send_code.setClickable(false);
        tv_ok.setClickable(false);
        textView.setText(R.string.bind_mobile);
    }

    private void initListener(){
        backView.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
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
                    getVerificationCode();
                }
                temMobileNum = et_mobile_num.getText().toString();
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

    private void getVerificationCode() {
        //请求获取验证码成功之后
        tv_send_code.setText(R.string.register_send_code_two);
        mHandler.sendEmptyMessageDelayed(COUNTER,1000);
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
            default:
                break;
        }
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
                et_mobile_num.setText(Html.fromHtml("<big><strong>" + txtContent +
                        "</strong></big>"));
                et_mobile_num.setSelection(index);
            } else if(sb.toString().replaceAll(" ", "").length() >=11){
                et_mobile_num.removeTextChangedListener(mobileNumTextWatcher);
                String txtContent = sb.toString();
                et_mobile_num.setText(Html.fromHtml("<big><strong>" + txtContent +
                        "</strong></big>"));
                et_mobile_num.setSelection(txtContent.length());
                LogInfo.log("haitian", "sb.toString()=" + sb.toString());
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
                tv_send_code.setClickable(true);
                tv_send_code.setTextColor(getResources().getColor(R.color.color_805500));
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
