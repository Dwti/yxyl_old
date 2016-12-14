package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.PhotoAdapter;
import com.yanxiu.gphone.student.base.YanxiuBaseActivity;
import com.yanxiu.gphone.student.view.StudentLoadingLayout;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/10/8.
 */
public class PhotoViewActivity extends YanxiuBaseActivity implements OnClickListener, ViewPager.OnPageChangeListener{

    private ViewPager mViewPager;
    private ImageView backBtn;


    private PhotoAdapter mAdapter;

    private ArrayList<String> dataList;

    private int pagerIndex;

    private int pagerCount;

    private TextView tvPagerIndex;
    private TextView tvPagerCount;

    public static void launch(Activity context, ArrayList<String> dataList, int pagerIndex) {
        Intent intent = new Intent(context, PhotoViewActivity.class);
        intent.putExtra("dataList", dataList);
        intent.putExtra("pagerIndex", pagerIndex);
//        intent.putParcelableArrayListExtra("paperTestEntityList", (ArrayList<? extends Parcelable>) dataList);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_photo_view);
        initView();
        initData();
    }

    private void initData() {
        Intent intent = this.getIntent();
        dataList = intent.getStringArrayListExtra("dataList");
        pagerIndex = intent.getIntExtra("pagerIndex", 0);
        mAdapter = new PhotoAdapter(getSupportFragmentManager());
        mAdapter.setDataSources(dataList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(pagerIndex);

        tvPagerCount.setText("/" + mAdapter.getCount());
        tvPagerIndex.setText(String.valueOf(pagerIndex + 1));
    }


//    protected abstract int getLayoutId();

    private void initView(){
        backBtn=(ImageView)findViewById(R.id.iv_top_back);
        mViewPager = (ViewPager) this.findViewById(R.id.photo_viewpager);
        mViewPager.setOnPageChangeListener(this);
        backBtn.setOnClickListener(this);
        tvPagerCount = (TextView) this.findViewById(R.id.tv_pager_count);
        tvPagerIndex = (TextView) this.findViewById(R.id.tv_pager_index);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_top_back:
                finish();
                break;
       }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        tvPagerCount.setText("/" + mAdapter.getCount());
        tvPagerIndex.setText(String.valueOf(i + 1));
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }


}
