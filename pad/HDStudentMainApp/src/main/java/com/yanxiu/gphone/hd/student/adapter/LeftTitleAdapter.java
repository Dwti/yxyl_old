package com.yanxiu.gphone.hd.student.adapter;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.bean.TitleBean;
import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/20.
 */
public class LeftTitleAdapter extends YXiuCustomerBaseAdapter {

    private final int LAYOUT_NUMS=2;
    private String[] mTitleArray;
    private final int TITLE_ITEM_LAYOUT=0;
    private final int SUB_TITLE_ITEM_LAYOUT=1;
    private int[] iconArrays=new int[]{
            R.drawable.left_ranking_icon,
            //R.drawable.left_save_icon,
            R.drawable.left_error_icon,
            R.drawable.left_exise_icon,
            R.drawable.left_set_icon
    };
    private int mCurPos=-1;//当前选中的索引值
    private List<TitleBean> titleList;

    public LeftTitleAdapter(Activity context) {
        super(context);
        mTitleArray =mContext.getResources().getStringArray(R.array.main_title_tab);
        initTitleBeanList(mTitleArray);
    }
    private void initTitleBeanList(String[] mTitleArray) {
        titleList=new ArrayList<>();
        List<YanXiuConstant.TITLE_ENUM> enumList=new ArrayList<>();
        enumList.add(YanXiuConstant.TITLE_ENUM.RANKING_ENUM);
        //enumList.add(YanXiuConstant.TITLE_ENUM.COLLECT_ENUM);
        enumList.add(YanXiuConstant.TITLE_ENUM.ERROR_ENUM);
        enumList.add(YanXiuConstant.TITLE_ENUM.HIS_ENUM);
        enumList.add(YanXiuConstant.TITLE_ENUM.SET_ENUM);
        int length=mTitleArray.length;
        for(int i=0;i<length;i++){
            TitleBean titleBean=new TitleBean();
            titleBean.setName(mTitleArray[i]);
            titleBean.setTitle_enum(enumList.get(i));
            titleList.add(titleBean);
        }
        setList(titleList);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getConvertView(position, convertView);
    }

    private View getConvertView(int position,View convertView) {
        int layoutType=getItemViewType(position);
        switch (layoutType){
            case TITLE_ITEM_LAYOUT:
                convertView=getTitleItemLyView(position, convertView);
                break;
            case SUB_TITLE_ITEM_LAYOUT:
                convertView=getSubTitleItemLyView(position,convertView);
                break;
        }

        return convertView;
    }

    public void setSelecteFlag(int position){
        this.mCurPos=position;
        notifyDataSetChanged();
    }

    public TitleBean getTitleBean(int i){
        if(titleList.isEmpty()){
            return  null;
        }
        if(i<0||i>titleList.size()-1){
            return null;
        }
        return titleList.get(i);
    }


    private View getSubTitleItemLyView(int position, View convertView) {
        SubTitleViewHolder subTitleViewHolder=null;
        if(convertView==null){
            subTitleViewHolder=new SubTitleViewHolder();
            convertView=View.inflate(mContext,R.layout.sub_title_item_layout,null);
            subTitleViewHolder.subTitleText=(TextView)convertView.findViewById(R.id.title);
            subTitleViewHolder.icon=(ImageView)convertView.findViewById(R.id.icon);
            convertView.setTag(subTitleViewHolder);
        }else{
            subTitleViewHolder= (SubTitleViewHolder) convertView.getTag();
        }
        TitleBean titleBean= (TitleBean) getItem(position);
        subTitleViewHolder.subTitleText.setText(titleBean.getName());

        if(mCurPos==position){
            subTitleViewHolder.subTitleText.setTextColor(mContext.getResources().getColor(R.color.color_ffdb4d));
        }else{
            subTitleViewHolder.subTitleText.setTextColor(mContext.getResources().getColor(R.color.color_00e6e6));
        }
        subTitleViewHolder.icon.setImageResource(iconArrays[position]);
        return convertView;
    }

    private View getTitleItemLyView(int position, View convertView) {
        TitleViewHolder titleViewHolder;
        if(convertView==null){
            titleViewHolder=new TitleViewHolder();
            convertView=View.inflate(mContext,R.layout.title_item_layout,null);
            titleViewHolder.titleText=(TextView)convertView.findViewById(R.id.title);
            titleViewHolder.icon=(ImageView)convertView.findViewById(R.id.icon);
            convertView.setTag(titleViewHolder);
        }else{
            titleViewHolder= (TitleViewHolder) convertView.getTag();
        }
        TitleBean titleBean= (TitleBean) getItem(position);
        titleViewHolder.titleText.setText(titleBean.getName());
        if(mCurPos==position){
            titleViewHolder.titleText.setTextColor(mContext.getResources().getColor(R.color.color_ffdb4d));
        }else{
            titleViewHolder.titleText.setTextColor(mContext.getResources().getColor(R.color.color_00e6e6));
        }
        titleViewHolder.icon.setImageResource(iconArrays[position]);
        return convertView;
    }


    class TitleViewHolder{
        private TextView titleText;
        private ImageView icon;
    }

    class SubTitleViewHolder{
        private TextView subTitleText;
        private ImageView icon;
    }

    @Override
    public int getViewTypeCount() {
        return LAYOUT_NUMS;
    }

    @Override
    public int getItemViewType(int position) {
        if(getList().size()<=0){
            return -1;
        }
        if(position<2||position==getList().size()-1){
            return TITLE_ITEM_LAYOUT;
        }else{
            return SUB_TITLE_ITEM_LAYOUT;
        }
    }


}
