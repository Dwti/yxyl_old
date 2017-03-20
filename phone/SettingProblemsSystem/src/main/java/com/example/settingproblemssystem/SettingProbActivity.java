package com.example.settingproblemssystem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/10 11:16.
 * Function :
 */

public class SettingProbActivity extends BaseActivity {

    private static final String string_problem="做题";
    private static final String string_system_redo="出题";
    private static final String string_parsing="解析";
    private static final String string_wrong_topic="错题";
    private static final String string_redo="重做";
    private static final String string_pc="PC联动";

    private ImageView iv_back;
    private TextView problem;
    private TextView parsing;
    private TextView wrong_topic;
    private TextView redo;
    private RecyclerView recy_prob_title;


    public static void lunch(Context context){
        Intent intent=new Intent(context,SettingProbActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar=getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_settingprob;
    }

    @Override
    protected void initView() {
        iv_back= (ImageView) findViewById(R.id.iv_back);
        recy_prob_title= (RecyclerView) findViewById(R.id.recy_prob_title);
        problem= (TextView) findViewById(R.id.problem);
        parsing= (TextView) findViewById(R.id.parsing);
        wrong_topic= (TextView) findViewById(R.id.wrong_topic);
        redo= (TextView) findViewById(R.id.redo);
    }

    @Override
    protected void initData() {
        recy_prob_title.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initListener() {
//        iv_back.setOnClickListener(this);
    }

    public void OnClick(View view){
        if (view==iv_back){
            Intent intent=new Intent("android.intent.action.SYSTEM");
            intent.addCategory("SYSTEM");
            startActivity(intent);
        }else if (view==problem){

        }else if (view==parsing){

        }else if (view==wrong_topic){

        }else if (view==redo){

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle()!=null) {
            switch (item.getTitle().toString()) {
                case string_problem:
                    ToastUtils(string_problem);
                    break;
                case string_parsing:
                    ToastUtils(string_parsing);
                    break;
                case string_wrong_topic:
                    ToastUtils(string_wrong_topic);
                    break;
                case string_redo:
                    ToastUtils(string_redo);
                    break;
                case string_pc:
                    ToastUtils(string_pc);
                    break;
            }
        }else {
            if (item.getItemId()==android.R.id.home){
                this.finish();
            }
        }
        return true;
    }

    private void ToastUtils(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
}
