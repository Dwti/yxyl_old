package com.yanxiu.gphone.parent.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.BasePopupWindow;
import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.utils.PictureHelper;
import com.common.core.utils.imageloader.RotateImageViewAware;
import com.common.core.utils.imageloader.UniversalImageLoadTool;
import com.common.core.view.roundview.RoundedImageView;
import com.common.login.LoginModel;
import com.common.login.model.ParentInfo;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.activity.base.TopViewBaseActivity;
import com.yanxiu.gphone.parent.bean.ParentDistrictModel;
import com.yanxiu.gphone.parent.bean.ParentHeaderEventBusModel;
import com.yanxiu.gphone.parent.bean.ParentNameEventBusModel;
import com.yanxiu.gphone.parent.bean.ParentProvinceModel;
import com.yanxiu.gphone.parent.bean.ParentUploadFileBean;
import com.yanxiu.gphone.parent.httpApi.YanxiuParentHttpApi;
import com.yanxiu.gphone.parent.utils.ParentMediaUtils;
import com.yanxiu.gphone.parent.utils.ParentUtils;
import com.yanxiu.gphone.parent.utils.ParentXmlParserHandler;
import com.yanxiu.gphone.parent.utils.PublicLoadUtils;
import com.yanxiu.gphone.parent.view.ParentSystemPicSelPop;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import de.greenrobot.event.EventBus;

/**
 * Created by lidongming on 16/3/18.
 * 家长端-------设置家长的详情界面
 */
public class ParentInfomationActivity extends TopViewBaseActivity{

    private final static int PHOTO_CUT_RESULT = 0x204;

    private RelativeLayout rlParentName, rlParentLocation, rlParentHeader;


    private TextView tvParentLocation;

    //家长头像
    private RoundedImageView userHeadIv;

    private String headUrl;

    private ArrayList<ParentProvinceModel> provinceList;

    private BasePopupWindow pop;

    private TextView tvParentInfoName;

    private Uri fileUri;

    private String headPath = null;


    /**
     * 上传任务
     * */
    private AsyncTask uploadAsyncTask;



    public static void launch(Activity context) {
        Intent intent = new Intent(context, ParentInfomationActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }



    @Override
    protected View getContentView() {
        mPublicLayout = PublicLoadUtils.createPage(this, R.layout.parent_information_fragment);
        mPublicLayout.finish();
        initView();
        initData();

        return mPublicLayout;
    }

    private void initView(){

        rlParentName = (RelativeLayout) mPublicLayout.findViewById(R.id.parent_name_layout);
        rlParentLocation = (RelativeLayout) mPublicLayout.findViewById(R.id.parent_location_layout);

        rlParentHeader = (RelativeLayout) mPublicLayout.findViewById(R.id.personal_information_layout);

        tvParentLocation = (TextView) mPublicLayout.findViewById(R.id.tv_parent_location);

        userHeadIv = (RoundedImageView) mPublicLayout.findViewById(R.id.iv_header);

        tvParentInfoName = (TextView) mPublicLayout.findViewById(R.id.tv_parent_info_name);
        pop = new ParentSystemPicSelPop(this);
    }

    private void initData(){
        titleText.setText(this.getResources().getString(R.string.parent_information));

        initProvinceDatas();

        if(LoginModel.getRoleUserInfoEntity() != null){
            headUrl = ((ParentInfo)LoginModel.getRoleUserInfoEntity()).getHead();

            if(TextUtils.isEmpty(((ParentInfo) LoginModel.getRoleUserInfoEntity()).getProvinceidName())){
                tvParentLocation.setText(this.getResources().getString(R.string.user_unknow_str));
            }else{
                StringBuilder sb = new StringBuilder();
                sb.append(((ParentInfo) LoginModel.getRoleUserInfoEntity()).getProvinceidName());
                sb.append("/");
                sb.append(((ParentInfo)LoginModel.getRoleUserInfoEntity()).getCityidName());
                sb.append("/");
                sb.append(((ParentInfo)LoginModel.getRoleUserInfoEntity()).getAreaidName());
                tvParentLocation.setText(sb.toString());
            }

            String parentName = ((ParentInfo)LoginModel.getRoleUserInfoEntity()).getRealname();
            if(TextUtils.isEmpty(parentName)){
                tvParentInfoName.setText(this.getResources().getString(R.string.user_name_txt));
            }else{
                tvParentInfoName.setText(parentName);
            }

        }else{
            tvParentInfoName.setText(this.getResources().getString(R.string.user_name_txt));
            tvParentLocation.setText(this.getResources().getString(R.string.user_unknow_str));
        }

        if (!TextUtils.isEmpty(headUrl)) {
            UniversalImageLoadTool.disPlay(headUrl,
                    new RotateImageViewAware(userHeadIv, headUrl),
                    R.drawable.parent_hearder_default);
        } else {
            UniversalImageLoadTool.disPlay("", new RotateImageViewAware(userHeadIv, ""), R.drawable.parent_hearder_default);
        }

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

    @Override
    protected void setContentListener() {
        rlParentName.setOnClickListener(this);
        rlParentLocation.setOnClickListener(this);
        rlParentHeader.setOnClickListener(this);
    }


    protected void initProvinceDatas() {
        provinceList = null;
        provinceList = new ArrayList<>();
        AssetManager asset = this.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            ParentXmlParserHandler handler = new ParentXmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList.addAll(handler.getDataList());
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }


    public void onEventMainThread(ParentDistrictModel districtModel) {
        if (districtModel != null) {
            String areaContent = districtModel.getProvinceName() + "/" + districtModel.getCityName() + "/" + districtModel.getName();
            LogInfo.log("haitian", "districtModel.toString=" + areaContent);
            tvParentLocation.setText(areaContent);
        }
    }


    public void onEventMainThread(ParentNameEventBusModel parentNameEventBusModel) {
        if (parentNameEventBusModel != null) {
            String areaContent = parentNameEventBusModel.getName();
            tvParentInfoName.setText(areaContent);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        mUserinfoEntity = LoginModel.getUserinfoEntity();
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ParentMediaUtils.OPEN_SYSTEM_CAMERA:
                    fileUri = ParentMediaUtils.getOutputMediaFileUri(false);
                    if(fileUri!=null){
                        LogInfo.log("geny"," MediaUtils.OPEN_SYSTEM_CAMERA fileUri"+fileUri);
                        startPhotoZoom(fileUri);
                    }else{
                        ParentUtils.showToast(getResources().getString(R.string.loading_fail));
                    }

                    break;
                case PHOTO_CUT_RESULT:
                    LogInfo.log("geny", "PHOTO_CUT_RESULT ----- =" + PHOTO_CUT_RESULT);
                    if (data != null) {
                        setPicToView(data);
                    }else{
                        LogInfo.log("geny", "PHOTO_CUT_RESULT data = null" + PHOTO_CUT_RESULT);
                    }
                    break;
                case ParentMediaUtils.OPEN_SYSTEM_PIC_BUILD_KITKAT:
                case ParentMediaUtils.OPEN_SYSTEM_PIC_BUILD:
                    Uri imageFileUri = data.getData();//获取选择图片的URI
                    startPhotoZoom(imageFileUri);
                    break;
            }
        }
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        mPublicLayout.loading(true);
        final String bitMapPath = picdata.getStringExtra("bitMapPath");
        if(!TextUtils.isEmpty(bitMapPath)){
            headPath = bitMapPath;
            LogInfo.log("geny", "bitMapPath s =" + bitMapPath);
//            mRootView.loading(true);
            Map<String, File> fileMap = new HashMap<String, File>();
            fileMap.put("imgFile", new File(bitMapPath));
            uploadAsyncTask = YanxiuParentHttpApi.requestUploadFile(fileMap, new YanxiuParentHttpApi.UploadFileListener() {
                @Override
                public void onFail(YanxiuBaseBean bean) {
                    mPublicLayout.finish();
                    if (bean == null) {
                        ParentUtils.showToast(R.string.upload_head_img_failed);
                    } else {
                        if(!TextUtils.isEmpty(((ParentUploadFileBean) bean).getStatus().getDesc())){
                            ParentUtils.showToast(((ParentUploadFileBean) bean).getStatus().getDesc());
                        }else{
                            ParentUtils.showToast(R.string.upload_head_img_failed);
                        }
                    }
                }

                @Override
                public void onSuccess(YanxiuBaseBean bean) {
                    mPublicLayout.finish();

                    if (((ParentUploadFileBean) bean).getData() != null && !((ParentUploadFileBean) bean).getData().isEmpty()) {
                        ParentUtils.showToast(R.string.upload_head_img_succeed);
                        String headUrl = ((ParentUploadFileBean) bean).getData().get(0).getHead();
                        if(!TextUtils.isEmpty(headUrl)){
                            LogInfo.log("geny", "headUrl.toString=" + headUrl);
                        }
                        userHeadIv.setImageBitmap(CommonCoreUtil.getImage(bitMapPath));
                        ParentHeaderEventBusModel parentHeaderEventBusModel = new ParentHeaderEventBusModel();
                        parentHeaderEventBusModel.setHeaderPath(bitMapPath);
                        EventBus.getDefault().post(parentHeaderEventBusModel);
                        ((ParentInfo)LoginModel.getRoleUserInfoEntity()).setHead(headUrl);
                        LoginModel.savaCacheData();

                    } else {
                        if(!TextUtils.isEmpty(((ParentUploadFileBean) bean).getStatus().getDesc())){
                            ParentUtils.showToast(((ParentUploadFileBean) bean).getStatus().getDesc());
                        }else{
                            ParentUtils.showToast(R.string.upload_head_img_failed);
                        }
                    }
                }

                @Override
                public void onProgress(int progress) {

                }
            });
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
        ParentClipHeadActivity.launchActivity(this, PictureHelper.getPath(this, uri), PHOTO_CUT_RESULT);
    }

    @Override
    protected void destoryData() {

    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view == rlParentName){
            ParentModifyNameActivity.launch(this);
        }else if(view == rlParentLocation){
            ParentLocationSelectActivity.launch(this, provinceList, ParentLocationSelectActivity.LOCATION_CONSTANT_PROVINCE);
        }else if(view == rlParentHeader){
            this.pop.showAtLocation(view, Gravity.BOTTOM,0,0);
        }
    }

    @Override
    protected void initLaunchIntentData() {

    }

    @Override
    protected boolean isAttach() {
        return false;
    }


}
