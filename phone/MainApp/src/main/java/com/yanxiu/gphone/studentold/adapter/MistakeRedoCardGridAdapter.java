package com.yanxiu.gphone.studentold.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.bean.MistakeRedoCardBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/2/20 10:26.
 * Function :
 */

public class MistakeRedoCardGridAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private List<Integer> mData=new ArrayList<>();
    private List<Integer> types=new ArrayList<>();

    public MistakeRedoCardGridAdapter(Context context){
        inflater=LayoutInflater.from(context);
    }

    public void setData(List<Integer> list,List<Integer> types){
        this.mData.clear();
        this.mData.addAll(list);
        this.types.clear();
        this.types.addAll(types);
        this.notifyDataSetChanged();
    }

    public List<Integer> getData(){
        return this.mData;
    }

    @Override
    public int getCount() {
        return this.mData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_mistakeredocard_grid,parent,false);
            holder.tv_number= (TextView) convertView.findViewById(R.id.tv_number);
            holder.answer_card_icon= (ImageView) convertView.findViewById(R.id.answer_card_icon);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        String s=mData.get(position)+"";
        holder.tv_number.setText(s);
        int type=types.get(position);
        if (type== MistakeRedoCardBean.TYPE_HASANSWER) {
            holder.answer_card_icon.setBackgroundResource(R.drawable.answer_card_done);
        }else if (type== MistakeRedoCardBean.TYPE_NOANSWER) {
            holder.answer_card_icon.setBackgroundResource(R.drawable.answer_card_undone);
        }
        return convertView;
    }

    class ViewHolder{
        TextView tv_number;
        ImageView answer_card_icon;
    }
}
