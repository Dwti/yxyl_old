package com.yanxiu.gphone.parent.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.utils.imageloader.RotateImageViewAware;
import com.common.core.utils.imageloader.UniversalImageLoadTool;
import com.common.core.view.roundview.RoundedImageView;
import com.common.login.LoginModel;
import com.common.login.model.ParentInfo;
import com.common.login.model.ParentInfoBean;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.activity.ParentFeedBackActivity;
import com.yanxiu.gphone.parent.activity.ParentInfomationActivity;
import com.yanxiu.gphone.parent.activity.ParentSettingsActivity;
import com.yanxiu.gphone.parent.bean.ParentChildHonorsBean;
import com.yanxiu.gphone.parent.bean.ParentHeaderEventBusModel;
import com.yanxiu.gphone.parent.bean.ParentNameEventBusModel;
import com.yanxiu.gphone.parent.contants.YanxiuParentConstants;
import com.yanxiu.gphone.parent.inter.AsyncCallBack;
import com.yanxiu.gphone.parent.jump.utils.ParentActivityJumpUtils;
import com.yanxiu.gphone.parent.requestTask.RequestParentChildHonorsTask;
import com.yanxiu.gphone.parent.requestTask.RequestUnbindTask;
import com.yanxiu.gphone.parent.utils.ParentUtils;
import com.yanxiu.gphone.parent.utils.PublicLoadUtils;
import com.yanxiu.gphone.parent.view.PublicLoadLayout;
import com.yanxiu.gphone.parent.view.UnbindDialog;

import de.greenrobot.event.EventBus;

/**
 * Created by hai8108 on 16/3/17.
 */
public class ParentMyFragment extends Fragment implements View.OnClickListener{

    private String headUrl;

    private PublicLoadLayout publicLoadLayout;
    //学生名字
    private TextView tvStudentsName;
    //学生的家长的名字
    private TextView tvParentsName;
    //学生获得的荣誉
    private TextView tvStudentHonor;
    //家长头像
    private RoundedImageView userHeadIv;

    private TextView tvTitle;


    private RelativeLayout rlFeedBack;
    private RelativeLayout rlSettings;
    private RelativeLayout rlLoginOut;

    private RelativeLayout rlRelieveivBind;

    private RelativeLayout rlInformationLayout;

    private TextView tvNationalRankingNum, tvRankHonorNum;

    private ImageView ivRelieveivBind;

    private RequestUnbindTask requestUnbindTask;

    //获取孩子荣誉接口
    private RequestParentChildHonorsTask requestParentChildHonorsTask;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        publicLoadLayout = PublicLoadUtils.createPage(this.getActivity(), R.layout.my_parent_fragment);
        publicLoadLayout.finish();

//        publicLoadLayout = inflater.inflate(R.layout.my_parent_fragment, null);

        initView();

        initData();

        return publicLoadLayout;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    private void initView(){
        userHeadIv = (RoundedImageView) publicLoadLayout.findViewById(R.id.iv_header);
//        userHeadIv.setCornerRadius(getResources().getDimensionPixelOffset(R.dimen.dimen_12));

        tvStudentsName = (TextView)publicLoadLayout.findViewById(R.id.tv_student_name);
        tvParentsName = (TextView)publicLoadLayout.findViewById(R.id.tv_parent_name);

        tvStudentHonor = (TextView)publicLoadLayout.findViewById(R.id.tv_person_honor);
        publicLoadLayout.findViewById(R.id.pub_top_left).setVisibility(View.GONE);
        tvTitle = (TextView) publicLoadLayout.findViewById(R.id.pub_top_mid);

        tvNationalRankingNum = (TextView) publicLoadLayout.findViewById(R.id.national_ranking_name_num);
        tvNationalRankingNum.setVisibility(View.GONE);
        tvRankHonorNum = (TextView) publicLoadLayout.findViewById(R.id.rank_of_honor_name_num);
        tvRankHonorNum.setVisibility(View.GONE);

        rlFeedBack = (RelativeLayout)publicLoadLayout.findViewById(R.id.feedback_layout);
        rlSettings = (RelativeLayout)publicLoadLayout.findViewById(R.id.settings_layout);
        rlLoginOut = (RelativeLayout)publicLoadLayout.findViewById(R.id.login_out_layout);

        rlRelieveivBind = (RelativeLayout) publicLoadLayout.findViewById(R.id.rl_relieveiv_bind);

        rlInformationLayout = (RelativeLayout) publicLoadLayout.findViewById(R.id.personal_information_layout);

        ivRelieveivBind = (ImageView) publicLoadLayout.findViewById(R.id.iv_relieveiv_bind);

        rlFeedBack.setOnClickListener(this);
        rlSettings.setOnClickListener(this);
        rlLoginOut.setOnClickListener(this);
        rlRelieveivBind.setOnClickListener(this);


        rlInformationLayout.setOnClickListener(this);

    }

    public void onEventMainThread(ParentNameEventBusModel parentNameEventBusModel) {
        if (parentNameEventBusModel != null) {
            String areaContent = parentNameEventBusModel.getName();
            tvParentsName.setText(areaContent);
        }
    }


    public void onEventMainThread(ParentHeaderEventBusModel parentHeaderEventBusModel) {
        if (parentHeaderEventBusModel != null) {
            String bitMapPath = parentHeaderEventBusModel.getHeaderPath();
            userHeadIv.setImageBitmap(CommonCoreUtil.getImage(bitMapPath));
        }
    }


    private void initData(){

        tvTitle.setText(this.getResources().getString(R.string.navi_tbm_mytab));

        if(LoginModel.getRoleUserInfoEntity() != null){

            headUrl = ((ParentInfo)LoginModel.getRoleUserInfoEntity()).getHead();


            String parentName = ((ParentInfo)LoginModel.getRoleUserInfoEntity()).getRealname();
            if(TextUtils.isEmpty(parentName)){
                tvParentsName.setText(this.getResources().getString(R.string.user_name_txt));
            }else{
                tvParentsName.setText(parentName);
            }

            if(((ParentInfo)LoginModel.getRoleUserInfoEntity()).getChild() != null){
                String studentName = ((ParentInfo)LoginModel.getRoleUserInfoEntity()).getChild().getRealname();
                if(TextUtils.isEmpty(studentName)){
                    String studentInfo = String.format(this.getResources().getString(R.string.parent_student_honor), this.getResources().getString(R.string.user_name_txt));
                    tvStudentHonor.setText(studentInfo);
                    studentInfo = String.format(this.getResources().getString(R.string.parent_student_parent), this.getResources().getString(R.string.user_name_txt));
                    tvStudentsName.setText(studentInfo);
                }else{
                    String studentInfo = String.format(this.getResources().getString(R.string.parent_student_parent), studentName);
                    tvStudentsName.setText(studentInfo);
                    studentInfo = String.format(this.getResources().getString(R.string.parent_student_honor), studentName);
                    tvStudentHonor.setText(studentInfo);
                }
            }else{
                ivRelieveivBind.setVisibility(View.GONE);
            }
        }else{
            tvParentsName.setText(this.getResources().getString(R.string.user_name_txt));
            String studentInfo = String.format(this.getResources().getString(R.string.parent_student_honor), this.getResources().getString(R.string.user_name_txt));
            tvStudentHonor.setText(studentInfo);
            studentInfo = String.format(this.getResources().getString(R.string.parent_student_parent), this.getResources().getString(R.string.user_name_txt));
            tvStudentsName.setText(studentInfo);
        }

        if(!TextUtils.isEmpty(headUrl)){
            LogInfo.log("geny", "headUrl---------" + headUrl);
            UniversalImageLoadTool.disPlay(headUrl,
                    new RotateImageViewAware(userHeadIv, headUrl), R.drawable.parent_hearder_default);
        }else{
            UniversalImageLoadTool.disPlay("", new RotateImageViewAware(userHeadIv, ""), R.drawable.parent_hearder_default);
        }

        requestData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            LogInfo.log("geny", "onHiddenChanged-------hide");
        }else{
            LogInfo.log("geny", "onHiddenChanged-------show");
            requestData();
        }
    }

    /**
     * 获取孩子荣誉接口
     */
    private void requestData(){
        cancelRequest();
        requestParentChildHonorsTask = new RequestParentChildHonorsTask(this.getActivity(), new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                ParentChildHonorsBean parentChildHonorsBean = (ParentChildHonorsBean) result;
                updateData(parentChildHonorsBean);
            }

            @Override
            public void dataError(int type, String msg) {

            }
        });
        requestParentChildHonorsTask.start();

    }


    private void updateData(ParentChildHonorsBean parentChildHonorsBean){
        if(parentChildHonorsBean.getData() != null && !parentChildHonorsBean.getData().isEmpty()){
            int rankNum = parentChildHonorsBean.getData().get(0).getRank();
            int honorNum = parentChildHonorsBean.getData().get(0).getTimes();
            tvNationalRankingNum.setVisibility(View.VISIBLE);
            tvNationalRankingNum.setText(String.valueOf(rankNum));
            tvRankHonorNum.setVisibility(View.VISIBLE);
            tvRankHonorNum.setText(String.valueOf(honorNum));
        }
    }

    private void cancelRequest(){
        if(requestParentChildHonorsTask != null){
            requestParentChildHonorsTask.cancel();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelRequest();
        requestParentChildHonorsTask = null;

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        if(view == rlFeedBack){
            ParentFeedBackActivity.launch(this.getActivity());
        }else if(view == rlSettings){
            ParentSettingsActivity.launch(this.getActivity());
        }else if(view == rlLoginOut){
            LoginModel.loginOut();
        }else if(view == rlInformationLayout){
            ParentInfomationActivity.launch(this.getActivity());
        }else if(view==rlRelieveivBind){
            UnbindDialog unbindDialog = new UnbindDialog(this.getActivity(), new UnbindDialog.UnbindCallBack() {
                @Override
                public void cancelButton() {

                }

                @Override
                public void sureButton() {
                    requestUnbind();
                }
            });
            unbindDialog.show();
        }
    }

    private void requestUnbind(){
        publicLoadLayout.loading(true);

        if(requestUnbindTask != null){
            requestUnbindTask.cancel();
        }

        requestUnbindTask = new RequestUnbindTask(this.getActivity(), new AsyncCallBack() {
            @Override
            public void update(YanxiuBaseBean result) {
                publicLoadLayout.finish();
                ParentUtils.showToast(R.string.parent_undind_success);
                ((ParentInfoBean)LoginModel.getRoleLoginBean()).getProperty().setIsBind(YanxiuParentConstants.NOTBIND);
                LoginModel.savaCacheData();
                ParentActivityJumpUtils.jumpToParentBindAccountActivity(ParentMyFragment.this.getActivity(), -1);
                ParentMyFragment.this.getActivity().finish();
            }

            @Override
            public void dataError(int type, String msg) {
                publicLoadLayout.finish();
                if (!TextUtils.isEmpty(msg)) {
                    ParentUtils.showToast(msg);
                } else {
                    ParentUtils.showToast(R.string.parent_undind_fail);
                }
            }
        });
        requestUnbindTask.start();
    }

}
