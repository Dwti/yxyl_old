package com.yanxiu.gphone.parent.httpApi;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import com.common.core.utils.LogInfo;
import com.common.login.LoginModel;
import com.common.login.model.ParentInfoBean;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.bean.YanxiuDataHull;
import com.yanxiu.basecore.impl.YanxiuHttpBaseParameter;
import com.yanxiu.basecore.impl.YanxiuHttpParameter;
import com.yanxiu.basecore.impl.YanxiuHttpTool;
import com.yanxiu.basecore.parse.YanxiuMainParser;
import com.yanxiu.gphone.parent.bean.ParentUploadFileBean;
import com.yanxiu.gphone.parent.parser.ParentUploadFileParser;
import com.yanxiu.gphone.parent.utils.ParentConfiguration;
import com.yanxiu.gphone.parent.utils.ParentConfigConstant;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by lidongming on 16/3/24.
 */
public class YanxiuParentHttpApi {

    public interface REQUESR_PARAMS{
        String MOBILE_KEY="mobile";
        String PASSWORD_KEY="password";
        String TYPE_KEY="type";
        String MSGCODE_KEY="msgCode";
        String BINDUID_KEY="bindUid";
        String STDUID_KEY="stdUid";
        String STDACCOUNT_KEY="stdAccount";
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
    }


    /**
     * 正式接口
     */
    public interface OFFICIAL_URL {
        String DYNAMIC_BASE_RURL = "http://mobile.hwk.yanxiu.com";
    }

    /**
     * /**
     * 测试接口
     */
    public interface TEST_URL {
        String DYNAMIC_TEST_BASE_URL = "http://test.hwk.yanxiu.com";
//        String DYNAMIC_TEST_BASE_URL = "http://dev.hwk.yanxiu.com";
    }

    /**
     * 正式接口
     *
     * @return
     */
    public static String getPublicUrl () {
        if (ParentConfiguration.isDebug()) {
            return TEST_URL.DYNAMIC_TEST_BASE_URL;
        } else {
            return OFFICIAL_URL.DYNAMIC_BASE_RURL;
        }
    }

    private static String getStaticHead () {
        return getPublicUrl();
    }

    private static String getStaticEnd () {
        return "";
    }

    /**
     * 根据参数，调起请求
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> request (
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
    private static <T extends YanxiuBaseBean, D> String generatePath (YanxiuHttpParameter<T, D> httpParameter) {
        return httpParameter.getBaseUrl() + httpParameter.encodeUrl();
    }
    //===================================================================================================
    //===================================================================================================

    /**
     * “荣誉”是否显示红色标记接口
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestHonorRedFlag(YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/prt/honor/honorRedFlag.do";

        Bundle params = new Bundle();
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, ParentConfigConstant.getVERSION());
        params.putString(PUBLIC_PARAMETERS.OS, ParentConfigConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, 0);
        LogInfo.log("haitian", "“荣誉”是否显示红色标记接口 url = " + httpParameter.getBaseUrl() + httpParameter
                .encodeUrl());
        return request(httpParameter);
    }

    /**
     * 荣誉已读后修改荣誉状态接口
     * @param honorIds
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestHonorSetReadFlag
            (String honorIds, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/prt/honor/honorSetReadFlag.do";

        Bundle params = new Bundle();
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString("honorIds", honorIds);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, ParentConfigConstant.getVERSION());
        params.putString(PUBLIC_PARAMETERS.OS, ParentConfigConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, 0);
        LogInfo.log("haitian", "荣誉已读后修改荣誉状态接口 url = " + httpParameter.getBaseUrl() + httpParameter
                .encodeUrl());
        return request(httpParameter);
    }
    /**
     * 周报列表页接口
     *
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestCurrentWeekReportData
    (YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/prt/week/list.do";

        Bundle params = new Bundle();
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, ParentConfigConstant.getVERSION());
        params.putString(PUBLIC_PARAMETERS.OS, ParentConfigConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, 0);
        LogInfo.log("haitian", "周报列表页接口 url = " + httpParameter.getBaseUrl() + httpParameter
                .encodeUrl());
        return request(httpParameter);
    }

    /**
     * 切换“上一周”或“下一周”获取列表数据接口
     *
     * @param type   0代表上一周 1代表下一周
     * @param week
     * @param year
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestWeekReportData
    (int type, int week, String year, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/prt/week/otherWeekList.do";

        Bundle params = new Bundle();
        params.putString("type", type + "");
        params.putString("week", week + "");
        params.putString("year", year);
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, ParentConfigConstant.getVERSION());
        params.putString(PUBLIC_PARAMETERS.OS, ParentConfigConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, 0);
        LogInfo.log("haitian", "切换“上一周”或“下一周”获取列表数据接口 url = " + httpParameter.getBaseUrl() +
                httpParameter
                        .encodeUrl());
        return request(httpParameter);
    }

    /**
     * 作业数据获取各学科统计详情数据接口
     *
     * @param classId 班级ID
     * @param week
     * @param year
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestHwListDetailsData
    (String classId, int week, String year, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/prt/week/listHwkDetail.do";

        Bundle params = new Bundle();
        params.putString("classId", classId);
        params.putString("week", week + "");
        params.putString("year", year);
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
//        params.putString(PUBLIC_PARAMETERS.TOKEN, "ead7fde66b87645cd3fcc9a4a1b6885c");
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, ParentConfigConstant.getVERSION());
        params.putString(PUBLIC_PARAMETERS.OS, ParentConfigConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, 0);
        LogInfo.log("haitian", "作业数据获取各学科统计详情数据接口 url = " + httpParameter.getBaseUrl() +
                httpParameter
                        .encodeUrl());
        return request(httpParameter);
    }


    /**
     * 获取首页数据接口
     * @param currentPage
     * @param pageSize
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestGetHomeInfo(int currentPage, int pageSize, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/prt/index/getIndex.do";

        Bundle params = new Bundle();
        params.putString("currentPage", currentPage + "");
        params.putString("pageSize", pageSize + "");
        params.putString("parentId", String.valueOf(LoginModel.getUid()));
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, ParentConfigConstant.getVERSION());
        params.putString(PUBLIC_PARAMETERS.OS, ParentConfigConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, 0);
        LogInfo.log("geny", "获取首页数据接口接口 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    /**
     * 获取作业详细统计信息接口
     * @param pid
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestGetHwkDetail(int pid, YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/prt/index/getHwkDetail.do";

        Bundle params = new Bundle();
        params.putString("pid", String.valueOf(pid));
        params.putString("parentId", String.valueOf(LoginModel.getUid()));
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, ParentConfigConstant.getVERSION());
        params.putString(PUBLIC_PARAMETERS.OS, ParentConfigConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, 0);
        LogInfo.log("geny", "获取作业详细统计信息接口 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }


    /**
     * 请求登录
     * @param  mobile
     * @param  password
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestLogin(String mobile,String password,YanxiuMainParser<T, D> parser){
        String baseUrl=getStaticHead()+"/prt/u/login.do?";
        Bundle params=new Bundle();
        params.putString(REQUESR_PARAMS.MOBILE_KEY,mobile);
        params.putString(REQUESR_PARAMS.PASSWORD_KEY,password);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, ParentConfigConstant.getVERSION());
        params.putString(PUBLIC_PARAMETERS.OS, ParentConfigConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.POST, parser, 0);
        LogInfo.log("lee",generatePath(httpParameter));
        return request(httpParameter);
    }

    /**
     * 获取手机验证码
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestVerifyCode(String mobile,int type,YanxiuMainParser<T, D> parser){
        String baseUrl=getStaticHead()+"/prt/u/sendMsgCode.do";
        Bundle params=new Bundle();
        params.putString(REQUESR_PARAMS.MOBILE_KEY,mobile);
        params.putString(REQUESR_PARAMS.TYPE_KEY, String.valueOf(type));
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, ParentConfigConstant.getVERSION());
        params.putString(PUBLIC_PARAMETERS.OS, ParentConfigConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, 0);
        LogInfo.log("lee", generatePath(httpParameter));
        return request(httpParameter);
    }
    /**
     *   验证手机验证码
     *
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestCheckVerifyCode(String mobile,String msgCode,int type,YanxiuMainParser<T, D> parser){
        String baseUrl=getStaticHead()+"/prt/u/checkMsgCode.do";
        Bundle params=new Bundle();
        params.putString(REQUESR_PARAMS.MOBILE_KEY,mobile);
        params.putString(REQUESR_PARAMS.TYPE_KEY, String.valueOf(type));
        params.putString(REQUESR_PARAMS.MSGCODE_KEY,msgCode);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, ParentConfigConstant.getVERSION());
        params.putString(PUBLIC_PARAMETERS.OS, ParentConfigConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, 0);
        LogInfo.log("lee", generatePath(httpParameter));
        return request(httpParameter);
    }

    /**
     * 注册
     *
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestRegister(String mobile,String password,String msgcode,YanxiuMainParser<T, D> parser){
        String baseUrl=getStaticHead()+"/prt/u/register.do?";
        Bundle params=new Bundle();
        params.putString(REQUESR_PARAMS.MOBILE_KEY,mobile);
        params.putString(REQUESR_PARAMS.PASSWORD_KEY,password);
        params.putString(REQUESR_PARAMS.MSGCODE_KEY,msgcode);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, ParentConfigConstant.getVERSION());
        params.putString(PUBLIC_PARAMETERS.OS, ParentConfigConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<>(baseUrl, params,
                YanxiuHttpParameter.Type.POST, parser, 0);
        LogInfo.log("lee", generatePath(httpParameter));
        return request(httpParameter);
    }

    /**
     * 获得学生信息
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestGetChildInfo(String stdUid,YanxiuMainParser<T, D> parser){
        String baseUrl=getStaticHead()+"/prt/u/getChildInfo.do";
        Bundle params=new Bundle();
        params.putString(REQUESR_PARAMS.STDUID_KEY,stdUid);
        params.putString(PUBLIC_PARAMETERS.TOKEN,LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, ParentConfigConstant.getVERSION());
        params.putString(PUBLIC_PARAMETERS.OS, ParentConfigConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, 0);
        LogInfo.log("lee", generatePath(httpParameter));
        return request(httpParameter);
    }

    /**
     * 绑定学生账号
     *
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestBindChildAccount(String stduid,YanxiuMainParser<T, D> parser){
        String baseUrl=getStaticHead()+"/prt/u/bindChildAccount.do";
        Bundle params=new Bundle();
        params.putString(REQUESR_PARAMS.STDACCOUNT_KEY,stduid);
        params.putString(PUBLIC_PARAMETERS.TOKEN,LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, ParentConfigConstant.getVERSION());
        params.putString(PUBLIC_PARAMETERS.OS, ParentConfigConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, 0);
        LogInfo.log("lee", generatePath(httpParameter));
        return request(httpParameter);
    }

    /**
     * 解绑定学生账号
     *
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestUnBindChildAccount(YanxiuMainParser<T, D> parser){
        String baseUrl=getStaticHead()+"/prt/personal/unBind.do";
        Bundle params=new Bundle();
        params.putString(PUBLIC_PARAMETERS.TOKEN,((ParentInfoBean)LoginModel.getRoleLoginBean()).getProperty().getPassport().getToken());
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, ParentConfigConstant.getVERSION());
        params.putString(PUBLIC_PARAMETERS.OS, ParentConfigConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, 0);
        LogInfo.log("geny", generatePath(httpParameter));
        return request(httpParameter);
    }


    /**
     * 找回密码
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T>requestSetPassword(String mobile,String password,YanxiuMainParser<T, D> parser){
        String baseUrl=getStaticHead()+"/prt/u/setPassword.do";
        Bundle params=new Bundle();
        params.putString(REQUESR_PARAMS.MOBILE_KEY,mobile);
        params.putString(REQUESR_PARAMS.PASSWORD_KEY,password);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, ParentConfigConstant.getVERSION());
        params.putString(PUBLIC_PARAMETERS.OS, ParentConfigConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, 0);
        LogInfo.log("lee", generatePath(httpParameter));
        return request(httpParameter);
    }

    /**
     * 获取荣誉列表接口
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestHonorList(YanxiuMainParser<T, D> parser){
        String baseUrl=getStaticHead()+"/prt/honor/listHonors.do";
        Bundle params=new Bundle();
        params.putString(PUBLIC_PARAMETERS.TOKEN,LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, ParentConfigConstant.getVERSION());
        params.putString(PUBLIC_PARAMETERS.OS, ParentConfigConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, 0);
        LogInfo.log("lee", generatePath(httpParameter));
        return request(httpParameter);
    }



    /**
     * 获取孩子荣誉接口
     * @param parser
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestetChildHonors(YanxiuMainParser<T, D> parser) {
        String baseUrl = getStaticHead() + "/prt/personal/getChildHonors.do";

        Bundle params = new Bundle();
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, ParentConfigConstant.getVERSION());
        params.putString(PUBLIC_PARAMETERS.OS, ParentConfigConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, 0);
        LogInfo.log("geny", "获取孩子荣誉接口 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }


    /**
     * 修改用户密码
     * @param updataId
     * @param parser
     * @param paramsMap
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestModifiedPwd(
            int updataId, YanxiuMainParser<T, D> parser, HashMap<String, String> paramsMap) {
        String baseUrl = getStaticHead() + "/prt/personal/resetPassWord.do";

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
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.PCODE_KEY, ParentConfigConstant.PCODE);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, ParentConfigConstant.getVERSION());
        params.putString(PUBLIC_PARAMETERS.OS, ParentConfigConstant.OS_TYPE);
        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, updataId);
        LogInfo.log("geny", "修改用户密码 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }


    /**
     * 意见反馈
     * role：（可传可不传，默认为0）0为学生端，1为家长端
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestUploadFeedBack(String feedBackContent,YanxiuMainParser<T, D> parser){
        String baseUrl=getStaticHead()+"/app/common/feedback.do";
        Bundle params=new Bundle();
        params.putString("content",feedBackContent);
        params.putString("role",String.valueOf(1));
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.OS_NAME, ParentConfigConstant.OS);
        params.putString(PUBLIC_PARAMETERS.OS_VERSION, ParentConfigConstant.OS_VERSION);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, ParentConfigConstant.getVERSION());
        params.putString(PUBLIC_PARAMETERS.BRAND, ParentConfigConstant.BRAND);

        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, 0);
        LogInfo.log("geny", "意见反馈 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }



    /**
     * 修改用户信息通用接口
     * @param parser
     * @param paramsMap
     * @param <T>
     * @param <D>
     * @return
     */
    public static <T extends YanxiuBaseBean, D> YanxiuDataHull<T> requestUpdateUserInfo(YanxiuMainParser<T, D> parser, HashMap<String, String> paramsMap) {
        String baseUrl = getStaticHead() + "/prt/personal/updateInfo.do";

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

        params.putString("parentId", String.valueOf(LoginModel.getUid()));
        params.putString(PUBLIC_PARAMETERS.TOKEN, LoginModel.getToken());
        params.putString(PUBLIC_PARAMETERS.OS_NAME, ParentConfigConstant.OS);
        params.putString(PUBLIC_PARAMETERS.OS_VERSION, ParentConfigConstant.OS_VERSION);
        params.putString(PUBLIC_PARAMETERS.VERSION_KEY, ParentConfigConstant.getVERSION());
        params.putString(PUBLIC_PARAMETERS.BRAND, ParentConfigConstant.BRAND);

        YanxiuHttpParameter<T, D> httpParameter = new YanxiuHttpParameter<T, D>(baseUrl, params,
                YanxiuHttpParameter.Type.GET, parser, 0);
        LogInfo.log("geny", "修改用户信息通用接口 url = " + httpParameter.getBaseUrl() + httpParameter.encodeUrl());
        return request(httpParameter);
    }

    public interface UploadFileListener<T extends YanxiuBaseBean>{
        void onFail(T bean);
        void onSuccess(T bean);
        void onProgress(int progress);
    }

    /**
     * 头像上传
     * @param files
     * @param listener
     * @return
     */
    public static AsyncTask requestUploadFile(final Map<String, File> files, final UploadFileListener listener){
        AsyncTask task = new AsyncTask<Object,Void,String>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected String doInBackground(Object... params) {
                // /app/common/uploadHeadImg.do?token=3kdkkslkdn0ldjk
                final String actionUrl = getStaticHead() + "/prt/personal/uploadHeadImg.do?";
                StringBuilder sb = new StringBuilder(actionUrl);
                sb.append("token=").append(LoginModel.getToken());
                LogInfo.log("geny", sb.toString());
                String result = reuqestUploadFile(sb.toString(), files, listener);
                return result;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(TextUtils.isEmpty(s)){
                    listener.onFail(null);
                } else {
                    LogInfo.log("geny", "requestUploadFile s ="+s);
                    ParentUploadFileBean bean = null;
                    try {
                        JSONObject obj = new JSONObject(s);
                        ParentUploadFileParser parser = new ParentUploadFileParser();
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
     * 上传文件接口
     * @param actionUrl
     * @param files
     * @return
     *
     * 注意params 为空  次接口只有文件的是post其他为get
     */
    public static String reuqestUploadFile(String actionUrl, Map<String, File> files, final UploadFileListener
            listener){
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
                        progress = (int)((uploadSize*100)/size);
                        if(tempProgress != progress){
                            if(listener != null ){
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
        } catch (InterruptedIOException e){
            if(e != null){
                LogInfo.log("geny", "InterruptedIOException msg =" + e.getMessage());
            }
        } catch (Exception e) {
            if(e != null){
                LogInfo.log("geny", "Exception msg =" + e.getMessage());
            }
        }
//        LogInfo.log("geny", "请求结束标志 -- sb" + sb2.toString());
        return in == null ? null : sb2.toString();
    }





}
