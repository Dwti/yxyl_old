package com.yanxiu.gphone.student.httpApi;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import com.common.core.utils.BitmapUtil;
import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.utils.MD5;
import com.common.core.utils.NetWorkTypeUtils;
import com.common.login.LoginModel;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.impl.YanxiuHttpBaseParameter;
import com.yanxiu.basecore.impl.YanxiuHttpParameter;
import com.yanxiu.basecore.impl.YanxiuHttpTool;
import com.yanxiu.basecore.parse.YanxiuMainParser;
import com.yanxiu.gphone.student.YanxiuApplication;
import com.yanxiu.gphone.student.bean.PaperTestEntity;
import com.yanxiu.gphone.student.bean.SubjectExercisesItemBean;
import com.yanxiu.gphone.student.bean.UploadFileBean;
import com.yanxiu.gphone.student.bean.UploadImageBean;
import com.yanxiu.gphone.student.bean.UrlBean;
import com.yanxiu.gphone.student.parser.UploadFileParser;
import com.yanxiu.gphone.student.parser.UploadImageParser;
import com.yanxiu.gphone.student.utils.Configuration;
import com.yanxiu.gphone.student.utils.Util;
import com.yanxiu.gphone.student.utils.YanXiuConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/5/19.
 */
public class YanxiuHttpApi {
    public static final String SUBJECT_ID = "subjectId";
    public static final String STAGE_ID = "stageId";
    //一级考点ID
    public static final String CHAPTER_ID = "chapterId";

    public static final boolean isTest = true;   //是否是测试环境
    public static final boolean isDev = false;    //是否是测试环境中的开发环境
    private static UrlBean mUrlBean = null;

    public static void setBaseURL(UrlBean urlBean) {
        if (mUrlBean == null) {
            mUrlBean = urlBean;
        }
    }

    /**
     * 公共参数  KEY
     */
    public interface PUBLIC_PARAMETERS {
        String MOD_KEY = "uid";
        String PCODE_KEY = "pcode";
        String TOKEN = "token";
        String VERSION_KEY = "version";  //App版本号
        String PAGEINDEX_KEY = "pageindex";
        String PAGESIZE_KEY = "pagesize";
        String OS = "osType";
        String OS_VERSION = "osversion"; //操作系统版本号
        String OS_NAME = "os";  //操作系统名称
        String BRAND = "brand"; //手机品牌
        String QUESTION_ID = "quesId"; //题目Id
        String TRACE_UID = "trace_uid";
    }

    /**
     * 正式接口
     */
    public interface OFFICIAL_URL {
        String DYNAMIC_APP_USERURL = "http://mobile.hwk.yanxiu.com/app";
        String DYNAMIC_APP_UPLOAD = "http://mobile.yanxiu.com/v14/api";
    }

    /**
     * /**
     * 测试接口
     */
    public interface TEST_URL {
        String DYNAMIC_DEV_BASE_URL = "http://dev.hwk.yanxiu.com/app";
        String DYNAMIC_TEST_BASE_URL = "http://test.hwk.yanxiu.com/app";
        String DYNAMIC_APP_UPLOAD = "http://mobile.yanxiu.com/test/api";
    }

//    public static String getPublicUrl() {
//        if (Configuration.isForTest()) {
//            return TEST_URL.DYNAMIC_DEV_BASE_URL;
//        } else {
//            return OFFICIAL_URL.DYNAMIC_APP_USERURL;
//        }
//    }

    public static String getPublicUrl() {
        if (mUrlBean != null) {
            return mUrlBean.getServer();
        }
        if (isTest) {
            if (isDev) {
                return TEST_URL.DYNAMIC_DEV_BASE_URL;
            } else {
                return TEST_URL.DYNAMIC_TEST_BASE_URL;
            }
        } else {
            return OFFICIAL_URL.DYNAMIC_APP_USERURL;
        }
    }

    public static String getInitUrl() {
        if (mUrlBean != null) {
            return mUrlBean.getUploadServer();
        } else {
            if (isTest) {
                return TEST_URL.DYNAMIC_APP_UPLOAD;
            } else {
                return OFFICIAL_URL.DYNAMIC_APP_UPLOAD;
            }
        }

    }

    private static String getStaticHead() {
        if (mUrlBean != null) {
            return mUrlBean.getServer();
        } else {
            return getPublicUrl();
        }
    }

    private static String getStaticEnd() {
        return "";
    }

    /**
     * 根据参数，调起请求
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> request(
            YanxiuHttpBaseParameter<T, D, ?> httpParameter) {
        YanxiuHttpTool<T> handler = new YanxiuHttpTool<T>();
        return handler.requsetData(httpParameter);
    }

    /**
     * 生成请求地址公用方法
     *
     * @param httpParameter
     * @param <T>
     * @param <D>
     * @return
     */
    private static <T extends YanxiuBaseBean, D> String generatePath(YanxiuHttpParameter<T, D> httpParameter) {
        return httpParameter.getBaseUrl() + httpParameter.encodeUrl();
    }


    //===================================================================================================
    //===================================================================================================

    /**
     * 查询班级信息
     *
     * @param updataId
     * @param parser
     * @param classId
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestClassInfo(
            int updataId, String classId, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/class/searchClass.do";

        Bundle params = new Bundle();
        params.putString("classId", classId);
//        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
//        params.putString(PUBLIC_PARAMETERS.TOKEN, "a059f68dbee0fc654c884a1f2bb7798b");
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        LogInfo.log("king", "查询班级信息接口 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 获取群组详细信息
     *
     * @param updataId
     * @param parser
     * @param groupId
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestGroupInfoDetail(
            int updataId, long groupId, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/group/getGroupDetail.do";

        Bundle params = new Bundle();
        params.putString("groupId", groupId + "");
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        LogInfo.log("king", "查询groupInfo详细接口 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }


    /**
     * 加入班级
     *
     * @param updataId
     * @param parser
     * @param classId
     * @param validMsg
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestAddClass(
            int updataId, long classId, String validMsg, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/class/joinClass.do";

        Bundle params = new Bundle();
        params.putString("classId", classId + "");
        params.putString("validMsg", validMsg);
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        LogInfo.log("king", "加入班级接口 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestNewAddClass(
            int updataId, long classId, int stageid, int areaid, int cityid, String mobile,
            String realname, int schoolid, String schoolName, int provinceid, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/user/registerByJoinClass.do";

        Bundle params = new Bundle();
        params.putString("classId", classId + "");
        params.putString("stageid", stageid + "");
        if (areaid != 0) {
            params.putString("areaid", areaid + "");
        }
        if (cityid != 0) {
            params.putString("cityid", cityid + "");
        }
        params.putString("mobile", mobile + "");
        params.putString("realname", realname);
        params.putString("schoolid", schoolid + "");
        params.putString("schoolName", schoolName);
        if (provinceid != 0) {
            params.putString("provinceid", provinceid + "");
        }
        String validKey = MD5.toMd5(mobile + "&" + "yxylmobile");
        params.putString("validKey", validKey);
        params.putString("deviceId", CommonCoreUtil.getAppDeviceId(YanxiuApplication.getInstance()));
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        LogInfo.log("king", "加入班级接口 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> thirdRequestNewAddClass(
            int updataId, int classId, int stageid, int areaid, int cityid,
            String openid, String realname, int schoolid, String pltform, String schoolName,
            String headimg, int sex, int provinceid, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/user/thirdRegisterByJoinClass.do";

        Bundle params = new Bundle();

        params.putString("stageid", stageid + "");
        if (areaid != 0) {
            params.putString("areaid", areaid + "");
        }
        if (cityid != 0) {
            params.putString("cityid", cityid + "");
        }
        params.putString("openid", openid + "");
        params.putString("realname", realname);
        params.putString("schoolid", schoolid + "");
        params.putString("pltform", pltform);
        params.putString("sex", sex + "");
        params.putString("schoolName", schoolName);
        params.putString("classId", classId + "");
        if (provinceid != 0) {
            params.putString("provinceid", provinceid + "");
        }
        params.putString("headimg", headimg);
        params.putString("deviceId", CommonCoreUtil.getAppDeviceId(YanxiuApplication.getInstance()));
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        LogInfo.log("king", "加入班级接口 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 退出班级
     *
     * @param updataId
     * @param parser
     * @param classId
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestExitClass(
            int updataId, long classId, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/class/exitClass.do";

        Bundle params = new Bundle();
        params.putString("classId", classId + "");
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        LogInfo.log("king", "退出班级接口 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 取消申请班级
     *
     * @param updataId
     * @param parser
     * @param classId
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestCancelClass(
            int updataId, long classId, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/class/cancelReply.do";

        Bundle params = new Bundle();
        params.putString("classId", classId + "");
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        LogInfo.log("king", "取消申请班级接口 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * "作业"小红点获取待完成作业数量接口
     *
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */

    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestGroupWaitfinishHwkNum(YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/class/getWaitfinishHwknum.do";

        Bundle params = new Bundle();
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, 0);
        return request(httpParameter);
    }

    /**
     * 待完成作业列表
     *
     * @param updataId
     * @param parser
     * @param page     当前页码
     * @param pagesize
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestGroupHwUndoList(
            int updataId, int page, int pagesize, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/class/listUnfinishPapers.do";

        Bundle params = new Bundle();
        params.putString("page", page + "");
        params.putString("pageSize", pagesize + "");
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        LogInfo.log("king", "待完成作业列表接口 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 作业展示页
     *
     * @param updataId
     * @param parser
     * @param groupId
     * @param page     当前页码
     * @param pagesize
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestGroupHwList(
            int updataId, int groupId, int page, int pagesize, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/class/listGroupPaper.do";

        Bundle params = new Bundle();
        params.putString("groupId", groupId + "");
        params.putString("page", page + "");
        params.putString("pageSize", pagesize + "");
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        LogInfo.log("king", "作业展示页接口 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 班级学科
     *
     * @param updataId
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestGroupList(
            int updataId, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/class/listGroups.do";

        Bundle params = new Bundle();
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        LogInfo.log("king", "班级学科接口 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 从列表中将“审核未通过删除“
     *
     * @param updataId
     * @param parser
     * @param groupId
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestDelGroupItem(
            int updataId, int groupId, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/group/delGroupFromList.do";

        Bundle params = new Bundle();
        params.putString("groupId", groupId + "");
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        LogInfo.log("king", "审核未通过删除接口 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 获得学科信息
     *
     * @param updataId
     * @param parser
     * @param stageId
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestSubjectInfo(
            int updataId, int stageId, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/common/getSubject.do";

        Bundle params = new Bundle();
        params.putString("stageId", stageId + "");
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        LogInfo.log("king", "获得学科信息 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }


    /**
     * 获得教材版本信息
     *
     * @param updataId
     * @param parser
     * @param stageId
     * @param subjectId
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestEditionInfo(
            int updataId, String stageId, String subjectId, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/common/getEditions.do";

        Bundle params = new Bundle();
        params.putString("stageId", stageId);
        params.putString("subjectId", subjectId);
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        LogInfo.log("geny", "获得教材版本信息 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }


    /**
     * 学生登录接口
     *
     * @param updataId
     * @param parser
     * @param mobile
     * @param password
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestLoginInfo(
            int updataId, String mobile, String password, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/user/login.do";

        Bundle params = new Bundle();
        params.putString("mobile", mobile);
        params.putString("password", password);
        params.putString("deviceId", CommonCoreUtil.getAppDeviceId(YanxiuApplication.getInstance()));
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.POST, parser, updataId);
        LogInfo.log("king", "学生登录接口 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 学生注册接口
     *
     * @param updataId
     * @param parser
     * @param mobile
     * @param password
     * @param realName
     * @param nickName
     * @param areaId
     * @param schoolId
     * @param stageId
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestRegisterInfo(
            int updataId, String mobile, String password, String realName, String nickName, String provinceId, String cityId,
            String areaId, String schoolId, int stageId, String schoolName, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/user/register.do";

        Bundle params = new Bundle();
        params.putString("mobile", mobile);
        params.putString("password", password);
        params.putString("realname", realName);
        params.putString("nickname", nickName);
        params.putString("provinceid", provinceId);
        params.putString("cityid", cityId);
        params.putString("areaid", areaId);
        params.putString("schoolid", schoolId);
        params.putString("stageid", stageId + "");
        params.putString("schoolName", schoolName);
        params.putString("deviceId", CommonCoreUtil.getAppDeviceId(YanxiuApplication.getInstance()));
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.POST, parser, updataId);
        LogInfo.log("king", "学生注册接口 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 注册第三步新接口
     **/
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestNewRegisterInfo(
            int updataId, String mobile, String realName, String provinceId, String cityId,
            String areaId, String schoolId, int stageId, String schoolName, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/user/registerV2.do";

        Bundle params = new Bundle();
        params.putString("mobile", mobile);
        params.putString("realname", realName);
        params.putString("provinceid", provinceId);
        params.putString("cityid", cityId);
        params.putString("areaid", areaId);
        params.putString("schoolid", schoolId);
        params.putString("stageid", stageId + "");
        params.putString("schoolName", schoolName);
        String validKey = MD5.toMd5(mobile + "&" + "yxylmobile");
        params.putString("validKey", validKey);
        params.putString("deviceId", CommonCoreUtil.getAppDeviceId(YanxiuApplication.getInstance()));
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.POST, parser, updataId);
        LogInfo.log("king", "学生注册新接口 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 重置密码接口
     *
     * @param updataId
     * @param parser
     * @param mobile
     * @param password
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestRetPwdInfo(
            int updataId, String mobile, String password, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/user/resetPassword.do";

        Bundle params = new Bundle();
        params.putString("mobile", mobile);
        params.putString("password", password);
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.POST, parser, updataId);
        LogInfo.log("king", "重置密码接口 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 发送验证码的接口
     *
     * @param updataId
     * @param parser
     * @param mobile
     * @param type
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestProduceGetCode(
            int updataId, String mobile, int type, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/user/produceCode.do";

        Bundle params = new Bundle();
        params.putString("mobile", mobile);
        params.putString("type", type + "");
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        LogInfo.log("king", "发送验证码的接口 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 验证验证码的接口
     *
     * @param updataId
     * @param parser
     * @param mobile
     * @param code
     * @param type
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestValidateProduceCode(
            int updataId, String mobile, String code, int type, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/user/firstStepCommit.do";

        Bundle params = new Bundle();
        params.putString("mobile", mobile);
        params.putString("code", code);
        params.putString("type", type + "");
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        LogInfo.log("king", "验证验证码的接口 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 注册新接口
     **/
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestNewCode(int updataId, String mobile, String password, String code, int type, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/user/firstStepCommitV2.do";

        Bundle params = new Bundle();
        params.putString("mobile", mobile);
        params.putString("code", code);
        params.putString("password", password);
        params.putString("type", type + "");
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.POST, parser, updataId);
        LogInfo.log("king", "注册新接口 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 个人资料相关接口
     *
     * @param updataId
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestUserInfo(
            int updataId, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/personalData/getUser.do";

        Bundle params = new Bundle();
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        return request(httpParameter);
    }

    /**
     * 修改用户信息通用接口
     *
     * @param updataId
     * @param parser
     * @param paramsMap
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestUpdateUserInfo(
            int updataId, YanxiuMainParser<T, D> parser, HashMap<String, String> paramsMap) {
        String baseUrl = getStaticHead() + "/personalData/updateUserInfo.do";

        Bundle params = new Bundle();
        if (paramsMap != null) {
            Iterator iter = paramsMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                params.putString(key, val);
            }
        }
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        return request(httpParameter);
    }

    /**
     * 修改用户密码
     *
     * @param updataId
     * @param parser
     * @param paramsMap
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestModifiedPwd(
            int updataId, YanxiuMainParser<T, D> parser, HashMap<String, String> paramsMap) {
        String baseUrl = getStaticHead() + "/user/modifyPassword.do";

        Bundle params = new Bundle();
        if (paramsMap != null) {
            Iterator iter = paramsMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                params.putString(key, val);
            }
        }
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        return request(httpParameter);
    }

    /**
     * 搜索学校接口
     *
     * @param updataId
     * @param school
     * @param regionId
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestSearchSchool(
            int updataId, String school, String regionId, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/personalData/searchSchool.do";

        Bundle params = new Bundle();
        params.putString("school", school);
        params.putString("regionId", regionId);
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        return request(httpParameter);
    }

    /**
     * 获取练习历史教材版本
     *
     * @param updataId
     * @param stageId
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestPracticeEdition(
            int updataId, String stageId, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/personalData/getPracticeEdition.do";

        Bundle params = new Bundle();
        params.putString("stageId", stageId);
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        LogInfo.log("king", " 获取练习历史教材版本接口 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 获取相应教材版本的练习历史
     *
     * @param updataId
     * @param stageId    学段Id
     * @param subjectId  科目Id
     * @param beditionId 教材版本Id
     * @param volume     年级Id
     * @param nextPage   请求页
     * @param pageSize   一页大小
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestPracticeHistory(
            int updataId, String stageId, String subjectId, String beditionId,
            String volume, int nextPage, int pageSize,
            YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/personalData/getPracticeHistory.do";

        Bundle params = new Bundle();
        params.putString("stageId", stageId);
        params.putString("subjectId", subjectId);
        params.putString("beditionId", beditionId);
        params.putString("volume", volume);
        params.putString("nextPage", nextPage + "");
        params.putString("pageSize", pageSize + "");
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        return request(httpParameter);
    }

    /**
     * 获取考点的练习历史
     *
     * @param updataId
     * @param stageId   学段Id
     * @param subjectId 科目Id
     * @param nextPage  请求页
     * @param pageSize  一页大小
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestPracticeHistoryByKnow(
            int updataId, String stageId, String subjectId, int nextPage, int pageSize,
            YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/personalData/getPracticeHistoryByKnow.do";

        Bundle params = new Bundle();
        params.putString("stageId", stageId);
        params.putString("subjectId", subjectId);
        params.putString("nextPage", nextPage + "");
        params.putString("pageSize", pageSize + "");
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        return request(httpParameter);
    }

    /**
     * 获取试题列表
     *
     * @param updataId
     * @param paperId  练习Id；
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestQuestionList(
            int updataId, String paperId,
            YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/personalData/getQuestionList.do";

        Bundle params = new Bundle();
        params.putString("paperId", paperId);
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        httpParameter.setIsEncode(true);
        LogInfo.log("king", "获取试题列表 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 获取教材版本错题数
     *
     * @param updataId
     * @param stageId
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestMistakeEdition(
            int updataId, String stageId,
            YanxiuMainParser<T, D> parser) {
        //String baseUrl = getStaticHead() + "/personalData/getMistakeEdition.do";
        String baseUrl = getStaticHead() + "/personalData/getSubjectMistakeV2.do";

        Bundle params = new Bundle();
        //params.putString("stageId", stageId);
        params.putString("stageId", "0");
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        return request(httpParameter);
    }

    /**
     * 获取章节错题数
     *
     * @param updataId
     * @param stageId    学段Id
     * @param subjectId  科目Id
     * @param beditionId 教材版本Id
     * @param volume     年级Id
     * @param ptype
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestSectionMistake(
            int updataId, String stageId, String subjectId, String beditionId,
            String volume, int ptype,
            YanxiuMainParser<T, D> parser) {
        //ptype == 0 章节错题 ； ptype == 1  考点错题
        String baseUrl = getStaticHead() + (ptype == 0 ? "/personalData/getSectionMistake.do" : "/personalData/getSectionMistakeByKnow.do");

        Bundle params = new Bundle();
        params.putString("stageId", stageId);
        params.putString("subjectId", subjectId);
        params.putString("beditionId", beditionId);
        params.putString("volume", volume);
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        return request(httpParameter);
    }

    /**
     * 删除错题
     *
     * @param updataId
     * @param questionId
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestDelMistake(
            int updataId, String questionId,
            YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/personalData/delMistake.do";

        Bundle params = new Bundle();
        params.putString("questionId", questionId);
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        return request(httpParameter);
    }


    /**
     * 获得智能练习题目
     *
     * @param updataId
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestIntelliExe(
            int updataId, int stageId, String subjectId, String editionId, String chapterId, String sectionId, int questNum, String volumeId, String cellId,
            YanxiuMainParser<T, D> parser) {
//        http://mobile.hwk.yanxiu.com/app/q/genSectionQBlock.do?token=1&stageId=1204&subjectId=1102&editionId=1406&chapterId=32080&sectionId=0&questNum=2&volumeId=32079
        String baseUrl = getStaticHead() + "/q/genSectionQBlock.do";
//        baseUrl = "http://mobile.hwk.yanxiu.com/internal/testQuestion.do?type_id=5";

        Bundle params = new Bundle();
        params.putString("stageId", String.valueOf(stageId));
        if (!TextUtils.isEmpty(subjectId)) {
            params.putString("subjectId", String.valueOf(subjectId));
        }
        if (!TextUtils.isEmpty(editionId)) {
            params.putString("editionId", String.valueOf(editionId));
        }
        if (!TextUtils.isEmpty(chapterId)) {
            params.putString("chapterId", String.valueOf(chapterId));
        }
        params.putString("questNum", String.valueOf(questNum));
        if (!TextUtils.isEmpty(sectionId)) {
            params.putString("sectionId", String.valueOf(sectionId));
        }

        ////////////////////////////////////////////////////////////////////
        if (!TextUtils.isEmpty(volumeId)) {
            params.putString("volumeId", String.valueOf(volumeId));
        }

        if (!TextUtils.isEmpty(cellId)) {
            params.putString("cellId", String.valueOf(cellId));
        }
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        httpParameter.setIsEncode(true);
        LogInfo.log("geny", "requestIntelliExe url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }


    /**
     * 2.4 保存用户版本信息：
     *
     * @param updataId
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestSaveEditionInfo(
            int updataId, int stageId, String subjectId, String beditionId,
            YanxiuMainParser<T, D> parser) {
//        http://mobile.hwk.yanxiu.com/app/common/saveEditionInfo.do?stageId=1203&subjectId=1102&beditionId=1406&token=ece8f582bfcf9c69d0caac6b05ad5c36
        String baseUrl = getStaticHead() + "/common/saveEditionInfo.do";

        Bundle params = new Bundle();
        params.putString("stageId", String.valueOf(stageId));
        params.putString("subjectId", String.valueOf(subjectId));
        params.putString("beditionId", String.valueOf(beditionId));
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        LogInfo.log("geny", "requestSaveEditionInfo url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }


    /**
     * 2.2 获得章节信息：/app/common/getChapterList.do?stageId=1&volume=23&subjectId=234&editionId=2344
     *
     * @param updataId
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestGetChapterList(
            int updataId, int stageId, String subjectId, String editionId, String volume,
            YanxiuMainParser<T, D> parser) {
//       http://mobile.hwk.yanxiu.com/app/common/getChapterList.do?stageId=1203&volume=31243&subjectId=1102&editionId=1406&token=ece8f582bfcf9c69d0caac6b05ad5c36
        String baseUrl = getStaticHead() + "/common/getChapterList.do";

        Bundle params = new Bundle();
        params.putString("stageId", String.valueOf(stageId));
        params.putString("subjectId", String.valueOf(subjectId));
        params.putString("editionId", String.valueOf(editionId));
        params.putString("volume", String.valueOf(volume));
//        params.putString("version", "1.2");
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        LogInfo.log("geny", "requestGetChapterList url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }


    /**
     * 2.5 获取知识点信息 (ver1.2)
     *
     * @param updataId
     * @param parser
     * @param <T>
     * @param <D>
     * @return knpId1=2345&knpId2=2345&knpId3=3456
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestGetKnpList(
            int updataId, int stageId, String subjectId, YanxiuMainParser<T, D> parser) {
//       http://mobile.hwk.yanxiu.com/app/common/getChapterList.do?stageId=1203&volume=31243&subjectId=1102&editionId=1406&token=ece8f582bfcf9c69d0caac6b05ad5c36
        String baseUrl = getStaticHead() + "/common/getKnpList.do";

        Bundle params = new Bundle();
        params.putString("stageId", String.valueOf(stageId));
        params.putString("subjectId", String.valueOf(subjectId));
//        if(!TextUtils.isEmpty(knpId1)){
//            params.putString("stageId", String.valueOf(stageId));
//        }
//        if(!TextUtils.isEmpty(knpId2)){
//            params.putString("knpId2", String.valueOf(knpId2));
//        }
//        if(!TextUtils.isEmpty(knpId3)){
//            params.putString("knpId3", String.valueOf(knpId3));
//        }
//        if(!TextUtils.isEmpty(fromType)){
//            params.putString("fromType", String.valueOf(fromType));
//        }
//        if(!TextUtils.isEmpty(ana)){
//            params.putString("ana", String.valueOf(ana));
//        }
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        LogInfo.log("geny", "requestGetKnpLis url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }


    /**
     * 2.5 获取知识点信息 (ver1.2)
     *
     * @param updataId
     * @param parser
     * @param <T>
     * @param <D>
     * @return knpId1=2345&knpId2=2345&knpId3=3456
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestGetKnpointQBlock(
            int updataId, int stageId, String subjectId, String knpId1, String knpId2, String knpId3,
            String fromType, YanxiuMainParser<T, D> parser) {
//       http://mobile.hwk.yanxiu.com/app/common/getChapterList.do?stageId=1203&volume=31243&subjectId=1102&editionId=1406&token=ece8f582bfcf9c69d0caac6b05ad5c36
        String baseUrl = getStaticHead() + "/q/genKnpointQBlock.do";

        Bundle params = new Bundle();
        params.putString("stageId", String.valueOf(stageId));
        params.putString("subjectId", String.valueOf(subjectId));
        if (!TextUtils.isEmpty(knpId1)) {
            params.putString("knpId1", String.valueOf(knpId1));
        }
        if (!TextUtils.isEmpty(knpId2)) {
            params.putString("knpId2", String.valueOf(knpId2));
        }
        if (!TextUtils.isEmpty(knpId3)) {
            params.putString("knpId3", String.valueOf(knpId3));
        }
        if (!TextUtils.isEmpty(fromType)) {
            params.putString("fromType", String.valueOf(fromType));
        }
//        if(!TextUtils.isEmpty(ana)){
//            params.putString("ana", String.valueOf(ana));
//        }
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        httpParameter.setIsEncode(true);
        LogInfo.log("geny", "requestGetKnpLis url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 3. 获得章错题
     * 请求接口（post || get）:
     * /app/q/getChapterWrongQ.do
     * 参数：
     * stageId 学段id
     * subjectId 学科id
     * editionId 教材版本
     * volumeId 册
     * chapterId 章id
     * pageSize 页面大小
     * currentPage 请求页面索引
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestWrongQuestion(
            int updataId, String stageId, String subjectId, String editionId, String volumeId, String chapterId,
            String sectionId, int pageSize, int currentPage, String currentId, String cellId, int ptype,
            YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/q/getWrongQs.do";

        Bundle params = new Bundle();
        params.putString("stageId", stageId);
        params.putString("subjectId", subjectId);
        params.putString("editionId", editionId);
        params.putString("volumeId", volumeId);
        params.putString("chapterId", chapterId);

        params.putString("sectionId", sectionId);

        params.putString("pageSize", String.valueOf(pageSize));
        params.putString("currentPage", String.valueOf(currentPage));//从 1 开始请求

        if (!TextUtils.isEmpty(currentId) && !"0".equals(currentId)) {
            params.putString("currentId", currentId);
        }
        if (!TextUtils.isEmpty(cellId) && !"0".equals(cellId)) {
            params.putString("cellId", cellId);
        }
        params.putString("ptype", ptype + "");
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        httpParameter.setIsEncode(true);
        LogInfo.log("geny", "requestChapterWrongQ url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 获得学科错题
     * 请求接口（post || get）:
     * /app/q/getChapterWrongQ.do
     * 参数：
     * stageId 学段id
     * subjectId 学科id
     * pageSize 页面大小
     * currentPage 请求页面索引
     * ptype  请求类型，2按时间排序倒序（默认）
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestWrongAllQuestion(
            int updataId, String stageId, String subjectId, int pageSize, int currentPage, String currentId, int ptype,
            YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/q/getWrongQsV2.do";

        Bundle params = new Bundle();
        params.putString("stageId", stageId);
        params.putString("subjectId", subjectId);

        params.putString("pageSize", String.valueOf(pageSize));
        params.putString("currentPage", String.valueOf(currentPage));//从 1 开始请求

        if (!TextUtils.isEmpty(currentId) && !"0".equals(currentId)) {
            params.putString("currentId", currentId);
        }

        params.putString("ptype", ptype + "");
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        httpParameter.setIsEncode(true);
        LogInfo.log("geny", "requestChapterWrongQ url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }


    //    //    1 提交答题：
//    //    提交作业： /app/q/submitQ.do?answers=&token=kk&ppId=344
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestsubmitQ(
            int updataId, SubjectExercisesItemBean bean, int ppstatus, YanxiuMainParser<T, D> parser) {
        //       http://mobile.hwk.yanxiu.com/app/common/getChapterList.do?stageId=1203&volume=31243&subjectId=1102&editionId=1406&token=ece8f582bfcf9c69d0caac6b05ad5c36
        String baseUrl = getStaticHead() + "/q/submitQ.do";
        String id = "-1";
        String childId = "-1";
        String paperTestId = "-1";
        Bundle params = new Bundle();
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
//        params.putString("ppId", String.valueOf(ppId));
        JSONObject node = new JSONObject();
        try {
            node.put("chapterId", bean.getData().get(0).getChapterid());
            node.put("ptype", bean.getData().get(0).getPtype());
//            node.put("ppstatus", String.valueOf(ppstatus));
            int count = bean.getData().get(0).getPaperTest().size();
            JSONArray array = new JSONArray();

            for (int i = 0; i < count; i++) {
//                LogInfo.log("geny", i + "---answer " + bean.getData().get(0).getPaperTest().get(i).getQuestions().getAnswerBean().getAnswerStr());
                JSONObject questionBean = new JSONObject();
                //if(bean.getData().get(0).getPaperTest().get(i).getQuestions().getChildren() != null && bean.getData().get(0).getPaperTest().get(i).getQuestions().getType_id() == QUESTION_READING.type){
                if (bean.getData().get(0).getPaperTest().get(i).getQuestions().getTemplate().equals(YanXiuConstant.MULTI_QUESTION)
                        || bean.getData().get(0).getPaperTest().get(i).getQuestions().getTemplate().equals(YanXiuConstant.CLOZE_QUESTION)
                        || bean.getData().get(0).getPaperTest().get(i).getQuestions().getTemplate().equals(YanXiuConstant.LISTEN_QUESTION)) {
                    List<PaperTestEntity> questionList = bean.getData().get(0).getPaperTest().get(i).getQuestions().getChildren();
                    if (questionList == null || questionList.isEmpty())
                        continue;
                    int childrenCount = questionList.size();
                    boolean isFalse = false;
                    boolean isFinish = false;
                    JSONArray childrenQuestionBean = new JSONArray();
                    JSONArray childrenBean = new JSONArray();
                    for (int j = 0; j < childrenCount; j++) {
                        JSONObject answerJson = new JSONObject();
                        answerJson.put("answer", Util.sortQuestionData(questionList.get(j).getQuestions()));
                        answerJson.put("qid", bean.getData().get(0).getPaperTest().get(i).getQuestions().getChildren().get(j).getQuestions().getId());
                        childrenQuestionBean.put(answerJson);
                        questionBean.put("answer", childrenQuestionBean);
                        isFinish = isFinish || questionList.get(j).getQuestions().getAnswerBean().isFinish();
                        isFalse = isFalse || !questionList.get(j).getQuestions().getAnswerBean().isRight();
                        JSONObject childJson = new JSONObject();
                        if (bean.getData().get(0).getPaperTest().get(i).getQuestions().getChildren().get(j).getQuestions().getPad() != null) {
                            childId = String.valueOf(bean.getData().get(0).getPaperTest().get(i).getQuestions().getChildren().get(j).getQuestions().getPad().getId());
                        }
                        childJson.put("id", childId);
                        childJson.put("qid", bean.getData().get(0).getPaperTest().get(i).getQuestions().getChildren().get(j).getQuestions().getId());
                        //childJson.put("qtype", bean.getData().get(0).getPaperTest().get(i).getQuestions().getChildren().get(j));
                        childJson.put("costtime", bean.getData().get(0).getPaperTest().get(i).getQuestions().getChildren().get(j).getQuestions().getAnswerBean().getConsumeTime());
                        childJson.put("ptid", bean.getData().get(0).getPaperTest().get(i).getQuestions().getChildren().get(j).getId());
                        childJson.put("status", bean.getData().get(0).getPaperTest().get(i).getQuestions().getChildren().get(j).getQuestions().getAnswerBean().getStatus());
                        childJson.put("uid", LoginModel.getUid());
                        childJson.put("answer", Util.sortQuestionData(questionList.get(j).getQuestions()));
                        childrenBean.put(childJson);
                        questionBean.put("children", childrenBean);
                    }
                    if (isFinish) {
                        bean.getData().get(0).getPaperTest().get(i).getQuestions().getAnswerBean().setIsFinish(isFinish);
                    }
                    if (isFalse) {
                        bean.getData().get(0).getPaperTest().get(i).getQuestions().getAnswerBean().setIsRight(!isFalse);
                    }
                } else {
                    questionBean.put("answer", Util.sortQuestionData(bean.getData().get(0).getPaperTest().get(i).getQuestions()));
                }
                questionBean.put("costtime", bean.getData().get(0).getPaperTest().get(i).getQuestions().getAnswerBean().getConsumeTime());
                LogInfo.log("geny", i + "=====costtime =   " + bean.getData().get(0).getPaperTest().get(i).getQuestions().getAnswerBean().getConsumeTime());
                questionBean.put("ptid", bean.getData().get(0).getPaperTest().get(i).getId());
                questionBean.put("qid", bean.getData().get(0).getPaperTest().get(i).getQuestions().getId());
                if (bean.getData().get(0).getPaperTest().get(i).getQuestions().getPad() != null && bean.getData().get(0).getPaperTest().get(i).getQuestions().getPad().getId() != -1) {
                    id = String.valueOf(bean.getData().get(0).getPaperTest().get(i).getQuestions().getPad().getId());
                    questionBean.put("id", id);
                } else {
                    questionBean.put("id", id);
                }
                questionBean.put("status", bean.getData().get(0).getPaperTest().get(i).getQuestions().getAnswerBean().getStatus());
                questionBean.put("uid", LoginModel.getUid());
                //questionBean.put("children", bean.getData().get(0).getPaperTest().get(i).getQuestions().getChildren());
                array.put(questionBean);


            }
            node.put("paperDetails", array);

            JSONObject paperStatusNode = new JSONObject();
            paperStatusNode.put("begintime", bean.getBegintime());
            paperStatusNode.put("endtime", bean.getEndtime());
//            paperStatusNode.put("costtime", bean.getEndtime() - bean.getBegintime());
            paperStatusNode.put("costtime", bean.getData().get(0).getPaperStatus().getCosttime());
            paperStatusNode.put("ppid", bean.getData().get(0).getId());
            if (bean.getData().get(0).getPaperStatus() != null && bean.getData().get(0).getPaperStatus().getId() != 0) {
                paperTestId = String.valueOf(bean.getData().get(0).getPaperStatus().getId());
                paperStatusNode.put("id", paperTestId);
            } else {
                paperStatusNode.put("id", paperTestId);
            }
            ////////这两个属性写死
            paperStatusNode.put("status", String.valueOf(ppstatus));
            paperStatusNode.put("tid", 0);
            //////
            paperStatusNode.put("uid", LoginModel.getUid());

            node.put("paperStatus", paperStatusNode);

            LogInfo.log("geny", "requestsubmitQ json = " + node.toString());
            params.putString("answers", node.toString());
//            params.putString("answers", URLEncoder.encode(node.toString(), "UTF-8"));
        } catch (JSONException e) {
            if (e != null)
                LogInfo.log("geny", "requestsubmitQ JSONException =   " + e.getMessage());
        } catch (Exception e) {
            if (e != null)
                LogInfo.log("geny", "requestsubmitQ Exception =   " + e.getMessage());
        }

        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.POST, parser, updataId);
        LogInfo.log("geny", "requestsubmitQ url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 获取试题列表
     *
     * @param updataId
     * @param parser
     * @param <T>
     * @param <D>
     * @param paperId
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestGetQuestionList(
            int updataId, String paperId, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/personalData/getQuestionList.do";

        Bundle params = new Bundle();
        params.putString("paperId", paperId);
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);

        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        httpParameter.setIsEncode(true);
        LogInfo.log("king", "获取试题列表 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }


    /**
     * /app/q/getQReport.do?ppid=1233&flag=0
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestGetQReport(
            int updataId, String ppid, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/q/getQReport.do";

        Bundle params = new Bundle();
        params.putString("ppid", ppid);
        LogInfo.log("geny", "getQReport ppid = " + ppid);
        params.putString("flag", "1");
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);

        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        httpParameter.setIsEncode(true);
        LogInfo.log("geny", "getQReport url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 开机接口
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestInitialize(int updataId, String content, YanxiuMainParser<T, D> parser) {

        String baseUrl = getInitUrl() + "/initialize";

        Bundle params = new Bundle();

        //params.putString("did",YanXiuConstant.DEVICEID);
        params.putString("did", CommonCoreUtil.getAppDeviceId(YanxiuApplication.getInstance()));
        params.putString("brand", YanXiuConstant.BRAND);
        params.putString("nettype", NetWorkTypeUtils.getNetWorkType());
        params.putString("osType", YanXiuConstant.OS_TYPE);
        params.putString("os", YanXiuConstant.OS);
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString("token", LoginModel.getToken());
        params.putString("appVersion", YanXiuConstant.VERSION);
        params.putString("content", content);
        params.putString("mode", Configuration.isForTest() ? "test" : "release");
        params.putString("operType", YanXiuConstant.OPERTYPE);
        params.putString("phone", LoginModel.getUserinfoEntity().getMobile() == null ? "-" : LoginModel.getUserinfoEntity().getMobile());
        params.putString("remoteIp", "");
        params.putString("productLine", YanXiuConstant.PRODUCTLINE + "");
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl,
                params, YanxiuHttpBaseParameter.Type.GET, parser, updataId);
        LogInfo.log("king", "开机接口 = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestFeedBackReport(
            int updataId, String quesId, YanxiuMainParser<T, D> parser) {
        String baseUrl = "http://mobile.hwk.yanxiu.com/internal/feedback.do";

        Bundle params = new Bundle();
        params.putString("quesId", quesId);
        params.putString("mobile", LoginModel.getUserinfoEntity().getMobile());
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        LogInfo.log("king", "反馈错题接口 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 头像上传
     *
     * @param files
     * @param listener
     * @return
     */
    public static AsyncTask requestUploadFile(final Map<String, File> files, final UploadFileListener listener) {
        AsyncTask task = new AsyncTask<Object, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Object... params) {
                // /app/common/uploadHeadImg.do?token=3kdkkslkdn0ldjk
                final String actionUrl = getStaticHead() + "/common/uploadHeadImg.do?";
                StringBuilder sb = new StringBuilder(actionUrl);
                sb.append("token=").append(LoginModel.getToken());
//                sb.append(PUBLIC_PARAMETERS.TRACE_UID+"=").append(LoginModel.getUid()+"");
                LogInfo.log("haitian", sb.toString());
                String result = reuqestUploadFile(sb.toString(), files, listener);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (TextUtils.isEmpty(s)) {
                    listener.onFail(null);
                } else {
                    LogInfo.log("haitian", "requestUploadFile s =" + s);
                    UploadFileBean bean = null;
                    try {
                        JSONObject obj = new JSONObject(s);
                        UploadFileParser parser = new UploadFileParser();
                        bean = parser.parse(obj);
                        if (bean.getStatus() != null && bean.getStatus().getCode() == 0) {
                            listener.onSuccess(bean);
                        } else {
                            listener.onFail(bean);
                        }
                    } catch (Exception e) {
                        listener.onFail(bean);
                    }
                }
            }
        };
        task.execute();
        return task;
    }


    /**
     * 上传bitmap
     *
     * @param actionUrl
     * @param files
     * @return
     */
    public static String reuqestUploadBitmap(String actionUrl, Map<String, File> files, final UploadFileListener listener) {
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";
        InputStream in = null;
        StringBuilder sb2 = null;
        try {
            URL uri = new URL(actionUrl);
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
            conn.setReadTimeout(5 * 1000); // 缓存的最长时
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
            conn.setUseCaches(false); // 不允许使用缓
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(60 * 1000);
//            conn.setChunkedStreamingMode(1024);//设置输出块数据大小
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charsert", CHARSET);
//            conn.setRequestProperty("Content-Length", "1048576");
            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
                    + ";boundary=" + BOUNDARY);
            DataOutputStream outStream = new DataOutputStream(
                    conn.getOutputStream());
            if (files != null) {
                for (Map.Entry<String, File> file : files.entrySet()) {
                    StringBuilder sb1 = new StringBuilder();
                    sb1.append(PREFIX);
                    sb1.append(BOUNDARY);
                    sb1.append(LINEND);
                    sb1.append("Content-Disposition: form-data; " +
                            "name=\"" + file.getKey() + "\"; " +
                            "filename=\""
                            + file.getValue().getName() + "\"" + LINEND);
                    sb1.append("Content-Type: application/octet-stream" + LINEND);
                    sb1.append(LINEND);
                    LogInfo.log("geny", sb1.toString());
                    outStream.write(sb1.toString().getBytes());

                    ByteArrayInputStream bis = BitmapUtil.compressImage(file.getValue().getAbsolutePath());

//                    InputStream is = new FileInputStream(file.getValue());
                    byte[] buffer = new byte[1024 * 128];
                    int len = 0;
                    float size = file.getValue().length();
                    LogInfo.log("MediaUtils", "UploadFileListener size =" + size);
                    float uploadSize = 0;
                    int progress = 0;
                    int tempProgress = progress;
                    while ((len = bis.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                        uploadSize += len;
                        progress = (int) ((uploadSize * 100) / size);
                        if (tempProgress != progress) {
                            if (listener != null) {
                                listener.onProgress(progress);
                            }
                            tempProgress = progress;
                        }
                    }
                    bis.close();
                    outStream.write(LINEND.getBytes());
                }
            }
            // 请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            outStream.write(end_data);
            outStream.flush();
            int res = conn.getResponseCode();
            in = null;
            sb2 = new StringBuilder();
            LogInfo.log("geny", "请求结束标志" + res);
            if (res == 200) {
                LogInfo.log("geny", "请求结束标志 -- 200");
                in = conn.getInputStream();
                byte[] buf = new byte[512];
                int n = 0;
                while ((n = in.read(buf)) != -1) {
                    sb2.append(new String(buf));
                }
            }
            if (in != null && outStream != null && conn != null) {
                outStream.close();
                in.close();
                conn.disconnect();
            }
        } catch (InterruptedIOException e) {
            if (e != null) {
                LogInfo.log("geny", "InterruptedIOException msg =" + e.getMessage());
            }
        } catch (Exception e) {
            if (e != null) {
                LogInfo.log("geny", "Exception msg =" + e.getMessage());
            }
        }
//        LogInfo.log("geny", "请求结束标志 -- sb" + sb2.toString());
        return in == null ? null : sb2.toString();
    }

    /**
     * 上传文件接口
     *
     * @param actionUrl
     * @param files
     * @return 注意params 为空  次接口只有文件的是post其他为get
     */
    public static String reuqestUploadFile(String actionUrl, Map<String, File> files, final UploadFileListener
            listener) {
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";
        InputStream in = null;
        StringBuilder sb2 = null;
        try {
            URL uri = new URL(actionUrl);
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
            conn.setReadTimeout(5 * 1000); // 缓存的最长时
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
            conn.setUseCaches(false); // 不允许使用缓
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(60 * 1000);
//            conn.setChunkedStreamingMode(1024);//设置输出块数据大小
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charsert", CHARSET);
//            conn.setRequestProperty("Content-Length", "1048576");
            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
                    + ";boundary=" + BOUNDARY);
            DataOutputStream outStream = new DataOutputStream(
                    conn.getOutputStream());
            if (files != null) {
                for (Map.Entry<String, File> file : files.entrySet()) {
                    StringBuilder sb1 = new StringBuilder();
                    sb1.append(PREFIX);
                    sb1.append(BOUNDARY);
                    sb1.append(LINEND);
                    sb1.append("Content-Disposition: form-data; " +
                            "name=\"" + file.getKey() + "\"; " +
                            "filename=\""
                            + file.getValue().getName() + "\"" + LINEND);
                    sb1.append("Content-Type: application/octet-stream" + LINEND);
                    sb1.append(LINEND);
                    LogInfo.log("geny", sb1.toString());
                    outStream.write(sb1.toString().getBytes());

                    InputStream is = new FileInputStream(file.getValue());
                    byte[] buffer = new byte[1024 * 128];
                    int len = 0;
                    float size = file.getValue().length();
                    LogInfo.log("geny", "UploadFileListener size =" + size);
                    float uploadSize = 0;
                    int progress = 0;
                    int tempProgress = progress;
                    while ((len = is.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                        uploadSize += len;
                        progress = (int) ((uploadSize * 100) / size);
                        if (tempProgress != progress) {
                            if (listener != null) {
                                listener.onProgress(progress);
                            }
                            tempProgress = progress;
                        }
                    }
                    is.close();
                    outStream.write(LINEND.getBytes());
                }
            }
            // 请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            outStream.write(end_data);
            outStream.flush();
            int res = conn.getResponseCode();
            in = null;
            sb2 = new StringBuilder();
            LogInfo.log("geny", "请求结束标志" + res);
            if (res == 200) {
                LogInfo.log("geny", "请求结束标志 -- 200");
                in = conn.getInputStream();
                byte[] buf = new byte[512];
                int n = 0;
                while ((n = in.read(buf)) != -1) {
                    sb2.append(new String(buf));
                }
            }
            if (in != null && outStream != null && conn != null) {
                outStream.close();
                in.close();
                conn.disconnect();
            }
        } catch (InterruptedIOException e) {
            if (e != null) {
                LogInfo.log("geny", "InterruptedIOException msg =" + e.getMessage());
            }
        } catch (Exception e) {
            if (e != null) {
                LogInfo.log("geny", "Exception msg =" + e.getMessage());
            }
        }
//        LogInfo.log("geny", "请求结束标志 -- sb" + sb2.toString());
        return in == null ? null : sb2.toString();
    }

    /**
     * 获取科目版本收藏数
     *
     * @param updataId
     * @param stageId
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestFavouriteEdition(
            int updataId, String stageId,
            YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/personalData/getFavoriteBedition.do";

        Bundle params = new Bundle();
        params.putString("stageId", stageId);
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        return request(httpParameter);
    }

    /**
     * 获取章节收藏数
     *
     * @param updataId
     * @param stageId    学段Id
     * @param subjectId  科目Id
     * @param beditionId 教材版本Id
     * @param volume     年级Id
     * @param ptype      ptype=0代表章节  ptype=1代表知识点
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestSectionFavourite(
            int updataId, String stageId, String subjectId, String beditionId,
            String volume, int ptype,
            YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/personalData/getSectionFavorite.do";

        Bundle params = new Bundle();
        params.putString("stageId", stageId);
        params.putString("subjectId", subjectId);
        params.putString("beditionId", beditionId);
        params.putString("volumeId", volume);
        params.putString("ptype", ptype + "");
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        return request(httpParameter);
    }

    /**
     * 获取收藏题目
     *
     * @param updataId
     * @param stageId
     * @param subjectId
     * @param editionId
     * @param volumeId
     * @param chapterId
     * @param sectionId
     * @param pageSize
     * @param currentPage
     * @param currentId
     * @param parser
     * @param cellId      小节ID
     * @param ptype
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestFavouriteQuestion(
            int updataId, String stageId, String subjectId, String editionId, String volumeId, String chapterId,
            String sectionId, int pageSize, int currentPage, String currentId, String cellId, int ptype,
            YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/personalData/getFavoriteQ.do";

        Bundle params = new Bundle();
        params.putString("stageId", stageId);
        params.putString("subjectId", subjectId);
        params.putString("beditionId", editionId);
        params.putString("volumeId", volumeId);
        params.putString("chapterId", chapterId);

        params.putString("sectionId", sectionId);

        params.putString("pageSize", String.valueOf(pageSize));
        params.putString("currentPage", String.valueOf(currentPage));//从 1 开始请求

        if (!TextUtils.isEmpty(currentId) && !"0".equals(currentId)) {
            params.putString("currentId", currentId);
        }
        if (!TextUtils.isEmpty(cellId) && !"0".equals(cellId)) {
            params.putString("cellId", cellId);
        }
        params.putString("ptype", ptype + "");
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        httpParameter.setIsEncode(true);
        LogInfo.log("geny", "requestFavouriteQuestion url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 增加收藏
     *
     * @param updataId
     * @param stageId
     * @param subjectId
     * @param beditionId
     * @param volumeId
     * @param chapterId
     * @param sectionId
     * @param questionId
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestSubmitFavourite(
            int updataId, String stageId, String subjectId, String beditionId, String volumeId, String chapterId, String sectionId, String questionId, String cellId, int ptype,
            YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/personalData/addFavoriteQ.do";

        Bundle params = new Bundle();
        params.putString("stageId", stageId);
        params.putString("subjectId", subjectId);
        params.putString("beditionId", beditionId);
        params.putString("volumeId", volumeId);
        params.putString("chapterId", chapterId);
        params.putString("sectionId", sectionId);
        params.putString("questionId", questionId);
        params.putString("cellId", cellId);
        params.putString("ptype", String.valueOf(ptype));

        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        LogInfo.log("geny", "requestFavouriteQuestion url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 删除收藏
     *
     * @param updataId
     * @param questionId
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestDelFavourite(
            int updataId, String questionId,
            YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/personalData/deleteFavoriteQ.do";

        Bundle params = new Bundle();
        params.putString("questionId", questionId);
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        LogInfo.log("geny", "requestFavouriteQuestion url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 批量删除收藏
     *
     * @param updataId
     * @param quesArrayStr
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestDelFavouriteList(
            int updataId, String quesArrayStr,
            YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/personalData/deleteFavoriteQs.do";

        Bundle params = new Bundle();
        params.putString("data", quesArrayStr);//data=[1,2,3,4]
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, YanXiuConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        LogInfo.log("geny", "requestFavouriteQuestion url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * @param files
     * @param listener
     * @return
     */
    public static AsyncTask requestUploadImage(final Map<String, File> files, final UploadFileListener listener) {
        AsyncTask task = new AsyncTask<Object, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Object... params) {
                // /app/common/uploadHeadImg.do?token=3kdkkslkdn0ldjk
                final String actionUrl = getStaticHead() + "/common/uploadImgs.do?";
                StringBuilder sb = new StringBuilder(actionUrl);
                sb.append("token=").append(LoginModel.getToken());
//                sb.append(PUBLIC_PARAMETERS.TRACE_UID+"=").append(LoginModel.getUid()+"");
                LogInfo.log("geny", sb.toString());
                String result = reuqestUploadBitmap(sb.toString(), files, listener);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (TextUtils.isEmpty(s)) {
                    listener.onFail(null);
                } else {
                    LogInfo.log("geny", "requestUploadFile s =" + s);
                    UploadImageBean bean = null;
                    try {
                        JSONObject obj = new JSONObject(s);
                        UploadImageParser parser = new UploadImageParser();
                        bean = parser.parse(obj);
                        if (bean.getStatus() != null && bean.getStatus().getCode() == 0) {
                            listener.onSuccess(bean);
                        } else {
                            listener.onFail(bean);
                        }
                    } catch (Exception e) {
                        listener.onFail(bean);
                    }
                }
            }
        };
        task.execute();
        return task;
    }

    public interface UploadFileListener<T extends YanxiuBaseBean> {
        void onFail(T bean);

        void onSuccess(T bean);

        void onProgress(int progress);
    }

    /**
     * 请求排行榜
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestRankingList(int updataId, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/q/weekRank.do";
        Bundle params = new Bundle();
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        LogInfo.log("lee", generatePath(httpParameter));
        return request(httpParameter);
    }


    //*****************************************************第三方登录接口start***************************************************//

    /**
     * 根据微信code换取token
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestWXToken(
            String code, String appid, String secret, String grandType, YanxiuMainParser<T, D> parser) {
        String baseUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";

        Bundle params = new Bundle();
        params.putString("appid", appid);
        params.putString("secret", secret);
        params.putString("code", code);
        params.putString("grant_type", grandType);

        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, 0);
        LogInfo.log("king", "wechat token = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 获取微信用户信息
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestWXUserInfo(String access_token,
                                                                                    String openid, YanxiuMainParser<T, D> parser) {
        String baseUrl = "https://api.weixin.qq.com/sns/userinfo";
        Bundle params = new Bundle();
        params.putString("access_token", access_token);
        params.putString("openid", openid);
        params.putString("lang", "zh_CN");

        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, 0);
        LogInfo.log("king", "wechat userinfo = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 第三方登录接口（目前仅支持 qq/weixin）
     * openid 用户第三方平台唯一标示
     * platform 平台id(qq标示qq平台， weixin标示微信平台)
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestThirdLoginUserInfo(String openid,
                                                                                            String uniqid, String platfor, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/user/oauthLogin.do";
        Bundle params = new Bundle();
        params.putString("openid", openid);
        params.putString("uniqid", uniqid);
        params.putString("platform", platfor);
        params.putString("deviceId", CommonCoreUtil.getAppDeviceId(YanxiuApplication.getInstance()));

        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, 0);
        LogInfo.log("king", "第三方登录接口 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 第三方登录注册接口（仅适用第三方登录注册）
     * openid 用户第三方平台唯一标示
     * platform 平台id(qq标示qq平台， weixin标示微信平台)
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestThirdRegister(String nickName,
                                                                                       String sex, String headimg, String openid, String uniqid, String pltform, String deviceId, String realname,
                                                                                       String provinceid, String schoolname, String schoolid, String cityid, String areaid, String stageid, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/user/thirdRegister.do";
        Bundle params = new Bundle();
        params.putString("openid", openid);
        params.putString("openid", openid);
        params.putString("nickName", nickName);
        params.putString("realname", realname);
        params.putString("sex", sex);
        params.putString("headimg", headimg);
        params.putString("pltform", pltform);
        params.putString("deviceId", deviceId);
        params.putString("provinceid", provinceid);
        params.putString("schoolname", schoolname);
        params.putString("schoolid", schoolid);
        params.putString("cityid", cityid);
        params.putString("areaid", areaid);
        params.putString("stageid", stageid);

        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, 0);
        LogInfo.log("king", "第三方登录注册接口 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    //*****************************************************第三方登录接口end*****************************************************//

    private static final String CONTENT = "content";

    /**
     * 意见反馈
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> RequestUploadFeedBack(int updataId, String feedBackContent, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/common/feedback.do";
        Bundle params = new Bundle();
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(CONTENT, feedBackContent);
        params.putString(PUBLIC_PARAMETERS.OS_NAME, YanXiuConstant.OS);
        params.putString(PUBLIC_PARAMETERS.OS_VERSION, YanXiuConstant.OS_VERSION);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.BRAND, YanXiuConstant.BRAND);

        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, 0);
        LogInfo.log("lee", generatePath(httpParameter));
        return request(httpParameter);
    }

    /**
     * 题目报错
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestErrorFeedBack(String questionId, String content, YanxiuMainParser<T, D> parser) {
        String baseUrl = "http://mobile.hwk.yanxiu.com/internal/feedback.do";
        Bundle params = new Bundle();
        params.putString(PUBLIC_PARAMETERS.MOD_KEY, String.valueOf(LoginModel.getUid()));
        params.putString(CONTENT, content);
        params.putString(PUBLIC_PARAMETERS.QUESTION_ID, questionId);
        params.putString(PUBLIC_PARAMETERS.OS_NAME, YanXiuConstant.OS);
        params.putString(PUBLIC_PARAMETERS.OS_VERSION, YanXiuConstant.OS_VERSION);

        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, 0);
        LogInfo.log("lee", generatePath(httpParameter));
        return request(httpParameter);

    }


    /**
     * ‰
     * 考点诊断
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestExamInfo(String subjectId, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/anaofstd/listKnpStat.do";
        Bundle params = new Bundle();
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());

        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        params.putString(SUBJECT_ID, subjectId);
        params.putString(STAGE_ID, String.valueOf(LoginModel.getUserinfoEntity().getStageid()));

        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, 0);
        LogInfo.log("lee", generatePath(httpParameter));
        return request(httpParameter);
    }

    /**
     * 请求一个考点下的所有信息
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestSingleExamInfo(String subjectId, String chapterId, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/anaofstd/listLevel1KnpStat.do";
        Bundle params = new Bundle();
        params.putString(PUBLIC_PARAMETERS.TRACE_UID, LoginModel.getUid() + "");
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, YanXiuConstant.VERSION);
        params.putString(PUBLIC_PARAMETERS.OS, YanXiuConstant.OS_TYPE);
        params.putString(SUBJECT_ID, subjectId);
        params.putString(STAGE_ID, String.valueOf(LoginModel.getUserinfoEntity().getStageid()));
        params.putString(CHAPTER_ID, chapterId);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, 0);
        LogInfo.log("lee", generatePath(httpParameter));
        return request(httpParameter);
    }

    public static final String statisticUrl = "http://boss.shangruitong.com";
    public static final String UPLOAD_ALL_POINT_FILE = statisticUrl + "/logup";
    public static final String UPLOAD_DEV_FILE = statisticUrl + "/upfile";
    public static final String UPLOAD_CRASH_FILE = statisticUrl + "/uploadFile";

    /**
     * 上传及时打点数据
     *
     * @param updataId
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> instantUploadPointData(int updataId, HashMap map, YanxiuMainParser<T, D> parser) {
        String baseUrl = UPLOAD_ALL_POINT_FILE;
        Bundle params = new Bundle();
        params.putString("yxyl_statistic", Util.hashMapToJson(map));

        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.POST, parser, updataId);

        LogInfo.log("frc", "上传及时打点数据: = " + generatePath(httpParameter));
        return request(httpParameter);
    }

    /**
     * 上传文件接口
     *
     * @param actionUrl
     * @param files
     * @return 注意params 为空  此接口只有文件的是post其他为get
     */

    public static String reuqestUploadFile(String actionUrl, Map<String, String> params, Map<String, File> files, final UploadListener listener, final boolean isHead) {

        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";
        InputStream in = null;
        StringBuilder sb2 = null;

        try {
            URL uri = new URL(actionUrl);
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
            conn.setReadTimeout(5 * 1000); // 缓存的最长时
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
            conn.setUseCaches(false); // 不允许使用缓
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(60 * 1000);
//            conn.setChunkedStreamingMode(4096);//设置输出块数据大小

            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charsert", CHARSET);
            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + "; boundary=" + BOUNDARY);
            DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());

            if (files != null) {
                float initProgress = 0;
                for (Map.Entry<String, File> file : files.entrySet()) {
                    byte[] start_data = (PREFIX + BOUNDARY + LINEND).getBytes();
                    outStream.write(start_data);
                    StringBuilder sb1 = new StringBuilder();
                    if (isHead) {
                        sb1.append("Content-Disposition: form-data; " + "name=\"" + file.getKey() + "\"; " + "filename=\"" + file.getValue().getName() + "\"" + LINEND);
                    } else {
                        sb1.append("Content-Disposition: form-data; " + "name=\"" + "file" + "\"; " + "filename=\"" + file.getValue().getName() + "\"" + LINEND);
                    }
                    sb1.append("Content-Type: application/octet-stream" + LINEND);
                    sb1.append(LINEND);
                    outStream.write(sb1.toString().getBytes());

                    InputStream is = new FileInputStream(file.getValue());
                    byte[] buffer = new byte[4096];
                    int len;
                    float size = file.getValue().length();
                    LogInfo.log("geny", "UploadFileListener size = " + size);
                    float uploadSize = 0;
                    float progress = 0;
                    float tempProgress = progress;
                    while ((len = is.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                        uploadSize += len;
                        progress = (int) ((uploadSize * 100) / size);
                        float showProgress = initProgress + (1 / ((float) files.size())) * progress;
                        if (progress == 100) {
                            initProgress = showProgress;
                        }
                        LogInfo.log("geny", "UploadFileListener progress = " + showProgress);
                        if (tempProgress != showProgress) {
                            if (listener != null) {
                                listener.onProgress((int) showProgress);
                            }
                            tempProgress = showProgress;
                        }
                    }
                    is.close();
                    outStream.write(LINEND.getBytes());
                }
            }
            outStream.write(LINEND.getBytes());
            // 请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            outStream.write(end_data);
            outStream.flush();

            int res = conn.getResponseCode();
            LogInfo.log("geny", "ResponseCode = " + res);
            in = null;
            sb2 = new StringBuilder();
            if (res == 200) {
                in = conn.getInputStream();
                int ch;
                while ((ch = in.read()) != -1) {
                    sb2.append((char) ch);
                }
                //TODO 上传成功后 删除该文件
                for (Map.Entry<String, File> file : files.entrySet()) {
                    file.getValue().delete();
                }

            }
            if (in != null && outStream != null && conn != null) {
                outStream.close();
                in.close();
                conn.disconnect();
            }
        } catch (InterruptedIOException e) {
            LogInfo.log("geny", e.toString());
        } catch (Exception e) {
            LogInfo.log("geny", e.toString());
        }
        return in == null ? null : sb2.toString();
    }

    public interface UploadListener {
        void onProgress(int progress);
    }


}
