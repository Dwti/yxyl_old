package com.yanxiu.gphone.studentold.requestTask;

import android.content.Context;

import com.common.core.utils.StringUtils;
import com.common.login.LoginModel;
import com.igexin.sdk.PushManager;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.impl.YanxiuHttpAsyncTask;
import com.yanxiu.gphone.studentold.manager.ActivityManager;
import com.yanxiu.gphone.studentold.utils.Util;

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
            PushManager.getInstance().unBindAlias(context.getApplicationContext(), String.valueOf(LoginModel.getUid()), true);
            LoginModel.loginOut();

        }
    }

}
