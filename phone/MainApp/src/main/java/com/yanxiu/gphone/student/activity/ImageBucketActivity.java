package com.yanxiu.gphone.student.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.ImageBucketAdapter;
import com.yanxiu.gphone.student.jump.ImagePicSelJumpBackModel;
import com.yanxiu.gphone.student.jump.utils.ActivityJumpUtils;
import com.yanxiu.gphone.student.view.picsel.utils.AlbumHelper;
import com.yanxiu.gphone.student.view.picsel.utils.ShareBitmapUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * 相册目录
 * Created by Administrator on 2015/9/28.
 */
public class ImageBucketActivity extends TopViewBaseActivity {
    private GridView mGridView;
    private ImageBucketAdapter adapter;
    public static List<String> mTempDrrList;
    private static final String TAG=ImageBucketActivity.class.getSimpleName();

    @Override
    protected boolean isAttach() {
        return false;
    }

    @Override
    protected View getContentView() {
        mTempDrrList= new ArrayList<>();
        rightView.setVisibility(View.GONE);
        titleText.setText(getResources().getString(R.string.pic_bucket));
        mGridView=(GridView)View.inflate(this,R.layout.image_bucket_activity,null);
        mGridView.setBackgroundColor(getResources().getColor(R.color.color_008080));
        adapter=new ImageBucketAdapter(this);
        mGridView.setAdapter(adapter);

        try {
            initData();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //动态计算最大值并初始化相册选择计数
        ShareBitmapUtils.getInstance().recordBucketPicSelNums=0;
        if(ShareBitmapUtils.getInstance().getDrrMaps()==null){
            LogInfo.log(TAG, "ShareBitmapUtils.getInstance().getBitmapMaps()==null");
        }
        if(ShareBitmapUtils.getInstance().getCurrentSbId()==null){
            LogInfo.log(TAG,"ShareBitmapUtils.getInstance().getCurrentSbId()==null");
        }
        ShareBitmapUtils.getInstance().countMax =ShareBitmapUtils.MAX_SEL_SIZE-ShareBitmapUtils.getInstance().getDrrMaps().get(ShareBitmapUtils.getInstance
                ().getCurrentSbId()).size();

        return mGridView;
    }

    @Override
    protected void setRootView() {
        super.setRootView();
        rootView.setBackgroundResource(0);
    }

    @Override
    protected void setTopView() {
        super.setTopView();
        RelativeLayout.LayoutParams topRootViewParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommonCoreUtil.dipToPx(this, 44));
        topRootView.setLayoutParams(topRootViewParams);
        topRootView.setBackgroundColor(getResources().getColor(R.color.color_00cccc));
    }


    @Override
    protected void setContentContainerView() {
        super.setContentContainerView();
        RelativeLayout.LayoutParams contentParams= (RelativeLayout.LayoutParams) contentContainer.getLayoutParams();
        contentParams.leftMargin=0;
        contentParams.rightMargin=0;
        contentContainer.setLayoutParams(contentParams);
        contentContainer.setPadding(0, 0, 0, 0);
        contentContainer.setBackgroundResource(0);
        contentContainer.setBackgroundColor(getResources().getColor(R.color.color_008080));
    }





    private void initData() throws ParseException {
        AlbumHelper helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());
        if(ShareBitmapUtils.getInstance().getDataList().size()>0){
            ShareBitmapUtils.getInstance().getDataList().clear();
        }
        helper.resetParmas();
        ShareBitmapUtils.getInstance().setDataList(helper.getImagesBucketList(false));
        if(ShareBitmapUtils.getInstance().getDataList()!=null && ShareBitmapUtils.getInstance().getDataList().size()>0){
            adapter.setList(ShareBitmapUtils.getInstance().getDataList());
        }
    }

    @Override
    protected void setContentListener() {
        mGridView.setOnItemClickListener(onItemClickListener);
    }
    private final AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if(ShareBitmapUtils.getInstance().getDataList()!=null&&ShareBitmapUtils.getInstance().getDataList().size()>0){
                ActivityJumpUtils.jumpToImagePicSelActivityForResult(ImageBucketActivity.this, i, ImagePicSelActivity.REQUEST_CODE);
            }
        }
    };



    @Override
    protected void destoryData() {
        if(mTempDrrList!=null){
            mTempDrrList.clear();
            mTempDrrList=null;
        }


    }

    @Override
    protected void initLaunchIntentData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case ImagePicSelActivity.REQUEST_CODE:
                    ImagePicSelJumpBackModel jumpBackModel= (ImagePicSelJumpBackModel) getBaseJumpModeFromSetResult(data);
                    if(jumpBackModel==null){
                        return;
                    }
                    boolean isAddList=jumpBackModel.isAddList();
                    if(isAddList){
                        ActivityJumpUtils.jumpBackFromImageBucketActivity(this, RESULT_OK);
//                        ShareBitmapUtils.getInstance().addAllPath(ShareBitmapUtils.getInstance().getCurrentSbId(),mTempDrrList);
                        executeFinish();
                    }else{
                        ActivityJumpUtils.jumpBackFromImageBucketActivity(this, RESULT_CANCELED);
                    }
                    break;
            }
        }
    }
}
