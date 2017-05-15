package com.yanxiu.gphone.studentold.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.studentold.bean.SchoolListBean;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/5/20.
 */
public class SchoolListParser extends YanxiuMobileParser<SchoolListBean> {
    @Override
    public SchoolListBean parse(JSONObject data) throws Exception {
        if(data != null ){
            return JSON.parseObject(data.toString(), SchoolListBean.class);
        }
        return null;
    }
}
