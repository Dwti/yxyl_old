package com.common.share;

import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;

import com.common.share.inter.IShare;
import com.common.share.inter.ShareResultCallBackListener;
import com.common.share.inter.impl.QQShare;
import com.common.share.inter.impl.QQZoneShare;
import com.common.share.inter.impl.WeiXinShare;

/**
 * Created by Administrator on 2015/10/27.
 */
@SuppressWarnings("unchecked")
public class ShareManager {
    private static  ShareManager sShareManager = null;
    private IShare mIShare;
    private static final String TAG=ShareManager.class.getSimpleName();
    private SparseArray<IShare> managrArray=new SparseArray();
    private ShareManager(){
    }
    /**
     * 获取ShareManager实例
     * 非线程安全，请在UI线程中操作
     * @return
     */
    public static ShareManager getInstance(){
        if(sShareManager == null){
            sShareManager = new ShareManager();
        }
        return sShareManager;
    }


    public void onShare(Context context,ShareEnums enums,Bundle bundle,ShareResultCallBackListener shareResultCallBackListener){
        if(bundle==null||bundle.isEmpty()||bundle.size()==0){
            return;
        }
           switch (enums){
            case QQ:
                if(managrArray.get(enums.type)==null){
                    mIShare=new QQShare();
                    managrArray.put(enums.type,mIShare);
                }else{
                    mIShare= managrArray.get(enums.type);
                }
                break;
            case QQZONE:
                if(managrArray.get(enums.type)==null){

                    mIShare=new QQZoneShare();
                    managrArray.put(enums.type,mIShare);
                }else{
                    mIShare=  managrArray.get(enums.type);
                }
                break;
            case WEIXIN:
            case WEIXIN_FRIENDS:
                if(managrArray.get(enums.type)==null){
                    mIShare=new WeiXinShare();
                    managrArray.put(enums.type,mIShare);
                }else{
                    mIShare= managrArray.get(enums.type);
                }
                break;
           }
           if(mIShare==null){
                return;
           }
           mIShare.onShare(context,bundle,shareResultCallBackListener);
    }

    public void clearInstance(){
        destoryIShare();
        if(managrArray != null){
            managrArray.clear();
        }
        managrArray=null;
        sShareManager=null;
    }

    private void destoryIShare(){
        if(mIShare!=null){
            mIShare.clear();
            mIShare=null;
        }
    }


}
