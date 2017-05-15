package com.yanxiu.gphone.studentold.bean;

/**
 * Created by Administrator on 2015/7/20.
 */
public class GroupHwUnBean extends SrtBaseBean{

    /**
     * name : 测试题2015卷
     * quesnum : 4
     * overTime : 06月03日12:00
     * id : 23
     * isEnd : 0
     * subjectid : 34
     * status : 0
     */
    private String name;
    private int quesnum;
    private String overTime;
    private int id;
    private int isEnd;
    private int subjectid;
    private int status;
    private PaperStatusBean paperStatus;
    private SimpleGroup group;

    public void setName(String name) {
        this.name = name;
    }

    public void setQuesnum(int quesnum) {
        this.quesnum = quesnum;
    }

    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIsEnd(int isEnd) {
        this.isEnd = isEnd;
    }

    public void setSubjectid(int subjectid) {
        this.subjectid = subjectid;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public int getQuesnum() {
        return quesnum;
    }

    public String getOverTime() {
        return overTime;
    }

    public int getId() {
        return id;
    }

    public int getIsEnd() {
        return isEnd;
    }

    public int getSubjectid() {
        return subjectid;
    }

    public int getStatus() {
        return status;
    }

    public PaperStatusBean getPaperStatus() {
        return paperStatus;
    }

    public void setPaperStatus(PaperStatusBean paperStatus) {
        this.paperStatus = paperStatus;
    }

    public SimpleGroup getGroup() {
        return group;
    }

    public void setGroup(SimpleGroup group) {
        this.group = group;
    }
}
