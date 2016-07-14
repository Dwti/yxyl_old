package com.yanxiu.gphone.student.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.*;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.DataTeacherEntity;
import com.yanxiu.gphone.student.inter.OnChildTreeViewClickListener;
import com.common.core.utils.CommonCoreUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2015/7/7.
 */
public class FavouriteAndMistakeChapterAdapter extends BaseExpandableListAdapter {
    public final static int CONSTANT_FAVOURITECHAPTERADAPTER = 0;//0:收藏
    public final static int CONSTANT_MISTAKECHAPTERADAPTER = 1;//1：错题
    private ArrayList<DataTeacherEntity> mChapterFavouriteList;
    private Context mContext;
    private int adapterType = CONSTANT_FAVOURITECHAPTERADAPTER;
    private ViewHolder holder;
    private OnRowClickListener mOnClickListener;
    private ExpandableListView mExpandableListView;
    //是否是考点进入
    private boolean isTestCenter = false;
    public FavouriteAndMistakeChapterAdapter(Context context, int adapterType){
        this.mContext = context;
        this.adapterType = adapterType;
    }

    public void setList(ArrayList<DataTeacherEntity> mChapterFavouriteList) {
        this.mChapterFavouriteList = mChapterFavouriteList;
        notifyDataSetChanged();
    }

    public void setmExpandableListView(ExpandableListView mExpandableListView) {
        this.mExpandableListView = mExpandableListView;
    }

    public OnRowClickListener getmOnClickListener () {
        return mOnClickListener;
    }

    public void setmOnClickListener (OnRowClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    public boolean isTestCenter () {
        return isTestCenter;
    }

    public void setIsTestCenter (boolean isTestCenter) {
        this.isTestCenter = isTestCenter;
    }
    @Override
    public int getGroupCount() {
        return mChapterFavouriteList != null ? mChapterFavouriteList.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (mChapterFavouriteList != null && mChapterFavouriteList.get(groupPosition) != null && mChapterFavouriteList.get(groupPosition)
                .getChildren() != null) ?
                mChapterFavouriteList.get(groupPosition).getChildren().size(): 0;
    }

    @Override
    public DataTeacherEntity getGroup(int groupPosition) {
        return (mChapterFavouriteList != null && mChapterFavouriteList.get(groupPosition) !=null ) ?
        mChapterFavouriteList.get(groupPosition) : null;
    }


    @Override
    public ArrayList<DataTeacherEntity> getChild(int groupPosition, int childPosition) {
        if(mChapterFavouriteList != null && mChapterFavouriteList.get(groupPosition) != null &&
                mChapterFavouriteList.get(groupPosition).getChildren() != null ){
            ArrayList<DataTeacherEntity> childrenEntityList = new ArrayList<DataTeacherEntity>();
            childrenEntityList.add(mChapterFavouriteList.get(groupPosition).getChildren().get(childPosition));
            return childrenEntityList;
        }
        return null;
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
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView,
                             ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.examination_father_item, null);
            holder  = new ViewHolder();
            holder.frameLayout = (FrameLayout) row.findViewById(R.id.fl_middle);
            holder.txtFather = (TextView) row.findViewById(R.id.tv_exa_title);
            holder.rightArrow = (ImageView) row.findViewById(R.id.iv_right_row);
            holder.txtError = (TextView) row.findViewById(R.id.tv_exa_error);
            holder.txtError.setVisibility(View.VISIBLE);
            holder.rlLeftExamination = (RelativeLayout) row.findViewById(R.id.rl_left_examination);
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
                    DataTeacherEntity dataEntity = getGroup(groupPosition);
                    List<DataTeacherEntity> childrenList = dataEntity.getChildren();
                    if(childrenList != null ){
                        for(DataTeacherEntity childrenEntity : childrenList){
                            childrenEntity.setIsExpanded(false);
                        }
                    }
                }
            }
        });
        int childrenCount = getChildrenCount(groupPosition);
        if(childrenCount == 0){
//            if (!isTestCenter()){
                holder.frameLayout.setVisibility(View.GONE);
//            }
//            holder.frameLayout.setBackgroundResource(R.drawable.examination_no_data);
        }else{
//            if (!isTestCenter()){
                holder.frameLayout.setVisibility(View.VISIBLE);
//            }
            if(isExpanded){
                holder.frameLayout.setBackgroundResource(R.drawable.chapter_center_examination_down);
            }else{
                holder.frameLayout.setBackgroundResource(R.drawable.chapter_center_examination_up);
            }
        }
        String numStr = null;
        if (getGroup(groupPosition) != null) {
            if (getGroup(groupPosition).getData() != null) {
                String num = null;
                if(adapterType == CONSTANT_FAVOURITECHAPTERADAPTER) {
                    num = getGroup(groupPosition).getData().getFavoriteNum();
                    if (num != null && !"0".equals(num)) {
                        if (num.length() == 1) {
                            numStr = "&#160;&#160;&#160;&#160;" + num;
                        } else if (num.length() == 2) {
                            numStr = "&#160;&#160;" + num;
                        } else {
                            numStr = num;
                        }
                        holder.rightArrow.setVisibility(View.VISIBLE);
                        holder.txtError.setVisibility(View.VISIBLE);
//                        holder.txtError.setText(Html.fromHtml("<font color='#52c6fd'>" + numStr + "</font>"));
                        holder.txtError.setText(Html.fromHtml(numStr));
                    } else {
                        holder.txtError.setVisibility(View.INVISIBLE);
                        holder.rightArrow.setVisibility(View.INVISIBLE);
                    }
                } else if(adapterType == CONSTANT_MISTAKECHAPTERADAPTER){
                    num = getGroup(groupPosition).getData().getWrongNum();
                    if (num != null && !"0".equals(num)) {
                        if (num.length() == 1) {
                            numStr = "&#160;&#160;&#160;&#160;" + num;
                        } else if (num.length() == 2) {
                            numStr = "&#160;&#160;" + num;
                        } else {
                            numStr = num;
                        }
                        holder.rightArrow.setVisibility(View.VISIBLE);
                        holder.txtError.setVisibility(View.VISIBLE);
//                        holder.txtError.setText(Html.fromHtml(mContext.getResources().getString(R.string.mistake_edition_num_txt) +
//                                "<font color='#ff0000'>" + numStr + "</font>"));
                        holder.txtError.setText(Html.fromHtml("<font color='#b3476b'>" + numStr +
                                "</font>"));
                    } else {
                        holder.txtError.setVisibility(View.INVISIBLE);
                        holder.rightArrow.setVisibility(View.INVISIBLE);
                    }
                }
            } else {
                holder.rightArrow.setVisibility(View.INVISIBLE);
                holder.txtError.setVisibility(View.INVISIBLE);
            }
            if (isTestCenter()){
                holder.txtFather.setText(getGroup(groupPosition).getName());
            } else {
                holder.txtFather.setText(Html.fromHtml("<strong>" + getGroup(groupPosition).getName() + "</strong>"));
            }
        }
        if(childrenCount > 0 && TextUtils.isEmpty(numStr)) {
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    if (!isExpanded) {
                        mExpandableListView.expandGroup(groupPosition, true);
                    } else {
                        mExpandableListView.collapseGroup(groupPosition);
                        DataTeacherEntity dataEntity = getGroup(groupPosition);
                        List<DataTeacherEntity> childrenList = dataEntity.getChildren();
                        if (childrenList != null) {
                            for (DataTeacherEntity childrenEntity : childrenList) {
                                childrenEntity.setIsExpanded(false);
                            }
                        }
                    }
                }
            });
        }else {
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View view) {
                    if(mOnClickListener != null){
                        mOnClickListener.onClick(groupPosition);
                    }
                }
            });
        }
        return row;
    }
    public interface OnRowClickListener{
        public void onClick(int position);
    }
    public ExpandableListView getExpandableListView(){
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        ExpandableListView superTreeView = new ExpandableListView(mContext);
        superTreeView.setSelector(R.color.color_transparent);
        superTreeView.setDivider(null);
        superTreeView.setDividerHeight(CommonCoreUtil.dipToPx(5));
        superTreeView.setGroupIndicator(null);
        superTreeView.setChildDivider(null);
        superTreeView.setLayoutParams(lp);
        superTreeView.setVerticalScrollBarEnabled(false);
        return superTreeView;
    }
//    public ArrayList<DataTeacherEntity> getNextLevelChild(int groupPosition, int childPosition) {
//        if(mChapterFavouriteList != null && mChapterFavouriteList.get(groupPosition) != null &&
//                mChapterFavouriteList.get(groupPosition).getChildren() != null ){
//            ArrayList<DataTeacherEntity> childrenEntityList = new ArrayList<DataTeacherEntity>();
//            childrenEntityList.add(mChapterFavouriteList.get(groupPosition).getChildren().get(childPosition));
//            return childrenEntityList;
//        }
//        return null;
//    }
//    private BaseExpandableListAdapter treeViewAdapter = null;
    @Override
    public View getChildView(final int parentPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ExpandableListView treeView = getExpandableListView();
        final FavouriteSectionNewAdapter mFavouriteSectionNewAdapter = new FavouriteSectionNewAdapter(mContext);
        final MistakeSectionNewAdapter mMistakeSectionNewAdapter = new MistakeSectionNewAdapter(mContext);
        if(adapterType == CONSTANT_FAVOURITECHAPTERADAPTER) {
            mFavouriteSectionNewAdapter.setList(getChild(parentPosition, childPosition));
            mFavouriteSectionNewAdapter.setIsTestCenter(isTestCenter);
            treeView.setAdapter(mFavouriteSectionNewAdapter);
            mFavouriteSectionNewAdapter.setmExpandableListView(treeView);
        } else if(adapterType == CONSTANT_MISTAKECHAPTERADAPTER){
            mMistakeSectionNewAdapter.setList(getChild(parentPosition, childPosition));
            mMistakeSectionNewAdapter.setIsTestCenter(isTestCenter);
            treeView.setAdapter(mMistakeSectionNewAdapter);
            mMistakeSectionNewAdapter.setmExpandableListView(treeView);
        }
        treeView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView arg0, View arg1, int groupIndex, int childIndex, long arg4) {
                if(CommonCoreUtil.checkClickEvent()) {
                    if (mTreeViewClickListener != null) {
                        mTreeViewClickListener.onGradesonClickPosition(parentPosition, childPosition, childIndex);
                    }
                }
                return true;
            }
        });
        treeView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if(CommonCoreUtil.checkClickEvent()) {
                    if (mTreeViewClickListener != null) {
                        mTreeViewClickListener.onChildClickPosition(parentPosition, childPosition, -1);
                    }
                }
                return true;   //默认为false，设为true时，点击事件不会展开Group
            }
        });

        if (isExpeand(parentPosition, childPosition)) {
            int childItem = 0;
            if(adapterType == CONSTANT_FAVOURITECHAPTERADAPTER) {
                childItem = mFavouriteSectionNewAdapter.getChildrenCount(0);
                if(childItem != 0){
                    treeView.expandGroup(0);
                }
                childItem ++;
            } else if(adapterType == CONSTANT_MISTAKECHAPTERADAPTER){
                childItem = mMistakeSectionNewAdapter.getChildrenCount(0);
                if(mMistakeSectionNewAdapter.getChildrenCount(0) != 0){
                    treeView.expandGroup(0);
                }
                childItem ++;
            }
            LayoutParams lp = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, childItem * (int) mContext.getResources
                    ().getDimension(R.dimen.dimen_70));
            treeView.setLayoutParams(lp);
        }
        treeView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                setIsExpeand(parentPosition, childPosition, true);
                int childItem = 0;
                if(adapterType == CONSTANT_FAVOURITECHAPTERADAPTER) {
                    childItem = mFavouriteSectionNewAdapter.getChildrenCount(groupPosition);
                    childItem ++;
                } else if(adapterType == CONSTANT_MISTAKECHAPTERADAPTER){
                    childItem = mMistakeSectionNewAdapter.getChildrenCount(groupPosition);
                    childItem ++;
                }
                LayoutParams lp = new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, childItem * (int) mContext
                        .getResources().getDimension(R.dimen.dimen_70));
                treeView.setLayoutParams(lp);
            }
        });

        treeView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                setIsExpeand(parentPosition, childPosition, false);
                LayoutParams lp = new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, (int) mContext
                        .getResources().getDimension(R.dimen.dimen_70_5));
                treeView.setLayoutParams(lp);
            }
        });
        return treeView;
    }
    /**
     * 设置gradeson展开的
     */
    private void setIsExpeand(int groupPosition, int childPosition, boolean isExpeand){
        ArrayList<DataTeacherEntity> entityList = getChild(groupPosition, childPosition);
        if(entityList != null && entityList.get(0) != null){
            entityList.get(0).setIsExpanded(isExpeand);
        }
    }
    /**
     * 判断 是否展开gradeson  mChapterListEntity的位置
     * @return
     */
    private boolean isExpeand(int groupPosition, int childPosition){
        boolean isExpend = false;
        ArrayList<DataTeacherEntity> entityList = getChild(groupPosition, childPosition);
        if(entityList != null && entityList.get(0) != null){
            isExpend = entityList.get(0).isExpanded();
        }
        return isExpend;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class ViewHolder {
        TextView txtFather;
        TextView txtError;
        FrameLayout frameLayout;
        RelativeLayout rlLeftExamination;
        ImageView rightArrow;
    }

    private OnChildTreeViewClickListener mTreeViewClickListener;

    public void setOnChildTreeViewClickListener(OnChildTreeViewClickListener treeViewClickListener) {
        this.mTreeViewClickListener = treeViewClickListener;
    }
}
