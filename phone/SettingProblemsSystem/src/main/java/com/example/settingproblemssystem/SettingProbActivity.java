package com.example.settingproblemssystem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
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
        recy_prob_title= (RecyclerView) findViewById(R.id.recy_prob_title);
    }

    @Override
    protected void initData() {
        recy_prob_title.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initListener() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

//    Intent intent=new Intent("android.intent.action.SYSTEM");
//            intent.addCategory("SYSTEM");
//    startActivity(intent);

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
