package com.yanxiu.gphone.parent.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by hai8108 on 16/3/25.
 */
public class ParentWeekReportDataBean implements YanxiuBaseBean{
    /**
     * type : 0
     * answerIntelQuesNum : 200
     * intelQuesCorrectNum : 50
     * intelWeekRank : 67
     * answerHwkQuesNum : 240
     * increaseRate : 0.5
     * avgCorrectRate : 0.34
     * classAvgCorrectRate : 0.45
     * classBestRate : 0.9
     * classId : 10000145
     * increaseFlag : 1
     */

    private int type;
    private int answerIntelQuesNum;
    private int intelQuesCorrectNum;
    private int intelWeekRank;
    private int answerHwkQuesNum;
    private double increaseRate;
    private double avgCorrectRate;
    private double classAvgCorrectRate;
    private double classBestRate;
    private String classId;
    private int increaseFlag;

    public int getType () {
        return type;
    }

    public void setType (int type) {
        this.type = type;
    }

    public int getAnswerIntelQuesNum () {
        return answerIntelQuesNum;
    }

    public void setAnswerIntelQuesNum (int answerIntelQuesNum) {
        this.answerIntelQuesNum = answerIntelQuesNum;
    }

    public int getIntelQuesCorrectNum () {
        return intelQuesCorrectNum;
    }

    public void setIntelQuesCorrectNum (int intelQuesCorrectNum) {
        this.intelQuesCorrectNum = intelQuesCorrectNum;
    }

    public int getIntelWeekRank () {
        return intelWeekRank;
    }

    public void setIntelWeekRank (int intelWeekRank) {
        this.intelWeekRank = intelWeekRank;
    }

    public int getAnswerHwkQuesNum () {
        return answerHwkQuesNum;
    }

    public void setAnswerHwkQuesNum (int answerHwkQuesNum) {
        this.answerHwkQuesNum = answerHwkQuesNum;
    }

    public double getIncreaseRate () {
        return increaseRate;
    }

    public void setIncreaseRate (double increaseRate) {
        this.increaseRate = increaseRate;
    }

    public double getAvgCorrectRate () {
        return avgCorrectRate;
    }

    public void setAvgCorrectRate (double avgCorrectRate) {
        this.avgCorrectRate = avgCorrectRate;
    }

    public double getClassAvgCorrectRate () {
        return classAvgCorrectRate;
    }

    public void setClassAvgCorrectRate (double classAvgCorrectRate) {
        this.classAvgCorrectRate = classAvgCorrectRate;
    }

    public double getClassBestRate () {
        return classBestRate;
    }

    public void setClassBestRate (double classBestRate) {
        this.classBestRate = classBestRate;
    }

    public String getClassId () {
        return classId;
    }

    public void setClassId (String classId) {
        this.classId = classId;
    }

    public int getIncreaseFlag () {
        return increaseFlag;
    }

    public void setIncreaseFlag (int increaseFlag) {
        this.increaseFlag = increaseFlag;
    }
}
