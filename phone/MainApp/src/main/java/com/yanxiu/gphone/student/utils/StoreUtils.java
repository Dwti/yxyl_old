package com.yanxiu.gphone.student.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import com.yanxiu.basecore.task.base.threadpool.YanxiuSimpleAsyncTask;

import java.io.*;

/**
 * Created by Administrator on 2015/5/19.
 */
public class StoreUtils {
    public static final String PATH = "YanxiuStudent/storage/";
    public static final String DATAINFO = PATH + "relevantData";
    public static boolean isSdcardAvailable() {
        boolean exist = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        return exist;
    }
    public static File getSdcardRootDirectory() {
        if (isSdcardAvailable()) {
            return Environment.getExternalStorageDirectory();
        }
        return null;
    }

    /**
     * 保存相关数据到文件
     *
     * @param data
     * @param file
     */
    private static void writeTxtData(Context context, final String data, final File file) {
        new YanxiuSimpleAsyncTask<Void>(context) {
            @Override
            public Void doInBackground() {
                OutputStreamWriter  out = null;
                try {
                    out = new OutputStreamWriter (new FileOutputStream(file), "UTF-8");
                    out.write(data);
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
            @Override
            public void onPostExecute(Void result) {

            }
        }.start();
    }
/**
     * 保存相关数据到文件
     *
     * @param data
     * @param file
     */
    private static void writeRelevantData(Context context, final String data, final File file) {
        new YanxiuSimpleAsyncTask<Void>(context) {
            @Override
            public Void doInBackground() {
                ObjectOutputStream out = null;
                try {
                    out = new ObjectOutputStream(new FileOutputStream(file));
                    out.writeObject(data);
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
            @Override
            public void onPostExecute(Void result) {

            }
        }.start();
    }

    /**
     * 从文件中读取数据
     *
     * @param file
     * @return
     */
    private static String readRelevantData(File file) {
        ObjectInputStream in = null;

        try {
            in = new ObjectInputStream(new FileInputStream(file));
            return (String) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static File getRelevantFile(Context context, String filename) {
        if (!isSdcardAvailable()) {
            return null;
        }

        try {
            File dir = new File(Environment.getExternalStorageDirectory(), DATAINFO);

            if (!dir.isDirectory()) {
                dir.mkdirs();
            }

            File file = new File(dir, filename);

            if (!file.exists()) {
                file.createNewFile();
            }

            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String saveRelevantData(Context context, String filename, String data) {
        File file = getRelevantFile(context, filename);

        if (file != null && !TextUtils.isEmpty(data)) {
            writeRelevantData(context, data, file);
            return file.getAbsolutePath();
        } else {
            return null;
        }
    }
    public static String saveTxtData(Context context, String filename, String data) {
        File file = getRelevantFile(context, filename);

        if (file != null && !TextUtils.isEmpty(data)) {
            writeTxtData(context, data, file);
            return file.getAbsolutePath();
        } else {
            return null;
        }
    }
    public static String getRelevantData(Context context, String filename) {
        File file = getRelevantFile(context, filename);

        if (file != null) {
            return readRelevantData(file);
        }
        return null;
    }
}
