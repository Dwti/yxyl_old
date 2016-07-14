package com.yanxiu.gphone.student.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/7/7.
 */
public class ExercisesBean extends SrtBaseBean {
    /**
     * examinationlist : [{"chapterid":"12345","chaptername":"第一章 集合与函数概念","sections":[{"sectionid":"12345","sectionname":"1.1函数与方程"},{"sectionid":"12345","sectionname":"1.2函数与方程"},{"sectionid":"12345","sectionname":"1.3函数与方程"},{"sectionid":"12345","sectionname":"1.4函数与方程"}]},{"chapterid":"12345","chaptername":"第二章 集合与函数概念","sections":[{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"}]},{"chapterid":"12345","chaptername":"第三章 集合与函数概念","sections":[{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"}]},{"chapterid":"12345","chaptername":"第四章 集合与函数概念","sections":[{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"}]},{"chapterid":"12345","chaptername":"第四章 集合与函数概念","sections":[{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"}]},{"chapterid":"12345","chaptername":"第五章 集合与函数概念","sections":[{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"}]},{"chapterid":"12345","chaptername":"第六章 集合与函数概念","sections":[{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"}]},{"chapterid":"12345","chaptername":"第七章 集合与函数概念","sections":[{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"}]},{"chapterid":"12345","chaptername":"第八章 集合与函数概念","sections":[{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"}]},{"chapterid":"12345","chaptername":"第九章 集合与函数概念","sections":[{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"}]},{"chapterid":"12345","chaptername":"第十章 集合与函数概念","sections":[{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"},{"sectionid":"12345","sectionname":"3.1函数与方程"}]}]
     * status : {"code":"0","desc":"返回成功"}
     */
    private List<ExaminationlistEntity> examinationlist;
    private StatusEntity status;

    public void setExaminationlist(List<ExaminationlistEntity> examinationlist) {
        this.examinationlist = examinationlist;
    }

    public void setStatus(StatusEntity status) {
        this.status = status;
    }

    public List<ExaminationlistEntity> getExaminationlist() {
        return examinationlist;
    }

    public StatusEntity getStatus() {
        return status;
    }

    public class ExaminationlistEntity {
        /**
         * chapterid : 12345
         * chaptername : 第一章 集合与函数概念
         * sections : [{"sectionid":"12345","sectionname":"1.1函数与方程"},{"sectionid":"12345","sectionname":"1.2函数与方程"},{"sectionid":"12345","sectionname":"1.3函数与方程"},{"sectionid":"12345","sectionname":"1.4函数与方程"}]
         */
        private String chapterid;
        private String chaptername;
        private List<SectionsEntity> sections;

        public void setChapterid(String chapterid) {
            this.chapterid = chapterid;
        }

        public void setChaptername(String chaptername) {
            this.chaptername = chaptername;
        }

        public void setSections(List<SectionsEntity> sections) {
            this.sections = sections;
        }


        public String getChapterid() {
            return chapterid;
        }

        public String getChaptername() {
            return chaptername;
        }
        public List<SectionsEntity> getSections() {
            return sections;
        }

        public class SectionsEntity {
            /**
             * sectionid : 12345
             * sectionname : 1.1函数与方程
             */
            private String sectionid;
            private String sectionname;

            public void setSectionid(String sectionid) {
                this.sectionid = sectionid;
            }

            public void setSectionname(String sectionname) {
                this.sectionname = sectionname;
            }

            public String getSectionid() {
                return sectionid;
            }

            public String getSectionname() {
                return sectionname;
            }
        }
    }

    public class StatusEntity {
        /**
         * code : 0
         * desc : 返回成功
         */
        private String code;
        private String desc;

        public void setCode(String code) {
            this.code = code;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

//    private Status status;
//
//    public Status getStatus() {
//        return status;
//    }
//
//    public void setStatus(Status status) {
//        this.status = status;
//    }
//
//    public class Status extends SrtBaseBean{
//        private String code;
//        private String desc;
//
//        public String getDesc() {
//            return desc;
//        }
//
//        public void setDesc(String desc) {
//            this.desc = desc;
//        }
//
//        public String getCode() {
//            return code;
//        }
//
//        public void setCode(String code) {
//            this.code = code;
//        }
//    }
//
//    private ArrayList<ChapterList> examinationlist;
//
//    public ArrayList<ChapterList> getExaminationlist() {
//        return examinationlist;
//    }
//
//    public void setExaminationlist(ArrayList<ChapterList> examinationlist) {
//        this.examinationlist = examinationlist;
//    }
//
//    public class ChapterList extends SrtBaseBean{
//        private String chaptername;
//        private String chapterid;
//        private ArrayList<Sections> sections = new ArrayList<ExercisesBean.Sections>();
//
//        public String getChaptername() {
//            return chaptername;
//        }
//
//        public void setChaptername(String chaptername) {
//            this.chaptername = chaptername;
//        }
//
//        public String getChapterid() {
//            return chapterid;
//        }
//
//        public void setChapterid(String chapterid) {
//            this.chapterid = chapterid;
//        }
//
//        public ArrayList<Sections> getSections() {
//            return sections;
//        }
//
//        public void setSections(ArrayList<Sections> sections) {
//            this.sections = sections;
//        }
//
//    }
//    public class Sections extends SrtBaseBean{
//        private String sectionname;
//        private String sectionid;
//
//        public String getSectionname() {
//            return sectionname;
//        }
//
//        public void setSectionname(String sectionname) {
//            this.sectionname = sectionname;
//        }
//
//        public String getSectionid() {
//            return sectionid;
//        }
//
//        public void setSectionid(String sectionid) {
//            this.sectionid = sectionid;
//        }
//    }



}
