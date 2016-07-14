package com.yanxiu.gphone.student.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/7/7.
 * 智能练习 bean
 */
public class IntelliExeBean extends SrtBaseBean {
    /**
     * data : [{"data":{"editionName":"人教版","editionId":1234},
     * "children":null,"name":"语文","id":123},
     * {"data":null,"children":null,"name":"数学","id":123},
     * {"data":null,"children":null,"name":"英语","id":123}]
     * page : null
     * status : {"code":0,"desc":"get all subject for execrise"}
     */
    private List<DataEntity> data;
    private String page;
    private StatusEntity status;

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setStatus(StatusEntity status) {
        this.status = status;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public String getPage() {
        return page;
    }

    public StatusEntity getStatus() {
        return status;
    }

    public class DataEntity {
        /**
         * data : {"editionName":"人教版","editionId":1234}
         * children : null
         * name : 语文
         * id : 123
         */
        private InterDataEntity data;
        private String children;
        private String name;
        private int id;

        public void setData(InterDataEntity data) {
            this.data = data;
        }

        public void setChildren(String children) {
            this.children = children;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setId(int id) {
            this.id = id;
        }

        public InterDataEntity getData() {
            return data;
        }

        public String getChildren() {
            return children;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public class InterDataEntity {
            /**
             * editionName : 人教版
             * editionId : 1234
             */
            private String editionName;
            private int editionId;

            public void setEditionName(String editionName) {
                this.editionName = editionName;
            }

            public void setEditionId(int editionId) {
                this.editionId = editionId;
            }

            public String getEditionName() {
                return editionName;
            }

            public int getEditionId() {
                return editionId;
            }
        }
    }

    public class StatusEntity {
        /**
         * code : 0
         * desc : get all subject for execrise
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

//    private Status status;
//
//    public Status getStatus() {
//        return status;
//    }
//
//    public void setStatus(Status status) {
//        this.status = status;
//    }
//
//    public class Status extends SrtBaseBean{
//        private String code;
//        private String desc;
//
//        public String getDesc() {
//            return desc;
//        }
//
//        public void setDesc(String desc) {
//            this.desc = desc;
//        }
//
//        public String getCode() {
//            return code;
//        }
//
//        public void setCode(String code) {
//            this.code = code;
//        }
//    }
//
//
//    private ArrayList<IntelliExeItem> body;
//
//    public ArrayList<IntelliExeItem> getBody() {
//        return body;
//    }
//
//    public void setBody(ArrayList<IntelliExeItem> body) {
//        this.body = body;
//    }
//
//    public class IntelliExeItem extends SrtBaseBean{
//
//        private String icon;
//        private String sname;
//        private String sid;
//        private Edition edition;
//
//        public String getIcon() {
//            return icon;
//        }
//
//        public void setIcon(String icon) {
//            this.icon = icon;
//        }
//
//        public String getSname() {
//            return sname;
//        }
//
//        public void setSname(String sname) {
//            this.sname = sname;
//        }
//
//        public String getSid() {
//            return sid;
//        }
//
//        public void setSid(String sid) {
//            this.sid = sid;
//        }
//
//        public Edition getEdition() {
//            return edition;
//        }
//
//        public void setEdition(Edition edition) {
//            this.edition = edition;
//        }
//
//        public class Edition extends SrtBaseBean{
//            private String ename;
//            private String eid;
//
//            public String getEname() {
//                return ename;
//            }
//
//            public void setEname(String ename) {
//                this.ename = ename;
//            }
//
//            public String getEid() {
//                return eid;
//            }
//
//            public void setEid(String eid) {
//                this.eid = eid;
//            }
//        }
//
//    }
}
