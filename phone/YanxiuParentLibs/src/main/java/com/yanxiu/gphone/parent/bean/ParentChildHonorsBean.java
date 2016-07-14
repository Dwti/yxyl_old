package com.yanxiu.gphone.parent.bean;

import java.util.List;

/**
 * Created by lidongming on 16/3/28.
 */
public class ParentChildHonorsBean extends ParentRequestBean{

    /**
     * rank : 0
     * times : 0
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private int rank;
        private int times;

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public int getTimes() {
            return times;
        }

        public void setTimes(int times) {
            this.times = times;
        }
    }
}
