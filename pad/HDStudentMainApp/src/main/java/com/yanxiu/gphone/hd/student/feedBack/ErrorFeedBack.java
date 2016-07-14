package com.yanxiu.gphone.hd.student.feedBack;

import android.content.Context;

import com.yanxiu.gphone.hd.student.requestTask.RequestErrorFeedBackTask;

/**
 * Created by Administrator on 2015/11/13.
 */
public class ErrorFeedBack extends AbstractFeedBack {
    private String questionId;
    private RequestErrorFeedBackTask mTask;
    public ErrorFeedBack(Context context) {
        this.mContext=context;
    }

    @Override
    public void setTextWatcherListener(TextWatcherListener textWatcherListener) {
        this.textWatcherListener=textWatcherListener;
    }

    @Override
    public void setAsyncCallBack(AsyncCallBack asyncCallBack) {
        this.asyncCallBack=asyncCallBack;
    }

    @Override
    public void startTask(String content) {

            if(asyncCallBack!=null){
                asyncCallBack.preExecute();
            }
            cancelTask();
            mTask=new RequestErrorFeedBackTask(mContext,questionId,content,this);
            mTask.start();

    }

    @Override
    public void cancelTask() {
        if(mTask!=null){
            mTask.cancel();
            mTask=null;
        }
    }

    @Override
    public void setParams(Object o) {
        if(o==null){
            return;
        }
        questionId= (String) o;
    }

    @Override
    protected void handlerMinTips() {

    }

    @Override
    protected void handlerMaxTips() {

    }

    @Override
    protected int getMaxNums() {
        return 0;
    }

    @Override
    protected int getMinNums() {
        return 0;
    }
}
