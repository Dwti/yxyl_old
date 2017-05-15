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
 * Created by Administrator on 2016/11/7.
 */

public class ThirdRequestNewAddClassTask extends AbstractAsyncTask<UserInfoBean> {

    private AsyncCallBack mAsyncCallBack;
    private int classId;
    private int stageid;
    private int areaid;
    private int cityid;
    private String openid;
    private String realname;
    private int schoolid;
    private String pltform;
    private String schoolName;
    private int provinceid;
    private int sex;
    private String headimg;

    public ThirdRequestNewAddClassTask (Context context, int classId, int stageid, int areaid, int cityid, String openid, String realname, int schoolid,String pltform, String schoolName,String headimg,int sex, int provinceid, AsyncCallBack mAsyncCallBack) {
        super(context);
        this.stageid=stageid;
        this.areaid=areaid;
        this.cityid=cityid;
        this.openid=openid;
        this.realname=realname;
        this.schoolid=schoolid;
        this.pltform=pltform;
        this.headimg=headimg;
        this.sex=sex;
        this.schoolName=schoolName;
        this.provinceid=provinceid;
        this.classId = classId;
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<UserInfoBean> doInBackground() {
        YanxiuDataHull<UserInfoBean> dataHull=YanxiuHttpApi.thirdRequestNewAddClass(0,classId, stageid, areaid, cityid, openid, realname, schoolid,pltform, schoolName,headimg,sex, provinceid,new LoginParser());
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
