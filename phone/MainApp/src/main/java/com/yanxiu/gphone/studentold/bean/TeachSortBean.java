package com.yanxiu.gphone.studentold.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/7/9.
 * 页面  移动server-学生作业 Home  1.0 智能练习主界面接口
 1.0 选择教材版本接口
 *
 */
public class TeachSortBean extends SrtBaseBean {

    /**
     * body : [{"edition":{"eid":"xxxxxx","ename":"人教版","grade":[{"gid":"123456","name":"必修1"},{"gid":"123456","name":"必修2"},{"gid":"123456","name":"必修3"}]}},{"edition":{"eid":"xxxxxx","ename":"人教版","grade":[{"gid":"123456","name":"必修1"},{"gid":"123456","name":"必修2"},{"gid":"123456","name":"必修3"}]}}]
     * status : {"code":"0","desc":"cheng gong"}
     */
    private List<BodyEntity> body;
    private StatusEntity status;

    public void setBody(List<BodyEntity> body) {
        this.body = body;
    }

    public void setStatus(StatusEntity status) {
        this.status = status;
    }

    public List<BodyEntity> getBody() {
        return body;
    }

    public StatusEntity getStatus() {
        return status;
    }

    public class BodyEntity {
        /**
         * edition : {"eid":"xxxxxx","ename":"人教版","grade":[{"gid":"123456","name":"必修1"},{"gid":"123456","name":"必修2"},{"gid":"123456","name":"必修3"}]}
         */
        private EditionEntity edition;

        public void setEdition(EditionEntity edition) {
            this.edition = edition;
        }

        public EditionEntity getEdition() {
            return edition;
        }

        public class EditionEntity {
            /**
             * eid : xxxxxx
             * ename : 人教版
             * grade : [{"gid":"123456","name":"必修1"},{"gid":"123456","name":"必修2"},{"gid":"123456","name":"必修3"}]
             */
            private String eid;
            private String ename;
            private List<GradeEntity> grade;

            public void setEid(String eid) {
                this.eid = eid;
            }

            public void setEname(String ename) {
                this.ename = ename;
            }

            public void setGrade(List<GradeEntity> grade) {
                this.grade = grade;
            }

            public String getEid() {
                return eid;
            }

            public String getEname() {
                return ename;
            }

            public List<GradeEntity> getGrade() {
                return grade;
            }

            public class GradeEntity {
                /**
                 * gid : 123456
                 * name : 必修1
                 */
                private String gid;
                private String name;

                public void setGid(String gid) {
                    this.gid = gid;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getGid() {
                    return gid;
                }

                public String getName() {
                    return name;
                }
            }
        }
    }

    public class StatusEntity {
        /**
         * code : 0
         * desc : cheng gong
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
}
