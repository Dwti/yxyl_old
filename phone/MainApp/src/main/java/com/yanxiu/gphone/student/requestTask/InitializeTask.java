package com.yanxiu.gphone.student.requestTask;

import android.content.Context;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.student.bean.InitializeBean;
import com.yanxiu.gphone.student.bean.NewInitializeBean;
import com.yanxiu.gphone.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.parser.InitializeParser;

/**
 * Created by Administrator on 2015/6/1.
 */
public class InitializeTask extends AbstractAsyncTask<NewInitializeBean>{
    private AsyncCallBack mAsyncCallBack;
    private String content;
    private String channel;
    public InitializeTask(Context context, String content,String channel,
            AsyncCallBack mAsyncCallBack) {
        super(context);
        this.content = content;
        this.channel=channel;
        this.mAsyncCallBack = mAsyncCallBack;
    }

    @Override
    public YanxiuDataHull<NewInitializeBean> doInBackground() {
        return YanxiuHttpApi
                .requestInitialize(0, content,channel, new InitializeParser());
    }

    @Override
    public void onPostExecute(int updateId, NewInitializeBean result) {
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
