package com.yanxiu.gphone.student.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.ExamInfoBean;
import com.common.core.utils.StringUtils;
import com.common.core.utils.CommonCoreUtil;

/**
 * Created by Administrator on 2015/11/19.
 */
public class ThirdExamSiteAdapter extends  YXiuCustomerBaseAdapter<ExamInfoBean> {

    public ThirdExamSiteAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(mContext, R.layout.exam_list_adapter,null);
            holder.adapterRootView=convertView.findViewById(R.id.adapter_rootView);

            holder.itemRelative=(RelativeLayout)convertView.findViewById(R.id.itemRelative);

            holder.titleText=(TextView)convertView.findViewById(R.id.titleText);

            holder.sectionValueText=(TextView)convertView.findViewById(R.id.sectionValueText);

            holder.bottomLine=convertView.findViewById(R.id.itemLineView);


            holder.childItemRelative=(RelativeLayout)convertView.findViewById(R.id.childRelative);
            holder.itemTitleText=(TextView)convertView.findViewById(R.id.itemTitleText);
            holder.valueText=(TextView)convertView.findViewById(R.id.itemValueText);

            holder.childItemRelative.setVisibility(View.VISIBLE);


            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        ExamInfoBean infoBean=getItem(position);

        if(position==0){
            setSelectionParams(holder, infoBean);
        }else{
            setItemParams(holder, infoBean, position);
        }

        return convertView;
    }

    private void setItemParams(ViewHolder holder, ExamInfoBean infoBean, int position) {
        holder.childItemRelative.setVisibility(View.VISIBLE);

        holder.sectionValueText.setVisibility(View.GONE);
        holder.titleText.setVisibility(View.GONE);


        if(position==getCount()-1){
            holder.itemRelative.setBackgroundResource(R.drawable.bottom_line_bg);
            holder.adapterRootView.setPadding(CommonCoreUtil.dipToPx(mContext, 15), 0, CommonCoreUtil.dipToPx(mContext, 15), CommonCoreUtil.dipToPx(mContext, 15));
        }else{
            holder.itemRelative.setBackgroundResource(R.drawable.center_line_bg);
            holder.adapterRootView.setPadding(CommonCoreUtil.dipToPx(mContext, 15), 0, CommonCoreUtil.dipToPx(mContext, 15),0);
        }



        if(StringUtils.isEmpty(infoBean.getName())){
            holder.itemTitleText.setText(mContext.getResources().getString(R.string.no_data_show));
        }else{
            holder.itemTitleText.setText(infoBean.getName());
        }
        if(infoBean.getData()==null||StringUtils.isEmpty(infoBean.getData().getMasterLevel())){
            holder.valueText.setText(mContext.getResources().getString(R.string.no_data_show));
        }else{
            holder.valueText.setText(infoBean.getData().getMasterLevel());
        }

    }

    private void setSelectionParams(ViewHolder holder, ExamInfoBean infoBean) {

        holder.adapterRootView.setPadding(CommonCoreUtil.dipToPx(mContext, 15),0, CommonCoreUtil.dipToPx(mContext, 15), 0);
        holder.childItemRelative.setVisibility(View.GONE);

        holder.titleText.setVisibility(View.VISIBLE);
        holder.sectionValueText.setVisibility(View.VISIBLE);

        holder.itemRelative.setBackgroundResource(R.drawable.half_corner_line_bg);
        holder.titleText.setTextColor(mContext.getResources().getColor(R.color.color_b2ab8f));

        if(!StringUtils.isEmpty(infoBean.getName())){
            holder.titleText.setText(infoBean.getName());
        }

//        if(!StringUtils.isEmpty(infoBean.getData().getMasterNum())&&!StringUtils.isEmpty(infoBean.getData().getTotalNum())){
//            holder.sectionValueText.setText(infoBean.getData().getMasterNum() + "/" + infoBean.getData().getTotalNum());
//        }



    }



    private  ViewGroup.LayoutParams getLayParams(int hValue){
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,hValue);
        return layoutParams;
    }
    class ViewHolder{
        View adapterRootView;

        RelativeLayout itemRelative;

         TextView titleText;
         TextView sectionValueText;


        TextView itemTitleText;
        TextView valueText;
        RelativeLayout childItemRelative;

        View bottomLine;
    }

}
