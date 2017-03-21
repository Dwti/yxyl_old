package com.example.settingproblemssystem.BaseAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.settingproblemssystem.R;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/20 18:20.
 * Function :
 */

public class SettingProbAdapter extends RecyclerView.Adapter<SettingProbAdapter.ProbViewHolder> {

    private Context mContext;

    public SettingProbAdapter(Context context){
        this.mContext=context;
    }

    @Override
    public ProbViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.adapter_settingprob,parent,false);
        return new ProbViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProbViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ProbViewHolder extends RecyclerView.ViewHolder{

        ProbViewHolder(View itemView) {
            super(itemView);
        }
    }
}
