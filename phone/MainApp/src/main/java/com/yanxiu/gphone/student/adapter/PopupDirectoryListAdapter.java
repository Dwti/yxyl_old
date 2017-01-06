package com.yanxiu.gphone.student.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.PhotoDirectory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 16/12/30.
 */
public class PopupDirectoryListAdapter extends BaseAdapter {

    private Context mContext;
    private List<PhotoDirectory> directories = new ArrayList<>();
    private int currentSelectPos = 0;

    public PopupDirectoryListAdapter(Context context, List<PhotoDirectory> directories) {
        this.directories = directories;
        this.mContext = context;
    }


    @Override
    public int getCount() {
        return directories.size();
    }


    @Override
    public PhotoDirectory getItem(int position) {
        return directories.get(position);
    }


    @Override
    public long getItemId(int position) {
        return directories.get(position).hashCode();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
            convertView = mLayoutInflater.inflate(R.layout.picker_item_directory, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bindData(directories.get(position));
        if (position == currentSelectPos) {
            holder.iv_select_flag.setVisibility(View.VISIBLE);
        } else {
            holder.iv_select_flag.setVisibility(View.GONE);
        }
        if(position == getCount() -1){
            holder.divider.setVisibility(View.INVISIBLE);
        }else {
            holder.divider.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public void updateSelectPos(int position) {
        if (position >= 0 && position < getCount()){
            currentSelectPos = position;
            notifyDataSetChanged();
        }
    }

    private class ViewHolder {

        public ImageView iv_cover;
        public TextView tv_name;
        public TextView tv_count;
        public ImageView iv_select_flag;
        public View divider;

        public ViewHolder(View rootView) {
            iv_cover = (ImageView) rootView.findViewById(R.id.iv_dir_cover);
            tv_name = (TextView) rootView.findViewById(R.id.tv_dir_name);
            tv_count = (TextView) rootView.findViewById(R.id.tv_dir_count);
            iv_select_flag = (ImageView) rootView.findViewById(R.id.iv_select_flag);
            divider = rootView.findViewById(R.id.divider);
        }

        public void bindData(PhotoDirectory directory) {
            Glide.with(mContext).load(directory.getCoverPath())
                    .dontAnimate()
                    .thumbnail(0.1f)
                    .into(iv_cover);
            tv_name.setText(directory.getName());
            tv_count.setText(tv_count.getContext().getString(R.string.picker_image_count, directory.getPhotos().size()));
        }
    }

}
