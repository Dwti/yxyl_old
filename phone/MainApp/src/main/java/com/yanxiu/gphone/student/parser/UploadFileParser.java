package com.yanxiu.gphone.student.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.student.bean.UploadFileBean;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/8.
 */
public class UploadFileParser extends YanxiuMobileParser<UploadFileBean> {
    @Override
    public UploadFileBean parse(JSONObject data) throws Exception {
        if(data != null ){
            return JSON.parseObject(data.toString(), UploadFileBean.class);
        }
        return null;
    }
}
