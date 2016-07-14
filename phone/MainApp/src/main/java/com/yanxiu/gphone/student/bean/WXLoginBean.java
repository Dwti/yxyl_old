package com.yanxiu.gphone.student.bean;

import com.yanxiu.basecore.bean.YanxiuBaseBean;

/**
 * Created by Administrator on 2015/9/21.
 */
public class WXLoginBean implements YanxiuBaseBean{
    /**
     * access_token 接口调用凭证
     expires_in access_token接口调用凭证超时时间，单位（秒）
     refresh_token 用户刷新access_token
     openid 授权用户唯一标识
     scope 用户授权的作用域，使用逗号（,）分隔

     */
    public String access_token;
    public String expires_in;
    public String refresh_token;
    public String openid;
    public String scope;
    public String uniqid;
}
