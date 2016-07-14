package com.common.login.model;

/**
 * Created by Administrator on 2015/7/17.
 */
public class UserInfo extends SrtBaseBean{

    /**
     * createtime : null
     * sex : null
     * mobile : 18642802671
     * cityid : 234
     * provinceid : 1234
     * realname : realname
     * head : http://img.baidu.com
     * stageName : 湛河
     * areaid : 4521
     * cityName : fuyang
     * areaName : yingdong
     * schoolid : 10023
     * nickname : nickName
     * id : 8
     * provinceName : anhui
     * schoolName : FuyangNo.1 School
     * stageid : 3455
     * status : null
     */
    private String createtime;
    private String sex;
    private String mobile;
    private String cityid;
    private String provinceid;
    private String realname;
    private String head;
    private String stageName;
    private String areaid;
    private String cityName;
    private String areaName;
    private String schoolid;
    private String nickname;
    private int id;
    private String provinceName;
    private String schoolName;
    private int stageid;
    private String status;

    private PasswordBean passport;







    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public void setSchoolid(String schoolid) {
        this.schoolid = schoolid;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public void setStageid(int stageid) {
        this.stageid = stageid;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatetime() {
        return createtime;
    }

    public String getSex() {
        return sex;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCityid() {
        return cityid;
    }

    public String getProvinceid() {
        return provinceid;
    }

    public String getRealname() {
        return realname;
    }

    public String getHead() {
        return head;
    }

    public String getStageName() {
        return stageName;
    }

    public String getAreaid() {
        return areaid;
    }

    public String getCityName() {
        return cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public String getSchoolid() {
        return schoolid;
    }

    public String getNickname() {
        return nickname;
    }

    public int getId() {
        return id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public int getStageid() {
        return stageid;
    }

    public String getStatus() {
        return status;
    }

    public PasswordBean getPassport() {
        return passport;
    }

    public void setPassport(PasswordBean passport) {
        this.passport = passport;
    }
}
