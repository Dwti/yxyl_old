package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.ExHistoryEventBus;
import com.yanxiu.gphone.student.fragment.AbsHistoryFragment;
import com.yanxiu.gphone.student.fragment.ChapterHistoryFragment;
import com.yanxiu.gphone.student.fragment.TestCenterHistoryFragment;
import com.yanxiu.gphone.student.view.ChapterTabTitleLayout;
import com.yanxiu.gphone.student.view.TitleTabLayout;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/7/16.
 */
public class PracticeHistoryActivity extends YanxiuBaseActivity{
    private TitleTabLayout titleTabLayout;
    private ChapterTabTitleLayout chapterTabTitleLayout;
    private View rightView;
    private TextView rightTextView;
    private ImageView rightImageView;
    private int currItem = -1;


    private String title;
    private String subjectId;
    private String editionId;
    private boolean hasKnp;

    public FragmentManager fragmentManager;
    public ChapterHistoryFragment chapterHistoryFragment;
    public TestCenterHistoryFragment testCenterHistoryFragment;

    public static void launch(Activity activity, String title, String subjectId, String editionId,int hasKnp) {
        Intent intent = new Intent(activity, PracticeHistoryActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("subjectId", subjectId);
        intent.putExtra("editionId", editionId);
        intent.putExtra("hasKnp", hasKnp);
        activity.startActivity(intent);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_layout);
        title = getIntent().getStringExtra("title");
        subjectId = getIntent().getStringExtra("subjectId");
        editionId = getIntent().getStringExtra("editionId");
        hasKnp = getIntent().getIntExtra("hasKnp",0) == 1 ? true : false;
        EventBus.getDefault().register(this);
        findView();
        initData();
    }
    private void findView(){
        chapterTabTitleLayout = (ChapterTabTitleLayout) findViewById(R.id.rl_top_title);
        rightView = findViewById(R.id.rl_right);
        rightTextView = (TextView)findViewById(R.id.intell_volume_txt);
        rightImageView = (ImageView)findViewById(R.id.iv_right);
        titleTabLayout = (TitleTabLayout) findViewById(R.id.view_tab);

        chapterTabTitleLayout.setLeftTitle("");
        chapterTabTitleLayout.setCenterTitle(title);
        if(hasKnp){
            titleTabLayout.setVisibility(View.VISIBLE);
        }
//        if(hasKnp){
//            chapterTabTitleLayout.setLeftTitle(title);
//            chapterTabTitleLayout.setCenterTitle("");
//            titleTabLayout.setVisibility(View.VISIBLE);
//        }else{
//            chapterTabTitleLayout.setCenterTitle(title);
//            chapterTabTitleLayout.setLeftTitle("");
//            titleTabLayout.setVisibility(View.GONE);
//        }
        titleTabLayout.setOnTitleTabClick(new TitleTabLayout.OnTitleTabClick() {
            @Override
            public void onLeftClick () {
                LogInfo.log("geny", "onLeftClick =====  ");
                hideAndShowFragment(AbsHistoryFragment.CHAPTER_HISTORY);
            }

            @Override
            public void onRightClick () {
                LogInfo.log("geny", "onRightClick =====  ");
                hideAndShowFragment(AbsHistoryFragment.TEST_CENTER_HISTORY);
            }
        });

    }

    private void initData(){
        fragmentManager = getSupportFragmentManager();
        title = this.getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            chapterTabTitleLayout.setLeftTitle("");
            chapterTabTitleLayout.setCenterTitle(title);
        }
        hideAndShowFragment(AbsHistoryFragment.CHAPTER_HISTORY);
    }

    public void hideAndShowFragment(int index){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(currItem == index){
            return;
        }
        currItem = index;
        if (chapterHistoryFragment != null) {
            transaction.hide(chapterHistoryFragment);
        }
        if (testCenterHistoryFragment != null) {
            transaction.hide(testCenterHistoryFragment);
        }
        switch (currItem) {
            case AbsHistoryFragment.CHAPTER_HISTORY:
                if (chapterHistoryFragment == null) {
                    chapterHistoryFragment = new ChapterHistoryFragment();
                    chapterHistoryFragment.setArguments(getArguments());
                    chapterHistoryFragment.setRightView(rightView,rightTextView,rightImageView );
                    transaction.add(R.id.view_content, chapterHistoryFragment);
                } else {
                    transaction.show(chapterHistoryFragment);
                    chapterHistoryFragment.setRightViewVisible(true);
                }
                break;
            case AbsHistoryFragment.TEST_CENTER_HISTORY:
                if (testCenterHistoryFragment == null) {
                    testCenterHistoryFragment = new TestCenterHistoryFragment();
                    testCenterHistoryFragment.setArguments(getArguments());
                    testCenterHistoryFragment.setRightView(rightView, rightTextView, rightImageView );
                    transaction.add(R.id.view_content, testCenterHistoryFragment);
                } else {
                    transaction.show(testCenterHistoryFragment);
                    testCenterHistoryFragment.setRightViewVisible(false);
                }
                break;
        }
        transaction.commit();
    }

    private Bundle getArguments(){
        Bundle args = new Bundle();
        args.putSerializable("title", title);
        args.putString("subjectId", subjectId);
        args.putString("editionId", editionId);
        return args;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    public void onEventMainThread(ExHistoryEventBus exHistoryEventBus){
        switch (currItem) {
            case AbsHistoryFragment.CHAPTER_HISTORY:
                if (chapterHistoryFragment != null) {
                    chapterHistoryFragment.refreshData();
                }
                break;
            case AbsHistoryFragment.TEST_CENTER_HISTORY:
                if (testCenterHistoryFragment != null) {
                    testCenterHistoryFragment.refreshData();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogInfo.log("king","PracticeHistoryActivity resultCode = " + resultCode + " ,requestCode = " + requestCode);
        if(requestCode == AnswerViewActivity.LAUNCHER_FROM_GROUP){
            if(chapterHistoryFragment!=null){
                chapterHistoryFragment.refreshData();
            }
            if(testCenterHistoryFragment!=null){
                testCenterHistoryFragment.refreshData();
            }
        }
    }
}
