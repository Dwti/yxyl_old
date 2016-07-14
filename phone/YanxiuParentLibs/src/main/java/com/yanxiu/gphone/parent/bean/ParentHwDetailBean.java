package com.yanxiu.gphone.parent.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by hai8108 on 16/3/25.
 */
public class ParentHwDetailBean implements YanxiuBaseBean{

    /**
     * answernum : 50
     * subjectid : 1102
     * correctrate : 0.45
     * increaserate : 0.34
     * classAvgRate : 0.4
     * increaseFlag : 1
     */
    private String subjectname;
    private int answernum;
    private int subjectid;
    private double correctrate;
    private double increaserate;
    private double classAvgRate;
    private int increaseFlag;

    public String getSubjectname () {
        return subjectname;
    }

    public void setSubjectname (String subjectname) {
        this.subjectname = subjectname;
    }

    public int getAnswernum () {
        return answernum;
    }

    public void setAnswernum (int answernum) {
        this.answernum = answernum;
    }

    public int getSubjectid () {
        return subjectid;
    }

    public void setSubjectid (int subjectid) {
        this.subjectid = subjectid;
    }

    public double getCorrectrate () {
        return correctrate;
    }

    public void setCorrectrate (double correctrate) {
        this.correctrate = correctrate;
    }

    public double getIncreaserate () {
        return increaserate;
    }

    public void setIncreaserate (double increaserate) {
        this.increaserate = increaserate;
    }

    public double getClassAvgRate () {
        return classAvgRate;
    }

    public void setClassAvgRate (double classAvgRate) {
        this.classAvgRate = classAvgRate;
    }

    public int getIncreaseFlag () {
        return increaseFlag;
    }

    public void setIncreaseFlag (int increaseFlag) {
        this.increaseFlag = increaseFlag;
    }

    @Override
    public String toString () {
        return "ParentHwDetailBean{" +
                "subjectname='" + subjectname + '\'' +
                ", answernum=" + answernum +
                ", subjectid=" + subjectid +
                ", correctrate=" + correctrate +
                ", increaserate=" + increaserate +
                ", classAvgRate=" + classAvgRate +
                ", increaseFlag=" + increaseFlag +
                '}';
    }
}
