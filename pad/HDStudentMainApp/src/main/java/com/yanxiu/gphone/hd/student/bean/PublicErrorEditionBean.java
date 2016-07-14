package com.yanxiu.gphone.hd.student.bean;

import android.content.ContentValues;

import com.common.core.utils.LogInfo;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/16.
 */
public class PublicErrorEditionBean extends SrtBaseBean {
    private String stageId ;

    private String subjectId ;
    private String subjectName ;

    private String editionId;
    private String editionName;
    private String wrongNum;


    public static boolean saveData(PublicErrorEditionBean mBean) {
        ContentValues values = new ContentValues();
        values.put("stageId", mBean.getStageId());

        values.put("subjectId", mBean.getSubjectId());
        values.put("subjectName", mBean.getSubjectName());

        values.put("editionId", mBean.getEditionId());
        values.put("editionName", mBean.getEditionName());

        int updateNum = DataSupport.updateAll(PublicErrorEditionBean.class, values,
                "stageId = ? and subjectId = ? and subjectName = ? and editionId = ? and editionName = ?",
                mBean.getStageId(), mBean.getSubjectId(), mBean.getSubjectName(), mBean.getEditionId(), mBean.getEditionName());
        if (updateNum <= 0) {
            mBean.save();
            LogInfo.log("haitian", "PublicErrorEditionBean to string =" + mBean.toString());
        } else {
            LogInfo.log("haitian", "update PublicErrorEditionBean to string =" + mBean.toString() + "--- updateNum=" + updateNum);
        }
        return true;
    }

    public static List<PublicErrorEditionBean> getErrorEditionDataByStageId(String stageId) {
        List<PublicErrorEditionBean> subjectList = DataSupport.select("stageId", "subjectId", "subjectName", "wrongNum").
                where("stageId = ? group by subjectId", stageId).find(PublicErrorEditionBean.class);
        if (subjectList != null && subjectList.size() > 0) {
            return subjectList;
        } else {
            return null;
        }
    }
    public static List<DataTeacherEntity> getErrorDataByStageId(String stageId) {
        List<PublicErrorEditionBean> subjectList = DataSupport.select("stageId", "subjectId", "subjectName", "wrongNum", "editionId", "editionName").
                where("stageId = ? group by subjectId", stageId).find(PublicErrorEditionBean.class);
        if (subjectList != null && subjectList.size() > 0) {
            List<DataTeacherEntity> dataList = new ArrayList<DataTeacherEntity>();
            DataTeacherEntity mDataTeacherEntity = null;
            DataWrongNumEntity mDataWrongNumEntity = null;
            for (PublicErrorEditionBean mBean : subjectList){
                mDataTeacherEntity = new DataTeacherEntity();
                mDataTeacherEntity.setId(mBean.getSubjectId());
                mDataTeacherEntity.setName(mBean.getSubjectName());
                mDataWrongNumEntity = new DataWrongNumEntity();
                mDataWrongNumEntity.setEditionId(mBean.getEditionId());
                mDataWrongNumEntity.setEditionName(mBean.getEditionName());
                int num = DataSupport.where("stageId = ? and subjectId = ?",
                        mBean.getStageId(), mBean.getSubjectId()).count(PublicErrorEditionBean.class);
                mDataWrongNumEntity.setWrongNum(num +"");
                mDataTeacherEntity.setData(mDataWrongNumEntity);
                dataList.add(mDataTeacherEntity);
            }
            return dataList;
        } else {
            return null;
        }
    }
    public static boolean has(PublicErrorEditionBean mBean) {
        return DataSupport.where("stageId = ? and subjectId = ? and editionId = ?",
                mBean.getStageId(), mBean.getSubjectId(), mBean.getEditionId()).count(PublicErrorQuestionCollectionBean.class) > 0;
    }
    //====================================================================================================================
    //====================================================================================================================

    @Override
    public String toString() {
        return "PublicErrorEditionBean{" +
                "stageId='" + stageId + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", editionId='" + editionId + '\'' +
                ", editionName='" + editionName + '\'' +
                ", wrongNum='" + wrongNum + '\'' +
                '}';
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getEditionId() {
        return editionId;
    }

    public void setEditionId(String editionId) {
        this.editionId = editionId;
    }

    public String getEditionName() {
        return editionName;
    }

    public void setEditionName(String editionName) {
        this.editionName = editionName;
    }

    public String getWrongNum() {
        return wrongNum;
    }

    public void setWrongNum(String wrongNum) {
        this.wrongNum = wrongNum;
    }
}