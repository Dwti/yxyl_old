package com.yanxiu.gphone.student.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.MistakeRedoCardAdapter;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.view.MyBoldTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/2/16 15:57.
 * Function :
 */

public class MistakeRedoCardActivity extends YanxiuBaseActivity implements View.OnClickListener, MistakeRedoCardAdapter.onRecyItemClickListener {

    private TextView pub_top_left;
    private TextView pub_top_right;
    private MyBoldTextView pub_top_mid;
    private RecyclerView recy_mistakecard;
    private MistakeRedoCardAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mistakeredocard);
        initView();
        initData();
        listener();
    }

    private void initView() {
        pub_top_left= (TextView) findViewById(R.id.pub_top_left);
        pub_top_right= (TextView) findViewById(R.id.pub_top_right);
        pub_top_mid= (MyBoldTextView) findViewById(R.id.pub_top_mid);
        recy_mistakecard= (RecyclerView) findViewById(R.id.recy_mistakecard);
    }

    private void listener() {
        pub_top_left.setOnClickListener(this);
        adapter.setListener(this);
    }

    private void initData() {
        pub_top_right.setVisibility(View.INVISIBLE);
        pub_top_mid.setText(R.string.mistakeredo_card);
        recy_mistakecard.setLayoutManager(new LinearLayoutManager(this));
        adapter=new MistakeRedoCardAdapter(this);
        recy_mistakecard.setAdapter(adapter);
        adapter.setData(getList());
    }

    private List<String> getList(){
        List<String> list=new ArrayList<>();
        for (int i=0;i<15;i++){
            list.add(i+"");
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pub_top_left:
                this.finish();
                break;

        }
    }

    @Override
    public void onClick(int position, int position_child, String s) {
        Toast.makeText(this,position+""+position_child+""+s,Toast.LENGTH_SHORT).show();
    }
}
