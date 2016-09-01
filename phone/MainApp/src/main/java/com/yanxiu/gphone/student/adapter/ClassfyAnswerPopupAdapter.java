package com.yanxiu.gphone.student.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.view.question.YXiuAnserTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/31.
 */
public class ClassfyAnswerPopupAdapter extends BaseAdapter {
    private DisplayImageOptions options;
    private Activity mContext;
    private List<String> mEntity = new ArrayList<>();
    public ClassfyAnswerPopupAdapter(Activity context) {
        mContext = context;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)                       // 设置下载的图片是否缓存在SD卡中
                .showImageOnFail(R.drawable.image_default)
                .showImageForEmptyUri(R.drawable.image_default)
                .build();                                   // 创建配置过得DisplayImageOption对象
    }

    public void setData(List<String> list) {
        mEntity = list;
        notifyDataSetInvalidated();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder=new ViewHolder();
            convertView=View.inflate(mContext, R.layout.classfy_answer_list_adapter,null);
            holder.classfyAnswerImg= (YXiuAnserTextView) convertView.findViewById(R.id.classfyAnswerImg);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.classfyAnswerImg.setTextHtml(mEntity.get(position));
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
        private YXiuAnserTextView classfyAnswerImg;
    }

}
