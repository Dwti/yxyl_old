package com.yanxiu.gphone.hd.student.adapter;

import android.app.Activity;

import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.bean.SubjectVersionBean;
import com.yanxiu.gphone.hd.student.utils.Util;


/**
 * Created by Administrator on 2015/7/13.
 */
public class TeachingMaterialAdapter extends CommonListAdapter{

    public TeachingMaterialAdapter(Activity context) {
        super(context);
    }

    @Override
    protected void setData(ViewHolder viewHolder, int position) {
        super.setData(viewHolder, position);
        SubjectVersionBean.DataEntity data = (SubjectVersionBean.DataEntity) mList.get(position);
        viewHolder.materialName.setText(data.getName());
        if (data.getData() != null) {
            viewHolder.materialSubName.setText(data.getData().getEditionName());
        }else {
            viewHolder.materialSubName.setText(R.string.subject_can_not_selected);
        }
        int idRes = Util.getIconRes(data.getId());
        viewHolder.materialIcon.setBackgroundResource(idRes);
    }
}
