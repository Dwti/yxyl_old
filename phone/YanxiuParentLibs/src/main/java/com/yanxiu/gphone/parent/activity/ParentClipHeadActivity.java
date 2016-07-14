package com.yanxiu.gphone.parent.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.activity.base.YanxiuParentBaseActivity;
import com.yanxiu.gphone.parent.contants.YanxiuParentConstants;
import com.yanxiu.gphone.parent.utils.ParentUtils;
import com.yanxiu.gphone.parent.view.clip.ClipImageLayout;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**

 */
public class ParentClipHeadActivity extends YanxiuParentBaseActivity implements View.OnClickListener {
    private String imgPath;

    public static void launchActivity(Activity context, String uriStr, int requestCode) {
        Intent intent = new Intent(context, ParentClipHeadActivity.class);
        intent.putExtra("imgPath", uriStr);
        context.startActivityForResult(intent, requestCode);
    }

    private ClipImageLayout mClipImageLayout;
    private TextView cancleBtn, surebtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_show_head_layout);
        imgPath = getIntent().getStringExtra("imgPath");
        if (TextUtils.isEmpty(imgPath)) {
            ParentUtils.showToast(R.string.select_location_data_error);
            finish();
        }
        cancleBtn = (TextView) findViewById(R.id.cancle);
        cancleBtn.setOnClickListener(this);
        surebtn = (TextView) findViewById(R.id.sure);
        surebtn.setOnClickListener(this);
        mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
        mClipImageLayout.setImagePath(imgPath);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancle) {
            ParentClipHeadActivity.this.finish();
        } else if (v.getId() == R.id.sure) {
            Intent intent = new Intent();
            intent.putExtra("bitMapPath", getClipBitmapPath());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public Bitmap getClipBitmap() {
        Bitmap bitmap = mClipImageLayout.clip();

        return bitmap;
    }
    public String getClipBitmapPath() {
        Bitmap bitmap = mClipImageLayout.clip();
        return saveFile(bitmap, YanxiuParentConstants.HEAD_IMAGE_FILENAME);
    }
    public byte[] getClipByte() {
        Bitmap bitmap = mClipImageLayout.clip();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] datas = baos.toByteArray();

        return datas;
    }
    /**
     * 保存文件
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public String saveFile(Bitmap bm, String fileName){
        LogInfo.log("geny", "saveFile path =" + YanxiuParentConstants.HEAD_IMAGE_SAVE_DIR);
        File dirFile = new File(YanxiuParentConstants.HEAD_IMAGE_SAVE_DIR);
        LogInfo.log("geny", "saveFile path =" + dirFile.getAbsolutePath());
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile;
        BufferedOutputStream bos = null;
        try{
            myCaptureFile = new File(YanxiuParentConstants.HEAD_IMAGE_SAVE_DIR + fileName);
            bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
            return myCaptureFile.getAbsolutePath().toString();
        } catch (Exception e){
            if(bos != null ){
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            bos = null;
        }
        return null;
    }
}
