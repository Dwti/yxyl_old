package com.yanxiu.gphone.hd.student.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.bean.DataTeacherEntity;

import java.util.ArrayList;


/**
 * Created by Administrator on 2015/7/7.
 */
public class FavouriteSectionNewAdapter extends BaseExpandableListAdapter {
    private ArrayList<DataTeacherEntity> mSectionFavouriteList;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ViewHolder holder;

    private ExpandableListView mExpandableListView;
    //是否是考点进入
    private boolean isTestCenter = false;
    public FavouriteSectionNewAdapter(Context context) {
        this.mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }
    public boolean isTestCenter () {
        return isTestCenter;
    }

    public void setIsTestCenter (boolean isTestCenter) {
        this.isTestCenter = isTestCenter;
    }
    public void setList(ArrayList<DataTeacherEntity> mSectionFavouriteList) {
        this.mSectionFavouriteList = mSectionFavouriteList;
        notifyDataSetChanged();
    }

    public void setmExpandableListView(ExpandableListView mExpandableListView) {
        this.mExpandableListView = mExpandableListView;
    }

    @Override
    public int getGroupCount() {
        if (mSectionFavouriteList != null) {
            return mSectionFavouriteList.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mSectionFavouriteList != null &&
                mSectionFavouriteList.get(groupPosition) != null) {
            ArrayList<DataTeacherEntity> children = mSectionFavouriteList.get(groupPosition).getChildren();
            return children != null ? children.size() : 0;
        }
        return 0;
    }

    @Override
    public DataTeacherEntity getGroup(int groupPosition) {
        if (mSectionFavouriteList != null &&
                mSectionFavouriteList.get(groupPosition) != null) {
            return mSectionFavouriteList.get(groupPosition);
        }
        return null;
    }

    @Override
    public DataTeacherEntity getChild(int groupPosition, int childPosition) {
        if (mSectionFavouriteList != null &&
                mSectionFavouriteList.get(groupPosition) != null) {
            ArrayList<DataTeacherEntity> children = mSectionFavouriteList.get(groupPosition).getChildren();
            return children.get(childPosition);
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
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        LogInfo.log("haitian", " groupPosition = " + groupPosition);
        View row = convertView;
        if (row == null) {
            row = layoutInflater.inflate(R.layout.examination_children_item, null);
            holder = new ViewHolder();
            holder.frameLayout = (FrameLayout) row.findViewById(R.id.fl_middle);
            holder.txtFather = (TextView) row.findViewById(R.id.tv_exa_title);
            holder.txtFather.setTextSize(16);
            holder.txtError = (TextView) row.findViewById(R.id.tv_exa_error);
            holder.rightArrow = (ImageView) row.findViewById(R.id.iv_right_row);
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
                }
            }

        });
        int childrenCount = getChildrenCount(groupPosition);
        if (childrenCount == 0) {
//            if (!isTestCenter()){
                holder.frameLayout.setVisibility(View.INVISIBLE);
//            }
//            holder.frameLayout.setBackgroundResource(R.drawable.child_examination_no_data);
        } else {
//            if (!isTestCenter()){
                holder.frameLayout.setVisibility(View.VISIBLE);
//            }
            if (isExpanded) {
                holder.frameLayout.setBackgroundResource(R.drawable.chapter_center_child_examination_up);
            } else {
                holder.frameLayout.setBackgroundResource(R.drawable.chapter_center_examination_up);
            }
        }
        DataTeacherEntity groupEntity = getGroup(groupPosition);
        if (groupEntity != null) {
            if (groupEntity.getData() != null) {
                String favouriteNum = groupEntity.getData().getFavoriteNum();
                String favouriteNumStr = null;
                if (favouriteNum != null && !"0".equals(favouriteNum)) {
                    if (favouriteNum.length() == 1) {
                        favouriteNumStr = "&#160;&#160;&#160;&#160;" + favouriteNum;
                    } else if (favouriteNum.length() == 2) {
                        favouriteNumStr = "&#160;&#160;" + favouriteNum;
                    } else {
                        favouriteNumStr = favouriteNum;
                    }
                    holder.rightArrow.setVisibility(View.VISIBLE);
                    holder.txtError.setVisibility(View.VISIBLE);
//                    holder.txtError.setText(Html.fromHtml("<font color='#52c6fd'>" + favouriteNumStr + "</font>"));
                    holder.txtError.setText(Html.fromHtml(favouriteNumStr));
                } else {
                    holder.txtError.setVisibility(View.INVISIBLE);
                    holder.rightArrow.setVisibility(View.INVISIBLE);
                }
            } else {
                holder.rightArrow.setVisibility(View.INVISIBLE);
                holder.txtError.setVisibility(View.INVISIBLE);
            }
            holder.txtFather.setText(getGroup(groupPosition).getName());
            if(holder.rightArrow.getVisibility() != View.VISIBLE ){
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
        }
        return row;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderChild mViewHolderChild = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.examination_gradeson_item, null);
            mViewHolderChild = new ViewHolderChild();
            mViewHolderChild.txtFather = (TextView) convertView.findViewById(R.id.tv_exa_title);
            mViewHolderChild.txtFather.setTextSize(14);
            mViewHolderChild.txtError = (TextView) convertView.findViewById(R.id.tv_exa_error);
            mViewHolderChild.txtError.setVisibility(View.VISIBLE);
            convertView.setTag(mViewHolderChild);
        } else {
            mViewHolderChild = (ViewHolderChild) convertView.getTag();
        }
        DataTeacherEntity childEntity = getChild(groupPosition, childPosition);
        if (childEntity != null) {
            if (childEntity.getData() != null) {
                mViewHolderChild.txtFather.setText(childEntity.getName());
                String favouriteNum = childEntity.getData().getFavoriteNum();
                String favouriteNumStr = null;
                if (favouriteNum != null && !"0".equals(favouriteNum)) {
                    if (favouriteNum.length() == 1) {
                        favouriteNumStr = "&#160;&#160;&#160;&#160;" + favouriteNum;
                    } else if (favouriteNum.length() == 2) {
                        favouriteNumStr = "&#160;&#160;" + favouriteNum;
                    } else {
                        favouriteNumStr = favouriteNum;
                    }
//                    mViewHolderChild.txtError.setText(Html.fromHtml("<font color='#52c6fd'>" + favouriteNumStr + "</font>"));
                    mViewHolderChild.txtError.setText(Html.fromHtml(favouriteNumStr));
//                    mViewHolderChild.txtError.setText(Html.fromHtml(mContext.getResources().getString(R.string.favourite_edition_num_txt) +
//                            "<font color='#ff0000'>" + favouriteNumStr + "</font>"));
                } else {
                    mViewHolderChild.txtError.setVisibility(View.INVISIBLE);
                }
            } else {
                mViewHolderChild.txtError.setVisibility(View.INVISIBLE);
            }

        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public final class ViewHolder {
        TextView txtFather;
        TextView txtError;
        FrameLayout frameLayout;
        RelativeLayout rlLeftExamination;

        ImageView rightArrow;
    }

    public final class ViewHolderChild {
        TextView txtFather;
        TextView txtError;
    }
}
