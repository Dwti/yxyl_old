package com.yanxiu.gphone.hd.student.activity;

import android.content.res.Configuration;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.common.core.utils.BasePopupWindow;
import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.adapter.LocalPhotoViewAdapter;
import com.yanxiu.gphone.hd.student.jump.LocalPhotoViewJumpModel;
import com.yanxiu.gphone.hd.student.jump.utils.ActivityJumpUtils;
import com.yanxiu.gphone.hd.student.view.DefineViewPager;
import com.yanxiu.gphone.hd.student.view.picsel.PicDelPopup;
import com.yanxiu.gphone.hd.student.view.picsel.utils.ShareBitmapUtils;


/**
 * 查看本地选择图片
 * Created by Administrator on 2015/10/10.
 */
public class LocalPhotoViewActivity extends TopViewBaseActivity implements View.OnClickListener {
    private final static String TAG=LocalPhotoViewActivity.class.getSimpleName();
    private DefineViewPager mViewPager;
    private LocalPhotoViewAdapter adapter;
    public static final int REQUEST_CODE=0x123;
    private int curPos;
    private BasePopupWindow pop;

    @Override
    protected View getContentView() {
        View view=getAttachView(R.layout.activity_photo_local_view);
        initView(view);
        return view;
    }

    @Override
    protected boolean isAttach() {
        return true;
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

    @Override
    protected void setRootView() {
        super.setRootView();
        rootView.setBackgroundResource(0);

    }

    @Override
    protected void setTopView() {
        RelativeLayout.LayoutParams topRootViewParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommonCoreUtil.dipToPx(this, 44));
        topRootView.setLayoutParams(topRootViewParams);
        topRootView.setBackgroundColor(getResources().getColor(R.color.color_00cccc));
        rightText.setBackgroundResource(R.drawable.icon_del_selector);
        RelativeLayout.LayoutParams rightParams= (RelativeLayout.LayoutParams) rightText.getLayoutParams();
        rightParams.width=getResources().getDimensionPixelOffset(R.dimen.dimen_25);
        rightParams.height=getResources().getDimensionPixelOffset(R.dimen.dimen_25);
        rightText.setLayoutParams(rightParams);
    }


    @Override
    protected void setContentListener() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                curPos = position;
                showPicNums(getHtmlHandlerString());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void destoryData() {
        ActivityJumpUtils.jumpBackFromLocalPhotoViewActivity(this, RESULT_OK);
    }


    private void initView(View view) {
        pop=new PicDelPopup(this);
        pop.setOnItemClickListener(new BasePopupWindow.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (position){
                    case BasePopupWindow.ItemClickListener.TWO:
                        LogInfo.log(TAG, "currentPos: " + curPos);
                        adapter.deleteItem(curPos);
                        showPicNums(getHtmlHandlerString());
                        if(ShareBitmapUtils.getInstance().isCurrentListIsEmpty(ShareBitmapUtils.getInstance().getCurrentSbId())){
                            ShareBitmapUtils.getInstance().delTempDir();
                            executeFinish();
                        }
                        break;

                }
            }
        });

        mViewPager = (DefineViewPager)view.findViewById(R.id.photo_viewpager);
        adapter=new LocalPhotoViewAdapter(this);
        mViewPager.setAdapter(adapter);

        mViewPager.setCurrentItem(curPos, false);
        LogInfo.log(TAG, "Curpos: " + curPos);
        showPicNums(getHtmlHandlerString());
    }

    /**
     * show the pic nums on the top Bar
     * @param text
     */
    private void showPicNums(Spanned text){
        titleText.setText(text);
    }

    private Spanned getHtmlHandlerString(){
        if(adapter.getCount()<=0){
            return Html.fromHtml("<big><strong>"+(curPos)+"</strong></big>"+"/"+adapter.getCount());
        }else{
            return Html.fromHtml("<big><strong>"+(curPos + 1)+"</strong></big>"+"/"+adapter.getCount());
        }

    }



    @Override
    protected void initLaunchIntentData() {
        LocalPhotoViewJumpModel jumpModel= (LocalPhotoViewJumpModel) getBaseJumpModel();
        if(jumpModel==null){
            return;
        }
        curPos=jumpModel.getPos();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.pub_top_right:
                pop.showAtLocation(view, Gravity.BOTTOM,0,0);
                break;
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
