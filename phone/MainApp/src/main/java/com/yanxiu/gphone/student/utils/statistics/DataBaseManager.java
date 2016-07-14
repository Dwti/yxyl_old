package com.yanxiu.gphone.student.utils.statistics;

import android.util.Log;

import com.common.core.utils.LogInfo;
import com.yanxiu.gphone.student.bean.statistics.InstantUploadErrorData;
import com.yanxiu.gphone.student.bean.statistics.YanXiuDataBase;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * powered by yanxiu.com
 */
public class DataBaseManager {

    public static DataBaseManager getInstance() {
        return DataBaseManagerHolder.instance;
    }

    private static class DataBaseManagerHolder {
        private static final DataBaseManager instance = new DataBaseManager();
    }

    public void addData(List<YanXiuDataBase> yanXiuDataBaseList) {
        if (yanXiuDataBaseList == null || yanXiuDataBaseList.size() <= 0) {
            Log.e("frc", "传入的表类型为null");
            return;
        } else {
            DataSupport.saveAll(yanXiuDataBaseList);

        }
    }

    public boolean addData(YanXiuDataBase yanXiuDataBase) {
        if (yanXiuDataBase == null) {
            LogInfo.log("frc", "传入的表类型为null");
            return false;
        } else {
            if (yanXiuDataBase.save()) {
                LogInfo.log("frc", "存储成功");
                return true;
            } else {
                LogInfo.log("frc", "存储失败");
                return false;
            }
        }


    }

    private int delData(Class<? extends YanXiuDataBase> clazz) {
        return DataSupport.deleteAll(clazz);
    }

    private int delData(Class<? extends YanXiuDataBase> clazz, long id) {
        return DataSupport.delete(clazz, id);
    }


    /**
     * 查询数据库所有数据
     *
     * @param clazz
     * @return
     */
    public List<? extends YanXiuDataBase> queryAllData(Class<? extends YanXiuDataBase> clazz) {
        return DataSupport.findAll(clazz);

    }

    /**
     * 删除指定表
     *
     * @param clazz
     * @return
     */
    public int delAllData(Class<? extends YanXiuDataBase> clazz) {
        return DataSupport.deleteAll(clazz);

    }


    /**
     * 根据偏移量来查询数据
     *
     * @param clazz
     * @param position
     * @param offset
     * @param asc
     * @return
     */
    public List<? extends YanXiuDataBase> queryByOffset(Class<? extends YanXiuDataBase> clazz, int position, int offset, String asc) {

        return DataSupport.order(asc).limit(position).offset(offset).find(clazz);

    }

    /**
     * 获取上传失败的表中的所有数据
     *
     * @return
     */
    public List<InstantUploadErrorData> findUploadErrorData() {
        List<InstantUploadErrorData> dataList = DataSupport.findAll(InstantUploadErrorData.class);
        DataSupport.deleteAll(InstantUploadErrorData.class);

        return dataList;
    }


}

