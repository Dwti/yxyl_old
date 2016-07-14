package com.yanxiu.gphone.hd.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by Administrator on 2015/11/2.
 */
public class PublicSubjectBaseBean implements YanxiuBaseBean {
    private String stageId ;

    private String subjectId ;
    private String subjectName ;

    private String editionId ;
    private String editionName ;

    private int isChapterSection = 0;//0: 代表章节收藏; 1:代表考点收藏

    private String volumeId ;
    private String volumeName ;

    private String chapterId ;
    private String chapterName ;

    private String sectionId ;
    private String sectionName ;
    private String sectionWrongNum ;

    private String uniteId = "0";
    private String uniteName;
    private String uniteQuestNum = "0";

    private boolean hasChapterFavQus = true;

    public boolean isHasChapterFavQus() {
        return hasChapterFavQus;
    }

    public void setHasChapterFavQus(boolean hasChapterFavQus) {
        this.hasChapterFavQus = hasChapterFavQus;
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

    public int getIsChapterSection() {
        return isChapterSection;
    }

    public void setIsChapterSection(int isChapterSection) {
        this.isChapterSection = isChapterSection;
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

    public String getSectionWrongNum() {
        return sectionWrongNum;
    }

    public void setSectionWrongNum(String sectionWrongNum) {
        this.sectionWrongNum = sectionWrongNum;
    }

    public String getUniteId() {
        return uniteId;
    }

    public void setUniteId(String uniteId) {
        this.uniteId = uniteId;
    }

    public String getUniteName() {
        return uniteName;
    }

    public void setUniteName(String uniteName) {
        this.uniteName = uniteName;
    }

    public String getUniteQuestNum() {
        return uniteQuestNum;
    }

    public void setUniteQuestNum(String uniteQuestNum) {
        this.uniteQuestNum = uniteQuestNum;
    }
}
