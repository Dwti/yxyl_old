package com.yanxiu.gphone.studentold.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.core.utils.StringUtils;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.bean.GroupHwBean;
import com.yanxiu.gphone.studentold.bean.PaperStatusBean;

/**
 * Created by Administrator on 2015/7/10.
 */
public class GroupHwListAdapter extends YXiuCustomerBaseAdapter<GroupHwBean>{

    public GroupHwListAdapter(Activity context){
        super(context);
    }

    @Override public View getView(int position, View convertView,
            ViewGroup parent) {
        GroupHwBean entity = getItem(position);
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.group_hw_list_item_layout, null);
            holder = new ViewHolder();
            holder.spaceTopView = convertView.findViewById(R.id.group_hw_top_space_view);
            holder.spaceBottomView = convertView.findViewById(R.id.group_hw_bottom_space_view);
            holder.groupHwMainView = convertView.findViewById(R.id.group_hw_main_view);
            holder.groupHwSubView = convertView.findViewById(R.id.group_hw_sub_view);
            holder.dashLine = (ImageView)convertView.findViewById(R.id.group_hw_dash_line);
            holder.itemDashLine = (ImageView)convertView.findViewById(R.id.item_divider_line);

            holder.groupHwSubView.setVisibility(View.VISIBLE);
            holder.dashLine.setVisibility(View.VISIBLE);

            holder.groupHwMainView.findViewById(R.id.item_icon).setVisibility(View.GONE);

            holder.paperName = (TextView)holder.groupHwMainView.findViewById(R.id.item_name);
            holder.paperName.setTextSize(17);
            holder.qusNum = (TextView)holder.groupHwMainView.findViewById(R.id.item_content);
            holder.qusNum.setVisibility(View.VISIBLE);

            holder.stateIcon = (ImageView)holder.groupHwSubView.findViewById(R.id.group_hw_tag_iv);
            holder.stateTv = (TextView)holder.groupHwSubView.findViewById(R.id.group_hw_title_tv);

            holder.deadLineIcon = (ImageView)holder.groupHwSubView.findViewById(R.id.group_hw_deadline_iv);
            holder.deadLineTimeTv = (TextView)holder.groupHwSubView.findViewById(R.id.group_hw_deadline_tv);

            holder.deadLineTimeTv.setVisibility(View.VISIBLE);

            holder.message = (TextView)convertView.findViewById(R.id.group_hw_list_item_message);
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

    private void setData(GroupHwBean entity,ViewHolder holder){
        if(entity!=null){
            holder.paperName.setText(entity.getName());
//            holder.qusNum.setText(" "+entity.getQuesnum()+" ");
            /**status含义：0 待完成 1 未完成 2 已完成  3 可补做*/
            PaperStatusBean paperStatusBean = entity.getPaperStatus();
            if(paperStatusBean!=null){
                if(paperStatusBean.getStatus() == 0){
                    holder.stateIcon.setBackgroundResource(R.drawable.group_hw_to_do);
                    holder.stateTv.setTextColor(mContext.getResources().getColor(R.color.color_b3476b));
                    holder.stateTv.setText(mContext.getResources().getText(R.string
                            .practice_history_todo_new) + (entity.getAnswernum() + "/" + entity
                            .getQuesnum()));
                    holder.deadLineTimeTv.setVisibility(View.VISIBLE);
                    if(entity.getIsEnd() == 0){  //未截止
                        holder.deadLineTimeTv.setText(mContext.getResources()
                                .getString(R.string.hw_undo_state_un_deadline_new)+entity.getRemaindertimeStr());
                    }else{//已截止
                        holder.deadLineTimeTv.setText(R.string.hw_undo_state_deadline_new);
                    }
                }else  if(paperStatusBean.getStatus() == 1){
                    holder.stateIcon.setBackgroundResource(R.drawable.group_hw_unfinished);
                    holder.stateTv.setTextColor(mContext.getResources().getColor(R.color
                            .color_007373));
                    holder.stateTv.setText(R.string.practice_history_not_done_new);
                    holder.deadLineTimeTv.setVisibility(View.GONE);
                }else  if(paperStatusBean.getStatus() == 2){
                    holder.stateIcon.setBackgroundResource(R.drawable.group_hw_completed);
                    holder.stateTv.setTextColor(mContext.getResources().getColor(R.color
                            .color_805500));
                    holder.stateTv.setText(R.string.practice_history_done_new);
                    holder.deadLineTimeTv.setVisibility(View.GONE);
                }else if(paperStatusBean.getStatus() == 3){

                }
                if (StringUtils.isEmpty(paperStatusBean.getTeachercomments()) || StringUtils.isEmpty(paperStatusBean.getTeacherName())) {
                    holder.message.setVisibility(View.GONE);
                    holder.groupHwSubView.setVisibility(View.VISIBLE);
                }else {
                    holder.groupHwSubView.setVisibility(View.GONE);
                    holder.message.setVisibility(View.VISIBLE);
                    holder.message.setText(paperStatusBean.getTeacherName() +":"+ paperStatusBean
                            .getTeachercomments());
                }
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
        private TextView message;

        private ImageView stateIcon;
        private TextView stateTv;

        private ImageView deadLineIcon;
        private TextView deadLineTimeTv;
    }
}
