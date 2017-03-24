package com.example.settingproblemssystem.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.settingproblemssystem.R;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/21 15:56.
 * Function :
 */

public class QuestionListActivity extends BaseActivity {

    private Toolbar tool_que_title;
    private RecyclerView recy_que_list;

    public static void lunch(Activity activity){
        Intent intent=new Intent(activity,QuestionListActivity.class);
        activity.startActivityForResult(intent,RESULT_OK);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_questionlist;
    }

    @Override
    protected void initView() {
        tool_que_title= (Toolbar) findViewById(R.id.tool_que_title);
        recy_que_list= (RecyclerView) findViewById(R.id.recy_que_list);
        recy_que_list.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }
}
