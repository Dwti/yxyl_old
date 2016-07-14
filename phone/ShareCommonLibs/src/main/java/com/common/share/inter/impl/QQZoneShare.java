package com.common.share.inter.impl;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.share.ShareExceptionEnums;
import com.common.share.constants.ShareConstants;
import com.common.share.inter.IShare;
import com.common.share.inter.ShareResultCallBackListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * Created by lee on 16-3-23.
 */
public class QQZoneShare implements IShare {
    private Tencent mTencent;
    private static final  String TAG=QQZoneShare.class.getSimpleName();
    @Override
    public void onShare(Context context, Bundle bundle, final ShareResultCallBackListener shareResultCallBackListener) {
        if(!CommonCoreUtil.checkBrowser(context, ShareConstants.QQ_PACKAGENAME)){
            if(shareResultCallBackListener!=null){
                shareResultCallBackListener.notInstall();
            }
            return;
        }
        if(bundle.containsKey(ShareConstants.QQ_APPID_KEY)){
            mTencent = Tencent.createInstance(bundle.getString(ShareConstants.QQ_APPID_KEY), context);
            if(mTencent==null){
                if(shareResultCallBackListener!=null){
                    shareResultCallBackListener.shareException(ShareExceptionEnums.NULLPOINT_EXCEPTION);
                }
                return;
            }
        }else{
            if(shareResultCallBackListener!=null){
                shareResultCallBackListener.shareException(ShareExceptionEnums.PARAMS_EXCEPTION);
            }
            return;
        }

        mTencent.shareToQzone((Activity) context, bundle, new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    LogInfo.log(TAG,"onComplete");
                    if(shareResultCallBackListener!=null){
                        shareResultCallBackListener.shareSuccess(o);
                    }
                }

                @Override
                public void onError(UiError uiError) {
                    LogInfo.log(TAG,"onError errorMessage: "+uiError.errorMessage+"errorDetail: "+uiError.errorDetail);
                    if(shareResultCallBackListener!=null){
                        shareResultCallBackListener.shareFailrue(uiError);
                    }
                }

                @Override
                public void onCancel() {
                    LogInfo.log(TAG,"onCancel");
                }
            });

    }

    @Override
    public void clear() {
        mTencent=null;
    }
}
