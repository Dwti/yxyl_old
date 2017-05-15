package com.yanxiu.gphone.studentold.bean.request;

import com.yanxiu.gphone.studentold.httpApi.ExerciseRequestBase;
import com.yanxiu.gphone.studentold.httpApi.YanxiuHttpApi;

/**
 * Created by sunpeng on 2017/4/11.
 */

public class BindNewMobileRequest extends ExerciseRequestBase {
    private String token;
    private String newMobile; //新手机号
    private String code;   // 验证码
    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlServer() {
        return YanxiuHttpApi.getPublicUrl();
    }

    @Override
    protected String urlPath() {
        return "/user/bindNewMobile.do";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewMobile() {
        return newMobile;
    }

    public void setNewMobile(String newMobile) {
        this.newMobile = newMobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
