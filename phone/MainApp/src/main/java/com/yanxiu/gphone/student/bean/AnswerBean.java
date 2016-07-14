package com.yanxiu.gphone.student.bean;

import java.util.ArrayList;

/**
 *
 */
public class AnswerBean extends SrtBaseBean {

    public static final int ANSER_RIGHT = 0;
    public static final int ANSER_WRONG = 1;
    public static final int ANSER_UNFINISH = 3;
    public static final int ANSER_FINISH = 4;
    public static final int ANSER_READED = 5;

    private boolean isCollectionn = false;


    private boolean isFinish = false;

    private boolean isRight = false;

    //是否是主观题
    private boolean isSubjective = false;

    private String selectType;

    private int consumeTime;

    private String answerStr = "";
//    status": 3,  // 题目状态 0 回答正确， 1 回答错误， 3 未作答案
    private int status = -1;

    private ArrayList<String> multiSelect = new ArrayList<String>();

    private ArrayList<String> fillAnswers = new ArrayList<String>();

    private ArrayList<String> subjectivImageUri = new ArrayList<String>();
    //学科id
    private String subjectId;

    public boolean isFinish() {
        return isFinish;
    }

    public void setIsFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }

    public boolean isSubjective() {
        return isSubjective;
    }

    public void setIsSubjective(boolean isSubjective) {
        this.isSubjective = isSubjective;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }


    public ArrayList<String> getMultiSelect() {
        return multiSelect;
    }

    public void setMultiSelect(ArrayList<String> multiSelect) {
        this.multiSelect = multiSelect;
    }

    public ArrayList<String> getFillAnswers() {
        return fillAnswers;
    }

    public void setFillAnswers(ArrayList<String> fillAnswers) {
        this.fillAnswers = fillAnswers;
    }

    public ArrayList<String> getSubjectivImageUri() {
        return subjectivImageUri;
    }

    public void setSubjectivImageUri(ArrayList<String> subjectivImageUri) {
//        if(subjectivImageUri==null||subjectivImageUri.isEmpty()){
//            return;
//        }
        this.subjectivImageUri = subjectivImageUri;
    }

    public int getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(int consumeTime) {
        this.consumeTime = consumeTime;
    }

    public String getAnswerStr() {
        return answerStr;
    }

    public void setAnswerStr(String answerStr) {
        this.answerStr = answerStr;
    }


    public boolean isCollectionn() {
        return isCollectionn;
    }

    public void setIsCollectionn(boolean isCollectionn) {
        this.isCollectionn = isCollectionn;
    }

    //题目状态 0 回答正确， 1 回答错误， 3 未作答案(如果市复合体型包含多个问题的， 如果有一道题目错误就设置成错误
    public int getStatus() {
        if(isFinish()){

            if(isSubjective()){
                status = ANSER_FINISH;
                return status;
            }

            if(isRight()){
                status = ANSER_RIGHT;
            }else{
                status = ANSER_WRONG;
            }
        }else{
            status = ANSER_UNFINISH;
        }
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isRight() {
        return isRight;
    }

    public void setIsRight(boolean isRight) {
        this.isRight = isRight;
    }

    @Override
    public String toString() {
        return "AnswerBean{" +
                "isFinish=" + isFinish +
                ", isRight=" + isRight +
                ", selectType='" + selectType + '\'' +
                ", consumeTime=" + consumeTime +
                ", answerStr='" + answerStr + '\'' +
                ", status=" + status +
                ", multiSelect=" + multiSelect +
                ", fillAnswers=" + fillAnswers +
                '}';
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectId() {
        return subjectId;
    }
}
