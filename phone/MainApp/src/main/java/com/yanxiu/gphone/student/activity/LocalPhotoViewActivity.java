package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.core.utils.BasePopupWindow;
import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.LocalPhotoViewAdapter;
import com.yanxiu.gphone.student.jump.BaseJumpModel;
import com.yanxiu.gphone.student.jump.LocalPhotoViewJumpModel;
import com.yanxiu.gphone.student.jump.constants.JumpModeConstants;
import com.yanxiu.gphone.student.jump.utils.ActivityJumpUtils;
import com.yanxiu.gphone.student.view.DefineViewPager;
import com.yanxiu.gphone.student.view.picsel.PicDelPopup;
import com.yanxiu.gphone.student.view.picsel.utils.ShareBitmapUtils;

/**
 * 查看本地选择图片
 * Created by Administrator on 2015/10/10.
 */
public class LocalPhotoViewActivity extends Activity implements View.OnClickListener {
    private final static String TAG=LocalPhotoViewActivity.class.getSimpleName();
    private DefineViewPager mViewPager;
    private LocalPhotoViewAdapter adapter;
    public static final int REQUEST_CODE=0x123;
    private int curPos;
    private BasePopupWindow pop;
    private TextView tv_title;
    private ImageView iv_left,iv_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_local_view);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        pop=new PicDelPopup(this);
        iv_left= (ImageView) findViewById(R.id.iv_left);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        iv_right.setVisibility(View.VISIBLE);
        tv_title = (TextView) findViewById(R.id.tv_title);
        mViewPager = (DefineViewPager)findViewById(R.id.photo_viewpager);
        adapter=new LocalPhotoViewAdapter(this);
        mViewPager.setAdapter(adapter);
    }

    private void initData() {
        LocalPhotoViewJumpModel jumpModel= (LocalPhotoViewJumpModel) getIntent()
                .getSerializableExtra(JumpModeConstants.JUMP_MODEL_KEY);
        if(jumpModel==null){
            return;
        }
        curPos=jumpModel.getPos();
        mViewPager.setCurrentItem(curPos, false);
        showPicNums(getHtmlHandlerString());
    }

    private void initListener() {

        iv_left.setOnClickListener(this);
        iv_right.setOnClickListener(this);
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
                            ActivityJumpUtils.jumpBackFromLocalPhotoViewActivity(LocalPhotoViewActivity.this, RESULT_OK);
                            finish();
                        }
                        break;

                }
            }
        });
    }

    private void showPicNums(Spanned text){
        tv_title.setText(text);
    }



    private Spanned getHtmlHandlerString(){
        if(adapter.getCount()<=0){
            return Html.fromHtml("<big><strong>"+(curPos)+"</strong></big>"+"/"+adapter.getCount());
        }else{
            return Html.fromHtml("<big><strong>"+(curPos + 1)+"</strong></big>"+"/"+adapter.getCount());
        }

    }

    @Override
    public void onBackPressed() {
        ActivityJumpUtils.jumpBackFromLocalPhotoViewActivity(LocalPhotoViewActivity.this, RESULT_OK);
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_left:
                ActivityJumpUtils.jumpBackFromLocalPhotoViewActivity(LocalPhotoViewActivity.this, RESULT_OK);
                finish();
                break;
            case R.id.iv_right:
                pop.showAtLocation(view, Gravity.BOTTOM,0,0);
                break;
            default:
                break;
        }

    }


}
