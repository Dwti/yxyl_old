package com.common.core.utils;

import java.io.File;

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
}
