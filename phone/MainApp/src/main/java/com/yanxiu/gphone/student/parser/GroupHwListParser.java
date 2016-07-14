package com.yanxiu.gphone.student.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.student.bean.GroupHwListBean;
import com.yanxiu.gphone.student.bean.GroupHwUndoListBean;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/8.
 */
public class GroupHwListParser extends YanxiuMobileParser<GroupHwListBean> {
    @Override
    public GroupHwListBean parse(JSONObject data) throws Exception {
        if(data != null ){
            return JSON.parseObject(data.toString(), GroupHwListBean.class);
        }
        return null;
    }
}
