package com.yanxiu.gphone.hd.student.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.common.core.view.roundview.RoundedImageView;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.view.picsel.bean.ImageBucket;
import com.yanxiu.gphone.hd.student.view.picsel.utils.BitmapCache;


/**
 * Created by Administrator on 2015/9/28.
 */
public class ImageBucketAdapter extends YXiuCustomerBaseAdapter<ImageBucket> {
    private static final String TAG=ImageBucketAdapter.class.getSimpleName();

    private BitmapCache cache;
    public ImageBucketAdapter(Activity context) {
        super(context);
        cache=new BitmapCache();
    }
    BitmapCache.ImageCallback callback = new BitmapCache.ImageCallback() {
        @Override
        public void imageLoad(ImageView imageView, Bitmap bitmap,
                              Object... params) {
            if (imageView != null && bitmap != null) {
                String url = (String) params[0];
                if (url != null && url.equals(imageView.getTag())) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    LogInfo.log(TAG, "callback, bmp not match");
                }
            } else {
                LogInfo.log(TAG, "callback, bmp null");
            }
        }
    };
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(mContext, R.layout.image_bucket_adapter,null);
            holder.iv=(RoundedImageView)convertView.findViewById(R.id.image);
            holder.iv.setCornerRadius(mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_16));
            holder.name=(TextView)convertView.findViewById(R.id.name);
            holder.count=(TextView)convertView.findViewById(R.id.count);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        ImageBucket item = getItem(position);
        holder.count.setText("" + item.count);
        if(!StringUtils.isEmpty(item.getBucketName())){
            holder.name.setText(item.getBucketName());
        }else{
            holder.name.setText(mContext.getResources().getString(R.string.no_data_show));
        }

        if (item.getImageList() != null && item.getImageList().size() > 0) {
            String thumbPath = item.getImageList().get(0).getThumbnailPath();
            String sourcePath = item.getImageList().get(0).getImagePath();
            holder.iv.setTag(sourcePath);
            cache.displayBmp(holder.iv, thumbPath, sourcePath, callback);
        } else {
            holder.iv.setImageBitmap(null);
        }
        return convertView;
    }

    class ViewHolder {

        private RoundedImageView iv;
        private TextView name;
        private TextView count;
    }
}
