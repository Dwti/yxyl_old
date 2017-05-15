package com.yanxiu.gphone.studentold.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.bean.SubjectVersionBean;

/**
 * Created by Administrator on 2015/11/16.
 */
public class SubjectListAdapter extends  YXiuCustomerBaseAdapter<SubjectVersionBean.DataEntity> {
    public SubjectListAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(mContext, R.layout.subject_list_adapter_layout,null);
            holder.text= (TextView) convertView.findViewById(R.id.subjectName);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        SubjectVersionBean.DataEntity entity=getItem(position);
        holder.text.setText(entity.getName());
        return convertView;
    }

    class ViewHolder{
        private TextView text;
    }

}
