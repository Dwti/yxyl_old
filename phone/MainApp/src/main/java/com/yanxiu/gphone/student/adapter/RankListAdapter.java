package com.yanxiu.gphone.student.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.RankItemBean;
import com.common.core.utils.StringUtils;
import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.imageloader.RotateImageViewAware;
import com.common.core.utils.imageloader.UniversalImageLoadTool;
import com.yanxiu.gphone.student.view.StrokeTextView;
import com.common.core.view.roundview.RoundedImageView;

/**
 * Created by Administrator on 2015/9/24.
 */
public class RankListAdapter extends YXiuCustomerBaseAdapter<RankItemBean> {


    private final int DEFAULT_NUM=0;
    public RankListAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(mContext, R.layout.ranking_adapter_layout, null);
            AbsListView.LayoutParams contentViewParams= new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            contentViewParams.height= CommonCoreUtil.dipToPx(mContext, 104);
            convertView.setLayoutParams(contentViewParams);
            holder.image=(RoundedImageView)convertView.findViewById(R.id.headImage);
            holder.decorateImg=(ImageView)convertView.findViewById(R.id.imageDecorate);
            holder.image.setCornerRadius(mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_12));
            holder.title=(TextView)convertView.findViewById(R.id.title);
            holder.content=(TextView)convertView.findViewById(R.id.content);

            holder.answerContentText=(TextView)convertView.findViewById(R.id.answer_content_text);

            holder.correctContentText=(TextView)convertView.findViewById(R.id.correct_content_text);
            holder.leftRankIcon=(StrokeTextView)convertView.findViewById(R.id.rankLeftIcon);

            holder.rightRankIcon=(ImageView)convertView.findViewById(R.id.rankRightIcon);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        RankItemBean rankItemBean=getItem(position);
        setDrawable(holder.rightRankIcon, holder.decorateImg, holder.leftRankIcon, position);
        if(!StringUtils.isEmpty(rankItemBean.getNickName())){
            holder.title.setText(rankItemBean.getNickName());
        }else{
            holder.title.setText(mContext.getResources().getString(R.string.no_data_show));
        }
        if(!StringUtils.isEmpty(rankItemBean.getSchoolName())){
            holder.content.setText(rankItemBean.getSchoolName());
        }else{
            holder.content.setText(mContext.getResources().getString(R.string.no_data_show));
        }

        UniversalImageLoadTool.disPlay(rankItemBean.getHeadImg(),
                new RotateImageViewAware(holder.image, rankItemBean.getHeadImg()), R.drawable.user_info_default_bg);

        if(!StringUtils.isEmpty(rankItemBean.getAnswerquenum())){
            holder.answerContentText.setText(rankItemBean.getAnswerquenum());
        }else{
            holder.answerContentText.setText(DEFAULT_NUM+"");
        }
        if(!StringUtils.isEmpty(rankItemBean.getCorrectrate())){
            int correctRate=Math.round(Float.valueOf(rankItemBean.getCorrectrate())*100);
            holder.correctContentText.setText(correctRate+"%");
        }else{
            holder.correctContentText.setText(DEFAULT_NUM+"%");
        }

        return convertView;
    }

    /**
     * 获取RankNum颜色值
     * @param pos
     * @return
     */
    void setDrawable(ImageView rightRankIcon,ImageView decorateImg,TextView leftRankIcon,int pos){
        switch (pos){
            case 0:
                rightRankIcon.setBackgroundResource(R.drawable.champion);
                setRankDrawble(rightRankIcon,leftRankIcon, decorateImg,pos);
                break;
            case 1:

                rightRankIcon.setBackgroundResource(R.drawable.runner_up);
                setRankDrawble(rightRankIcon,leftRankIcon,decorateImg,pos);
                break;
            case 2:

                rightRankIcon.setBackgroundResource(R.drawable.second_runner_up);
                setRankDrawble(rightRankIcon,leftRankIcon, decorateImg,pos);
                break;
            default:
                setNotRankDrawble(rightRankIcon,leftRankIcon, decorateImg,pos);
                break;
        }

    }

    private void setRankDrawble(ImageView rightRankIcon,TextView leftRankIcon,ImageView decorateImg,int pos){
        rightRankIcon.setVisibility(View.VISIBLE);
        decorateImg.setBackgroundResource(R.drawable.user_group_detail_bg);
        leftRankIcon.setBackgroundResource(R.drawable.blue_star);
        leftRankIcon.setText(pos+1+"");
    }

    private void setNotRankDrawble(ImageView rightRankIcon,TextView leftRankIcon,ImageView decorateImg,int pos){
        rightRankIcon.setVisibility(View.GONE);
        decorateImg.setBackgroundResource(R.drawable.pic_not_sel);
        leftRankIcon.setBackgroundResource(R.drawable.yellow_star);
        leftRankIcon.setText(pos+1+"");
    }

    class ViewHolder{
        private RoundedImageView image;
        private ImageView decorateImg;
        private TextView title;
        private TextView content;
        private TextView answerContentText;
        private TextView correctContentText;
        private ImageView rightRankIcon;
        private StrokeTextView leftRankIcon;
    }

}
