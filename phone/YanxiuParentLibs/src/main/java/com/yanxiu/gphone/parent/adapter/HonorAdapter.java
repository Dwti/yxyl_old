package com.yanxiu.gphone.parent.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.common.login.LoginModel;
import com.common.login.model.ParentInfo;
import com.common.share.ShareEnums;
import com.common.share.ShareExceptionEnums;
import com.common.share.ShareManager;
import com.common.share.constants.ShareConstants;
import com.common.share.inter.ShareResultCallBackListener;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.bean.ParentItemHonorBean;
import com.yanxiu.gphone.parent.inter.ListItemInter;
import com.yanxiu.gphone.parent.utils.ParentUtils;
import com.yanxiu.gphone.parent.utils.SharePopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lee on 16-3-22.
 */
public class HonorAdapter extends RecyclerView.Adapter<HonorAdapter.HonorHolder> implements ListItemInter,SharePopupWindow.ShareItemSelListener  {
    private final Context mContext;
    private final SharePopupWindow mSharePop;
    private List mList;
    private int mPosition;
    private static final int IS_HEAD = 0;
    private static final int IS_NORMAL = 1;
    private static final int IS_FOOTER = 2;
    private static final String TAG=HonorHolder.class.getSimpleName();
    private String testUrl="";
    private String shareDes;
    private int aquType;
    public HonorAdapter(Context context){
        this.mContext=context;
        mSharePop=new SharePopupWindow(context);
        mSharePop.setShareItemSelListener(this);
    }


    private int getCurrentPosition(){
        return mPosition;
    }

    @Override
    public HonorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if(viewType == IS_HEAD || viewType == IS_FOOTER) {
            itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.week_report_recycleview_header, parent, false);
        } else {
            itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.honor_adapter_layout, parent, false);
        }
        return new HonorHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(HonorHolder holder, int position) {
        if((position == 0 && holder.viewType == IS_HEAD) || holder.viewType == IS_FOOTER ){
        } else {
            setListItemInfo(holder,position);
        }
    }

    private void setListItemInfo(HonorHolder holder, int position) {
        holder.honorSmallFlag.setTag(position-1);
        ParentItemHonorBean honorBean = (ParentItemHonorBean) getItem(position-1);
        if (!StringUtils.isEmpty(honorBean.getHonorname())) {
            holder.title.setText(honorBean.getHonorname());
        } else {
            holder.title.setText(R.string.unknow);
        }
        if (!StringUtils.isEmpty(honorBean.getHonordesc())) {
            holder.des.setText(honorBean.getHonordesc());
        } else {
            holder.des.setText(R.string.unknow);
        }
        if (!StringUtils.isEmpty(honorBean.getAcquiretimeStr())) {
            holder.date.setVisibility(View.VISIBLE);
            holder.date.setText(String.format(mContext.getResources().getString(
                    R.string.honor_get_time
            ), honorBean.getAcquiretimeStr()));
        } else {
            holder.date.setVisibility(View.GONE);
            holder.date.setText(String.format(mContext.getResources().getString(
                    R.string.honor_get_time),""));
        }
        if (!StringUtils.isEmpty(honorBean.getHasAcquire())) {
            if (ParentItemHonorBean.HAS_ACQUIRE.equals(honorBean.getHasAcquire())) {
                if (!StringUtils.isEmpty(honorBean.getHonortype())) {
                    holder.honorshowImg.setImageResource(ParentUtils.getSelHonorType(Integer.valueOf(honorBean.getHonortype())));
                }
                holder.honorSmallFlag.setVisibility(View.VISIBLE);
                holder.honorBigImgFlag.setVisibility(View.VISIBLE);
            } else {
                if (!StringUtils.isEmpty(honorBean.getHonortype())) {
                    ParentUtils.getNotSelHonorType(Integer.valueOf(honorBean.getHonortype()));
                    holder.honorshowImg.setImageResource(ParentUtils.getNotSelHonorType(Integer.valueOf(honorBean.getHonortype())));
                }
                holder.honorSmallFlag.setVisibility(View.GONE);
                holder.honorBigImgFlag.setVisibility(View.GONE);
            }
        } else {
            holder.honorSmallFlag.setVisibility(View.GONE);
            holder.honorBigImgFlag.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount () {
        if(mList != null && mList.size() > 0) {
            return mList.size()+2;
        } else {
            return 0;
        }
    }
    @Override
    public void setList(List list) {
        this.mList=list;
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return IS_HEAD;
        } else if((position+1) == getItemCount()){
            return IS_FOOTER;
        } else {
            return IS_NORMAL;
        }
    }
    @Override
    public List getList() {
        return mList;
    }

    @Override
    public Object getItem(int position) {
        return mList!=null?mList.get(position):null;
    }

    @Override
    public void onShareItemSel(ShareEnums shareEnums) {
        if(mSharePop.isShowing()){
            mSharePop.dismiss();
        }
        switch (shareEnums){
            case QQ:
                Bundle qqBundle=getQQShareParams();
                ShareManager.getInstance().onShare(mContext,ShareEnums.QQ,qqBundle,new ShareResultCallBackListener(){

                    @Override
                    public void notInstall() {
                        ParentUtils.showToast(R.string.no_install_qq_p);
                    }

                    @Override
                    public void shareException(ShareExceptionEnums exceptionEnums) {
                        LogInfo.log(TAG,"shareException: "+exceptionEnums);
                    }

                    @Override
                    public void shareSuccess(Object o) {

                    }

                    @Override
                    public void shareFailrue(Object o) {
                        LogInfo.log(TAG,"shareFailrue: ");
                    }
                });
                break;
            case QQZONE:
                Bundle qqZoneBundle=getQQZoneShareParams();
                ShareManager.getInstance().onShare(mContext,ShareEnums.QQZONE,qqZoneBundle,new ShareResultCallBackListener(){

                    @Override
                    public void notInstall() {
                        ParentUtils.showToast(R.string.no_install_qq_p);
                    }

                    @Override
                    public void shareException(ShareExceptionEnums exceptionEnums) {

                    }

                    @Override
                    public void shareSuccess(Object o) {

                    }

                    @Override
                    public void shareFailrue(Object o) {

                    }
                });
                break;
            case WEIXIN:
                Bundle weixinBundle=createWeiShareParams(ShareConstants.WEIXIN_SHARE_TYPE_TALK);
                ShareManager.getInstance().onShare(mContext,ShareEnums.WEIXIN,weixinBundle,new ShareResultCallBackListener(){

                    @Override
                    public void notInstall() {
                        ParentUtils.showToast(R.string.no_install_weixin_p);
                    }

                    @Override
                    public void shareException(ShareExceptionEnums exceptionEnums) {

                    }

                    @Override
                    public void shareSuccess(Object o) {

                    }

                    @Override
                    public void shareFailrue(Object o) {

                    }
                });
                break;
            case WEIXIN_FRIENDS:
                Bundle weixinfriends=createWeiShareParams(ShareConstants.WEIXIN_SHARE_TYPE_FRENDS);

                ShareManager.getInstance().onShare(mContext,ShareEnums.WEIXIN_FRIENDS,weixinfriends,new ShareResultCallBackListener(){

                    @Override
                    public void notInstall() {
                        ParentUtils.showToast(R.string.no_install_weixin_p);
                    }

                    @Override
                    public void shareException(ShareExceptionEnums exceptionEnums) {

                    }

                    @Override
                    public void shareSuccess(Object o) {

                    }

                    @Override
                    public void shareFailrue(Object o) {

                    }
                });
                break;
        }
    }

    private Bundle createWeiShareParams(int ShareType){
        Bundle bundle=new Bundle();
        bundle.putString(ShareConstants.WX_APPID_KEY,ShareConstants.STUDENT_WX_AppID);
        bundle.putInt(ShareConstants.WEIXIN_SHARE_TYPE,ShareType);
        bundle.putInt(ShareConstants.WEIXIN_SHARE_WAY_TYPE,ShareConstants.WEIXIN_SHARE_WAY_PIC);
        bundle.putString(ShareConstants.WEIXIN_SHARE_TITLE_KEY,getShareTitle());
        bundle.putString(ShareConstants.WEIXIN_SHARE_DES_KEY,shareDes);
        bundle.putString(ShareConstants.WEIXIN_SHARE_IMG_URL, testUrl);
        bundle.putInt(ShareConstants.WEIXIN_SHARE_IMG_ICON,ParentUtils.getAquShareIcon(aquType));
        return bundle;
    }



    private Bundle getQQZoneShareParams(){
        Bundle bundle = new Bundle();
        bundle.putString(ShareConstants.QQ_APPID_KEY,ShareConstants.STUDENT_QQ_AppID);
        bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
                QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        //这条分享消息被好友点击后的跳转URL。
        bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, testUrl);
        //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_	SUMMARY不能全为空，最少必须有一个是有值的。
        bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE,getShareTitle());
        //分享的图片URL
        ArrayList<String> icon = new ArrayList<>();
        icon.add(ParentUtils.getAquShareIconPath(aquType));
        bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, icon);
        //分享的消息摘要，最长50个字
        bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,shareDes);
        return  bundle;
    }

    private Bundle getQQShareParams(){
        Bundle bundle=new Bundle();
        bundle.putString(ShareConstants.QQ_APPID_KEY, ShareConstants.STUDENT_QQ_AppID);
//        //这条分享消息被好友点击后的跳转URL。
//        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL,testUrl);
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_	SUMMARY不能t全为空，最少必须有一个是有值的。
//        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, getShareTitle());
        bundle.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        //分享的图片URL
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,ParentUtils.getAquShareIconPath(aquType));
//        //分享的消息摘要，最长50个字
//        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY,shareDes);
        //手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, getShareTitle()+"\n"+shareDes);
        //标识该消息的来源应用，值为应用名称+AppId。
        bundle.putInt(QQShare.SHARE_TO_QQ_EXT_INT,QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        return bundle;
    }

    private String getShareTitle(){
        String nickName = null;
        if((LoginModel.getRoleUserInfoEntity())!=null){
            ParentInfo parentInfo=((ParentInfo) LoginModel.getRoleUserInfoEntity());
            if(parentInfo!=null&&parentInfo.getChild()!=null){
                if(!StringUtils.isEmpty(parentInfo.getChild().getNickname())){
                    nickName =parentInfo.getChild().getNickname();
                }
            }
        }
        if(StringUtils.isEmpty(nickName)){
            return mContext.getResources().getString(R.string.default_share_title_p);
        }
        return String.format(mContext.getResources().getString(R.string.share_title_p),nickName);
    }

    class HonorHolder extends RecyclerView.ViewHolder {
        final int viewType;
        TextView title,des,date;
        ImageView honorBigImgFlag,honorshowImg,honorSmallFlag;
        public HonorHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if(viewType != IS_HEAD && viewType != IS_FOOTER){
                title = (TextView) itemView.findViewById(R.id.title);
                des = (TextView) itemView.findViewById(R.id.des);
                date = (TextView) itemView.findViewById(R.id.date);
                honorBigImgFlag = (ImageView) itemView.findViewById(R.id.honorBigImageFlag);
                honorshowImg = (ImageView) itemView.findViewById(R.id.honorShowImg);
                honorSmallFlag = (ImageView) itemView.findViewById(R.id.honorSmallImageFlag);
                honorSmallFlag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        mPosition= (int) v.getTag();
                        ParentItemHonorBean honorBean  = (ParentItemHonorBean)getItem(getCurrentPosition());
                        StringBuilder dateBuilder=new StringBuilder();
                        if(!StringUtils.isEmpty(honorBean.getAcquiretimeStr())){
                            dateBuilder.append("\n").append(String.format(mContext.getResources().getString(R.string.share_date_p),honorBean.getAcquiretimeStr()));
                        }
                        shareDes=dateBuilder.toString();
                        aquType=Integer.valueOf(honorBean.getHonortype());

                            if (mSharePop.isShowing()) {
                                mSharePop.dismiss();
                            } else {
                                mSharePop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                            }

                    }
                });
            }
        }
    }
}
