package com.yanxiu.gphone.student.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.GroupHwUnBean;
import com.yanxiu.gphone.student.bean.SimpleGroup;
import com.common.core.utils.CommonCoreUtil;

/**
 * Created by Administrator on 2015/7/10.
 */
public class GroupHwUndoListAdapter extends YXiuCustomerBaseAdapter<GroupHwUnBean>{

    public GroupHwUndoListAdapter(Activity context){
        super(context);
    }

    @Override public View getView(int position, View convertView,
            ViewGroup parent) {
        GroupHwUnBean entity = getItem(position);
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.group_hw_undo_list_item_layout,null);
            holder = new ViewHolder();
            holder.spaceTopView = convertView.findViewById(R.id.group_todo_top_space_view);
            holder.spaceBottomView = convertView.findViewById(R.id.group_todo_bottom_space_view);
            holder.groupHwMainView = convertView.findViewById(R.id.group_todo_main_view);
            holder.groupHwSubView = convertView.findViewById(R.id.group_todo_sub_view);
            holder.dashLine = (ImageView)convertView.findViewById(R.id.group_todo_dash_line);
            holder.itemDashLine = (ImageView)convertView.findViewById(R.id.item_divider_line);

            holder.groupHwSubView.setVisibility(View.VISIBLE);
            holder.dashLine.setVisibility(View.VISIBLE);

            holder.groupHwMainView.findViewById(R.id.item_icon).setVisibility(View.GONE);

            holder.paperName = (TextView)holder.groupHwMainView.findViewById(R.id.item_name);
            holder.paperName.setTextSize(17);
            holder.qusNum = (TextView)holder.groupHwMainView.findViewById(R.id.item_content);
            holder.qusNum.setVisibility(View.VISIBLE);

            holder.stateIcon = (ImageView)holder.groupHwSubView.findViewById(R.id.group_hw_tag_iv);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(CommonCoreUtil.dipToPx(30),
                    CommonCoreUtil.dipToPx(18));
            params.leftMargin = CommonCoreUtil.dipToPx(10);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            holder.stateIcon.setLayoutParams(params);
            holder.stateIcon.setBackgroundResource(R.drawable.group_todo_subject);

            holder.stateTv = (TextView)holder.groupHwSubView.findViewById(R.id.group_hw_title_tv);
            holder.stateTv.setTextColor(mContext.getResources().getColor(R.color.color_805500));
            holder.deadLineIcon = (ImageView)holder.groupHwSubView.findViewById(R.id.group_hw_deadline_iv);
            holder.deadLineTimeTv = (TextView)holder.groupHwSubView.findViewById(R.id.group_hw_deadline_tv);

            holder.deadLineIcon.setVisibility(View.VISIBLE);
            holder.deadLineTimeTv.setVisibility(View.VISIBLE);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        if(position == 0){
            holder.spaceTopView.setVisibility(View.VISIBLE);
        } else{
            holder.spaceTopView.setVisibility(View.GONE);
        }
        if((position+1) >= getCount()){
            holder.itemDashLine.setVisibility(View.GONE);
            holder.spaceBottomView.setVisibility(View.VISIBLE);
        } else {
            if(holder.spaceBottomView.getVisibility() == View.VISIBLE){
                holder.spaceBottomView.setVisibility(View.GONE);
            }
            if(holder.itemDashLine.getVisibility() == View.GONE){
                holder.itemDashLine.setVisibility(View.VISIBLE);
            }
        }
        setData(entity,holder);
        return convertView;
    }

    private void setData(GroupHwUnBean entity,ViewHolder holder){
        if(entity!=null){
            holder.paperName.setText(entity.getName());
            holder.qusNum.setText(" "+entity.getQuesnum()+" ");
            if(entity.getIsEnd() == 0){  //未截止
                holder.deadLineTimeTv.setText(mContext.getResources()
                        .getString(R.string.hw_undo_state_un_deadline,
                                entity.getOverTime()));
            }else{ //已截止
                holder.deadLineTimeTv.setText(R.string.hw_undo_state_deadline);
            }
            SimpleGroup simpleGroup = entity.getGroup();
            if(simpleGroup!=null){
                holder.stateTv.setVisibility(View.VISIBLE);
                holder.stateTv.setText(mContext.getResources()
                        .getString(R.string.hw_undo_from, simpleGroup.getName()));
            }else{
                holder.stateTv.setVisibility(View.GONE);
            }
        }
    }

    private class ViewHolder{
        private View spaceTopView;
        private View spaceBottomView;
        private View groupHwMainView;
        private View groupHwSubView;
        private ImageView itemDashLine;
        private ImageView dashLine;

        private TextView paperName;
        private TextView qusNum;

        private ImageView stateIcon;
        private TextView stateTv;

        private ImageView deadLineIcon;
        private TextView deadLineTimeTv;
    }
}
