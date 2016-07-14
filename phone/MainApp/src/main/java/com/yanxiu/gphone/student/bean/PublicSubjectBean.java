package com.yanxiu.gphone.student.bean;

import android.content.ContentValues;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/22.
 */
public class PublicSubjectBean extends SrtBaseBean{
    private String stageId;
    private String subjectId;
    private String subjectName;
    private int subjectErrorQuest;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
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

    public int getSubjectErrorQuest() {
        return subjectErrorQuest;
    }

    public void setSubjectErrorQuest(int subjectErrorQuest) {
        this.subjectErrorQuest = subjectErrorQuest;
    }
//=========================================================================================
//=========================================================================================
    public static boolean saveData(PublicSubjectBean mBean){
        if(has(mBean)){
            ContentValues values = new ContentValues();
            values.put("stageId", mBean.getStageId());
            values.put("subjectId", mBean.getSubjectId());
            values.put("subjectName", mBean.getSubjectName());
            values.put("subjectErrorQuest", mBean.getSubjectErrorQuest());
            DataSupport.updateAll(PublicSubjectBean.class, values, "stageId = ? and subjectId = ?",
                    mBean.getStageId(), mBean.getSubjectId());
        } else {
            mBean.save();
        }
        return true;
    }
    public static void deleteData(PublicSubjectBean mBean){
        DataSupport.deleteAll(PublicSubjectBean.class, "stageId = ? and subjectId = ?",
                mBean.getStageId(), mBean.getSubjectId());
    }
    public static void saveListFromSubjectVersionBean(List<SubjectVersionBean.DataEntity> dataList, String stageId){
        if(dataList != null && dataList.size() > 0){
            PublicSubjectBean mPublicSubjectBean = null;
            List<PublicSubjectBean> subDataList = new ArrayList<PublicSubjectBean>();
            for (SubjectVersionBean.DataEntity mBean : dataList) {
                mPublicSubjectBean = new PublicSubjectBean();
                mPublicSubjectBean.setStageId(stageId);
                mPublicSubjectBean.setSubjectId(mBean.getId() + "");
                mPublicSubjectBean.setSubjectName(mBean.getName());
                subDataList.add(mPublicSubjectBean);
            }
            saveListData(subDataList);
        }
    }
    public static List<SubjectVersionBean.DataEntity> findSubjectVersionList(String stageId){
        List<PublicSubjectBean> newsList = DataSupport
                .where("stageId = ?", stageId).find(PublicSubjectBean.class);
        if(newsList != null && newsList.size()>0){
            List<SubjectVersionBean.DataEntity> subjectVersionList = new ArrayList<SubjectVersionBean.DataEntity>();
            SubjectVersionBean.DataEntity dataEntity = null;
            for (PublicSubjectBean mBean : newsList){
                dataEntity = new SubjectVersionBean.DataEntity();
                dataEntity.setId(mBean.getSubjectId());
                dataEntity.setName(mBean.getSubjectName());
                subjectVersionList.add(dataEntity);
            }
            return subjectVersionList;
        } else {
            return null;
        }
    }
    public static void saveListData(List<PublicSubjectBean> dataList){
        if(dataList != null && dataList.size() > 0){
            for (PublicSubjectBean mBean : dataList) {
                saveData(mBean);
            }
        }
    }
    public static List<PublicSubjectBean> findDataById(String stageId){
        List<PublicSubjectBean> newsList = DataSupport
                .where("stageId = ?", stageId).find(PublicSubjectBean.class);
        if(newsList != null && newsList.size()>0){
            return newsList;
        } else {
            return null;
        }
    }
    public static boolean has(PublicSubjectBean mBean){
        return DataSupport.where("stageId = ? and subjectId = ?", mBean.getStageId(), mBean.getSubjectId()).count(PublicSubjectBean.class)>0;
    }
}
