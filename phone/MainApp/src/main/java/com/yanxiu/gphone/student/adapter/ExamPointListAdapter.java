package com.yanxiu.gphone.student.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.ExamPointEntity;
import com.common.core.utils.CommonCoreUtil;


/**
 * Created by Administrator on 2015/11/16.
 */
public class ExamPointListAdapter extends YXiuCustomerBaseAdapter<ExamPointEntity> {

    public ExamPointListAdapter(Activity context) {
        super(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(mContext, R.layout.exam_list_adapter,null);
            holder.itemRelative=(RelativeLayout)convertView.findViewById(R.id.itemRelative);
            holder.titleText=(TextView)convertView.findViewById(R.id.titleText);
            holder.itemTitleText=(TextView)convertView.findViewById(R.id.itemTitleText);
            holder.sectionValueText=(TextView)convertView.findViewById(R.id.sectionValueText);
            holder.itemValueText=(TextView)convertView.findViewById(R.id.itemValueText);
            holder.itemFlag=(ImageView)convertView.findViewById(R.id.itemFlag);
            holder.itemLineView=convertView.findViewById(R.id.itemLineView);
            holder.childRelative=(RelativeLayout)convertView.findViewById(R.id.childRelative);
            holder.adapterRootView=convertView.findViewById(R.id.adapter_rootView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        ExamPointEntity entity=getItem(position);
        switch (entity.getType()){
            case ExamPointEntity.SECTION:
                setSelectionParams(holder,entity,position);
                break;
            case ExamPointEntity.ITEM:
                setItemParams(holder,entity,position);
                break;
        }

        return convertView;
    }

    private void setItemParams(ViewHolder holder, ExamPointEntity entity, int position) {
        holder.childRelative.setVisibility(View.VISIBLE);
        holder.sectionValueText.setVisibility(View.GONE);
        holder.titleText.setVisibility(View.GONE);

        if(position<getCount()-1){
            if(getItem(position+1).getType()==ExamPointEntity.SECTION){
                holder.itemRelative.setBackgroundResource(R.drawable.bottom_line_bg);
                holder.itemLineView.setVisibility(View.GONE);
                holder.adapterRootView.setPadding(CommonCoreUtil.dipToPx(mContext, 15), 0, CommonCoreUtil.dipToPx(mContext, 15), CommonCoreUtil.dipToPx(mContext, 15));
            }else{
                holder.itemRelative.setBackgroundResource(R.drawable.center_line_bg);
                holder.itemLineView.setVisibility(View.VISIBLE);
                holder.adapterRootView.setPadding(CommonCoreUtil.dipToPx(mContext, 15), 0, CommonCoreUtil.dipToPx(mContext, 15),0);
            }
        }else if(position==getCount()-1){
            holder.itemRelative.setBackgroundResource(R.drawable.bottom_line_bg);
            holder.itemLineView.setVisibility(View.GONE);
            holder.adapterRootView.setPadding(CommonCoreUtil.dipToPx(mContext, 15), 0, CommonCoreUtil.dipToPx(mContext, 15), CommonCoreUtil.dipToPx(mContext, 15));

        }else{
            holder.itemRelative.setBackgroundResource(R.drawable.center_line_bg);
            holder.itemLineView.setVisibility(View.GONE);
            holder.adapterRootView.setPadding(CommonCoreUtil.dipToPx(mContext, 15), 0, CommonCoreUtil.dipToPx(mContext, 15), 0);
        }

        holder.itemTitleText.setTextColor(mContext.getResources().getColor(R.color.color_666252));
        holder.itemTitleText.setText(entity.getName());
        holder.itemValueText.setText(entity.getValue());




    }

    private void setSelectionParams(ViewHolder holder, ExamPointEntity entity,int position) {

        if(position==0){
            holder.adapterRootView.setPadding(CommonCoreUtil.dipToPx(mContext, 15), CommonCoreUtil.dipToPx(mContext, 15), CommonCoreUtil.dipToPx(mContext, 15),0);
        }else{
            holder.adapterRootView.setPadding(CommonCoreUtil.dipToPx(mContext, 15), 0, CommonCoreUtil.dipToPx(mContext, 15), 0);

        }
        holder.childRelative.setVisibility(View.GONE);

        holder.titleText.setVisibility(View.VISIBLE);
        holder.sectionValueText.setVisibility(View.VISIBLE);


        holder.itemRelative.setBackgroundResource(R.drawable.half_corner_line_bg);
        holder.titleText.setTextColor(mContext.getResources().getColor(R.color.color_b2ab8f));



        holder.titleText.setText(entity.getName());
        holder.sectionValueText.setText(entity.getValue());
    }

    private  ViewGroup.LayoutParams getLayParams(int hValue){
        AbsListView.LayoutParams layoutParams=new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,hValue);
        return layoutParams;
    }


    class ViewHolder{
        public View adapterRootView;
        public RelativeLayout itemRelative;
        public RelativeLayout childRelative;
        public TextView titleText;
        public TextView itemTitleText;
        public TextView sectionValueText;
        public TextView itemValueText;
        public ImageView itemFlag;
        public View itemLineView;
    }

}
