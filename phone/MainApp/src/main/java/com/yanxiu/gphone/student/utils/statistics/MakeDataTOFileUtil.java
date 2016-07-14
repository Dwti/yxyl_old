package com.yanxiu.gphone.student.utils.statistics;

import android.util.Log;

import com.yanxiu.gphone.student.bean.statistics.DataForCreateLogTxtData;
import com.yanxiu.gphone.student.bean.statistics.YanXiuDataBase;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by frc on 16-6-7.
 */
public class MakeDataTOFileUtil {
    private final static String ALL_POINT_FILE_PATH = "sdcard/frc/";
    private final static String ALL_POINT_FILE_NAME = new Date() + "frc.txt";

    public List<File> getAllPointFile() {
        List<DataForCreateLogTxtData> list = (List<DataForCreateLogTxtData>) DataBaseManager.getInstance().queryAllData(DataForCreateLogTxtData.class);
        DataBaseManager.getInstance().delAllData(DataForCreateLogTxtData.class);
        return CreateFile(ALL_POINT_FILE_PATH, ALL_POINT_FILE_NAME, list);

    }

    private List<File> CreateFile(String filePath, String fileName, List<? extends YanXiuDataBase> list) {
        List<File> filelist = makeRootDirectory(filePath);
        if (filelist == null) {
            filelist = new ArrayList<>();
        }
        String strFilePath = filePath + fileName;
        File file = new File(strFilePath);
        if (!file.exists()) {
            Log.e("frc", "Create the file" + strFilePath);
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String strContent = AddFileContent(list);
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        filelist.add(file);
        return filelist;
    }

    private String AddFileContent(List<? extends YanXiuDataBase> list) {
        String fileContent = null;
        for (YanXiuDataBase data :
                list) {
            DataForCreateLogTxtData dataForCreateLogText = (DataForCreateLogTxtData) data;
            fileContent = dataForCreateLogText.getDataName() + " : " + dataForCreateLogText.getDataContent() + " create at" + dataForCreateLogText.getUploadTime() + "\r\n";
        }
        return fileContent;
    }

    /**
     * 可能存在上次上传没有成功的文件  本次一起上传
     *
     * @param filePath
     * @return
     */
    private List<File> makeRootDirectory(String filePath) {
        List<File> fileList = null;
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (fileList == null) {
                        fileList = new ArrayList<>();
                    }
                    fileList.add(files[i]);

                }

            }
        } catch (Exception e) {
        }
        return fileList;

    }


}


