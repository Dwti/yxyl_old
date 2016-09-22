package com.yanxiu.gphone.student.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.ClassfyBean;
import com.yanxiu.gphone.student.bean.QuestionEntity;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/8/31.
 */
public class ClassfyAnswerAdapter extends BaseAdapter {
    private DisplayImageOptions options;
    private Activity mContext;
    private List<ClassfyBean> mEntity = new ArrayList<ClassfyBean>();
    private int clickTemp = -1;//标识被选择的item
    public ClassfyAnswerAdapter(Activity context) {
        mContext = context;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)                       // 设置下载的图片是否缓存在SD卡中
                .showImageOnFail(R.drawable.image_default)
                .showImageForEmptyUri(R.drawable.image_default)
                .build();                                   // 创建配置过得DisplayImageOption对象
    }

    public void setData(List<ClassfyBean> list) {
        mEntity = list;
        notifyDataSetInvalidated();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder=new ViewHolder();
            convertView=View.inflate(mContext, R.layout.classfy_answer_list_adapter,null);
            holder.classfyAnswerImg= (ImageView) convertView.findViewById(R.id.classfyAnswerImg);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        Pattern pattern = Pattern.compile("<img src=\\\"(.*?)\\\"");
        Matcher matcher = pattern.matcher(mEntity.get(position).getName());
        while(matcher.find()){
            ImageLoader.getInstance().displayImage(matcher.group(1), holder.classfyAnswerImg, options);
        }
        if(clickTemp == position) {    //根据点击的Item当前状态设置背景
            convertView.setAlpha(0.5f);
        } else {
            convertView.setAlpha(1f);
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return mEntity.size();//返回数组的长度
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
        private ImageView classfyAnswerImg;
    }

    public void setSeclection(int posiTion) {
        clickTemp = posiTion;
    }

}
