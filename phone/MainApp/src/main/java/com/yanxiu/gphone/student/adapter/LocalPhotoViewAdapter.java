package com.yanxiu.gphone.student.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.common.core.utils.LogInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.DeleteImageBean;
import com.yanxiu.gphone.student.fragment.question.SubjectiveQuestionFragment;
import com.yanxiu.gphone.student.utils.CorpUtils;
import com.yanxiu.gphone.student.view.ZoomImageView;
import com.yanxiu.gphone.student.view.picsel.bean.LocalImageView;
import com.yanxiu.gphone.student.view.picsel.utils.ShareBitmapUtils;

import java.io.IOException;

import de.greenrobot.event.EventBus;


/**
 * Created by Administrator on 2015/10/10.
 */
public class LocalPhotoViewAdapter extends PagerAdapter {
    private Context context;
    private View mCurrentView;
    private DisplayImageOptions options;
    private static  final String TAG=LocalPhotoViewAdapter.class.getSimpleName();
    public LocalPhotoViewAdapter(Context context) {
        this.context = context;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)                       // 设置下载的图片是否缓存在SD卡中
                .showImageOnFail(R.drawable.image_default)
                .showImageForEmptyUri(R.drawable.image_default)
                .build();                                   // 创建配置过得DisplayImageOption对象
    }


    @Override
    public int getCount() {
        return ShareBitmapUtils.getInstance().getDrrMaps().get(ShareBitmapUtils.getInstance().getCurrentSbId()).size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view== object;
    }
    public View getPrimaryItem() {
        return mCurrentView;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=View.inflate(context, R.layout.item_photo_view,null);
        ZoomImageView imageView=(ZoomImageView) view.findViewById(R.id.iv_photo_view);
        if(ShareBitmapUtils.getInstance().getDrrMaps().get(ShareBitmapUtils.getInstance().getCurrentSbId()).size()>0){
            String path=ShareBitmapUtils.getInstance().getDrrMaps().get(ShareBitmapUtils.getInstance().getCurrentSbId()).get(position);
            Glide.with(context).load(path).asBitmap()
                    .placeholder(R.drawable.image_default)
                    .error(R.drawable.image_default)
                    .into(imageView);
        }
        container.addView(view, 0);
        return view;
    }
    public void deleteItem(int position){
        if(ShareBitmapUtils.getInstance().getDrrMaps().get(ShareBitmapUtils.getInstance().getCurrentSbId()).size()>0){
            DeleteImageBean bean=new DeleteImageBean();
            bean.setType(SubjectiveQuestionFragment.TYPE);
            bean.setImage_url(ShareBitmapUtils.getInstance().getDrrMaps().get(ShareBitmapUtils.getInstance().getCurrentSbId()).get(position));
            CorpUtils.getInstence().getCorpListener().ondelete(bean);
            ShareBitmapUtils.getInstance().getDrrMaps().get(ShareBitmapUtils.getInstance().getCurrentSbId()).remove(position);
            int loadIndex=ShareBitmapUtils.getInstance().getListIndexMaps().get(ShareBitmapUtils.getInstance().getCurrentSbId());
            loadIndex--;
            ShareBitmapUtils.getInstance().getListIndexMaps().put(ShareBitmapUtils.getInstance().getCurrentSbId(),loadIndex);
            LogInfo.log(TAG, "del: List_Index: " + loadIndex);
            notifyDataSetChanged();
        }
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((View) object));
    }
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentView = (View)object;
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
