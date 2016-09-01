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
public class ClassfyQuestionAdapter extends  YXiuCustomerBaseAdapter<QuestionEntity.PointEntity> {
    public ClassfyQuestionAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(mContext, R.layout.classfy_question_list_adapter,null);
            holder.classfyQuestionName= (TextView) convertView.findViewById(R.id.classfyQuestionName);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        QuestionEntity.PointEntity entity=getItem(position);
        holder.classfyQuestionName.setText(entity.getName()+"(0)");
        return convertView;
    }

    class ViewHolder{
        private TextView classfyQuestionName;
        private TextView classfyQuestionNum;
    }

}
