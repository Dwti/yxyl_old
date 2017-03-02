package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.utils.MediaUtils;
import com.yanxiu.gphone.student.view.PhotoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sp on 17-2-28.
 */

public class NoteEditActivity extends Activity implements View.OnClickListener {
    private View fl_root;
    private EditText editText;
    private PhotoView photoView;
    private ImageView iv_cancel,iv_save;
    public static final String PHOTO_PATH = "phtoPath";
    public static final String NOTE_CONTENT = "noteContent";
    public static final int REQUEST_NOTE_EDIT = 0x001;

    public static void lanuch(Fragment fragment, String content, ArrayList<String> imagePaths){
        Intent intent = new Intent(fragment.getActivity(),NoteEditActivity.class);
        intent.putExtra(NOTE_CONTENT,content);
        intent.putStringArrayListExtra(PHOTO_PATH,imagePaths);
        fragment.startActivityForResult(intent,REQUEST_NOTE_EDIT);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        fl_root = findViewById(R.id.fl_root);
        iv_cancel = (ImageView) findViewById(R.id.iv_cancel);
        iv_save = (ImageView) findViewById(R.id.iv_save);
        editText = (EditText) findViewById(R.id.editText);
        photoView = (PhotoView) findViewById(R.id.photoView);
        ArrayList<String> photoPath = getIntent().getStringArrayListExtra(PHOTO_PATH);
        String content = getIntent().getStringExtra(NOTE_CONTENT);

        editText.setText(content);
        photoView.setMaxCount(4);
        photoView.setData(photoPath);

        iv_cancel.setOnClickListener(this);
        iv_save.setOnClickListener(this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ImagePreviewActivity.IMAGE_PREVIEW:
                if(resultCode == RESULT_OK){
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
        switch (v.getId()){
            case R.id.iv_cancel:
                finish();
                break;
            case R.id.iv_save:
                Intent intent = new Intent();
                intent.putExtra(NOTE_CONTENT,editText.getText().toString());
                intent.putStringArrayListExtra(PHOTO_PATH,photoView.getPhotos());
                setResult(RESULT_OK,intent);
                finish();
                break;
            default:
                break;
        }
    }
}
