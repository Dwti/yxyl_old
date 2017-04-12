package com.yanxiu.gphone.student.bean.request;

import com.yanxiu.gphone.student.httpApi.ExerciseRequestBase;
import com.yanxiu.gphone.student.httpApi.YanxiuHttpApi;

/**
 * Created by sunpeng on 2017/4/11.
 */

public class CheckMobileRequest extends ExerciseRequestBase {
    private String token;
    private String mobile; //新手机号
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
        return "/user/checkMobileMsgCode.do";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
