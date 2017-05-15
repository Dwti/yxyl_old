package com.yanxiu.gphone.studentold.adapter;

import android.content.Context;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.yanxiu.gphone.studentold.HtmlParser.MyHtml;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.bean.ClassfyBean;
import com.yanxiu.gphone.studentold.utils.ClassfyImageGetter2;
import com.yanxiu.gphone.studentold.view.question.YXiuAnserTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/31.
 */
public class ClassfyAnswerPopupAdapter extends BaseAdapter {
    private DisplayImageOptions options;
    private Context mContext;
    private List<ClassfyBean> mEntity = new ArrayList<ClassfyBean>();
    public ClassfyAnswerPopupAdapter(Context context) {
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
            convertView=View.inflate(mContext, R.layout.classfy_answer_list_popup_adapter,null);
            holder.classfyAnswerImg= (ImageView) convertView.findViewById(R.id.classfyAnswerImg);
            holder.tv_classfyAnswerImg= (YXiuAnserTextView) convertView.findViewById(R.id.tv_classfyAnswerImg);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
//        Pattern pattern = Pattern.compile("<img src=\\\"(.*?)\\\"");
//        Matcher matcher = pattern.matcher(mEntity.get(position).getName());
//        while(matcher.find()){
//            ImageLoader.getInstance().displayImage(matcher.group(1), holder.classfyAnswerImg, options);
//        }
        holder.classfyAnswerImg.setVisibility(View.GONE);
        holder.tv_classfyAnswerImg.setVisibility(View.VISIBLE);

        ClassfyImageGetter2 classfyImageGetter = new ClassfyImageGetter2(holder.tv_classfyAnswerImg, mContext);
        Spanned spanned = MyHtml.fromHtml(mContext, mEntity.get(position).getName(), classfyImageGetter, null, null, null);
        holder.tv_classfyAnswerImg.setText(spanned);
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
        private YXiuAnserTextView tv_classfyAnswerImg;
    }

}
