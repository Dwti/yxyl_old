package com.yanxiu.gphone.hd.student.bean;

import android.content.ContentValues;

import com.common.core.utils.LogInfo;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/22.
 */
public class PublicChapterBean extends SrtBaseBean {
    private String stageId;
    private String subjectId;
    private String editionId;
    private String volumeId;
    private String chapterId;
    private String chapterName;
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

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getEditionId() {
        return editionId;
    }

    public void setEditionId(String editionId) {
        this.editionId = editionId;
    }

    public String getWrongNum() {
        return wrongNum;
    }

    public void setWrongNum(String wrongNum) {
        this.wrongNum = wrongNum;
    }

    @Override
    public String toString() {
        return "PublicChapterBean{" +
                "stageId='" + stageId + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", editionId='" + editionId + '\'' +
                ", volumeId='" + volumeId + '\'' +
                ", chapterId='" + chapterId + '\'' +
                ", chapterName='" + chapterName + '\'' +
                ", wrongNum='" + wrongNum + '\'' +
                '}';
    }

    //=========================================================================================
//=========================================================================================
    public static void saveListData(List<PublicChapterBean> dataList) {
        if (dataList != null && dataList.size() > 0) {
            for (PublicChapterBean mBean : dataList) {
                saveData(mBean);
            }
        }
    }

    public static boolean saveData(PublicChapterBean mBean) {
            ContentValues values = new ContentValues();
            values.put("stageId", mBean.getStageId());
            values.put("subjectId", mBean.getSubjectId());
            values.put("editionId", mBean.getEditionId());
            values.put("volumeId", mBean.getVolumeId());
            values.put("chapterId", mBean.getChapterId());
            values.put("chapterName", mBean.getChapterName());
            values.put("wrongNum", mBean.getWrongNum());
            int updateNum =    DataSupport.updateAll(PublicChapterBean.class, values, "stageId = ? and subjectId = ? and editionId = ? and volumeId = ? and chapterId = ? and chapterName = ?",
                    mBean.getStageId(), mBean.getSubjectId(), mBean.getEditionId(), mBean.getVolumeId(), mBean.getChapterId(), mBean.getChapterName());
        if (updateNum <= 0) {
            mBean.save();
        } else {

        }
        LogInfo.log("haitian", "PublicChapterBean to string =" + mBean.toString());
        return true;
    }

    public static void saveListFromChapterListDataBean(List<ChapterListDataEntity> dataList, String stageId, String subjectId, String editionId, String volumeId) {
        if (dataList != null && dataList.size() > 0) {
            PublicChapterBean mPublicChapterBean = null;
            List<PublicChapterBean> mPublicChapterBeanList = new ArrayList<PublicChapterBean>();
            for (ChapterListDataEntity mChapterListDataEntity : dataList) {
                mPublicChapterBean = new PublicChapterBean();
                mPublicChapterBean.setStageId(stageId);
                mPublicChapterBean.setSubjectId(subjectId);
                mPublicChapterBean.setEditionId(editionId);
                mPublicChapterBean.setVolumeId(volumeId);
                mPublicChapterBean.setChapterId(mChapterListDataEntity.getId());
                mPublicChapterBean.setChapterName(mChapterListDataEntity.getName());
                //To Do
                mPublicChapterBeanList.add(mPublicChapterBean);
                List<ChapterListDataEntity.ChildrenEntity> mChildrenEntityList = mChapterListDataEntity.getChildren();
                if(mChildrenEntityList != null && mChildrenEntityList.size() > 0){
                    PublicSectionBean.saveListFromChapterListDataEntity(mChildrenEntityList, stageId, subjectId, editionId, volumeId, mChapterListDataEntity.getId());
                }
            }
            PublicChapterBean.saveListData(mPublicChapterBeanList);
        }
    }
    public static void saveListFromDataTeacherEntity(List<DataTeacherEntity> dataList, String stageId, String subjectId, String editionId, String volumeId) {
        if (dataList != null && dataList.size() > 0) {
            PublicChapterBean mPublicChapterBean = null;
            List<PublicChapterBean> mPublicChapterBeanList = new ArrayList<PublicChapterBean>();
            LogInfo.log("haitian", "doInBackground--dataList.size()=" + dataList.size());
            for (DataTeacherEntity mDataTeacherEntity : dataList) {
                mPublicChapterBean = new PublicChapterBean();
                mPublicChapterBean.setStageId(stageId);
                mPublicChapterBean.setSubjectId(subjectId);
                mPublicChapterBean.setEditionId(editionId);
                mPublicChapterBean.setVolumeId(volumeId);
                LogInfo.log("haitian", "mDataTeacherEntity to string =" + mDataTeacherEntity.toString());
                mPublicChapterBean.setChapterId(mDataTeacherEntity.getId());
                mPublicChapterBean.setChapterName(mDataTeacherEntity.getName());
                mPublicChapterBean.setWrongNum(mDataTeacherEntity.getData() != null ? mDataTeacherEntity.getData().getWrongNum() : "0");
                //To Do
                mPublicChapterBeanList.add(mPublicChapterBean);
                List<DataTeacherEntity> mChildrenEntityList = mDataTeacherEntity.getChildren();
                if(mChildrenEntityList != null && mChildrenEntityList.size() > 0){
                    PublicSectionBean.saveListFromDataTeacherEntity(mChildrenEntityList, stageId, subjectId, editionId, volumeId, mDataTeacherEntity.getId());
                }
            }
            PublicChapterBean.saveListData(mPublicChapterBeanList);
        }
    }
    public static ArrayList<DataTeacherEntity> findDataListToDataTeacherEntity(String stageId, String subjectId, String editionId, String volumeId){
        List<PublicChapterBean> newsList = DataSupport
                .where("stageId = ? and subjectId = ? and editionId = ? and volumeId = ?",
                        stageId, subjectId, editionId, volumeId).find(PublicChapterBean.class);
        if(newsList != null && newsList.size() > 0){
            ArrayList<DataTeacherEntity> dataList = new ArrayList<DataTeacherEntity>();
            DataTeacherEntity mDataTeacherEntity = null;
            DataWrongNumEntity mDataWrongNumEntity = null;
            for (PublicChapterBean mPublicChapterBean:newsList){
                mDataTeacherEntity = new DataTeacherEntity();
                mDataTeacherEntity.setId(mPublicChapterBean.getChapterId());
                mDataTeacherEntity.setName(mPublicChapterBean.getChapterName());
                mDataWrongNumEntity = new DataWrongNumEntity();
                mDataWrongNumEntity.setWrongNum(mPublicChapterBean.getWrongNum());
                mDataTeacherEntity.setData(mDataWrongNumEntity);
                mDataTeacherEntity.setChildren(PublicSectionBean.findDataToDataTeacherEntity(stageId, subjectId, editionId, volumeId, mPublicChapterBean.getChapterId()));
                dataList.add(mDataTeacherEntity);
            }
            return dataList;
        } else {
            return null;
        }
    }
    public static void deleteData(PublicChapterBean mBean) {
        DataSupport.deleteAll(PublicChapterBean.class, "stageId = ? and subjectId = ? and editionId = ? and volumeId = ? and chapterId = ?",
                mBean.getStageId(), mBean.getSubjectId(), mBean.getEditionId(), mBean.getVolumeId(), mBean.getChapterId());
    }

    public static List<PublicChapterBean> findDataById(String stageId, String subjectId, String editionId, String volumeId) {
        List<PublicChapterBean> newsList = DataSupport
                .where("stageId = ? and subjectId = ? and editionId = ? and volumeId = ?",
                        stageId, subjectId, editionId, volumeId).find(PublicChapterBean.class);
        if (newsList != null && newsList.size() > 0) {
            return newsList;
        } else {
            return null;
        }
    }

    public static boolean has(PublicChapterBean mBean) {
        return DataSupport.where("stageId = ? and subjectId = ? and editionId = ? and volumeId = ? and chapterId = ?",
                mBean.getStageId(), mBean.getSubjectId(), mBean.getEditionId(), mBean.getVolumeId(), mBean.getChapterId()).count(PublicChapterBean.class) > 0;
    }
}
