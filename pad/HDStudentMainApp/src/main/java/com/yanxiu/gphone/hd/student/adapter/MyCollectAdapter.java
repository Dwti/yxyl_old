package com.yanxiu.gphone.hd.student.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.View;

import com.yanxiu.gphone.hd.student.bean.DataTeacherEntity;
import com.yanxiu.gphone.hd.student.utils.Util;

/**
 * Created by Administrator on 2016/2/16.
 */
public class MyCollectAdapter extends CommonListAdapter  {

    public MyCollectAdapter(Activity context) {
        super(context);
    }
    @Override
    protected void setData(ViewHolder viewHolder, int position) {
        super.setData(viewHolder, position);
        DataTeacherEntity data = (DataTeacherEntity) mList.get(position);
        viewHolder.materialName.setText(data.getName());
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
            viewHolder.materialSubName.setText(Html.fromHtml(favoriteNumStr));
        } else {
            viewHolder.materialSubName.setVisibility(View.INVISIBLE);
        }
        int idRes = Util.getIconRes(data.getId());
        viewHolder.materialIcon.setBackgroundResource(idRes);
    }
}
