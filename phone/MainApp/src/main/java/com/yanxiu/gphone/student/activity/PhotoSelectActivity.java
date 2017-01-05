package com.yanxiu.gphone.student.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.adapter.PhotoGridAdapter;
import com.yanxiu.gphone.student.adapter.PopupDirectoryListAdapter;
import com.yanxiu.gphone.student.bean.Photo;
import com.yanxiu.gphone.student.bean.PhotoDirectory;
import com.yanxiu.gphone.student.task.PhotoStoreHelper;
import com.yanxiu.gphone.student.utils.PhotoLoaderConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunpeng on 2016/12/30.
 */

public class PhotoSelectActivity extends Activity {
    private RecyclerView recyclerView;
    private ImageView iv_left;
    private View ll_bottom;
    private TextView tv_title;
    private final int COLUMN = 3;
    private TextView tv_switch_dir;
    private ListPopupWindow listPopupWindow;
    //目录弹出框的一次最多显示的目录数目
    public static final int COUNT_MAX = 4;
    private PhotoGridAdapter photoGridAdapter;
    private PopupDirectoryListAdapter popDirListAdapter;
    private List<PhotoDirectory> directories = new ArrayList<>();
    private List<Photo> photos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_select);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        tv_title = (TextView) findViewById(R.id.tv_title);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tv_switch_dir = (TextView) findViewById(R.id.tv_switch_dir);
        ll_bottom = findViewById(R.id.ll_bottom);
        popDirListAdapter = new PopupDirectoryListAdapter(this,directories);
        photoGridAdapter = new PhotoGridAdapter(this,photos);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(COLUMN, OrientationHelper.VERTICAL);
//        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(photoGridAdapter);
        tv_title.setText(R.string.picker_all_image);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());

        photoGridAdapter.setOnItemClickListener(new PhotoGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Photo photo) {
                if(!TextUtils.isEmpty(photo.getPath())){
                    dispatchImageCropIntent(photo.getPath());
                }
            }
        });
        listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
        listPopupWindow.setHeight(COUNT_MAX * getResources().getDimensionPixelOffset(R.dimen.picker_item_directory_height));
        listPopupWindow.setAnchorView(ll_bottom);
        listPopupWindow.setAdapter(popDirListAdapter);
        listPopupWindow.setModal(true);
        listPopupWindow.setDropDownGravity(Gravity.BOTTOM);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listPopupWindow.dismiss();

                PhotoDirectory directory = directories.get(position);

                tv_switch_dir.setText(directory.getName());
                tv_title.setText(directory.getName());
                photos.clear();
                photos.addAll(directories.get(position).getPhotos());
                photoGridAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(0);
            }
        });

        tv_switch_dir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listPopupWindow.isShowing()) {
                    listPopupWindow.dismiss();
                } else if (!isFinishing()) {
//                    adjustHeight();
                    listPopupWindow.show();
                }
            }
        });

        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        int hasPer = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(hasPer == PackageManager.PERMISSION_GRANTED){

        }else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }

        Bundle args = new Bundle();
        args.putBoolean(PhotoLoaderConstant.EXTRA_SHOW_GIF,false);
        PhotoStoreHelper.getPhotoDirs(this,args,photosResultCallback);
    }

    private PhotoStoreHelper.PhotosResultCallback photosResultCallback = new PhotoStoreHelper.PhotosResultCallback() {
        @Override
        public void onResultCallback(List<PhotoDirectory> directories) {
            refreshData(directories);
        }
    };

    private void refreshData(List<PhotoDirectory> directories) {
        if(directories == null || directories.isEmpty())
            return;
        this.directories.clear();
        this.directories.addAll(directories);
        popDirListAdapter.notifyDataSetChanged();
        photos.clear();
        photos.addAll(directories.get(PhotoStoreHelper.INDEX_ALL_PHOTOS).getPhotos());
        Log.i("count",photos.size()+"");
        photoGridAdapter.notifyDataSetChanged();
    }

    private void adjustHeight() {
        if (popDirListAdapter == null) return;
        int count = popDirListAdapter.getCount();
        if (listPopupWindow != null) {
            listPopupWindow.setHeight(COUNT_MAX * getResources().getDimensionPixelOffset(R.dimen.picker_item_directory_height));
        }
    }

    private void dispatchImageCropIntent(String data){
        Intent intent = new Intent(this,ImageCropActivity.class);
        intent.putExtra(ImageCropActivity.IMAGE_PATH,data);
        startActivityForResult(intent,ImageCropActivity.REQUEST_IMAGE_CROP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ImageCropActivity.REQUEST_IMAGE_CROP){
            if (resultCode == RESULT_OK){
                setResult(RESULT_OK, data);
            }
            finish();
        }
    }
}
