package com.common.share.inter.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.common.core.utils.BitmapUtil;
import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.share.ShareExceptionEnums;
import com.common.share.constants.ShareConstants;
import com.common.share.inter.IShare;
import com.common.share.inter.ShareResultCallBackListener;
import com.common.share.utils.ShareUtils;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by lee on 16-3-23.
 */
public class WeiXinShare implements IShare{
    private static final String TAG=WeiXinShare.class.getSimpleName();
    private IWXAPI wxApi;
    @Override
    public void onShare(Context context, Bundle bundle, ShareResultCallBackListener shareResultCallBackListener) {
        if(bundle.containsKey(ShareConstants.WX_APPID_KEY)){
            String APPID=bundle.getString(ShareConstants.WX_APPID_KEY);
            wxApi = WXAPIFactory.createWXAPI(context,APPID, true);
            wxApi.registerApp(APPID);
        }
        if(wxApi==null){
            LogInfo.log(TAG,"wxApi is null");
            if(shareResultCallBackListener!=null){
                shareResultCallBackListener.shareException(ShareExceptionEnums.PARAMS_EXCEPTION);
            }
            return;
        }
        boolean isInstall = CommonCoreUtil.checkBrowser(context, "com.tencent.mm");
        LogInfo.log(TAG, "isInstall = " + isInstall);
        if(isInstall){
            shareByWeixin(context,bundle);
        }else{
            if(shareResultCallBackListener!=null){
                shareResultCallBackListener.notInstall();
            }
        }
    }

    private void shareByWeixin(Context context,Bundle bundle) {
        int shareWayType=bundle.getInt(ShareConstants.WEIXIN_SHARE_WAY_TYPE);
        int shareType=bundle.getInt(ShareConstants.WEIXIN_SHARE_TYPE);
        switch (shareWayType){
            case ShareConstants.WEIXIN_SHARE_WAY_TEXT:
                shareText(shareType,context, bundle);
                break;
            case ShareConstants.WEIXIN_SHARE_WAY_PIC:
                sharePicture(shareType, context, bundle);
                break;
            case ShareConstants.WEIXIN_SHARE_WAY_WEBPAGE:
                shareWebPage(shareType, context, bundle);
                break;
        }
    }
    /*
    * 分享文字
    */
    private void shareText(int shareType,Context context,Bundle bundle) {
        String text = null;
        if(bundle.containsKey(ShareConstants.WEIXIN_SHARE_DES_KEY)){
            text =bundle.getString(ShareConstants.WEIXIN_SHARE_DES_KEY);
        }
        //初始化一个WXTextObject对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;
        //用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = text;
        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        //transaction字段用于唯一标识一个请求
        req.transaction = ShareUtils.buildTransaction("textshare");
        req.message = msg;
        //发送的目标场景， 可以选择发送到会话 WXSceneSession 或者朋友圈 WXSceneTimeline。 默认发送到会话。
        req.scene = shareType;
        wxApi.sendReq(req);
    }

    /*
     * 分享图片
     */
    private void sharePicture(int shareType,Context context,Bundle bundle) {
        Bitmap bmp = null;
        Bitmap thumbBmp=null;
        if(bundle.containsKey(ShareConstants.WEIXIN_SHARE_IMG_ICON)){
            bmp   = BitmapUtil.readBitMap(context,bundle.getInt(ShareConstants.WEIXIN_SHARE_IMG_ICON));
//            BitmapFactory.decodeResource(context.getResources(),bundle.getInt(ShareConstants.WEIXIN_SHARE_IMG_ICON));
        }
        WXImageObject imgObj = new WXImageObject(bmp);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        if(bmp!=null){
            thumbBmp= Bitmap.createScaledBitmap(bmp, ShareConstants.THUMB_SIZE, ShareConstants.THUMB_SIZE,
                    true);
            if(!bmp.isRecycled()){
                bmp.recycle();
            }
            msg.thumbData =  CommonCoreUtil.getBitmapByte(thumbBmp);  //设置缩略图
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = ShareUtils.buildTransaction("imgshareappdata");
        req.message = msg;
        req.scene = shareType;
        wxApi.sendReq(req);
        if(thumbBmp!=null&&!thumbBmp.isRecycled()){
            thumbBmp.recycle();
        }
    }

    /*
     * 分享链接
     */
    private void shareWebPage(int shareType,Context context,Bundle bundle) {
        WXWebpageObject webpage = new WXWebpageObject();
        if(bundle.containsKey(ShareConstants.WEIXIN_SHARE_IMG_URL)){
            webpage.webpageUrl = bundle.getString(ShareConstants.WEIXIN_SHARE_IMG_URL);
        }else{
            webpage.webpageUrl="";
        }

        WXMediaMessage msg = new WXMediaMessage(webpage);
        if(shareType == ShareConstants.WEIXIN_SHARE_TYPE_TALK){
            if(bundle.containsKey(ShareConstants.WEIXIN_SHARE_TITLE_KEY)){
                msg.title = bundle.getString(ShareConstants.WEIXIN_SHARE_TITLE_KEY);
            }else{
                msg.title="";
            }

        }else if(shareType ==  ShareConstants.WEIXIN_SHARE_TYPE_FRENDS){
            if(bundle.containsKey(ShareConstants.WEIXIN_SHARE_DES_KEY)){
                msg.title =bundle.getString(ShareConstants.WEIXIN_SHARE_DES_KEY);
            }else{
                msg.title="";
            }

        }
        if(bundle.containsKey(ShareConstants.WEIXIN_SHARE_DES_KEY)){
            msg.description  =bundle.getString(ShareConstants.WEIXIN_SHARE_DES_KEY);
        }else{
            msg.description="";
        }

        Bitmap thumb;
        if(bundle.containsKey(ShareConstants.WEIXIN_SHARE_IMG_ICON)){
            thumb   = BitmapFactory.decodeResource(context.getResources(),bundle.getInt(ShareConstants.WEIXIN_SHARE_IMG_ICON));
            if(thumb!=null){
                msg.thumbData = CommonCoreUtil.getBitmapByte(thumb);
            }
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = ShareUtils.buildTransaction("webpage");
        req.message = msg;
        req.scene = shareType;
        wxApi.sendReq(req);
    }
    @Override
    public void clear() {
        wxApi=null;
    }
}
