package com.yanxiu.gphone.hd.student.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.bean.SubjectEditionBean;

/**SubjectVersionSortAdapter
 * Created by Administrator on 2015/7/20.
 */
public class SubjectVersionSortAdapter extends YXiuCustomerBaseAdapter<SubjectEditionBean.DataEntity.ChildrenEntity> {

    private Activity mContext;
    private ViewHolder holder;
    private int selectPosition = 0;
    private String volume;

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public SubjectVersionSortAdapter(Activity context) {
        super(context);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.item_subject_version, null);
            holder  = new ViewHolder();
            holder.ivSbuVerSel = (ImageView) row.findViewById(R.id.iv_subject_version_select);
            holder.tvSubVerName = (TextView) row.findViewById(R.id.tv_subject_version_name);
            row.setTag(holder);
            CommonCoreUtil.zoomViewHeight(60, row);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        if(selectPosition == position){
            holder.ivSbuVerSel.setVisibility(View.VISIBLE);
//            holder.tvSubVerName.setTextColor(mContext.getResources().getColor(R.color.color_ff00a0e6));
        }else{
            holder.ivSbuVerSel.setVisibility(View.INVISIBLE);
//            holder.tvSubVerName.setTextColor(mContext.getResources().getColor(R.color.color_ffdb4d));
        }

        SubjectEditionBean.DataEntity.ChildrenEntity entity = getItem(position);
        if(!TextUtils.isEmpty(volume)){
            if(volume.equals(entity.getId())){
                holder.ivSbuVerSel.setVisibility(View.VISIBLE);
//                holder.tvSubVerName.setTextColor(mContext.getResources().getColor(R.color.color_ff00a0e6));
            }else{
                holder.ivSbuVerSel.setVisibility(View.INVISIBLE);
//                holder.tvSubVerName.setTextColor(mContext.getResources().getColor(R.color.color_ffdb4d));
            }
        }
        holder.tvSubVerName.setText(entity.getName());
        return row;
    }

    static class ViewHolder {
        TextView tvSubVerName;
        ImageView ivSbuVerSel;
    }

}
