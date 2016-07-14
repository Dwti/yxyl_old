package com.common.login.model;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by lee on 16-3-28.
 */
public class ParentBindInfo implements YanxiuBaseBean {
    private int id;
    private int parentid;
    private int uid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
