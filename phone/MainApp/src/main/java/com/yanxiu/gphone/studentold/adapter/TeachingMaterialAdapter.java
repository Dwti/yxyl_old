package com.yanxiu.gphone.studentold.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.activity.TeachingMaterialActivity;
import com.yanxiu.gphone.studentold.bean.DataTeacherEntity;
import com.yanxiu.gphone.studentold.bean.SubjectVersionBean;
import com.yanxiu.gphone.studentold.utils.Util;

/**
 * Created by Administrator on 2015/7/13.
 */
public class TeachingMaterialAdapter extends YXiuCustomerBaseAdapter{
    private LayoutInflater layoutInflater;
    private int type;
    public TeachingMaterialAdapter(Activity context, int type) {
        super(context);
        this.type = type;
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
        if(type == TeachingMaterialActivity.PRACTICE_ERROR_COLLECTION_ACTIVITY){
            DataTeacherEntity data = (DataTeacherEntity) mList.get(position);
            mViewHolder.materialName.setText(data.getName());
            String wrongNum = data.getData().getWrongNum();
            String wrongNumStr = null;
            if(wrongNum != null) {
                if (wrongNum.length() == 1) {
                    wrongNumStr = "&#160;&#160;"+wrongNum+"&#160;&#160;";
                } else if (wrongNum.length() == 2) {
                    wrongNumStr = "&#160;"+wrongNum+"&#160;";
                } else {
                    wrongNumStr = wrongNum;
                }
//                mViewHolder.materialSubName.setText(Html.fromHtml(mContext.getResources().getString(R.string.mistake_edition_num_txt) +
//                        "<font color='#ff0000'>" + wrongNumStr + "</font>"));
                mViewHolder.materialSubName.setText(Html.fromHtml("<font color='#b3476b'>" + wrongNumStr + "</font>"));
            } else {
                mViewHolder.materialSubName.setVisibility(View.INVISIBLE);
            }
            int idRes = Util.getIconRes(data.getId());
            mViewHolder.materialIcon.setBackgroundResource(idRes);
        } else if(type == TeachingMaterialActivity.PRACTICE_HISTORY_ACTIVITY) {
            DataTeacherEntity data = (DataTeacherEntity) mList.get(position);
            mViewHolder.materialName.setText(data.getName());
            if (data.getData() != null) {
                mViewHolder.materialSubName.setText(data.getData().getEditionName());
            }
            int idRes = Util.getIconRes(data.getId());
            mViewHolder.materialIcon.setBackgroundResource(idRes);
        } else if(type == TeachingMaterialActivity.MY_FAVOURITE_COLLECTION_ACTIVITY){
            DataTeacherEntity data = (DataTeacherEntity) mList.get(position);
            mViewHolder.materialName.setText(data.getName());
            String favoriteNum = data.getData().getFavoriteNum();
            String favoriteNumStr = null;
            if(favoriteNum != null) {
                if (favoriteNum.length() == 1) {
                    favoriteNumStr = "&#160;&#160;"+favoriteNum+"&#160;&#160;";
                } else if (favoriteNum.length() == 2) {
                    favoriteNumStr = "&#160;"+favoriteNum+"&#160;";
                } else {
                    favoriteNumStr = favoriteNum;
                }
//                mViewHolder.materialSubName.setText(Html.fromHtml(/*mContext.getResources().getString(R.string.favourite_edition_num_txt) +*/
//                        "<font color='#52c6fd'>" + favoriteNumStr + "</font>"));
                mViewHolder.materialSubName.setText(Html.fromHtml(favoriteNumStr));
            } else {
                mViewHolder.materialSubName.setVisibility(View.INVISIBLE);
            }
            int idRes =Util.getIconRes(data.getId());
            mViewHolder.materialIcon.setBackgroundResource(idRes);
        } else {
            SubjectVersionBean.DataEntity data = (SubjectVersionBean.DataEntity) mList.get(position);
            mViewHolder.materialName.setText(data.getName());
            if (data.getData() != null) {
                mViewHolder.materialSubName.setText(data.getData().getEditionName());
            }else {
                mViewHolder.materialSubName.setText(R.string.subject_can_not_selected);
            }
            int idRes = Util.getIconRes(data.getId());
            mViewHolder.materialIcon.setBackgroundResource(idRes);
        }
        return convertView;
    }

    public final class ViewHolder{
        public TextView materialName;
        private TextView materialSubName;
        private View spaceView;
        private View bootmSpaceView;
        public ImageView materialIcon;
        public ImageView materialDividerLine;
    }
}
