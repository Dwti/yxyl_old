package com.yanxiu.gphone.studentold.bean;

/**
 * Created by Administrator on 2015/7/20.
 */
public class GroupDetailBean extends SrtBaseBean{

    /**
     * subjectname : SubjectName
     * stagename : TheStageName
     * gradeid : 123
     * authorid : 123
     * stdnum : 23
     * subjectid : 23445
     * gradename : TheGradeName
     * buildtime : 1437131060059
     * bedition : 23
     * authorname : theTeachName
     * name : TheGroupName
     * beditionname : TheEditionName
     * id : 45678
     * stageid : 4456
     * status : 0
     * isMemeberFull：0    人员是否已满 0 表示未满， 1表示已满
     */
    private String subjectname;
    private String stagename;
    private int gradeid;
    private int authorid;
    private int stdnum;
    private int subjectid;
    private String gradename;
    private long buildtime;
    private int bedition;
    private String authorname;
    private String name;
    private String beditionname;
    private int id;
    private int stageid;
    private int status;
    private int isMemeberFull;

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public void setStagename(String stagename) {
        this.stagename = stagename;
    }

    public void setGradeid(int gradeid) {
        this.gradeid = gradeid;
    }

    public void setAuthorid(int authorid) {
        this.authorid = authorid;
    }

    public void setStdnum(int stdnum) {
        this.stdnum = stdnum;
    }

    public void setSubjectid(int subjectid) {
        this.subjectid = subjectid;
    }

    public void setGradename(String gradename) {
        this.gradename = gradename;
    }

    public void setBuildtime(long buildtime) {
        this.buildtime = buildtime;
    }

    public void setBedition(int bedition) {
        this.bedition = bedition;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBeditionname(String beditionname) {
        this.beditionname = beditionname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStageid(int stageid) {
        this.stageid = stageid;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public String getStagename() {
        return stagename;
    }

    public int getGradeid() {
        return gradeid;
    }

    public int getAuthorid() {
        return authorid;
    }

    public int getStdnum() {
        return stdnum;
    }

    public int getSubjectid() {
        return subjectid;
    }

    public String getGradename() {
        return gradename;
    }

    public long getBuildtime() {
        return buildtime;
    }

    public int getBedition() {
        return bedition;
    }

    public String getAuthorname() {
        return authorname;
    }

    public String getName() {
        return name;
    }

    public String getBeditionname() {
        return beditionname;
    }

    public int getId() {
        return id;
    }

    public int getStageid() {
        return stageid;
    }

    public int getStatus() {
        return status;
    }

    public int getIsMemeberFull() {
        return isMemeberFull;
    }

    public void setIsMemeberFull(int isMemeberFull) {
        this.isMemeberFull = isMemeberFull;
    }
}
