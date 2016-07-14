package com.yanxiu.gphone.student.bean;


import java.util.List;

/**
 *获得教材版本信息
 */
public class SubjectVersionBean extends SrtBaseBean {

    /**
     * data : [{"data":null,"children":[{"data":null,"children":null,"name":"初1第1册","id":123},{"data":null,"children":null,"name":"初1第2册","id":124},{"data":null,"children":null,"name":"初2第1册","id":125},{"data":null,"children":null,"name":"初2第2册","id":126},{"data":null,"children":null,"name":"初3第1册","id":127}],"name":"人教版","id":123},{"data":null,"children":[{"data":null,"children":null,"name":"初1第1册","id":123},{"data":null,"children":null,"name":"初1第2册","id":124},{"data":null,"children":null,"name":"初2第1册","id":125},{"data":null,"children":null,"name":"初2第2册","id":126},{"data":null,"children":null,"name":"初3第1册","id":127}],"name":"胡教版","id":123}]
     * page : null
     * status : {"code":0,"desc":"get all geditions for execrise"}
     */
    private List<DataEntity> data;
    private String page;
    private DataStatusEntityBean status;

    public void setData(List<DataEntity> data) {
        this.data = data;
    }
    public void setPage(String page) {
        this.page = page;
    }

    public DataStatusEntityBean getStatus() {
        return status;
    }

    public void setStatus(DataStatusEntityBean status) {
        this.status = status;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public String getPage() {
        return page;
    }


    public static class DataEntity extends SrtBaseBean {
        /**
         * data : null
         * children : [{"data":null,"children":null,"name":"初1第1册","id":123},{"data":null,"children":null,"name":"初1第2册","id":124},{"data":null,"children":null,"name":"初2第1册","id":125},{"data":null,"children":null,"name":"初2第2册","id":126},{"data":null,"children":null,"name":"初3第1册","id":127}]
         * name : 人教版
         * id : 123
         */
        private EditionBean data;
        private List<ChildrenEntity> children;
        private String name;
        private String id = "";
        private int resId;


        public void setData(EditionBean data) {
            this.data = data;
        }

        public EditionBean getData() {
            return data;
        }


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

        public int getResId() {
            return resId;
        }

        public void setResId(int resId) {
            this.resId = resId;
        }
        public class ChildrenEntity extends SrtBaseBean {
            /**
             * data : null
             * children : null
             * name : 初1第1册
             * id : 123
             */
            private String data;
            private String children;
            private String name;
            private int id;

            public void setData(String data) {
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

            public String getData() {
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
        }

    }

}
