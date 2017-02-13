package com.yanxiu.gphone.student.bean;


import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.List;

/**
 * Created by Administrator on 2015/7/31.
 */
public class PadBean implements YanxiuBaseBean {

    /**
     * uid : 28
     * costtime : 0
     * answer :
     * id : 369
     * ptid : 620
     * status : 0
     */
    private int uid;
    private int costtime;
    private String jsonAnswer;
    private int id = -1;
    private int ptid;
    private int status;

    private List<AudioCommentBean> jsonAudioComment;
    /**
     * teachercheck : {"score":4,"qcomment":"good","checktime":1442633365526,"padid":12456,"pid":2345678,"id":234,"tid":34567789}
     */
    private TeachercheckEntity teachercheck;

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getJsonAnswer() {
        return jsonAnswer;
    }

    public void setJsonAnswer(String jsonAnswer) {
        this.jsonAnswer = jsonAnswer;
    }

    public void setCosttime(int costtime) {
        this.costtime = costtime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPtid(int ptid) {
        this.ptid = ptid;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUid() {
        return uid;
    }

    public int getCosttime() {
        return costtime;
    }

    public int getId() {
        return id;
    }

    public int getPtid() {
        return ptid;
    }

    public int getStatus() {
        return status;
    }

    public void setTeachercheck(TeachercheckEntity teachercheck) {
        this.teachercheck = teachercheck;
    }

    public TeachercheckEntity getTeachercheck() {
        return teachercheck;
    }

    public List<AudioCommentBean> getJsonAudioComment() {
        return jsonAudioComment;
    }

    public void setJsonAudioComment(List<AudioCommentBean> jsonAudioComment) {
        this.jsonAudioComment = jsonAudioComment;
    }

    public class TeachercheckEntity implements YanxiuBaseBean {
        /**
         * score : 4
         * qcomment : good
         * checktime : 1442633365526
         * padid : 12456
         * pid : 2345678
         * id : 234
         * tid : 34567789
         */
        private int score;
        private String qcomment;
        private long checktime;
        private int padid;
        private int pid;
        private int id;
        private int tid;

        public void setScore(int score) {
            this.score = score;
        }

        public void setQcomment(String qcomment) {
            this.qcomment = qcomment;
        }

        public void setChecktime(long checktime) {
            this.checktime = checktime;
        }

        public void setPadid(int padid) {
            this.padid = padid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setTid(int tid) {
            this.tid = tid;
        }

        public int getScore() {
            return score;
        }

        public String getQcomment() {
            return qcomment;
        }

        public long getChecktime() {
            return checktime;
        }

        public int getPadid() {
            return padid;
        }

        public int getPid() {
            return pid;
        }

        public int getId() {
            return id;
        }

        public int getTid() {
            return tid;
        }

        @Override
        public String toString() {
            return "TeachercheckEntity{" +
                    "score=" + score +
                    ", qcomment='" + qcomment + '\'' +
                    ", checktime=" + checktime +
                    ", padid=" + padid +
                    ", pid=" + pid +
                    ", id=" + id +
                    ", tid=" + tid +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PadBean{" +
                "uid=" + uid +
                ", costtime=" + costtime +
                ", jsonAnswer='" + jsonAnswer + '\'' +
                ", id=" + id +
                ", ptid=" + ptid +
                ", status=" + status +
                ", teachercheck=" + teachercheck +
                '}';
    }
}
