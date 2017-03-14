package com.yanxiu.gphone.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by Administrator on 2015/7/17.
 */
public class PaperTestEntity implements YanxiuBaseBean {
    public static final int UN_FAVORITE = 0;
    public static final int FAVORITE = 1;
    private QuestionEntity questions;
    private boolean isHomeWorkFinished;
    private int pid;
    //    "isfavorite" : 0,      // 0标示为收藏， 1 标示已经收藏 是否收藏;
    private int isfavorite;
    private int id;
    private int sectionid;
    private int qid;
    private int status;
    private String wqid;
    private int wqnumber;
    private int redostatus;

    private ExtendEntity extend;

    public int getWqnumber() {
        return wqnumber;
    }

    public void setWqnumber(int wqnumber) {
        this.wqnumber = wqnumber;
    }

    public int getRedostatus() {
        return redostatus;
    }

    public void setRedostatus(int redostatus) {
        this.redostatus = redostatus;
    }

    public boolean isHomeWorkFinished() {
        return isHomeWorkFinished;
    }

    public void setHomeWorkFinished(boolean homeWorkFinished) {
        isHomeWorkFinished = homeWorkFinished;
    }

    public String getWqid() {
        return wqid;
    }

    public void setWqid(String wqid) {
        this.wqid = wqid;
    }

    public ExtendEntity getExtend() {
        return extend;
    }

    public void setExtend(ExtendEntity extend) {
        this.extend = extend;
    }

    public void setQuestions(QuestionEntity questions) {
        this.questions = questions;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSectionid(int sectionid) {
        this.sectionid = sectionid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public QuestionEntity getQuestions() {
        return questions;
    }

    public int getPid() {
        return pid;
    }

    public int getId() {
        return id;
    }

    public int getSectionid() {
        return sectionid;
    }

    public int getQid() {
        return qid;
    }

    public int getStatus() {
        return status;
    }

    public int getIsfavorite() {
        return isfavorite;
    }

    public void setIsfavorite(int isfavorite) {
        this.isfavorite = isfavorite;
    }

    @Override
    public String toString() {
        return "PaperTestEntity{" +
                ", isfavorite====================" + isfavorite +
                '}';
    }


}
