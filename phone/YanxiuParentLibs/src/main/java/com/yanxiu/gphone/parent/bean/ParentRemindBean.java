package com.yanxiu.gphone.parent.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by lee on 16-3-31.
 */
public class ParentRemindBean implements YanxiuBaseBean {
    private ParentDataStatusEntityBean status;
    private ParentPublicPropertyBean property;

    public ParentDataStatusEntityBean getStatus() {
        return status;
    }

    public void setStatus(ParentDataStatusEntityBean status) {
        this.status = status;
    }

    public ParentPublicPropertyBean getProperty() {
        return property;
    }

    public void setProperty(ParentPublicPropertyBean property) {
        this.property = property;
    }
}
