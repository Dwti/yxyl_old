package com.yanxiu.gphone.student.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.view.picsel.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/2/17 17:45.
 * Function :
 */

public class MistakeRedoCardAdapter extends RecyclerView.Adapter<MistakeRedoCardAdapter.MyViewHolder> {

    private Context mContext;
    private final LayoutInflater inflater;
    private onRecyItemClickListener listener;
    private List<String> mData=new ArrayList<>();

    public MistakeRedoCardAdapter(Context context){
        this.mContext=context;
        inflater=LayoutInflater.from(context);
    }

    public void setData(List<String> list){
        this.mData.addAll(list);
        this.notifyDataSetChanged();
    }

    public interface onRecyItemClickListener{
       void onClick(int position,int position_child,String s);
    }

    public void setListener(onRecyItemClickListener listener){
        this.listener=listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_mistakeredocard_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_time.setText("2016.99");
        holder.position_list=position;
        holder.grid_mistake_card.setAdapter(holder.adapter);
        holder.adapter.setData(getList(position));
    }

    private List<String> getList(int num){
        List<String> list=new ArrayList<>();
        for (int i=0;i<50;i++){
            list.add(i+1+"");
        }
        return list;
    }

    @Override
    public int getItemCount() {
        return this.mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        final TextView tv_time;
        final NoScrollGridView grid_mistake_card;
        final MistakeRedoCardGridAdapter adapter;
        int position_list;

        MyViewHolder(View itemView) {
            super(itemView);
//            position_list=getPosition();
            tv_time= (TextView) itemView.findViewById(R.id.tv_time);
            grid_mistake_card= (NoScrollGridView) itemView.findViewById(R.id.grid_mistake_card);
            adapter=new MistakeRedoCardGridAdapter(mContext);
//            grid_mistake_card.setAdapter(adapter);
            grid_mistake_card.setFocusable(false);
            grid_mistake_card.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String s=adapter.getData().get(position);
                    if (listener!=null) {
                        listener.onClick(position_list, position, s);
                    }
                }
            });
        }
    }
}
