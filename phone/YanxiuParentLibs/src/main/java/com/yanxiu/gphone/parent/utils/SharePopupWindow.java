package com.yanxiu.gphone.parent.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.core.utils.BasePopupWindow;
import com.common.share.ShareEnums;
import com.yanxiu.gphone.parent.R;

/**
 * Created by lee on 16-3-24.
 */
public class SharePopupWindow extends BasePopupWindow {
    private ShareItemSelListener shareItemSelListener;
    private final static String TAG=SharePopupWindow.class.getSimpleName();
    public SharePopupWindow(Context mContext) {
        super(mContext);
    }
    public interface ShareItemSelListener{
        void onShareItemSel(ShareEnums shareEnums);
    }
    @Override
    protected void initView(Context mContext) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.share_popup_window_layout,null);
        ImageView qqShareImg,qqZoneShareImg,weixinShareImg,weixinFriendsImg;
        TextView cancelShareTv;
        qqShareImg=(ImageView)view.findViewById(R.id.qqImg);
       // qqZoneShareImg=(ImageView)view.findViewById(R.id.qqZoneImg);
        weixinFriendsImg=(ImageView)view.findViewById(R.id.weixinFriendsImg) ;
        weixinShareImg=(ImageView)view.findViewById(R.id.weixinImg) ;
        cancelShareTv=(TextView)view.findViewById(R.id.cancelShareTv);
        qqShareImg.setOnClickListener(this);
       // qqZoneShareImg.setOnClickListener(this);
        weixinFriendsImg.setOnClickListener(this);
        weixinShareImg.setOnClickListener(this);
        cancelShareTv.setOnClickListener(this);
        this.pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.pop.setContentView(view);
    }
    public void setShareItemSelListener(ShareItemSelListener shareLitener){
        this.shareItemSelListener=shareLitener;
    }
    @Override
    public void loadingData() {

    }

    @Override
    protected void destoryData() {

    }

    @Override
    public void onClick(View v) {
           if(v.getId()==R.id.qqImg){
               if(shareItemSelListener!=null){
                   shareItemSelListener.onShareItemSel(ShareEnums.QQ);
               }
           }else if(v.getId()==R.id.weixinImg){
               if(shareItemSelListener!=null){
                   shareItemSelListener.onShareItemSel(ShareEnums.WEIXIN);
               }
           }else if(v.getId()==R.id.weixinFriendsImg){
               if(shareItemSelListener!=null){
                   shareItemSelListener.onShareItemSel(ShareEnums.WEIXIN_FRIENDS);
               }
           }else if(v.getId()==R.id.cancelShareTv){
               dismiss();
           }

//           else if(v.getId()==R.id.qqZoneImg){
//               if(shareItemSelListener!=null){
//                   shareItemSelListener.onShareItemSel(ShareEnums.QQZONE);
//               }
//           }
    }
}
