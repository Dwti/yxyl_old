package com.yanxiu.gphone.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/17.
 * 练习题bean
 */
public class SubjectExercisesItemBean implements YanxiuBaseBean {

    public static final int ANSWER_QUESTION = 0;
    public static final int RESOLUTION = 1;
    public static final int WRONG_SET = 2;
    public static final int IS_CLICK = 3;

    private DataStatusEntityBean status;
    private int totalNum;
    private ArrayList<ExercisesDataEntity> data;
    private long begintime;
    private long endtime;
    private YanxiuPageInfoBean page;

    private int showana;
//    //考点是否点击
//    private boolean isTestCenterOnclick = false;
    private int rightQuestionNum;
    private boolean isResolution = false;
    private boolean isWrongSet = false;
    private boolean isClick = false;

    public int getViewType(){
        if(isResolution){
            return RESOLUTION;
        }else if(isWrongSet){
            return WRONG_SET;
        }else if(isClick){
            return IS_CLICK;
        }
        return ANSWER_QUESTION;
    }

    public int getShowana() {
        return showana;
    }

    public void setShowana(int showana) {
        this.showana = showana;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public YanxiuPageInfoBean getPage() {
        return page;
    }

    public void setPage(YanxiuPageInfoBean page) {
        this.page = page;
    }

    public DataStatusEntityBean getStatus() {
        return status;
    }

    public void setStatus(DataStatusEntityBean status) {
        this.status = status;
    }

    public ArrayList<ExercisesDataEntity> getData() {
        return data;
    }

    public void setData(ArrayList<ExercisesDataEntity> data) {
        this.data = data;
    }

    public long getBegintime() {
        return begintime;
    }

    public void setBegintime(long begintime) {
        this.begintime = begintime;
    }

    public long getEndtime() {
        return endtime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }

    public int getRightQuestionNum() {
        return rightQuestionNum;
    }

    public void setRightQuestionNum(int rightQuestionNum) {
        this.rightQuestionNum = rightQuestionNum;
    }

    public boolean getIsResolution() {
        return isResolution;
    }

    public void setIsResolution(boolean isResolution) {
        this.isResolution = isResolution;
    }

    public boolean isWrongSet() {
        return isWrongSet;
    }

    public void setIsWrongSet(boolean isWrongSet) {
        this.isWrongSet = isWrongSet;
    }


    //    public ExercisesDataEntity getData() {
//        return data;
//    }
//
//    public void setData(ExercisesDataEntity data) {
//        this.data = data;
//    }







}
