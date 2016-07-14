package com.yanxiu.gphone.parent.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.common.login.LoginModel;
import com.common.login.model.ParentInfoBean;
import com.common.login.model.UserInfo;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.activity.base.TopViewBaseActivity;
import com.yanxiu.gphone.parent.bean.ParentGetChildInfoBean;
import com.yanxiu.gphone.parent.contants.YanxiuParentConstants;
import com.yanxiu.gphone.parent.eventbus.bean.ParentJumpToLoginChoiceEventBean;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;
import com.yanxiu.gphone.parent.jump.ParentBindJumpModel;
import com.yanxiu.gphone.parent.jump.out.utils.ParentOutActivityJumpUtils;
import com.yanxiu.gphone.parent.jump.utils.ParentActivityJumpUtils;
import com.yanxiu.gphone.parent.requestTask.RequestGetChildInfoTask;
import com.yanxiu.gphone.parent.view.ParentLoadingLayout;

import de.greenrobot.event.EventBus;

/**
 * 绑定账号
 * Created by lee on 16-3-21.
 */
public class ParentBindAccountActivity extends TopViewBaseActivity {
    private View mView;
    private EditText mAccountTv;
    private TextView mNextTv;
    private RequestGetChildInfoTask mTask;
    private int from;
    private static  final String TAG=ParentBindAccountActivity.class.getSimpleName();
    private   String mDefaultSchoolName;
    @Override
    protected boolean isAttach() {
        return true;
    }

    @Override
    protected void setTopView() {
        super.setTopView();
        topRootView.setBackgroundColor(getResources().getColor(R.color.color_white_p));
        titleText.setText(getResources().getString(R.string.bind_title));
        titleText.setTextColor(getResources().getColor(R.color.color_b28f47_p));
    }

    @Override
    protected void setRootView() {
        super.setRootView();
        rootView.setBackgroundResource(R.drawable.login_in_common_bg);
    }

    @Override
    protected View getContentView() {
        initLoadingBar(ParentBindAccountActivity.this,INDEX_SECOND);
        mView=getAttachView(R.layout.parent_bind_account_layout);
        initView();
        return mView;
    }
    private void requesetToServerForBind(){
        cancelTask();
        loading.setViewType(ParentLoadingLayout.LoadingType.LAODING_COMMON);
        mTask=new RequestGetChildInfoTask(ParentBindAccountActivity.this,mAccountTv.getText().toString(), new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                LogInfo.log(TAG, "update");
                loading.setViewGone();
                ParentGetChildInfoBean bean= (ParentGetChildInfoBean) result;
                String schoolName;
                if(bean.getProperty().getClassInfo()==null||StringUtils.isEmpty(bean.getProperty().getClassInfo().getSchoolname())){
                    schoolName=mDefaultSchoolName;
                }else{
                    schoolName=bean.getProperty().getClassInfo().getSchoolname();
                }
                String head=bean.getProperty().getStudent().getHead();
                String realName=bean.getProperty().getStudent().getRealname();
                String nickName=bean.getProperty().getStudent().getNickname();
                String id=bean.getProperty().getStudent().getId();
                ParentInfoBean parentInfoBean= (ParentInfoBean) LoginModel.getRoleLoginBean();
                if(parentInfoBean!=null&&parentInfoBean.getProperty()!=null){
                    parentInfoBean.getProperty().setClassinfo(bean.getProperty().getClassInfo());
                    UserInfo child=new UserInfo();
                    parentInfoBean.getProperty().getUser().setChild(child);
                    parentInfoBean.getProperty().getUser().getChild().setHead(head);
                    parentInfoBean.getProperty().getUser().getChild().setRealname(realName);
                    parentInfoBean.getProperty().getUser().getChild().setNickname(nickName);
                    parentInfoBean.getProperty().getUser().getChild().setId(Integer.valueOf(id));
                    LoginModel.savaCacheData();
                }
                ParentActivityJumpUtils.jumpToParentBindShowActivity(ParentBindAccountActivity
                        .this, mAccountTv.getText().toString(), schoolName, head, realName);
                mAccountTv.setText("");
            }

            @Override
            public void dataError(int type, String msg) {
                loading.setViewGone();
                showErrorWithFlag(type,msg,false);
                LogInfo.log(TAG,"type: "+type+"msg: "+msg);
            }
        });
        mTask.start();
    }


    @Override
    protected void showErrorWithFlag(int type, String msg, boolean isFristLoad) {
        switch (type){
            case ErrorCode.NETWORK_NOT_AVAILABLE:
                showNetErrorToast(R.string.public_loading_net_null_errtxt_p);
                break;
            case ErrorCode.NETWORK_REQUEST_ERROR:

            case ErrorCode.DATA_REQUEST_NULL:

            case ErrorCode.JOSN_PARSER_ERROR:
                if(StringUtils.isEmpty(msg)){
                    showNetErrorToast(R.string.data_request_fail_p);
                }else{
                    showNetErrorToast(msg);
                }
                break;
        }
    }

    private void cancelTask(){
        if(mTask!=null){
            mTask.cancel();
            mTask=null;
        }
    }


    private void initView() {
        mDefaultSchoolName=getResources().getString(R.string.no_class_tip);
        mAccountTv=(EditText)mView.findViewById(R.id.bindAccountTv);
        CommonCoreUtil.setViewTypeface(YanxiuParentConstants.METRO_STYLE, mAccountTv);
        mNextTv=(TextView)mView.findViewById(R.id.bingNextTv);
    }

    @Override
    protected void setContentListener() {
        mNextTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId()==R.id.bingNextTv){
            requesetToServerForBind();
        }
    }

    @Override
    protected void destoryData() {
        CommonCoreUtil.hideSoftInput(mAccountTv);
        if(from==YanxiuParentConstants.FROM_SETPSD){
            jumpToLoginChoiceComMethod();
        }else{
            ParentOutActivityJumpUtils.jumpOutToLoginChoiceActivity(ParentBindAccountActivity.this);
        }
    }

    private void  jumpToLoginChoiceComMethod(){
        ParentJumpToLoginChoiceEventBean eventBean=new ParentJumpToLoginChoiceEventBean();
        eventBean.setContext(ParentBindAccountActivity.this);
        EventBus.getDefault().post(eventBean);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(loading!=null){
            loading.setViewGone();
        }
        cancelTask();
        CommonCoreUtil.hideSoftInput(mAccountTv);
    }

    @Override
    protected void initLaunchIntentData() {
        ParentBindJumpModel jumpModel= (ParentBindJumpModel) getBaseJumpModel();
        if(jumpModel==null){
            return;
        }
        from=jumpModel.getForm();
    }
}
