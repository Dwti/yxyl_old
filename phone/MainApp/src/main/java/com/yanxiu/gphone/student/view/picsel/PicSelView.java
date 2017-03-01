package com.yanxiu.gphone.student.view.picsel;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
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

import com.bumptech.glide.Glide;
import com.common.core.utils.BitmapUtil;
import com.common.core.utils.LogInfo;
import com.common.core.utils.PictureHelper;
import com.common.core.utils.StringUtils;
import com.common.core.view.roundview.RoundedImageView;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.activity.LocalPhotoViewActivity;
import com.yanxiu.gphone.student.inter.CorpListener;
import com.yanxiu.gphone.student.jump.utils.ActivityJumpUtils;
import com.yanxiu.gphone.student.utils.CorpUtils;
import com.yanxiu.gphone.student.utils.MediaUtils;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;
import com.yanxiu.gphone.student.view.picsel.utils.ShareBitmapUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/9/28.
 */
public class PicSelView extends RelativeLayout {
    private static final String TAG = PicSelView.class.getSimpleName();
    private NoScrollGridView noScrollGridView;
    private final int MAX_PIC_SIZE = 9;//最大图片显示个数
    private String currentQuestionId;    //当前题目的id 用来跟上传的图片绑定
    private GridAdapter gridAdapter;
    private final int updateCode = 0;
    private final int failCode = 1;
    private final int UIChanged_TRUE=2;
    private final int UIChanged_FAIL=3;
    private Context mContext;
    private TextView addAnswerView;
    private List<String> currentDrrList;
    private CorpListener fragment;

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
                case UIChanged_TRUE:
//                    PicSelView.this.removeView(addAnswerView);
                    addAnswerView.setVisibility(View.GONE);
                    noScrollGridView.setVisibility(View.VISIBLE);
                    break;
                case UIChanged_FAIL:
                    addAnswerView.setVisibility(View.VISIBLE);
                    noScrollGridView.setVisibility(View.GONE);
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
        controllShowGridAndHideAddAnswView(false);
    }

    private OnClickListener addAnswerViewListener=new OnClickListener() {
        @Override
        public void onClick(View view) {
            CorpUtils.getInstence().AddListener(fragment);
            showPicSelPop(view);
        }
    };

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            CorpUtils.getInstence().AddListener(fragment);
            if (currentDrrList == null || i == currentDrrList.size()) {
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
//                EventBus.getDefault().register(fragment);
                YanXiuConstant.index_position=YanXiuConstant.catch_position;
                ShareBitmapUtils.getInstance().setCurrentSbId(currentQuestionId);
                ActivityJumpUtils.jumpToLocalPhotoViewActivityForResult((Activity) mContext, i, LocalPhotoViewActivity.REQUEST_CODE);
            }
        }
    };

    private void showPicSelPop(View view){
        if(!TextUtils.isEmpty(currentQuestionId))
            ShareBitmapUtils.getInstance().setCurrentSbId(currentQuestionId);
        EventBus.getDefault().unregister(fragment);
        YanXiuConstant.index_position=YanXiuConstant.catch_position;
        EventBus.getDefault().register(fragment);
        MediaUtils.openLocalCamera(((Activity) mContext), MediaUtils.CAPATURE_AND_CROP);
    }

    private void controllShowGridAndHideAddAnswView(boolean isShowGrid){
        if(isShowGrid){
            handler.sendEmptyMessage(UIChanged_TRUE);
        }else{
            handler.sendEmptyMessage(UIChanged_FAIL);
        }
    }

    public void upDate(Activity activity,int type, String id) {
        switch (type){
            case LocalPhotoViewActivity.REQUEST_CODE:
                handler.sendEmptyMessage(updateCode);
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

    /**
     * 切换到当前主观题答案List 刷新主观题缩略图
     */
    public void changeData() {
        handler.sendEmptyMessage(updateCode);
    }

    public void setSubjectiveId(String id,List<String> list) {
        if (StringUtils.isEmpty(id)) {
            return;
        }
        currentQuestionId=id;
        if (ShareBitmapUtils.getInstance().getDrrMaps().get(id) == null) {

            currentDrrList = new ArrayList<>();
            currentDrrList.addAll(list);
            ShareBitmapUtils.getInstance().getDrrMaps().put(id, currentDrrList);
        } else {
            currentDrrList = ShareBitmapUtils.getInstance().getDrrMaps().get(id);
        }

        if (ShareBitmapUtils.getInstance().getListIndexMaps().get(id) == null) {
            ShareBitmapUtils.getInstance().getListIndexMaps().put(id, 0);
        }

    }

    public void setFragment(CorpListener listener){
        this.fragment=listener;
    }

    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private Context context;
        private int imgWidth;

        public GridAdapter(Context context) {
            this.context=context;
            inflater = LayoutInflater.from(context);
            imgWidth = Util.convertDpToPx(context,35);
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

            ViewHolder holder;
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
                if (position >= MAX_PIC_SIZE) {
                    holder.image.setVisibility(View.GONE);
                } else {
                    holder.image.setVisibility(View.VISIBLE);
                }
            } else {
                holder.decorateImage.setBackgroundResource(R.drawable.upload_pic);
                String absolutePath = currentDrrList.get(position);
                Glide.with(context)
                        .load(absolutePath).asBitmap()
                        .placeholder(R.drawable.image_default)
                        .error(R.drawable.image_default)
                        .override(imgWidth,imgWidth).into(holder.image);
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
