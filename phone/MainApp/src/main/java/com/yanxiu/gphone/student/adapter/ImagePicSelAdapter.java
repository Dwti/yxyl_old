package com.yanxiu.gphone.student.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.ImageBucketActivity;
import com.common.core.utils.CommonCoreUtil;
import com.yanxiu.gphone.student.view.picsel.bean.ImageItem;
import com.yanxiu.gphone.student.view.picsel.inter.PicNumListener;
import com.yanxiu.gphone.student.view.picsel.utils.BitmapCache;
import com.yanxiu.gphone.student.view.picsel.utils.ShareBitmapUtils;
import com.common.core.view.roundview.RoundedImageView;

/**
 * Created by Administrator on 2015/9/29.
 */
public class ImagePicSelAdapter extends YXiuCustomerBaseAdapter<ImageItem> {
    private BitmapCache cache;
    private PicNumListener mPicNumListener;
    private int columNum=3;
    private int screentWidth;
    private int itemWidth;
    public int lastSelectedPosition;   //记录上一次选中的位置

    public ImagePicSelAdapter(Activity context) {
        super(context);
        cache=new BitmapCache();
        screentWidth= CommonCoreUtil.getScreenWidth();
        int offset = (int)mContext.getResources().getDimension(R.dimen.dimen_15);
        itemWidth= (screentWidth-((columNum+1)*offset)) /columNum;

    }

    public void setPicNumListener(PicNumListener mPicNumListener){
        this.mPicNumListener=mPicNumListener;
    }

    BitmapCache.ImageCallback callback = new BitmapCache.ImageCallback() {
        @Override
        public void imageLoad(ImageView imageView, Bitmap bitmap,
                              Object... params) {
            if (imageView != null && bitmap != null) {
                String url = (String) params[0];
                if (url != null && url.equals(imageView.getTag())) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    };
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder mHolder;
        if(convertView==null){
            mHolder=new ViewHolder();
            convertView=View.inflate(mContext,R.layout.item_pic_sel_adapter,null);
            mHolder.pic=(RoundedImageView)convertView.findViewById(R.id.image);
            mHolder.pic.setCornerRadius(mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_12));
            mHolder.picDecorate=(ImageView)convertView.findViewById(R.id.imageDecorate);
            RelativeLayout.LayoutParams imageParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            imageParams.width=itemWidth;
            imageParams.height=itemWidth;
            mHolder.pic.setLayoutParams(imageParams);
            RelativeLayout.LayoutParams desParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            desParams.width=itemWidth;
            desParams.height=itemWidth;
            mHolder.picDecorate.setLayoutParams(desParams);
            convertView.setTag(mHolder);
        }else{
            mHolder= (ViewHolder) convertView.getTag();
        }

        final ImageItem item=getItem(position);
        mHolder.pic.setTag(item.getImagePath());
        cache.displayBmp(mHolder.pic, item.getThumbnailPath(), item.getImagePath(), callback);
        if(item.isSelected()){
            lastSelectedPosition=position;
            mHolder.picDecorate.setSelected(true);
        }else{
            mHolder.picDecorate.setSelected(false);
        }
//        mHolder.picDecorate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if(!mHolder.picDecorate.isSelected()){
//                    if(ShareBitmapUtils.getInstance().getRecordBucketPicSelNums()== ShareBitmapUtils.getInstance().getCountMax()){
//                        notifyNumChange(ShareBitmapUtils.getInstance().getRecordBucketPicSelNums());
//                        return;
//                    }
//                    item.setIsSelected(true);
//                    mHolder.picDecorate.setSelected(true);
//                    ImageBucketActivity.mTempDrrList.add(item.getImagePath());
//                    ShareBitmapUtils.getInstance().recordBucketPicSelNums++;
//                    notifyNumChange(ShareBitmapUtils.getInstance().getRecordBucketPicSelNums());
//                } else {
//                    item.setIsSelected(false);
//                    mHolder.picDecorate.setSelected(false);
//                    if(ImageBucketActivity.mTempDrrList.size()>0){
//                        ImageBucketActivity.mTempDrrList.remove(item.getImagePath());
//                        ShareBitmapUtils.getInstance().recordBucketPicSelNums--;
//                        notifyNumChange(ShareBitmapUtils.getInstance().getRecordBucketPicSelNums());
//                    }
//                }
//            }
//        });

        return convertView;
    }

    private void notifyNumChange(int count){
        if(mPicNumListener!=null){
            mPicNumListener.numCountCallBack(count);
        }
    }

    class ViewHolder{
        private RoundedImageView pic;
        private ImageView picDecorate;
    }

}
