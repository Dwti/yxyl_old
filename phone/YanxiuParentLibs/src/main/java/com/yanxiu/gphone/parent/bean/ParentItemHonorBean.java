package com.yanxiu.gphone.parent.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by lee on 16-3-30.
 */
public class ParentItemHonorBean implements YanxiuBaseBean {
    public static final String NOT_ACQUIRE="0";
    public static final String HAS_ACQUIRE="1";
    private String id;
    private String honortype;
    private String honorname;
    private String honordesc;
    private String acquiretimeStr;
    private String hasAcquire;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHonortype() {
        return honortype;
    }

    public void setHonortype(String honortype) {
        this.honortype = honortype;
    }

    public String getHonorname() {
        return honorname;
    }

    public void setHonorname(String honorname) {
        this.honorname = honorname;
    }

    public String getHonordesc() {
        return honordesc;
    }

    public void setHonordesc(String honordesc) {
        this.honordesc = honordesc;
    }

    public String getAcquiretimeStr() {
        return acquiretimeStr;
    }

    public void setAcquiretimeStr(String acquiretimeStr) {
        this.acquiretimeStr = acquiretimeStr;
    }

    public String getHasAcquire() {
        return hasAcquire;
    }

    public void setHasAcquire(String hasAcquire) {
        this.hasAcquire = hasAcquire;
    }
}
