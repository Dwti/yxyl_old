package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.bean.DistrictModel;
import com.yanxiu.gphone.student.bean.ProvinceModel;
import com.yanxiu.gphone.student.bean.School;
import com.yanxiu.gphone.student.bean.UploadFileBean;
import com.yanxiu.gphone.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.student.inter.AsyncCallBack;
import com.yanxiu.gphone.student.requestTask.RequestUserInfoTask;
import com.yanxiu.gphone.student.utils.MediaUtils;
import com.yanxiu.gphone.student.utils.PublicLoadUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.XmlParserHandler;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.PublicLoadLayout;
import com.yanxiu.gphone.student.view.SystemPicSelPop;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/7/6
 */
public class MyUserInfoActivity extends YanxiuBaseActivity implements View.OnClickListener {
    private final static String TAG=MyUserInfoActivity.class.getSimpleName();
    public final static int MY_USERINFO_REQUESTCODE = 0x100;

    private final static int PHOTO_CUT_RESULT = 0x204;
    private final static int SEARCH_SCHOOL_REQUESTCODE = 0x205;

    private PublicLoadLayout mRootView;

    private Uri fileUri;

    private View userInfoLayout, topLayout;
    private View backView;
    private RoundedImageView userHeadIv;

    private TextView topTitle;
    private View userHeadLayout, userNameLayout, userNickNameLayout,
            userGenderLayout, userAreaLayout, userSchoolLayout;

    private TextView userName, userNameContent; //姓名
    private TextView userNickName, userNickNameContent;//昵称
    private TextView gendeyContent;//性别
    private TextView userArea, userAreaContent;//地区
    private TextView userSchool, userSchoolContent;//学校
    private ArrayList<ProvinceModel> provinceList;
    private int type;
    private String editMsg;

    private RequestUserInfoTask mRequestUserInfoTask;
    private UserInfo mUserinfoEntity = LoginModel.getUserinfoEntity();
    private String headPath = null;
    /**
     * 上传任务
     * */
    private AsyncTask uploadAsyncTask;

    private BasePopupWindow pop;


    public static void launchActivity(Activity context) {
        Intent intent = new Intent(context, MyUserInfoActivity.class);
        context.startActivityForResult(intent, MY_USERINFO_REQUESTCODE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = PublicLoadUtils.createPage(this, R.layout.activity_info_user_layout);
        setContentView(mRootView);
        EventBus.getDefault().register(this);
        mRootView.setmRefreshData(new PublicLoadLayout.RefreshData() {
            @Override
            public void refreshData() {
                requestUserInfoTask();
            }
        });
        initProvinceDatas();
        findView();

        if (mUserinfoEntity != null) {
            updateView();
        } else {
            requestUserInfoTask();
        }

    }


    private void findView() {
        userInfoLayout = mRootView.findViewById(R.id.user_info_layout);
        topLayout = mRootView.findViewById(R.id.top_layout);

        userHeadLayout = mRootView.findViewById(R.id.user_info_headiv_layout);
        userHeadLayout.setOnClickListener(this);
        userNameLayout = mRootView.findViewById(R.id.user_info_name_layout);
        userNickNameLayout = mRootView.findViewById(R.id.user_info_nickname_layout);
        userGenderLayout = mRootView.findViewById(R.id.user_info_gender_layout);
        gendeyContent = (TextView)userGenderLayout. findViewById(R.id.item_sub_content);
        gendeyContent.setVisibility(View.VISIBLE);

        userAreaLayout = mRootView.findViewById(R.id.user_info_area_layout);
        userSchoolLayout = mRootView.findViewById(R.id.user_info_school_layout);



        backView = topLayout.findViewById(R.id.pub_top_left);
        backView.setOnClickListener(this);
        topTitle = (TextView) topLayout.findViewById(R.id.pub_top_mid);
        topTitle.setText(R.string.user_detail_info);
        userHeadIv = (RoundedImageView) userHeadLayout.findViewById(R.id.user_icon);
        userHeadIv.setCornerRadius(getResources().getDimensionPixelOffset(R.dimen.dimen_12));
        UniversalImageLoadTool.disPlay(LoginModel.getUserinfoEntity().getHead(),
                new RotateImageViewAware(userHeadIv, LoginModel.getUserinfoEntity().getHead()), R.drawable.user_info_default_bg);

        createView();


        pop=new SystemPicSelPop(MyUserInfoActivity.this);







    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private AsyncCallBack mAsyncCallBack = new AsyncCallBack() {
        @Override
        public void update(YanxiuBaseBean result) {
            mRootView.finish();
            mUserinfoEntity = ((UserInfoBean) result).getData().get(0);
            updateView();
        }

        @Override
        public void dataError(int type, String msg) {
            userInfoLayout.setVisibility(View.GONE);
            mRootView.dataError(true);
        }
    };

    private void requestUserInfoTask() {
        cancelUserInfoTask();
        mRootView.loading(true);
        mRequestUserInfoTask = new RequestUserInfoTask(this, mAsyncCallBack);
        mRequestUserInfoTask.start();
    }

    private void cancelUserInfoTask() {
        if (mRequestUserInfoTask != null) {
            mRequestUserInfoTask.cancel();
        }
        mRequestUserInfoTask = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        cancelUserInfoTask();
    }



    private void createView() {


        userNameLayout.findViewById(R.id.item_right_arrow).setVisibility(View.VISIBLE);
        userName = (TextView) userNameLayout.findViewById(R.id.item_name);
        userName.setText(R.string.user_name_str);
        userNameContent = (TextView) userNameLayout.findViewById(R.id.item_sub_content);
        userNameContent.setVisibility(View.VISIBLE);
        userNameLayout.setOnClickListener(this);

        userNickNameLayout.findViewById(R.id.item_right_arrow).setVisibility(View.VISIBLE);
        userNickName = (TextView) userNickNameLayout.findViewById(R.id.item_name);
        userNickName.setText(R.string.user_nick_name);
        userNickNameContent = (TextView) userNickNameLayout.findViewById(R.id.item_sub_content);
        userNickNameContent.setVisibility(View.VISIBLE);
        userNickNameLayout.setOnClickListener(this);

        userGenderLayout.findViewById(R.id.item_right_arrow).setVisibility(View.VISIBLE);
        userGenderLayout.setOnClickListener(this);

        TextView itemContent = (TextView) userGenderLayout.findViewById(R.id.item_sub_content);
        itemContent.setVisibility(View.VISIBLE);

        TextView rightText=(TextView)userGenderLayout.findViewById(R.id.item_name);
        rightText.setText(R.string.user_gender_str);

        ImageView rightImg=(ImageView)userGenderLayout.findViewById(R.id.item_right_arrow);
        rightImg.setVisibility(View.VISIBLE);



        userAreaLayout.findViewById(R.id.item_right_arrow).setVisibility(View.VISIBLE);
        userArea = (TextView) userAreaLayout.findViewById(R.id.item_name);
        userArea.setText(R.string.user_area_str);
        userAreaContent = (TextView) userAreaLayout.findViewById(R.id.item_sub_content);
        userAreaContent.setVisibility(View.VISIBLE);
        userAreaLayout.setOnClickListener(this);

        userSchoolLayout.findViewById(R.id.item_right_arrow).setVisibility(View.VISIBLE);
        userSchool = (TextView) userSchoolLayout.findViewById(R.id.item_name);
        userSchool.setText(R.string.user_school_str);
        userSchoolContent = (TextView) userSchoolLayout.findViewById(R.id.item_sub_content);
        userSchoolContent.setVisibility(View.VISIBLE);
        userSchoolLayout.setOnClickListener(this);
    }

    private void updateView() {
        UniversalImageLoadTool.disPlay(mUserinfoEntity.getHead(),
                new RotateImageViewAware(userHeadIv, mUserinfoEntity.getHead()), R.drawable.user_info_default_bg);
        userInfoLayout.setVisibility(View.VISIBLE);
        userNameContent.setText(mUserinfoEntity.getRealname());
        userNickNameContent.setText(mUserinfoEntity.getNickname());
        LogInfo.log(TAG, "sex: " + mUserinfoEntity.getSex());

        if(StringUtils.isEmpty(mUserinfoEntity.getSex())){
            gendeyContent.setText(R.string.sex_unknown_txt);
        }else{
            try{
                setGenderTxt(Integer.valueOf(mUserinfoEntity.getSex()));
            }catch (Exception e){
                gendeyContent.setText(R.string.sex_unknown_txt);
            }
        }

        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(mUserinfoEntity.getProvinceName())) {
            sb.append(mUserinfoEntity.getProvinceName() + "/");
        }
        if (!TextUtils.isEmpty(mUserinfoEntity.getCityName())) {
            sb.append(mUserinfoEntity.getCityName() + "/");
        }
        if (!TextUtils.isEmpty(mUserinfoEntity.getAreaName())) {
            sb.append(mUserinfoEntity.getAreaName());
        }
        userAreaContent.setText(sb.toString());
        userSchoolContent.setText(mUserinfoEntity.getSchoolName());
    }

    @Override
    public void onClick(View v) {
        if (v == userHeadLayout) {
            this.pop.showAtLocation(v, Gravity.BOTTOM,0,0);
        } else if (v == userNameLayout) {
            UserNameEditActivity.launch(this, UserNameEditActivity.USER_EDIT_NAME_TYPE, userNameContent.getText().toString());
        } else if (v == userNickNameLayout) {
            UserNameEditActivity.launch(this, UserNameEditActivity.USER_EDIT_NICK_NAME_TYPE, userNickNameContent.getText().toString());
        } else if (v == userGenderLayout) {

            if (mUserinfoEntity != null) {

                if(StringUtils.isEmpty(mUserinfoEntity.getSex())){
                    MyGenderSelectActivity.launch(this, YanXiuConstant.Gender.GENDER_TYPE_UNKNOWN);
                }else{
                    try {
                        int type=Integer.valueOf(mUserinfoEntity.getSex());
                        MyGenderSelectActivity.launch(this,type);
                    }catch (Exception e){
                        MyGenderSelectActivity.launch(this, YanXiuConstant.Gender.GENDER_TYPE_UNKNOWN);
                    }

                }
            }
        } else if (v == userAreaLayout) {
            UserLocationSelectActivity.launch(this, provinceList, UserLocationSelectActivity.LOCATION_CONSTANT_PROVINCE);
        } else if (v == userSchoolLayout) {
            if (mUserinfoEntity != null) {
                SchoolSearchActivity.launch(this, mUserinfoEntity.getAreaid(), SEARCH_SCHOOL_REQUESTCODE);
            }
        } else if (v == backView) {
            forResult();
        }
    }







    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            forResult();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void forResult() {
        Intent intent = new Intent();
        intent.putExtra("name", userNickNameContent.getText().toString());
        intent.putExtra("urlicon", mUserinfoEntity.getHead());
        intent.putExtra("headPath", headPath);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onEventMainThread(DistrictModel districtModel) {
        if (districtModel != null) {
            String areaContent = districtModel.getProvinceName() + "/" + districtModel.getCityName() + "/" + districtModel.getName();
            LogInfo.log("haitian", "districtModel.toString=" + areaContent);
            userAreaContent.setText(areaContent);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mUserinfoEntity = LoginModel.getUserinfoEntity();
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case UserLocationSelectActivity.LOCATION_REQUEST_CODE:
                    if (data != null) {
                        DistrictModel districtModel = (DistrictModel) data.getSerializableExtra("districtModel");
                        if (districtModel != null) {
                            String areaContent = districtModel.getProvinceName() + "/" + districtModel.getCityName() + "/" + districtModel.getName();
                            LogInfo.log("haitian", "districtModel.toString=" + areaContent);
                            userAreaContent.setText(areaContent);
                        }
                    }
                    break;
                case UserNameEditActivity.USER_EDIT_REQUEST_CODE:
                    if (data != null) {
                        type = data.getIntExtra("type", UserNameEditActivity.USER_EDIT_NAME_TYPE);
                        editMsg = data.getStringExtra("editMsg");
                        LogInfo.log("haitian", "type=" + type + "  editMsg=" + editMsg);
                        if (type == UserNameEditActivity.USER_EDIT_NAME_TYPE) {
                            userNameContent.setText(editMsg);
                        } else {
                            userNickNameContent.setText(editMsg);
                        }
                    }
                    break;
                case MyGenderSelectActivity.GENDER_REQUEST_CODE:
                    if (data != null) {
                        type = data.getIntExtra("type", 0);
                        LogInfo.log("haitian", "type =" + type);
                        setGenderTxt(type);
                    }
                    break;
                case MediaUtils.OPEN_SYSTEM_PIC_BUILD_KITKAT:
                case MediaUtils.OPEN_SYSTEM_PIC_BUILD:
                    Uri imageFileUri = data.getData();//获取选择图片的URI
                    startPhotoZoom(imageFileUri);
                    break;
                case MediaUtils.OPEN_SYSTEM_CAMERA:
                    fileUri = MediaUtils.getOutputMediaFileUri(false);
                    if(fileUri!=null){
                        LogInfo.log(TAG," MediaUtils.OPEN_SYSTEM_CAMERA fileUri"+fileUri);
                        startPhotoZoom(fileUri);
                    }else{
                        Util.showToast(getResources().getString(R.string.loading_fail));
                    }

                    break;
                case PHOTO_CUT_RESULT:
                    if (data != null) {
                        setPicToView(data);
                    }
                    break;
                case SEARCH_SCHOOL_REQUESTCODE:
                    if (data != null) {
                        Bundle bundle = data.getBundleExtra("data");
                        School school = (School) bundle.getSerializable("school");
                        if (school != null) {
                            userSchoolContent.setText(school.getName());
                        }
                    }
                    break;
            }
        }
    }

    /**
     * 图片裁剪
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        if (uri == null) {
            LogInfo.log("haitian", "The uri is not exist.");
            return;
        }
        ClipHeadActivity.launchActivity(MyUserInfoActivity.this, PictureHelper.getPath(this, uri), PHOTO_CUT_RESULT);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
    */
    private void setPicToView(Intent picdata) {
        String bitMapPath = picdata.getStringExtra("bitMapPath");
        if(!TextUtils.isEmpty(bitMapPath)){
            headPath = bitMapPath;
            LogInfo.log("haitian", "bitMapPath s =" + bitMapPath);
            mRootView.loading(true);
            userHeadIv.setImageBitmap(CommonCoreUtil.getImage(bitMapPath));
            Map<String, File> fileMap = new HashMap<String, File>();
            fileMap.put("filename", new File(bitMapPath));
            uploadAsyncTask = YanxiuHttpApi.requestUploadFile(fileMap, new YanxiuHttpApi.UploadFileListener() {
                @Override
                public void onFail(YanxiuBaseBean bean) {
                    mRootView.finish();
                    if(bean == null){
                        Util.showToast(R.string.upload_head_img_failed);
                    } else {
                        if(!TextUtils.isEmpty(((UploadFileBean)bean).getStatus().getDesc())) {
                            Util.showToast(((UploadFileBean) bean).getStatus().getDesc());
                        } else {
                            Util.showToast(R.string.upload_head_img_failed);
                        }
                    }
                }

                @Override
                public void onSuccess(YanxiuBaseBean bean) {
                    mRootView.finish();
                    if(bean == null){
                        Util.showToast(R.string.upload_head_img_succeed);
                    } else {
                        if(!TextUtils.isEmpty(((UploadFileBean) bean).getStatus().getDesc())) {
                            if(((UploadFileBean) bean).getData() != null && ((UploadFileBean) bean).getData().size() > 0){
                                String headUrl = ((UploadFileBean) bean).getData().get(0).getHead();
                                LoginModel.getUserinfoEntity().setHead(headUrl);
                                LoginModel.savaCacheData();
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

    private void setGenderTxt(int type) {
        if (type == YanXiuConstant.Gender.GENDER_TYPE_MALE) {
            gendeyContent.setText(R.string.male_txt);
        } else if (type == YanXiuConstant.Gender.GENDER_TYPE_FEMALE) {
            gendeyContent.setText(R.string.female_txt);
        } else {
            gendeyContent.setText(R.string.sex_unknown_txt);
        }
    }

    protected void initProvinceDatas() {
        provinceList = null;
        provinceList = new ArrayList<ProvinceModel>();
        AssetManager asset = this.getAssets();
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
        } finally {

        }
    }
}
