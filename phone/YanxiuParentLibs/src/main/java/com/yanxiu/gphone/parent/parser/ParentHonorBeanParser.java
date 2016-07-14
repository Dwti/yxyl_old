package com.yanxiu.gphone.parent.parser;
import com.yanxiu.gphone.parent.bean.ParentHonorBean;
import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import org.json.JSONObject;

/**
 * Created by lee on 16-3-30.
 */
public class ParentHonorBeanParser extends YanxiuMobileParser<ParentHonorBean> {
    @Override
    public ParentHonorBean parse(JSONObject data) throws Exception {
        if(data!=null){
            return JSON.parseObject(data.toString(), ParentHonorBean.class);
        }
        return null;
    }
}

