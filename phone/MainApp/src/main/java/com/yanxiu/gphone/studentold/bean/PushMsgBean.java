package com.yanxiu.gphone.studentold.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by Administrator on 2015/7/10.
 */
public class PushMsgBean implements YanxiuBaseBean{
    /**
     * {"id":100069,"msg_title":"test","msg_type":2}
     *
     msg_title：消息标题内容；
     msg_type：0为作业报告，1为学科作业列表，2为作业首页；
     id：msg_type为0时，id表示作业ID；msg_type为1时，id表示群组ID；
     name：msg_type为0时，name表示作业名称；msg_type为1时，name表示学科名称；；
     */
    private String msg_title;
    private int msg_type;
    private int id;
    private String name;

    public String getMsg_title() {
        return msg_title;
    }

    public void setMsg_title(String msg_title) {
        this.msg_title = msg_title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    @Override
    public String toString() {
        return "PushMsgBean{" +
                "msg_title='" + msg_title + '\'' +
                ", msg_type=" + msg_type +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
