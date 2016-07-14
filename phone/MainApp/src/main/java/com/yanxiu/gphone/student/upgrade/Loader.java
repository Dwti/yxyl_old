package com.yanxiu.gphone.student.upgrade;

import android.text.TextUtils;

import com.common.core.utils.LogInfo;
import com.yanxiu.basecore.exception.ServiceException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by king on 2015/5/14.
 */
public class Loader implements Runnable {

    private String urlStr;
    private String absolutePath;
    private LoaderListener loadListener;
    private long currentPosition = 0;
    private boolean cancelFlag;
    private static final String HTTP = "http://";
    public static final String STUDENT = "student";
    public static final String STUDENT_ATTACH = "student/attach_";
    public static final String STUDENT_UPLOAD = "student/yanxiustudent.apk";

    /**
     * constructor to create a object.
     *
     * @param urlStr , the String of url.
     */
    public Loader(String urlStr, LoaderListener loadListener) {
        this.urlStr = urlStr;
        this.absolutePath = FileLoadUtils
                .getExternalStorageAbsolutePath(STUDENT);
        this.loadListener = loadListener;
    }

    public Loader(String urlStr,String name, LoaderListener loadListener) {
        this.urlStr = urlStr;
        this.absolutePath = FileLoadUtils
                .getExternalStorageAbsolutePath(name);
        this.loadListener = loadListener;
    }

    public Loader(String urlStr, LoaderListener loadListener,
            long currentPosition) {
        this.urlStr = urlStr;
        this.absolutePath = FileLoadUtils
                .getExternalStorageAbsolutePath(STUDENT);
        this.loadListener = loadListener;
        this.currentPosition = currentPosition;
    }

    public void run() {
        loadListener.onStart();

        URL mURL = null;
        InputStream in = null;
        boolean flag = true;
        int count = 0;
        long totalSize = 0;

        File file = new File(absolutePath);
        String redirectUrl = null;
        try {
            if (!urlStr.startsWith(HTTP)) {
                urlStr = HTTP + urlStr;
            }
            {
                HttpURLConnection conn = null;
                try {
                    LogInfo.log("haitian", "xxxxxxxxx----loader_url=" + urlStr);
                    URL serverUrl = new URL(urlStr);
                    conn = (HttpURLConnection) serverUrl.openConnection();
                    conn.setRequestMethod("GET");
                    // 必须设置false，否则会自动redirect到Location的地址
                    conn.setInstanceFollowRedirects(false);
                    conn.addRequestProperty("Accept-Charset", "UTF-8;");
                    conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
                    conn.addRequestProperty("Referer", "http://www.360doc.com/");
                    conn.setReadTimeout(10 * 1000);
                    conn.setConnectTimeout(15 * 1000);
                    conn.connect();
                    redirectUrl = conn.getHeaderField("Location");
                    LogInfo.log("haitian", "xxxxxxxxx----loader urlRedirect=" + redirectUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    conn.disconnect();
                }
            }
            if(!TextUtils.isEmpty(redirectUrl)){
                urlStr = redirectUrl;
            }
            mURL = new URL(urlStr);
            totalSize = getContentLength(mURL);

            long availableSize = FileLoadUtils
                    .getAvailableExternalStorageSize();
            if (totalSize > availableSize && availableSize > 0) {
                loadListener.onUnAvailable();
                throw new ServiceException("Lack of AvailableExternalStorageSize!");
            }

            file.createNewFile();
            RandomAccessFile randomFileTotal = new RandomAccessFile(file,
                    "rwd");
            randomFileTotal.setLength(totalSize);
            randomFileTotal.close();
        } catch (ServiceException e1) {
            flag = false;
            loadListener.onError(e1.getMessage(), 1);
        } catch (IOException e) {
            flag = false;
            loadListener.onError(e.getMessage(), 1);
        }

        while (count < FileLoadUtils.NO_3 && flag) {
            try {
                HttpURLConnection connection = (HttpURLConnection) mURL
                        .openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setConnectTimeout(
                        (FileLoadUtils.NO_5+FileLoadUtils.NO_10) * FileLoadUtils.SECOND_1);
                connection.setReadTimeout(
                        (FileLoadUtils.NO_5+FileLoadUtils.NO_10) * FileLoadUtils.SECOND_1);
                connection.setRequestProperty("Range",
                        new StringBuffer().append("bytes=")
                                .append(currentPosition).append("-")
                                .append(totalSize).toString());
                in = connection.getInputStream();

                RandomAccessFile randomFile = new RandomAccessFile(file, "rwd");
                randomFile.seek(currentPosition);

                byte[] buffer = new byte[FileLoadUtils.BUFFER];
                int length = 0;
                int oldProgress = 0;
                while ((length = in.read(buffer)) != FileLoadUtils.NO_END) {
                    try {
                        randomFile.write(buffer, FileLoadUtils.NO_0, length);
                        currentPosition += length;
                        int newProgress = (int) (
                                (currentPosition * 1.0 / totalSize) * 100);
                        if (newProgress > oldProgress) {
                            loadListener.onProgress(newProgress);
                            oldProgress = newProgress;
                        }
                        if(cancelFlag) throw new ServiceException(2);
                    } catch (IOException e) {
                        loadListener.onFailure(e.getMessage(), 1);
                    }
                }
                connection.disconnect();

                if (randomFile != null)
                    randomFile.close();

                loadListener.onComplete(absolutePath);
                break;

            } catch (ServiceException e) {
                if(e.getErrorCode() == 2){
                    loadListener.onCancel();
                    break;
                }
            }catch (IOException e) {
                count++;
                if (count == FileLoadUtils.NO_3) {
                    loadListener.onFailure(e.getMessage(), 1);
                }
            } catch (Throwable t) {
                loadListener.onError(t.getMessage(), -1);
            } finally {
                try {
                    if (in != null)
                        in.close();
                } catch (Throwable e) {
                    loadListener.onError(e.getMessage(), 1);
                }
            }
        }
    }

    public long getContentLength(URL mURL) throws IOException {
        long totalSize = 0;
        int count = FileLoadUtils.NO_0;
        while (count < FileLoadUtils.NO_3) {
            try {
                if (totalSize > FileLoadUtils.NO_0){
                    return totalSize;
                }else{
                    count ++;
                }
                LogInfo.log("geny", "hearder getContentLength------start");
                HttpURLConnection connection = (HttpURLConnection) mURL
                        .openConnection();
                connection.setRequestMethod("HEAD");
                connection.setReadTimeout(10 * 1000);
                connection.setConnectTimeout(15*1000);
                connection.connect();
                totalSize = connection.getContentLength();
                connection.disconnect();
                LogInfo.log("geny", "hearder getContentLength------end");
            } catch (IOException e) {
                loadListener.onFailure(e.getMessage(), 1);
                count++;
            } catch (NullPointerException e) {
                loadListener.onFailure(e.getMessage(), 1);
                count++;
            }
        }

        if (totalSize < 0) {
            throw new IOException();
        }

        return totalSize;
    }
    public void cancel(){
        cancelFlag = true;
    }


    public long getCurrentPosition() {
        return currentPosition;
    }

    public interface LoaderListener {
        void onStart();

        void onError(String message, int code);

        void onFailure(String message, int code);

        void onComplete(String path);

        void onProgress(double progress);

        void onCancel();

        void onUnAvailable();
    }
}
