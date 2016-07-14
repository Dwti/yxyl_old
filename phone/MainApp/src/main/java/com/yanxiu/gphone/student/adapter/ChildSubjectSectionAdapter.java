package com.yanxiu.gphone.student.adapter;

import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.ChapterListDataEntity;
import com.yanxiu.gphone.student.bean.ExamPropertyData;

import java.util.List;


/**
 * Created by Administrator on 2015/7/7.
 */
public class ChildSubjectSectionAdapter extends BaseExpandableListAdapter {

    private List<ChapterListDataEntity.ChildrenEntity> childrenEntityList;
    private Context mContext;

    private ViewHolder holder;
    private ViewChildHolder childHolder;

    private ExpandableListView mExpandableListView;

    //是否是考点进入
    private boolean isTestCenter = false;

    public ChildSubjectSectionAdapter(Context context){
        this.mContext = context;
    }

    public boolean isTestCenter() {
        return isTestCenter;
    }

    public void setIsTestCenter(boolean isTestCenter) {
        this.isTestCenter = isTestCenter;
    }

    public void setList(List<ChapterListDataEntity.ChildrenEntity> childrenEntity) {
        this.childrenEntityList = childrenEntity;
        notifyDataSetChanged();
    }

    public void setmExpandableListView(ExpandableListView mExpandableListView) {
        this.mExpandableListView = mExpandableListView;
    }

    @Override
    public int getGroupCount() {
        return childrenEntityList!=null ? childrenEntityList.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childrenEntityList!=null && childrenEntityList.get(groupPosition).getChildren() != null ? childrenEntityList.get(groupPosition).getChildren().size(): 0;
    }

    @Override
    public ChapterListDataEntity.ChildrenEntity getGroup(int groupPosition) {
        return childrenEntityList!=null ? childrenEntityList.get(groupPosition) : null;
    }


    @Override
    public ChapterListDataEntity.GrandsonEntity getChild(int groupPosition, int childPosition) {
        return childrenEntityList!=null && childrenEntityList.get(groupPosition).getChildren()!=null ? childrenEntityList.get(groupPosition).getChildren().get(childPosition) : null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {

        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.examination_children_item, null);
            holder  = new ViewHolder();
            holder.frameLayout = (FrameLayout) row.findViewById(R.id.fl_middle);
            holder.txtFather = (TextView) row.findViewById(R.id.tv_exa_title);
            holder.txtFather.setTextSize(16);
            holder.imageView = (ImageView) row.findViewById(R.id.iv_right_row);
            holder.rlLeftExamination = (RelativeLayout) row.findViewById(R.id.rl_left_examination);

            holder.dashLine = row.findViewById(R.id.exa_child_dash_line);
            holder.exaFatherTestPercentView = (RelativeLayout) row.findViewById(R.id
                    .exa_child_test_percent_view);
            holder.testCenterDec = (TextView) row.findViewById(R.id.tv_exa_percent_view);
            holder.testCenterPercent = (TextView) row.findViewById(R.id.tv_exa_percent_percent);
            holder.clipView = (ImageView) row.findViewById(R.id.iv_exa_percent_clip_view);
            holder.clipDrawable = (ClipDrawable) holder.clipView.getDrawable();
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        holder.rlLeftExamination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isExpanded) {
                    mExpandableListView.expandGroup(groupPosition, true);
                } else {
                    mExpandableListView.collapseGroup(groupPosition);
                }
            }

        });
        if (isTestCenter()){
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isExpanded) {
                        mExpandableListView.expandGroup(groupPosition, true);
                    } else {
                        mExpandableListView.collapseGroup(groupPosition);
                    }
                }

            });
        }
        int childrenCount = getChildrenCount(groupPosition);
        if(childrenCount == 0){
            if(isTestCenter()){
                holder.imageView.setVisibility(View.INVISIBLE);
            } else {
                holder.frameLayout.setVisibility(View.INVISIBLE);
            }
//            holder.frameLayout.setBackgroundResource(R.drawable.child_examination_no_data);
        }else{
            if (!isTestCenter()){
                holder.frameLayout.setVisibility(View.VISIBLE);
            }
            if(isTestCenter()){
                ChapterListDataEntity.ChildrenEntity dataEntity = getGroup(groupPosition);
                if(dataEntity != null && dataEntity.getData()!=null) {
                    ExamPropertyData mExamPropertyData = dataEntity.getData();
                    holder.dashLine.setVisibility(View.VISIBLE);
                    holder.exaFatherTestPercentView.setVisibility(View.VISIBLE);
                    holder.testCenterPercent.setText(Html.fromHtml("<big><strong>" +
                            mExamPropertyData.getMasterNum() + "</strong></big>"
                            + "/" + mExamPropertyData.getTotalNum()));
//                    Random random = new Random();
//                    holder.clipDrawable.setLevel(random.nextInt(100)*100);
//                    LogInfo.log("haitian", "------------father avg="+mExamPropertyData
//                            .getAvgMasterRate());
                  holder.clipDrawable.setLevel((int)(((float)Integer.valueOf(mExamPropertyData
                          .getMasterNum())/Integer.valueOf(mExamPropertyData
                          .getTotalNum()))*10000));
                }
                holder.imageView.setVisibility(View.INVISIBLE);
            }
            if(isExpanded){
                holder.frameLayout.setBackgroundResource(R.drawable.chapter_center_child_examination_up);
            }else{
                holder.frameLayout.setBackgroundResource(R.drawable.chapter_center_examination_up);
            }
        }
//        LogInfo.log("geny","getGroupView" + getGroup(groupPosition).getName());
        holder.txtFather.setText(getGroup(groupPosition).getName());
        return row;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            childHolder = new ViewChildHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.examination_gradeson_item, null);
            childHolder.txtFather = (TextView) view.findViewById(R.id.tv_exa_title);
            childHolder.txtFather.setTextSize(14);
            childHolder.dashLine = view.findViewById(R.id.exa_grandson_dash_line);
            childHolder.exaFatherTestPercentView = (RelativeLayout) view.findViewById(R.id
                    .exa_grandson_test_percent_view);
            childHolder.testCenterDec = (TextView) view.findViewById(R.id.tv_exa_percent_view);
            childHolder.testCenterPercent = (TextView) view.findViewById(R.id.tv_exa_percent_percent);
            childHolder.clipView = (ImageView) view.findViewById(R.id.iv_exa_percent_clip_view);
            childHolder.clipDrawable = (ClipDrawable) childHolder.clipView.getDrawable();
            view.setTag(childHolder);
        } else {
            childHolder = (ViewChildHolder) view.getTag();
        }

        if(isTestCenter()){
            ChapterListDataEntity.GrandsonEntity dataEntity = getChild(groupPosition, childPosition);
            if(dataEntity != null && dataEntity.getData()!=null) {
                ExamPropertyData mExamPropertyData = dataEntity.getData();
                childHolder.dashLine.setVisibility(View.VISIBLE);
                childHolder.exaFatherTestPercentView.setVisibility(View.VISIBLE);
                String levelSt = mExamPropertyData.getMasterLevel();
                if(!TextUtils.isEmpty(levelSt)){
                    levelSt = levelSt.substring(0, levelSt.lastIndexOf("%"));
                    LogInfo.log("haitian", "levelStr = " + levelSt);
                } else {
                    levelSt = "0";
                }
                childHolder.testCenterPercent.setText(Html.fromHtml("<big><strong>" +
                        levelSt + "</strong></big>"
                        + "%"));
                childHolder.clipDrawable.setLevel(Integer.valueOf(levelSt) *100);
            }
        }
        childHolder.testCenterDec.setText(R.string.exa_total_percent_value);
        childHolder.txtFather.setText(getChild(groupPosition, childPosition).getName());
//        LogInfo.log("geny", "getChildView" + getChild(groupPosition, childPosition).getName());
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class ViewHolder {
        TextView txtFather;
        FrameLayout frameLayout;
        RelativeLayout rlLeftExamination;
        ImageView imageView;

        View dashLine;
        RelativeLayout exaFatherTestPercentView;
        TextView testCenterDec;
        TextView testCenterPercent;
        ImageView clipView;
        ClipDrawable clipDrawable;
    }

    static class ViewChildHolder {
        TextView txtFather;

        View dashLine;
        RelativeLayout exaFatherTestPercentView;
        TextView testCenterDec;
        TextView testCenterPercent;
        ImageView clipView;
        ClipDrawable clipDrawable;
    }

}
