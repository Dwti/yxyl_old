package com.yanxiu.gphone.parent.jump;

/**
 * Created by lee on 16-3-22.
 */
public class ParentSetPsdJumpModel extends BaseJumpModel {
    private int from;
    private String mobile;
    private String verifyCode;
    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
