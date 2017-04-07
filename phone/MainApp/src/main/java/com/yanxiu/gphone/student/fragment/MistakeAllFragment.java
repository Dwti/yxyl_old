package com.yanxiu.gphone.student.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.MistakeDetailsActivity;
import com.yanxiu.gphone.student.adapter.MistakeAllFragmentAdapter;
import com.yanxiu.gphone.student.bean.MistakeAllFragBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/31 17:00.
 * Function :
 */

public class MistakeAllFragment extends Fragment implements MistakeAllFragmentAdapter.OnItemClickListener {

    private Context mContext;
    private MistakeAllFragmentAdapter adapter;

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
        RecyclerView recyProSelect= (RecyclerView) view.findViewById(R.id.recy_cpter_koledge);
        recyProSelect.setLayoutManager(new LinearLayoutManager(mContext));
        adapter=new MistakeAllFragmentAdapter(mContext);
        recyProSelect.setAdapter(adapter);
    }

    private List<MistakeAllFragBean> getList(){
        List<MistakeAllFragBean> list=new ArrayList<>();

        for (int i=0;i<3;i++){
            MistakeAllFragBean bean=new MistakeAllFragBean();
            bean.setId((i+1));
            bean.setHaveChildren(true);
            bean.setHierarchy(0);
            bean.setName("i"+i);
            ArrayList<MistakeAllFragBean> list_i=new ArrayList<>();
            for (int j=0;j<3;j++){
                MistakeAllFragBean bean_j=new MistakeAllFragBean();
                bean_j.setId((i+1)*10+(j+1));
                bean_j.setHaveChildren(true);
                bean_j.setName("j"+j);
                bean_j.setHierarchy(1);
                ArrayList<MistakeAllFragBean> list_j=new ArrayList<>();
                for (int k=0;k<3;k++){
                    MistakeAllFragBean bean_k=new MistakeAllFragBean();
                    bean_k.setId((i+1)*100+(j+1)*10+(k+1));
                    bean_k.setHaveChildren(true);
                    bean_k.setHierarchy(2);
                    bean_k.setName("k"+k);
                    ArrayList<MistakeAllFragBean> list_k=new ArrayList<>();
                    for (int m=0;m<3;m++){
                        MistakeAllFragBean bean_m=new MistakeAllFragBean();
                        bean_m.setId((i+1)*1000+(j+1)*100+(k+1)*10+(m+1));
                        bean_m.setHaveChildren(false);
                        bean_m.setHierarchy(3);
                        bean_m.setName("m"+m);
                        list_k.add(bean_m);
                    }
                    bean_k.setChildren(list_k);
                    list_j.add(bean_k);
                }
                bean_j.setChildren(list_j);
                list_i.add(bean_j);
            }
            bean.setChildren(list_i);
            list.add(bean);
        }

        MistakeAllFragBean bean=new MistakeAllFragBean();
        bean.setId(4);
        bean.setHaveChildren(false);
        bean.setHierarchy(0);
        bean.setName("i"+4);
        list.add(bean);
        return list;
    }

    private void initData() {

    }

    private void initListener() {
        adapter.setOnItemClickListener(this);
    }

    public void setData(int index){
        adapter.setData(getList());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void itemClickListener(MistakeAllFragBean bean) {
        Toast.makeText(mContext,bean.getName(),Toast.LENGTH_SHORT).show();
        MistakeDetailsActivity.launch(getActivity(),bean.getName(),"subjectId",bean.getQids());
    }
}
