package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.view.xlistview.XListView;
import com.common.login.LoginModel;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.WrongAllListAdapter;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.PaperTestEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.requestTask.RequestWrongAllQuestionTask;
import com.yanxiu.gphone.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.PublicLoadLayout;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Canghaixiao.
 * Time : 2017/4/6 15:31.
 * Function :
 */

public class MistakeDetailsActivity extends YanxiuBaseActivity {

    private PublicLoadLayout rootView;
    private int pageIndex = 1;
    private TextView backView;
    private String title;
    private XListView listView;
    private WrongAllListAdapter wrongAllListAdapter;
    private RequestWrongAllQuestionTask mRequestWrongAllQuestionTask;
    private SubjectExercisesItemBean subjectExercisesItemBeanIntent = new SubjectExercisesItemBean();
    private String stageId;
    private String subjectId;
    private ArrayList<Integer> qids;
    private int mMistakeCount;

    public static void launch(Activity activity, String title, String subjectId, ArrayList<Integer> qids) {
        Intent intent = new Intent(activity, MistakeDetailsActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("subjectId", subjectId);
//        intent.putIntegerArrayListExtra("qids",qids);
        activity.startActivityForResult(intent, 00);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stageId = LoginModel.getUserinfoEntity().getStageid() + "";
        title = getIntent().getStringExtra("title");
        subjectId = getIntent().getStringExtra("subjectId");
        qids = getIntent().getIntegerArrayListExtra("qids");

        rootView = PublicLoadUtils.createPage(this, R.layout.activity_mistake_all_layout);
        rootView.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override
            public void refreshData() {
                pageIndex = 1;
                requestMistakeAllList(false, true, false);
            }
        });
        setContentView(rootView);
        findView();
    }

    private void findView() {
        View top_space_view = findViewById(R.id.top_space_views);
        top_space_view.setVisibility(View.VISIBLE);
        backView = (TextView) findViewById(R.id.pub_top_left);
        TextView titleView = (TextView) findViewById(R.id.pub_top_mid);
        titleView.setText(title);

        LinearLayout llBelowTitle = (LinearLayout) findViewById(R.id.ll_below_title);
        llBelowTitle.setVisibility(View.GONE);

        listView = (XListView) findViewById(R.id.mistack_all_list);
        wrongAllListAdapter = new WrongAllListAdapter(this);
        listView.setAdapter(wrongAllListAdapter);
        listView.setScrollable(false);
        listView.setPullLoadEnable(false);
        listView.setXListViewListener(ixListViewListener);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (wrongAllListAdapter != null) {
                    List<PaperTestEntity> list = wrongAllListAdapter.getList();
                    if (subjectExercisesItemBeanIntent.getData() != null && subjectExercisesItemBeanIntent.getData().size() > 0) {
                        subjectExercisesItemBeanIntent.getData().get(0).setName("");
                    }
                    if (list != null && list.size() > 0 && position > 0 && position - 1 < list.size()) {
                        WrongAnswerViewActivity.launch(MistakeDetailsActivity.this, subjectExercisesItemBeanIntent, subjectId, pageIndex, list.get(position - 1).getQuestions().getChildPageIndex(), YanXiuConstant.WRONG_REPORT, String.valueOf(mMistakeCount), position - 1);
                    } else {
                        WrongAnswerViewActivity.launch(MistakeDetailsActivity.this, subjectExercisesItemBeanIntent, subjectId, pageIndex, 0, YanXiuConstant.WRONG_REPORT, String.valueOf(mMistakeCount), position);
                    }
                }
            }
        });

        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTaskAndFinish();
            }
        });

        RelativeLayout linear_number = (RelativeLayout) findViewById(R.id.linear_number);
        linear_number.setVisibility(View.GONE);
    }

    private XListView.IXListViewListener ixListViewListener = new XListView.IXListViewListener() {

        @Override
        public void onRefresh(XListView view) {

        }

        @Override
        public void onLoadMore(XListView view) {

        }
    };

    private void cancelTaskAndFinish() {
        cancelTask();
//        forResult();
        MistakeDetailsActivity.this.finish();
    }

    private void cancelTask() {
        if (mRequestWrongAllQuestionTask != null) {
            mRequestWrongAllQuestionTask.cancel();
            mRequestWrongAllQuestionTask = null;
        }
    }

    private void requestMistakeAllList(final boolean isRefresh, final boolean showLoading, final boolean isLoaderMore) {

    }
}
