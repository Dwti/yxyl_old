package com.yanxiu.gphone.student.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.core.view.UnMoveGridView;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.bean.SubjectVersionBean;
import com.yanxiu.gphone.student.view.UnMoveTextView;

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
            holder.classfyQuestionName= (UnMoveTextView) convertView.findViewById(R.id.classfyQuestionName);
            holder.classfyQuestionNum= (UnMoveTextView) convertView.findViewById(R.id.classfyQuestionNum);
            holder.position=position;
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

    int index=0;
    int heights=0;

    int max_height=0;

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public interface updatesuccesslistener{
        void updatesuccess();
    }
    updatesuccesslistener listenr;
    public void setlistener(updatesuccesslistener listenr){
        this.listenr=listenr;
    }

    class ViewHolder{
        private UnMoveTextView classfyQuestionName;
        private UnMoveTextView classfyQuestionNum;
        private int position;
        private int mheight;

        public void update() {
            // 精确计算GridView的item高度
            classfyQuestionName.getViewTreeObserver().addOnGlobalLayoutListener(listener);

        }
        ViewTreeObserver.OnGlobalLayoutListener listener=new ViewTreeObserver.OnGlobalLayoutListener(){
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onGlobalLayout() {
                classfyQuestionName.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                            int position = (Integer) classfyQuestionName.getTag();
                // 这里是保证同一行的item高度是相同的！！也就是同一行是齐整的 height相等
//                            if (position > 0) {
//                View v = (View) classfyQuestionNum.getTag();
//                int height = v.getHeight();

                View view = gv.getChildAt(position);
                int lastheight = view.getHeight();

                // 得到同一行的最后一个item和前一个item想比较，把谁的height大，就把两者中                                                                // height小的item的高度设定为height较大的item的高度一致，也就是保证同一                                                                 // 行高度相等即可
//                if (height > lastheight) {
////                                    view.setLayoutParams(new GridView.LayoutParams(
////                                            GridView.LayoutParams.MATCH_PARENT,
////                                            height));
////                                    index=index+height;
//                    mheight=height;
//                } else{
//                                    v.setLayoutParams(new GridView.LayoutParams(
//                                            GridView.LayoutParams.MATCH_PARENT,
//                                            lastheight));

                    mheight=lastheight;
//                }
//                                gv.setLayoutParams(new GridView.LayoutParams(
//                                        GridView.LayoutParams.FILL_PARENT,
//                                        index));
//                            int count=getCount();
                if (heights>mheight){

                }else {
                    heights=mheight;
                }

                if (position%2>0){
                    View view1 = gv.getChildAt(position-1);
                    View view2 = gv.getChildAt(position);

                    ViewHolder viewHolder1= (ViewHolder) view1.getTag();
                    ViewHolder viewHolder2= (ViewHolder) view2.getTag();

                    if (viewHolder1.mheight>viewHolder2.mheight){
                        max_height+=viewHolder1.mheight;
                        view1.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT,viewHolder1.mheight));
                        view2.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT,viewHolder1.mheight));
                    }else {
                        max_height+=viewHolder2.mheight;
                        view1.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT,viewHolder2.mheight));
                        view2.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT,viewHolder2.mheight));
                    }


                }

                if (position%2==0&&position==getCount()-1){
                    max_height+=mheight;
                }

                if (position%2>0||position==getCount()-1){
                    index=index+heights;
                    heights=0;
                }

                if (position==getCount()-1){
//                    LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
//                    gv.setLayoutParams(params);
//                    listenr.updatesuccess();
                }
//                LinearLayout.LayoutParams LayoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,100);
//                gv.setLayoutParams(LayoutParams);
            }
////                        }
        };
    }

}
