package com.yanxiu.gphone.student.view.picsel;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.BasePopupWindow;
import com.common.core.utils.BitmapUtil;
import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.utils.PictureHelper;
import com.common.core.utils.StringUtils;
import com.common.core.utils.imageloader.RotateImageViewAware;
import com.common.core.utils.imageloader.UniversalImageLoadTool;
import com.common.core.view.roundview.RoundedImageView;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.LocalPhotoViewActivity;
import com.yanxiu.gphone.student.activity.takephoto.CameraActivity;
import com.yanxiu.gphone.student.jump.utils.ActivityJumpUtils;
import com.yanxiu.gphone.student.utils.MediaUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.view.picsel.utils.ShareBitmapUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/28.
 */
public class PicSelView extends RelativeLayout {
    private static final String TAG = PicSelView.class.getSimpleName();
    private NoScrollGridView noScrollGridView;
    private final int MAX_PIC_SIZE = 9;//最大图片显示个数
    private String currentQuestionId;    //当前题目的id 用来跟上传的图片绑定
    private GridAdapter gridAdapter;
    private BasePopupWindow pop;
    private final int updateCode = 0;
    private final int failCode = 1;
    private Context mContext;
    private TextView addAnswerView;
    private List<String> currentDrrList;
    private Fragment fragment;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case updateCode:
                    //获得回调的数据更新PicSelView
                    updateUI();
                    break;
                case failCode:
                    Util.showToast(getResources().getString(R.string.loading_fail));
                    break;
            }
        }
    };


    private void updateUI() {
        if (gridAdapter != null) {
            if (currentDrrList != null) {
                if(currentDrrList.size()==0){
                    controllShowGridAndHideAddAnswView(false);
                }else{
                    controllShowGridAndHideAddAnswView(true);
                }
                //TODO 临时解决  如果只执行Notify会导致子View宽高无法确认而收缩
                noScrollGridView.setAdapter(gridAdapter);
            }

        }

    }

    public PicSelView(Context context) {
        super(context);
        initView(context);
    }

    public PicSelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }


    public PicSelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PicSelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        LogInfo.log(TAG, "initView");
        mContext = context;
        inflate(context, R.layout.pic_sel_view_layout, this);
        addAnswerView=(TextView)findViewById(R.id.addAnswerView);
        addAnswerView.setOnClickListener(addAnswerViewListener);
        noScrollGridView = (NoScrollGridView) findViewById(R.id.noScrollGrid);
        noScrollGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridAdapter = new GridAdapter(context);
        noScrollGridView.setAdapter(gridAdapter);
        noScrollGridView.setOnItemClickListener(onItemClickListener);
        pop = new PicSelPopup(context);
        controllShowGridAndHideAddAnswView(false);
    }

    private OnClickListener addAnswerViewListener=new OnClickListener() {
        @Override
        public void onClick(View view) {
            showPicSelPop(view);
        }
    };

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (ShareBitmapUtils.getInstance().getDrrMaps().get(ShareBitmapUtils.getInstance().getCurrentSbId()) == null || i == currentDrrList.size()) {
                if (ShareBitmapUtils.getInstance().getCurrentSbId() == null) {
                    LogInfo.log(TAG, "ShareBitmapUtils.getInstance().getCurrentSbId()==null");
                    return;
                }
                if (i == ShareBitmapUtils.MAX_SEL_SIZE){
                    return;
                }
                showPicSelPop(view);
            } else {
                if (currentDrrList == null) {
                    return;
                }
                ShareBitmapUtils.getInstance().setCurrentSbId(currentQuestionId);
                ActivityJumpUtils.jumpToLocalPhotoViewActivityForResult((Activity) mContext, i, LocalPhotoViewActivity.REQUEST_CODE);
            }
        }
    };

    private void showPicSelPop(View view){
        /*pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);*/
        if(!TextUtils.isEmpty(currentQuestionId))
            ShareBitmapUtils.getInstance().setCurrentSbId(currentQuestionId);
        if (CommonCoreUtil.sdCardMounted()) {
            String path=MediaUtils.getOutputMediaFileUri(true).toString();
            if(StringUtils.isEmpty(path)){
                return;
            }
            MediaUtils.openLocalCamera(((Activity) mContext), path, MediaUtils.OPEN_DEFINE_PIC_BUILD);
        }
    }

    private void controllShowGridAndHideAddAnswView(boolean isShowGrid){
        if(isShowGrid){
            addAnswerView.setVisibility(View.GONE);
            noScrollGridView.setVisibility(View.VISIBLE);
        }else{
            addAnswerView.setVisibility(View.VISIBLE);
            noScrollGridView.setVisibility(View.GONE);
        }
    }

    public void upDate(Activity activity,int type, String id) {
        switch (type){
            case LocalPhotoViewActivity.REQUEST_CODE:
                handler.sendEmptyMessage(updateCode);
                break;
            case MediaUtils.OPEN_SYSTEM_CAMERA:
                handlerCameraBit(activity,id);
                break;
            case MediaUtils.OPEN_DEFINE_PIC_BUILD :
                currentDrrList = ShareBitmapUtils.getInstance().getDrrMaps().get(id);
                handler.sendEmptyMessage(updateCode);
                break;
        }
    }

    public void updateImage(String id){
        currentDrrList = ShareBitmapUtils.getInstance().getDrrMaps().get(id);
        handler.sendEmptyMessage(updateCode);
    }
    private void handlerCameraBit(Activity activity,String id ) {
        if(ShareBitmapUtils.getInstance().getDrrMaps().get(id)!=null){
            Uri uri=MediaUtils.getOutputMediaFileUri(false);
            String path = null;
            if(uri!=null){
                path= PictureHelper.getPath(mContext,
                        uri);
                LogInfo.log(TAG, "111path"+path);
            }
            if(path==null){
                return;
            }

            Bitmap bitmap = null;
            try {
                bitmap = BitmapUtil.revitionImageSize(path);
                BitmapUtil.reviewPicRotate(bitmap, path, true);
                if(bitmap != null && !bitmap.isRecycled()){
                    bitmap.recycle();
                }
                //在此处进行裁剪
                MediaUtils.cropImage(activity,uri,MediaUtils.IMAGE_CROP,MediaUtils.FROM_CAMERA);
//                ShareBitmapUtils.getInstance().addPath(id, path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 切换到当前主观题答案List 刷新主观题缩略图
     */
    public void changeData() {
        handler.sendEmptyMessage(updateCode);
    }

    public void setSubjectiveId(String id) {
        if (StringUtils.isEmpty(id)) {
            return;
        }
        currentQuestionId=id;
        if (ShareBitmapUtils.getInstance().getDrrMaps().get(id) == null) {

            currentDrrList = new ArrayList<>();
            ShareBitmapUtils.getInstance().getDrrMaps().put(id, currentDrrList);
        } else {
            currentDrrList = ShareBitmapUtils.getInstance().getDrrMaps().get(id);
        }

        if (ShareBitmapUtils.getInstance().getListIndexMaps().get(id) == null) {
            ShareBitmapUtils.getInstance().getListIndexMaps().put(id, 0);
        }

    }

    public void setFragment(Fragment fragment){
        this.fragment=fragment;
    }

    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private Context context;

        public GridAdapter(Context context) {
            this.context=context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return currentDrrList == null ? 1 : currentDrrList.size() + 1;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.no_scroll_grid_item,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (RoundedImageView) convertView
                        .findViewById(R.id.item_grida_image);
                holder.image.setCornerRadius(getResources().getDimensionPixelOffset(R.dimen.dimen_10));
                holder.decorateImage=(ImageView)convertView.findViewById(R.id.item_grid_decorate);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (currentDrrList == null || currentDrrList.size() == 0 || position == currentDrrList.size()) {
                holder.decorateImage.setBackgroundResource(0);
                holder.image.setBackgroundResource(R.drawable.add_pic_selector);
                if (position == MAX_PIC_SIZE) {
                    holder.image.setVisibility(View.GONE);
                } else {
                    holder.image.setVisibility(View.VISIBLE);
                }
            } else {
                holder.decorateImage.setBackgroundResource(R.drawable.upload_pic);
                String absolutePath = currentDrrList.get(position);
                UniversalImageLoadTool.disPlay("file://" + absolutePath,
                        new RotateImageViewAware(holder.image, absolutePath), R.drawable.image_default);
            }

            return convertView;
        }

        public class ViewHolder {
            public RoundedImageView image;
            public ImageView decorateImage;

        }

    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public static void resetAllData() {

        ShareBitmapUtils.getInstance().resetParams();
    }

}
