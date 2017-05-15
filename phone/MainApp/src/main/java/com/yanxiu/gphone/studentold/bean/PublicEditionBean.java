package com.yanxiu.gphone.studentold.bean;

import android.content.ContentValues;

import com.common.core.utils.LogInfo;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.task.base.threadpool.YanxiuSimpleAsyncTask;
import com.yanxiu.gphone.studentold.YanxiuApplication;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/22.
 */
public class PublicEditionBean extends SrtBaseBean{
    private String stageId;
    private String subjectId;
    private String subjectName;
    private String editionId;
    private String editionName;

    public String getEditionId() {
        return editionId;
    }

    public void setEditionId(String editionId) {
        this.editionId = editionId;
    }

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

    public String getEditionName() {
        return editionName;
    }

    public void setEditionName(String editionName) {
        this.editionName = editionName;
    }

    @Override
    public String toString() {
        return "PublicEditionBean{" +
                "stageId='" + stageId + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", editionId='" + editionId + '\'' +
                ", editionName='" + editionName + '\'' +
                '}';
    }

    //=========================================================================================
//=========================================================================================
    public static boolean saveData(PublicEditionBean mBean){
        if(has(mBean)){
            ContentValues values = new ContentValues();
            values.put("stageId", mBean.getStageId());
            values.put("subjectId", mBean.getSubjectId());
            values.put("subjectName", mBean.getSubjectName());
            values.put("editionId", mBean.getEditionId());
            values.put("editionName", mBean.getEditionName());
            DataSupport.updateAll(PublicEditionBean.class, values, "stageId = ? and subjectId = ? and editionId = ?",
                    mBean.getStageId(), mBean.getSubjectId(), mBean.getEditionId());
        } else {
            mBean.save();
        }
        return true;
    }

    public static void saveListFromSubjectEditionBean(final List<SubjectEditionBean.DataEntity> dataList, final String stageId, final String subjectId){
        new YanxiuSimpleAsyncTask<YanxiuBaseBean>(YanxiuApplication.getInstance()){
            @Override
            public YanxiuBaseBean doInBackground() {
                if(dataList != null && dataList.size() > 0){
                    PublicEditionBean.deleteData(stageId, subjectId);
                    PublicEditionBean mPublicEditionBean = null;
                    List<PublicEditionBean> edDataList = new ArrayList<PublicEditionBean>();
                    for (SubjectEditionBean.DataEntity mBean : dataList) {
                        mPublicEditionBean = new PublicEditionBean();
                        mPublicEditionBean.setStageId(stageId);
                        mPublicEditionBean.setSubjectId(subjectId);
                        mPublicEditionBean.setEditionId(mBean.getId());
                        mPublicEditionBean.setEditionName(mBean.getName());
                        edDataList.add(mPublicEditionBean);
                        List<SubjectEditionBean.DataEntity.ChildrenEntity> mChildrenEntityList = mBean.getChildren();
                        if(mChildrenEntityList != null && mChildrenEntityList.size()>0){
                            PublicVolumeBean mPublicVolumeBean = null;
                            List<PublicVolumeBean> mPublicVolumeBeanList = new ArrayList<PublicVolumeBean>();
                            for(SubjectEditionBean.DataEntity.ChildrenEntity mChildrenEntity : mChildrenEntityList){
                                mPublicVolumeBean = new PublicVolumeBean();
                                mPublicVolumeBean.setVolumeId(mChildrenEntity.getId());
                                mPublicVolumeBean.setVolumeName(mChildrenEntity.getName());
                                mPublicVolumeBean.setStageId(stageId);
                                mPublicVolumeBean.setSubjectId(subjectId);
                                mPublicVolumeBean.setEditionId(mBean.getId());
                                mPublicVolumeBeanList.add(mPublicVolumeBean);
                            }
                            if(mPublicVolumeBeanList.size() > 0){
                                PublicVolumeBean.deleteData(stageId, subjectId);
                                PublicVolumeBean.saveListData(mPublicVolumeBeanList);
                            }
                        }
                    }
                    saveListData(edDataList);
                }
                return null;
            }
            @Override
            public void onPostExecute(YanxiuBaseBean result) {

            }
        }.start();

    }
    public static void saveListData(List<PublicEditionBean> dataList){
        if(dataList != null && dataList.size() > 0){
            for (PublicEditionBean mBean : dataList) {
                saveData(mBean);
            }
        }
    }
    public static void deleteData(PublicEditionBean mBean){
        DataSupport.deleteAll(PublicEditionBean.class, "stageId = ? and subjectId = ? and editionId = ?",
                mBean.getStageId(), mBean.getSubjectId(), mBean.getEditionId());
    }
    public static void deleteData(String stageId, String subjectId){
        DataSupport.deleteAll(PublicEditionBean.class, "stageId = ? and subjectId = ?",
                stageId, subjectId);
    }

    public static List<PublicEditionBean> findDataById(String stageId, String subjectId){
        List<PublicEditionBean> newsList = DataSupport
                .where("stageId = ? and subjectId = ?",
                        stageId, subjectId).find(PublicEditionBean.class);
        if(newsList != null && newsList.size()>0){
            return newsList;
        } else {
            return null;
        }
    }
    public static List<SubjectEditionBean.DataEntity> findDataToSubjectEditionBean(String stageId, String subjectId){
        List<PublicEditionBean> newsList = DataSupport
                .where("stageId = ? and subjectId = ?",
                        stageId, subjectId).find(PublicEditionBean.class);
        if(newsList != null && newsList.size()>0){
            List<SubjectEditionBean.DataEntity> dataEntityList = new ArrayList<SubjectEditionBean.DataEntity>();
            SubjectEditionBean.DataEntity mDataEntity = null;
            for (PublicEditionBean mPublicEditionBean : newsList){
                mDataEntity = new SubjectEditionBean.DataEntity();
                mDataEntity.setId(mPublicEditionBean.getEditionId());
                mDataEntity.setName(mPublicEditionBean.getEditionName());
                LogInfo.log("haitian", "findDataToSubjectEditionBean to String =" + mDataEntity.toString());
                dataEntityList.add(mDataEntity);
            }
            return dataEntityList;
        } else {
            return null;
        }
    }

    public static boolean has(PublicEditionBean mBean){
        return DataSupport.where("stageId = ? and subjectId = ? and editionId = ?", mBean.getStageId(), mBean.getSubjectId(), mBean.getEditionId()).count(PublicEditionBean.class)>0;
    }
}
