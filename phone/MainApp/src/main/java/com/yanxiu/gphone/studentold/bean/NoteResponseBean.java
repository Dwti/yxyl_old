package com.yanxiu.gphone.studentold.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by sunpeng on 2017/3/13.
 */

public class NoteResponseBean implements YanxiuBaseBean{
    private StatusBean status;

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }
}
