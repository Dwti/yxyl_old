package com.yanxiu.gphone.hd.student.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.hd.student.R;

/**
 * Created by Administrator on 2016/2/16.
 */
public class CommonListAdapter  extends YXiuCustomerBaseAdapter {
    private LayoutInflater layoutInflater;
    public CommonListAdapter(Activity context) {
        super(context);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = null;
        if( null == convertView){
            mViewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.search_scholl_item_layout, null);
            mViewHolder.materialName = (TextView) convertView.findViewById(R.id.item_name);
            mViewHolder.materialSubName = (TextView) convertView.findViewById(R.id.item_sub_content);
            mViewHolder.materialIcon = (ImageView) convertView.findViewById(R.id.item_icon);
            mViewHolder.spaceView = convertView.findViewById(R.id.top_space_view);
            mViewHolder.bootmSpaceView = convertView.findViewById(R.id.bottom_space_view);
            mViewHolder.materialDividerLine = (ImageView) convertView.findViewById(R.id.item_divider_line);
//            Util.setViewTypeface(YanxiuTypefaceTextView.TypefaceType.FANGZHENG, mViewHolder.materialName,
//                    mViewHolder.materialSubName);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        if(position == 0){
            mViewHolder.spaceView.setVisibility(View.VISIBLE);
        } else{
            mViewHolder.spaceView.setVisibility(View.GONE);
        }
        if((position+1)>= getCount()){
            mViewHolder.materialDividerLine.setVisibility(View.GONE);
            mViewHolder.bootmSpaceView.setVisibility(View.VISIBLE);
        } else {
            if(mViewHolder.materialDividerLine.getVisibility() == View.GONE) {
                mViewHolder.materialDividerLine.setVisibility(View.VISIBLE);
            }
            if(mViewHolder.bootmSpaceView.getVisibility() == View.VISIBLE) {
                mViewHolder.bootmSpaceView.setVisibility(View.GONE);
            }
        }
        setData(mViewHolder,position);
        return convertView;
    }

    protected void  setData(ViewHolder viewHolder,int position){

    }


    public final class ViewHolder{
        public TextView materialName;
        public TextView materialSubName;
        public View spaceView;
        public View bootmSpaceView;
        public ImageView materialIcon;
        public ImageView materialDividerLine;
    }
}
