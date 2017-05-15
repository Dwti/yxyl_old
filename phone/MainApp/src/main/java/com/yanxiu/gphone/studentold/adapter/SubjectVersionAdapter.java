package com.yanxiu.gphone.studentold.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.bean.SubjectEditionBean;


/**
 * Created by Administrator on 2015/7/7.
 */
public class SubjectVersionAdapter extends YXiuCustomerBaseAdapter<SubjectEditionBean.DataEntity> {

    private Activity mContext;
    private ViewHolder holder;
    private String editionId = null;
    private SelectPositionEntityListener mListener;
    private SubjectEditionBean.DataEntity selectedEntity;
//    private int selectedBg;
//    private int normalBg;
    public SubjectVersionAdapter(Activity context) {
        super(context);
        mContext = context;
//        selectedBg = mContext.getResources().getColor(R.color.color_ff00a0e6);
//        normalBg = mContext.getResources().getColor(R.color.color_ff323232);
    }

    public String getEditionId() {
        return editionId;
    }

    public SubjectEditionBean.DataEntity getSelectedEntity(){
        return selectedEntity;
    }
    public void setEditionId(String editionId) {
        this.editionId = editionId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.item_home_containter, null);
            holder  = new ViewHolder();
            holder.ivSbuVerSel = (ImageView) row.findViewById(R.id.iv_subject_version_select);
            holder.tvSubVerName = (TextView) row.findViewById(R.id.tv_subject_version_name);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        SubjectEditionBean.DataEntity entity = getItem(position);
//        if(editionId != null && editionId.equals(entity.getId())){
//            selectedEntity = entity;
//            holder.ivSbuVerSel.setVisibility(View.VISIBLE);
//            holder.tvSubVerName.setTextColor(selectedBg);
//        }else{
//            holder.ivSbuVerSel.setVisibility(View.INVISIBLE);
//            holder.tvSubVerName.setTextColor(normalBg);
//        }
        holder.tvSubVerName.setText(entity.getName());
        return row;
    }


    static class ViewHolder {
        TextView tvSubVerName;
        ImageView ivSbuVerSel;
    }

    public interface SelectPositionEntityListener{
        void onSelectionPosition(SubjectEditionBean.DataEntity entity);
    }

    public SelectPositionEntityListener getmListener() {
        return mListener;
    }

    public void setmListener(SelectPositionEntityListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void setListView(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogInfo.log("geny", "setListView-----setDataSources----" + "---" + position);
                editionId = getItem(position).getId();
                if(mListener != null){
                    mListener.onSelectionPosition(getItem(position));
                }
                SubjectVersionAdapter.this.notifyDataSetChanged();
            }
        });
    }
}
