package com.yanxiu.gphone.hd.student.jump;

/**
 * startForResult 方式跳转统一继承此类
 */
public class BaseJumModelForResult extends BaseJumpModel {
    private int requestCode;

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }
}
