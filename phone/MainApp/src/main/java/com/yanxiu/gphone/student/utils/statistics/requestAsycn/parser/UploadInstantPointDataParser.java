package com.yanxiu.gphone.student.utils.statistics.requestAsycn.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.student.bean.statistics.UploadInstantPointDataBean;

import org.json.JSONObject;

/**
 * Created on 16-6-2.
 */
public class UploadInstantPointDataParser extends YanxiuMobileParser<UploadInstantPointDataBean> {
    @Override
    public UploadInstantPointDataBean parse(JSONObject data) throws Exception {
        if(data != null ){
            return JSON.parseObject(data.toString(), UploadInstantPointDataBean.class);
        }
        return null;
    }
}
