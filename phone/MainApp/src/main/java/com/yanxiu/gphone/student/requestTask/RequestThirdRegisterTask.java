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
public class RequestThirdRegisterTask extends AbstractAsyncTask<UserInfoBean>{
    private AsyncCallBack mAsyncCallBack;
    private String openid;
    private String uniqid;
    private String nickName;
    private String sex;
    private String headimg;
    private String pltform;
    private String deviceId;
    private String realname;
    private String provinceid;
    private String schoolname;
    private String schoolid;
    private String cityid;
    private String areaid;
    private String stageid;
    public RequestThirdRegisterTask(Context context, String openid,String uniqid,
            String pltform, String headimg, String sex,String nickName,String deviceId,String realname,
            String provinceid,String schoolname,String schoolid,String cityid,String areaid,String stageid,
            AsyncCallBack mAsyncCallBack) {
        super(context);
        this.openid = openid;
        this.uniqid = uniqid;
        this.nickName = nickName;
        this.sex = sex;
        this.headimg = headimg;
        this.pltform = pltform;
        this.deviceId = deviceId;
        this.realname = realname;
        this.provinceid = provinceid;
        this.schoolname = schoolname;
        this.schoolid = schoolid;
        this.cityid = cityid;
        this.areaid = areaid;
        this.stageid = stageid;
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<UserInfoBean> doInBackground() {
        YanxiuDataHull<UserInfoBean> dataHull = YanxiuHttpApi.requestThirdRegister(nickName, sex,headimg,openid,uniqid,pltform,deviceId,realname,provinceid,schoolname,schoolid,
                cityid,areaid,stageid,new LoginParser());
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
