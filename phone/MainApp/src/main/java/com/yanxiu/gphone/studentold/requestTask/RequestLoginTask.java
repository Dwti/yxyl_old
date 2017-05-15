package com.yanxiu.gphone.studentold.requestTask;

import android.content.Context;

import com.common.login.LoginModel;
import com.common.login.constants.LoginConstants;
import com.common.login.model.UserInfoBean;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.studentold.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.studentold.inter.AsyncCallBack;
import com.yanxiu.gphone.studentold.parser.LoginParser;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestLoginTask extends AbstractAsyncTask<UserInfoBean>{
    private AsyncCallBack mAsyncCallBack;
    private String mobile;
    private String password;
    public RequestLoginTask(Context context, String mobile, String password,
            AsyncCallBack mAsyncCallBack) {
        super(context);
        this.mobile = mobile;
        this.password = password;
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<UserInfoBean> doInBackground() {
        YanxiuDataHull<UserInfoBean> dataHull = YanxiuHttpApi.requestLoginInfo(0, mobile, password,
                new LoginParser());
        if(dataHull!=null && dataHull.getDataType() == YanxiuDataHull.DataType.DATA_IS_INTEGRITY){
            UserInfoBean userInfoBean = dataHull.getDataEntity();
            if(userInfoBean.getData()!=null && userInfoBean.getStatus().getCode()== 0 && userInfoBean.getData().get(0)!=null){
                LoginModel.loginIn(LoginConstants.ROLE_STUDENT,userInfoBean);
            }
        }
        return dataHull;
    }

    @Override
    public void onPostExecute(int updateId, UserInfoBean result) {
        if(result != null){
            if(mAsyncCallBack != null){
                mAsyncCallBack.update(result);
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
