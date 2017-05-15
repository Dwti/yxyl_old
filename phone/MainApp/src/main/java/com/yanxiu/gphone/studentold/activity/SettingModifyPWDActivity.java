package com.yanxiu.gphone.studentold.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.task.base.threadpool.YanxiuSimpleAsyncTask;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.base.YanxiuBaseActivity;
import com.yanxiu.gphone.studentold.bean.ModifiedPwdBean;
import com.yanxiu.gphone.studentold.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.studentold.parser.ModifiedPwdBeanParse;
import com.yanxiu.gphone.studentold.utils.PublicLoadUtils;
import com.yanxiu.gphone.studentold.utils.Util;
import com.yanxiu.gphone.studentold.view.PublicLoadLayout;
import com.yanxiu.gphone.studentold.view.YanxiuTypefaceTextView;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/7/14.
 */
public class SettingModifyPWDActivity extends YanxiuBaseActivity implements View.OnClickListener {
    private PublicLoadLayout mRootView;
    private View topView, backView;
    private TextView topTitle;
    private View submitView;
    private EditText oldPwdEt, newPwdEt, newPwdAgainEt;
    private ImageView clearCurrentPwdImg,clearNewPwd,clearNewPwdAgain;
    public static void launchActivity(Activity context) {
        Intent intent = new Intent(context, SettingModifyPWDActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = PublicLoadUtils.createPage(this, R.layout.activity_pwd_modify_setting_layout);
        setContentView(mRootView);
        findView();
    }

    private void findView() {
        topView = findViewById(R.id.top_layout);
        backView = topView.findViewById(R.id.pub_top_left);
        topTitle = (TextView) topView.findViewById(R.id.pub_top_mid);
        topTitle.setText(R.string.modify_pwd_txt);
        submitView = findViewById(R.id.submit_txt);
        Util.setViewTypeface(YanxiuTypefaceTextView.TypefaceType.FANGZHENG, (TextView) submitView);
        oldPwdEt = (EditText) findViewById(R.id.input_current_pwd);
        newPwdEt = (EditText) findViewById(R.id.input_new_pwd);
        newPwdAgainEt = (EditText) findViewById(R.id.input_new_pwd_again);


        clearCurrentPwdImg=(ImageView)findViewById(R.id.clearCurrentPwd);
        clearNewPwd=(ImageView)findViewById(R.id.clearNewPwd);
        clearNewPwdAgain=(ImageView)findViewById(R.id.clearNewPwdAgainPwd);


        backView.setOnClickListener(this);
        submitView.setOnClickListener(this);
        clearCurrentPwdImg.setOnClickListener(this);
        clearNewPwd.setOnClickListener(this);
        clearNewPwdAgain.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonCoreUtil.hideSoftInput(oldPwdEt);
        CommonCoreUtil.hideSoftInput(newPwdEt);
        CommonCoreUtil.hideSoftInput(newPwdAgainEt);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pub_top_left:
                finish();
            break;
            case R.id.submit_txt:
                String oldPwd = oldPwdEt.getEditableText().toString();
                String newPwd = newPwdEt.getEditableText().toString();
                String newpwdAgain = newPwdAgainEt.getEditableText().toString();
                if (TextUtils.isEmpty(oldPwd)) {
                    Util.showUserToast(R.string.input_current_pwd, -1, -1);
                    return;
                } else if (TextUtils.isEmpty(newPwd)) {
                    Util.showUserToast(R.string.input_new_pwd, -1, -1);
                    return;
                } else if (TextUtils.isEmpty(newpwdAgain)) {
                    Util.showUserToast(R.string.input_new_pwd_again, -1, -1);
                    return;
                } else if (!newPwd.equals(newpwdAgain)) {
                    Util.showUserToast(R.string.new_pwd_not_equals, -1, -1);
                    return;
                } else if(!CommonCoreUtil.isPasswordRight(newpwdAgain)){
                    Util.showUserToast(R.string.set_password_6_8, -1, -1);
                    return;
                } else {
                    upLoadSchool(newPwd, oldPwd);
                }
            break;

            case R.id.clearCurrentPwd:
                oldPwdEt.setText("");
                break;
            case R.id.clearNewPwd:
                newPwdEt.setText("");
                break;
            case R.id.clearNewPwdAgainPwd:
                newPwdAgainEt.setText("");
                break;
        }


    }

    private void upLoadSchool(final String newPwd, final String oldPwd) {
        mRootView.loading(true);
        new YanxiuSimpleAsyncTask<ModifiedPwdBean>(this){

            @Override
            public ModifiedPwdBean doInBackground() {
                YanxiuDataHull<ModifiedPwdBean> dataHull = null;
                HashMap<String, String> hashMap = new HashMap<String, String>();
                String mobile = LoginModel.getUserinfoEntity().getMobile();
                if(TextUtils.isEmpty(mobile)){
                    mobile = LoginModel.getLoginName();
                    hashMap.put("mobile", "");
                    hashMap.put("loginName",mobile);

                }else {
                    hashMap.put("mobile",mobile);
                    hashMap.put("loginName","");
                }
                hashMap.put("newPass", newPwd);
                hashMap.put("oldPass", oldPwd);
                dataHull= YanxiuHttpApi.requestModifiedPwd(0, new ModifiedPwdBeanParse(), hashMap);
                if(dataHull != null && dataHull.getDataType() == YanxiuDataHull.DataType.DATA_IS_INTEGRITY){
                    return dataHull.getDataEntity();
                } else {
                    return null;
                }
            }
            @Override
            public void onPostExecute(ModifiedPwdBean result) {
                mRootView.finish();
                if (result == null) {
                    Util.showUserToast(R.string.data_uploader_failed, -1, -1);
                } else {
                    if(result.getStatus().getCode() == 0){
                        Util.showUserToast(R.string.pwd_modify_succeed, -1, -1);
                        if(result.getData() != null && result.getData().size()>= 0){
                            ModifiedPwdBean.DataEntity dataEntity = result.getData().get(0);
                            LoginModel.setToken(dataEntity.getToken());
                            LoginModel.getUserinfoEntity().setId(dataEntity.getUid());
                            LoginModel.savaCacheData();
                            finish();
                        }
                    } else{
                        if(!TextUtils.isEmpty(result.getStatus().getDesc())){
                            Util.showUserToast(result.getStatus().getDesc(), null, null);
                        } else {
                            Util.showUserToast(R.string.data_uploader_failed, -1, -1);
                        }
                    }
                }
            }
        }.start();
    }
}
