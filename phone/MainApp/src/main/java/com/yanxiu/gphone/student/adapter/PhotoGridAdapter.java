package com.yanxiu.gphone.student.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.Photo;

import java.util.List;

/**
 * Created by sunpeng on 2016/12/30.
 */

public class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridAdapter.PhotoViewHolder> {
    private Context mContext;
    private List<Photo> photos;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;
    private int width;

    public PhotoGridAdapter(Context context, List<Photo> photos) {
        this.mContext = context;
        this.photos = photos;
        inflater = LayoutInflater.from(context);
        width = context.getResources().getDisplayMetrics().widthPixels;
        width = width / 3;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoViewHolder(inflater.inflate(R.layout.item_photo_grid, null));
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {
        Glide.with(mContext)
                .load(photos.get(position).getPath()).asBitmap()
//                .placeholder(R.drawable.__picker_ic_photo_black_48dp)
//                .error(R.drawable.__picker_ic_broken_image_black_48dp)
                .override(width,width).into(holder.iv_photo);
        if(onItemClickListener != null){
            holder.iv_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.itemView,position,photos.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_photo;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            iv_photo = (ImageView) itemView.findViewById(R.id.iv_photo);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position,Photo photo);
    }

}
