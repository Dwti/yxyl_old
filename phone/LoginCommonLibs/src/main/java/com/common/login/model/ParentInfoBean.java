package com.common.login.model;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by lee on 16-3-28.
 */
public class ParentInfoBean  extends SrtBaseBean implements YanxiuBaseBean {
    private DataStatusEntityBean status;
    private ParentProperty property;
    public DataStatusEntityBean getStatus() {
        return status;
    }

    public void setStatus(DataStatusEntityBean status) {
        this.status = status;
    }

    public ParentProperty getProperty() {
        return property;
    }

    public void setProperty(ParentProperty property) {
        this.property = property;
    }
}
