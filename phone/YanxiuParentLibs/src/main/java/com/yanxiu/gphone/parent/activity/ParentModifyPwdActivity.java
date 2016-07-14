package com.yanxiu.gphone.parent.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.StrMD5;
import com.common.login.LoginModel;
import com.common.login.model.ParentInfo;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.task.base.threadpool.YanxiuSimpleAsyncTask;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.activity.base.TopViewBaseActivity;
import com.yanxiu.gphone.parent.bean.ParentResetPwdBean;
import com.yanxiu.gphone.parent.httpApi.YanxiuParentHttpApi;
import com.yanxiu.gphone.parent.parser.ParentResetPwdParser;
import com.yanxiu.gphone.parent.utils.ParentUtils;
import com.yanxiu.gphone.parent.utils.PublicLoadUtils;

import java.util.HashMap;

/**
 * Created by lidongming on 16/3/18.
 * 家长端-------修改密码
 */
public class ParentModifyPwdActivity extends TopViewBaseActivity {

//    private View mView;

    private EditText oldPwdEt, newPwdEt, newPwdAgainEt;

    public static void launch(Activity context) {
        Intent intent = new Intent(context, ParentModifyPwdActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected View getContentView() {
        mPublicLayout = PublicLoadUtils.createPage(this, R.layout.parent_modity_pwd_layout);
//        mPublicLayout= LayoutInflater.from(this).inflate(R.layout.parent_modity_pwd_layout,null);
        mPublicLayout.finish();
        initView();
        initData();
        return mPublicLayout;
    }


    private void initView(){

        oldPwdEt = (EditText) mPublicLayout.findViewById(R.id.input_current_pwd);
        newPwdEt = (EditText) mPublicLayout.findViewById(R.id.input_new_pwd);
        newPwdAgainEt = (EditText) mPublicLayout.findViewById(R.id.input_new_pwd_again);

    }

    private void initData() {
        titleText.setText(this.getResources().getString(R.string.parent_modify_pwd_txt));
        rightText.setText(this.getResources().getString(R.string.submmit));
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


    private void pwdTipsAndUpload(){

        String oldPwd = oldPwdEt.getEditableText().toString();
        String newPwd = newPwdEt.getEditableText().toString();
        String newpwdAgain = newPwdAgainEt.getEditableText().toString();
        if (TextUtils.isEmpty(oldPwd)) {
            ParentUtils.showToast(R.string.input_current_pwd);
            return;
        } else if (TextUtils.isEmpty(newPwd)) {
            ParentUtils.showToast(R.string.input_new_pwd);
            return;
        } else if (TextUtils.isEmpty(newpwdAgain)) {
            ParentUtils.showToast(R.string.input_new_pwd_again);
            return;
        } else if (!newPwd.equals(newpwdAgain)) {
            ParentUtils.showToast(R.string.new_pwd_not_equals);
            return;
        } else if(!ParentUtils.isPasswordRight(newpwdAgain)){
            ParentUtils.showToast(R.string.set_password_6_8);
            return;
        } else {
            uploadPwd(newPwd, oldPwd);
        }

    }

    private void uploadPwd(final String newPwd, final String oldPwd) {

        mPublicLayout.loading(true);

        new YanxiuSimpleAsyncTask<ParentResetPwdBean>(this){

            @Override
            public ParentResetPwdBean doInBackground() {
                YanxiuDataHull<ParentResetPwdBean> dataHull;
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("newPassWord", StrMD5.getStringMD5(newPwd));
                hashMap.put("oldPassWord", StrMD5.getStringMD5(oldPwd));
                hashMap.put("mobile", ((ParentInfo)LoginModel.getRoleUserInfoEntity()).getMobile());
                dataHull= YanxiuParentHttpApi.requestModifiedPwd(0, new ParentResetPwdParser(), hashMap);
                if(dataHull != null && dataHull.getDataType() == YanxiuDataHull.DataType.DATA_IS_INTEGRITY){
                    return dataHull.getDataEntity();
                } else {
                    return null;
                }
            }
            @Override
            public void onPostExecute(ParentResetPwdBean result) {
//                mRootView.finish();
                mPublicLayout.finish();
                if (result == null) {
                    ParentUtils.showToast(R.string.data_uploader_failed);
                } else {
                    if(result.getStatus().getCode() == 0
                                                    && result.getData() != null
                                                    && !result.getData().isEmpty()
                                                    && result.getData().get(0) != null
                                                    && !TextUtils.isEmpty(result.getData().get(0).getToken())){
                        ParentUtils.showToast(R.string.pwd_modify_succeed);
                        LoginModel.setToken(result.getData().get(0).getToken());
                        LoginModel.savaCacheData();
                        ParentModifyPwdActivity.this.finish();
//                        LoginModel.loginOut();
                    } else{
                        if(!TextUtils.isEmpty(result.getStatus().getDesc())){
                            ParentUtils.showToast(result.getStatus().getDesc());
                        } else {
                            ParentUtils.showToast(R.string.data_uploader_failed);
                        }
                    }
                }
            }
        }.start();

    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view == rightText){
            CommonCoreUtil.hideSoftInput(view);
            pwdTipsAndUpload();
        }
    }


    @Override
    protected void initLaunchIntentData() {

    }

    @Override
    protected boolean isAttach() {
        return false;
    }


    @Override
    protected void setContentListener() {

    }

    @Override
    protected void destoryData() {

    }



}
