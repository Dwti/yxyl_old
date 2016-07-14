package com.yanxiu.gphone.hd.student.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.hd.student.bean.RankResultBean;

import org.json.JSONObject;


/**
 * Created by Administrator on 2015/9/24.
 */
public class RequestRankListParser extends YanxiuMobileParser<RankResultBean> {
    @Override
    public RankResultBean parse(JSONObject data) throws Exception {
        if(data!=null){
            return JSON.parseObject(data.toString(), RankResultBean.class);
        }
        return null;
    }
}
