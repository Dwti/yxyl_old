package com.yanxiu.gphone.parent.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.activity.ParentWeekReportDetailsActivity;
import com.yanxiu.gphone.parent.bean.ParentPublicPropertyBean;
import com.yanxiu.gphone.parent.bean.ParentWeekReportDataBean;
import com.yanxiu.gphone.parent.view.ColorArcProgressBar;

import java.util.ArrayList;

/**
 * Created by hai8108 on 16/3/23.
 */
public class WeekReportAdapter extends RecyclerView.Adapter<WeekReportAdapter.WeekReportViewHolder> {
    private static final int IS_HEAD = 0;
    private static final int IS_EXERCISE = 1;
    private static final int IS_HOMEWORK = 2;
    private ArrayList<ParentWeekReportDataBean> modelDatas;
    private ParentPublicPropertyBean timeBean;
    private Context mContext;
    private int increaseColor, descreaseColor, normalColor;
    public WeekReportAdapter (Context mContext, ArrayList<ParentWeekReportDataBean> modelDatas, ParentPublicPropertyBean timeBean){
//        if(modelDatas == null){
//            throw new IllegalArgumentException(
//                    "modelData must not be null");
//        }
        this.mContext = mContext;
        this.modelDatas = modelDatas;
        this.timeBean = timeBean;
        increaseColor = mContext.getResources().getColor(R.color.color_cc0000_p);
        descreaseColor = mContext.getResources().getColor(R.color.color_00cc66_p);
        normalColor = mContext.getResources().getColor(R.color.color_333333_p);
    }
    public void setList(ArrayList<ParentWeekReportDataBean> modelDatas, ParentPublicPropertyBean timeBean){
        this.modelDatas = modelDatas;
        this.timeBean = timeBean;
        notifyDataSetChanged();
    }
    @Override
    public WeekReportViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View itemView = null;
        if(viewType == IS_HEAD) {
            itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.parent_public_time_date_layout, parent, false);
        }else if(viewType == IS_EXERCISE){
            itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.fragment_weekreport_exercise_item_layout, parent, false);
        } else {
            itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.fragment_weekreport_hw_item_layout, parent, false);
        }
        return new WeekReportViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder (WeekReportViewHolder holder, int position) {
        if(holder.viewType == IS_HEAD) {
            holder.timeTv.setText(timeBean.getMondayDate());
        }else {
            final ParentWeekReportDataBean modelBean = modelDatas.get(position-1);
            if (holder.viewType == IS_EXERCISE) {
                holder.currentExerciseFinishedTv.setText(Html.fromHtml(mContext.getString(R.string
                        .yanxiu_parent_current_exercise_finished_txt) +
                        "<big><strong>" + "&#160;" + modelBean.getAnswerIntelQuesNum() + "&#160;" +
                        "</strong></big>" + mContext.getString(R.string.yanxiu_parent_exercise)));
                holder.currentExerciseRightTv.setText(Html.fromHtml(mContext.getString(R.string.yanxiu_parent_current_exercise_right_txt) +
                        "<big><strong>" + "&#160;" + modelBean.getIntelQuesCorrectNum() + "&#160;" +
                        "</strong></big>" + mContext.getString(R.string.yanxiu_parent_exercise)));

                holder.currentExerciseOrdersTv.setText(modelBean.getIntelWeekRank() + "");
            } else {
                holder.currentReportHomeworkFinishedTv.setText(Html.fromHtml(mContext.getString(R.string
                        .yanxiu_parent_current_homework_finished_txt_start) +
                        "<big><strong>" + "&#160;" + modelBean.getAnswerHwkQuesNum() + "&#160;" +
                        "</strong></big>" + mContext.getString(R.string.yanxiu_parent_exercise)));
                if (modelBean.getIncreaseFlag() == 1) {
                    holder.currentReportHomeworkFinishedPercentTv.setTextColor(increaseColor);
                    holder.percentTv.setTextColor(increaseColor);
                    holder.currentReportHomeworkFinishedPercentTv.setText("+" + (int)(modelBean
                            .getIncreaseRate() * 100));
                } else if(modelBean.getIncreaseFlag() == 2){
                    holder.currentReportHomeworkFinishedPercentTv.setTextColor(descreaseColor);
                    holder.percentTv.setTextColor(descreaseColor);
                    holder.currentReportHomeworkFinishedPercentTv.setText("-" + (int)(modelBean
                            .getIncreaseRate() * 100));
                } else {
                    holder.currentReportHomeworkFinishedPercentTv.setTextColor(normalColor);
                    holder.percentTv.setTextColor(normalColor);
                    holder.currentReportHomeworkFinishedPercentTv.setText(""+(int)(modelBean
                            .getIncreaseRate() * 100));
                }
                holder.leftPb.setCurrentValues((float) modelBean.getClassAvgCorrectRate() * 100);
                holder.middlePb.setCurrentValues((float) modelBean.getAvgCorrectRate() * 100);
                holder.rightPb.setCurrentValues((float) modelBean.getClassBestRate() * 100);
                holder.detailsForHomeWorkTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v) {
                        ParentWeekReportDetailsActivity.launchActivity((Activity) mContext,
                                modelBean.getClassId(), timeBean.getWeek(), timeBean.getYear());
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount () {
        if(modelDatas != null && modelDatas.size() > 0) {
            return modelDatas.size()+1;
        } else if(timeBean != null){
            return 1;
        } else {
            return 0;
        }
    }
    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return IS_HEAD;
        } else {
            ParentWeekReportDataBean modelBean = modelDatas.get(position-1);
            if (modelBean.getType() == 0) {
                return IS_EXERCISE;
            } else {
                return IS_HOMEWORK;
            }
        }
    }
    public final static class WeekReportViewHolder extends RecyclerView.ViewHolder {
        int viewType;
        TextView timeTv;
        //练习专用
        TextView currentExerciseFinishedTv;
        TextView currentExerciseRightTv;
        TextView currentExerciseOrdersTv;

        //作业专用
        TextView currentReportHomeworkFinishedTv;
        TextView currentReportHomeworkFinishedPercentTv;
        TextView detailsForHomeWorkTv;
        TextView percentTv;
        View currentReportPercentLayout;
        ColorArcProgressBar leftPb, middlePb, rightPb;

        public WeekReportViewHolder (View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if(viewType == IS_HEAD) {
                timeTv = (TextView) itemView.findViewById(R.id.week_date_tv);
            }else if(viewType == IS_EXERCISE) {
                currentExerciseFinishedTv = (TextView) itemView.findViewById(R.id.current_exercise_finished_tv);
                currentExerciseRightTv = (TextView) itemView.findViewById(R.id.current_exercise_right_tv);
                currentExerciseOrdersTv = (TextView) itemView.findViewById(R.id.current_exercise_orders_really_tv);
            } else {
                detailsForHomeWorkTv = (TextView) itemView.findViewById(R.id.current_week_homework_average_detail_tv);
                percentTv = (TextView) itemView.findViewById(R.id.week_report_homework_percent_iv);
                currentReportHomeworkFinishedTv = (TextView) itemView.findViewById(R.id.current_report_homework_finished_tv);
                currentReportHomeworkFinishedPercentTv = (TextView) itemView.findViewById(R.id
                        .current_report_homework_finished_percent_tv);
                currentReportPercentLayout = itemView.findViewById(R.id.current_report_percent_layout);
                leftPb = (ColorArcProgressBar) currentReportPercentLayout.findViewById(R.id.left_class_precision_pb);
                middlePb = (ColorArcProgressBar) currentReportPercentLayout.findViewById(R.id
                        .center_class_precision_pb);
                rightPb = (ColorArcProgressBar) currentReportPercentLayout.findViewById(R.id
                        .right_precision_pb);
            }
        }
    }
}
