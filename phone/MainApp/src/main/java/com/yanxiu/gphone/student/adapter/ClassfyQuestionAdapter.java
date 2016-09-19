package com.yanxiu.gphone.student.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.SubjectVersionBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/31.
 */
public class ClassfyQuestionAdapter extends BaseAdapter {
    private Context mContext;
    private QuestionEntity questionsEntity;
    private GridView gv;
    public ClassfyQuestionAdapter(GridView gv, Context context) {
        this.gv = gv;
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
            holder.update();
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        JSONObject object = null;
        String string = null;
        try {
            object = new JSONObject(questionsEntity.getAnswer().get(position));
            string = object.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.classfyQuestionName.setText(string);
        holder.classfyQuestionNum.setText("(" + questionsEntity.getAnswerBean().getConnect_classfy_answer().get(position).size() + ")");
        holder.classfyQuestionName.setTag(position);
        holder.classfyQuestionNum.setTag(convertView);
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
        return questionsEntity.getAnswer().size();//返回数组的长度
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

        public void update() {
            // 精确计算GridView的item高度
            classfyQuestionName.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        public void onGlobalLayout() {
                            int position = (Integer) classfyQuestionName.getTag();
                            // 这里是保证同一行的item高度是相同的！！也就是同一行是齐整的 height相等
                            if (position > 0 && position % 2 == 1) {
                                View v = (View) classfyQuestionNum.getTag();
                                int height = v.getHeight();

                                View view = gv.getChildAt(position - 1);
                                int lastheight = view.getHeight();
                                // 得到同一行的最后一个item和前一个item想比较，把谁的height大，就把两者中                                                                // height小的item的高度设定为height较大的item的高度一致，也就是保证同一                                                                 // 行高度相等即可
                                if (height > lastheight) {
                                    view.setLayoutParams(new GridView.LayoutParams(
                                            GridView.LayoutParams.FILL_PARENT,
                                            height));
                                } else if (height < lastheight) {
                                    v.setLayoutParams(new GridView.LayoutParams(
                                            GridView.LayoutParams.FILL_PARENT,
                                            lastheight));
                                }
                            }
                        }
                    });
        }

    }

}
