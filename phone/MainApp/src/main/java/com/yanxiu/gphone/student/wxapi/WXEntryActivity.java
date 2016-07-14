package com.yanxiu.gphone.student.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.common.core.utils.LogInfo;
import com.common.share.constants.ShareConstants;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.utils.Util;

/**
 * Created by Administrator on 2015/9/21.
 */
public class WXEntryActivity extends YanxiuBaseActivity implements IWXAPIEventHandler{

    private IWXAPI api;
    private static WXLoginListener wxLoginListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, ShareConstants.STUDENT_WX_AppID,false);
        api.handleIntent(getIntent(), this);
        LogInfo.log("king", "WXEntryActivity onCreate");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogInfo.log("king", "WXEntryActivity onNewIntent");
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    public static void setListener(WXLoginListener listener){
        wxLoginListener = listener;
    }

    @Override public void onReq(BaseReq baseReq) {
        LogInfo.log("king","WXEntryActivity onReq");
        Util.showToast("onReq");
    }

    @Override public void onResp(BaseResp baseResp) {
        LogInfo.log("king","WXEntryActivity onResp ");
        if(baseResp != null && baseResp instanceof SendAuth.Resp){
            SendAuth.Resp resp = (SendAuth.Resp)baseResp;
            LogInfo.log("king","WXEntryActivity onResp state = " + resp.state + " ,errCode = " + resp.errCode);
            if(resp!=null && "none_weixin_login".equals(resp.state)){
                switch (baseResp.errCode){
                    case BaseResp.ErrCode.ERR_OK://用户同意
                        getCode(resp);
                        break;
                    case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                        break;
                }
                WXEntryActivity.this.finish();
            }else{
                if(wxLoginListener!=null){
                    wxLoginListener.onUserCancelOrFail();
                    WXEntryActivity.this.finish();
                }
            }
        }else if(baseResp != null && baseResp instanceof SendMessageToWX.Resp){
            SendMessageToWX.Resp resp = (SendMessageToWX.Resp)baseResp;
            LogInfo.log("king","WXEntryActivity onResp transaction = " + resp.transaction + " ,errCode = " + resp.errCode);
            if(resp!=null && resp.transaction.startsWith("webpage")){
                switch (resp.errCode){
                    case BaseResp.ErrCode.ERR_OK://用户同意
    //                    Util.showToast(R.string.share_success);
                        break;
                    case BaseResp.ErrCode.ERR_SENT_FAILED://分享失败
    //                    Util.showToast(R.string.share_fail);
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL: //用户取消
                        break;
                }
            }
            WXEntryActivity.this.finish();
        }
    }

    private void getCode(SendAuth.Resp resps) {
        switch (resps.errCode) {
        case BaseResp.ErrCode.ERR_UNSUPPORT://不支持错误
            if(wxLoginListener!=null){
                wxLoginListener.onUserCancelOrFail();
            }
            Util.showToast(R.string.login_fail);
            break;
        case BaseResp.ErrCode.ERR_OK://正确返回
            if(wxLoginListener!=null){
                LogInfo.log("king","WXEntryActivity onResp resps.code = " + resps.code);
                wxLoginListener.onGetWXLoginCode(resps.code);
            }
            break;
        case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
            if(wxLoginListener!=null){
                wxLoginListener.onUserCancelOrFail();
            }
            break;
        default:
            break;
        }
    }


    public interface WXLoginListener{
        void onGetWXLoginCode(String code);
        void onUserCancelOrFail();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogInfo.log("king", "WXEntryActivity onDestroy");
        if(wxLoginListener!=null){
            wxLoginListener.onUserCancelOrFail();
        }
    }
}
