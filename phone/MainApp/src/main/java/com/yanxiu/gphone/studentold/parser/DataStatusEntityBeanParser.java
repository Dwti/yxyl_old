package com.yanxiu.gphone.studentold.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.studentold.bean.DataStatusEntityBean;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/17.
 */
public class DataStatusEntityBeanParser extends YanxiuMobileParser<DataStatusEntityBean> {
    @Override
    public DataStatusEntityBean parse(JSONObject data) throws Exception {
        if(data != null ){
            data = data.getJSONObject("status");
            return JSON.parseObject(data.toString(), DataStatusEntityBean.class);
        }
        return null;
    }
}
