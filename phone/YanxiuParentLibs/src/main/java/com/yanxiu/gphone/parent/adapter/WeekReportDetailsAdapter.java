package com.yanxiu.gphone.parent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.bean.ParentHwDetailBean;
import com.yanxiu.gphone.parent.utils.ParentUtils;
import com.yanxiu.gphone.parent.view.ColorArcProgressBar;

import java.util.ArrayList;

/**
 * Created by hai8108 on 16/3/23.
 */
public class WeekReportDetailsAdapter extends RecyclerView.Adapter<WeekReportDetailsAdapter.WeekReportDetailsViewHolder> {
    private static final int IS_NORMAL = 1;
    private static final int IS_HEADER = 2;
    private ArrayList<ParentHwDetailBean> modelDatas;
    private Context mContext;
    private int increaseColor, descreaseColor, normalColor;
    public WeekReportDetailsAdapter (Context mContext, ArrayList<ParentHwDetailBean> modelDatas){
        this.mContext = mContext;
        this.modelDatas = modelDatas;
        increaseColor = mContext.getResources().getColor(R.color.color_cc0000_p);
        descreaseColor = mContext.getResources().getColor(R.color.color_00cc66_p);
        normalColor = mContext.getResources().getColor(R.color.color_999999_p);
    }
    @Override
    public WeekReportDetailsViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View itemView = null;
        if(viewType == IS_HEADER){
            itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.week_report_recycleview_header, parent, false);
        } else {
            itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.activity_details_weekreport_item_layout, parent, false);
        }
        return new WeekReportDetailsViewHolder(itemView, viewType);
    }
    public void setList(ArrayList<ParentHwDetailBean> modelDatas){
        this.modelDatas = modelDatas;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder (WeekReportDetailsViewHolder holder, int position) {
        if(position == 0 && holder.viewType == IS_HEADER){

        } else {
            final ParentHwDetailBean modelBean = modelDatas.get(position - 1);
            holder.subjectNameTv.setText(modelBean.getSubjectname());
            ParentUtils.setIcon(modelBean.getSubjectid(), holder.subjectIcon);
            int textColor = ParentUtils.getSubjectTextColor(modelBean.getSubjectid());
            ParentUtils.setSubjectTextColor(modelBean.getSubjectid(), holder.subjectNameTv);

            holder.subjectAllCountTv.setText(Html.fromHtml(mContext.getString(R.string.yanxiu_parent_current_exercise_finished_txt) +
                    "<font color='#000000'><big><strong>" + "&#160;" + modelBean.getAnswernum() +
                    "&#160;" +
                    "</strong></big></font>" + mContext.getString(R.string.yanxiu_parent_exercise)));
            holder.leftPb.setCurrentValues((float) modelBean.getClassAvgRate() * 100);
            holder.middlePb.setCurrentValues((float) modelBean.getCorrectrate() * 100);
            holder.middlePb.setFontColor(textColor);
            holder.rightPb.setCurrentValues((float) modelBean.getIncreaserate() * 100);
            if (modelBean.getIncreaseFlag() == 1) {
                holder.rightPb.setSignTextProcess(true, increaseColor, "+");
                holder.rightPb.setFontColor(increaseColor);
            } else if(modelBean.getIncreaseFlag() == 2){
                holder.rightPb.setSignTextProcess(true, descreaseColor, "-");
                holder.rightPb.setFontColor(descreaseColor);
            } else {
                holder.rightPb.setSignTextProcess(false, normalColor, "");
                holder.rightPb.setFontColor(normalColor);
            }
//            LogInfo.log("haitian", "modelBean ="+modelBean.toString());
        }
    }

    @Override
    public int getItemCount () {
        if(modelDatas != null && modelDatas.size() > 0) {
            return modelDatas.size()+1;
        } else {
            return 0;
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return IS_HEADER;
        } else {
            return IS_NORMAL;
        }
    }
    public final static class WeekReportDetailsViewHolder extends RecyclerView.ViewHolder {
        int viewType;
        ImageView subjectIcon;
        TextView subjectNameTv, subjectAllCountTv;
        TextView rightPbTv;
        ColorArcProgressBar leftPb, middlePb, rightPb;

        public WeekReportDetailsViewHolder (View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if(viewType == IS_HEADER) {

            } else {
                subjectIcon = (ImageView) itemView.findViewById(R.id.week_report_detail_iv);
                subjectNameTv = (TextView) itemView.findViewById(R.id.week_report_detail_tv);
                subjectAllCountTv = (TextView) itemView.findViewById(R.id.week_report_detail_number_dec_tv);
                View percentView = itemView.findViewById(R.id.week_homework_report_percent_detail_layout);
                leftPb = (ColorArcProgressBar) percentView.findViewById(R.id.left_class_precision_pb);
                middlePb = (ColorArcProgressBar) percentView.findViewById(R.id.center_class_precision_pb);
                rightPb = (ColorArcProgressBar) percentView.findViewById(R.id.right_precision_pb);
                rightPbTv = (TextView) percentView.findViewById(R.id.right_class_precision_pb_txt);
                rightPbTv.setText(R.string.current_report_homework_corrected_to_last_week_txt);
            }
        }
    }
}
