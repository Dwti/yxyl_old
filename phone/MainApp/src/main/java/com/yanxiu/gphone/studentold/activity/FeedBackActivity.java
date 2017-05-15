package com.yanxiu.gphone.studentold.activity;

import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.common.core.utils.CommonCoreUtil;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.feedBack.AbstractFeedBack;
import com.yanxiu.gphone.studentold.feedBack.AdviceFeedBack;
import com.yanxiu.gphone.studentold.feedBack.ErrorFeedBack;
import com.yanxiu.gphone.studentold.jump.FeedbackJumpModel;
import com.yanxiu.gphone.studentold.utils.PublicLoadUtils;
import com.yanxiu.gphone.studentold.utils.Util;
import com.yanxiu.gphone.studentold.view.PublicLoadLayout;
import com.yanxiu.gphone.studentold.view.YanxiuTypefaceTextView;


/**
 * Created by Administrator on 2015/11/3.
 *  意见反馈
 */
public class FeedBackActivity extends TopViewBaseActivity implements AbstractFeedBack.TextWatcherListener,AbstractFeedBack.AsyncCallBack {
    private EditText _feedBackText;
    private PublicLoadLayout mPublic;
    private Handler mHandler=new Handler();
    private final static int DELAY_FINISH_TIME=1000;
    private AbstractFeedBack feedBack;
    private int type;
    private String quesetionId;
    private Button submitBtn;
    @Override
    protected boolean isAttach() {
        return false;
    }

    @Override
    protected View getContentView() {
        mPublic= PublicLoadUtils.createPage(this, R.layout.feedback_layout);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mPublic.setLayoutParams(params);
        mPublic.setContentBackground(getResources().getColor(R.color.color_transparent));
        mPublic.setBackgroundColor(getResources().getColor(R.color.color_transparent));
        rightText.setVisibility(View.GONE);
        _feedBackText=(EditText)mPublic.findViewById(R.id.editFeedBackContent);

        submitBtn=(Button)mPublic.findViewById(R.id.subBtn);
        Util.setViewTypeface(YanxiuTypefaceTextView.TypefaceType.FANGZHENG, submitBtn);
        switch (type){
            case AbstractFeedBack.ADVICE_FEED_BAck:
                feedBack=new AdviceFeedBack(this);
                titleText.setText(getResources().getString(R.string.feedback_title));
                _feedBackText.setHint(getResources().getString(R.string.feedback_hit));
                break;
            case AbstractFeedBack.ERROR_FEED_BACK:
                feedBack=new ErrorFeedBack(this);
                feedBack.setParams(quesetionId);
                titleText.setText(getResources().getString(R.string.subject_error_title));
                _feedBackText.setHint(getResources().getString(R.string.subject_error_hint));
                break;
        }

        _feedBackText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(AbstractFeedBack.MAX_NUMBERS)});
        return mPublic;
    }

    @Override
    protected void setContentContainerView() {
        super.setContentContainerView();
        RelativeLayout.LayoutParams contentParams= (RelativeLayout.LayoutParams) contentContainer.getLayoutParams();
        contentContainer.setLayoutParams(contentParams);
        contentContainer.setBackgroundResource(0);

    }



    @Override
    protected void setContentListener() {
        feedBack.setAsyncCallBack(this);
        feedBack.setTextWatcherListener(this);
        submitBtn.setOnClickListener(this);
        _feedBackText.addTextChangedListener(feedBack);
    }

    @Override
    protected void destoryData() {
        cancelTask();
    }

    @Override
    protected void initLaunchIntentData() {
        FeedbackJumpModel jumpModel= (FeedbackJumpModel) getBaseJumpModel();
        if(jumpModel==null){
            return;
        }
        type=jumpModel.getTypeCode();
        quesetionId=jumpModel.getQuestionId();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.subBtn:
                if(!CommonCoreUtil.isNetAvailableForPlay(FeedBackActivity.this)){
                    Util.showToast(getResources().getString(R.string.public_loading_net_errtxt));
                    return;
                }
                String content=_feedBackText.getText().toString().trim();
                requestUpLoad(content);
                break;
        }
    }





    private void requestUpLoad(String content){
        if(feedBack==null){
            return;
        }
        feedBack.startTask(content);

    }
    private void cancelTask(){
        if(feedBack==null){
            return;
        }
        feedBack.cancelTask();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {


    }


    @Override
    public void preExecute() {
        mPublic.finish();
        mPublic.loading(true);
    }

    @Override
    public void updateResult(YanxiuBaseBean result) {
           mPublic.finish();
        Util.showToast(getResources().getString(R.string.update_sucess));
            mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
            },DELAY_FINISH_TIME);
    }

    @Override
    public void dataResultError(int type, String msg) {
        mPublic.finish();
        Util.showToast(getResources().getString(R.string.update_fail));

    }
}
