package com.yanxiu.gphone.parent.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.StringUtils;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.activity.base.TopViewBaseActivity;
import com.yanxiu.gphone.parent.contants.YanxiuParentConstants;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;
import com.yanxiu.gphone.parent.jump.ParentSetPsdJumpModel;
import com.yanxiu.gphone.parent.jump.utils.ParentActivityJumpUtils;
import com.yanxiu.gphone.parent.manager.ParentActivityManager;
import com.yanxiu.gphone.parent.requestTask.RequestRegisterTask;
import com.yanxiu.gphone.parent.requestTask.RequestSetPasswordTask;
import com.yanxiu.gphone.parent.utils.ParentUtils;

/**
 * Created by lee on 16-3-22.
 */
public class ParentSetPsdActivity extends TopViewBaseActivity {
    private View mView;
    private EditText mInputPdsEd,mConfirmPdsEd;
    private TextView mSetNextEd;
    private int mFrom;
    private String mobile,msgCode;
    private RequestRegisterTask mTask;
    private RequestSetPasswordTask mSetpasswordTask;
    @Override
    protected void setRootView() {
        super.setRootView();
        rootView.setBackgroundResource(R.drawable.login_in_common_bg);
    }

    @Override
    protected void setTopView() {
        super.setTopView();
        topRootView.setBackgroundColor(getResources().getColor(R.color.color_white_p));
        titleText.setText(getResources().getString(R.string.set_pds));
        titleText.setTextColor(getResources().getColor(R.color.color_b28f47_p));
    }

    @Override
    protected boolean isAttach() {
        return true;
    }

    @Override
    protected View getContentView() {
        mView=getAttachView(R.layout.parent_set_psd_layout);
        initView();
        return mView;
    }

    private void initView() {
        mInputPdsEd=(EditText)mView.findViewById(R.id.inputPsw);
        mConfirmPdsEd=(EditText)mView.findViewById(R.id.confirmPsw);
        CommonCoreUtil.setViewTypeface(YanxiuParentConstants.METRO_STYLE, mInputPdsEd, mConfirmPdsEd);
        mSetNextEd=(TextView)mView.findViewById(R.id.setNextTv);
    }

    @Override
    protected void setContentListener() {
        mSetNextEd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId()==R.id.setNextTv){
            if(StringUtils.isEmpty(mInputPdsEd.getText().toString())){
                ParentUtils.showToast(R.string.password_can_not_empty);
                return;
            }
            if(StringUtils.isEmpty(mConfirmPdsEd.getText().toString())){
                ParentUtils.showToast(R.string.confirm_password_can_not_empty);
                return;
            }
            String password = mInputPdsEd.getText().toString();
            if(!CommonCoreUtil.isPasswordRight(password)){
                ParentUtils.showToast(R.string.set_password_6_8);
                return;
            }

            if(!(mInputPdsEd.getText().toString().equals(mConfirmPdsEd.getText().toString()))){
                ParentUtils.showToast(R.string.psd_is_not_same);
                return;
            }



            switch (mFrom){
                case YanxiuParentConstants.LOGIN_FROM.FROM_FORGET_FROM:
                    requestToServerForForgetPsd();
                    break;
                case YanxiuParentConstants.LOGIN_FROM.FROM_REGISTER_FROM:
                    requestToServerForRegister();
                    break;
            }


        }
    }

    private void requestToServerForForgetPsd() {
        cancelSetPsdTask();
        mSetpasswordTask=new RequestSetPasswordTask(ParentSetPsdActivity.this,mobile,mInputPdsEd.getText().toString(),new AsyncCallBack(){
            @Override
            public void update(YanxiuBaseBean result) {
                MainForParentActivity.launchActivity(ParentSetPsdActivity.this);
                ParentActivityManager.destoryAllActivity();
            }

            @Override
            public void dataError(int type, String msg) {
                showErrorWithFlag(type, getResources().getString(R.string.modify_net_exception), false);
            }
        });
        mSetpasswordTask.start();
    }

    private void cancelSetPsdTask(){
        if(mSetpasswordTask!=null){
            mSetpasswordTask.cancel();
            mSetpasswordTask=null;
        }
    }


    private void cancelTask(){
        if(mTask!=null){
            mTask.cancel();
            mTask=null;
        }

    }
    private void requestToServerForRegister(){
        cancelTask();
        mTask=new RequestRegisterTask(ParentSetPsdActivity.this,mobile,mInputPdsEd.getText().toString(),msgCode,new AsyncCallBack(){
            @Override
            public void update(YanxiuBaseBean result) {
                ParentActivityJumpUtils.jumpToParentBindAccountActivity(ParentSetPsdActivity.this, YanxiuParentConstants.FROM_SETPSD);
            }

            @Override
            public void dataError(int type, String msg) {
                 showErrorWithFlag(type,getResources().getString(R.string.register_net_exception),false);
            }
        });
        mTask.start();
    }

    @Override
    protected void showErrorWithFlag(int type, String msg, boolean isFristLoad) {
        switch (type){
            case ErrorCode.NETWORK_NOT_AVAILABLE:
                showNetErrorToast(msg);
                break;
            case ErrorCode.NETWORK_REQUEST_ERROR:

            case ErrorCode.DATA_REQUEST_NULL:

            case ErrorCode.JOSN_PARSER_ERROR:
                showDataError(isFristLoad);
                break;
        }
    }

    @Override
    protected void destoryData() {
        cancelTask();
        cancelSetPsdTask();
        CommonCoreUtil.hideSoftInput(mInputPdsEd);
        CommonCoreUtil.hideSoftInput(mConfirmPdsEd);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonCoreUtil.hideSoftInput(mInputPdsEd);
        CommonCoreUtil.hideSoftInput(mConfirmPdsEd);
    }

    @Override
    protected void initLaunchIntentData() {
        ParentSetPsdJumpModel jump= (ParentSetPsdJumpModel) getBaseJumpModel();
        if(jump==null){
            return;
        }
        mFrom=jump.getFrom();
        mobile=jump.getMobile();
        msgCode=jump.getVerifyCode();
    }
}
