package com.yanxiu.gphone.studentold.utils.statistics;

/**
 * Created by FengRongCheng on 2016/6/6 06:49.
 * powered by yanxiu.com
 * 数据统计配置类
 */
public class DataStatisticsConfig {
    /**
     * 上传类型为单点上传
     **/
    public static final int TYPE_SINGLE_POINT_UPLOAD = 0;
    /**
     * 上传类型为多点上传
     **/
    public static final int TYPE_MORE_POINT_UPLOAD = 1;
    /**
     * 上传类型为文件上传
     **/
    public static final int TYPE_FILE_UPLOAD = 2;
    /**
     * 上传类型为开发者调试LOG日志上传
     **/
    public static final int TYPE_DEV_LOG_UPLOAD = 3;
    /**
     * 上传类型为app崩溃日志上传
     **/
    public static final int TYPE_CRASH_LOG_UPLOAD = 4;

    /**
     * 上传失败重传分类
     */
    public enum errorRetryType {
        GIVE_UP, TRY_AGAIN, TRY_THREE_TIMES;

    }

    /**
     * 上传时间
     */
    public enum uploadTime {
        UPLOAD_NOW, UPLOAD_WHEN_OPEN_APP, UPLOAD_WHEN_CRASH;
    }
}
