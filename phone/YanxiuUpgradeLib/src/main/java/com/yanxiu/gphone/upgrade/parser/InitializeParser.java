package com.yanxiu.gphone.upgrade.parser;

import com.alibaba.fastjson.JSON;
import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.ContextProvider;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.upgrade.bean.InitializeBean;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/5/20.
 */
public class InitializeParser extends YanxiuMobileParser<InitializeBean> {
    @Override
    public InitializeBean parse(JSONObject data) throws Exception {
        if(data != null ){
            JSONArray body = data.getJSONArray("body");
            if(body!=null && body.length()>0){
                JSONObject content = (JSONObject)body.get(0);
                return JSON
                        .parseObject(content.toString(), InitializeBean.class);
            }else{
                InitializeBean initializeBean = new InitializeBean();
                initializeBean.setVersion(CommonCoreUtil.getClientVersionName(ContextProvider.getApplicationContext()));
                return initializeBean;
            }
        }
        return null;
    }
}
