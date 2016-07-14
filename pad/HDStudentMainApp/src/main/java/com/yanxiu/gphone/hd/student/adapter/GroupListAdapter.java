package com.yanxiu.gphone.hd.student.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.bean.GroupBean;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.view.YanxiuTypefaceTextView;

/**
 * Created by Administrator on 2015/7/10.
 */
public class GroupListAdapter extends YXiuCustomerBaseAdapter<GroupBean>{

    public GroupListAdapter (Activity context){
        super(context);
    }

    @Override public View getView(int position, View convertView,
            ViewGroup parent) {
        GroupBean entity = getItem(position);
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.group_item_layout,null);
            holder = new ViewHolder();
            holder.spaceTopView = convertView.findViewById(R.id.group_hw_top_space_view);
            holder.spaceBottomView = convertView.findViewById(R.id.group_hw_bottom_space_view);
            holder.groupHwMainView = convertView.findViewById(R.id.group_hw_main_view);
            holder.groupHwSubView = convertView.findViewById(R.id.group_hw_sub_view);
            holder.dashLine = (ImageView)convertView.findViewById(R.id.group_hw_dash_line);
            holder.itemDashLine = (ImageView)convertView.findViewById(R.id.item_divider_line);

            holder.groupName = (TextView)holder.groupHwMainView.findViewById(R.id.item_name);
            holder.itemContent = (TextView)holder.groupHwMainView.findViewById(R.id.item_content);
            holder.itemContent.setVisibility(View.VISIBLE);
            holder.icon = (ImageView)holder.groupHwMainView.findViewById(R.id.item_icon);
            holder.itemHwIcon = (ImageView)holder.groupHwSubView.findViewById(R.id.group_hw_tag_iv);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Util.dipToPx(50),
                    Util
                    .dipToPx(18));
            params.leftMargin = Util.dipToPx(15);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            holder.itemHwIcon.setLayoutParams(params);
            Util.setViewTypeface(YanxiuTypefaceTextView.TypefaceType.FANGZHENG, holder.groupName);
            holder.info = (TextView)holder.groupHwSubView.findViewById(R.id.group_hw_title_tv);

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

    private void setData(GroupBean entity,ViewHolder holder){
        holder.groupName.setText(entity.getName());
        //-----------modified by zengsonghai 201604111816-----start-----
        if(entity.getWaitFinishNum() > 0) {
            holder.itemContent.setText(Html.fromHtml("<font color='#b3476b'>" + entity
                    .getWaitFinishNum() + " " + mContext.getResources().getString(R.string
                    .group_item_waitfinish_info) + "</font>"));
        } else {
            holder.itemContent.setText(Html.fromHtml("<font color='#805500'>" + mContext
                    .getResources().getString(R.string.group_item_finished_info) + "</font>"));

        }
        //-----------modified by zengsonghai 201604111816-----end-----
//        PaperBean paperBean = entity.getPaper();
//        if(paperBean!=null){
//            holder.groupHwSubView.setVisibility(View.VISIBLE);
//            holder.dashLine.setVisibility(View.VISIBLE);
//            holder.info.setText(paperBean.getName());
//        }else{
//            holder.dashLine.setVisibility(View.GONE);
//            holder.groupHwSubView.setVisibility(View.GONE);
//        }
        Util.setIcon(entity.getSubjectid(), holder.icon);
    }

    private class ViewHolder{
        private View spaceTopView;
        private View spaceBottomView;
        private View groupHwMainView;
        private View groupHwSubView;

        private ImageView itemDashLine;
        private ImageView itemHwIcon;
        private ImageView dashLine;
        private ImageView icon;
        private TextView groupName;
        private TextView itemContent;
        private TextView info;
    }

}
