package com.yanxiu.gphone.parent.activity;

import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.activity.base.TopViewBaseActivity;
import com.yanxiu.gphone.parent.contants.YanxiuParentConstants;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;
import com.yanxiu.gphone.parent.jump.ParentVerifyJumpModel;
import com.yanxiu.gphone.parent.jump.utils.ParentActivityJumpUtils;
import com.yanxiu.gphone.parent.requestTask.RequestCheckVerifyCodeTask;
import com.yanxiu.gphone.parent.requestTask.RequestPhoneVerifyCodeTask;
import com.yanxiu.gphone.parent.utils.ParentUtils;
import com.yanxiu.gphone.parent.view.ParentLoadingLayout;

/**
 * Created by lee on 16-3-21.
 */
public class ParentVerifyActivity extends TopViewBaseActivity {
    private static final String TAG=ParentVerifyActivity.class.getSimpleName();
    private EditText inputEdit, verifyCodeEdit;
    private TextView nextTv, vetifyCodeTv;
    private int mFrom;
    private TimeCount time;
    private static final int TOTAL_TIME = 46000;
    private static final int EVERY_TIME = 1000;
    private RequestPhoneVerifyCodeTask mTask;
    private RequestCheckVerifyCodeTask mCheckTask;

    @Override
    protected void setTopView() {
        super.setTopView();
        topRootView.setBackgroundColor(getResources().getColor(R.color.color_white_p));
        titleText.setText(getResources().getString(R.string.verify_phone_num));
        titleText.setTextColor(getResources().getColor(R.color.color_b28f47_p));
    }

    @Override
    protected void setRootView() {
        super.setRootView();
        rootView.setBackgroundResource(R.drawable.login_in_common_bg);
    }

    @Override
    protected boolean isAttach() {
        return true;
    }

    @Override
    protected View getContentView() {
        View mView = getAttachView(R.layout.parent_verify_layout);
        initView();
        return mView;
    }

    private void initView() {
        initLoadingBar(ParentVerifyActivity.this,INDEX_SECOND);
        vetifyCodeTv = (TextView) findViewById(R.id.vetifyCodeTv);
        inputEdit = (EditText) findViewById(R.id.inputPhone);
        verifyCodeEdit = (EditText) findViewById(R.id.inputVerifyCode);
        CommonCoreUtil.setViewTypeface(YanxiuParentConstants.METRO_STYLE, inputEdit, verifyCodeEdit);
        nextTv = (TextView) findViewById(R.id.verifyNextTv);
        time = new TimeCount(TOTAL_TIME, EVERY_TIME);

    }

    @Override
    protected void setContentListener() {
        nextTv.setOnClickListener(this);
        vetifyCodeTv.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.verifyNextTv) {
            if(StringUtils.isEmpty(inputEdit.getText().toString())){
                ParentUtils.showToast(R.string.phone_is_cannot_empty);
                return;
            }
            if(StringUtils.isEmpty(verifyCodeEdit.getText().toString())){
                ParentUtils.showToast(R.string.verifycode_cannot_empty);
                return;
            }
            if (!CommonCoreUtil.isMobileNo(inputEdit.getText().toString().replaceAll(" ", ""))) {
                ParentUtils.showToast(R.string.login_name_ival_p);
                return;
            }
            inputEdit.setEnabled(false);
            verifyCodeEdit.setEnabled(false);
            requestToServerForCheckCode(mFrom);

        } else if (view.getId() == R.id.vetifyCodeTv) {
            if(StringUtils.isEmpty(inputEdit.getText().toString())){
                ParentUtils.showToast(R.string.phone_is_cannot_empty);
                return;
            }
            if (!CommonCoreUtil.isMobileNo(inputEdit.getText().toString().replaceAll(" ", ""))) {
                ParentUtils.showToast(R.string.login_name_ival_p);
                return;
            }
            time.start();
            switch (mFrom){
                case YanxiuParentConstants.LOGIN_FROM.FROM_FORGET_FROM:
                    requestToServerForSendCode(YanxiuParentConstants.LOGIN_FROM.FROM_FORGET_FROM);
                    break;
                case YanxiuParentConstants.LOGIN_FROM.FROM_REGISTER_FROM:
                    requestToServerForSendCode(YanxiuParentConstants.LOGIN_FROM.FROM_REGISTER_FROM);
                    break;
            }



        }
    }

    private void requestToServerForCheckCode(int type){
       cancelCheckCodeTask();
        loading.setViewType(ParentLoadingLayout.LoadingType.LAODING_COMMON);
        mCheckTask=new RequestCheckVerifyCodeTask(ParentVerifyActivity.this, inputEdit.getText().toString(), verifyCodeEdit.getText().toString(),type, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                loading.setViewGone();
                inputEdit.setEnabled(true);
                verifyCodeEdit.setEnabled(true);
                ParentActivityJumpUtils.jumpToParentSetPsdActivity(ParentVerifyActivity.this, mFrom,inputEdit.getText().toString(),verifyCodeEdit.getText().toString());
            }

            @Override
            public void dataError(int type, String msg) {
                loading.setViewGone();
                inputEdit.setEnabled(true);
                verifyCodeEdit.setEnabled(true);
                if(StringUtils.isEmpty(msg)){
                    ParentUtils.showToast(R.string.data_request_fail_p);
                }else{
                    ParentUtils.showToast(msg);
                }
            }
        });
        mCheckTask.start();
    }

    private void requestToServerForSendCode(int type){
        cancelSendCodeTask();
        mTask=new RequestPhoneVerifyCodeTask(ParentVerifyActivity.this, inputEdit.getText().toString(),type, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                LogInfo.log(TAG,"update");
            }

            @Override
            public void dataError(int type, String msg) {
                cancelCountTime();
                if(StringUtils.isEmpty(msg)){
                    ParentUtils.showToast(R.string.data_request_fail_p);
                }else{
                    ParentUtils.showToast(msg);
                }

                LogInfo.log(TAG, "dataError");
            }
        });
        mTask.start();
    }

    private void cancelSendCodeTask(){
        if(mTask!=null){
            mTask.cancel();
            mTask=null;
        }
    }

    private void cancelCheckCodeTask(){
        if(mCheckTask!=null){
            mCheckTask.cancel();
            mCheckTask=null;
        }
    }


    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            LogInfo.log(TAG, "millisUntilFinished:onFinish ");
            defaultSet();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            LogInfo.log(TAG, "millisUntilFinished: " + millisUntilFinished);
            vetifyCodeTv.setClickable(false);
            String mill=millisUntilFinished / 1000 + "";
            vetifyCodeTv.setText(String.format(ParentVerifyActivity.this.getResources().getString(R.string.count_time),mill));
        }
    }

    private void defaultSet(){
        vetifyCodeTv.setText(getResources().getString(R.string.get_verify_code));
        vetifyCodeTv.setClickable(true);
    }

    private void cancelCountTime() {
        if (time != null) {
            time.cancel();
            defaultSet();
        }
    }

    @Override
    protected void destoryData() {
        cancelCountTime();
        cancelSendCodeTask();
        cancelCheckCodeTask();
        CommonCoreUtil.hideSoftInput(inputEdit);
        CommonCoreUtil.hideSoftInput(verifyCodeEdit);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonCoreUtil.hideSoftInput(inputEdit);
        CommonCoreUtil.hideSoftInput(verifyCodeEdit);
        cancelCountTime();
        cancelSendCodeTask();
        cancelCheckCodeTask();
    }

    @Override
    protected void initLaunchIntentData() {
        ParentVerifyJumpModel mJump = (ParentVerifyJumpModel) getBaseJumpModel();
        if (mJump == null) {
            return;
        }
        mFrom = mJump.getFrom();
    }

}

