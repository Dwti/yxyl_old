package com.yanxiu.gphone.studentold.fragment.question;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.activity.MistakeRedoActivity;
import com.yanxiu.gphone.studentold.adapter.MistakeRedoCardAdapter;
import com.yanxiu.gphone.studentold.bean.MistakeRedoCardBean;
import com.yanxiu.gphone.studentold.utils.PublicLoadUtils;
import com.yanxiu.gphone.studentold.view.PublicLoadLayout;

/**
 * Created by Canghaixiao.
 * Time : 2017/2/16 15:57.
 * Function :
 */

public class MistakeRedoCardFragment extends Fragment implements View.OnClickListener, MistakeRedoCardAdapter.onRecyItemClickListener {

    private ImageView pub_top_left;
    private TextView pub_top_right;
    private RecyclerView recy_mistakecard;
    private MistakeRedoCardAdapter adapter;
    private MistakeRedoCardBean bean;
    private PublicLoadLayout mRootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bean= getArguments()!=null? (MistakeRedoCardBean) getArguments().getSerializable("MistakeRedoCardBean") :null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view=inflater.inflate(R.layout.activity_mistakeredocard,container,false);
//        view.setOnClickListener(this);
        mRootView = PublicLoadUtils.createPage(getActivity(), R.layout.activity_mistakeredocard);
        mRootView.setOnClickListener(this);
        initView(mRootView);
        initData();
        listener();
        return mRootView;
    }

    private void initView(View view) {
        pub_top_left= (ImageView) view.findViewById(R.id.pub_top_left);
        pub_top_left.setImageResource(R.drawable.answer_exam_delete);
        pub_top_right= (TextView) view.findViewById(R.id.pub_top_right);
        recy_mistakecard= (RecyclerView) view.findViewById(R.id.recy_mistakecard);
    }

    private void listener() {
        pub_top_left.setOnClickListener(this);
        adapter.setListener(this);
    }

    private void initData() {
        pub_top_right.setText(R.string.mistakeredo_card);
        recy_mistakecard.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=new MistakeRedoCardAdapter(getActivity());
        recy_mistakecard.setAdapter(adapter);
        adapter.setData(bean);
    }

    public void onRefresh(){
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pub_top_left:
                if (getActivity()!=null) {
                    ((MistakeRedoActivity) getActivity()).removeFragment();
                }
                break;
        }
    }

    public void onLoadFinish(){
        if (mRootView!=null){
            mRootView.finish();
        }
    }

    @Override
    public void onClick(int position, int position_child, int index) {
        if (getActivity()!=null) {
            mRootView.loading(true);
            ((MistakeRedoActivity) getActivity()).setViewPagerCurrent(index);
        }
    }
}
