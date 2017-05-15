package com.yanxiu.gphone.studentold.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/7 11:25.
 * Function :
 */

public class MistakeRedoNumberBean implements YanxiuBaseBean {

    private DataStatusEntityBean status;

    private ProPerty property;

    public class ProPerty{
        private int questionNum;

        public int getQuestionNum() {
            return questionNum;
        }

        public void setQuestionNum(int questionNum) {
            this.questionNum = questionNum;
        }
    }

    public DataStatusEntityBean getStatus() {
        return status;
    }

    public void setStatus(DataStatusEntityBean status) {
        this.status = status;
    }

    public ProPerty getProperty() {
        return property;
    }

    public void setProperty(ProPerty property) {
        this.property = property;
    }
}
