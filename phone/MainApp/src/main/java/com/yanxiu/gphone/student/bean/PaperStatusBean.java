package com.yanxiu.gphone.student.bean;

/**
 * Created by Administrator on 2015/7/20.
 */
public class PaperStatusBean extends SrtBaseBean{

    /**
     * teacherName : 张三
     * tid : 3
     * teachercomments : 做的很好
     * status : 0
     */
    private String teacherName;
    private int tid;
    private String teachercomments;
    private int status;
    /**
     * uid : 28
     * costtime : 4151
     * endtime : 1438313228000
     * begintime : 1438313224000
     * id : 78
     * tid : 0
     * ppid : 116
     * status : 1
     */
    private int uid;
    private int costtime;
    private long endtime;
    private long begintime;
    private int id;
    private int ppid;

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public void setTeachercomments(String teachercomments) {
        this.teachercomments = teachercomments;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public int getTid() {
        return tid;
    }

    public String getTeachercomments() {
        return teachercomments;
    }

    public int getStatus() {
        return status;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setCosttime(int costtime) {
        this.costtime = costtime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }

    public void setBegintime(long begintime) {
        this.begintime = begintime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPpid(int ppid) {
        this.ppid = ppid;
    }

    public int getUid() {
        return uid;
    }

    public int getCosttime() {
        return costtime;
    }

    public long getEndtime() {
        return endtime;
    }

    public long getBegintime() {
        return begintime;
    }

    public int getId() {
        return id;
    }

    public int getPpid() {
        return ppid;
    }

    @Override
    public String toString() {
        return "PaperStatusBean{" +
                "teacherName='" + teacherName + '\'' +
                ", tid=" + tid +
                ", teachercomments='" + teachercomments + '\'' +
                ", status=" + status +
                ", uid=" + uid +
                ", costtime=" + costtime +
                ", endtime=" + endtime +
                ", begintime=" + begintime +
                ", id=" + id +
                ", ppid=" + ppid +
                '}';
    }
}
