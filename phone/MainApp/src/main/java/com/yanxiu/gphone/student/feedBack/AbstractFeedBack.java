package com.yanxiu.gphone.student.feedBack;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.student.inter.AsyncCallBack;

/**
 * Created by Administrator on 2015/11/13.
 */
public abstract class AbstractFeedBack<T> implements TextWatcher,AsyncCallBack {
    // 意见反馈
    public static final int ADVICE_FEED_BAck=0;
    //题目报错反馈
    public static final int ERROR_FEED_BACK=1;

    protected TextWatcherListener textWatcherListener;

    protected AsyncCallBack asyncCallBack;



    public interface  TextWatcherListener{

        void beforeTextChanged(CharSequence s, int start, int count, int after);

         void onTextChanged(CharSequence s, int start, int before, int count);

         void afterTextChanged(Editable s) ;
    }

    public interface AsyncCallBack{

        void preExecute();

         void updateResult(YanxiuBaseBean result);

         void dataResultError(int type, String msg);
    }


    protected Context mContext;
    public final static int MIN_NUMBERS=4;
    public final static int MAX_NUMBERS=500;

    protected boolean checkDataParams(String content){
        LogInfo.log("lee", "length: " + content.length());
        if(StringUtils.isEmpty(content)||content.length()<getMinNums()){
            handlerMinTips();
            return false;
        }else if(content.length()>getMaxNums()){
            handlerMaxTips();
            return false;
        }else {
            return true;
        }
    }




    public abstract void setTextWatcherListener(TextWatcherListener textWatcherListener);

    public abstract void setAsyncCallBack(AsyncCallBack asyncCallBack);

    public abstract void startTask(String content);

    public abstract void cancelTask();

    public abstract void  setParams(T t);



    protected abstract void handlerMinTips();

    protected abstract void handlerMaxTips();

    protected abstract int getMaxNums();

    protected abstract int getMinNums();


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if(textWatcherListener==null){
            return;
        }
        textWatcherListener.beforeTextChanged(s,start,count,after);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(textWatcherListener==null){
            return;
        }
        textWatcherListener.onTextChanged(s,start,before,count);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(textWatcherListener==null){
            return ;
        }
        textWatcherListener.afterTextChanged(s);
    }

    @Override
    public void update(YanxiuBaseBean result) {
        if( asyncCallBack==null){
            return;
        }
        asyncCallBack.updateResult(result);
    }

    @Override
    public void dataError(int type, String msg) {
        if(asyncCallBack==null){
            return;
        }
        asyncCallBack.dataResultError(type, msg);
    }
}
