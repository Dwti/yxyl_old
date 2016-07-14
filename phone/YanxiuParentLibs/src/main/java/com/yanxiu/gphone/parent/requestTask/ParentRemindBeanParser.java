package com.yanxiu.gphone.parent.requestTask;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.parent.bean.ParentRemindBean;

import org.json.JSONObject;

/**
 * Created by lee on 16-3-31.
 */
public class ParentRemindBeanParser extends YanxiuMobileParser<ParentRemindBean> {
    @Override
    public ParentRemindBean parse(JSONObject data) throws Exception {
        if(data!=null){
            return JSON.parseObject(data.toString(), ParentRemindBean.class);
        }
        return null;
    }
}
