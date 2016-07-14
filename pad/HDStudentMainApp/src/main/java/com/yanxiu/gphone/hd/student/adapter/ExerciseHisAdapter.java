package com.yanxiu.gphone.hd.student.adapter;

import android.app.Activity;

import com.yanxiu.gphone.hd.student.bean.DataTeacherEntity;
import com.yanxiu.gphone.hd.student.utils.Util;

/**
 * Created by Administrator on 2016/2/16.
 */
public class ExerciseHisAdapter extends CommonListAdapter {

    public ExerciseHisAdapter(Activity context) {
        super(context);

    }

    @Override
    protected void setData(ViewHolder viewHolder,int position) {
        super.setData(viewHolder,position);
        DataTeacherEntity data = (DataTeacherEntity) mList.get(position);
        viewHolder.materialName.setText(data.getName());
        if (data.getData() != null) {
            viewHolder.materialSubName.setText(data.getData().getEditionName());
        }
        int idRes = Util.getIconRes(data.getId());
        viewHolder.materialIcon.setBackgroundResource(idRes);
    }
}
