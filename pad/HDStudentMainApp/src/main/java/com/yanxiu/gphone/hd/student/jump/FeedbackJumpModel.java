package com.yanxiu.gphone.hd.student.jump;

/**
 * Created by Administrator on 2015/11/3.
 */
public class FeedbackJumpModel extends  BaseJumpModel {
    private int typeCode;
    private String questionId;
    public int getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(int typeCode) {
        this.typeCode = typeCode;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
