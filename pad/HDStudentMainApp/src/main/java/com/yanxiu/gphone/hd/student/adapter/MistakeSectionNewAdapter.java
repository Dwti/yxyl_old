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
public class MistakeSectionNewAdapter extends BaseExpandableListAdapter {
    private ArrayList<DataTeacherEntity> mSectionMistakeList;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ViewHolder holder;

    private ExpandableListView mExpandableListView;
    //是否是考点进入
    private boolean isTestCenter = false;
    public MistakeSectionNewAdapter(Context context) {
        this.mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }
    public boolean isTestCenter () {
        return isTestCenter;
    }

    public void setIsTestCenter (boolean isTestCenter) {
        this.isTestCenter = isTestCenter;
    }
    public void setList(ArrayList<DataTeacherEntity> mSectionMistakeList) {
        this.mSectionMistakeList = mSectionMistakeList;
        notifyDataSetChanged();
    }

    public void setmExpandableListView(ExpandableListView mExpandableListView) {
        this.mExpandableListView = mExpandableListView;
    }

    @Override
    public int getGroupCount() {
        if (mSectionMistakeList != null) {
            return mSectionMistakeList.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mSectionMistakeList != null &&
                mSectionMistakeList.get(groupPosition) != null) {
            ArrayList<DataTeacherEntity> children = mSectionMistakeList.get(groupPosition).getChildren();
            return children != null ? children.size() : 0;
        }
        return 0;
    }

    @Override
    public DataTeacherEntity getGroup(int groupPosition) {
        if (mSectionMistakeList != null &&
                mSectionMistakeList.get(groupPosition) != null) {
            return mSectionMistakeList.get(groupPosition);
        }
        return null;
    }

    @Override
    public DataTeacherEntity getChild(int groupPosition, int childPosition) {
        if (mSectionMistakeList != null &&
                mSectionMistakeList.get(groupPosition) != null) {
            ArrayList<DataTeacherEntity> children = mSectionMistakeList.get(groupPosition).getChildren();
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
//            holder.imageView = (ImageView) row.findViewById(R.id.iv_middle_icon);
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
//            holder.frameLayout.setBackgroundResource(R.drawable.examination_no_data);
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
                String wrongNum = groupEntity.getData().getWrongNum();
                String wrongNumStr = null;
                if (wrongNum != null && !"0".equals(wrongNum)) {
                    if (wrongNum.length() == 1) {
                        wrongNumStr = "&#160;&#160;&#160;&#160;" + wrongNum;
                    } else if (wrongNum.length() == 2) {
                        wrongNumStr = "&#160;&#160;" + wrongNum;
                    } else {
                        wrongNumStr = wrongNum;
                    }
                    holder.rightArrow.setVisibility(View.VISIBLE);
                    holder.txtError.setVisibility(View.VISIBLE);
//                    holder.txtError.setText(Html.fromHtml(mContext.getResources().getString(R.string.mistake_edition_num_txt) +
//                            "<font color='#ff0000'>" + wrongNumStr + "</font>"));
                    holder.txtError.setText(Html.fromHtml("<font color='#b3476b'>" + wrongNumStr + "</font>"));
                } else {
                    holder.txtError.setVisibility(View.INVISIBLE);
                    holder.rightArrow.setVisibility(View.INVISIBLE);
                }
            } else {
                holder.rightArrow.setVisibility(View.INVISIBLE);
                holder.txtError.setVisibility(View.INVISIBLE);
            }
            holder.txtFather.setText(getGroup(groupPosition).getName());
            if(holder.rightArrow.getVisibility() != View.VISIBLE ) {
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

//    public void updateErrorSection(String chapterId, String sectionId) {
//        if (mSectionMistakeList != null) {
//            if (!TextUtils.isEmpty(sectionId) && !"0".equals(sectionId)) {
//                LogInfo.log("haitian", "chapterId =" + chapterId + "  sectionId=" + sectionId);
//                int dataIndex = 0;
//                for (DataTeacherEntity mDataTeacherEntity : mSectionMistakeList) {
//                    boolean isRemove = false;
//                    if (mDataTeacherEntity.getId().equals(chapterId)) {
//                        if (mDataTeacherEntity.getChildren() != null) {
//                            int index = 0;
//                            for (DataTeacherEntity mChildDataTeacherEntity : mDataTeacherEntity.getChildren()) {
//                                if (sectionId.equals(mChildDataTeacherEntity.getId())) {
//                                    LogInfo.log("haitian", "remove mChildDataTeacherEntity index=" + index);
//                                    mDataTeacherEntity.getChildren().remove(index);
//                                    isRemove = true;
//                                    if (mDataTeacherEntity.getData() != null) {
//                                        String wrongNum = mDataTeacherEntity.getData().getWrongNum();
//                                        if (wrongNum == null || "0".equals(wrongNum)) {
//                                            mSectionMistakeList.remove(dataIndex);
//                                        }
//                                    }
//                                    break;
//                                }
//                                index++;
//                            }
//                        }
//                        if (isRemove) {
//                            break;
//                        }
//                    }
//                    dataIndex++;
//                }
//            } else {
//                DataTeacherEntity mDataTeacherEntity = null;
//                int size = mSectionMistakeList.size();
//                for (int i = 0; i < size; i++) {
//                    mDataTeacherEntity = mSectionMistakeList.get(i);
//                    if (mDataTeacherEntity.getId().equals(chapterId)) {
//                        if (mDataTeacherEntity.getChildren() == null || mDataTeacherEntity.getChildren().size() <= 0) {
//                            mSectionMistakeList.remove(i);
//                        } else {
//                            DataWrongNumEntity dataWrongNumEntity = new DataWrongNumEntity();
//                            dataWrongNumEntity.setWrongNum("0");
//                            mSectionMistakeList.get(i).setData(dataWrongNumEntity);
//                        }
//                        break;
//                    }
//                }
//            }
//            DataTeacherEntity mDataTeacherEntity = null;
//            int size = mSectionMistakeList.size();
//            boolean haveData = false;
//            for (int i = 0; i < size; i++) {
//                mDataTeacherEntity = mSectionMistakeList.get(i);
//                if (mDataTeacherEntity != null && mDataTeacherEntity.getData() != null) {
//                    String wrongNum = mDataTeacherEntity.getData().getWrongNum();
//                    if (wrongNum != null && !"0".equals(wrongNum)) {
//                        haveData = true;
//                    }
//                }
//                if (mDataTeacherEntity != null && mDataTeacherEntity.getChildren() != null && mDataTeacherEntity.getChildren().size() > 0) {
//                    haveData = true;
//                }
//            }
//            if (!haveData) {
//                ((Activity) mContext).finish();
//            }
//        }
//        notifyDataSetChanged();
//    }

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
                String wrongNum = childEntity.getData().getWrongNum();
                String wrongNumStr = null;
                if (wrongNum != null && !"0".equals(wrongNum)) {
                    if (wrongNum.length() == 1) {
                        wrongNumStr = "&#160;&#160;&#160;&#160;" + wrongNum;
                    } else if (wrongNum.length() == 2) {
                        wrongNumStr = "&#160;&#160;" + wrongNum;
                    } else {
                        wrongNumStr = wrongNum;
                    }
//                    mViewHolderChild.txtError.setText(Html.fromHtml(mContext.getResources().getString(R.string.mistake_edition_num_txt) +
//                            "<font color='#ff0000'>" + wrongNumStr + "</font>"));
                    mViewHolderChild.txtError.setText(Html.fromHtml("<font color='#b3476b'>" + wrongNumStr + "</font>"));
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
