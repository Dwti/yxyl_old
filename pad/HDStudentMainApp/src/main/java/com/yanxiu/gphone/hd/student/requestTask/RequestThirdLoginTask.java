package com.yanxiu.gphone.hd.student.requestTask;

import android.content.Context;

import com.common.login.LoginModel;
import com.common.login.constants.LoginConstants;
import com.common.login.model.UserInfoBean;
import com.common.login.parser.LoginParser;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.hd.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;


/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestThirdLoginTask extends AbstractAsyncTask<UserInfoBean>{
    private AsyncCallBack mAsyncCallBack;
    private String openid;
    private String platfor;
    public RequestThirdLoginTask(Context context, String openid, String platfor,AsyncCallBack mAsyncCallBack) {
        super(context);
        this.openid = openid;
        this.platfor = platfor;
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<UserInfoBean> doInBackground() {
        YanxiuDataHull<UserInfoBean> dataHull = YanxiuHttpApi.requestThirdLoginUserInfo(openid, platfor, new LoginParser());
        if(dataHull!=null && dataHull.getDataType() == YanxiuDataHull.DataType.DATA_IS_INTEGRITY){
            UserInfoBean userInfoBean = dataHull.getDataEntity();
            if(userInfoBean.getData()!=null && userInfoBean.getStatus().getCode() == 0 && userInfoBean.getData().get(0)!=null){
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
