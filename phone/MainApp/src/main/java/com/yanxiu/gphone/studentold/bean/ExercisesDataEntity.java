package com.yanxiu.gphone.studentold.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.List;

/**
 * Created by Administrator on 2015/7/17.
 */
public class ExercisesDataEntity implements YanxiuBaseBean {
    //章节 收藏
    public static final int CHAPTER = 0;
    //考点 收藏
    public static final int TEST_CENTER = 1;
    //考点是否点击
    private boolean isTestCenterOnclick = false;
    private int gradeid;
    private long endtime;
    private int ptype;
    private long begintime;
    private String sectionid;
    private String sectionName;
    private int authorid;
    private String subjectid;
    private String subjectName;

    private String volume;
    private String volumeName;
    private long buildtime;
    private String chapterid;
    private String chapterName;
    private String bedition;
    private String editionName;
    private String name;
    private String cellid = "0";
    private String cellName;

    private int isChapterSection = 0;//0: 代表章节收藏; 1:代表考点收藏
    private int quesnum;
    private List<PaperTestEntity> paperTest;
    private int id;
    private PaperStatusBean paperStatus = new PaperStatusBean();
    private String stageid;
    private String stageName;
    private int status;

    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
    }

    public String getEditionName() {
        return editionName;
    }

    public void setEditionName(String editionName) {
        this.editionName = editionName;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public boolean isTestCenterOnclick() {
        return isTestCenterOnclick;
    }

    public void setIsTestCenterOnclick(boolean isTestCenterOnclick) {
        this.isTestCenterOnclick = isTestCenterOnclick;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public void setGradeid(int gradeid) {
        this.gradeid = gradeid;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }

    public void setPtype(int ptype) {
        this.ptype = ptype;
    }

    public void setBegintime(long begintime) {
        this.begintime = begintime;
    }

    public void setSectionid(String sectionid) {
        this.sectionid = sectionid;
    }

    public void setAuthorid(int authorid) {
        this.authorid = authorid;
    }

    public void setSubjectid(String subjectid) {
        this.subjectid = subjectid;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public void setBuildtime(long buildtime) {
        this.buildtime = buildtime;
    }

    public void setChapterid(String chapterid) {
        this.chapterid = chapterid;
    }

    public void setBedition(String bedition) {
        this.bedition = bedition;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuesnum(int quesnum) {
        this.quesnum = quesnum;
    }

    public void setPaperTest(List<PaperTestEntity> paperTest) {
        this.paperTest = paperTest;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPaperStatus(PaperStatusBean paperStatus) {
        this.paperStatus = paperStatus;
    }

    public void setStageid(String stageid) {
        this.stageid = stageid;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getGradeid() {
        return gradeid;
    }

    public long getEndtime() {
        return endtime;
    }

    public int getPtype() {
        return ptype;
    }

    public long getBegintime() {
        return begintime;
    }

    public String getSectionid() {
        return sectionid;
    }

    public int getAuthorid() {
        return authorid;
    }

    public String getSubjectid() {
        return subjectid;
    }

    public String getVolume() {
        return volume;
    }

    public long getBuildtime() {
        return buildtime;
    }

    public String getChapterid() {
        return chapterid;
    }

    public String getBedition() {
        return bedition;
    }

    public String getName() {
        return name;
    }

    public int getQuesnum() {
        return quesnum;
    }

    public List<PaperTestEntity> getPaperTest() {
        return paperTest;
    }

    public int getId() {
        return id;
    }

    public PaperStatusBean getPaperStatus() {
        return paperStatus;
    }

    public String getStageid() {
        return stageid;
    }

    public int getStatus() {
        return status;
    }

    public int getIsChapterSection() {
        return isChapterSection;
    }

    public void setIsChapterSection(int isChapterSection) {
        this.isChapterSection = isChapterSection;
    }

    public String getCellid() {
        return cellid;
    }

    public void setCellid(String cellid) {
        this.cellid = cellid;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    @Override
    public String toString() {
        return "ExercisesDataEntity{" +
                "gradeid=" + gradeid +
                ", endtime=" + endtime +
                ", ptype=" + ptype +
                ", begintime=" + begintime +
                ", sectionid='" + sectionid + '\'' +
                ", sectionName='" + sectionName + '\'' +
                ", authorid=" + authorid +
                ", subjectid='" + subjectid + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", volume='" + volume + '\'' +
                ", volumeName='" + volumeName + '\'' +
                ", buildtime=" + buildtime +
                ", chapterid='" + chapterid + '\'' +
                ", chapterName='" + chapterName + '\'' +
                ", bedition='" + bedition + '\'' +
                ", editionName='" + editionName + '\'' +
                ", name='" + name + '\'' +
                ", quesnum=" + quesnum +
                ", id=" + id +
                ", stageid='" + stageid + '\'' +
                ", stageName='" + stageName + '\'' +
                ", uniteId='" + cellid + '\'' +
                ", uniteName='" + cellName + '\'' +
                ", status=" + status +
                '}';
    }
}
