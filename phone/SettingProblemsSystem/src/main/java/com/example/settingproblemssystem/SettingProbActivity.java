package com.example.settingproblemssystem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/10 11:16.
 * Function :
 */

public class SettingProbActivity extends BaseActivity {

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
        switch (item.getItemId()){
//            case R.id.menu_problem:
//                break;
//            case R.id.menu_parsing:
//                break;
//            case R.id.menu_wrong_topic:
//                break;
//            case R.id.menu_redo:
//                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
