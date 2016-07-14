package com.yanxiu.gphone.parent.activity;

import android.view.View;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.common.core.utils.imageloader.RotateImageViewAware;
import com.common.core.utils.imageloader.UniversalImageLoadTool;
import com.common.core.view.roundview.RoundedImageView;
import com.common.login.LoginModel;
import com.common.login.model.ParentInfoBean;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.activity.base.TopViewBaseActivity;
import com.yanxiu.gphone.parent.contants.YanxiuParentConstants;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;
import com.yanxiu.gphone.parent.jump.ParentBindShowJumpModel;
import com.yanxiu.gphone.parent.manager.ParentActivityManager;
import com.yanxiu.gphone.parent.requestTask.RequestBindChildTask;
import com.yanxiu.gphone.parent.utils.PublicLoadUtils;

/**
 * Created by lee on 16-3-21.
 */
public class ParentBindDataShowActivity extends TopViewBaseActivity {
    private RoundedImageView mRoundedImage;
    private TextView studentName,studentNameDes,inputAgainTv,bindConfirmTv;
    private String stdUid;
    private String schoolName,head,realName;
    private RequestBindChildTask mTask;
    private static final String TAG=ParentBindDataShowActivity.class.getSimpleName();
    @Override
    protected void initLaunchIntentData() {
        ParentBindShowJumpModel jumpModel= (ParentBindShowJumpModel) getBaseJumpModel();
        if(jumpModel==null){
            return;
        }
        stdUid=jumpModel.getStdUid();
        schoolName=jumpModel.getSchoolName();
        head=jumpModel.getHead();
        realName=jumpModel.getRealName();
    }

    @Override
    protected boolean isAttach() {
        return false;
    }

    @Override
    protected View getContentView() {
        mPublicLayout= PublicLoadUtils.createPage(ParentBindDataShowActivity.this, R.layout.parent_bind_data_show_actiity);
        mPublicLayout.finish();
        initView();
        showUI();
        return mPublicLayout;
    }

    @Override
    protected void setTopView() {
        super.setTopView();
        topRootView.setVisibility(View.GONE);
    }

    @Override
    protected void setRootView() {
        super.setRootView();
        rootView.setBackgroundResource(R.drawable.login_in_common_bg);
    }

    private void showUI() {
        if(!StringUtils.isEmpty(realName)){
            studentName.setText(realName);
        }else{
            studentName.setText(getResources().getString(R.string.unknow));
        }

        if(!StringUtils.isEmpty(schoolName)){
            studentNameDes.setText(schoolName);
        }else{
            studentNameDes.setText(getResources().getString(R.string.unknow));
        }
        UniversalImageLoadTool.disPlay(head, new RotateImageViewAware(mRoundedImage, head), R.drawable.default_student_icon);
    }

    private void requestToServerForBindData(){
        cancelTask();
        mPublicLayout.loading(true);
        mTask=new RequestBindChildTask(ParentBindDataShowActivity.this, stdUid, new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                finishPublicLayout();
                assert ((ParentInfoBean) LoginModel.getRoleLoginBean()) != null;
                ((ParentInfoBean)LoginModel.getRoleLoginBean()).getProperty().setIsBind(YanxiuParentConstants.HASBIND);
                LoginModel.savaCacheData();
                MainForParentActivity.launchActivity(ParentBindDataShowActivity.this);
                ParentActivityManager.destoryAllActivity();
            }

            @Override
            public void dataError(int type, String msg) {
                finishPublicLayout();
                showErrorWithFlag(type,msg,false);
                LogInfo.log(TAG,"type: "+type+"MSG: "+msg);
            }
        });
        mTask.start();
    }

    @Override
    protected void showErrorWithFlag(int type, String msg, boolean isFristLoad) {
        switch (type){
            case ErrorCode.NETWORK_NOT_AVAILABLE:
                showNetErrorToast(R.string.bind_net_exception);
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
    @Override
    protected void setContentListener() {
        inputAgainTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bindConfirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestToServerForBindData();

            }
        });
    }

    @Override
    protected void destoryData() {

    }



    private void initView() {
        mRoundedImage=(RoundedImageView)mPublicLayout.findViewById(R.id.studentImage);
        studentName=(TextView)mPublicLayout.findViewById(R.id.studentTextTv);
        studentNameDes=(TextView)mPublicLayout.findViewById(R.id.studentDes);
        inputAgainTv=(TextView)mPublicLayout.findViewById(R.id.inputAgainTv);
        bindConfirmTv=(TextView)mPublicLayout.findViewById(R.id.confirmInputTv);
    }
}
