package com.yanxiu.gphone.hd.student.requestTask;

import android.content.Context;

import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.hd.student.bean.WXLoginBean;
import com.yanxiu.gphone.hd.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.parser.WXLoginParse;

/**
 * Created by Administrator on 2015/6/1.
 */
public class RequestTokenBackTask extends AbstractAsyncTask<WXLoginBean>{
    private AsyncCallBack mAsyncCallBack;
    private String code;
    private String appid;
    private String secret;
    private String grant_type;
    public RequestTokenBackTask(Context context, String code, String appid,
            String secret,String grant_type,AsyncCallBack mAsyncCallBack) {
        super(context);
        this.code = code;
        this.appid = appid;
        this.secret = secret;
        this.grant_type = grant_type;
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<WXLoginBean> doInBackground() {
        return YanxiuHttpApi.requestWXToken(code, appid, secret,grant_type, new WXLoginParse());
    }

    @Override
    public void onPostExecute(int updateId, WXLoginBean result) {
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
