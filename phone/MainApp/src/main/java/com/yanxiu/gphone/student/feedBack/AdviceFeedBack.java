package com.yanxiu.gphone.student.feedBack;

import android.content.Context;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.requestTask.RequestUpLoadFeedBackTask;
import com.yanxiu.gphone.student.utils.Util;

/**
 * Created by Administrator on 2015/11/13.
 */
public class AdviceFeedBack extends  AbstractFeedBack{

    private  RequestUpLoadFeedBackTask mTask;
    public AdviceFeedBack(Context context){
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
        if(checkDataParams(content)){
            if(asyncCallBack!=null){
                asyncCallBack.preExecute();
            }
            cancelTask();
            mTask=new RequestUpLoadFeedBackTask(mContext, content,this);
            mTask.start();
        }

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

    }

    @Override
    protected void handlerMinTips() {
        Util.showToast(mContext.getResources().getString(R.string.feedback_content_few));
    }

    @Override
    protected void handlerMaxTips() {
        Util.showToast(mContext.getResources().getString(R.string.feedback_conetnt_exceed));
    }

    @Override
    protected int getMaxNums() {
        return MAX_NUMBERS;
    }

    @Override
    protected int getMinNums() {
        return MIN_NUMBERS;
    }


}
