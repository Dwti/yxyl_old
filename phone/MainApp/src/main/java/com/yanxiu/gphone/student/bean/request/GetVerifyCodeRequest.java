package com.yanxiu.gphone.student.bean.request;

import com.yanxiu.gphone.student.httpApi.ExerciseRequestBase;
import com.yanxiu.gphone.student.httpApi.YanxiuHttpApi;

/**
 * Created by sunpeng on 2017/4/12.
 */

public class GetVerifyCodeRequest extends ExerciseRequestBase {
    private String mobile;
    private String type;   //0 新手机号获取验证码 ； 1 旧手机号获取验证码
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
        return "/user/produceCodeByBind.do";
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
