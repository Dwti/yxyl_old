package com.yanxiu.gphone.hd.student.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.common.core.utils.BasePopupWindow;
import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.utils.PictureHelper;
import com.common.core.utils.StringUtils;
import com.common.core.utils.imageloader.RotateImageViewAware;
import com.common.core.utils.imageloader.UniversalImageLoadTool;
import com.common.core.view.roundview.RoundedImageView;
import com.common.login.LoginModel;
import com.common.login.model.UserInfo;
import com.common.login.model.UserInfoBean;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.activity.UserLocationSelectActivity;
import com.yanxiu.gphone.hd.student.bean.DistrictModel;
import com.yanxiu.gphone.hd.student.bean.ProvinceModel;
import com.yanxiu.gphone.hd.student.bean.School;
import com.yanxiu.gphone.hd.student.bean.UploadFileBean;
import com.yanxiu.gphone.hd.student.eventbusbean.ClipHeadBean;
import com.yanxiu.gphone.hd.student.eventbusbean.MyGenderSelectBean;
import com.yanxiu.gphone.hd.student.eventbusbean.UserNameEditBean;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.requestTask.RequestUserInfoTask;
import com.yanxiu.gphone.hd.student.utils.HeadInfoObserver;
import com.yanxiu.gphone.hd.student.utils.MediaUtils;
import com.yanxiu.gphone.hd.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.hd.student.utils.RightContainerUtils;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.utils.XmlParserHandler;
import com.yanxiu.gphone.hd.student.utils.YanXiuConstant;
import com.yanxiu.gphone.hd.student.view.PublicLoadLayout;
import com.yanxiu.gphone.hd.student.view.SystemPicSelPop;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/1/28.
 */
public class MyUserInfoFragment extends TopBaseFragment {
    private final static String TAG = MyUserInfoFragment.class.getSimpleName();
    private UserInfo mUserinfoEntity = LoginModel.getUserinfoEntity();
    private RoundedImageView userHeadIv;
    private View userInfoLayout;
    private View userNameLayout;
    private View userNickNameLayout;
    private View userGenderLayout;
    private View userAreaLayout;
    private View userSchoolLayout;
    private TextView userNameContent; //姓名
    private TextView userNickNameContent;//昵称
    private TextView gendeyContent;//性别
    private TextView userAreaContent;//地区
    private TextView userSchoolContent;//学校
    private ArrayList<ProvinceModel> provinceList;
    private int type;
    private RequestUserInfoTask mRequestUserInfoTask;

    private BasePopupWindow pop;
    private final static int PHOTO_CUT_RESULT = 0x204;
    private final static int SEARCH_SCHOOL_REQUESTCODE = 0x205;
    private MyUserInfoContainerFragment fg;

    private BaseFragment mCurFg;


    public static Fragment newInstance () {

        return new MyUserInfoFragment();
    }

    @Override
    protected boolean isAttach () {
        return false;
    }


    @Override
    protected void setRootView() {
        super.setRootView();
        rootView.setBackgroundResource(R.drawable.blue_bg);
    }
    protected View getContentView () {
        fg = (MyUserInfoContainerFragment) getParentFragment();
        EventBus.getDefault().register(this);
        mPublicLayout = PublicLoadUtils.createPage(getActivity(), R.layout.activity_info_user_layout);
        mPublicLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.trans));
        mPublicLayout.setContentBackground(android.R.color.transparent);
        initProvinceDatas();
        findView();
        return mPublicLayout;
    }

    @Override
    protected void initLoadData() {
        if (mUserinfoEntity != null) {
            updateView();
        } else {
            requestUserInfoTask();
        }
    }


    private void findView () {
        userInfoLayout = mPublicLayout.findViewById(R.id.user_info_layout);

        View userHeadLayout = mPublicLayout.findViewById(R.id.user_info_headiv_layout);
        userHeadLayout.setOnClickListener(this);
        userNameLayout = mPublicLayout.findViewById(R.id.user_info_name_layout);
        userNickNameLayout = mPublicLayout.findViewById(R.id.user_info_nickname_layout);
        userGenderLayout = mPublicLayout.findViewById(R.id.user_info_gender_layout);
        gendeyContent = (TextView) userGenderLayout.findViewById(R.id.item_sub_content);
        gendeyContent.setVisibility(View.VISIBLE);

        userAreaLayout = mPublicLayout.findViewById(R.id.user_info_area_layout);
        userSchoolLayout = mPublicLayout.findViewById(R.id.user_info_school_layout);


        userHeadIv = (RoundedImageView) userHeadLayout.findViewById(R.id.user_icon);
        userHeadIv.setCornerRadius(getResources().getDimensionPixelOffset(R.dimen.dimen_12));
        if(LoginModel.getUserinfoEntity()!=null&&LoginModel.getUserinfoEntity().getHead()!=null&&!StringUtils.isEmpty(LoginModel.getUserinfoEntity().getHead())){
            UniversalImageLoadTool.disPlay(LoginModel.getUserinfoEntity().getHead(),
                    new RotateImageViewAware(userHeadIv, LoginModel.getUserinfoEntity().getHead()), R.drawable.user_info_default_bg);
        }else{
            UniversalImageLoadTool.disPlay("",
                    new RotateImageViewAware(userHeadIv, ""), R.drawable.user_info_default_bg);

        }
        createView();


        pop = new SystemPicSelPop(getActivity());


    }

    private void createView () {


        userNameLayout.findViewById(R.id.item_right_arrow).setVisibility(View.VISIBLE);
        TextView userName = (TextView) userNameLayout.findViewById(R.id.item_name);
        userName.setText(R.string.user_name_str);
        userNameContent = (TextView) userNameLayout.findViewById(R.id.item_sub_content);
        userNameContent.setVisibility(View.VISIBLE);
        userNameLayout.setOnClickListener(this);

        userNickNameLayout.findViewById(R.id.item_right_arrow).setVisibility(View.VISIBLE);
        TextView userNickName = (TextView) userNickNameLayout.findViewById(R.id.item_name);
        userNickName.setText(R.string.user_nick_name);
        userNickNameContent = (TextView) userNickNameLayout.findViewById(R.id.item_sub_content);
        userNickNameContent.setVisibility(View.VISIBLE);
        userNickNameLayout.setOnClickListener(this);

        userGenderLayout.findViewById(R.id.item_right_arrow).setVisibility(View.VISIBLE);
        userGenderLayout.setOnClickListener(this);

        TextView itemContent = (TextView) userGenderLayout.findViewById(R.id.item_sub_content);
        itemContent.setVisibility(View.VISIBLE);

        TextView rightText = (TextView) userGenderLayout.findViewById(R.id.item_name);
        rightText.setText(R.string.user_gender_str);

        ImageView rightImg = (ImageView) userGenderLayout.findViewById(R.id.item_right_arrow);
        rightImg.setVisibility(View.VISIBLE);


        userAreaLayout.findViewById(R.id.item_right_arrow).setVisibility(View.VISIBLE);
        TextView userArea = (TextView) userAreaLayout.findViewById(R.id.item_name);
        userArea.setText(R.string.user_area_str);
        userAreaContent = (TextView) userAreaLayout.findViewById(R.id.item_sub_content);
        userAreaContent.setVisibility(View.VISIBLE);
        userAreaLayout.setOnClickListener(this);

        userSchoolLayout.findViewById(R.id.item_right_arrow).setVisibility(View.VISIBLE);
        TextView userSchool = (TextView) userSchoolLayout.findViewById(R.id.item_name);
        userSchool.setText(R.string.user_school_str);
        userSchoolContent = (TextView) userSchoolLayout.findViewById(R.id.item_sub_content);
        userSchoolContent.setVisibility(View.VISIBLE);
        userSchoolLayout.setOnClickListener(this);
    }


    private final AsyncCallBack mAsyncCallBack = new AsyncCallBack() {
        @Override
        public void update (YanxiuBaseBean result) {
            mPublicLayout.finish();
            mUserinfoEntity = ((UserInfoBean) result).getData().get(0);
            updateView();
        }

        @Override
        public void dataError (int type, String msg) {
            userInfoLayout.setVisibility(View.GONE);
            mPublicLayout.dataError(true);
        }
    };

    private void requestUserInfoTask () {
        cancelUserInfoTask();
        mPublicLayout.loading(true);
        mRequestUserInfoTask = new RequestUserInfoTask(getActivity(), mAsyncCallBack);
        mRequestUserInfoTask.start();
    }


    private void cancelUserInfoTask () {
        if (mRequestUserInfoTask != null) {
            mRequestUserInfoTask.cancel();
        }
        mRequestUserInfoTask = null;
    }

    @Override
    protected void setTopView () {
        super.setTopView();
        titleText.setText(getActivity().getResources().getString(R.string.user_detail_info));
        leftView.setVisibility(View.GONE);
    }

    @Override
    protected void setContentListener () {
        mPublicLayout.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override
            public void refreshData() {
                requestUserInfoTask();
            }
        });
    }

    private void updateView () {
        UniversalImageLoadTool.disPlay(mUserinfoEntity.getHead(),
                new RotateImageViewAware(userHeadIv, mUserinfoEntity.getHead()), R.drawable.user_info_default_bg);
        userInfoLayout.setVisibility(View.VISIBLE);
        userNameContent.setText(mUserinfoEntity.getRealname());
        userNickNameContent.setText(mUserinfoEntity.getNickname());
        LogInfo.log(TAG, "sex: " + mUserinfoEntity.getSex());

        if (StringUtils.isEmpty(mUserinfoEntity.getSex())) {
            gendeyContent.setText(R.string.sex_unknown_txt);
        } else {
            try {
                setGenderTxt(Integer.valueOf(mUserinfoEntity.getSex()));
            } catch (Exception e) {
                gendeyContent.setText(R.string.sex_unknown_txt);
            }
        }

        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(mUserinfoEntity.getProvinceName())) {
            sb.append(mUserinfoEntity.getProvinceName()).append("/");
        }
        if (!TextUtils.isEmpty(mUserinfoEntity.getCityName())) {
            sb.append(mUserinfoEntity.getCityName()).append("/");
        }
        if (!TextUtils.isEmpty(mUserinfoEntity.getAreaName())) {
            sb.append(mUserinfoEntity.getAreaName());
        }
        userAreaContent.setText(sb.toString());
        userSchoolContent.setText(mUserinfoEntity.getSchoolName());
    }


    private void setGenderTxt (int type) {
        if (type == YanXiuConstant.Gender.GENDER_TYPE_MALE) {
            gendeyContent.setText(R.string.male_txt);
        } else if (type == YanXiuConstant.Gender.GENDER_TYPE_FEMALE) {
            gendeyContent.setText(R.string.female_txt);
        } else {
            gendeyContent.setText(R.string.sex_unknown_txt);
        }
    }

    private void initProvinceDatas() {
        provinceList = null;
        provinceList = new ArrayList<>();
        AssetManager asset = getActivity().getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList.addAll(handler.getDataList());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片裁剪
     *
     * @param uri
     */
    public void startPhotoZoom (Uri uri) {
        if (uri == null) {
            LogInfo.log("haitian", "The uri is not exist.");
            return;
        }
        fg.mIFgManager.addFragment(ClipHeadFragment.newInstance(PictureHelper.getPath(getActivity(), uri), PHOTO_CUT_RESULT), true);
    }


    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case MediaUtils.OPEN_SYSTEM_PIC_BUILD_KITKAT:
                case MediaUtils.OPEN_SYSTEM_PIC_BUILD:
                    Uri imageFileUri = data.getData();//获取选择图片的URI
                    startPhotoZoom(imageFileUri);
                    break;
                case MediaUtils.OPEN_SYSTEM_CAMERA:
                    Uri fileUri = MediaUtils.getOutputMediaFileUri(false);
                    if (fileUri != null) {
                        LogInfo.log(TAG, " MediaUtils.OPEN_SYSTEM_CAMERA fileUri" + fileUri);
                        startPhotoZoom(fileUri);
                    } else {
                        Util.showToast(getResources().getString(R.string.loading_fail));
                    }
                    break;
            }
        }

    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     */
    private void setPicToView (final String bitMapPath) {
        if (!TextUtils.isEmpty(bitMapPath)) {
            LogInfo.log(TAG, "bitMapPath s =" + bitMapPath);
            mPublicLayout.loading(true);
            Map<String, File> fileMap = new HashMap<>();
            fileMap.put("filename", new File(bitMapPath));
            /*
      上传任务
     */
             YanxiuHttpApi.requestUploadFile(fileMap, new YanxiuHttpApi.UploadFileListener() {
                @Override
                public void onFail(YanxiuBaseBean bean) {
                    mPublicLayout.finish();
                    if (bean == null) {
                        Util.showToast(R.string.upload_head_img_failed);
                    } else {
                        if (!TextUtils.isEmpty(((UploadFileBean) bean).getStatus().getDesc())) {
                            LoginModel.loginOut();
                            Util.showToast(((UploadFileBean) bean).getStatus().getDesc());
                        } else {
                            Util.showToast(R.string.upload_head_img_failed);
                        }
                    }
                }

                @Override
                public void onSuccess(YanxiuBaseBean bean) {
                    mPublicLayout.finish();
                    if (bean == null) {
                        Util.showToast(R.string.upload_head_img_succeed);
                    } else {
                        if (!TextUtils.isEmpty(((UploadFileBean) bean).getStatus().getDesc())) {
                            if (((UploadFileBean) bean).getData() != null && ((UploadFileBean) bean).getData().size() > 0) {
                                String headUrl = ((UploadFileBean) bean).getData().get(0).getHead();
                                if(   LoginModel.getUserinfoEntity()!=null){
                                    LoginModel.getUserinfoEntity().setHead(headUrl);
                                }
                                LoginModel.savaCacheData();
                                userHeadIv.setImageBitmap(CommonCoreUtil.getImage(bitMapPath));
                                notifyMyHeadInfoView();
                            }
                            Util.showToast(((UploadFileBean) bean).getStatus().getDesc());
                        } else {
                            Util.showToast(R.string.upload_head_img_succeed);
                        }
                    }
                }

                @Override
                public void onProgress(int progress) {

                }
            });
        }
    }


    @Override
    public void onClick (View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.user_info_headiv_layout:
                int x=CommonCoreUtil.getScreenWidth()- RightContainerUtils.getInstance().getContainerWidth();
                this.pop.showAtLocation(view, Gravity.NO_GRAVITY, x,topRootView.getMeasuredHeight()+CommonCoreUtil.getStatusBarHeight());
                break;
            case R.id.user_info_name_layout:
                if(!CommonCoreUtil.checkClickEvent(800)){
                    return;
                }
                mCurFg= (BaseFragment) UserNameEdFragment.newInstance(UserNameEdFragment.USER_EDIT_NAME_TYPE, userNameContent.getText().toString());
                fg.mIFgManager.addFragment(mCurFg, true);
                break;
            case R.id.user_info_nickname_layout:
                if(!CommonCoreUtil.checkClickEvent(800)){
                    return;
                }
                mCurFg= (BaseFragment) UserNameEdFragment.newInstance(UserNameEdFragment.USER_EDIT_NICK_NAME_TYPE, userNickNameContent.getText().toString());
                fg.mIFgManager.addFragment(mCurFg, true);
                break;
            case R.id.user_info_gender_layout:
                if (mUserinfoEntity != null) {
                    if (StringUtils.isEmpty(mUserinfoEntity.getSex())) {
                        mCurFg= (BaseFragment) MyGenderSelectFragment.newInstance(YanXiuConstant.Gender.GENDER_TYPE_UNKNOWN);
                        fg.mIFgManager.addFragment(mCurFg, true);
                    } else {
                        try {
                            int type = Integer.valueOf(mUserinfoEntity.getSex());
                            mCurFg= (BaseFragment) MyGenderSelectFragment.newInstance(type);
                            fg.mIFgManager.addFragment(mCurFg, true);
                        } catch (Exception e) {
                            mCurFg= (BaseFragment) MyGenderSelectFragment.newInstance(YanXiuConstant.Gender.GENDER_TYPE_UNKNOWN);
                            fg.mIFgManager.addFragment(mCurFg, true);
                        }
                    }
                }
                break;
            case R.id.user_info_area_layout:
                if(!CommonCoreUtil.checkClickEvent(800)){
                    return;
                }
                mCurFg= (BaseFragment) UserLocationFragment.newInstance(provinceList, UserLocationSelectActivity.LOCATION_CONSTANT_PROVINCE);
                fg.mIFgManager.addFragment(mCurFg,true,UserLocationFragment.class.getName());
                break;
            case R.id.user_info_school_layout:
                mCurFg= (BaseFragment) SchoolSearchFragment.newInstance(LoginModel.getUserinfoEntity().getAreaid(),SEARCH_SCHOOL_REQUESTCODE);
                fg.mIFgManager.addFragment(mCurFg,true);
                break;


        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogInfo.log(TAG, "onKeyDown");
        if(mCurFg!=null){
            return mCurFg.onKeyDown(keyCode,event);
        }else{
            return super.onKeyDown(keyCode,event);
        }
    }

    @Override
    protected void destoryData () {
        cancelUserInfoTask();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        cancelUserInfoTask();
        mUserinfoEntity =null;
        userHeadIv=null;
        userInfoLayout=null;
        userNameLayout=null;
        userNickNameLayout=null;
        userGenderLayout=null;
        userAreaLayout=null;
        userSchoolLayout=null;
        userNameContent=null; //姓名
        userNickNameContent=null;//昵称
        gendeyContent=null;//性别
        userAreaContent=null;//地区
        userSchoolContent=null;//学校
        provinceList=null;
        pop=null;
        fg=null;
        mCurFg=null;
    }



    @Override
    protected IFgManager getFragmentManagerFromSubClass () {
        return null;
    }

    @Override
    protected int getFgContainerIDFromSubClass () {
        return 0;
    }

    public void onEventMainThread (DistrictModel districtModel) {
        if (districtModel != null) {
            String areaContent = districtModel.getProvinceName() + "/" + districtModel.getCityName() + "/" + districtModel.getName();
            LogInfo.log("haitian", "districtModel.toString=" + areaContent);
            userAreaContent.setText(areaContent);
        }
    }


    public void onEventMainThread (ClipHeadBean clipHeadBean) {
        if (clipHeadBean != null) {
            String headPicPath = clipHeadBean.getClipPicPath();
            setPicToView(headPicPath);
        }
    }

    public void onEventMainThread (UserNameEditBean userNameEditBean) {
        if (userNameEditBean != null) {
            type = userNameEditBean.getType();
            if (type == 0) {
                type = UserNameEdFragment.USER_EDIT_NAME_TYPE;
            }
            String editMsg = userNameEditBean.getEditMsg();
            LogInfo.log(TAG, "type=" + type + "  editMsg=" + editMsg);
            if (type == UserNameEdFragment.USER_EDIT_NAME_TYPE) {
                userNameContent.setText(editMsg);
            } else {
                userNickNameContent.setText(editMsg);
            }
            notifyMyHeadInfoView();
        }

    }


    public void onEventMainThread (MyGenderSelectBean myGenderSelectBean) {
        if (myGenderSelectBean != null) {
            type = myGenderSelectBean.getType();
            LogInfo.log(TAG, "type =" + type);
            if(mUserinfoEntity != null){
                mUserinfoEntity.setSex(type+"");
            }
            setGenderTxt(type);
        }
    }

    public void onEventMainThread(School mSchool){
        if (mSchool != null) {
            userSchoolContent.setText(mSchool.getName());
        }
    }

    private void notifyMyHeadInfoView () {
        HeadInfoObserver.getInstance().notifyChange();
    }


    @Override
    public void onReset() {
        destoryData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(mCurFg!=null){
            mCurFg.onHiddenChanged(hidden);
        }
    }
}
