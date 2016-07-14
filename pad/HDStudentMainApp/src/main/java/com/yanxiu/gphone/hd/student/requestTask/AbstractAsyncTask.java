package com.yanxiu.gphone.hd.student.requestTask;

import android.content.Context;

import com.common.core.utils.StringUtils;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.impl.YanxiuHttpAsyncTask;
import com.yanxiu.gphone.hd.student.manager.ActivityManager;
import com.yanxiu.gphone.hd.student.utils.Util;

/**
 * Created by Administrator on 2015/6/3.
 */
public abstract class AbstractAsyncTask<T extends YanxiuBaseBean> extends
        YanxiuHttpAsyncTask<T> {

    public AbstractAsyncTask(Context context) {
        super(context);
    }

    @Override public void tokenInvalidate(String msg) {
        if(!ActivityManager.isExistLoginActivity()){
            if(!StringUtils.isEmpty(msg)){
                Util.showUserToast(msg, null, null);
            }
            LoginModel.loginOut();
        }
    }
}
