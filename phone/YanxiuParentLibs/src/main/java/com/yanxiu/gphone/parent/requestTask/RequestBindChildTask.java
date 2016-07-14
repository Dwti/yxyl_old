package com.yanxiu.gphone.parent.requestTask;

import android.app.Activity;
import android.content.Context;

import com.common.core.utils.LogInfo;
import com.common.login.model.DataStatusEntityBean;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.parent.bean.ParentBindChildBean;
import com.yanxiu.gphone.parent.httpApi.YanxiuParentHttpApi;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;
import com.yanxiu.gphone.parent.jump.out.utils.ParentOutActivityJumpUtils;
import com.yanxiu.gphone.parent.parser.ParentBindChildInfoParser;

/**
 * Created by lee on 16-3-29.
 */
public class RequestBindChildTask extends  AbstractAsyncTask<ParentBindChildBean> {
    private String stduid;
    private AsyncCallBack mAsyncCallBack;
    private ParentBindChildBean mBindChildBean;
    private Context context;
    public RequestBindChildTask(Context context,String stduid,AsyncCallBack mAsyncCallBack) {
        super(context);
        this.context=context;
        this.stduid=stduid;
        this.mAsyncCallBack=mAsyncCallBack;
    }

    @Override
    public void tokenInvalidate(String msg) {
        super.tokenInvalidate(msg);
        //由于TOKEN失效导致执行了登出操作，但此时无法在MainForParent进行接收，所以需要在ParentBindDataShowActivity执行跳转
        //登录选择页面的操作。
        LogInfo.log("geny", "ParentBindDataShowActivity-----------" + msg);
        ParentOutActivityJumpUtils.jumpOutToLoginChoiceActivity(context);
        ((Activity)context).finish();
    }

    @Override
    public YanxiuDataHull<ParentBindChildBean> doInBackground() {
        YanxiuDataHull dataHUll=YanxiuParentHttpApi.requestBindChildAccount(stduid,new ParentBindChildInfoParser());
        if(dataHUll!=null){
            mBindChildBean= (ParentBindChildBean) dataHUll.getDataEntity();
        }
        return dataHUll;
    }

    @Override
    public void onPostExecute(int updateId, ParentBindChildBean result) {
        if(result!=null){
            if(result.getStatus().getCode()== DataStatusEntityBean.REQUEST_SUCCESS){
                mAsyncCallBack.update(result);
            }else{
                mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, result.getStatus().getDesc());
            }
        }else{
            mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, null);
        }
    }

    @Override
    public void netNull() {
        if(mAsyncCallBack != null){
            mAsyncCallBack.dataError(ErrorCode.NETWORK_NOT_AVAILABLE, mBindChildBean!=null?mBindChildBean.getStatus()!= null?mBindChildBean.getStatus().getDesc():null:null);
        }
    }

    @Override
    public void netErr(int updateId, String errMsg) {
        if(mAsyncCallBack != null){
            mAsyncCallBack.dataError(ErrorCode.NETWORK_REQUEST_ERROR, mBindChildBean!=null?mBindChildBean.getStatus()!=null?mBindChildBean.getStatus().getDesc():null:null);
        }
    }

    @Override
    public void dataNull(int updateId, String errMsg) {
        if(mAsyncCallBack != null){
            mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, mBindChildBean!=null?mBindChildBean.getStatus()!=null?mBindChildBean.getStatus().getDesc():null:null);
        }
    }

}
