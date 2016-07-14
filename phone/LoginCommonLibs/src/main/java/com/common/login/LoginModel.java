package com.common.login;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.common.login.constants.LoginConstants;
import com.common.login.eventbusbean.TokenInviateBean;
import com.common.login.inter.LoginCommonInter;
import com.common.login.inter.LoginParentMgr;
import com.common.login.inter.LoginStudentMgr;
import com.common.login.model.LocalCacheBean;
import com.common.login.model.UserInfo;
import com.common.login.model.UserInfoBean;
import com.common.login.params.LoginStatus;
import com.common.login.parser.LoginParser;
import com.common.login.parser.ParentLoginParser;
import com.yanxiu.basecore.bean.YanxiuBaseBean;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/5/20.
 */
public class LoginModel {

    private static LoginCommonInter mCurInter;

    private static final String TAG=LoginModel.class.getSimpleName();

    private static LoginStatus loginStatus=LoginStatus.LOGIN_STATUS_NOT_INIT;

    private static int mCurPoleID=-1;

    static {
        loginStatus=LoginStatus.LOGIN_STATUS_NOT_INIT;
        LogInfo.log(TAG, "LoginModel static");
        LocalCacheBean cacheData = getCacheData();
        if(cacheData!=null&&!StringUtils.isEmpty(cacheData.getCacheData())){
            LogInfo.log(TAG, "init LoginModel cache success");
            initLocalCache(cacheData);
        }else{
            LogInfo.log(TAG, "init LoginModel cache failture");
            loginStatus=LoginStatus.LOGIN_STATUS_NOT_INIT;
            mCurPoleID=-1;
        }
    }

    private static void initLocalCache(LocalCacheBean cacheData){
        switch (cacheData.getPoleId()){
            case LoginConstants.ROLE_STUDENT:
                try {
                    LogInfo.log(TAG, "LoginModel cacheData student");
                    mCurInter=initLoginParams(LoginConstants.ROLE_STUDENT);
                    YanxiuBaseBean loginBean = new LoginParser().initialParse(cacheData.getCacheData());
                    mCurInter.loginIn(loginBean);
                    loginStatus=LoginStatus.LOGIN_STATUS_LOGIN;
                } catch (Exception e) {
                    LogInfo.log(TAG, e.toString());
                    loginStatus=LoginStatus.LOGIN_STATUS_NOT_INIT;
                    mCurPoleID=-1;
                }
                break;
            case LoginConstants.ROLE_PARENT:
                try {
                    LogInfo.log(TAG, "LoginModel cacheData parent");
                    mCurInter=initLoginParams(LoginConstants.ROLE_PARENT);
                    YanxiuBaseBean loginBean = new ParentLoginParser().initialParse(cacheData.getCacheData());
                    mCurInter.loginIn(loginBean);
                    loginStatus=LoginStatus.LOGIN_STATUS_LOGIN;
                }catch (Exception e){
                    LogInfo.log(TAG, e.toString());
                    loginStatus=LoginStatus.LOGIN_STATUS_NOT_INIT;
                    mCurPoleID=-1;
                }
                break;
        }
    }

    private static LoginCommonInter initLoginParams(int poleId){
        LoginCommonInter mCurInter=null;
        switch (poleId){
            case LoginConstants.ROLE_STUDENT:
                LogInfo.log(TAG,"LoginConstants.ROLE_STUDENT");
                mCurPoleID=LoginConstants.ROLE_STUDENT;
                mCurInter=new LoginStudentMgr();
                break;
            case LoginConstants.ROLE_PARENT:
                mCurPoleID=LoginConstants.ROLE_PARENT;
                mCurInter=new LoginParentMgr();
                break;
        }
        return  mCurInter;
    }

    public static LocalCacheBean getCacheData() {

        LocalCacheBean localCacheBean = LocalCacheBean.findDataById(LoginConstants.CACHE_ID);
        if (localCacheBean != null) {
            LogInfo.log(TAG, "UserLoginBean getCacheSuccess");
            return localCacheBean;
        }
        return null;
    }

    public synchronized  static void savaCacheData() {
        if(!isLoginIn()){
            return ;
        }
        String data= JSON.toJSONString(getRoleLoginBean());
        if(!TextUtils.isEmpty(data)&&mCurPoleID!=-1) {
            try {
                LocalCacheBean localCacheBean = new LocalCacheBean();
                localCacheBean.setCacheId(LoginConstants.CACHE_ID);
                localCacheBean.setCacheData(data);
                localCacheBean.setPoleId(mCurPoleID);
                localCacheBean.setCacheTime(System.currentTimeMillis());
                LocalCacheBean.saveData(localCacheBean);
                switch (mCurPoleID){
                    case LoginConstants.ROLE_STUDENT:
                        if(mCurInter!=null){
                            mCurInter.loginIn(new LoginParser().initialParse(data));
                        }
                        break;
                    case LoginConstants.ROLE_PARENT:
                        if(mCurInter!=null){
                            mCurInter.loginIn(new ParentLoginParser().initialParse(data));
                        }
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    private static String mobile;

    private static String password;

    public static String getMobile(){
        return mobile;
    }

    public static void setMobile(String mob){
        mobile = mob;
    }

    public static String getPassword(){
        return password;
    }

    public static void setPassword(String pwd){
        password = pwd;
    }

    public static int getUid() {
        return mCurInter!=null?mCurInter.getUid():0;
    }

    public static String getToken() {
        if(!isLoginIn()){
            return null;
        }
        return mCurInter!=null?mCurInter.getToken():null;
    }


    public static void setToken(String token) {
        if(!isLoginIn()){
            return ;
        }
        if(mCurInter!=null){
            mCurInter.setToken(token);
        }
    }

    /**
     * 适用用于学生登录
     * @return
     */
    @Deprecated
    public static UserInfoBean getLoginBean() {
        if(!isLoginIn()){
            return null;
        }
        if(mCurPoleID==LoginConstants.ROLE_STUDENT){
            return mCurInter!=null? (UserInfoBean) mCurInter.getLoginBean() :null;
        }
        return null;
    }
    /**
     * 适用用于学生登录
     * @return
     */
    @Deprecated
    public static UserInfo getUserinfoEntity(){
        if(!isLoginIn()){
            return null;
        }
        if(mCurPoleID==LoginConstants.ROLE_STUDENT){
            return mCurInter!=null? (UserInfo) mCurInter.getLoginBeanChildData() :null;
        }
        return null;
    }

    public static YanxiuBaseBean getRoleLoginBean(){
        if(!isLoginIn()){
            return null;
        }
        return mCurInter!=null?  mCurInter.getLoginBean() :null;
    }

    public static YanxiuBaseBean getRoleUserInfoEntity(){
        if(!isLoginIn()){
            return null;
        }
        return mCurInter!=null?mCurInter.getLoginBeanChildData() :null;
    }


    public synchronized static void loginIn(int poleId,YanxiuBaseBean loginBean){
        loginStatus=LoginStatus.LOGIN_STATUS_LOGIN;
        mCurInter=initLoginParams(poleId);
        if(mCurInter!=null){
            mCurInter.loginIn(loginBean);
            savaCacheData();
        }
    }

    private  static boolean isLoginIn(){
        return loginStatus != null && loginStatus == LoginStatus.LOGIN_STATUS_LOGIN;
    }

    public synchronized static void loginOut(){
        loginStatus=LoginStatus.LOGIN_STATUS_LOGOUT;
        //MainActivity进行接收
        EventBus.getDefault().post(new TokenInviateBean());
        if(mCurInter!=null){
            mCurInter.loginOut();
        }
        LocalCacheBean.deleteAllData();
    }





}
