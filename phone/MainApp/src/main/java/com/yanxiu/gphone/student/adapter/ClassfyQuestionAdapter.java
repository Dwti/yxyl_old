package com.yanxiu.gphone.student.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.SubjectVersionBean;

/**
 * Created by Administrator on 2016/8/31.
 */
public class ClassfyQuestionAdapter extends  YXiuCustomerBaseAdapter<QuestionEntity> {
    public ClassfyQuestionAdapter(Activity context) {
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
        QuestionEntity entity=getItem(position);
        //holder.text.setText(entity.getName());
        return convertView;
    }

    class ViewHolder{
        private TextView text;
    }

}
