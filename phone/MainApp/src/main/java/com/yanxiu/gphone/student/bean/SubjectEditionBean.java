package com.yanxiu.gphone.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

import java.util.List;

/**
 * Created by Administrator on 2015/7/16.
 */
public class SubjectEditionBean extends SrtBaseBean{

    /**
     * data : [{"data":null,"children":[{"data":null,"children":null,"name":"初1第1册","id":123},{"data":null,"children":null,"name":"初1第2册","id":124},{"data":null,"children":null,"name":"初2第1册","id":125},{"data":null,"children":null,"name":"初2第2册","id":126},{"data":null,"children":null,"name":"初3第1册","id":127}],"name":"人教版","id":123},{"data":null,"children":[{"data":null,"children":null,"name":"初1第1册","id":123},{"data":null,"children":null,"name":"初1第2册","id":124},{"data":null,"children":null,"name":"初2第1册","id":125},{"data":null,"children":null,"name":"初2第2册","id":126},{"data":null,"children":null,"name":"初3第1册","id":127}],"name":"胡教版","id":123}]
     * page : null
     * status : {"code":0,"desc":"get all geditions for execrise"}
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

    public static class DataEntity implements YanxiuBaseBean{
        /**
         * data : null
         * children : [{"data":null,"children":null,"name":"初1第1册","id":123},{"data":null,"children":null,"name":"初1第2册","id":124},{"data":null,"children":null,"name":"初2第1册","id":125},{"data":null,"children":null,"name":"初2第2册","id":126},{"data":null,"children":null,"name":"初3第1册","id":127}]
         * name : 人教版
         * id : 123
         */
        private KNPDataEntity data;
        private List<ChildrenEntity> children;
        private String name;
        private String id;

//        public void setData(String data) {
//            this.data = data;
//        }


        public KNPDataEntity getData() {
            return data;
        }

        public void setData(KNPDataEntity data) {
            this.data = data;
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

//        public String getData() {
//            return data;
//        }

        public List<ChildrenEntity> getChildren() {
            return children;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            return "DataEntity{" +
                    "name='" + name + '\'' +
                    ", id='" + id + '\'' +
                    '}';
        }

        public static class KNPDataEntity implements YanxiuBaseBean{

            private int has_knp;

            public int getHas_knp() {
                return has_knp;
            }

            public void setHas_knp(int has_knp) {
                this.has_knp = has_knp;
            }
        }

        public static class ChildrenEntity implements YanxiuBaseBean{
            /**
             * data : null
             * children : null
             * name : 初1第1册
             * id : 123
             */
            private String data;
            private String children;
            private String name;
            private String id;

            public void setData(String data) {
                this.data = data;
            }

            public void setChildren(String children) {
                this.children = children;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setId(String id) {
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

            public String getId() {
                return id;
            }
        }
    }

    public class StatusEntity implements YanxiuBaseBean{
        /**
         * code : 0
         * desc : get all geditions for execrise
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
}
