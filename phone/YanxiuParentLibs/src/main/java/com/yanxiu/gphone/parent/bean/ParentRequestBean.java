package com.yanxiu.gphone.parent.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by lidongming on 16/3/24.
 * 家长端jsonobject header bean
 */
public class ParentRequestBean implements YanxiuBaseBean {

    /**
     * status : {"code":3,"desc":"加入成功"}
     */
    private ParentDataStatusEntityBean status;

    public void setStatus(ParentDataStatusEntityBean status) {
        this.status = status;
    }

    public ParentDataStatusEntityBean getStatus() {
        return status;
    }
}
