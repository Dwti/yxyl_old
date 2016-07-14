package com.yanxiu.gphone.parent.requestTask;

import android.content.Context;

import com.common.core.utils.StrMD5;
import com.common.login.LoginModel;
import com.common.login.constants.LoginConstants;
import com.common.login.model.ParentInfoBean;
import com.common.login.parser.ParentLoginParser;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.parent.httpApi.YanxiuParentHttpApi;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;

/**
 * Created by lee on 16-3-29.
 */
public class RequestRegisterTask extends AbstractAsyncTask<ParentInfoBean> {
    private AsyncCallBack mAsyncCallBack;
    private String mobile;
    private String password;
    private String msgCode;
    public RequestRegisterTask(Context context,String mobile,String password,String msgCode,AsyncCallBack mAsyncCallBack) {
        super(context);
        this.mobile=mobile;
        this.password=password;
        this.msgCode=msgCode;
        this.mAsyncCallBack=mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<ParentInfoBean> doInBackground() {
        YanxiuDataHull<ParentInfoBean> dataHull=YanxiuParentHttpApi.requestRegister(mobile, StrMD5.getStringMD5(password), msgCode, new ParentLoginParser());
        if(dataHull!=null){
            ParentInfoBean info=dataHull.getDataEntity();
            if(info!=null){
                if(info.getProperty().getUser()!=null){
                    LoginModel.loginIn(LoginConstants.ROLE_PARENT, info);
                }
            }
        }
        return dataHull;
    }

    @Override
    public void onPostExecute(int updateId, ParentInfoBean result) {
        if(result != null){
            if(result != null) {
                mAsyncCallBack.update(result);
            } else {
                mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, null);
            }
        } else {
            if(mAsyncCallBack != null){
                mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, null);
            }
        }
    }
    @Override
    public void netNull() {
        if(mAsyncCallBack != null){
            mAsyncCallBack.dataError(ErrorCode.NETWORK_NOT_AVAILABLE, null);
        }
    }

    @Override
    public void netErr(int updateId, String errMsg) {
        if(mAsyncCallBack != null){
            mAsyncCallBack.dataError(ErrorCode.NETWORK_REQUEST_ERROR, errMsg);
        }
    }

    @Override
    public void dataNull(int updateId, String errMsg) {
        if(mAsyncCallBack != null){
            mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, errMsg);
        }
    }
}
