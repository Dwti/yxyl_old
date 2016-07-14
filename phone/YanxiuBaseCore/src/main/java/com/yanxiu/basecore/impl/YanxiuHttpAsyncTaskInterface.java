package com.yanxiu.basecore.impl;

import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.bean.YanxiuDataHull;

/**
 * 异步任务接口类（网络任务）
 */
public interface YanxiuHttpAsyncTaskInterface<T extends YanxiuBaseBean> {

    /**
     * 异步任务开始前
     */
    public boolean onPreExecute();

    /**
     * 异步任务执行
     */
    public YanxiuDataHull<T> doInBackground();

    /**
     * 异步任务完成
     */
    public void onPostExecute(int updateId, T result);

}
