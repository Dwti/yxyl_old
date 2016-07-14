package com.yanxiu.gphone.parent.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by lee on 16-3-31.
 */
public class ParentSendMsgBean implements YanxiuBaseBean {
    private ParentDataStatusEntityBean status;

    public ParentDataStatusEntityBean getStatus() {
        return status;
    }

    public void setStatus(ParentDataStatusEntityBean status) {
        this.status = status;
    }
}
