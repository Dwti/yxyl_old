package com.yanxiu.gphone.student.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.SubjectEditionBean;


/**
 * Created by Administrator on 2015/7/7.
 */
public class SubjectVersionMyModuleAdapter extends YXiuCustomerBaseAdapter<SubjectEditionBean.DataEntity> {

    private Activity mContext;
    private ViewHolder holder;
    private String editionId = null;
    private SelectPositionEntityListener mListener;
    private SubjectEditionBean.DataEntity selectedEntity;
    public SubjectVersionMyModuleAdapter (Activity context) {
        super(context);
        mContext = context;
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
            row = inflater.inflate(R.layout.item_version_my_module_containter, null);
            holder  = new ViewHolder();
            holder.ivSbuVerSel = (ImageView) row.findViewById(R.id.iv_subject_version_select);
            holder.tvSubVerName = (TextView) row.findViewById(R.id.tv_subject_version_name);
            holder.dashLine = row.findViewById(R.id.dash_line);
            holder.spaceView = row.findViewById(R.id.top_space_view);
            holder.bottomSpaceView = row.findViewById(R.id.bottom_space_view);
//            Util.setViewTypeface(YanxiuTypefaceTextView.TypefaceType.FANGZHENG, holder.tvSubVerName);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        SubjectEditionBean.DataEntity entity = getItem(position);
        if(position == 0){
            holder.spaceView.setVisibility(View.VISIBLE);
        } else{
            holder.spaceView.setVisibility(View.GONE);
        }
        if((position + 1) >= getCount()){
            holder.dashLine.setVisibility(View.GONE);
            holder.bottomSpaceView.setVisibility(View.VISIBLE);
        } else {
            if(holder.bottomSpaceView.getVisibility() == View.VISIBLE) {
                holder.bottomSpaceView.setVisibility(View.GONE);
            }
            if(holder.dashLine.getVisibility() == View.GONE) {
                holder.dashLine.setVisibility(View.VISIBLE);
            }
        }
        if(editionId != null && editionId.equals(entity.getId())){
            selectedEntity = entity;
            holder.ivSbuVerSel.setVisibility(View.VISIBLE);
        } else{
            holder.ivSbuVerSel.setVisibility(View.INVISIBLE);
        }
        holder.tvSubVerName.setText(entity.getName());
        return row;
    }


    static class ViewHolder {
        TextView tvSubVerName;
        ImageView ivSbuVerSel;
        View dashLine;
        View spaceView;
        View bottomSpaceView;
    }

    public interface SelectPositionEntityListener{
        void onSelectionPosition (SubjectEditionBean.DataEntity entity);
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
                SubjectVersionMyModuleAdapter.this.notifyDataSetChanged();
            }
        });
    }
}
