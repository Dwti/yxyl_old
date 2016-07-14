package com.yanxiu.gphone.parent.bean;

import java.util.List;

/**
 * Created by lidongming on 16/3/28.
 */
public class ParentHomeSubjectInfoBean extends ParentRequestBean{

    /**
     * uid : 32
     * pid : 17656
     * detailJson : {"teacherComment":"哈哈","subjectName":"数学","classAvgCostTime":13,"costTimeBeatNum":1,"classMinCostTime":2,"subjectId":1103,"paperCostTime":5,"pid":17656,"paperRate":0.4,"classAvgRate":0.46666667,"submitRank":1,"rateBeatNum":1,"classMaxRate":0.7}
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private int uid;
        private int pid;
        /**
         * teacherComment : 哈哈
         * subjectName : 数学
         * classAvgCostTime : 13
         * costTimeBeatNum : 1
         * classMinCostTime : 2
         * subjectId : 1103
         * paperCostTime : 5
         * pid : 17656
         * paperRate : 0.4
         * classAvgRate : 0.46666667
         * submitRank : 1
         * rateBeatNum : 1
         * classMaxRate : 0.7
         */

        private DetailJsonBean detailJson;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public DetailJsonBean getDetailJson() {
            return detailJson;
        }

        public void setDetailJson(DetailJsonBean detailJson) {
            this.detailJson = detailJson;
        }

        public static class DetailJsonBean {
            private String teacherComment;
            private String subjectName;
            private int classAvgCostTime;
            private int costTimeBeatNum;
            private int classMinCostTime;
            private int subjectId;
            private int paperCostTime;
            private int pid;
            private double paperRate;
            private double classAvgRate;
            private int submitRank;
            private int rateBeatNum;
            private double classMaxRate;

            public String getTeacherComment() {
                return teacherComment;
            }

            public void setTeacherComment(String teacherComment) {
                this.teacherComment = teacherComment;
            }

            public String getSubjectName() {
                return subjectName;
            }

            public void setSubjectName(String subjectName) {
                this.subjectName = subjectName;
            }

            public int getClassAvgCostTime() {
                return classAvgCostTime;
            }

            public void setClassAvgCostTime(int classAvgCostTime) {
                this.classAvgCostTime = classAvgCostTime;
            }

            public int getCostTimeBeatNum() {
                return costTimeBeatNum;
            }

            public void setCostTimeBeatNum(int costTimeBeatNum) {
                this.costTimeBeatNum = costTimeBeatNum;
            }

            public int getClassMinCostTime() {
                return classMinCostTime;
            }

            public void setClassMinCostTime(int classMinCostTime) {
                this.classMinCostTime = classMinCostTime;
            }

            public int getSubjectId() {
                return subjectId;
            }

            public void setSubjectId(int subjectId) {
                this.subjectId = subjectId;
            }

            public int getPaperCostTime() {
                return paperCostTime;
            }

            public void setPaperCostTime(int paperCostTime) {
                this.paperCostTime = paperCostTime;
            }

            public int getPid() {
                return pid;
            }

            public void setPid(int pid) {
                this.pid = pid;
            }

            public double getPaperRate() {
                return paperRate;
            }

            public void setPaperRate(double paperRate) {
                this.paperRate = paperRate;
            }

            public double getClassAvgRate() {
                return classAvgRate;
            }

            public void setClassAvgRate(double classAvgRate) {
                this.classAvgRate = classAvgRate;
            }

            public int getSubmitRank() {
                return submitRank;
            }

            public void setSubmitRank(int submitRank) {
                this.submitRank = submitRank;
            }

            public int getRateBeatNum() {
                return rateBeatNum;
            }

            public void setRateBeatNum(int rateBeatNum) {
                this.rateBeatNum = rateBeatNum;
            }

            public double getClassMaxRate() {
                return classMaxRate;
            }

            public void setClassMaxRate(double classMaxRate) {
                this.classMaxRate = classMaxRate;
            }
        }
    }
}
