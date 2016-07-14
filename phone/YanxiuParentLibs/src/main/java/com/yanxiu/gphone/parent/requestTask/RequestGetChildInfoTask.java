package com.yanxiu.gphone.parent.requestTask;

import android.app.Activity;
import android.content.Context;

import com.common.core.utils.LogInfo;
import com.common.login.model.DataStatusEntityBean;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.parent.bean.ParentGetChildInfoBean;
import com.yanxiu.gphone.parent.httpApi.YanxiuParentHttpApi;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;
import com.yanxiu.gphone.parent.jump.out.utils.ParentOutActivityJumpUtils;
import com.yanxiu.gphone.parent.parser.ParentGetChildInfoParser;

/**
 * Created by lee on 16-3-29.
 */
public class RequestGetChildInfoTask extends  AbstractAsyncTask<ParentGetChildInfoBean> {
    private AsyncCallBack mAsyncCallBack;
    private String stdUid;
    private ParentGetChildInfoBean mResult;
    public RequestGetChildInfoTask(Context context,String stdUid,AsyncCallBack mAsyncCallBack) {
        super(context);
        this.stdUid=stdUid;
        this.mAsyncCallBack=mAsyncCallBack;
    }
    @Override
    public void tokenInvalidate(String msg) {
        super.tokenInvalidate(msg);
        //由于TOKEN失效导致执行了登出操作，但此时无法在MainForParent进行接收，所以需要在parentBindAccountActivity执行跳转
        //登录选择页面的操作。
        LogInfo.log("geny", "jumpOutToLoginChoiceActivity-----------" + msg);
        ParentOutActivityJumpUtils.jumpOutToLoginChoiceActivity(context);
        ((Activity)context).finish();
    }
    @Override
    public YanxiuDataHull<ParentGetChildInfoBean> doInBackground() {
        YanxiuDataHull infoBean=YanxiuParentHttpApi.requestGetChildInfo(stdUid,new ParentGetChildInfoParser());
        mResult= (ParentGetChildInfoBean) infoBean.getDataEntity();
        return infoBean;
    }

    @Override
    public void onPostExecute(int updateId, ParentGetChildInfoBean result) {
        if(result != null&&result.getStatus()!=null&&mAsyncCallBack!=null){
            if(result.getStatus().getCode()== DataStatusEntityBean.REQUEST_SUCCESS) {
                mAsyncCallBack.update(result);
            } else {
                mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, result.getStatus().getDesc());
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
            mAsyncCallBack.dataError(ErrorCode.NETWORK_NOT_AVAILABLE, mResult!=null?mResult.getStatus()!= null?mResult.getStatus().getDesc():null:null);
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
