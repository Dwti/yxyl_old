package com.yanxiu.gphone.studentold.adapter;

import android.app.Activity;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
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
public class ClassfyAnswerAdapter extends BaseAdapter {
    private DisplayImageOptions options;
    private Activity mContext;
    private List<ClassfyBean> mEntity = new ArrayList<ClassfyBean>();
    private int clickTemp = -1;//标识被选择的item
    private GridView gridView;
    public ClassfyAnswerAdapter(Activity context, GridView gridView) {
        mContext = context;
        this.gridView=gridView;
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
            holder.tv_classfyAnswerImg= (YXiuAnserTextView) convertView.findViewById(R.id.tv_classfyAnswerImg);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.tv_classfyAnswerImg.setVisibility(View.VISIBLE);
        holder.classfyAnswerImg.setVisibility(View.GONE);
//        Pattern pattern = Pattern.compile("<img src=\\\"(.*?)\\\"");
//        Matcher matcher = pattern.matcher(mEntity.get(position).getName());
//        while(matcher.find()){
//            ImageLoader.getInstance().displayImage(matcher.group(1), holder.classfyAnswerImg, options);
//        }

        ClassfyImageGetter2 classfyImageGetter = new ClassfyImageGetter2(holder.tv_classfyAnswerImg, mContext);
        Spanned spanned = MyHtml.fromHtml(mContext, mEntity.get(position).getName(), classfyImageGetter, null, null, null);
        holder.tv_classfyAnswerImg.setText(spanned);
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
        private YXiuAnserTextView tv_classfyAnswerImg;
    }

    public void setSeclection(int posiTion) {
        int vis_fir_posi=gridView.getFirstVisiblePosition();
        int vis_la_posi=gridView.getLastVisiblePosition();
        if (clickTemp>vis_fir_posi-1&&clickTemp<vis_la_posi+1){
            setBG(vis_fir_posi,false);
        }
        clickTemp = posiTion;
        if (posiTion!=-1){
            setBG(vis_fir_posi,true);
        }
    }

    private void setBG(int start,boolean isSelect){

            View view=null;
            try {
                view=gridView.getChildAt(clickTemp-start);
            }catch (Exception e){
                e.printStackTrace();
            }
            if (view!=null){
                if (isSelect){
                    view.setAlpha(0.5f);
                }else {
                    view.setAlpha(1f);
                }
            }

    }

}
