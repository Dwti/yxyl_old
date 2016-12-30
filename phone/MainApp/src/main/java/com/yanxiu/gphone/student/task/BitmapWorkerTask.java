package com.yanxiu.gphone.student.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.yanxiu.gphone.student.commoninterface.OnTaskCompleteListener;
import com.yanxiu.gphone.student.utils.Utils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by sp on 16-12-16.
 */

public class BitmapWorkerTask extends AsyncTask<String,Integer,Bitmap> {
    private OnTaskCompleteListener<Bitmap> onTaskCompleteListener;

    public BitmapWorkerTask(OnTaskCompleteListener<Bitmap> onTaskCompleteListener) {
        this.onTaskCompleteListener = onTaskCompleteListener;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String imagePath = params[0];
        int targetW = Integer.parseInt(params[1]);
        int targetH = Integer.parseInt(params[2]);

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath,bmOptions);

        int scaleFactor = Utils.calculateInSampleSize(bmOptions,targetW,targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath,bmOptions);
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(onTaskCompleteListener != null)
            onTaskCompleteListener.onComplete(bitmap);
    }
}
