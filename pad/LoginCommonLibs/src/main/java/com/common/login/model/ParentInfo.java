package com.common.login.model;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by lee on 16-3-28.
 */
public class ParentInfo implements YanxiuBaseBean {
    private String areaid;
    private String areaidName;
    private String cityid;
    private String cityidName;
    private String createtime;
    private String head;
    private int id;  //家长id
    private String mobile;
    private String provinceid;
    private String provinceidName;
    private String realname;
    private String role;
    private String roleName;
    private UserInfo child;


    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public String getAreaidName() {
        return areaidName;
    }

    public void setAreaidName(String areaidName) {
        this.areaidName = areaidName;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getCityidName() {
        return cityidName;
    }

    public void setCityidName(String cityidName) {
        this.cityidName = cityidName;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    public String getProvinceidName() {
        return provinceidName;
    }

    public void setProvinceidName(String provinceidName) {
        this.provinceidName = provinceidName;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public UserInfo getChild() {
        return child;
    }

    public void setChild(UserInfo child) {
        this.child = child;
    }
}
