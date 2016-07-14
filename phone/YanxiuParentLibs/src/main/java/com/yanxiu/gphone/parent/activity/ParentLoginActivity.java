package com.yanxiu.gphone.parent.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.common.login.model.ParentInfoBean;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.activity.base.TopViewBaseActivity;
import com.yanxiu.gphone.parent.contants.YanxiuParentConstants;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;
import com.yanxiu.gphone.parent.jump.utils.ParentActivityJumpUtils;
import com.yanxiu.gphone.parent.requestTask.RequestLoginTask;
import com.yanxiu.gphone.parent.utils.ParentUtils;
import com.yanxiu.gphone.parent.utils.PublicLoadUtils;
import com.yanxiu.gphone.parent.view.LineEditText;

/**
 * Created by Administrator on 2016/3/18.
 */
public class ParentLoginActivity extends TopViewBaseActivity {
    private TextView loginTv;
    private EditText mPhoneEd,mPassEd;
    private TextView mForgetPasTv,mFastRegisterTv;
    private RequestLoginTask mTask;
    private static final String TAG=ParentLoginActivity.class.getSimpleName();
    @Override
    protected void setRootView() {
        super.setRootView();
        rootView.setBackgroundResource(R.drawable.login_center_bg);
    }

    @Override
    protected void setTopView() {
        super.setTopView();
        topRootView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    @Override
    protected boolean isAttach() {
        return false;
    }

    @Override
    protected View getContentView() {
        mPublicLayout = PublicLoadUtils.createPage(ParentLoginActivity.this, R.layout
                .login_parent_activity_layout);
        initView();
        finishPublicLayout();
        return mPublicLayout;
    }

    private void initView() {
        loginTv=(TextView)mPublicLayout.findViewById(R.id.loginTv);
        mPhoneEd=(LineEditText)mPublicLayout.findViewById(R.id.userInputEdit);
        mPhoneEd.requestFocus();
        mPassEd=(EditText)mPublicLayout.findViewById(R.id.pasEdit);
        mForgetPasTv=(TextView)mPublicLayout.findViewById(R.id.forgetPasTv);
        mFastRegisterTv=(TextView)mPublicLayout.findViewById(R.id.fastRegisteTv);
        CommonCoreUtil.setViewTypeface(YanxiuParentConstants.METRO_STYLE, mPassEd, mPhoneEd);

    }

    @Override
    protected void setContentListener() {
        loginTv.setOnClickListener(this);
        mForgetPasTv.setOnClickListener(this);
        mFastRegisterTv.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId()==R.id.loginTv){
            if (StringUtils.isEmpty(mPhoneEd.getText().toString())) {
                ParentUtils.showToast(R.string.login_name_null_p);
                return;
            }
            if (StringUtils.isEmpty(mPassEd.getText().toString())) {
                ParentUtils.showToast(R.string.login_pwd_null_p);
                return;
            }
            if(!CommonCoreUtil.isNetAvailableForPlay(ParentLoginActivity.this)){
                ParentUtils.showToast(R.string.public_loading_net_errtxt_p);
                return;
            }
            if (!CommonCoreUtil.isMobileNo(mPhoneEd.getText().toString().replaceAll(" ", ""))) {
                ParentUtils.showToast(R.string.login_name_ival_p);
                return;
            }
            CommonCoreUtil.hideSoftInput(mPhoneEd);
            CommonCoreUtil.hideSoftInput(mPassEd);
            requesetToServerForLogin();
        }else if(view.getId()==R.id.forgetPasTv){
            ParentActivityJumpUtils.jumpToParentVerifyActivity(ParentLoginActivity.this,YanxiuParentConstants.LOGIN_FROM.FROM_FORGET_FROM);
        }else if(view.getId()==R.id.fastRegisteTv){
            ParentActivityJumpUtils.jumpToParentVerifyActivity(ParentLoginActivity.this,YanxiuParentConstants.LOGIN_FROM.FROM_REGISTER_FROM);
        }

    }

    private void requesetToServerForLogin(){
        cancelTask();
        mPublicLayout.loading(true);
        mTask=new RequestLoginTask(ParentLoginActivity.this,mPhoneEd.getText().toString(),mPassEd.getText().toString(),callBack);
        mTask.start();

    }

    private void cancelTask(){
        if(mTask!=null){
            mTask.cancel();
            mTask=null;
        }
    }
    private final AsyncCallBack callBack=new AsyncCallBack() {
        @Override
        public void update(YanxiuBaseBean result) {
            finishPublicLayout();
            ParentInfoBean infoBean= (ParentInfoBean) result;
            if(infoBean==null||infoBean.getProperty()==null){
                return;
            }
            if(infoBean.getProperty().getIsBind()==YanxiuParentConstants.HASBIND){
                MainForParentActivity.launchActivity(ParentLoginActivity.this);
                finish();
            }else{
                ParentActivityJumpUtils.jumpToParentBindAccountActivity(ParentLoginActivity.this,-1);
            }

        }

        @Override
        public void dataError(int type, String msg) {
            finishPublicLayout();
            showErrorWithFlag(type, msg, false);
            LogInfo.log(TAG,"msg: "+msg);

        }
    };

    @Override
    protected void showErrorWithFlag(int type, String msg, boolean isFristLoad) {
        switch (type){
            case ErrorCode.NETWORK_NOT_AVAILABLE:
                showNetErrorToast(R.string.login_net_exception);
                break;
            case ErrorCode.NETWORK_REQUEST_ERROR:

            case ErrorCode.DATA_REQUEST_NULL:

            case ErrorCode.JOSN_PARSER_ERROR:
                if(StringUtils.isEmpty(msg)){
                    ParentUtils.showToast(R.string.data_request_fail_p);
                }else{
                    ParentUtils.showToast(msg);
                }
                break;
        }
    }

    @Override
    protected void destoryData() {
         cancelTask();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTask();
        CommonCoreUtil.hideSoftInput(mPhoneEd);
        CommonCoreUtil.hideSoftInput(mPassEd);
    }

    @Override
    protected void initLaunchIntentData() {

    }
}
