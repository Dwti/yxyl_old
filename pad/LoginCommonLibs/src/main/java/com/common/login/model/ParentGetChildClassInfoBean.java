package com.common.login.model;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by lee on 16-3-29.
 */
public class ParentGetChildClassInfoBean implements YanxiuBaseBean {
    private String id;
    private String name;
    private String schoolname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }
}
