package com.yanxiu.gphone.hd.student.utils.statistics.requestAsycn;

import android.content.Context;

import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.impl.YanxiuHttpAsyncTask;

/**
 * Created by frc on 16-6-2.
 */
public abstract class AbstractAsyncTask<T extends YanxiuBaseBean> extends YanxiuHttpAsyncTask<T> {
    public AbstractAsyncTask(Context context) {
        super(context);
    }
}
