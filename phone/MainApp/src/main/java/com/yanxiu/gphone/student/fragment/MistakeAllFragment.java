package com.yanxiu.gphone.student.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.yanxiu.gphone.student.R;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/31 17:00.
 * Function :
 */

public class MistakeAllFragment extends Fragment {

    private Context mContext;
    private RelativeLayout rlTestBgView;
    private RecyclerView recyProSelect;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext=inflater.getContext();
        View view=inflater.inflate(R.layout.fragment_mistakeall,container,false);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initView(View view) {
        rlTestBgView= (RelativeLayout) view.findViewById(R.id.rl_fragment);
        recyProSelect= (RecyclerView) view.findViewById(R.id.recy_cpter_koledge);
        recyProSelect.setLayoutManager(new LinearLayoutManager(mContext));
    }

    private void initData() {

    }

    private void initListener() {

    }

    public void setData(int index){
        if (index==1){
            rlTestBgView.setBackgroundColor(Color.BLUE);
        }else {
            rlTestBgView.setBackgroundColor(Color.GREEN);
        }
    }
}
