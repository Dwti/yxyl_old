package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.common.core.utils.LogInfo;
import com.common.login.LoginModel;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.MistakeRedoAdapter;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.PaperTestEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.fragment.question.BaseQuestionFragment;
import com.yanxiu.gphone.student.fragment.question.SubjectiveQuestionFragment;
import com.yanxiu.gphone.student.utils.QuestionUtils;
import com.yanxiu.gphone.student.utils.YanXiuConstant;

import java.util.List;

/**
 * Created by Canghaixiao.
 * Time : 2017/2/10 11:59.
 * Function :
 */

public class MistakeRedoActivity extends BaseAnswerViewActivity {

    private MistakeRedoAdapter mistakeRedoAdapter;
    private int pageIndex = 0;
    private String stageId;
    private String subjectId;
    private String editionId;
    private String volumeId;
    private String chapterId;
    private String sectionId;
    private String questionId;
    private int isChapterSection = 0;
    private String uniteId;
    private int wrongCounts;
    private boolean isNetData = true;

    private int comeFrom = 0;
    private int position;


    public static void launch(Activity context, SubjectExercisesItemBean bean, String subjectId, int pagerIndex, int childIndex, int comeFrom, String wrongCount, int position) {
        Intent intent = new Intent(context, MistakeRedoActivity.class);
        intent.putExtra("subjectExercisesItemBean", bean);
        intent.putExtra("subjectId", subjectId);
        intent.putExtra("pagerIndex", pagerIndex);
        intent.putExtra("childIndex", childIndex);
        intent.putExtra("comeFrom", comeFrom);
        intent.putExtra("position", position);
        intent.putExtra("wrongCount", wrongCount);
        context.startActivityForResult(intent, YanXiuConstant.LAUNCHER_FROM_MISTAKE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stageId = LoginModel.getUserinfoEntity().getStageid() + "";
        subjectId = getIntent().getStringExtra("subjectId");
        editionId = getIntent().getStringExtra("editionId");
        volumeId = getIntent().getStringExtra("volumeId");
        chapterId = getIntent().getStringExtra("chapterId");
        sectionId = getIntent().getStringExtra("sectionId");
        uniteId = getIntent().getStringExtra("uniteId");
        isChapterSection = getIntent().getIntExtra("isChapterSection", 0);
        isNetData = getIntent().getBooleanExtra("isNetData", true);
        comeFrom = getIntent().getIntExtra("comeFrom", 0);
        position = getIntent().getIntExtra("position", 0);
        String wrongCount = getIntent().getStringExtra("wrongCount");
        try {
            wrongCounts = Integer.parseInt(wrongCount);
        } catch (Exception e) {

        }
        initView();
        initData();
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {

        if (getIntent() != null) {
            if (getIntent().getSerializableExtra("subjectExercisesItemBean") != null) {
                dataSources = (SubjectExercisesItemBean) getIntent().getSerializableExtra("subjectExercisesItemBean");
            }
        }

        if (dataSources == null) {
            return;
        }

        if (dataSources == null || dataSources.getData() == null || dataSources.getData().isEmpty()) {
            this.finish();
        } else {
            List<PaperTestEntity> dataList = dataSources.getData().get(0).getPaperTest();
            if (dataList != null && !dataList.isEmpty()) {
                QuestionUtils.addChildQuestionToParent(dataList);     //对题目的pageIndex childPageIndex,positionForCard,childPositionForCard进行赋值

                mistakeRedoAdapter = new MistakeRedoAdapter(getSupportFragmentManager());

                mistakeRedoAdapter.addDataSources(dataSources);
                vpAnswer.setAdapter(mistakeRedoAdapter);
                mistakeRedoAdapter.setViewPager(vpAnswer);
                pageCount = mistakeRedoAdapter.getCount();

                ivBack.setOnClickListener(this);
                ivAnswerCard.setOnClickListener(this);
                ivFavCard.setOnClickListener(this);
                if (!TextUtils.isEmpty(dataSources.getData().get(0).getName())) {
                    tvQuestionTitle.setText(dataSources.getData().get(0).getName());
                }
            }
        }
        setReportError();

        pageIndex = position;
        vpAnswer.setCurrentItem(position);
        tvPagerIndex.setText(String.valueOf(position + 1));
        //tvPagerCount.setText(" / " + String.format(this.getResources().getString(R.string.pager_count), String.valueOf(adapter.getTotalCount())));
        tvPagerCount.setText(" / " + String.format(this.getResources().getString(R.string.pager_count), (wrongCounts) + ""));
        tvToptext.setText(this.getResources().getString(R.string.questiong_resolution));
        tvToptext.setCompoundDrawables(null, null, null, null);
//            tvAnswerCard.setVisibility(View.GONE);
        ivAnswerCard.setBackgroundResource(R.drawable.selector_answer_delete);

    }

    @Override
    public boolean getCurrent(int fragment_ID) {
        return super.getCurrent(fragment_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == RESULT_OK) {
                BaseQuestionFragment currentFragment = (BaseQuestionFragment) mistakeRedoAdapter.getItem(currentIndex);
                if (dataSources.getData().get(0).getPaperTest().get(currentIndex).getQuestions().getChildren() != null && !dataSources.getData().get(0).getPaperTest().get(currentIndex).getQuestions().getChildren().isEmpty())
                    if (currentFragment != null && currentFragment.getChildFragment() != null) {
                        currentFragment = (BaseQuestionFragment) currentFragment.getChildFragment();
                    }
                if (currentFragment != null && currentFragment instanceof SubjectiveQuestionFragment) {
                    currentFragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        } catch (Exception e) {
        }
    }
}
