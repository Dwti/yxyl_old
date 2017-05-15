package com.yanxiu.gphone.studentold.bean;

/**
 * Created by Administrator on 2015/7/20.
 */
public class PageBean extends SrtBaseBean{

    /**
     * totalCou : 1
     * totalPage : 1
     * nextPage : 1
     * pageSize : 1
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
}
