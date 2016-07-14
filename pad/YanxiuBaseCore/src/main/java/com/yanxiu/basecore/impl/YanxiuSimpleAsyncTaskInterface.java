package com.yanxiu.basecore.impl;

/**
 * 异步任务接口
 */
public interface YanxiuSimpleAsyncTaskInterface<D> {

    /**
     * 异步任务开始前
     */
    public boolean onPreExecute();

    /**
     * 异步任务执行
     */
    public D doInBackground();

    /**
     * 异步任务完成
     */
    public void onPostExecute(D result);

}
