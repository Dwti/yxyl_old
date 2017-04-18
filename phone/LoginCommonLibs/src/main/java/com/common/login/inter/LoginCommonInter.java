package com.common.login.inter;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by libo on 16/3/14.
 */
public interface LoginCommonInter {
     void loginIn(YanxiuBaseBean loginBean);

     void loginOut();

     YanxiuBaseBean getLoginBean();

     YanxiuBaseBean getLoginBeanChildData();

     int getUid();

     void setToken(String token);

     String getToken();

     void setBindMobile(String mobile);

     String getBindMobile();

     String getLoginType();

     String getLoginName();

}
