package com.yanxiu.gphone.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by Administrator on 2015/7/31.
 */
public class ExtendEntity  implements YanxiuBaseBean {
    private int id;
    private DataEntity data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public class DataEntity  implements YanxiuBaseBean{
        private String globalStatis;
        private String answerCompare;

        public String getGlobalStatis() {
            return globalStatis;
        }

        public void setGlobalStatis(String globalStatis) {
            this.globalStatis = globalStatis;
        }

        public String getAnswerCompare() {
            return answerCompare;
        }

        public void setAnswerCompare(String answerCompare) {
            this.answerCompare = answerCompare;
        }
    }
}
