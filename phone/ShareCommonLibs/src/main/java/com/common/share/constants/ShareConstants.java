package com.common.share.constants;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

/**
 * Created by lee on 16-3-23.
 */
public class ShareConstants {
    public static final int THUMB_SIZE = 150;



    public static final String SHARE_ICON_ASSET_NAME = "/assets/share_logo.png";

    //QQ相关参数
    public static final String QQ_APPID_KEY="qq_appid_key";
    public static final String QQ_APPSECRET_KEY="qq_appsecret_key";

    public static final String QQ_PACKAGENAME="com.tencent.mobileqq";
    /**
     * QQ好友
     * */
    public static final int QQ_SHARE_TYPE_TALK = 2;
    /**
     * QQ空间
     * */
    public static final int QQ_SHARE_TYPE_FRENDS = 3;

    public static final String STUDENT_QQ_AppID = "1104826608";

    public static final String STUDENT_QQ_AppSecret = "PsLMILpDwU6QDNOk";


    public static final String HD_STUDENT_QQ_AppID = "1105149060";

    public static final String HD_STUDENT_QQ_AppSecret = "E9YWHMESAaGLNXDj";





    //微信相关的参数Keys

    public static final String WX_APPID_KEY="wx_appid_key";
    public static final String WX_APPSECRET_KEY="wx_appsecret_key";
    //微信分享方式类型  会话或者朋友圈
    public static final String WEIXIN_SHARE_WAY_TYPE="weixin_share_way_type";
    //微信分享类型  文字 图片 或者时网页地址
    public static final String WEIXIN_SHARE_TYPE="weixin_share_type";
    //标题
    public static final String WEIXIN_SHARE_TITLE_KEY="weixin_share_title_key";
    //描述
    public static final String WEIXIN_SHARE_DES_KEY="weixin_share_des_key";
    //分享图片地址
    public static final String WEIXIN_SHARE_IMG_URL="weixin_share_img_url";
    //分享应用icon标示
    public static final String WEIXIN_SHARE_IMG_ICON="weixin_share_img_icon";


    public static final String STUDENT_WX_AppID = "wxb6704ac52abcfe4c";
    public static final String STUDENT_WX_AppSecret ="943d690bd5020ae629c20281e53bc334";


    public static final String HD_STUDENT_WX_AppID = "wxb85389da49835869";
    public static final String HD_STUDENT_WX_AppSecret = "dd4ec04a8ab6f31448371806072ad56a";
    /**
     * 文字
     */
    public static final int WEIXIN_SHARE_WAY_TEXT = 1;
    /**
     * 图片
     */
    public static final int WEIXIN_SHARE_WAY_PIC = 2;
    /**
     * 链接
     */
    public static final int WEIXIN_SHARE_WAY_WEBPAGE = 3;
    /**
     * 会话
     */
    public static final int WEIXIN_SHARE_TYPE_TALK = SendMessageToWX.Req.WXSceneSession;
    /**
     * 朋友圈
     */
    public static final int WEIXIN_SHARE_TYPE_FRENDS = SendMessageToWX.Req.WXSceneTimeline;






}
