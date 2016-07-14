package com.yanxiu.gphone.student.bean;

import android.content.ContentValues;

import com.alibaba.fastjson.JSON;
import com.common.core.utils.LogInfo;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.task.base.threadpool.YanxiuSimpleAsyncTask;
import com.yanxiu.gphone.student.YanxiuApplication;
import com.yanxiu.gphone.student.utils.YanXiuConstant;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static com.yanxiu.gphone.student.utils.YanXiuConstant.QUESTION_TYP.QUESTION_READING;

/**
 * Created by Administrator on 2015/7/23.
 */
public class PublicErrorQuestionCollectionBean extends SrtBaseBean {
    private String stageId = "";

    private String intelligName;
    private String subjectId = "";
    private String subjectName = "";

    private String editionId = "";
    private String editionName = "";

    private int isChapterSection = 0;//0: 代表章节错题; 1:代表考点错题

    private String volumeId = "";
    private String volumeName = "";

    private String chapterId = "";
    private String chapterName = "";
    private String chapterWrongNum = "";

    private String sectionId = "";
    private String sectionName = "";
    private String sectionWrongNum = "";

    private String uniteId = "0";
    private String uniteName = "";
    private String uniteQuestNum = "0";

    private String qid = "";

    private String questionJsonStr = "";

    @Override
    public String toString() {
        return "PublicErrorQuestionCollectionBean{" +
                "stageId='" + stageId + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", editionId='" + editionId + '\'' +
                ", editionName='" + editionName + '\'' +
                ", volumeId='" + volumeId + '\'' +
                ", volumeName='" + volumeName + '\'' +
                ", isChapterSection='" + isChapterSection + '\'' +
                ", chapterId='" + chapterId + '\'' +
                ", chapterName='" + chapterName + '\'' +
                ", chapterWrongNum='" + chapterWrongNum + '\'' +
                ", sectionId='" + sectionId + '\'' +
                ", sectionName='" + sectionName + '\'' +
                ", sectionWrongNum='" + sectionWrongNum + '\'' +
                ", uniteId='" + uniteId + '\'' +
                ", uniteName='" + uniteName + '\'' +
                ", uniteQuestNum='" + uniteQuestNum + '\'' +
                ", qid='" + qid + '\'' +
                ", intelligName='" + intelligName + '\'' +
                ", questionJsonStr='" + questionJsonStr + '\'' +
                '}';
    }

    //==========================================================================
//==========================================================================
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public static void updateDelData(String qid) {
        ContentValues values = new ContentValues();
        values.put("stageId", "0");
        values.put("subjectId", "0");
        values.put("editionId", "0");
        int updateNum = DataSupport.updateAll(PublicErrorQuestionCollectionBean.class, values, "qid = ?", qid);
        LogInfo.log("haitian", "updateDelData num =" + updateNum);
    }

    public static void deleteAllData() {
        DataSupport.deleteAll(PublicErrorQuestionCollectionBean.class, "stageId <> ?", "1");
    }

    public static void deleteItemList(final String volumeId, final ArrayList<String> qidList) {
        new YanxiuSimpleAsyncTask<YanxiuBaseBean>(YanxiuApplication.getInstance()) {
            @Override
            public YanxiuBaseBean doInBackground() {
                for (String qid : qidList) {
                    int num = DataSupport.deleteAll(PublicErrorQuestionCollectionBean.class, "stageId = ? and subjectId = ? and editionId = ? and volumeId = ? and qid = ?",
                            "0", "0", "0", volumeId, qid);
                    LogInfo.log("haitian", "deleteItemList num =" + num);
                }
                return null;
            }

            @Override
            public void onPostExecute(YanxiuBaseBean result) {

            }
        }.start();
    }

    /**
     * 获取本地章错题， 章错题 sectionId 为 0
     *
     * @param stageId
     * @param subjectId
     * @param editionId
     * @param volumeId
     * @param chapterId
     * @param offset    分页偏移量 (currentPage-1)*pageSize
     * @return
     */
    public static ExercisesDataEntity findExercisesDataEntityWithChapter(String stageId, String subjectId, String editionId, String volumeId,
                                                                         String chapterId, String sectionId, String uniteId, int isChapterSection, int offset) {
        List<PublicErrorQuestionCollectionBean> newsList = null;
        if(isChapterSection == 1){
            editionId = "0";
            volumeId = "0";
        }
        if (offset <= 0) {
            newsList = DataSupport
                    .where("stageId = ? and subjectId = ? and editionId = ? and volumeId = ? and chapterId = ? and sectionId = ? and uniteId = ? " +
                                    "and isChapterSection = ?",
                            stageId, subjectId, editionId, volumeId, chapterId, sectionId, uniteId, isChapterSection+"").limit(YanXiuConstant.YX_PAGESIZE_CONSTANT).find
                            (PublicErrorQuestionCollectionBean.class);
        } else {
            newsList = DataSupport
                    .where("stageId = ? and subjectId = ? and editionId = ? and volumeId = ? and chapterId = ? and sectionId = ? and uniteId = ? " +
                                    "and isChapterSection = ?",
                            stageId, subjectId, editionId, volumeId, chapterId, sectionId, uniteId, isChapterSection+"").limit(YanXiuConstant
                            .YX_PAGESIZE_CONSTANT)
                    .offset
                            (offset).find(PublicErrorQuestionCollectionBean.class);
        }
        if (newsList != null && newsList.size() > 0) {
            ExercisesDataEntity mExercisesDataEntity = new ExercisesDataEntity();
            PaperTestEntity mPaperTestEntity = null;
            List<PaperTestEntity> paperTest = new ArrayList<PaperTestEntity>();
            for (PublicErrorQuestionCollectionBean mBean : newsList) {
                mPaperTestEntity = JSON.parseObject(mBean.getQuestionJsonStr(), PaperTestEntity.class);
                paperTest.add(mPaperTestEntity);
            }
            if (paperTest.size() > 0) {
                PublicErrorQuestionCollectionBean pBean = newsList.get(0);
                mExercisesDataEntity.setStageid(pBean.getStageId());
                mExercisesDataEntity.setName(pBean.getIntelligName());

                mExercisesDataEntity.setSubjectid(pBean.getSubjectId());
                mExercisesDataEntity.setSubjectName(pBean.getSubjectName());

                mExercisesDataEntity.setBedition(pBean.getEditionId());
                mExercisesDataEntity.setEditionName(pBean.getEditionName());

                mExercisesDataEntity.setVolume(pBean.getVolumeId());
                mExercisesDataEntity.setVolumeName(pBean.getVolumeName());

                mExercisesDataEntity.setIsChapterSection(pBean.getIsChapterSection());

                mExercisesDataEntity.setChapterid(pBean.getChapterId());
                mExercisesDataEntity.setChapterName(pBean.getChapterName());

                mExercisesDataEntity.setSectionid(pBean.getSectionId());
                mExercisesDataEntity.setSectionName(pBean.getSectionName());

                mExercisesDataEntity.setCellid(pBean.getUniteId());
                mExercisesDataEntity.setCellName(pBean.getUniteName());

                mExercisesDataEntity.setPaperTest(paperTest);
                return mExercisesDataEntity;
            }
        }
        return null;
    }

    /**
     * 获取本地章错题， 章错题 sectionId 为 0
     *
     * @param stageId
     * @param subjectId
     * @param offset    分页偏移量 (currentPage-1)*pageSize
     * @return
     */
    public static ExercisesDataEntity findExercisesDataEntityWithAll(String stageId, String subjectId, int offset) {
        List<PublicErrorQuestionCollectionBean> newsList = null;
        if (offset <= 0) {
            newsList = DataSupport
                    .where("stageId = ? and subjectId = ?",
                            stageId, subjectId).limit(YanXiuConstant.YX_PAGESIZE_CONSTANT).find
                            (PublicErrorQuestionCollectionBean.class);
        } else {
            newsList = DataSupport
                    .where("stageId = ? and subjectId = ?",
                            stageId, subjectId).limit(YanXiuConstant
                            .YX_PAGESIZE_CONSTANT)
                    .offset
                            (offset).find(PublicErrorQuestionCollectionBean.class);
        }
        if (newsList != null && newsList.size() > 0) {
            ExercisesDataEntity mExercisesDataEntity = new ExercisesDataEntity();
            PaperTestEntity mPaperTestEntity = null;
            List<PaperTestEntity> paperTest = new ArrayList<PaperTestEntity>();
            for (PublicErrorQuestionCollectionBean mBean : newsList) {
                mPaperTestEntity = JSON.parseObject(mBean.getQuestionJsonStr(), PaperTestEntity.class);
                paperTest.add(mPaperTestEntity);
            }
            if (paperTest.size() > 0) {
                PublicErrorQuestionCollectionBean pBean = newsList.get(0);
                mExercisesDataEntity.setStageid(pBean.getStageId());
                mExercisesDataEntity.setName(pBean.getIntelligName());

                mExercisesDataEntity.setSubjectid(pBean.getSubjectId());
                mExercisesDataEntity.setSubjectName(pBean.getSubjectName());

                mExercisesDataEntity.setBedition(pBean.getEditionId());
                mExercisesDataEntity.setEditionName(pBean.getEditionName());

                mExercisesDataEntity.setVolume(pBean.getVolumeId());
                mExercisesDataEntity.setVolumeName(pBean.getVolumeName());

                mExercisesDataEntity.setIsChapterSection(pBean.getIsChapterSection());

                mExercisesDataEntity.setChapterid(pBean.getChapterId());
                mExercisesDataEntity.setChapterName(pBean.getChapterName());

                mExercisesDataEntity.setSectionid(pBean.getSectionId());
                mExercisesDataEntity.setSectionName(pBean.getSectionName());

                mExercisesDataEntity.setCellid(pBean.getUniteId());
                mExercisesDataEntity.setCellName(pBean.getUniteName());

                mExercisesDataEntity.setPaperTest(paperTest);
                return mExercisesDataEntity;
            }
        }
        return null;
    }

    public static void saveExercisesDataEntity(final ExercisesDataEntity mExercisesDataEntity) {
        new YanxiuSimpleAsyncTask<YanxiuBaseBean>(YanxiuApplication.getInstance()) {
            @Override
            public YanxiuBaseBean doInBackground() {
                if (mExercisesDataEntity != null) {
                    List<PaperTestEntity> paperTestEntityList = mExercisesDataEntity.getPaperTest();
                    if (paperTestEntityList != null && paperTestEntityList.size() > 0) {
                        ContentValues values = new ContentValues();
                        values.put("stageId", mExercisesDataEntity.getStageid());

                        values.put("subjectId", mExercisesDataEntity.getSubjectid());
                        values.put("subjectName", mExercisesDataEntity.getSubjectName());

                        values.put("isChapterSection", mExercisesDataEntity.getIsChapterSection());

                        if(mExercisesDataEntity.getIsChapterSection() == 1){
                            values.put("volumeId", "0");
                            values.put("editionId", "0");
                        } else {
                            values.put("editionId", mExercisesDataEntity.getBedition());
                            values.put("volumeId", mExercisesDataEntity.getVolume());
                        }

                        values.put("volumeName", mExercisesDataEntity.getVolumeName());
                        values.put("editionName", mExercisesDataEntity.getEditionName());

                        values.put("chapterId", mExercisesDataEntity.getChapterid());
                        values.put("chapterName", mExercisesDataEntity.getChapterName());

                        values.put("sectionId", mExercisesDataEntity.getSectionid());
                        values.put("sectionName", mExercisesDataEntity.getSectionName());

                        values.put("uniteId", mExercisesDataEntity.getCellid());
                        values.put("uniteName", mExercisesDataEntity.getCellName());
                        LogInfo.log("haitian", "uniteName" + mExercisesDataEntity.getCellName() + "\r\n--------------------------------------------");
                        PublicErrorQuestionCollectionBean mBean = null;
                        for (PaperTestEntity mPaperTestEntity : paperTestEntityList) {
                            boolean isFalse = false;
                            if (mPaperTestEntity.getQuestions() != null && mPaperTestEntity.getQuestions().getChildren() != null) {
                                if (mPaperTestEntity.getQuestions().getType_id() == QUESTION_READING.type) {
                                    List<QuestionEntity> questionList = mPaperTestEntity.getQuestions().getChildren();
                                    int childrenCount = questionList.size();
                                    for (int j = 0; j < childrenCount; j++) {
                                        //add by v1.1 未作答的题目进入错题集
                                        if (!questionList.get(j).getAnswerBean().isRight() || questionList.get(j).getAnswerBean().getStatus() == AnswerBean.ANSER_WRONG || questionList.get(j).getAnswerBean().getStatus() == AnswerBean.ANSER_UNFINISH) {
                                            isFalse = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            //add by v1.1 未作答的题目进入错题集
                            if (isFalse || (mPaperTestEntity.getQuestions() != null && (mPaperTestEntity.getQuestions().getAnswerBean().getStatus() == AnswerBean.ANSER_WRONG) || mPaperTestEntity.getQuestions().getAnswerBean().getStatus() == AnswerBean.ANSER_UNFINISH)) {
                                String mPaperTestStr = JSON.toJSONString(mPaperTestEntity);
                                values.put("qid", mPaperTestEntity.getQid());
//                                values.put("pid", mPaperTestEntity.getPid());
                                values.put("questionJsonStr", mPaperTestStr);

                                mBean = new PublicErrorQuestionCollectionBean();
                                mBean.setStageId(mExercisesDataEntity.getStageid() + "");
                                mBean.setIntelligName(mExercisesDataEntity.getName());
                                mBean.setSubjectId(mExercisesDataEntity.getSubjectid());
                                mBean.setSubjectName(mExercisesDataEntity.getSubjectName());

                                mBean.setIsChapterSection(mExercisesDataEntity.getIsChapterSection());
                                if(mExercisesDataEntity.getIsChapterSection() == 1){
                                    mBean.setEditionId("0");
                                    mBean.setVolumeId("0");
                                } else {
                                    mBean.setEditionId(mExercisesDataEntity.getBedition() + "");
                                    mBean.setVolumeId(mExercisesDataEntity.getVolume() + "");
                                }
                                mBean.setEditionName(mExercisesDataEntity.getEditionName());
                                mBean.setVolumeName(mExercisesDataEntity.getVolumeName());

                                mBean.setChapterId(mExercisesDataEntity.getChapterid() + "");
                                mBean.setChapterName(mExercisesDataEntity.getChapterName());
                                mBean.setSectionId(mExercisesDataEntity.getSectionid() + "");
                                mBean.setSectionName(mExercisesDataEntity.getSectionName());

                                mBean.setUniteId(mExercisesDataEntity.getCellid());
                                mBean.setUniteName(mExercisesDataEntity.getCellName());

                                mBean.setQid(mPaperTestEntity.getQid() + "");
                                mBean.setQuestionJsonStr(mPaperTestStr);

                                int updateNum = DataSupport.updateAll(PublicErrorQuestionCollectionBean.class, values,
                                        "stageId = ? and subjectId = ? and editionId = ? and volumeId = ? and chapterId = ? and chapterName = ? " +
                                                "and sectionid = ? and sectionName = ? and uniteId = ? and uniteName = ? and isChapterSection = ?  and qid = ?",
                                        mBean.getStageId(), mBean.getSubjectId(), mBean.getEditionId(), mBean.getVolumeId(),
                                        mBean.getChapterId(), mBean.getChapterName(), mBean.getSectionId(), mBean.getSectionName(), mBean
                                                .getUniteId(), mBean.getUniteName(), mBean.getIsChapterSection()+"", mBean
                                                .getQid());
                                if (updateNum <= 0) {
                                    boolean isSaved = mBean.save();
                                    LogInfo.log("haitian", ">>>>>>----mBean to isSaved ="+ isSaved);
                                }
                                LogInfo.log("haitian", ">>>>>>----mBean to string ="+updateNum+"---" + mBean.toString() +
                                        "\r\n--------------------------------------------");
                            }
                        }
                    }
                }
                return null;
            }

            @Override
            public void onPostExecute(YanxiuBaseBean result) {

            }
        }.start();

    }

    /**
     * 获取学科错题信息
     *
     * @param stageId
     * @return
     */
    public static ArrayList<DataTeacherEntity> findDataListToSubjectEntity(String stageId) {
        List<PublicErrorQuestionCollectionBean> subjectList = DataSupport.select("stageId", "subjectId", "subjectName", "editionId", "editionName", "volumeId", "volumeName").
                where("stageId = ? group by subjectId", stageId).find(PublicErrorQuestionCollectionBean.class);
        if (subjectList != null && subjectList.size() > 0) {
            ArrayList<DataTeacherEntity> dataList = new ArrayList<DataTeacherEntity>();
            DataTeacherEntity mDataTeacherEntity = null;
            DataWrongNumEntity mDataWrongNumEntity = null;
            for (PublicErrorQuestionCollectionBean mBean : subjectList) {
                mDataTeacherEntity = new DataTeacherEntity();
                mDataTeacherEntity.setId(mBean.getSubjectId());
                mDataTeacherEntity.setName(mBean.getSubjectName());
                mDataTeacherEntity.setVolumeId(mBean.getVolumeId());
                mDataTeacherEntity.setVolumeName(mBean.getVolumeName());
                mDataWrongNumEntity = new DataWrongNumEntity();
                mDataWrongNumEntity.setEditionId(mBean.getEditionId());
                mDataWrongNumEntity.setEditionName(mBean.getEditionName());
                int num = getSubjectErrorNum(mBean.getStageId(), mBean.getSubjectId());
                mDataWrongNumEntity.setWrongNum(num + "");
                mDataTeacherEntity.setData(mDataWrongNumEntity);
                dataList.add(mDataTeacherEntity);
                LogInfo.log("haitian", ">>>>>>----mBean to string =" + mBean.toString() + "\r\n");
            }
            return dataList;
        } else {
            LogInfo.log("haitian", ">>>>>>----mBean to string is null-----<<<<<<<" + "\r\n");
            return null;
        }
    }

    public static int getSubjectErrorNum(String stageId, String subjectId) {
        return DataSupport.where("stageId = ? and subjectId = ?",
                stageId, subjectId).count(PublicErrorQuestionCollectionBean.class);
    }
    //--------------------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------------------


    /**
     * 获取章列表错题信息
     *
     * @param stageId
     * @param subjectId
     * @param editionId
     * @param volumeId
     * @return
     */
    public static ArrayList<DataTeacherEntity> findDataListToChapterEntity(String stageId, String subjectId, String editionId, String volumeId, int
                                                                        isChapterSection ) {
        if(isChapterSection == 1){
            editionId = "0";
            volumeId = "0";
        }
        List<PublicErrorQuestionCollectionBean> newsList = DataSupport
                .where("stageId = ? and subjectId = ? and editionId = ? and volumeId = ? and isChapterSection = ? group by chapterId",
                        stageId, subjectId, editionId, volumeId, isChapterSection+"").find(PublicErrorQuestionCollectionBean.class);
        if (newsList != null && newsList.size() > 0) {
            ArrayList<DataTeacherEntity> dataList = new ArrayList<DataTeacherEntity>();
            DataTeacherEntity mDataTeacherEntity = null;
            DataWrongNumEntity mDataWrongNumEntity = null;
            for (PublicErrorQuestionCollectionBean mPublicChapterBean : newsList) {
                mDataTeacherEntity = new DataTeacherEntity();
                mDataTeacherEntity.setId(mPublicChapterBean.getChapterId());
                mDataTeacherEntity.setName(mPublicChapterBean.getChapterName());
                mDataTeacherEntity.setVolumeId(mPublicChapterBean.getVolumeId());
                mDataTeacherEntity.setVolumeName(mPublicChapterBean.getVolumeName());
                mDataWrongNumEntity = new DataWrongNumEntity();
                mDataWrongNumEntity.setWrongNum(getChapterOrSectionErrorNum(stageId, subjectId, editionId, volumeId, mPublicChapterBean
                        .getChapterId(), "0", "0", isChapterSection) + "");
                mDataTeacherEntity.setData(mDataWrongNumEntity);
                ArrayList<DataTeacherEntity> sectionList = findDataToSectionEntity(stageId, subjectId, editionId, volumeId, mPublicChapterBean.getChapterId(),
                        isChapterSection);
                if(sectionList != null && sectionList.size() > 0){
                    mDataTeacherEntity.setChildren(sectionList);
                }
                dataList.add(mDataTeacherEntity);
                LogInfo.log("haitian", ">>>>>>---findDataListToChapterEntity -mBean to string =" + mDataTeacherEntity.toString() + "\r\n");
            }
            return dataList;
        } else {
            return null;
        }
    }
    /**
     *获取章或节收藏数
     * @param stageId
     * @param subjectId
     * @param editionId
     * @param volumeId
     * @param chapterId
     * @param sectionId
     * @param isChapterSection
     * @return
     */
    public static int getChapterOrSectionErrorNum(String stageId, String subjectId, String editionId, String volumeId, String chapterId, String
            sectionId, String uniteId, int isChapterSection) {
        if(isChapterSection == 1){
            editionId = "0";
            volumeId = "0";
        }
        return DataSupport.where("stageId = ? and subjectId = ? and editionId = ? and volumeId = ? and chapterId = ? and sectionId = ? and " +
                        "uniteId = ? and isChapterSection = ?",
                stageId, subjectId, editionId, volumeId, chapterId, sectionId, uniteId, isChapterSection+"").count(PublicErrorQuestionCollectionBean
                .class);
    }
    /**
     * 获取单元收藏数
     * @param stageId
     * @param subjectId
     * @param editionId
     * @param volumeId
     * @param chapterId
     * @param sectionId
     * @param uniteId
     * @return
     */
    public static int getUniteErrorNum(String stageId, String subjectId, String editionId, String volumeId, String chapterId, String sectionId,
                                       String uniteId, int isChapterSection) {
        if(isChapterSection == 1){
            editionId = "0";
            volumeId = "0";
        }
        return DataSupport.where("stageId = ? and subjectId = ? and editionId = ? and volumeId = ? and chapterId = ? and sectionId = ? and uniteId " +
                        "= ? and isChapterSection = ?",
                stageId, subjectId, editionId, volumeId, chapterId, sectionId, uniteId, isChapterSection+"").count(PublicErrorQuestionCollectionBean.class);
    }
    /**
     * 获取解列表错题信息
     *
     * @param stageId
     * @param subjectId
     * @param editionId
     * @param volumeId
     * @param chapterId
     * @return
     */
    public static ArrayList<DataTeacherEntity> findDataToSectionEntity(String stageId, String subjectId, String editionId, String volumeId, String
            chapterId, int isChapterSection) {
        if(isChapterSection == 1){
            editionId = "0";
            volumeId = "0";
        }
        List<PublicErrorQuestionCollectionBean> newsList = DataSupport
                .where("stageId = ? and subjectId = ? and editionId = ? and volumeId = ? and chapterId = ? and isChapterSection = ? group by " +
                                "sectionId",
                        stageId, subjectId, editionId, volumeId, chapterId, isChapterSection+"").find(PublicErrorQuestionCollectionBean.class);
        if (newsList != null && newsList.size() > 0) {
            ArrayList<DataTeacherEntity> dataList = new ArrayList<DataTeacherEntity>();
            DataTeacherEntity mDataTeacherEntity = null;
            DataWrongNumEntity mDataWrongNumEntity = null;
            for (PublicErrorQuestionCollectionBean mPublicSectionBean : newsList) {
                mDataTeacherEntity = new DataTeacherEntity();
                if (!"0".equals(mPublicSectionBean.getSectionId())) {
                    mDataTeacherEntity.setId(mPublicSectionBean.getSectionId());
                    mDataTeacherEntity.setName(mPublicSectionBean.getSectionName());
                    mDataTeacherEntity.setVolumeId(mPublicSectionBean.getVolumeId());
                    mDataTeacherEntity.setVolumeName(mPublicSectionBean.getVolumeName());
                    mDataWrongNumEntity = new DataWrongNumEntity();
                    mDataWrongNumEntity.setWrongNum(getChapterOrSectionErrorNum(stageId, subjectId, editionId, volumeId, chapterId,
                            mPublicSectionBean.getSectionId(), "0", isChapterSection) + "");
                    mDataTeacherEntity.setData(mDataWrongNumEntity);
                    ArrayList<DataTeacherEntity> uniteList = findDataToUniteEntity(stageId, subjectId, editionId, volumeId, chapterId, mPublicSectionBean
                            .getSectionId(), isChapterSection);
                    if(uniteList != null && uniteList.size() > 0) {
                        mDataTeacherEntity.setChildren(uniteList);
                    }
                    dataList.add(mDataTeacherEntity);
                    LogInfo.log("haitian", ">>>>>>---findDataToSectionEntity -mBean to string =" + mDataTeacherEntity.toString() + "\r\n");
                }
            }
            return dataList;
        } else {
            return null;
        }
    }
    /**
     * 获取单元列表收藏信息
     * @param stageId
     * @param subjectId
     * @param editionId
     * @param volumeId
     * @param chapterId
     * @param sectionId
     * @return
     */
    public static ArrayList<DataTeacherEntity> findDataToUniteEntity(String stageId, String subjectId, String editionId, String volumeId, String
            chapterId, String sectionId, int isChapterSection) {
        if(isChapterSection == 1){
            editionId = "0";
            volumeId = "0";
        }
        List<PublicErrorQuestionCollectionBean> newsList = DataSupport
                .where("stageId = ? and subjectId = ? and editionId = ? and volumeId = ? and chapterId = ? and sectionId = ? and isChapterSection =" +
                                " ? " +
                                "group by uniteId",
                        stageId, subjectId, editionId, volumeId, chapterId, sectionId, isChapterSection+"").find(PublicErrorQuestionCollectionBean.class);
        if (newsList != null && newsList.size() > 0) {
            ArrayList<DataTeacherEntity> dataList = new ArrayList<DataTeacherEntity>();
            DataTeacherEntity mDataTeacherEntity = null;
            DataWrongNumEntity mDataWrongNumEntity = null;
            for (PublicErrorQuestionCollectionBean mPublicSectionBean : newsList) {
                if (!"0".equals(mPublicSectionBean.getUniteId())) {
                    mDataTeacherEntity = new DataTeacherEntity();
                    mDataTeacherEntity.setId(mPublicSectionBean.getUniteId());
                    mDataTeacherEntity.setName(mPublicSectionBean.getUniteName());
                    mDataTeacherEntity.setVolumeId(mPublicSectionBean.getVolumeId());
                    mDataTeacherEntity.setVolumeName(mPublicSectionBean.getVolumeName());
                    mDataWrongNumEntity = new DataWrongNumEntity();
                    mDataWrongNumEntity.setWrongNum(getUniteErrorNum(stageId, subjectId, editionId, volumeId, chapterId, sectionId, mPublicSectionBean
                            .getUniteId(), isChapterSection) + "");
                    mDataTeacherEntity.setData(mDataWrongNumEntity);
                    dataList.add(mDataTeacherEntity);
                    LogInfo.log("haitian", ">>>>>>---findDataToUniteEntity -mBean to string =" + mDataTeacherEntity.toString() + "\r\n");

                }
            }
            return dataList;
        } else {
            return null;
        }
    }

    public String getIntelligName() {
        return intelligName;
    }

    public void setIntelligName(String intelligName) {
        this.intelligName = intelligName;
    }

    public String getChapterWrongNum() {
        return chapterWrongNum;
    }

    public void setChapterWrongNum(String chapterWrongNum) {
        this.chapterWrongNum = chapterWrongNum;
    }

    public String getSectionWrongNum() {
        return sectionWrongNum;
    }

    public void setSectionWrongNum(String sectionWrongNum) {
        this.sectionWrongNum = sectionWrongNum;
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

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public int getIsChapterSection() {
        return isChapterSection;
    }

    public void setIsChapterSection(int isChapterSection) {
        this.isChapterSection = isChapterSection;
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

    public String getQuestionJsonStr() {
        return questionJsonStr;
    }

    public void setQuestionJsonStr(String questionJsonStr) {
        this.questionJsonStr = questionJsonStr;
    }


}
