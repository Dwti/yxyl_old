package com.yanxiu.gphone.hd.student.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.common.core.utils.StringUtils;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.bean.PracticeHistoryChildBean;
import com.yanxiu.gphone.hd.student.utils.Util;

/**
 * Created by Administrator on 2015/7/13.
 */
public class PracticeHistoryAdapter extends YXiuCustomerBaseAdapter {
    private LayoutInflater layoutInflater;
    private int idRes;
    public PracticeHistoryAdapter(Activity context, String subjectId) {
        super(context);
        idRes = Util.getIconRes(subjectId);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.practice_history_item_layout, null);
            holder.spaceTopView = convertView.findViewById(R.id.practice_history_top_space_view);
            holder.groupHwMainView = convertView.findViewById(R.id.practice_history_main_view);
            holder.groupHwSubView = convertView.findViewById(R.id.practice_history_sub_view);
            holder.dashLine = (ImageView)convertView.findViewById(R.id.practice_history_dash_line);
            holder.itemDashLine = (ImageView)convertView.findViewById(R.id.item_divider_line);

            holder.groupHwSubView.setVisibility(View.VISIBLE);
            holder.dashLine.setVisibility(View.VISIBLE);

            holder.groupHwMainView.findViewById(R.id.item_icon).setVisibility(View.GONE);

            holder.paperName = (TextView)holder.groupHwMainView.findViewById(R.id.item_name);
            holder.paperName.setTextSize(17);
            holder.qusNum = (TextView)holder.groupHwMainView.findViewById(R.id.item_content);

            holder.stateIcon = (ImageView)holder.groupHwSubView.findViewById(R.id.group_hw_tag_iv);

            holder.stateTv = (TextView)holder.groupHwSubView.findViewById(R.id.group_hw_title_tv);
            holder.stateTv.setTextColor(mContext.getResources().getColor(R.color.color_805500));
            holder.deadLineIcon = (ImageView)holder.groupHwSubView.findViewById(R.id.group_hw_deadline_iv);
            holder.deadLineTimeTv = (TextView)holder.groupHwSubView.findViewById(R.id.group_hw_deadline_tv);

            holder.deadLineIcon.setVisibility(View.VISIBLE);
            holder.deadLineTimeTv.setVisibility(View.VISIBLE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PracticeHistoryChildBean data = (PracticeHistoryChildBean) mList.get(position);
        if(data != null) {
            holder.paperName.setText(data.getName());
            holder.deadLineTimeTv.setText(data.getBuildTime());
            String questionNum = data.getQuestionNum()+"";
            if(!StringUtils.isEmpty(questionNum)){
//                String str = "";
//                if(questionNum.length() == 1){
//                    str = "&#160;&#160;" + questionNum + "&#160;&#160;";
//                }else if(questionNum.length() == 2){
//                    str = "&#160;" + questionNum + "&#160;";
//                }else {
//                    str = questionNum;
//                }
                holder.qusNum.setVisibility(View.VISIBLE);
//                holder.qusNum.setText(Html.fromHtml(mContext.getString(
//                        R.string.practice_history_total, str)));
                holder.qusNum.setText(questionNum);
            }else{
                holder.qusNum.setVisibility(View.GONE);
            }
//            holder.practiceHistoryStatus.setText(data.getStatus() == 0 ? R.string.practice_history_not_done : R.string.practice_history_done);
            if(data.getStatus() == 2 ) {   //已完成
                holder.stateTv.setTextColor(mContext.getResources().getColor(R.color.color_805500));
                holder.stateIcon.setBackgroundResource(R.drawable.group_hw_completed);
                String wrongNum = data.getCorrectNum()+"";
                if(!StringUtils.isEmpty(wrongNum)){
//                    String str = "";
//                    if(wrongNum.length() == 1){
//                        str = "&#160;&#160;" + wrongNum + "&#160;&#160;";
//                    }else if(wrongNum.length() == 2){
//                        str = "&#160;" + wrongNum + "&#160;";
//                    }else {
//                        str = wrongNum;
//                    }
                    holder.stateTv.setText(mContext.getResources().getString(R.string
                            .practice_history_qus_right, wrongNum));
                }else{
                    holder.stateTv.setText(mContext.getResources().getString(R.string
                            .practice_history_qus_right, "0"));
                }
            } else {  //未完成
                holder.stateIcon.setBackgroundResource(R.drawable.group_hw_unfinished);
                holder.stateTv.setTextColor(mContext.getResources().getColor(R.color.color_007373));
                holder.stateTv.setText(R.string.practice_history_not_done);
            }
        }
        if(position == 0){
            holder.spaceTopView.setVisibility(View.VISIBLE);
        } else{
            holder.spaceTopView.setVisibility(View.GONE);
        }
        if((position+1) >= getCount()){
            holder.itemDashLine.setVisibility(View.GONE);
        } else {
            if(holder.itemDashLine.getVisibility() == View.GONE){
                holder.itemDashLine.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }
    public final class ViewHolder {
//        public ImageView practiceHistoryIcon;
//        public TextView practiceHistoryName;
//        public TextView practiceHistoryTime;
//        public TextView practiceHistoryTotal;
//        public TextView practiceHistoryRight;
//        public TextView practiceHistoryStatus;

        private View spaceTopView;
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
