package com.yanxiu.gphone.student.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.common.core.utils.LogInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.DeleteImageBean;
import com.yanxiu.gphone.student.fragment.question.SubjectiveQuestionFragment;
import com.yanxiu.gphone.student.utils.CorpUtils;
import com.yanxiu.gphone.student.view.ZoomImageView;
import com.yanxiu.gphone.student.view.picsel.utils.ShareBitmapUtils;

import java.util.List;

/**
 * Created by sp on 17-3-1.
 */

public class ImagePagerAdapter extends PagerAdapter {
    private Context context;
    private List<String> paths;
    public ImagePagerAdapter(Context context,List<String> paths) {
        this.context = context;
        this.paths = paths;
    }


    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view== object;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=View.inflate(context, R.layout.item_photo_view,null);
        ZoomImageView imageView=(ZoomImageView) view.findViewById(R.id.iv_photo_view);
            String path=paths.get(position);
            Glide.with(context).load(path).asBitmap()
//                    .placeholder(R.drawable.image_default)
                    .error(R.drawable.load_image_error)
                    .into(imageView);
        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((View) object));
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
