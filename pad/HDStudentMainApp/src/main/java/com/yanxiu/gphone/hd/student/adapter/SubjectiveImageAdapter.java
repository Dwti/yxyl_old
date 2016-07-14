package com.yanxiu.gphone.hd.student.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.common.core.view.roundview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yanxiu.gphone.hd.student.R;


/**
 * Created by Administrator on 2015/7/7.
 */
public class SubjectiveImageAdapter extends YXiuCustomerBaseAdapter<String> {

    private Activity mContext;
    private ViewHolder holder;
    private DisplayImageOptions options;
    public SubjectiveImageAdapter(Activity context) {
        super(context);
        mContext = context;
        options = new DisplayImageOptions.Builder()
                                                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                                                .cacheOnDisk(true)                       // 设置下载的图片是否缓存在SD卡中
                                                .build();                                   // 创建配置过得DisplayImageOption对象
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.item_subjective_image, null);
            holder  = new ViewHolder();
            holder.ivSubjective = (RoundedImageView) row.findViewById(R.id.iv_subjective_image);
            holder.ivSubjective.setCornerRadius(mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_10));
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        String data = mList.get(position);
//        holder.ivSubjective.setImageUrl(data, YanxiuApplication.getInstance().getImageLoader());
        ImageLoader.getInstance().displayImage(data, holder.ivSubjective, options);
//        holder.tvSname.setText(data.getName());
//        if(data.getData() != null){
//            holder.tvEname.setText(data.getData().getEditionName());
//        }else {
//            holder.tvEname.setText(R.string.subject_can_not_selected);
//        }
//        int idRes = Util.getIconRes(data.getId());
//        holder.ivIcon.setBackgroundResource(idRes);
        return row;
    }


    static class ViewHolder {
        RoundedImageView ivSubjective;
    }

}
