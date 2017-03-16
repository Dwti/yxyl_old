package com.yanxiu.gphone.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/14 10:08.
 * Function :
 */

public class MistakeRedoCardBean implements YanxiuBaseBean {

    public static final int TYPE_NOANSWER=0;
    public static final int TYPE_HASANSWER=1;

    List<Mdata> data;

    public class Mdata implements YanxiuBaseBean{
        String date;
        int count;
        List<Integer> wqnumbers;
        List<Integer> wqtypes;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<Integer> getWqnumbers() {
            return wqnumbers;
        }

        public void setWqnumbers(List<Integer> wqnumbers) {
            this.wqnumbers = wqnumbers;
            if (wqnumbers!=null&&wqnumbers.size()>0) {
                wqtypes=new ArrayList<>();
                for (Integer i : wqnumbers) {
                    wqtypes.add(TYPE_NOANSWER);
                }
            }
        }

        public List<Integer> getWqtypes() {
            return wqtypes;
        }

        public void setWqtypes(List<Integer> wqtypes) {
            this.wqtypes = wqtypes;
        }
    }

    public List<Mdata> getData() {
        return data;
    }

    public void setData(List<Mdata> data) {
        this.data = data;
    }
}
