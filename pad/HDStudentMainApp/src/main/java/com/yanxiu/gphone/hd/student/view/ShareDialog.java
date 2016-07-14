package com.yanxiu.gphone.hd.student.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yanxiu.gphone.hd.student.R;

/**
 * Created by Administrator on 2015/6/3.
 */
public class ShareDialog extends Dialog{

    protected FrameLayout wechatView;
    protected FrameLayout wechatFridsView;
    protected FrameLayout qqView;
    protected FrameLayout qzoneView;
    protected TextView celView;

    protected ShareCallBack shareCallBack;

    protected Context mContext;

    public ShareDialog(Context context, ShareCallBack callBack) {
        super(context, R.style.del_dialog_style);
        setOwnerActivity((Activity) context);
        this.shareCallBack = callBack;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window win = getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        setContentView(R.layout.share_dialog_layout);

        wechatView = (FrameLayout)findViewById(R.id.share_wechat);
        wechatFridsView = (FrameLayout)findViewById(R.id.share_wechat_friends);
        qqView = (FrameLayout)findViewById(R.id.share_qq);
        qzoneView = (FrameLayout)findViewById(R.id.share_qzone);
        celView = (TextView)findViewById(R.id.share_cel);


        wechatView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismiss();
                if (shareCallBack != null) {
                    shareCallBack.wechatShare();
                }
            }
        });
        wechatFridsView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismiss();
                if(shareCallBack!=null){
                    shareCallBack.wechatFridsShare();
                }
            }
        });
        qqView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismiss();
                if(shareCallBack!=null){
                    shareCallBack.qqShare();
                }
            }
        });
        qzoneView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismiss();
                if(shareCallBack!=null){
                    shareCallBack.qzoneShare();
                }
            }
        });
        celView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismiss();
                if(shareCallBack!=null){
                    shareCallBack.cancel();
                }
            }
        });
    }
    public interface ShareCallBack{
        void cancel();
        void wechatShare();
        void wechatFridsShare();
        void qqShare();
        void qzoneShare();
    }
}
