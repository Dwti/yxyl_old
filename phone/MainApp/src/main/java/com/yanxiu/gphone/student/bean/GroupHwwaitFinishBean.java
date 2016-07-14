package com.yanxiu.gphone.student.bean;

/**
 * Created by Administrator on 2015/7/20.
 */
public class GroupHwwaitFinishBean extends SrtBaseBean{
    private DataStatusEntityBean status;
    private PublicPropertyBean property;

    public DataStatusEntityBean getStatus () {
        return status;
    }

    public void setStatus (DataStatusEntityBean status) {
        this.status = status;
    }

    public PublicPropertyBean getProperty () {
        return property;
    }

    public void setProperty (PublicPropertyBean property) {
        this.property = property;
    }
}
