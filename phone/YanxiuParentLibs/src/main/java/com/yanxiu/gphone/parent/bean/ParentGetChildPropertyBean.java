package com.yanxiu.gphone.parent.bean;

import com.common.login.model.ParentGetChildClassInfoBean;
import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by lee on 16-3-29.
 */
public class ParentGetChildPropertyBean implements YanxiuBaseBean {
    private ParentGetChildClassInfoBean classInfo;
    private ParentGetChildStudentBean student;

    public ParentGetChildClassInfoBean getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(ParentGetChildClassInfoBean classInfo) {
        this.classInfo = classInfo;
    }

    public ParentGetChildStudentBean getStudent() {
        return student;
    }

    public void setStudent(ParentGetChildStudentBean student) {
        this.student = student;
    }
}
