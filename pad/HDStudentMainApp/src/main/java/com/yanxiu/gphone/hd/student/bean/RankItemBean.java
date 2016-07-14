package com.yanxiu.gphone.hd.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by Administrator on 2015/9/24.
 */
public class RankItemBean implements YanxiuBaseBean {
    private String id;
    private String userid;
    private String  year;
    private String  week;
    private String answerquenum;
    private String correctquenum;
    private String correctrate;//正确率
    private String nickName; //昵称
    private String schoolName;//学校名称
    private String headImg;//头像

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getAnswerquenum() {
        return answerquenum;
    }

    public void setAnswerquenum(String answerquenum) {
        this.answerquenum = answerquenum;
    }

    public String getCorrectquenum() {
        return correctquenum;
    }

    public void setCorrectquenum(String correctquenum) {
        this.correctquenum = correctquenum;
    }

    public String getCorrectrate() {
        return correctrate;
    }

    public void setCorrectrate(String correctrate) {
        this.correctrate = correctrate;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }
}
