package com.yanxiu.gphone.parent.requestTask;

import android.content.Context;

import com.common.core.utils.StrMD5;
import com.common.login.LoginModel;
import com.common.login.constants.LoginConstants;
import com.common.login.model.DataStatusEntityBean;
import com.common.login.model.ParentInfoBean;
import com.common.login.parser.ParentLoginParser;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.parent.httpApi.YanxiuParentHttpApi;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;


/**
 * Created by lee on 16-3-28.
 */
public class RequestLoginTask extends AbstractAsyncTask<ParentInfoBean> {
    private AsyncCallBack mAsyncCallBack;
    private String mobile;
    private String password;
    private ParentInfoBean mResult;
    public RequestLoginTask(Context context,String mobile,String password,AsyncCallBack mAsyncCallBack) {
        super(context);
        this.mobile=mobile;
        this.password=password;
        this.mAsyncCallBack=mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<ParentInfoBean> doInBackground() {
        YanxiuDataHull<ParentInfoBean> dataHull=YanxiuParentHttpApi.requestLogin(mobile, StrMD5.getStringMD5(password),new ParentLoginParser());
        if(dataHull!=null ){
            ParentInfoBean info=dataHull.getDataEntity();
            mResult=info;
            if(info!=null&&info.getProperty().getUser()!=null){
                if(info.getStatus().getCode()== DataStatusEntityBean.REQUEST_SUCCESS){
                    LoginModel.loginIn(LoginConstants.ROLE_PARENT,info);
                }
            }
        }
         return  dataHull;
    }

    @Override
    public void onPostExecute(int updateId, ParentInfoBean result) {
        if(result != null&&result.getStatus()!=null&&mAsyncCallBack!=null){
            if(result.getStatus().getCode()==DataStatusEntityBean.REQUEST_SUCCESS) {
                mAsyncCallBack.update(result);
            } else {
                mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, result.getStatus().getDesc());
            }
        } else {
            if(mAsyncCallBack != null){
                mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, mResult!=null?mResult.getStatus()!=null?mResult.getStatus().getDesc():null:null);
            }
        }
    }
    @Override
    public void netNull() {
        if(mAsyncCallBack != null){
            mAsyncCallBack.dataError(ErrorCode.NETWORK_NOT_AVAILABLE, mResult!=null?mResult.getStatus()!=null?mResult.getStatus().getDesc():null:null);
        }
    }

    @Override
    public void netErr(int updateId, String errMsg) {
        if(mAsyncCallBack != null){
            mAsyncCallBack.dataError(ErrorCode.NETWORK_REQUEST_ERROR, mResult!=null?mResult.getStatus()!=null?mResult.getStatus().getDesc():null:null);
        }
    }

    @Override
    public void dataNull(int updateId, String errMsg) {
        if(mAsyncCallBack != null){
            mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, mResult!=null?mResult.getStatus()!=null?mResult.getStatus().getDesc():null:null);
        }
    }
}
