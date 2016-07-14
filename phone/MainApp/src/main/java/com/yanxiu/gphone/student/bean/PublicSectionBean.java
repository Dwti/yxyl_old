package com.yanxiu.gphone.student.bean;

import android.content.ContentValues;

import com.common.core.utils.LogInfo;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/22.
 */
public class PublicSectionBean extends SrtBaseBean {
    private String stageId;
    private String subjectId;
    private String editionId;
    private String volumeId;
    private String chapterId;
    private String sectionId;
    private String sectionName;
    private String wrongNum;
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

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getEditionId() {
        return editionId;
    }

    public void setEditionId(String editionId) {
        this.editionId = editionId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getWrongNum() {
        return wrongNum;
    }

    public void setWrongNum(String wrongNum) {
        this.wrongNum = wrongNum;
    }

    @Override
    public String toString() {
        return "PublicSectionBean{" +
                "stageId='" + stageId + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", editionId='" + editionId + '\'' +
                ", volumeId='" + volumeId + '\'' +
                ", chapterId='" + chapterId + '\'' +
                ", sectionId='" + sectionId + '\'' +
                ", sectionName='" + sectionName + '\'' +
                ", wrongNum='" + wrongNum + '\'' +
                '}';
    }

    //=========================================================================================
//=========================================================================================
    public static void saveListData(List<PublicSectionBean> dataList) {
        if (dataList != null && dataList.size() > 0) {
            for (PublicSectionBean mBean : dataList) {
                saveData(mBean);
            }
        }
    }

    public static boolean saveData(PublicSectionBean mBean) {
        if (has(mBean)) {
            ContentValues values = new ContentValues();
            values.put("stageId", mBean.getStageId());
            values.put("subjectId", mBean.getSubjectId());
            values.put("editionId", mBean.getEditionId());
            values.put("volumeId", mBean.getVolumeId());
            values.put("chapterId", mBean.getChapterId());
            values.put("sectionId", mBean.getSectionId());
            values.put("sectionName", mBean.getSectionName());
            DataSupport.updateAll(PublicSectionBean.class, values, "stageId = ? and subjectId = ? and editionId = ? and volumeId = ? and chapterId = ? and sectionId = ?",
                    mBean.getStageId(), mBean.getSubjectId(), mBean.getEditionId(), mBean.getVolumeId(), mBean.getChapterId(), mBean.getSectionId());
        } else {
            mBean.save();
        }
        LogInfo.log("haitian", "PublicSectionBean to string =" + mBean.toString());
        return true;
    }

    public static void saveListFromChapterListDataEntity(List<ChapterListDataEntity.ChildrenEntity> dataList, String stageId, String subjectId, String editionId, String volumeId, String chapterId) {
        if (dataList != null && dataList.size() > 0) {
            PublicSectionBean mPublicSectionBean = null;
            List<PublicSectionBean> mPublicVolumeBeanList = new ArrayList<PublicSectionBean>();
            for (ChapterListDataEntity.ChildrenEntity mChildrenEntity : dataList) {
                mPublicSectionBean = new PublicSectionBean();
                mPublicSectionBean.setStageId(stageId);
                mPublicSectionBean.setSubjectId(subjectId);
                mPublicSectionBean.setEditionId(editionId);
                mPublicSectionBean.setVolumeId(volumeId);
                mPublicSectionBean.setChapterId(chapterId);
                mPublicSectionBean.setSectionId(mChildrenEntity.getId());
                mPublicSectionBean.setSectionName(mChildrenEntity.getName());
                mPublicVolumeBeanList.add(mPublicSectionBean);
            }
            PublicSectionBean.saveListData(mPublicVolumeBeanList);
        }
    }

    public static void saveListFromDataTeacherEntity(List<DataTeacherEntity> dataList, String stageId, String subjectId, String editionId, String volumeId, String chapterId) {
        if (dataList != null && dataList.size() > 0) {
            PublicSectionBean mPublicSectionBean = null;
            List<PublicSectionBean> mPublicVolumeBeanList = new ArrayList<PublicSectionBean>();
            for (DataTeacherEntity mChildrenEntity : dataList) {
                mPublicSectionBean = new PublicSectionBean();
                mPublicSectionBean.setStageId(stageId);
                mPublicSectionBean.setSubjectId(subjectId);
                mPublicSectionBean.setEditionId(editionId);
                mPublicSectionBean.setVolumeId(volumeId);
                mPublicSectionBean.setChapterId(chapterId);
                mPublicSectionBean.setSectionId(mChildrenEntity.getId());
                mPublicSectionBean.setSectionName(mChildrenEntity.getName());
                mPublicSectionBean.setWrongNum(mChildrenEntity.getData() != null ? mChildrenEntity.getData().getWrongNum() : "0");
                mPublicVolumeBeanList.add(mPublicSectionBean);
            }
            PublicSectionBean.saveListData(mPublicVolumeBeanList);
        }
    }
    public static ArrayList<DataTeacherEntity> findDataToDataTeacherEntity(String stageId, String subjectId, String editionId, String volumeId, String chapterId){
        List<PublicSectionBean> newsList = DataSupport
                .where("stageId = ? and subjectId = ? and editionId = ? and volumeId = ? and chapterId = ?",
                        stageId, subjectId, editionId, volumeId, chapterId).find(PublicSectionBean.class);
        if (newsList != null && newsList.size() > 0) {
            ArrayList<DataTeacherEntity> dataList = new ArrayList<DataTeacherEntity>();
            DataTeacherEntity mDataTeacherEntity = null;
            DataWrongNumEntity mDataWrongNumEntity = null;
            for (PublicSectionBean mPublicSectionBean:newsList) {
                mDataTeacherEntity = new DataTeacherEntity();
                mDataTeacherEntity.setId(mPublicSectionBean.getSectionId());
                mDataTeacherEntity.setName(mPublicSectionBean.getSectionName());
                mDataWrongNumEntity = new DataWrongNumEntity();
                mDataWrongNumEntity.setWrongNum(mPublicSectionBean.getWrongNum());
                mDataTeacherEntity.setData(mDataWrongNumEntity);
                dataList.add(mDataTeacherEntity);
            }
            return dataList;
        } else {
            return null;
        }
    }
    public static void deleteData(PublicSectionBean mBean) {
        DataSupport.deleteAll(PublicSectionBean.class, "stageId = ? and subjectId = ? and editionId = ? and volumeId = ? and chapterId = ? and sectionId = ?",
                mBean.getStageId(), mBean.getSubjectId(), mBean.getEditionId(), mBean.getVolumeId(), mBean.getChapterId(), mBean.getSectionId());
    }

    public static List<PublicSectionBean> findDataById(String stageId, String subjectId, String editionId, String volumeId, String chapterId) {
        List<PublicSectionBean> newsList = DataSupport
                .where("stageId = ? and subjectId = ? and editionId = ? and volumeId = ? and chapterId = ?",
                        stageId, subjectId, editionId, volumeId, chapterId).find(PublicSectionBean.class);
        if (newsList != null && newsList.size() > 0) {
            return newsList;
        } else {
            return null;
        }
    }

    public static boolean has(PublicSectionBean mBean) {
        return DataSupport.where("stageId = ? and subjectId = ? and editionId = ? and volumeId = ? and chapterId = ? and sectionId = ?",
                mBean.getStageId(), mBean.getSubjectId(), mBean.getEditionId(), mBean.getVolumeId(), mBean.getChapterId(), mBean.getSectionId()).count(PublicSectionBean.class) > 0;
    }
}
