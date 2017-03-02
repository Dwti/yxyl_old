package com.yanxiu.gphone.student.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.common.core.view.roundview.RoundedImageView;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.view.PhotoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 17-3-2.
 */

public class NoteImageGridAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private int imgWidth;
    private ArrayList<String> data = new ArrayList<>();

    public NoteImageGridAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        imgWidth = Util.convertDpToPx(context, 35);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.no_scroll_grid_item,
                    parent, false);
            holder = new ViewHolder();
            holder.image = (RoundedImageView) convertView
                    .findViewById(R.id.item_grida_image);
            holder.image.setCornerRadius(context.getResources().getDimensionPixelOffset(R.dimen.dimen_10));
            holder.decorateImage = (ImageView) convertView.findViewById(R.id.item_grid_decorate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.decorateImage.setBackgroundResource(R.drawable.upload_pic);
        String absolutePath = data.get(position);
        Glide.with(context)
                .load(absolutePath).asBitmap()
                .placeholder(R.drawable.image_default)
                .error(R.drawable.image_default)
                .override(imgWidth, imgWidth).into(holder.image);

        return convertView;
    }

    public void setData(List<String> data){
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(List<String> data){
        this.data.addAll(data);
        notifyDataSetChanged();
    }
    public void addData(String string){
        this.data.add(string);
        notifyDataSetChanged();
    }
    public void clearData(){
        this.data.clear();
        notifyDataSetChanged();
    }
    public ArrayList<String> getData(){
        return data;
    }
    private class ViewHolder {
        public RoundedImageView image;
        public ImageView decorateImage;

    }
}
