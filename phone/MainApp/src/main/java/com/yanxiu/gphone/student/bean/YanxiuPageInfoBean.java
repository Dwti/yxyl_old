package com.yanxiu.gphone.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by Administrator on 2015/7/17.
 */
public class YanxiuPageInfoBean implements YanxiuBaseBean{

    /**
     * totalCou : 20
     * totalPage : 0
     * nextPage : 2
     * pageSize : 2
     */
    private int totalCou;
    private int totalPage;
    private int nextPage;
    private int pageSize;

    public void setTotalCou(int totalCou) {
        this.totalCou = totalCou;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCou() {
        return totalCou;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    @Override
    public String toString() {
        return "YanxiuPageInfoBean{" +
                "totalCou=" + totalCou +
                ", totalPage=" + totalPage +
                ", nextPage=" + nextPage +
                ", pageSize=" + pageSize +
                '}';
    }
}
