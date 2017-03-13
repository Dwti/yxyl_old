package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.UploadImageBean;
import com.yanxiu.gphone.student.httpApi.YanxiuHttpApi;
import com.yanxiu.gphone.student.utils.MediaUtils;
import com.yanxiu.gphone.student.utils.ToastMaster;
import com.yanxiu.gphone.student.view.PhotoView;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by sp on 17-2-28.
 */

public class NoteEditActivity extends Activity implements View.OnClickListener {
    private View fl_root;
    private EditText mEditText;
    private PhotoView photoView;
    private ImageView iv_cancel, iv_save;
    public static final String PHOTO_PATH = "phtoPath";
    public static final String NOTE_CONTENT = "noteContent";
    public static final int REQUEST_NOTE_EDIT = 0x001;
    private ArrayList<String> mPhotoPath;
    private ArrayList<String> mHttpPath = new ArrayList<>();

    public static void lanuch(Fragment fragment, String content, ArrayList<String> imagePaths) {
        Intent intent = new Intent(fragment.getActivity(), NoteEditActivity.class);
        intent.putExtra(NOTE_CONTENT, content);
        intent.putStringArrayListExtra(PHOTO_PATH, imagePaths);
        fragment.startActivityForResult(intent, REQUEST_NOTE_EDIT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        fl_root = findViewById(R.id.fl_root);
        iv_cancel = (ImageView) findViewById(R.id.iv_cancel);
        iv_save = (ImageView) findViewById(R.id.iv_save);
        mEditText = (EditText) findViewById(R.id.editText);
        photoView = (PhotoView) findViewById(R.id.photoView);
        mPhotoPath = getIntent().getStringArrayListExtra(PHOTO_PATH);
        String content = getIntent().getStringExtra(NOTE_CONTENT);

        mEditText.setText(content);
        photoView.setMaxCount(4);
        photoView.setData(mPhotoPath);

        iv_cancel.setOnClickListener(this);
        iv_save.setOnClickListener(this);

    }

    private void saveData() {
        List<String> localPhotoPath = new ArrayList<>();
        if (photoView.getPhotos() != null && photoView.getPhotos().size() > 0) {
            for (String path : photoView.getPhotos()) {
                if (!path.startsWith("http"))
                    localPhotoPath.add(path);
                else mHttpPath.add(path);
            }
            if (localPhotoPath.size() > 0)
                uploadImages(localPhotoPath);
            else saveContentAndImages(mHttpPath, mEditText.getText().toString());
        }else saveContentAndImages(mHttpPath,mEditText.getText().toString());
    }

    private void saveContentAndImages(List<String> images,String content){
        //调增加笔记 的接口，然后setResult
    }
    private void uploadImages(List<String> photos) {
        LinkedHashMap<String, File> hashMap = new LinkedHashMap<>();
        for(String path : photos)
            hashMap.put(String.valueOf(path.hashCode()),new File(path));
        YanxiuHttpApi.requestUploadImage(hashMap, new NoteUploadFileListener());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ImagePreviewActivity.IMAGE_PREVIEW:
                if (resultCode == RESULT_OK) {
                    ArrayList<String> paths = data.getStringArrayListExtra(ImagePreviewActivity.PATH_LIST);
                    photoView.setData(paths);
                }
                break;
            case MediaUtils.CAPATURE_AND_CROP:
                if (resultCode == RESULT_OK) {
                    String imagePath = data.getStringExtra(ImageCropActivity.IMAGE_PATH);
                    photoView.add(imagePath);
                }
                CameraActivity.bitmap = null;
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel:
                finish();
                break;
            case R.id.iv_save:
                saveData();
//                setResultOK(mHttpPath,mEditText.getText().toString());
                break;
            default:
                break;
        }
    }

    private void setResultOK(ArrayList<String> data,String text) {
        Intent intent = new Intent();
        intent.putExtra(NOTE_CONTENT, text);
        intent.putStringArrayListExtra(PHOTO_PATH, data);
        setResult(RESULT_OK, intent);
        finish();
    }

    private class NoteUploadFileListener implements YanxiuHttpApi.UploadFileListener<UploadImageBean> {

        @Override
        public void onFail(UploadImageBean bean) {
            ToastMaster.showShortToast(NoteEditActivity.this, "上传失败");
        }

        @Override
        public void onSuccess(UploadImageBean bean) {
            mHttpPath.addAll(bean.getData());
            ToastMaster.showShortToast(NoteEditActivity.this, "上传成功");
        }

        @Override
        public void onProgress(int progress) {

        }
    }
}
