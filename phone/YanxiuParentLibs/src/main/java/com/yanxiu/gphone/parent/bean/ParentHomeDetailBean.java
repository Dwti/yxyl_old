package com.yanxiu.gphone.parent.bean;

import java.util.List;

/**
 * Created by lidongming on 16/3/24.
 * 家长端首页bean
 */
public class ParentHomeDetailBean extends ParentRequestBean {
    /**
     * totalPage : 1
     * pageSize : 10
     * nextPage : 0
     * totalCou : 3
     */

    private PageBean page;
    /**
     * id : 14
     * uid : 32
     * pid : 17656
     * type : 0
     * pushTime : 1458532141000
     * userName : haha哈巴
     * detailJson : {"teacherComment":"哈哈","subjectName":"数学","honorList":[{"honorDesc":"在一次作业中第一次提交并且全对","honorType":2,"honorName":"唯快不破"},{"honorDesc":"连续十次作业全对","honorType":3,"honorName":"连战连胜"}],"subjectId":1103,"pid":17656,"paperRate":0.4,"teacherName":"test","teacherId":10262067,"rateBeatNum":1}
     */

    private List<DataBean> data;

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class PageBean {
        private int totalPage;
        private int pageSize;
        private int nextPage;
        private int totalCou;

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getNextPage() {
            return nextPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
        }

        public int getTotalCou() {
            return totalCou;
        }

        public void setTotalCou(int totalCou) {
            this.totalCou = totalCou;
        }
    }

    public static class DataBean {
        private int id;
        private int uid;
        private int pid;
        private int type;
        private long pushTime;
        private String userName;
        private String formateTime;
        private boolean isFirstTag;

        public String getFormateTime() {
            return formateTime;
        }

        public void setFormateTime(String formateTime) {
            this.formateTime = formateTime;
        }

        public boolean isFirstTag() {
            return isFirstTag;
        }

        public void setIsFirstTag(boolean isFirstTag) {
            this.isFirstTag = isFirstTag;
        }

        /**

         * teacherComment : 哈哈
         * subjectName : 数学
         * honorList : [{"honorDesc":"在一次作业中第一次提交并且全对","honorType":2,"honorName":"唯快不破"},{"honorDesc":"连续十次作业全对","honorType":3,"honorName":"连战连胜"}]
         * subjectId : 1103
         * pid : 17656
         * paperRate : 0.4
         * teacherName : test
         * teacherId : 10262067
         * rateBeatNum : 1
         */

        private DetailJsonBean detailJson;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public long getPushTime() {
            return pushTime;
        }

        public void setPushTime(long pushTime) {
            this.pushTime = pushTime;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
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
            private int subjectId;
            private int pid;
            private int questionNum;
            private double paperRate;
            private String teacherName;
            private int teacherId;
            private int rateBeatNum;
            private String messageText;
            /**
             * honorDesc : 在一次作业中第一次提交并且全对
             * honorType : 2
             * honorName : 唯快不破
             */

            private List<HonorListBean> honorList;

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

            public int getSubjectId() {
                return subjectId;
            }

            public void setSubjectId(int subjectId) {
                this.subjectId = subjectId;
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

            public String getTeacherName() {
                return teacherName;
            }

            public void setTeacherName(String teacherName) {
                this.teacherName = teacherName;
            }

            public int getTeacherId() {
                return teacherId;
            }

            public void setTeacherId(int teacherId) {
                this.teacherId = teacherId;
            }

            public int getRateBeatNum() {
                return rateBeatNum;
            }

            public void setRateBeatNum(int rateBeatNum) {
                this.rateBeatNum = rateBeatNum;
            }

            public List<HonorListBean> getHonorList() {
                return honorList;
            }

            public void setHonorList(List<HonorListBean> honorList) {
                this.honorList = honorList;
            }

            public String getMessageText() {
                return messageText;
            }

            public void setMessageText(String messageText) {
                this.messageText = messageText;
            }



            public static class HonorListBean {
                private String honorDesc;
                private int honorType;
                private String honorName;

                public String getHonorDesc() {
                    return honorDesc;
                }

                public void setHonorDesc(String honorDesc) {
                    this.honorDesc = honorDesc;
                }

                public int getHonorType() {
                    return honorType;
                }

                public void setHonorType(int honorType) {
                    this.honorType = honorType;
                }

                public String getHonorName() {
                    return honorName;
                }

                public void setHonorName(String honorName) {
                    this.honorName = honorName;
                }
            }
        }
    }


//    /**
//     * id : 14
//     * uid : 32
//     * pid : 17656
//     * detail : {"paperRate":0.4,"pid":17656,"rateBeatNum":1,"subjectId":1103,"subjectName":"数学","teacherComment":"哈哈","teacherId":10262067,"teacherName":"test"}
//     * type : 0
//     */
//
//    private List<DataBean> data;
//
//    public List<DataBean> getData() {
//        return data;
//    }
//
//    public void setData(List<DataBean> data) {
//        this.data = data;
//    }
//
//    public static class DataBean {
//        private int id;
//        private int uid;
//        private int pid;
//        /**
//         * paperRate : 0.4
//         * pid : 17656
//         * rateBeatNum : 1
//         * subjectId : 1103
//         * subjectName : 数学
//         * teacherComment : 哈哈
//         * teacherId : 10262067
//         * teacherName : test
//         */
//
//        private DetailBean detail;
//        private int type;
//        private String pushTime;
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public int getUid() {
//            return uid;
//        }
//
//        public void setUid(int uid) {
//            this.uid = uid;
//        }
//
//        public int getPid() {
//            return pid;
//        }
//
//        public void setPid(int pid) {
//            this.pid = pid;
//        }
//
//        public DetailBean getDetail() {
//            return detail;
//        }
//
//        public void setDetail(DetailBean detail) {
//            this.detail = detail;
//        }
//
//        public int getType() {
//            return type;
//        }
//
//        public void setType(int type) {
//            this.type = type;
//        }
//
//        public String getPushTime() {
//            return pushTime;
//        }
//
//        public void setPushTime(String pushTime) {
//            this.pushTime = pushTime;
//        }
//
//        public static class DetailBean {
//            private double paperRate;
//            private int pid;
//            private int rateBeatNum;
//            private int subjectId;
//            private String subjectName;
//            private String teacherComment;
//            private int teacherId;
//            private String teacherName;
//            private String messageText;
//
//            public double getPaperRate() {
//                return paperRate;
//            }
//
//            public void setPaperRate(double paperRate) {
//                this.paperRate = paperRate;
//            }
//
//            public int getPid() {
//                return pid;
//            }
//
//            public void setPid(int pid) {
//                this.pid = pid;
//            }
//
//            public int getRateBeatNum() {
//                return rateBeatNum;
//            }
//
//            public void setRateBeatNum(int rateBeatNum) {
//                this.rateBeatNum = rateBeatNum;
//            }
//
//            public int getSubjectId() {
//                return subjectId;
//            }
//
//            public void setSubjectId(int subjectId) {
//                this.subjectId = subjectId;
//            }
//
//            public String getSubjectName() {
//                return subjectName;
//            }
//
//            public void setSubjectName(String subjectName) {
//                this.subjectName = subjectName;
//            }
//
//            public String getTeacherComment() {
//                return teacherComment;
//            }
//
//            public void setTeacherComment(String teacherComment) {
//                this.teacherComment = teacherComment;
//            }
//
//            public int getTeacherId() {
//                return teacherId;
//            }
//
//            public void setTeacherId(int teacherId) {
//                this.teacherId = teacherId;
//            }
//
//            public String getTeacherName() {
//                return teacherName;
//            }
//
//            public void setTeacherName(String teacherName) {
//                this.teacherName = teacherName;
//            }
//
//            public String getMessageText() {
//                return messageText;
//            }
//
//            public void setMessageText(String messageText) {
//                this.messageText = messageText;
//            }
//        }
//    }
}
