package com.common.core.utils;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by lee on 16-4-1.
 */
public class FileUtils {
    public static void RecursionDeleteFile(File file){
        if(!file.exists()){
            return;
        }
        if(file.isFile()){
            file.delete();
            return;
        }
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return;
            }
            for(File f : childFile){
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }

    public static boolean fileIsExists(String path) {
        if (path == null || path.trim().length() <= 0) {
            return false;
        }
        try {
            File f = new File(path);
            if (!f.exists()) {
                Log.e("TMG",path+"file not exists");
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static File writeStringToFile(String path, String data){
        File file  =new File(path);
        OutputStream os = null;
        BufferedWriter bw = null;
        try {
            file.createNewFile();
            os= new FileOutputStream(file);
            bw= new BufferedWriter(new OutputStreamWriter(os));
            bw.write(data);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
            file = null;
        }finally {
            try {
                if(os!=null){
                    os.close();
                }
                if(bw!=null){
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                file = null;
            }

        }
        return file;
    }
}
