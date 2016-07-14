package com.yanxiu.gphone.parent.requestTask;

import android.content.Context;

import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.impl.YanxiuHttpAsyncTask;
import com.yanxiu.gphone.parent.utils.ParentUtils;

/**
 * Created by Administrator on 2015/6/3.
 */
public abstract class AbstractAsyncTask<T extends YanxiuBaseBean> extends
        YanxiuHttpAsyncTask<T> {

    public AbstractAsyncTask(Context context) {
        super(context);
    }

    @Override public void tokenInvalidate(String msg) {
        LogInfo.log("geny", "tokenInvalidate-----------" + msg);
        if(!StringUtils.isEmpty(msg)){
            ParentUtils.showToast(msg);
        }
        LoginModel.loginOut();
    }

}
