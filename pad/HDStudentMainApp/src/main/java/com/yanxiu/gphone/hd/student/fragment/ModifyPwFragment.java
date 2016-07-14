package com.yanxiu.gphone.hd.student.fragment;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.common.core.utils.CommonCoreUtil;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.task.base.threadpool.YanxiuSimpleAsyncTask;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.bean.ModifiedPwdBean;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.hd.student.parser.ModifiedPwdBeanParse;
import com.yanxiu.gphone.hd.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.view.YanxiuTypefaceTextView;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/2/3.
 */
public class ModifyPwFragment extends TopBaseFragment {
    private YanxiuSimpleAsyncTask mTask;
    private SetContainerFragment mFg;
    private View submitView;
    private EditText oldPwdEt, newPwdEt, newPwdAgainEt;
    private ImageView clearCurrentPwdImg,clearNewPwd,clearNewPwdAgain;
    private static ModifyPwFragment modifyPwFragment;
    public static Fragment newInstance(){
        if(modifyPwFragment==null){
            modifyPwFragment=new ModifyPwFragment();
        }
        return modifyPwFragment;
    }


    @Override
    protected void setTopView() {
        super.setTopView();
        titleText.setText(R.string.modify_pwd_txt);
    }

    @Override
    protected void setRootView() {
        super.setRootView();
        rootView.setBackgroundResource(R.drawable.wood_bg);
    }

    @Override
    protected boolean isAttach() {
        return false;
    }

    @Override
    protected View getContentView() {
        mFg= (SetContainerFragment) getParentFragment();
        mPublicLayout= PublicLoadUtils.createPage(getActivity(), R.layout.activity_pwd_modify_setting_layout);
        mPublicLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mPublicLayout.setContentBackground(android.R.color.transparent);

        submitView = mPublicLayout.findViewById(R.id.submit_txt);
        Util.setViewTypeface(YanxiuTypefaceTextView.TypefaceType.FANGZHENG, (TextView) submitView);
        oldPwdEt = (EditText)mPublicLayout.findViewById(R.id.input_current_pwd);
        newPwdEt = (EditText)mPublicLayout.findViewById(R.id.input_new_pwd);
        newPwdAgainEt = (EditText)mPublicLayout.findViewById(R.id.input_new_pwd_again);

        clearCurrentPwdImg=(ImageView)mPublicLayout.findViewById(R.id.clearCurrentPwd);
        clearNewPwd=(ImageView)mPublicLayout.findViewById(R.id.clearNewPwd);
        clearNewPwdAgain=(ImageView)mPublicLayout.findViewById(R.id.clearNewPwdAgainPwd);

        return mPublicLayout;
    }

    @Override
    protected void initLoadData() {

    }

    @Override
    protected void setContentListener() {
        submitView.setOnClickListener(this);
        clearCurrentPwdImg.setOnClickListener(this);
        clearNewPwd.setOnClickListener(this);
        clearNewPwdAgain.setOnClickListener(this);
    }

    @Override
    protected void destoryData() {
        CommonCoreUtil.hideSoftInput(oldPwdEt);
        CommonCoreUtil.hideSoftInput(newPwdEt);
        CommonCoreUtil.hideSoftInput(newPwdAgainEt);
        cancelTask();
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFg=null;
        submitView=null;
        oldPwdEt=null;
        newPwdEt=null;
        newPwdAgainEt=null;
        clearCurrentPwdImg=null;
        clearNewPwd=null;
        clearNewPwdAgain=null;
    }

    private void finish(){
        CommonCoreUtil.hideSoftInput(oldPwdEt);
        CommonCoreUtil.hideSoftInput(newPwdEt);
        CommonCoreUtil.hideSoftInput(newPwdAgainEt);
        if(mFg!=null&&mFg.mIFgManager!=null){
            mFg.mIFgManager.popStack();
        }
        modifyPwFragment=null;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
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

    private void cancelTask(){
        if(mTask!=null){
            mTask.cancel();
            mTask=null;
        }
    }



    private void upLoadSchool(final String newPwd, final String oldPwd) {
        mPublicLayout.loading(true);
        cancelTask();
        mTask= new YanxiuSimpleAsyncTask<ModifiedPwdBean>(getActivity()){

            @Override
            public ModifiedPwdBean doInBackground() {
                YanxiuDataHull<ModifiedPwdBean> dataHull = null;
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("newPass", newPwd);
                hashMap.put("oldPass", oldPwd);
                hashMap.put("mobile", LoginModel.getUserinfoEntity().getMobile());
                dataHull= YanxiuHttpApi.requestModifiedPwd(0, new ModifiedPwdBeanParse(), hashMap);
                if(dataHull != null && dataHull.getDataType() == YanxiuDataHull.DataType.DATA_IS_INTEGRITY){
                    return dataHull.getDataEntity();
                } else {
                    return null;
                }
            }
            @Override
            public void onPostExecute(ModifiedPwdBean result) {
                mPublicLayout.finish();
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
        };
        mTask.start();
    }
    @Override
    protected IFgManager getFragmentManagerFromSubClass() {
        return null;
    }

    @Override
    protected int getFgContainerIDFromSubClass() {
        return 0;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            CommonCoreUtil.hideSoftInput(oldPwdEt);
            CommonCoreUtil.hideSoftInput(newPwdEt);
            CommonCoreUtil.hideSoftInput(newPwdAgainEt);
            cancelTask();
        }
    }

    @Override
    public void onReset() {
        destoryData();
    }
}
