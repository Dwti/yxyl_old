package com.yanxiu.gphone.hd.student.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.View;

import com.yanxiu.gphone.hd.student.bean.DataTeacherEntity;
import com.yanxiu.gphone.hd.student.utils.Util;

/**
 * Created by Administrator on 2016/2/16.
 */
public class MyErrorRecordAdapter extends CommonListAdapter {

    public MyErrorRecordAdapter(Activity context) {
        super(context);
    }

    @Override
    protected void setData(ViewHolder viewHolder, int position) {
        super.setData(viewHolder, position);
        DataTeacherEntity data = (DataTeacherEntity) mList.get(position);
        viewHolder.materialName.setText(data.getName());
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
            viewHolder.materialSubName.setText(Html.fromHtml("<font color='#b3476b'>" + wrongNumStr + "</font>"));
        } else {
            viewHolder.materialSubName.setVisibility(View.INVISIBLE);
        }
        int idRes = Util.getIconRes(data.getId());
        viewHolder.materialIcon.setBackgroundResource(idRes);
    }
}
