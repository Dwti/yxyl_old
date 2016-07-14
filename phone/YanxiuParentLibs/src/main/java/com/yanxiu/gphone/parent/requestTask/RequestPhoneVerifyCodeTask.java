package com.yanxiu.gphone.parent.requestTask;

import android.content.Context;

import com.common.core.utils.ContextProvider;
import com.common.login.model.DataStatusEntityBean;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.bean.ParentSendMsgBean;
import com.yanxiu.gphone.parent.httpApi.YanxiuParentHttpApi;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;
import com.yanxiu.gphone.parent.parser.ParentPhoneVerifyParser;

/**
 * 请求验证码
 * Created by lee on 16-3-28.
 */
public class RequestPhoneVerifyCodeTask extends AbstractAsyncTask<ParentSendMsgBean> {
    private String mobile;
    private AsyncCallBack mAsyncCallBack;
    private ParentSendMsgBean mResult;
    private int type;
    public RequestPhoneVerifyCodeTask(Context context,String mobile,int type,AsyncCallBack mAsyncCallBack) {
        super(context);
        this.mobile=mobile;
        this.mAsyncCallBack= mAsyncCallBack;
        this.type=type;
    }

    @Override
    public YanxiuDataHull<ParentSendMsgBean> doInBackground() {
        YanxiuDataHull<ParentSendMsgBean> dataHull=YanxiuParentHttpApi.requestVerifyCode(mobile, type, new ParentPhoneVerifyParser());
        mResult=dataHull.getDataEntity();
        return dataHull;
    }

    @Override
    public void onPostExecute(int updateId, ParentSendMsgBean result) {
        if (result != null && result.getStatus() != null) {
            if (result.getStatus().getCode() == DataStatusEntityBean.REQUEST_SUCCESS) {
                mAsyncCallBack.update(result);
            } else {
                mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL, result.getStatus().getDesc());

            }
        }else{
                  String msg= ContextProvider.getApplicationContext().getResources().getString(R.string.data_request_fail_p);
                  mAsyncCallBack.dataError(ErrorCode.DATA_REQUEST_NULL,msg);
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
