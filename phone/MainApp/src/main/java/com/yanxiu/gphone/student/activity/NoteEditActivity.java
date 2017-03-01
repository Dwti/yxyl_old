package com.yanxiu.gphone.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yanxiu.gphone.student.R;
import com.yanxiu.gphone.student.utils.MediaUtils;
import com.yanxiu.gphone.student.view.PhotoView;

import java.util.ArrayList;

/**
 * Created by sp on 17-2-28.
 */

public class NoteEditActivity extends Activity {
    private View fl_root;
    private EditText editText;
    private PhotoView photoView;
    private String content;
    private ArrayList<String> photoPath;
    private Button btn_delete;
    public static final String PHOTO_PATH = "phtoPath";
    public static final String NOTE_CONTENT = "noteContent";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        fl_root = findViewById(R.id.fl_root);
        editText = (EditText) findViewById(R.id.editText);
        photoView = (PhotoView) findViewById(R.id.photoView);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        photoPath = getIntent().getStringArrayListExtra(PHOTO_PATH);
        content = getIntent().getStringExtra(NOTE_CONTENT);
        photoView.setMaxCount(4);
        photoView.setData(photoPath);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoView.delete(photoView.getPhotos().size() -1);
            }
        });
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
}
