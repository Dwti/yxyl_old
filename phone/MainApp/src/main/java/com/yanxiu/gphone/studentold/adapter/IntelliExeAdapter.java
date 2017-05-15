package com.yanxiu.gphone.studentold.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.bean.SubjectVersionBean;
import com.yanxiu.gphone.studentold.utils.Util;

/**
 * Created by Administrator on 2015/7/7.
 */
public class IntelliExeAdapter extends YXiuCustomerBaseAdapter<SubjectVersionBean.DataEntity> {

    private Activity mContext;
    private ViewHolder holder;

    private OnSubjectListener mOnSubjectListener;


    public IntelliExeAdapter(Activity context) {
        super(context);
        mContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            row = inflater.inflate(R.layout.item_intelliexe, null);
            holder  = new ViewHolder();
            holder.ivIcon = (ImageView) row.findViewById(R.id.intelliexe_icon);
            holder.tvSname = (TextView) row.findViewById(R.id.tv_sname);
            holder.tvEname = (TextView) row.findViewById(R.id.tv_ename);
            holder.rlSelectSubjectVersion = (RelativeLayout) row.findViewById(R.id.rl_select_subject_version);
            holder.rlSelectSubject = (RelativeLayout) row.findViewById(R.id.rl_select_subject);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        final View finalRow = row;

        final SubjectVersionBean.DataEntity data = mList.get(position);
        holder.tvSname.setText(data.getName());
        if(data.getData() != null){
            holder.tvEname.setText(data.getData().getEditionName());
            holder.tvEname.setGravity(Gravity.CENTER);
            holder.rlSelectSubjectVersion.setBackgroundResource(R.drawable.icon_subject_select_selector);
        }else {
            holder.tvEname.setText(R.string.subject_can_not_selected);
            holder.tvEname.setGravity(Gravity.LEFT);
            holder.rlSelectSubjectVersion.setBackgroundResource(R.drawable.icon_subject_selector);

        }

        holder.rlSelectSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnSubjectListener != null){
                    mOnSubjectListener.selectSubject(position, finalRow);
                }
            }
        });

        holder.rlSelectSubjectVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.getData() != null && !TextUtils.isEmpty(data.getData().getEditionName())){
                    if(mOnSubjectListener != null){
                        mOnSubjectListener.selectSubject(position, finalRow);
                    }
                }else {
                    if(mOnSubjectListener != null){
                        mOnSubjectListener.selectSubjectVersion(position, finalRow);
                    }
                }

            }
        });

        int idRes = Util.getIconRes(data.getId());
        data.setResId(idRes);
        holder.ivIcon.setBackgroundResource(idRes);
        return row;
    }

    public void setOnSubjectListener(OnSubjectListener onSubjectListener) {
        this.mOnSubjectListener = onSubjectListener;
    }



    public interface OnSubjectListener{
        void selectSubjectVersion(int position, View tagView);
        void selectSubject(int position, View tagView);


    }


    static class ViewHolder {
        TextView tvSname;
        TextView tvEname;
        ImageView ivIcon;
        RelativeLayout rlSelectSubjectVersion;
        RelativeLayout rlSelectSubject;
    }
}
