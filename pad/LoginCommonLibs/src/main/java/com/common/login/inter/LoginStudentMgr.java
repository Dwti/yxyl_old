package com.common.login.inter;

import com.common.core.utils.LogInfo;
import com.common.login.model.PasswordBean;
import com.common.login.model.UserInfoBean;
import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by libo on 16/3/14.
 */
public class LoginStudentMgr implements LoginCommonInter {
    /**
     * 此类不仅仅是一个bean类，可以处理所有登录模块的事情
     * */
    private final static String TAG=LoginStudentMgr.class.getSimpleName();
    private static UserInfoBean loginBean = new UserInfoBean();


    @Override
    public void loginIn(YanxiuBaseBean dloginBean) {
        loginBean= (UserInfoBean) dloginBean;
    }

    @Override
    public void loginOut() {
        loginBean=null;
    }

    @Override
    public YanxiuBaseBean getLoginBean() {
        return loginBean;
    }

    @Override
    public YanxiuBaseBean getLoginBeanChildData() {
        if(loginBean!=null && loginBean.getData() !=null){
            return loginBean.getData().get(0);
        }else{
            return null;
        }
    }

    @Override
    public int getUid() {
        if(loginBean!=null && loginBean.getData()!=null){
            LogInfo.log(TAG, "LoginModel getUid is not null");
            return loginBean.getData().get(0).getId();
        }else{
            LogInfo.log(TAG, "LoginModel getUid is 0");
            return 0;
        }
    }

    @Override
    public void setToken(String token) {
        if(loginBean.getData().get(0).getPassport()==null){
            PasswordBean passwordBean = new PasswordBean();
            passwordBean.setToken(token);
            loginBean.getData().get(0).setPassport(passwordBean);
        }else {
            loginBean.getData().get(0).getPassport().setToken(token);
        }
    }

    @Override
    public String getToken() {
        if(loginBean.getData().get(0).getPassport()!=null){
            return loginBean.getData().get(0).getPassport().getToken();
        }
        return null;
    }
}
