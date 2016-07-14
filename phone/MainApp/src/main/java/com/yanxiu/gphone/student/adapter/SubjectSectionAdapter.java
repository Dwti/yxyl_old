package com.yanxiu.gphone.student.adapter;

import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.ChapterListDataEntity;
import com.yanxiu.gphone.student.bean.ChapterListEntity;
import com.yanxiu.gphone.student.bean.ExamPropertyData;
import com.yanxiu.gphone.student.inter.OnChildTreeViewClickListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2015/7/7.
 */
public class SubjectSectionAdapter extends BaseExpandableListAdapter {

    private ChapterListEntity mChapterListEntity;
    private Context mContext;

    private ViewHolder holder;

    private ExpandableListView mExpandableListView;
    //是否是考点进入
    private boolean isTestCenter = false;

    public SubjectSectionAdapter (Context context) {
        this.mContext = context;
    }

    public void setList (ChapterListEntity chapterListEntity) {
        this.mChapterListEntity = chapterListEntity;
        notifyDataSetChanged();
    }

    public boolean isTestCenter () {
        return isTestCenter;
    }

    public void setIsTestCenter (boolean isTestCenter) {
        this.isTestCenter = isTestCenter;
    }

    public void setmExpandableListView (ExpandableListView mExpandableListView) {
        this.mExpandableListView = mExpandableListView;
    }

    @Override
    public int getGroupCount () {
        return mChapterListEntity != null && mChapterListEntity.getData() != null ? mChapterListEntity.getData().size() : 0;
//        return mChapterListEntity.getData().size();
//        return mExercisesBean.getExaminationlist().size();
    }

    @Override
    public int getChildrenCount (int groupPosition) {
//        if(mChapterListEntity.getData().get(groupPosition) != null){
//            return mChapterListEntity.getData().get(groupPosition).getChildren().size();
//        }else{
//            return 0;
//        }
        return mChapterListEntity != null && mChapterListEntity.getData() != null && mChapterListEntity.getData().get(groupPosition) != null ? mChapterListEntity.getData().get(groupPosition).getChildren().size() : 0;
    }

    @Override
    public ChapterListDataEntity getGroup (int groupPosition) {
//        return mExercisesBean.getExaminationlist().get(groupPosition);
        return mChapterListEntity != null ? mChapterListEntity.getData().get(groupPosition) : null;
    }


    @Override
    public List<ChapterListDataEntity.ChildrenEntity> getChild (int groupPosition, int childPosition) {
        List<ChapterListDataEntity.ChildrenEntity> childrenEntityList = new ArrayList<ChapterListDataEntity.ChildrenEntity>();
        childrenEntityList.add(mChapterListEntity.getData().get(groupPosition).getChildren().get(childPosition));
        return childrenEntityList;
    }

    @Override
    public long getGroupId (int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId (int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds () {
        return false;
    }

    @Override
    public View getGroupView (final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {

        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.examination_father_item, null);
            holder = new ViewHolder();
            holder.frameLayout = (FrameLayout) row.findViewById(R.id.fl_middle);
            holder.txtFather = (TextView) row.findViewById(R.id.tv_exa_title);
            holder.rlLeftExamination = (RelativeLayout) row.findViewById(R.id.rl_left_examination);
            holder.imageView = (ImageView) row.findViewById(R.id.iv_right_row);
            holder.dashLine = row.findViewById(R.id.exa_father_dash_line);
            holder.exaFatherTestPercentView = (RelativeLayout) row.findViewById(R.id.exa_father_test_percent_view);
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
            public void onClick (View v) {
                if (!isExpanded) {
                    mExpandableListView.expandGroup(groupPosition, true);
                } else {
                    mExpandableListView.collapseGroup(groupPosition);
                    ChapterListDataEntity dataEntity = getGroup(groupPosition);
                    List<ChapterListDataEntity.ChildrenEntity> childrenList = dataEntity.getChildren();
                    if (childrenList != null) {
                        for (ChapterListDataEntity.ChildrenEntity childrenEntity : childrenList) {
                            childrenEntity.setIsExpanded(false);
                        }
                    }
                }
            }
        });
        int childrenCount = getChildrenCount(groupPosition);
        if (childrenCount == 0) {
            if (isTestCenter()) {
                holder.imageView.setVisibility(View.INVISIBLE);
            } else {
                holder.frameLayout.setVisibility(View.GONE);
            }
//            holder.frameLayout.setBackgroundResource(R.drawable.examination_no_data);
        } else {
            if (!isTestCenter()){
                holder.frameLayout.setVisibility(View.VISIBLE);
            }
            if (isTestCenter()) {
                ChapterListDataEntity dataEntity = getGroup(groupPosition);
                if (dataEntity != null && dataEntity.getData() != null) {
                    ExamPropertyData mExamPropertyData = dataEntity.getData();
                    holder.dashLine.setVisibility(View.VISIBLE);
                    holder.exaFatherTestPercentView.setVisibility(View.VISIBLE);

                    holder.testCenterPercent.setText(Html.fromHtml("<big><strong>" +
                            mExamPropertyData
                            .getMasterNum() + "</strong></big>"
                            + "/" + mExamPropertyData.getTotalNum()));
//                    Random random = new Random();
//                    holder.clipDrawable.setLevel(random.nextInt(100) * 100);
                    LogInfo.log("haitian", "------------father avg=" + mExamPropertyData
                            .getAvgMasterRate());
                    holder.clipDrawable.setLevel((int)(((float)Integer.valueOf(mExamPropertyData
                            .getMasterNum())/Integer.valueOf(mExamPropertyData
                            .getTotalNum()))*10000));

                }
                holder.imageView.setVisibility(View.INVISIBLE);
            }
            if (isExpanded) {
//                holder.imageView.setImageResource(R.drawable.icon_expanded);
                holder.frameLayout.setBackgroundResource(R.drawable.chapter_center_examination_down);
            } else {
//                holder.imageView.setImageResource(R.drawable.icon_unexpanded);
                holder.frameLayout.setBackgroundResource(R.drawable.chapter_center_examination_up);
            }
        }
        if (isTestCenter()){
            holder.txtFather.setText(getGroup(groupPosition).getName());
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    if (!isExpanded) {
                        mExpandableListView.expandGroup(groupPosition, true);
                    } else {
                        mExpandableListView.collapseGroup(groupPosition);
                        ChapterListDataEntity dataEntity = getGroup(groupPosition);
                        List<ChapterListDataEntity.ChildrenEntity> childrenList = dataEntity.getChildren();
                        if (childrenList != null) {
                            for (ChapterListDataEntity.ChildrenEntity childrenEntity : childrenList) {
                                childrenEntity.setIsExpanded(false);
                            }
                        }
                    }
                }
            });
        } else {
            holder.txtFather.setText(Html.fromHtml("<strong>" + getGroup(groupPosition).getName() + "</strong>"));
        }
        return row;
    }

    public ExpandableListView getExpandableListView () {
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ExpandableListView superTreeView = new ExpandableListView(mContext);
        superTreeView.setSelector(R.color.color_transparent);
        superTreeView.setDivider(null);
        superTreeView.setDividerHeight(CommonCoreUtil.dipToPx(5));
        superTreeView.setGroupIndicator(null);
        superTreeView.setChildDivider(null);
        superTreeView.setLayoutParams(lp);
        return superTreeView;
    }

    @Override
    public View getChildView (final int parentPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ExpandableListView treeView = getExpandableListView();
        final ChildSubjectSectionAdapter treeViewAdapter = new ChildSubjectSectionAdapter(mContext);
        treeViewAdapter.setIsTestCenter(isTestCenter);
        treeViewAdapter.setList(getChild(parentPosition, childPosition));

        treeView.setAdapter(treeViewAdapter);
        treeViewAdapter.setmExpandableListView(treeView);

        treeView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick (ExpandableListView arg0, View arg1, int groupIndex, int childIndex, long arg4) {
                if (mTreeViewClickListener != null) {
                    mTreeViewClickListener.onGradesonClickPosition(parentPosition, childPosition, childIndex);
                }
                return true;
            }
        });


        treeView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick (ExpandableListView parent, View v, int childGroupPosition, long id) {
                if (mTreeViewClickListener != null) {
                    mTreeViewClickListener.onChildClickPosition(parentPosition, childPosition, -1);
                }
                return true;   //默认为false，设为true时，点击事件不会展开Group
            }
        });
        if (isExpeand(parentPosition, childPosition)) {
            treeView.expandGroup(0);
            LayoutParams lp = null;
            if (isTestCenter()) {
                lp = new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, (treeViewAdapter.getChildrenCount(0) + 1)
                        * (int) mContext.getResources().getDimension(R.dimen.dimen_94));
            } else {
                lp = new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, (treeViewAdapter.getChildrenCount(0) + 1)
                        * (int) mContext.getResources().getDimension(R.dimen.dimen_70));
            }
            treeView.setLayoutParams(lp);
        }
        treeView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand (int groupPosition) {
                setIsExpeand(parentPosition, childPosition, true);
                LayoutParams lp = null;
                if (isTestCenter()) {
                    lp = new LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, (treeViewAdapter.getChildrenCount(groupPosition) + 1)
                            * (int) mContext.getResources().getDimension(R.dimen.dimen_94));
                } else {
                    lp = new LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, (treeViewAdapter.getChildrenCount(groupPosition) + 1)
                            * (int) mContext.getResources().getDimension(R.dimen.dimen_70));
                }
                treeView.setLayoutParams(lp);
            }
        });
        treeView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse (int groupPosition) {
                setIsExpeand(parentPosition, childPosition, false);
                LayoutParams lp = null;
                if (isTestCenter()) {
                    LogInfo.log("haitian", "-------onGroupCollapse--------");
                    lp = new LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, (int) mContext
                            .getResources().getDimension(R.dimen.dimen_94));
                } else {
                    lp = new LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, (int) mContext
                            .getResources().getDimension(R.dimen.dimen_70));
                }
                treeView.setLayoutParams(lp);
            }
        });

        return treeView;
    }


    /**
     * 设置gradeson展开的
     */
    private void setIsExpeand (int groupPosition, int childPosition, boolean isExpeand) {
        List<ChapterListDataEntity.ChildrenEntity> entityList = getChild(groupPosition, childPosition);
        if (entityList != null && entityList.get(0) != null) {
            entityList.get(0).setIsExpanded(isExpeand);
        }
    }


    /**
     * 判断 是否展开gradeson  mChapterListEntity的位置
     *
     * @return
     */
    private boolean isExpeand (int groupPosition, int childPosition) {
        boolean isExpend = false;
        List<ChapterListDataEntity.ChildrenEntity> entityList = getChild(groupPosition, childPosition);
        if (entityList != null && entityList.get(0) != null) {
            isExpend = entityList.get(0).isExpanded();
        }
        return isExpend;
    }

    @Override
    public boolean isChildSelectable (int groupPosition, int childPosition) {
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

    private OnChildTreeViewClickListener mTreeViewClickListener;

    public void setOnChildTreeViewClickListener (OnChildTreeViewClickListener treeViewClickListener) {
        this.mTreeViewClickListener = treeViewClickListener;
    }

}
