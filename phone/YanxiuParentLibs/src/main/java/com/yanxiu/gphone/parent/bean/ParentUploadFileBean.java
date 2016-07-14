package com.yanxiu.gphone.parent.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.List;

/**
 * Created by Administrator on 2015/7/17.
 */
public class ParentUploadFileBean implements YanxiuBaseBean {


    /**
     * desc : 操作成功
     * code : 0
     */

    private StatusBean status;
    /**
     * id : 17
     * mobile : null
     * realname : null
     * provinceid : null
     * provinceidName : null
     * cityid : null
     * cityidName : null
     * areaid : null
     * areaidName : null
     * role : null
     * roleName : null
     * createtime : null
     * head : http://scc.jsyxw.cn/avatar/images/2016/0331/file_56fcdb0716c2a.jpeg
     * child : null
     */

    private List<DataBean> data;

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class StatusBean {
        private String desc;
        private int code;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

    public static class DataBean {
        private int id;
        private Object mobile;
        private Object realname;
        private Object provinceid;
        private Object provinceidName;
        private Object cityid;
        private Object cityidName;
        private Object areaid;
        private Object areaidName;
        private Object role;
        private Object roleName;
        private Object createtime;
        private String head;
        private Object child;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getMobile() {
            return mobile;
        }

        public void setMobile(Object mobile) {
            this.mobile = mobile;
        }

        public Object getRealname() {
            return realname;
        }

        public void setRealname(Object realname) {
            this.realname = realname;
        }

        public Object getProvinceid() {
            return provinceid;
        }

        public void setProvinceid(Object provinceid) {
            this.provinceid = provinceid;
        }

        public Object getProvinceidName() {
            return provinceidName;
        }

        public void setProvinceidName(Object provinceidName) {
            this.provinceidName = provinceidName;
        }

        public Object getCityid() {
            return cityid;
        }

        public void setCityid(Object cityid) {
            this.cityid = cityid;
        }

        public Object getCityidName() {
            return cityidName;
        }

        public void setCityidName(Object cityidName) {
            this.cityidName = cityidName;
        }

        public Object getAreaid() {
            return areaid;
        }

        public void setAreaid(Object areaid) {
            this.areaid = areaid;
        }

        public Object getAreaidName() {
            return areaidName;
        }

        public void setAreaidName(Object areaidName) {
            this.areaidName = areaidName;
        }

        public Object getRole() {
            return role;
        }

        public void setRole(Object role) {
            this.role = role;
        }

        public Object getRoleName() {
            return roleName;
        }

        public void setRoleName(Object roleName) {
            this.roleName = roleName;
        }

        public Object getCreatetime() {
            return createtime;
        }

        public void setCreatetime(Object createtime) {
            this.createtime = createtime;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public Object getChild() {
            return child;
        }

        public void setChild(Object child) {
            this.child = child;
        }
    }
}
