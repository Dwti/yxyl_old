package com.yanxiu.gphone.student.upgrade;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by king on 2015/5/14.
 */
public class FileLoadUtils {

    /**
     * 100M
     */
    private static long SIZE_100M = 104857600L;

    /**
     * number from - 1 to 9
     */
    public static int NO_0 = 0;
    public static int NO_1 = 1;
    public static int NO_2 = 2;
    public static int NO_3 = 3;
    public static int NO_4 = 4;
    public static int NO_5 = 5;
    public static int NO_6 = 6;
    public static int NO_7 = 7;
    public static int NO_8 = 8;
    public static int NO_9 = 9;
    public static int NO_10 = 10;
    public static int NO_40 = 40;
    public static int NO_41 = 41;
    public static int NO_42 = 42;
    public static int NO_ERROR = -1;
    public static int NO_END = -1;

    /**
     * seconds count
     */
    public static int SECOND_HALF = 500;
    public static int SECOND_1 = 1000;

    /**
     * 1K
     */
    public static int BUFFER = 1024 * 100;

    /**
     * common character for reusing
     */
    public static char CHARACTER_SLASH = '/';
    public static char CHARACTER_EQUAL = '=';
    public static char CHARACTER_AND = '&';
    public static char CHARACTER_QUESTION = '?';
    public static char CHARACTER_BARS = '-';

    public static String DOMYBOXDIR = "YanXiu/app/student";

    /**
     * check the sdcard if it is existed or available.
     */
    public static boolean isExternalStorageMountAvailable() {
        return Environment.MEDIA_MOUNTED
                .equals(Environment.getExternalStorageState());
    }

    /**
     * external storage available memory size
     *
     * @return long , available size
     * @author Arashmen
     */
    public static long getAvailableExternalStorageSize() {
        long availableExternalMemorySize = 0;
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        availableExternalMemorySize = availableBlocks * blockSize;
        availableExternalMemorySize = availableExternalMemorySize - SIZE_100M;
        if (availableExternalMemorySize < NO_0)
            availableExternalMemorySize = 0L;
        return availableExternalMemorySize;
    }

    /**
     * check the file if it exists in external storage. If it existed , it
     * indicated that this file didn't down-load completely, it's a resuming
     * load file.
     *
     * @return String , absolutely path.
     */
    public static String getExternalStorageAbsolutePath(String urlStr) {
        int position = urlStr.lastIndexOf(CHARACTER_SLASH);
        if (position > 0) {
            urlStr = urlStr.substring(position + 1);
        }
        String dir = getLoadExternalDir();
        File file = new File(dir);
        if (!file.exists())
            file.mkdirs();
        return dir + urlStr;
    }

    public static boolean isFileExist(String urlStr) {
        int position = urlStr.lastIndexOf(CHARACTER_SLASH);
        if (position > 0) {
            urlStr = urlStr.substring(position + 1);
        }
        String dir = getLoadExternalDir()+urlStr;
        File file = new File(dir);
        return file.exists();
    }

    public static void  deleteFile(String urlStr) {
        int position = urlStr.lastIndexOf(CHARACTER_SLASH);
        if (position > 0) {
            urlStr = urlStr.substring(position + 1);
        }
        String dir = getLoadExternalDir()+urlStr;
        File file = new File(dir);
        if (file.exists()){
            file.delete();
        }
    }

    public static String getLoadExternalDir() {
        String path = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        StringBuffer mBuffer = new StringBuffer();
        mBuffer.append(path);
        mBuffer.append(CHARACTER_SLASH);
        mBuffer.append(DOMYBOXDIR);
        mBuffer.append(CHARACTER_SLASH);
        return mBuffer.toString();
    }

}