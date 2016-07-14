package com.yanxiu.gphone.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.List;

/**
 * Created by Administrator on 2015/7/10.
 */
public class UploadImageBean implements YanxiuBaseBean{


    /**
     * data : ["http://scc.jsyxw.cn/avatar/images//2015/0924/file_56036b2d6b5ae.jpg"]
     * status : {"code":0,"desc":"操作成功"}
     */
    private List<String> data;
    private StatusEntity status;

    public void setData(List<String> data) {
        this.data = data;
    }

    public void setStatus(StatusEntity status) {
        this.status = status;
    }

    public List<String> getData() {
        return data;
    }

    public StatusEntity getStatus() {
        return status;
    }

    public class StatusEntity {
        /**
         * code : 0
         * desc : 操作成功
         */
        private int code;
        private String desc;

        public void setCode(int code) {
            this.code = code;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }
}
