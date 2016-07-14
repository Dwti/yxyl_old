package com.yanxiu.gphone.hd.student.eventbusbean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by Administrator on 2016/2/1.
 */
public class UserNameEditBean implements YanxiuBaseBean {
    private int type;
    private String editMsg;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getEditMsg() {
        return editMsg;
    }

    public void setEditMsg(String editMsg) {
        this.editMsg = editMsg;
    }
}
