package com.yanxiu.gphone.student.bean;

/**
 * Created by Administrator on 2015/7/20.
 */
public class ClassDetailBean extends SrtBaseBean{

    /**
     * adminName : 长安区
     * periodid : 2015
     * gradeid : 1307
     * schoolid : 23091
     * name : aaaa
     * adminid : 3841885
     * teachernum : 1
     * id : 22
     * stdnum : 1
     * schoolname : 北京市第五十四中学
     * stageid : 1203
     * status : 1
     */
    /**班主任名称*/
    private String adminName;
    private int periodid;
    private int areaid;
    private int cityid;
    private String gradename;
    private int provinceid;
    private int gradeid;
    private int schoolid;
    /**班级名称*/
    private String name;
    private int adminid;
    private int teachernum;
    private int id;
    /**学生人数*/
    private int stdnum;
    /**学校名称*/
    private String schoolname;
    private int stageid;
    private int status;

    public String getGradename() {
        return gradename;
    }

    public void setGradename(String gradename) {
        this.gradename = gradename;
    }

    public int getAreaid() {
        return areaid;
    }

    public void setAreaid(int areaid) {
        this.areaid = areaid;
    }

    public int getCityid() {
        return cityid;
    }

    public void setCityid(int cityid) {
        this.cityid = cityid;
    }

    public int getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(int provinceid) {
        this.provinceid = provinceid;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public void setPeriodid(int periodid) {
        this.periodid = periodid;
    }

    public void setGradeid(int gradeid) {
        this.gradeid = gradeid;
    }

    public void setSchoolid(int schoolid) {
        this.schoolid = schoolid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAdminid(int adminid) {
        this.adminid = adminid;
    }

    public void setTeachernum(int teachernum) {
        this.teachernum = teachernum;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStdnum(int stdnum) {
        this.stdnum = stdnum;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public void setStageid(int stageid) {
        this.stageid = stageid;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAdminName() {
        return adminName;
    }

    public int getPeriodid() {
        return periodid;
    }

    public int getGradeid() {
        return gradeid;
    }

    public int getSchoolid() {
        return schoolid;
    }

    public String getName() {
        return name;
    }

    public int getAdminid() {
        return adminid;
    }

    public int getTeachernum() {
        return teachernum;
    }

    public int getId() {
        return id;
    }

    public int getStdnum() {
        return stdnum;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public int getStageid() {
        return stageid;
    }

    public int getStatus() {
        return status;
    }
}
