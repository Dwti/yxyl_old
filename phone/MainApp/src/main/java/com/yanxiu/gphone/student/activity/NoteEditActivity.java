package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.common.login.LoginModel;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.bean.NoteBean;
import com.yanxiu.gphone.student.bean.NoteResponseBean;
import com.yanxiu.gphone.student.bean.UploadImageBean;
import com.yanxiu.gphone.student.bean.request.NoteRequest;
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
    private EditText mEditText;
    private PhotoView mPhotoView;
    private ImageView iv_cancel, iv_save;
    public static final String PHOTO_PATH = "photoPath";
    public static final String NOTE_CONTENT = "noteContent";
    public static final String WQID = "wqid";
    public static final String QID = "qid";
    public static final int REQUEST_NOTE_EDIT = 0x001;
    private String wqid,qid;
    private ArrayList<String> mPhotoPath = new ArrayList<>();
    private ArrayList<String> mHttpPath = new ArrayList<>();
    private List<String> localPhotoPath = new ArrayList<>();

    private String mContent;

    public static void launch(Fragment fragment, String content, ArrayList<String> imagePaths) {
        Intent intent = new Intent(fragment.getActivity(), NoteEditActivity.class);
        intent.putExtra(NOTE_CONTENT, content);
        intent.putStringArrayListExtra(PHOTO_PATH, imagePaths);
        fragment.startActivityForResult(intent, REQUEST_NOTE_EDIT);
    }

    public static void launch(Fragment fragment, Bundle args) {
        Intent intent = new Intent(fragment.getActivity(), NoteEditActivity.class);
        intent.putExtra("data",args);
        fragment.startActivityForResult(intent, REQUEST_NOTE_EDIT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        iv_cancel = (ImageView) findViewById(R.id.iv_cancel);
        iv_save = (ImageView) findViewById(R.id.iv_save);
        mEditText = (EditText) findViewById(R.id.editText);
        mPhotoView = (PhotoView) findViewById(R.id.photoView);
        Bundle args = getIntent().getBundleExtra("data");
        mPhotoPath = args.getStringArrayList(PHOTO_PATH);
        mContent = args.getString(NOTE_CONTENT);
        wqid = args.getString(WQID);
        qid = args.getString(QID);

        mEditText.setText(mContent);
        mPhotoView.setMaxCount(4);
        mPhotoView.setData(mPhotoPath);

        iv_cancel.setOnClickListener(this);
        iv_save.setOnClickListener(this);

    }

    /**
     * 剥离出来没上传到server的图片，也就是路径是本地的图片
     */
    private void saveData() {
        localPhotoPath.clear();
        mHttpPath.clear();
        if (mPhotoView.getPhotos() != null && mPhotoView.getPhotos().size() > 0) {
            for (String path : mPhotoView.getPhotos()) {
                if (!path.startsWith("http"))
                    localPhotoPath.add(path);
                else mHttpPath.add(path);
            }
            if (localPhotoPath.size() > 0)
                uploadImages(localPhotoPath);
            else saveContentAndImages(mHttpPath, mEditText.getText().toString());
        }else saveContentAndImages(mHttpPath,mEditText.getText().toString());
    }

    private void saveContentAndImages(ArrayList<String> images,String content){
        //调增加笔记 的接口，然后setResult
        NoteBean note = new NoteBean();
        note.setImages(images);
        note.setText(content);
        note.setQid(qid);

        NoteRequest request = new NoteRequest();
        request.setWqid(wqid);
        request.setToken(LoginModel.getToken());
        request.setNote(note);
        request.startRequest(NoteResponseBean.class,new NoteCallBack());
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
                    mPhotoView.setData(paths);
                }
                break;
            case MediaUtils.CAPATURE_AND_CROP:
                if (resultCode == RESULT_OK) {
                    String imagePath = data.getStringExtra(ImageCropActivity.IMAGE_PATH);
                    mPhotoView.add(imagePath);
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
            saveContentAndImages(mHttpPath,mEditText.getText().toString());
            ToastMaster.showShortToast(NoteEditActivity.this, "上传成功");
        }

        @Override
        public void onProgress(int progress) {

        }
    }

    private class NoteCallBack implements HttpCallback<NoteResponseBean>{

        @Override
        public void onSuccess(RequestBase request, NoteResponseBean response) {
            if(response.getStatus().getCode() == 0){
                NoteRequest noteRequest = (NoteRequest) request;
                ToastMaster.showShortToast(NoteEditActivity.this, "保存成功");
                setResultOK(noteRequest.getNote().getImages(),noteRequest.getNote().getText());
            }
        }

        @Override
        public void onFail(RequestBase request, Error error) {
            ToastMaster.showShortToast(NoteEditActivity.this, "保存失败");
        }
    }
}
