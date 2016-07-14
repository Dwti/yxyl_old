package com.yanxiu.gphone.student.bean;

/**
 * Created by Administrator on 2015/7/20.
 */
public class PropertyBean extends SrtBaseBean{

    /**
     * totalUnfinish : 5
     */
    private int totalUnfinish;
    private int classId;
    private String className;

    public void setTotalUnfinish(int totalUnfinish) {
        this.totalUnfinish = totalUnfinish;
    }

    public int getTotalUnfinish() {
        return totalUnfinish;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    @Override public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
