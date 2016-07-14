package com.common.login.inter;

import com.common.core.utils.LogInfo;
import com.common.login.model.ParentInfoBean;
import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by libo on 16/3/14.
 */
public class LoginParentMgr implements LoginCommonInter{
    private final static String TAG=LoginParentMgr.class.getSimpleName();
    private ParentInfoBean loginBean=new ParentInfoBean();

    @Override
    public void loginIn(YanxiuBaseBean dloginBean) {
        loginBean= (ParentInfoBean) dloginBean;
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
        if(loginBean.getProperty()!=null){
            return loginBean.getProperty().getUser();
        }else{
            return null;
        }

    }

    @Override
    public int getUid() {
        if(loginBean!=null && loginBean.getProperty()!=null&&loginBean.getProperty().getUser()!=null){
            LogInfo.log(TAG, "LoginModel getUid is not null");
            return loginBean.getProperty().getUser().getId();
        }else{
            LogInfo.log(TAG, "LoginModel getUid is 0");
            return 0;
        }
    }

    @Override
    public void setToken(String token) {
        if(loginBean!=null && loginBean.getProperty()!=null&&loginBean.getProperty().getPassport()!=null){
            loginBean.getProperty().getPassport().setToken(token);
        }
    }

    @Override
    public String getToken() {
        if(loginBean!=null && loginBean.getProperty()!=null&&loginBean.getProperty().getPassport()!=null){
            return loginBean.getProperty().getPassport().getToken();
        }else{
            return "";
        }

    }
}
