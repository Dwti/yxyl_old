package com.yanxiu.gphone.hd.student.bean;

/**
 * Created by Administrator on 2015/7/20.
 */
public class GroupHwBean extends SrtBaseBean{

    /**
     * id : 17265
     * ptype : 1
     * authorid : 10364884
     * name : 0203_语文_作业
     * subjectid : 1102
     * bedition : 1402
     * stageid : 1203
     * buildtime : 1454472988000
     * begintime : 1454428800000
     * volume : 64108
     * chapterid : 64109
     * sectionid : 0
     * quesnum : 5
     * answernum : 3
     * remaindertimeStr : 2小时
     * endtime : 1454483400000
     * status : 1
     * showana : 0
     * overTime : 02月03日15:10
     * isEnd : 1
     * subquesnum : 0
     */
    private PaperStatusBean paperStatus;
    private String id;
    private int ptype;
    private String authorid;
    private String name;
    private String subjectid;
    private String bedition;
    private String stageid;
    private long buildtime;
    private long begintime;
    private String volume;
    private String chapterid;
    private int sectionid;
    private int quesnum;
    private int answernum;
    private String remaindertimeStr;
    private long endtime;
    private int status;
    private int showana;
    private String overTime;
    private int isEnd;
    private int subquesnum;

    public PaperStatusBean getPaperStatus() {
        return paperStatus;
    }

    public void setPaperStatus(PaperStatusBean paperStatus) {
        this.paperStatus = paperStatus;
    }

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public int getPtype () {
        return ptype;
    }

    public void setPtype (int ptype) {
        this.ptype = ptype;
    }

    public String getAuthorid () {
        return authorid;
    }

    public void setAuthorid (String authorid) {
        this.authorid = authorid;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getSubjectid () {
        return subjectid;
    }

    public void setSubjectid (String subjectid) {
        this.subjectid = subjectid;
    }

    public String getBedition () {
        return bedition;
    }

    public void setBedition (String bedition) {
        this.bedition = bedition;
    }

    public String getStageid () {
        return stageid;
    }

    public void setStageid (String stageid) {
        this.stageid = stageid;
    }

    public long getBuildtime () {
        return buildtime;
    }

    public void setBuildtime (long buildtime) {
        this.buildtime = buildtime;
    }

    public long getBegintime () {
        return begintime;
    }

    public void setBegintime (long begintime) {
        this.begintime = begintime;
    }

    public String getVolume () {
        return volume;
    }

    public void setVolume (String volume) {
        this.volume = volume;
    }

    public String getChapterid () {
        return chapterid;
    }

    public void setChapterid (String chapterid) {
        this.chapterid = chapterid;
    }

    public int getSectionid () {
        return sectionid;
    }

    public void setSectionid (int sectionid) {
        this.sectionid = sectionid;
    }

    public int getQuesnum () {
        return quesnum;
    }

    public void setQuesnum (int quesnum) {
        this.quesnum = quesnum;
    }

    public int getAnswernum () {
        return answernum;
    }

    public void setAnswernum (int answernum) {
        this.answernum = answernum;
    }

    public String getRemaindertimeStr () {
        return remaindertimeStr;
    }

    public void setRemaindertimeStr (String remaindertimeStr) {
        this.remaindertimeStr = remaindertimeStr;
    }

    public long getEndtime () {
        return endtime;
    }

    public void setEndtime (long endtime) {
        this.endtime = endtime;
    }

    public int getStatus () {
        return status;
    }

    public void setStatus (int status) {
        this.status = status;
    }

    public int getShowana () {
        return showana;
    }

    public void setShowana (int showana) {
        this.showana = showana;
    }

    public String getOverTime () {
        return overTime;
    }

    public void setOverTime (String overTime) {
        this.overTime = overTime;
    }

    public int getIsEnd () {
        return isEnd;
    }

    public void setIsEnd (int isEnd) {
        this.isEnd = isEnd;
    }

    public int getSubquesnum () {
        return subquesnum;
    }

    public void setSubquesnum (int subquesnum) {
        this.subquesnum = subquesnum;
    }
}
