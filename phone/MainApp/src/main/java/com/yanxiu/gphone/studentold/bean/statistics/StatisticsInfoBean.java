package com.yanxiu.gphone.studentold.bean.statistics;

/**
 * Created by JS-00 on 2016/6/12.
 */
public class StatisticsInfoBean extends YanXiuDataBase {
    //事件：eventID
    /*
     0:注册成功
     1:每次启动
     2:提交练习/作业
     3:收到作业(每份作业统计一次)
     4:完成作业
     5:进入练习
     6:进入后台
     7:进入前台
     8:退出app
     9:加入班级成功
    */
    private String eventID;
    //用户id：uid
    private String uid;
    //时间戳：timestamp
    private String timestamp;
    //来源：source（0，移动端，1，页面）
    private String source = "0";
    //客户端类型：client（0，iOS，1，android）
    private String client = "1";
    //教材版本：editionID
    private String editionID;
    //年级：gradeID
    private String gradeID;
    //学科：subjectID



    private String subjectID;
    //试卷类型：paperType（0，练习，1，作业）
    private String paperType;
    //题目数量：quesNum
    private String quesNum;

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getEditionID() {
        return editionID;
    }

    public void setEditionID(String editionID) {
        this.editionID = editionID;
    }

    public String getGradeID() {
        return gradeID;
    }

    public void setGradeID(String gradeID) {
        this.gradeID = gradeID;
    }

    public String getPaperType() {
        return paperType;
    }

    public void setPaperType(String paperType) {
        this.paperType = paperType;
    }

    public String getQuesNum() {
        return quesNum;
    }

    public void setQuesNum(String quesNum) {
        this.quesNum = quesNum;
    }

    @Override
    public String toString() {
        return getUid()+getEventID()+getTimestamp();
    }



}
