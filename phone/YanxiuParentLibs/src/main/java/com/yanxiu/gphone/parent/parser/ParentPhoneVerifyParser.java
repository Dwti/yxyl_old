package com.yanxiu.gphone.parent.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.parent.bean.ParentSendMsgBean;

import org.json.JSONObject;

/**
 * Created by lee on 16-3-28.
 */
public class ParentPhoneVerifyParser extends YanxiuMobileParser<ParentSendMsgBean> {
    @Override
    public ParentSendMsgBean parse(JSONObject data) throws Exception {
        if(data!=null){
            return JSON.parseObject(data.toString(),ParentSendMsgBean.class);
        }
        return null;
    }
}
