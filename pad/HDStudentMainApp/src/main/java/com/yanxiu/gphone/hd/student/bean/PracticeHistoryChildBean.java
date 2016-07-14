package com.yanxiu.gphone.hd.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by Administrator on 2015/7/17.
 */
public class PracticeHistoryChildBean implements YanxiuBaseBean {

    /**
     * volume : 0
     * buildTime : 2015-6-3 23:00
     * chapterId : 12
     * name : 第一章 微分求小明心里阴影面积
     * beditionId : 0
     * questionNum : 5
     * correctNum : 0
     * paperId : 33
     * subjectId : 0
     * stageId : 0
     * status : 0
     */
    private int volume;
    private String buildTime;
    private int chapterId;
    private String name;
    private int beditionId;
    private int questionNum;
    private int correctNum;
    private int paperId;
    private int subjectId;
    private int stageId;
    private int status;

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBeditionId(int beditionId) {
        this.beditionId = beditionId;
    }

    public void setQuestionNum(int questionNum) {
        this.questionNum = questionNum;
    }

    public void setCorrectNum(int correctNum) {
        this.correctNum = correctNum;
    }

    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getVolume() {
        return volume;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public int getChapterId() {
        return chapterId;
    }

    public String getName() {
        return name;
    }

    public int getBeditionId() {
        return beditionId;
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public int getCorrectNum() {
        return correctNum;
    }

    public int getPaperId() {
        return paperId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getStageId() {
        return stageId;
    }

    public int getStatus() {
        return status;
    }
}
