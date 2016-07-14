package com.yanxiu.gphone.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/17.
 */
public class ChapterListDataEntity implements YanxiuBaseBean {
    /**
     * children : [{"name":"1、社戏（鲁迅）","id":31245},{"name":"2、安塞腰鼓（刘成章）","id":31246},{"name":"3、竹影（丰子恺）","id":31247},{"name":"4、观舞记","id":31248},{"name":"5、看戏（叶君健）","id":31249},{"name":"6、口技（林嗣环）","id":31250}]
     * name : 第一单元
     * id : 31244
     */
    private List<ChildrenEntity> children = new ArrayList<ChildrenEntity>();
    private String name;
    private String id;
    private ExamPropertyData data;


    public void setChildren(List<ChildrenEntity> children) {
        this.children = children;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ChildrenEntity> getChildren() {
        return children;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public ExamPropertyData getData () {
        return data;
    }

    public void setData (ExamPropertyData data) {
        this.data = data;
    }

    public static class ChildrenEntity {
        /**
         * name : 1、社戏（鲁迅）
         * id : 31245
         */
        private String name;
        private String id;
        private boolean isExpanded;
        private List<GrandsonEntity> children = new ArrayList<GrandsonEntity>();
        private ExamPropertyData data;
        public List<GrandsonEntity> getChildren() {
            return children;
        }

        public void setChildren(List<GrandsonEntity> children) {
            this.children = children;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public boolean isExpanded() {
            return isExpanded;
        }

        public void setIsExpanded(boolean isExpanded) {
            this.isExpanded = isExpanded;
        }
        public ExamPropertyData getData () {
            return data;
        }

        public void setData (ExamPropertyData data) {
            this.data = data;
        }
    }

    public static class GrandsonEntity {
        /**
         * name : 1、社戏（鲁迅）
         * id : 31245
         */
        private String name;
        private String id;
        private boolean isExpanded;
        private ExamPropertyData data;

        public void setName(String name) {
            this.name = name;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public boolean isExpanded() {
            return isExpanded;
        }

        public void setIsExpanded(boolean isExpanded) {
            this.isExpanded = isExpanded;
        }

        public ExamPropertyData getData () {
            return data;
        }

        public void setData (ExamPropertyData data) {
            this.data = data;
        }
    }



}
