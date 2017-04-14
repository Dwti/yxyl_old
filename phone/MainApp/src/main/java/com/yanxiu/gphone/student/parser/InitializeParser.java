package com.yanxiu.gphone.student.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.student.bean.InitializeBean;
import com.yanxiu.gphone.student.bean.NewInitializeBean;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/5/20.
 */
public class InitializeParser extends YanxiuMobileParser<NewInitializeBean> {
    @Override
    public NewInitializeBean parse(JSONObject data) throws Exception {
        if(data != null ){
            JSONArray body = data.getJSONArray("data");
            if(body!=null && body.length()>0){
                JSONObject content = (JSONObject)body.get(0);
                return JSON.parseObject(content.toString(), NewInitializeBean.class);
            }else{
                NewInitializeBean initializeBean = new NewInitializeBean();
                initializeBean.setVersion(YanXiuConstant.VERSION);
                return initializeBean;
            }
        }
        return null;
    }
}
