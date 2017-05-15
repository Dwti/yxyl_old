package com.yanxiu.gphone.studentold.view.picsel.utils;

import com.common.core.utils.LogInfo;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.gphone.studentold.YanxiuApplication;
import com.yanxiu.gphone.studentold.utils.MediaUtils;
import com.yanxiu.gphone.studentold.utils.YanXiuConstant;
import com.yanxiu.gphone.studentold.view.picsel.bean.ImageBucket;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 用于保存选择的图片数据
 * Created by Administrator on 2015/9/28.
 */
public class ShareBitmapUtils implements YanxiuBaseBean {
    public static final String KEY="Share_key";
    private static  final  String TAG=ShareBitmapUtils.class.getSimpleName();
    private ShareBitmapUtils(){

    }
    private final static ShareBitmapUtils  mInstance = new ShareBitmapUtils();

    private  String currentSbId;//当前的主观题ID

    private boolean isInitCurrentId;//是否初始化首个CurrentId，初始之后置为true之后控制不在走初始化currentId

    public static ShareBitmapUtils getInstance(){
        if(mInstance==null){
            LogInfo.log(TAG, "ShareBitmapUtils create Instance");
        }
        return  mInstance;
    }

    private   List<ImageBucket> dataList = new ArrayList<>();

    //地址数组
    private final Map<String,List<String>> drrMaps = new HashMap<>();
    //添加减少 图片数
    private final Map<String,Integer> listIndexMaps = new HashMap<>();

    //默认最大可选择的图片数
    public static final int MAX_SEL_SIZE=9;

    //动态计算最大选择数目
    public  int countMax;
    //记录在相册选择的张数
    public  int recordBucketPicSelNums;

    public  void resetParams(){
        LogInfo.log(TAG,"resetParams");
        delTempDir();
        currentSbId="";
        if(dataList!=null){
            dataList.clear();
        }
        if(drrMaps!=null){
            drrMaps.clear();
        }
        if(listIndexMaps!=null){
            listIndexMaps.clear();
        }
        recordBucketPicSelNums=0;
        countMax=0;
        isInitCurrentId=false;
        MediaUtils.currentFile=null;
    }


    //添加单个图片路径
    public void addPath(String id,String path ){
        drrMaps.get(id).add(path);
        updateBitList(id);
    }

    //当前列表是否为空
    public boolean isCurrentListIsEmpty(String id ){
        return !(drrMaps.get(id) != null && (!drrMaps.get(id).isEmpty() || drrMaps.get(id).size() > 0));
    }


    public List getPathList(String id){
        return drrMaps.get(id);
    }


    //添加多个图片路径
    public  void addAllPath(String id,List paths){
        if(drrMaps.get(id)!=null){
            drrMaps.get(id).addAll(paths);
            updateBitList(id);
        }
    }


    //删除临时文件夹中的文件
    public  void  delTempDir(){
        File mediaStorageDir = new File(YanXiuConstant.SDCARD_ROOT_PATH, MediaUtils.TEMP_UPLOAD_PIC_DIR);
        if (!mediaStorageDir.exists() || !mediaStorageDir.isDirectory())
            return;

        for (File file : mediaStorageDir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                delTempDir(); // 递规的方式删除文件夹
        }
        mediaStorageDir.delete();// 删除目录本身
    }
    private boolean checkParams(String id){
        return drrMaps.size() > 0 && drrMaps.get(id).size() > 0;
    }


    private void updateBitList(String id){
        int size = drrMaps.get(id).size();
        while (true) {
            int loadIndex = listIndexMaps.get(id);
            if (checkParams(id)) {
                if (loadIndex == size) {
                    LogInfo.log(TAG, "list is size is max");
                    break;
                } else {
                    try {
                        List<String> list=drrMaps.get(id);
                        if (loadIndex<0){
                            loadIndex += 1;
                            listIndexMaps.put(id, loadIndex);
                        }else if (loadIndex>list.size()){
                            loadIndex -= 1;
                            listIndexMaps.put(id, loadIndex);
                        }else {
                            String path = drrMaps.get(id).get(loadIndex);
                            MediaUtils.scanFile(YanxiuApplication.getContext(), path);
                            LogInfo.log(TAG, "Add new Pic Path: " + path);
                            LogInfo.log(TAG, "LIST_INDEX " + loadIndex);
                            loadIndex += 1;
                            listIndexMaps.put(id, loadIndex);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else {
                break;
            }
        }

    }



    public  String getCurrentSbId() {
        return currentSbId;
    }

    public  void setCurrentSbId(String currentSbId) {
        this.currentSbId = currentSbId;
    }

    public List<ImageBucket> getDataList() {
        return dataList;
    }

    public void setDataList(List<ImageBucket> dataList) {
        this.dataList = dataList;
    }


    public Map<String, List<String>> getDrrMaps() {
        return drrMaps;
    }


    public Map<String, Integer> getListIndexMaps() {
        return listIndexMaps;
    }


    public int getCountMax() {
        return countMax;
    }


    public int getRecordBucketPicSelNums() {
        return recordBucketPicSelNums;
    }


    public boolean isInitCurrentId() {
        return isInitCurrentId;
    }

    public void setIsInitCurrentId(boolean isInitCurrentId) {
        this.isInitCurrentId = isInitCurrentId;
    }
}
