package com.yanxiu.gphone.studentold.parser;

import com.alibaba.fastjson.JSON;
import com.yanxiu.basecore.parse.YanxiuMobileParser;
import com.yanxiu.gphone.studentold.bean.ChapterListEntity;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/8.
 */
public class ChapterListParser extends YanxiuMobileParser<ChapterListEntity> {
    @Override
    public ChapterListEntity parse(JSONObject data) throws Exception {
        if(data != null ){
            return JSON.parseObject(data.toString(), ChapterListEntity.class);
        }
        return null;
    }
}
