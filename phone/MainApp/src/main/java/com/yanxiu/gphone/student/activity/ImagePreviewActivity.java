package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.core.utils.BasePopupWindow;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.ImagePagerAdapter;
import com.yanxiu.gphone.student.view.picsel.PicDelPopup;

import java.util.ArrayList;

/**
 * Created by sp on 17-3-1.
 */

public class ImagePreviewActivity extends Activity implements View.OnClickListener {
    private Activity mActivity;
    private ViewPager mViewPager;
    private ImagePagerAdapter adapter;
    private int currentItem;
    private BasePopupWindow pop;
    private TextView tv_title;
    private ImageView iv_back, iv_delete;
    private ArrayList<String> paths;
    private boolean canBeDeleted = true;
    public static final String PATH_LIST = "pathList";
    private static final String CURRENT_ITEM = "currentItem";
    private static final String CAN_BE_DELETED = "canBeDeleted";
    public static final int IMAGE_PREVIEW = 0x08;
    private boolean isCountChanged = false;

    public static void lanuch(Activity activity,ArrayList<String> paths, int currentItem, boolean canDelete) {
        Intent intent = new Intent(activity,ImagePreviewActivity.class);
        intent.putStringArrayListExtra(PATH_LIST,paths);
        intent.putExtra(CURRENT_ITEM,currentItem);
        intent.putExtra(CAN_BE_DELETED,canDelete);
        activity.startActivityForResult(intent,IMAGE_PREVIEW);
    }

    public static void lanuch(Fragment fragment, ArrayList<String> paths, int currentItem, boolean canDelete) {
        Intent intent = new Intent(fragment.getActivity(),ImagePreviewActivity.class);
        intent.putStringArrayListExtra(PATH_LIST,paths);
        intent.putExtra(CURRENT_ITEM,currentItem);
        intent.putExtra(CAN_BE_DELETED,canDelete);
        fragment.startActivityForResult(intent,IMAGE_PREVIEW);
    }
    public static void lanuch(Activity activity,ArrayList<String> paths, int currentItem) {
        Intent intent = new Intent(activity,ImagePreviewActivity.class);
        intent.putStringArrayListExtra(PATH_LIST,paths);
        intent.putExtra(CURRENT_ITEM,currentItem);
        activity.startActivityForResult(intent,IMAGE_PREVIEW);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_image_pager);
        mActivity = this;
        initView();
        initData();
        initListener();
    }

    private void initView() {
        pop = new PicDelPopup(mActivity);
        iv_back = (ImageView) findViewById(R.id.iv_left);
        iv_delete = (ImageView) findViewById(R.id.iv_right);
        iv_delete.setVisibility(View.VISIBLE);
        tv_title = (TextView) findViewById(R.id.tv_title);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    private void initData() {
        Intent args = getIntent();
        if (args == null)
            return;
        paths = args.getStringArrayListExtra(PATH_LIST);
        currentItem = args.getIntExtra(CURRENT_ITEM,0);
        canBeDeleted = args.getBooleanExtra(CAN_BE_DELETED,true);

        if(!canBeDeleted)
            iv_delete.setVisibility(View.GONE);
        adapter = new ImagePagerAdapter(mActivity, paths);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(currentItem);
        showPicNums(getHtmlHandlerString());
    }

    private void initListener() {

        iv_back.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                showPicNums(getHtmlHandlerString());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pop.setOnItemClickListener(new BasePopupWindow.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case BasePopupWindow.ItemClickListener.TWO:
                        isCountChanged = true;
                        paths.remove(currentItem);
                        adapter.notifyDataSetChanged();
                        mViewPager.setCurrentItem(currentItem < paths.size() ? currentItem : currentItem - 1);
                        showPicNums(getHtmlHandlerString());
                        if (adapter.getCount() <=0 ){
                            Intent intent = new Intent();
                            intent.putStringArrayListExtra(PATH_LIST,paths);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                }
            }
        });
    }

    private void showPicNums(Spanned text) {
        tv_title.setText(text);
    }


    private Spanned getHtmlHandlerString() {
        return Html.fromHtml("<big><strong>" + (adapter.getCount() <= 0 ? 0 : currentItem + 1) + "</strong></big>" + "/" + adapter.getCount());
    }

    @Override
    public void onBackPressed() {
        if(isCountChanged){
            Intent intent = new Intent();
            intent.putStringArrayListExtra(PATH_LIST,paths);
            setResult(RESULT_OK,intent);
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //返回
            case R.id.iv_left:
                if(isCountChanged){
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra(PATH_LIST,paths);
                    setResult(RESULT_OK,intent);
                }
                finish();
                break;
            //删除
            case R.id.iv_right:
                if (adapter.getCount() > 0)
                    pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                break;
            default:
                break;
        }

    }

}
