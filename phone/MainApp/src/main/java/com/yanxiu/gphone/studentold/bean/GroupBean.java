package com.yanxiu.gphone.studentold.bean;

/**
 * Created by Administrator on 2015/7/20.
 */
public class GroupBean extends SrtBaseBean{

    /**
     * name : 三年级语文二组
     * id : 34
     * status : 0
     */
    private String name;
    private int id;
    private int subjectid;
    private int subScore;
    private PaperBean paper;
    private int waitFinishNum;


    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public PaperBean getPaper() {
        return paper;
    }

    public void setPaper(PaperBean paper) {
        this.paper = paper;
    }

    public int getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(int subjectid) {
        this.subjectid = subjectid;
    }

    public int getSubScore() {
        return subScore;
    }

    public void setSubScore(int subScore) {
        this.subScore = subScore;
    }

    public int getWaitFinishNum () {
        return waitFinishNum;
    }

    public void setWaitFinishNum (int waitFinishNum) {
        this.waitFinishNum = waitFinishNum;
    }
}
