package com.example.settingproblemssystem.baseAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.settingproblemssystem.bean.SettingProbBean;
import com.example.settingproblemssystem.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/20 18:20.
 * Function :
 */

public class SettingProbAdapter extends RecyclerView.Adapter<SettingProbAdapter.ProbViewHolder> {

    private Context mContext;
    private List<SettingProbBean> mDatas=new ArrayList<>();
    private OnItemClickListener listener;

    public SettingProbAdapter(Context context){
        this.mContext=context;
    }

    public void setDatas(List<SettingProbBean> list){
        if (list!=null){
            this.mDatas.addAll(list);
            this.notifyDataSetChanged();
        }
    }

    public List<SettingProbBean> getDatas(){
        return mDatas;
    }

    @Override
    public ProbViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.adapter_settingprob,parent,false);
        return new ProbViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProbViewHolder holder, int position) {
        if (mDatas!=null){
            if (position==mDatas.size()){
                holder.tv_question.setVisibility(View.GONE);
                holder.tv_add_question.setVisibility(View.VISIBLE);
            }else {
                SettingProbBean bean=mDatas.get(position);
                holder.bean=bean;
                holder.tv_question.setText(bean.getQname());
                holder.tv_add_question.setVisibility(View.GONE);
                holder.tv_question.setVisibility(View.VISIBLE);
            }
        }else {
            holder.tv_question.setVisibility(View.GONE);
            holder.tv_add_question.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas!=null?mDatas.size()+1:1;
    }

    public interface OnItemClickListener{
        void onItemClick(SettingProbBean bean,int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }

    class ProbViewHolder extends RecyclerView.ViewHolder{

        SettingProbBean bean;
        final TextView tv_add_question;
        final TextView tv_question;

        ProbViewHolder(View itemView) {
            super(itemView);
            tv_question= (TextView) itemView.findViewById(R.id.tv_question);
            tv_add_question= (TextView) itemView.findViewById(R.id.tv_add_question);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null){
                        listener.onItemClick(bean,getPosition());
                    }
                }
            });
        }
    }
}
