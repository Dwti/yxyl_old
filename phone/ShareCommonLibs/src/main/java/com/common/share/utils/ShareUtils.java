package com.common.share.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import com.common.core.utils.ContextProvider;
import com.common.core.utils.StringUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lee on 16-3-23.
 */
public class ShareUtils {


    public static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


    public static void deleteShareBitmap(String deleteShareFilePath){
        if(StringUtils.isEmpty(deleteShareFilePath)){
            return;
        }
        File file = new File(deleteShareFilePath);
        if(file.exists()){
            file.delete();
        }
    }



    public static void saveShareBitmap(final Bitmap bitmap, final String dirPath, final String name) {
        if (!sdCardMounted()) {
            return ;
        }
        if(StringUtils.isEmpty(dirPath)||StringUtils.isEmpty(name)){
            return;
        }
        File dir = new File(dirPath);
        File file = null;
        if (!dir.exists()) {
            dir.mkdirs();
        }
        file = new File(dir, name);
        if (file.exists() && file.length() > 5 * 1024 * 1024) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedOutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            file.delete();
        } finally {
            try {
                os.close();
            } catch (Exception e) {
                file.delete();
                e.printStackTrace();
            }
        }
    }


    public static String getAsssetFilePath(String fileName) throws IOException {
        InputStream abpath = ContextProvider.getApplicationContext().getClass().getResourceAsStream("/assets/"+fileName);
        return new String(InputStreamToByte(abpath));
    }

    public static InputStream getAsssetStream(String fileName){
        return  ContextProvider.getApplicationContext().getClass().getResourceAsStream("/assets/"+fileName);
    }

    private static byte[] InputStreamToByte(InputStream is) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        while ((ch = is.read()) != -1) {
            bytestream.write(ch);
        }
        byte imgdata[] = bytestream.toByteArray();
        bytestream.close();
        return imgdata;
    }

    /**
     * 检查是否装在sd卡
     *
     * @return
     */
    private static boolean sdCardMounted() {
        final String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED) && !state
                .equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }


}
