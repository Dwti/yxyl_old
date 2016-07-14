package com.yanxiu.gphone.parent.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by hai8108 on 16/3/25.
 */
public class ParentPublicPropertyBean implements YanxiuBaseBean{
    private int week;
    private String year;
    private String mondayDate;
    private int canNext;//是否可以下一周，0代表不可以，1代表可以
    private int canLast;//是否可以上一周，0代表不可以，1代表可以
    private int shouldShow;

    public int getShouldShow () {
        return shouldShow;
    }

    public void setShouldShow (int shouldShow) {
        this.shouldShow = shouldShow;
    }

    public int getWeek () {
        return week;
    }

    public void setWeek (int week) {
        this.week = week;
    }

    public String getYear () {
        return year;
    }

    public void setYear (String year) {
        this.year = year;
    }

    public String getMondayDate () {
        return mondayDate;
    }

    public void setMondayDate (String mondayDate) {
        this.mondayDate = mondayDate;
    }

    public int getCanNext () {
        return canNext;
    }

    public void setCanNext (int canNext) {
        this.canNext = canNext;
    }

    public int getCanLast () {
        return canLast;
    }

    public void setCanLast (int canLast) {
        this.canLast = canLast;
    }

    @Override
    public String toString () {
        return "ParentPublicPropertyBean{" +
                "week=" + week +
                ", year='" + year + '\'' +
                ", mondayDate='" + mondayDate + '\'' +
                ", canNext=" + canNext +
                ", canLast=" + canLast +
                '}';
    }
}
