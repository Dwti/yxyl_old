package com.yanxiu.gphone.studentold.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.studentold.bean.GroupListBean;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/8.
 */
public class GroupListParser extends YanxiuMobileParser<GroupListBean> {
    @Override
    public GroupListBean parse(JSONObject data) throws Exception {
        if(data != null ){
            return JSON.parseObject(data.toString(), GroupListBean.class);
        }
        return null;
    }
}
