package com.yanxiu.gphone.student.activity;

import android.content.Intent;
import android.net.Uri;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.ImagePicSelAdapter;
import com.yanxiu.gphone.student.inter.CorpFinishListener;
import com.yanxiu.gphone.student.jump.ImagePicSelJumpModel;
import com.yanxiu.gphone.student.jump.utils.ActivityJumpUtils;
import com.yanxiu.gphone.student.utils.CorpUtils;
import com.yanxiu.gphone.student.utils.MediaUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.view.picsel.bean.ImageItem;
import com.yanxiu.gphone.student.view.picsel.inter.PicNumListener;
import com.yanxiu.gphone.student.view.picsel.utils.ShareBitmapUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择
 * Created by Administrator on 2015/9/29.
 *
 *
 * //
 //        if(drr!=null){
 //            picSelText.setText(String.format(getResources().getString(R.string.has_sel), ""+drr.size()));
 //        }
 */
public class ImagePicSelActivity extends  TopViewBaseActivity implements PicNumListener, CorpFinishListener {
    private static final String TAG=ImagePicSelActivity.class.getSimpleName();
    private GridView gridView;
    private TextView picSelText;
    private TextView doneText;
    private int bucketPos;//目录索引
    private boolean isAddList;
    private boolean isAttachMax=false;//已经达到最大值
    public final static int REQUEST_CODE=0X00;
    private ImagePicSelAdapter adapter;

    @Override
    protected boolean isAttach() {
        return false;
    }

    @Override
    protected View getContentView() {
        View view=View.inflate(this,R.layout.image_pic_sel,null);
        rightText.setVisibility(View.INVISIBLE);
        titleText.setText(getResources().getString(R.string.local_bucket));
        gridView=(GridView)view.findViewById(R.id.gridview);
        gridView.setBackgroundColor(getResources().getColor(R.color.color_008080));

        picSelText=(TextView)view.findViewById(R.id.picSelText);
        TextPaint picTextPaint=picSelText.getPaint();
        picTextPaint.setFakeBoldText(true);
        picSelText.setShadowLayer(2F, 0F, 4F, getResources().getColor(R.color.color_005959));
        doneText=(TextView)view.findViewById(R.id.doneText);
        doneText.setText(R.string.done);
        doneText.getPaint().setFakeBoldText(true);
        doneText.setShadowLayer(2F, 0F, 4F, getResources().getColor(R.color.color_005959));

        picSelText.setText(String.format(getResources().getString(R.string.has_sel), "" + ShareBitmapUtils.getInstance().getRecordBucketPicSelNums()));
        initData();
        CorpUtils.getInstence().AddFinishListener(this);
        return view;
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

    private void initData() {
        if(ShareBitmapUtils.getInstance().getDataList()==null||ShareBitmapUtils.getInstance().getDataList().size()==0){
            return;
        }
        List<ImageItem> tempList = new ArrayList<>();
        if (bucketPos != 0) {
            tempList = ShareBitmapUtils.getInstance().getDataList().get(bucketPos).getImageList();
        } else {
            for (int i=0; i<ShareBitmapUtils.getInstance().getDataList().size(); i++) {
                tempList.addAll(ShareBitmapUtils.getInstance().getDataList().get(i).getImageList());
            }
        }

        final List<ImageItem> imageList = tempList;
        if(imageList ==null){
            return;
        }
        adapter = new ImagePicSelAdapter(this);
        gridView.setAdapter(adapter);
        adapter.setPicNumListener(this);
        adapter.setList(imageList);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView iv_decorate = (ImageView) view.findViewById(R.id.imageDecorate);
                ImageItem imageItem = imageList.get(position);
                imageItem.setIsSelected(!imageItem.isSelected());
                iv_decorate.setSelected(imageItem.isSelected());
                if(imageItem.isSelected()){
                    ImageBucketActivity.mSelectedImagePath = imageItem.getImagePath();
                    MediaUtils.setPic_select_string(imageItem.getImagePath());
                }
                LogInfo.log("tttttttttt", ImageBucketActivity.mSelectedImagePath);
                if( adapter.lastSelectedPosition!=position && imageList.get(adapter.lastSelectedPosition).isSelected()){
                    imageList.get(adapter.lastSelectedPosition).setIsSelected(false);
                }
                adapter.lastSelectedPosition = position;
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void setContentListener() {
        doneText.setOnClickListener(this);
        rightText.setOnClickListener(this);
        leftView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.doneText:
                isAddList=true;
                if(!TextUtils.isEmpty(ImageBucketActivity.mSelectedImagePath))
                    MediaUtils.cropImage(ImagePicSelActivity.this, Uri.fromFile(new File(ImageBucketActivity.mSelectedImagePath)),MediaUtils.IMAGE_CROP,MediaUtils.FROM_PICTURE);
                destoryData();
                break;
            case R.id.pub_top_left:
                ActivityJumpUtils.jumpToImageBucketActivityForResult(ImagePicSelActivity.this, MediaUtils.OPEN_SYSTEM_PIC_BUILD_CAMERA);
                this.finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void destoryData() {
        ActivityJumpUtils.jumpBackFromImagePicSelActivity(this,isAddList,RESULT_OK);
    }

    @Override
    protected void initLaunchIntentData() {
        ImagePicSelJumpModel jumpModel= (ImagePicSelJumpModel) getBaseJumpModel();
        if(jumpModel==null){
            return;
        }
        bucketPos=jumpModel.getSelPos();
    }

    @Override
    public void numCountCallBack(int count) {
        LogInfo.log(TAG, "numCountCallBack: " + count);
        if(count<ShareBitmapUtils.getInstance().getCountMax()){
            isAttachMax=false;
        }

        if(isAttachMax){
            Util.showToast(String.format(getResources().getString(R.string.max_sel_tips), "" + ShareBitmapUtils.getInstance().getCountMax()));
            return;
        }
        if(count==ShareBitmapUtils.getInstance().getCountMax()){
            isAttachMax=true;
            picSelText.setTextColor(getResources().getColor(R.color.color_ffdb4d));
            picSelText.setText(String.format(getResources().getString(R.string.has_sel),""+ShareBitmapUtils.getInstance().getCountMax()));
            return;
        }
        isAttachMax=false;
        picSelText.setText(String.format(getResources().getString(R.string.has_sel), ""+count));
        if(count<=0){
            doneText.setTextColor(getResources().getColor(R.color.color_00e6e6));
            picSelText.setTextColor(getResources().getColor(R.color.color_00e6e6));
        }else{
            picSelText.setTextColor(getResources().getColor(R.color.color_ffdb4d));
            doneText.setTextColor(getResources().getColor(R.color.color_ffdb4d));
        }
    }

    @Override
    public void onfinish() {
        this.finish();
    }
}
