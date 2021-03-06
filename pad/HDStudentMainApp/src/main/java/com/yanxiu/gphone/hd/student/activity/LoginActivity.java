package com.yanxiu.gphone.hd.student.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.LogInfo;
import com.common.core.utils.NetWorkTypeUtils;
import com.common.core.utils.StringUtils;
import com.common.login.model.UserInfoBean;
import com.common.share.constants.ShareConstants;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yanxiu.basecore.bean.YanxiuBaseBean;
import com.yanxiu.basecore.exception.ErrorCode;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.activity.base.YanxiuBaseActivity;
import com.yanxiu.gphone.hd.student.bean.QQLoginBean;
import com.yanxiu.gphone.hd.student.bean.WXLoginBean;
import com.yanxiu.gphone.hd.student.bean.WXUserInfoBean;
import com.yanxiu.gphone.hd.student.inter.AsyncCallBack;
import com.yanxiu.gphone.hd.student.parser.QQLoginBeanParser;
import com.yanxiu.gphone.hd.student.preference.PreferencesManager;
import com.yanxiu.gphone.hd.student.requestTask.RequestLoginTask;
import com.yanxiu.gphone.hd.student.requestTask.RequestThirdLoginTask;
import com.yanxiu.gphone.hd.student.requestTask.RequestTokenBackTask;
import com.yanxiu.gphone.hd.student.requestTask.RequestWXUserInfoTask;
import com.yanxiu.gphone.hd.student.utils.Util;
import com.yanxiu.gphone.hd.student.view.LoginRelativeLayoutView;
import com.yanxiu.gphone.hd.student.view.StudentLoadingLayout;
import com.yanxiu.gphone.hd.student.view.YanxiuTypefaceTextView;
import com.yanxiu.gphone.hd.student.wxapi.WXEntryActivity;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/6.
 */
public class LoginActivity extends YanxiuBaseActivity implements
        WXEntryActivity.WXLoginListener {
    public final static int TOKEN_INVADALITE = 1;

    public static void launcherActivity (Context context, int type) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("type", type);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private ImageView logoImage;

    private EditText userNameText;

    private EditText passwordText;

    private TextView forgetPwdText;

    private TextView loginText;

    private TextView registerText;

    private LinearLayout wechatLogin;

    private LinearLayout qqLogin;

    private ImageView delView;

    private ImageView openOrPwd;

    private StudentLoadingLayout loadingView;

    private LoginRelativeLayoutView windowSoftView;

    private final static int SHOW_LOGO = 1;
    private final static int HIDE_LOGO = 2;
    private final static int LOGIN = 3;
    private final static int HIDE_INPUT_SOFT = 4;

    private final String PLAT_QQ = "qq";
    private final String PLAT_WEIXIN = "weixin";
    private RequestLoginTask requestLoginTask;

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    private Tencent mTencent;
    private QQListener listener;
    private UserTextWatcher mUserTextWatcher;
    private Window window;
    private boolean isLock = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_LOGO:
                    logoImage.setVisibility(View.VISIBLE);
                    break;
                case HIDE_LOGO:
                    logoImage.setVisibility(View.GONE);
                    break;
                case LOGIN:
                    MainActivity.launchActivity(LoginActivity.this);
                    break;
                /*case HIDE_INPUT_SOFT:
                    CommonCoreUtil.hideSoftInput(userNameText);
                    CommonCoreUtil.hideSoftInput(passwordText);
                    break;*/
            }
        }
    };

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        findView();
    }

    private void findView () {

        logoImage = (ImageView) findViewById(R.id.login_logo);
        userNameText = (EditText) findViewById(R.id.login_username);
        Util.setViewTypeface(YanxiuTypefaceTextView.TypefaceType.METRO_BOLD, userNameText);
        passwordText = (EditText) findViewById(R.id.login_password);
        forgetPwdText = (TextView) findViewById(R.id.forget_password);
        loginText = (TextView) findViewById(R.id.login);
        registerText = (TextView) findViewById(R.id.register);
        wechatLogin = (LinearLayout) findViewById(R.id.login_third_wechat);
        qqLogin = (LinearLayout) findViewById(R.id.login_third_qq);
        delView = (ImageView) findViewById(R.id.del_username);
        openOrPwd = (ImageView) findViewById(R.id.open_or_close_pwd);
        loadingView = (StudentLoadingLayout) findViewById(R.id.loading);
        Util.setViewTypeface(YanxiuTypefaceTextView.TypefaceType.FANGZHENG, loginText,
                forgetPwdText, registerText);
        windowSoftView = (LoginRelativeLayoutView) findViewById(R.id.windowsoft);
        /*windowSoftView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.sendEmptyMessage(HIDE_INPUT_SOFT);
            }
        });*/
        windowSoftView.setWindowSoftListener(new LoginRelativeLayoutView.WindowSoftListener() {
            @Override
            public void windowSoftShow () {
                handler.sendEmptyMessage(HIDE_LOGO);
            }

            @Override
            public void windowSoftHide () {
                handler.sendEmptyMessage(SHOW_LOGO);
            }
        });
        mUserTextWatcher = new UserTextWatcher();
        userNameText.addTextChangedListener(mUserTextWatcher);
        forgetPwdText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                CommonCoreUtil.hideSoftInput(userNameText);
                CommonCoreUtil.hideSoftInput(passwordText);
                cancelLoginTask();
                RegisterActivity.launchActivity(LoginActivity.this, 1, userNameText.getText().toString());
            }
        });
        delView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                userNameText.setText("");
            }
        });
        openOrPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                isLock = !isLock;
                onCheckedChanged(view, isLock);
            }
        });
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (StringUtils.isEmpty(userNameText.getText().toString())) {
                    Util.showUserToast(R.string.login_name_null, -1, -1);
                    return;
                }
                if (!CommonCoreUtil.isMobileNo(userNameText.getText().toString().replaceAll(" ", ""))) {
                    Util.showUserToast(R.string.login_name_ival, -1, -1);
                    return;
                }
                if (StringUtils.isEmpty(passwordText.getText().toString())) {
                    Util.showUserToast(R.string.login_pwd_null, -1, -1);
                    return;
                }
                CommonCoreUtil.hideSoftInput(userNameText);
                CommonCoreUtil.hideSoftInput(passwordText);
                cancelLoginTask();
                PreferencesManager.getInstance().setIsThirdLogIn(false);
                login();
            }
        });
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                CommonCoreUtil.hideSoftInput(userNameText);
                CommonCoreUtil.hideSoftInput(passwordText);
                cancelLoginTask();
                RegisterActivity.launchActivity(LoginActivity.this, 0);
            }
        });
        wechatLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (NetWorkTypeUtils.isNetAvailable()) {
                    Util.showToast(R.string.net_null);
                } else {
                    PreferencesManager.getInstance().setIsThirdLogIn(true);
                    cancelLoginTask();
                    openWXinClient();
                }
            }
        });
        qqLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (NetWorkTypeUtils.isNetAvailable()) {
                    Util.showToast(R.string.net_null);
                } else {
                    PreferencesManager.getInstance().setIsThirdLogIn(true);
                    cancelLoginTask();
                    openQQClient();
                }
            }
        });
    }

    public void onCheckedChanged (View lockView, boolean isChecked) {
        // TODO Auto-generated method stub
        if (isChecked) {
            //如果选中，显示密码
            lockView.setBackgroundResource(R.drawable.pwd_unlock);
            passwordText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            //否则隐藏密码
            lockView.setBackgroundResource(R.drawable.pwd_lock);
            passwordText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        if (!TextUtils.isEmpty(passwordText.getText().toString())) {
            passwordText.setSelection(passwordText.getText().toString().length());
        }
    }

    private void login () {
        loadingView.setViewType(StudentLoadingLayout.LoadingType.LAODING_COMMON);
        requestLoginTask = new RequestLoginTask(this,
                userNameText.getText().toString().replaceAll(" ", ""),
                passwordText.getText().toString(),
                new AsyncCallBack() {
                    @Override
                    public void update (YanxiuBaseBean result) {
                        loadingView.setViewGone();
                        UserInfoBean userInfoBean = (UserInfoBean) result;
                        if (userInfoBean.getStatus().getCode() == 0) {

                            handler.sendEmptyMessage(LOGIN);
                        } else {
                            Util.showUserToast(userInfoBean.getStatus().getDesc(), null, null);
                        }
                    }

                    @Override
                    public void dataError (int type, String msg) {
                        loadingView.setViewGone();
                        if (!StringUtils.isEmpty(msg)) {
                            Util.showUserToast(msg, null, null);
                        } else {
                            Util.showUserToast(R.string.net_null, -1, -1);
                        }
                    }
                });
        requestLoginTask.start();
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        cancelLoginTask();
        CommonCoreUtil.hideSoftInput(userNameText);
        CommonCoreUtil.hideSoftInput(passwordText);
    }

    private void cancelLoginTask () {
        if (requestLoginTask != null) {
            requestLoginTask.cancel();
            requestLoginTask = null;
            loadingView.setViewGone();
        }
    }

    private void openWXinClient () {
        loadingView.setViewType(StudentLoadingLayout.LoadingType.LAODING_COMMON);
        // send oauth request
        api = WXAPIFactory.createWXAPI(this, ShareConstants.HD_STUDENT_WX_AppID);
        api.registerApp(ShareConstants.HD_STUDENT_WX_AppID);
        boolean isInstall = CommonCoreUtil.checkBrowser(this, "com.tencent.mm");
        LogInfo.log("king", "isInstall = " + isInstall);
        if (isInstall) {
            WXEntryActivity.setListener(this);
            final com.tencent.mm.sdk.modelmsg.SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "none_weixin_login";
            api.sendReq(req);
            LogInfo.log("king", "sendReq");
        } else {
            Util.showToast(R.string.no_install_weixin);
            loadingView.setViewGone();
        }
    }

    private void openQQClient () {
        loadingView.setViewType(StudentLoadingLayout.LoadingType.LAODING_COMMON);
        // Tencent类是SDK的主要实现类，通过此访问腾讯开放的OpenAPI。
        mTencent = Tencent.createInstance(ShareConstants.HD_STUDENT_QQ_AppID, this);
        if (mTencent == null) {
            Util.showToast(R.string.no_install_qq);
            loadingView.setViewGone();
        } else {
            if (!mTencent.isSessionValid()) {
                LogInfo.log("king", "QQ login start");
                listener = new QQListener();
                mTencent.login(this, "all", listener);
            }
        }
    }

    private class QQListener implements IUiListener {

        @Override
        public void onComplete (Object object) {
            if (object != null) {
                try {
                    JSONObject jsonObject = (JSONObject) object;
                    QQLoginBean qqLoginBean = new QQLoginBeanParser().parse(jsonObject);
                    if (qqLoginBean != null) {
                        if (!StringUtils.isEmpty(qqLoginBean.getOpenid())) {
                            requestLoginTask(null, qqLoginBean, PLAT_QQ);
                        } else {
                            Util.showToast(R.string.net_null);
                        }
                    }
                } catch (Exception e) {
                    Util.showToast(R.string.login_fail);
                }
            }
        }

        @Override
        public void onError (UiError uiError) {
            loadingView.setViewGone();
            LogInfo.log("king", "QQ login onError");
            if (uiError != null) {
                Util.showToast(uiError.errorMessage);
                LogInfo.log("king", "code = " + uiError.errorCode + " ,detail = " + uiError.errorMessage);
            }
        }

        @Override
        public void onCancel () {
            loadingView.setViewGone();
            LogInfo.log("king", "QQ login onCancel");
        }
    }


    /**
     * 根据code换取token
     *
     * @param code
     * @param appid
     * @param secret
     */
    private void getAccessTokenByCode (String code, String appid, String secret) {
        new RequestTokenBackTask(this, code, appid, secret,
                "authorization_code", new AsyncCallBack() {
            @Override
            public void update (YanxiuBaseBean result) {
                WXLoginBean wxLoginBean = (WXLoginBean) result;
                LogInfo.log("king", "getAccessTokenByCode wxLoginBean");
                if (wxLoginBean != null) {
                    if (!StringUtils.isEmpty(wxLoginBean.openid)) {
                        requestLoginTask(wxLoginBean, null, PLAT_WEIXIN);
                    } else {
                        loadingView.setViewGone();
                        Util.showToast(R.string.net_null);
                    }
                }
            }

            @Override
            public void dataError (int type, String msg) {
                LogInfo.log("king", "getAccessTokenByCode dataError msg = " + msg);
                loadingView.setViewGone();
                if (type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                    Util.showToast(R.string.net_null);
                } else {
                    Util.showToast(R.string.login_fail);
                }
            }
        }).start();
    }

    @Override
    public void onGetWXLoginCode (String code) {
        getAccessTokenByCode(code, ShareConstants.HD_STUDENT_WX_AppID,
                ShareConstants.HD_STUDENT_WX_AppSecret);
    }

    @Override
    public void onUserCancelOrFail () {
        loadingView.setViewGone();
    }

    private void requestLoginTask (final WXLoginBean wxLoginBean, final QQLoginBean qqLoginBean, final String platfom) {
        loadingView.setViewType(StudentLoadingLayout.LoadingType.LAODING_COMMON);
        String openId = "";
        String uniqid = "";
        if (platfom.equals(PLAT_QQ)) {
            openId = qqLoginBean.getOpenid();
        } else {
            openId = wxLoginBean.openid;
        }
        if (platfom.equals(PLAT_QQ)) {
            uniqid = qqLoginBean.getUniqid();
        } else {
            uniqid = wxLoginBean.uniqid;
        }
        new RequestThirdLoginTask(LoginActivity.this, openId, platfom, new AsyncCallBack() {
            @Override
            public void update (YanxiuBaseBean result) {
                loadingView.setViewGone();
                UserInfoBean userInfoBean = (UserInfoBean) result;
                if (userInfoBean != null && userInfoBean.getStatus() != null) {
                    if (userInfoBean.getStatus().getCode() == 0) {
                        userInfoBean.setIsThridLogin(true);

                        handler.sendEmptyMessage(LOGIN);
                    } else if (userInfoBean.getStatus().getCode() == 80) {
                        if (platfom.equals(PLAT_QQ)) {
                            UserInfoActivity.launchActivity(LoginActivity.this, qqLoginBean.getOpenid(), PLAT_QQ, null);
                        } else {
                            requestWXUserInfoTask(wxLoginBean);
                        }
                    } else {
                        if (!StringUtils.isEmpty(userInfoBean.getStatus().getStatus())) {
                            Util.showToast(userInfoBean.getStatus().getStatus());
                        } else {
                            Util.showToast(R.string.login_fail);
                        }
                    }
                } else {
                    Util.showToast(R.string.login_fail);
                }
            }

            @Override
            public void dataError (int type, String msg) {
                loadingView.setViewGone();
                if (type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                    Util.showToast(R.string.net_null);
                } else {
                    Util.showToast(R.string.login_fail);
                }
            }
        }).start();
    }

    private void requestWXUserInfoTask (final WXLoginBean wxLoginBean) {
        if (wxLoginBean != null) {
            new RequestWXUserInfoTask(LoginActivity.this, wxLoginBean.access_token,
                    wxLoginBean.openid, new AsyncCallBack() {
                @Override
                public void update (YanxiuBaseBean result) {
                    WXUserInfoBean wxUserInfoBean = (WXUserInfoBean) result;
                    if (wxUserInfoBean != null) {
                        LogInfo.log("king", "RequestWXUserInfoTask UserInfoActivity");
                        UserInfoActivity.launchActivity(LoginActivity.this, wxLoginBean.openid, PLAT_WEIXIN, wxUserInfoBean);
                    } else {
                        LogInfo.log("king", "RequestWXUserInfoTask result == null");
                        loadingView.setViewGone();
                        Util.showToast(R.string.login_fail);
                    }
                }

                @Override
                public void dataError (int type, String msg) {
                    LogInfo.log("king", "RequestWXUserInfoTask dataError msg = " + msg);
                    loadingView.setViewGone();
                    if (type == ErrorCode.NETWORK_NOT_AVAILABLE) {
                        Util.showToast(R.string.net_null);
                    } else {
                        Util.showToast(R.string.login_fail);
                    }
                }
            }).start();
        } else {
            loadingView.setViewGone();
            Util.showToast(R.string.login_fail);
        }
    }



    @Override
    protected void onActivityResult (int requestCode, int resultCode,
                                     Intent data) {
        LogInfo.log("king", "LoginActivity onActivityResult");
        if (mTencent != null) {
            Tencent.onActivityResultData(requestCode, resultCode, data,
                    listener);
        }
    }

    private class UserTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged (CharSequence s, int start,
                                       int count, int after) {
        }

        @Override
        public void onTextChanged (CharSequence s, int start,
                                   int before, int count) {
            if (s == null || s.length() == 0) return;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                if (i != 3 && i != 8 && i != 13 && s.charAt(i) == ' ') {
                    continue;
                } else {
                    sb.append(s.charAt(i));
                    if ((sb.length() == 4 || sb.length() == 9 || sb.length() == 14) && sb.charAt(sb
                            .length() - 1)
                            != ' ') {
                        sb.insert(sb.length() - 1, ' ');
                    }
                }
            }
            if (!sb.toString().equals(s.toString())) {
                int index = start + 1;
                if (sb.charAt(start) == ' ') {
                    if (before == 0) {
                        index++;
                    } else {
                        index--;
                    }
                } else {
                    if (before == 1) {
                        index--;
                    }
                }
                String txtContent = sb.toString();
//                userNameText.setText(txtContent);
                userNameText.setText(Html.fromHtml("<big><strong>" + txtContent +
                        "</strong></big>"));
                userNameText.setSelection(index);
            } else if (sb.toString().replaceAll(" ", "").length() >= 11) {
                userNameText.removeTextChangedListener(mUserTextWatcher);
                String txtContent = sb.toString();
//                userNameText.setText(txtContent);
                userNameText.setText(Html.fromHtml("<big><strong>" + txtContent +
                        "</strong></big>"));
                userNameText.setSelection(txtContent.length());
                LogInfo.log("haitian", "sb.toString()=" + sb.toString());
                userNameText.addTextChangedListener(mUserTextWatcher);
            }

        }

        @Override
        public void afterTextChanged (final Editable s) {
//            int length = s.length();
//            if(length>0){
//                delView.setVisibility(View.VISIBLE);
//            }else{
//                delView.setVisibility(View.GONE);
//            }
        }
    }

}
