package com.yanxiu.gphone.hd.student.bean;

import android.content.ContentValues;

import com.common.core.utils.LogInfo;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/22.
 */
public class PublicVolumeBean extends SrtBaseBean {
    private String stageId;
    private String subjectId;
    private String subjectName;
    private String editionId;
    private String volumeId;
    private String volumeName;

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

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }

    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
    }

    public String getEditionId() {
        return editionId;
    }

    public void setEditionId(String editionId) {
        this.editionId = editionId;
    }

    @Override
    public String toString() {
        return "PublicVolumeBean{" +
                "stageId='" + stageId + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", editionId='" + editionId + '\'' +
                ", volumeId='" + volumeId + '\'' +
                ", volumeName='" + volumeName + '\'' +
                '}';
    }

    //=========================================================================================
//=========================================================================================
    public static void saveListData(List<PublicVolumeBean> dataList) {
        if (dataList != null && dataList.size() > 0) {
            for (PublicVolumeBean mBean : dataList) {
                saveData(mBean);
            }
        }
    }

    public static boolean saveData(PublicVolumeBean mBean) {
        if (has(mBean)) {
            ContentValues values = new ContentValues();
            values.put("stageId", mBean.getStageId());
            values.put("subjectId", mBean.getSubjectId());
            values.put("subjectName", mBean.getSubjectName());
            values.put("editionId", mBean.getEditionId());
            values.put("volumeId", mBean.getVolumeId());
            values.put("volumeName", mBean.getVolumeName());
            DataSupport.updateAll(PublicVolumeBean.class, values, "stageId = ? and subjectId = ? and editionId = ? and volumeId = ?",
                    mBean.getStageId(), mBean.getSubjectId(), mBean.getEditionId(), mBean.getVolumeId());
        } else {
            mBean.save();
        }
//        LogInfo.log("haitian", "PublicVolumeBean to string =" + mBean.toString());
        return true;
    }

    public static void saveListFromSubjectEditionBean(List<SubjectEditionBean.DataEntity.ChildrenEntity> dataList, String stageId, String subjectId, String editionId) {
        if (dataList != null && dataList.size() > 0) {
            PublicVolumeBean mPublicVolumeBean = null;
            List<PublicVolumeBean> mPublicVolumeBeanList = new ArrayList<PublicVolumeBean>();
            for (SubjectEditionBean.DataEntity.ChildrenEntity mChildrenEntity : dataList) {
                mPublicVolumeBean = new PublicVolumeBean();
                mPublicVolumeBean.setVolumeId(mChildrenEntity.getId());
                mPublicVolumeBean.setVolumeName(mChildrenEntity.getName());
                mPublicVolumeBean.setStageId(stageId);
                mPublicVolumeBean.setSubjectId(subjectId);
                mPublicVolumeBean.setEditionId(editionId);
                mPublicVolumeBeanList.add(mPublicVolumeBean);
            }
            PublicVolumeBean.saveListData(mPublicVolumeBeanList);
        }
    }

    public static void deleteData(PublicVolumeBean mBean) {
        DataSupport.deleteAll(PublicVolumeBean.class, "stageId = ? and subjectId = ? and editionId = ?",
                mBean.getStageId(), mBean.getSubjectId(), mBean.getEditionId());
    }

    public static void deleteData(String stageId, String subjectId){
        DataSupport.deleteAll(PublicVolumeBean.class, "stageId = ? and subjectId = ?",
                stageId, subjectId);
    }
    public static List<PublicVolumeBean> findDataById(String stageId, String subjectId, String editionId) {
        List<PublicVolumeBean> newsList = DataSupport
                .where("stageId = ? and subjectId = ? and editionId = ?",
                        stageId, subjectId, editionId).find(PublicVolumeBean.class);
        if (newsList != null && newsList.size() > 0) {
            return newsList;
        } else {
            return null;
        }
    }

    public static List<SubjectEditionBean.DataEntity.ChildrenEntity> findDataSortList(String stageId, String subjectId, String editionId) {
        List<PublicVolumeBean> newsList = DataSupport
                .where("stageId = ? and subjectId = ? and editionId = ?",
                        stageId, subjectId, editionId).order("volumeId asc").find(PublicVolumeBean.class);
        if (newsList != null && newsList.size() > 0) {
            List<SubjectEditionBean.DataEntity.ChildrenEntity> sortList = new ArrayList<SubjectEditionBean.DataEntity.ChildrenEntity>();
            SubjectEditionBean.DataEntity.ChildrenEntity mChildrenEntity = null;
            for (PublicVolumeBean mBean : newsList) {
                LogInfo.log("haitian", "PublicVolumeBean to string =" + mBean.toString());
                mChildrenEntity = new SubjectEditionBean.DataEntity.ChildrenEntity();
                mChildrenEntity.setId(mBean.getVolumeId());
                mChildrenEntity.setName(mBean.getVolumeName());
                sortList.add(mChildrenEntity);
            }
            return sortList;
        } else {
            return null;
        }
    }

    public static boolean has(PublicVolumeBean mBean) {
        return DataSupport.where("stageId = ? and subjectId = ? and editionId = ? and volumeId = ?",
                mBean.getStageId(), mBean.getSubjectId(), mBean.getEditionId(), mBean.getVolumeId()).count(PublicVolumeBean.class) > 0;
    }
}
