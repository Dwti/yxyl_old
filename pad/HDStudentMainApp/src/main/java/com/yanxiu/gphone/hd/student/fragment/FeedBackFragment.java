package com.yanxiu.gphone.hd.student.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.activity.FeedBackActivity;
import com.yanxiu.gphone.hd.student.feedBack.AbstractFeedBack;
import com.yanxiu.gphone.hd.student.feedBack.AdviceFeedBack;
import com.yanxiu.gphone.hd.student.feedBack.ErrorFeedBack;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.inter.ProhibitedDoubleClickImpl;
import com.yanxiu.gphone.hd.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.view.YanxiuTypefaceTextView;

/**
 * Created by Administrator on 2016/2/14.
 * 反馈公用页  兼顾题目报错和意见反馈
 */
public class FeedBackFragment extends TopBaseFragment implements AbstractFeedBack.TextWatcherListener,AbstractFeedBack.AsyncCallBack {
    private EditText _feedBackText;
    private final Handler mHandler=new Handler();
    private final static int DELAY_FINISH_TIME=1000;
    private AbstractFeedBack feedBack;
    private int type;
    private String quesetionId;
    private Button submitBtn;
    private SetContainerFragment mFg;
    private static final String TYPECODE_KEY="type_code";
    private static final String QUESTIONID_KEY="questionid";

    private static FeedBackFragment mFeedBackFg;
    public static Fragment newInstance(int typeCode){
        if(mFeedBackFg==null){
            mFeedBackFg=new FeedBackFragment();
            Bundle bundle=new Bundle();
            bundle.putInt(TYPECODE_KEY, typeCode);
            mFeedBackFg.setArguments(bundle);
        }
        return mFeedBackFg;
    }

    public static Fragment newInstance(String questionId,int typeCode){
        FeedBackFragment mFeedBackFg=new FeedBackFragment();
        Bundle bundle=new Bundle();
        bundle.putInt(TYPECODE_KEY, typeCode);
        bundle.putString(QUESTIONID_KEY, questionId);
        mFeedBackFg.setArguments(bundle);
        return mFeedBackFg;
    }

    @Override
    protected boolean isAttach() {
        return false;
    }

    @Override
    protected void setTopView() {
        super.setTopView();
        rightText.setVisibility(View.GONE);
    }

    @Override
    protected void setRootView() {
        super.setRootView();
        rootView.setBackgroundResource(R.drawable.wood_bg);
    }

    @Override
    protected View getContentView() {
        type=getArguments().getInt(TYPECODE_KEY);
        quesetionId=getArguments().getString(QUESTIONID_KEY);
        mFg= (SetContainerFragment) getParentFragment();
        mPublicLayout=PublicLoadUtils.createPage(getActivity(), R.layout.feedback_layout);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mPublicLayout.setLayoutParams(params);
        mPublicLayout.setContentBackground(getResources().getColor(R.color.color_transparent));
        mPublicLayout.setBackgroundColor(getResources().getColor(R.color.color_transparent));
        _feedBackText=(EditText)mPublicLayout.findViewById(R.id.editFeedBackContent);
        submitBtn=(Button)mPublicLayout.findViewById(R.id.subBtn);
        Util.setViewTypeface(YanxiuTypefaceTextView.TypefaceType.FANGZHENG, submitBtn);
        return mPublicLayout;
    }


    @Override
    protected void setContentContainerView() {
        super.setContentContainerView();
        RelativeLayout.LayoutParams contentParams= (RelativeLayout.LayoutParams) contentContainer.getLayoutParams();
        contentParams.leftMargin=CommonCoreUtil.dipToPx(getActivity(),40);
        contentParams.rightMargin=CommonCoreUtil.dipToPx(getActivity(),43);
        contentParams.topMargin=CommonCoreUtil.dipToPx(getActivity(),22);
        contentContainer.setLayoutParams(contentParams);
        contentContainer.setBackgroundResource(0);
    }

    @Override
    protected void initLoadData() {

        switch (type){
            case AbstractFeedBack.ADVICE_FEED_BAck:
                feedBack=new AdviceFeedBack(getActivity());
                titleText.setText(getResources().getString(R.string.feedback_title));
                _feedBackText.setHint(getResources().getString(R.string.feedback_hit));
                break;
            case AbstractFeedBack.ERROR_FEED_BACK:
                feedBack=new ErrorFeedBack(getActivity());
                feedBack.setParams(quesetionId);
                titleText.setText(getResources().getString(R.string.subject_error_title));
                _feedBackText.setHint(getResources().getString(R.string.subject_error_hint));
                break;
        }
        feedBack.setAsyncCallBack(this);
        feedBack.setTextWatcherListener(this);
        _feedBackText.addTextChangedListener(feedBack);
        _feedBackText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(AbstractFeedBack.MAX_NUMBERS)});
    }

    @Override
    protected void setContentListener() {
        submitBtn.setOnClickListener(new ProhibitedDoubleClickImpl(new ProhibitedDoubleClickImpl.ProhibitedDroubleOnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CommonCoreUtil.isNetAvailableForPlay(getActivity())) {
                    Util.showToast(getResources().getString(R.string.public_loading_net_errtxt));
                    return;
                }
                String content = _feedBackText.getText().toString().trim();
                requestUpLoad(content);
            }
        }));

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.subBtn:

                break;
        }
    }
    private void requestUpLoad(String content){
        if(feedBack==null){
            return;
        }
        cancelTask();
        feedBack.startTask(content);

    }
    private void cancelTask(){
        if(feedBack==null){
            return;
        }
        feedBack.cancelTask();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            CommonCoreUtil.hideSoftInput(_feedBackText);
        }
    }

    @Override
    protected void destoryData() {
        cancelTask();
        finish();
    }

    @Override
    protected IFgManager getFragmentManagerFromSubClass() {
        return null;
    }

    @Override
    protected int getFgContainerIDFromSubClass() {
        return 0;
    }

    @Override
    public void preExecute() {
        mPublicLayout.finish();
        mPublicLayout.loading(true);
    }

    @Override
    public void updateResult(YanxiuBaseBean result) {
        mPublicLayout.finish();
        Util.showToast(getResources().getString(R.string.update_sucess));
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, DELAY_FINISH_TIME);
    }

    private void finish() {
        CommonCoreUtil.hideSoftInput(_feedBackText);
        //是否是题目报错的activity
        if(this.getActivity()!=null){
            if(this.getActivity() instanceof FeedBackActivity){
                this.getActivity().finish();
            }else{
                if(mFg!=null&&mFg.mIFgManager!=null){
                    mFg.mIFgManager.popStack();
                }
                mFeedBackFg=null;
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void dataResultError(int type, String msg) {
        mPublicLayout.finish();
        Util.showToast(getResources().getString(R.string.update_fail));
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
    public void onReset() {
     destoryData();
    }
}
