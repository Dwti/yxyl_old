package com.example.settingproblemssystem.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.settingproblemssystem.BaseAdapter.SettingProbAdapter;
import com.example.settingproblemssystem.Bean.SettingProbBean;
import com.example.settingproblemssystem.R;
import com.example.settingproblemssystem.Utils.QuestionType;
import com.example.settingproblemssystem.Utils.ToastUtils;

import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/3/10 11:16.
 * Function :
 */

public class SettingProbActivity extends BaseActivity implements SettingProbAdapter.OnItemClickListener {

    private static final String string_problem = "前往做题";
//    private static final String string_system_redo = "出题";
    private static final String string_parsing = "前往解析";
    private static final String string_wrong_topic = "前往错题";
    private static final String string_redo = "前往重做";
    private static final String string_pc = "前往PC";

    private static final String string_no = "功能开发中，敬请期待！";

    private RecyclerView recy_prob_title;
    private SettingProbAdapter adapter;

    public static void lunch(Context context) {
        Intent intent = new Intent(context, SettingProbActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        if (bar!=null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_settingprob;
    }

    @Override
    protected void initView() {
        recy_prob_title = (RecyclerView) findViewById(R.id.recy_prob_title);
    }

    @Override
    protected void initData() {
        recy_prob_title.setLayoutManager(new LinearLayoutManager(this));
        adapter=new SettingProbAdapter(this);
        adapter.setOnItemClickListener(this);
        recy_prob_title.setAdapter(adapter);
    }

    @Override
    protected void initListener() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void setIntent() {
        List<SettingProbBean> list= adapter.getDatas();
        if (list==null||list.size()==0){
            ToastUtils.show(this,R.string.have_no_question);
            return;
        }
        Intent intent = new Intent("android.intent.action.SYSTEM");
        intent.addCategory("SYSTEM");
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.setDatas(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle() != null) {
            switch (item.getTitle().toString()) {
                case string_problem:
                    setIntent();
                    break;
                case string_parsing:
                    setIntent();
                    break;
                case string_wrong_topic:
                    setIntent();
                    break;
                case string_redo:
                    setIntent();
                    break;
                case string_pc:
                    ToastUtils.show(this,string_no);
                    break;
            }
        } else {
            if (item.getItemId() == android.R.id.home) {
                this.finish();
            }
        }
        return true;
    }

    @Override
    public void onItemClick(SettingProbBean bean, int position) {
        if (bean==null){
            QuestionListActivity.lunch(this);
        }else {
            switch (bean.getQType()){
                case QuestionType.CLASSFY:
                case QuestionType.COMPUTE:
                case QuestionType.CONNECT:
                case QuestionType.FILL_BLANKS:
                case QuestionType.JUDGE:
                case QuestionType.MULTI:
                case QuestionType.SINGLE:
                case QuestionType.SUBJECTIVE:
                    //单题
                    WebViewActivity.lunch(this,bean);
                    break;
                case QuestionType.CLOZE:
                case QuestionType.LISTEN:
                case QuestionType.READ:
                case QuestionType.READING:
                case QuestionType.SOLVE:
                    //复合题
                    break;
            }
        }
    }
}
