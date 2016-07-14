package com.yanxiu.gphone.student.requestTask;

import android.content.Context;

import com.common.login.LoginModel;
import com.common.login.constants.LoginConstants;
import com.common.login.model.UserInfoBean;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.parser.LoginParser;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestRegisterTask extends AbstractAsyncTask<UserInfoBean>{
    private AsyncCallBack mAsyncCallBack;
    private String mobile;
    private String password;
    private String realName;
    private String nickName;
    private String provinceId;
    private String cityId;
    private String areaId;
    private String schoolId;
    private int stageId;
    private String schoolName;
    public RequestRegisterTask(Context context, String mobile, String password,String realName,
            String nickName,String provinceId,String cityId,String areaId,String schoolId,int stageId,String schoolName,
            AsyncCallBack mAsyncCallBack) {
        super(context);
        this.mobile = mobile;
        this.password = password;
        this.realName = realName;
        this.nickName = nickName;
        this.provinceId = provinceId;
        this.cityId = cityId;
        this.areaId = areaId;
        this.schoolId = schoolId;
        this.stageId = stageId;
        this.stageId = stageId;
        this.schoolName = schoolName;
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<UserInfoBean> doInBackground() {
        YanxiuDataHull<UserInfoBean> dataHull = YanxiuHttpApi.requestRegisterInfo(0, mobile, password, realName,
                nickName, provinceId,cityId,areaId, schoolId, stageId,schoolName, new LoginParser());
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
