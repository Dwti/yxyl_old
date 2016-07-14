package com.yanxiu.gphone.parent.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by lee on 16-3-29.
 */
public class ParentGetChildInfoBean implements YanxiuBaseBean {
    private ParentGetChildPropertyBean property;
    private ParentDataStatusEntityBean status;

    public ParentGetChildPropertyBean getProperty() {
        return property;
    }

    public void setProperty(ParentGetChildPropertyBean property) {
        this.property = property;
    }

    public ParentDataStatusEntityBean getStatus() {
        return status;
    }

    public void setStatus(ParentDataStatusEntityBean status) {
        this.status = status;
    }
}
