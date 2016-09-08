package com.yanxiu.gphone.student.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.SubjectVersionBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/31.
 */
public class ClassfyQuestionAdapter extends BaseAdapter {
    private Context mContext;
    private QuestionEntity questionsEntity;
    public ClassfyQuestionAdapter(Context context) {
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(mContext, R.layout.classfy_question_list_adapter,null);
            holder.classfyQuestionName= (TextView) convertView.findViewById(R.id.classfyQuestionName);
            holder.classfyQuestionNum= (TextView) convertView.findViewById(R.id.classfyQuestionNum);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.classfyQuestionName.setText(questionsEntity.getPoint().get(position).getName());
        holder.classfyQuestionNum.setText("(" + questionsEntity.getAnswerBean().getConnect_classfy_answer().get(position).size() + ")");
        return convertView;
    }

    public void setData(QuestionEntity questionEntity) {
        questionsEntity = questionEntity;
        notifyDataSetInvalidated();
    }

    @Override
    public int getCount() {
        if (questionsEntity == null) {
            return 0;
        }
        return questionsEntity.getPoint().size();//返回数组的长度
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder{
        private TextView classfyQuestionName;
        private TextView classfyQuestionNum;
    }

}
