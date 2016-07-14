package com.yanxiu.gphone.parent.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.common.core.utils.CommonCoreUtil;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.activity.base.TopViewBaseActivity;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;
import com.yanxiu.gphone.parent.requestTask.RequestParentUploadFeedBackTask;
import com.yanxiu.gphone.parent.utils.ParentUtils;
import com.yanxiu.gphone.parent.utils.PublicLoadUtils;

/**
 * Created by lidongming on 16/3/18.
 * 家长端-------意见反馈
 */
public class ParentFeedBackActivity extends TopViewBaseActivity {

//    private View mView;

    public final static int MAX_NUMBERS=500;

    private Handler mHandler=new Handler();
    private final static int DELAY_FINISH_TIME=1000;

//    private Button submitBtn;
    private EditText feedBackText;

    private RequestParentUploadFeedBackTask requestParentUploadFeedBackTask;


    public static void launch(Activity context) {
        Intent intent = new Intent(context, ParentFeedBackActivity.class);
//        intent.putExtra("subjectExercisesItemBean", bean);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initLaunchIntentData() {

    }

    @Override
    protected boolean isAttach() {
        return false;
    }

    @Override
    protected View getContentView() {
        mPublicLayout = PublicLoadUtils.createPage(this, R.layout.parent_feedback_layout);
        mPublicLayout.finish();
//        mView= LayoutInflater.from(this).inflate(R.layout.parent_feedback_layout,null);
        initView();
        initData();
        return mPublicLayout;
    }

    private void initData() {
        titleText.setText(this.getResources().getString(R.string.feedback_name));
        rightText.setText(this.getResources().getString(R.string.submmit));
    }

    @Override
    protected void setContentListener() {
        rightText.setOnClickListener(this);
    }

    @Override
    protected void destoryData() {

    }

    @Override
    protected void setRootView() {
        super.setRootView();
        rootView.setBackgroundResource(R.drawable.login_in_common_bg);
    }



    @Override
    protected void setTopView() {
        super.setTopView();
        topRootView.setBackgroundColor(getResources().getColor(android.R.color.white));
    }

    private void initView(){
        feedBackText = (EditText) mPublicLayout.findViewById(R.id.edit_parent_feed_back);
        feedBackText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_NUMBERS)});

    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view == rightText){
            CommonCoreUtil.hideSoftInput(view);
            if(checkDataParams(feedBackText.getText().toString().trim())){
                uploadFeedBack(feedBackText.getText().toString().trim());
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        ParentUtils.hideSoftInput(feedBackText);
    }

    private void uploadFeedBack(String content){
        mPublicLayout.loading(true);
        cancenTask();
        requestParentUploadFeedBackTask = new RequestParentUploadFeedBackTask(this, content, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                mPublicLayout.finish();
                ParentUtils.showToast(R.string.update_sucess);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, DELAY_FINISH_TIME);
            }

            @Override
            public void dataError(int type, String msg) {
                mPublicLayout.finish();
                ParentUtils.showToast(R.string.update_fail);
            }
        });
        requestParentUploadFeedBackTask.start();
    }

    private void cancenTask(){
        if(requestParentUploadFeedBackTask != null){
            requestParentUploadFeedBackTask.cancel();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancenTask();
        requestParentUploadFeedBackTask = null;
    }

    protected boolean checkDataParams(String content){
        if(TextUtils.isEmpty(content)){
            handlerMinTips();
            return false;
        }else if(content.length()>MAX_NUMBERS){
            handlerMaxTips();
            return false;
        }else {
            return true;
        }
    }

    private void handlerMaxTips() {
        ParentUtils.showToast(R.string.feedback_conetnt_exceed);
    }

    private void handlerMinTips() {
        ParentUtils.showToast(R.string.feedback_content_few);

    }

}
