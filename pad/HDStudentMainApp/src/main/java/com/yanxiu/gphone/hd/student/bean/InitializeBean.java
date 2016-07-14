
package com.yanxiu.gphone.hd.student.bean;

/**
 * Created by Administrator on 2015/5/20.
 */
public class InitializeBean extends SrtBaseBean {

    private String id;

    private String version;

    private String title;

    private String resid;

    private String ostype;

    private String upgradetype;

    private String upgradeswitch;

    private String targetenv;

    private String uploadtime;

    private String modifytime;

    private String content;

    private String fileURL;

    private String fileLocalPath;

    private String versionStr;

    private String productName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

    public String getOstype() {
        return ostype;
    }

    public void setOstype(String ostype) {
        this.ostype = ostype;
    }

    public String getUpgradetype() {
        return upgradetype;
    }

    public void setUpgradetype(String upgradetype) {
        this.upgradetype = upgradetype;
    }

    public String getUpgradeswitch() {
        return upgradeswitch;
    }

    public void setUpgradeswitch(String upgradeswitch) {
        this.upgradeswitch = upgradeswitch;
    }

    public String getTargetenv() {
        return targetenv;
    }

    public void setTargetenv(String targetenv) {
        this.targetenv = targetenv;
    }

    public String getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(String uploadtime) {
        this.uploadtime = uploadtime;
    }

    public String getModifytime() {
        return modifytime;
    }

    public void setModifytime(String modifytime) {
        this.modifytime = modifytime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public String getFileLocalPath() {
        return fileLocalPath;
    }

    public void setFileLocalPath(String fileLocalPath) {
        this.fileLocalPath = fileLocalPath;
    }

    public String getVersionStr() {
        return versionStr;
    }

    public void setVersionStr(String versionStr) {
        this.versionStr = versionStr;
    }

    @Override public String toString() {
        return "InitializeBean{" +
                "id='" + id + '\'' +
                ", version='" + version + '\'' +
                ", title='" + title + '\'' +
                ", resid='" + resid + '\'' +
                ", ostype='" + ostype + '\'' +
                ", upgradetype='" + upgradetype + '\'' +
                ", upgradeswitch='" + upgradeswitch + '\'' +
                ", targetenv='" + targetenv + '\'' +
                ", uploadtime='" + uploadtime + '\'' +
                ", modifytime='" + modifytime + '\'' +
                ", content='" + content + '\'' +
                ", fileURL='" + fileURL + '\'' +
                ", fileLocalPath='" + fileLocalPath + '\'' +
                ", versionStr='" + versionStr + '\'' +
                ", productName='" + productName + '\'' +
                '}';
    }
}
