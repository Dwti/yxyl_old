package com.yanxiu.gphone.parent.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.activity.ParentHomeSubjectInfActivity;
import com.yanxiu.gphone.parent.bean.ParentHomeDetailBean;
import com.yanxiu.gphone.parent.utils.ParentUtils;

import java.util.List;

/**
 * Created by Administrator on 2015/7/10.
 */
public class ParentHomeAdapter extends YXiuCustomerBaseAdapter<ParentHomeDetailBean.DataBean>{

    public ParentHomeAdapter(Activity context){
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ParentHomeDetailBean.DataBean entity = getItem(position);
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_home_layout,null);
            holder = new ViewHolder();
            holder.tvHomeDate = (TextView) convertView.findViewById(R.id.tv_home_date);
            holder.tvHomeTime = (TextView) convertView.findViewById(R.id.tv_home_time);
            holder.ivSubjectIcon = (ImageView) convertView.findViewById(R.id.iv_subject_icon);


            holder.tvSubjectName = (TextView) convertView.findViewById(R.id.tv_subjcet_name);
            holder.tvHomeTeacherName = (TextView) convertView.findViewById(R.id.tv_home_teacher_name);

            holder.tvHomeTeacherSayed = (TextView) convertView.findViewById(R.id.tv_home_teacher_sayed);
            holder.tvStudentHonor = (TextView) convertView.findViewById(R.id.tv_home_student_honor);


            holder.line = convertView.findViewById(R.id.view_home_line);
            holder.tvCheckMore = (TextView) convertView.findViewById(R.id.tv_check_more);

            holder.tvTeacherComment = (TextView) convertView.findViewById(R.id.tv_home_techer_comment);

            holder.rlLine = (RelativeLayout) convertView.findViewById(R.id.rl_item_line);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }


        setViewType(entity.getType(), holder, entity);


        return convertView;
    }

    @Override
    public void clearDataSrouces() {
        strDate = "";
        if(mList != null){
            mList.clear();
        }
        this.notifyDataSetChanged();
    }


    private class ViewHolder{
        private TextView tvHomeDate;
        private TextView tvHomeTime;
        private ImageView ivSubjectIcon;

        private TextView tvSubjectName;
        private TextView tvHomeTeacherName;

        private TextView tvHomeTeacherSayed;
        private TextView tvStudentHonor;

        private View line;
        private TextView tvCheckMore;

        private TextView tvTeacherComment;

        private RelativeLayout rlLine;
    }

    private enum HomeType{
        FINISH(0),UNFINIS(1),EXERCISE(2);

        HomeType(int type) {
            this.type = type;
        }
        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    private void setViewType(int type, ViewHolder holder, final ParentHomeDetailBean.DataBean entity){
        final ParentHomeDetailBean.DataBean.DetailJsonBean detailBean = entity.getDetailJson();

        try {
            String strDate = ParentUtils.longToFormateTime(entity.getPushTime());
            holder.tvHomeTime.setText(strDate);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(entity.isFirstTag()){
            String strTime = entity.getFormateTime();
            holder.tvHomeDate.setText(strTime);
            holder.rlLine.setVisibility(View.VISIBLE);
        }else{
            holder.rlLine.setVisibility(View.GONE);
        }

        if(detailBean != null) {

            //设置subject icon
            ParentUtils.setIcon(detailBean.getSubjectId(), holder.ivSubjectIcon);

            //设置subjcet name
            if (!TextUtils.isEmpty(detailBean.getSubjectName())) {
                holder.tvSubjectName.setVisibility(View.VISIBLE);
                ParentUtils.setSubjectTextColor(detailBean.getSubjectId(), holder.tvSubjectName);
                holder.tvSubjectName.setText(detailBean.getSubjectName());
            } else {
                holder.tvSubjectName.setVisibility(View.GONE);
            }

            if(type == HomeType.FINISH.type){
                //老师评语 --------
                if(!TextUtils.isEmpty(detailBean.getTeacherComment())){
                    holder.tvTeacherComment.setVisibility(View.VISIBLE);
                    String teacherComment = mContext.getResources().getString(R.string.parent_home_teacher_comment);
                    teacherComment = String.format(teacherComment, detailBean.getTeacherComment());
                    holder.tvTeacherComment.setText(teacherComment);
                }else{
                    holder.tvTeacherComment.setVisibility(View.GONE);
                }


                holder.line.setVisibility(View.VISIBLE);
                holder.tvCheckMore.setVisibility(View.VISIBLE);


//                holder.tvTeacherComment.setVisibility(View.VISIBLE);
                holder.tvSubjectName.setVisibility(View.VISIBLE);


                holder.tvCheckMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ParentHomeSubjectInfActivity.launch(mContext, entity.getPid(), detailBean.getSubjectName(), entity.getPushTime());
                    }
                });


            }else if(type == HomeType.UNFINIS.type){

                holder.line.setVisibility(View.GONE);
                holder.tvCheckMore.setVisibility(View.GONE);

                holder.tvStudentHonor.setVisibility(View.GONE);
                holder.tvTeacherComment.setVisibility(View.GONE);

                holder.tvSubjectName.setVisibility(View.VISIBLE);

            }else if(type == HomeType.EXERCISE.type){

                holder.tvSubjectName.setVisibility(View.VISIBLE);
                holder.tvSubjectName.setText(mContext.getResources().getString(R.string.yanxiu_parent_exercise_txt));
                holder.tvSubjectName.setTextColor(mContext.getResources().getColor(R.color.color_666666_p));

                holder.ivSubjectIcon.setBackgroundResource(R.drawable.parent_exercise_icon);


                holder.line.setVisibility(View.GONE);
                holder.tvCheckMore.setVisibility(View.GONE);

                holder.tvStudentHonor.setVisibility(View.GONE);

                holder.tvTeacherComment.setVisibility(View.GONE);
//                holder.tvSubjectName.setVisibility(View.GONE);

            }


            //未完成的填充内容 -----  teacher sayed View
            if(!TextUtils.isEmpty(detailBean.getMessageText())){
                holder.tvHomeTeacherSayed.setVisibility(View.VISIBLE);
                holder.tvHomeTeacherSayed.setText(detailBean.getMessageText());
            }else{
                holder.tvHomeTeacherSayed.setVisibility(View.GONE);
            }

            //设置老师名字
            if (!TextUtils.isEmpty(detailBean.getTeacherName())) {
                holder.tvHomeTeacherName.setVisibility(View.VISIBLE);
                holder.tvHomeTeacherName.setText(detailBean.getTeacherName());
            } else {
                holder.tvHomeTeacherName.setVisibility(View.GONE);
            }


            //解析荣誉数据
            List<ParentHomeDetailBean.DataBean.DetailJsonBean.HonorListBean> honorList = detailBean.getHonorList();
            if(honorList != null && !honorList.isEmpty()){
                holder.tvStudentHonor.setVisibility(View.VISIBLE);
                StringBuilder sbHonorList = new StringBuilder();
                int count = honorList.size();
                for(int i = 0; i < count; i++){
                    sbHonorList.append(honorList.get(i).getHonorName());
                    if(i != count -1){
                        sbHonorList.append(",");
                    }
                }
                String studentHonor = mContext.getResources().getString(R.string.parent_home_student_honor);
                studentHonor = String.format(studentHonor, sbHonorList.toString());
                holder.tvStudentHonor.setText(studentHonor);
            }else{
                holder.tvStudentHonor.setVisibility(View.GONE);
            }

        }
    }

    private String strDate = "";
    public void filterData(List<ParentHomeDetailBean.DataBean> dataList){


        for(ParentHomeDetailBean.DataBean dataBean : dataList){
            try {
                if(!strDate.equals(ParentUtils.longToFormateDate(dataBean.getPushTime()))){
                    strDate = ParentUtils.longToFormateDate(dataBean.getPushTime());
                    dataBean.setIsFirstTag(true);
                }
                dataBean.setFormateTime(strDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }



}
