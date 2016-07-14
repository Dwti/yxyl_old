package com.yanxiu.gphone.hd.student.fragment;

import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.common.core.utils.CommonCoreUtil;
import com.common.core.utils.StringUtils;
import com.yanxiu.gphone.hd.student.R;
import com.yanxiu.gphone.hd.student.fragment.manager.IFgManager;
import com.yanxiu.gphone.hd.student.utils.Util;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/2/14.
 */
public class WebFragment extends TopBaseFragment {
    private WebView mWebView;
    private String baseUrl;
    private String titleName;
    private View mView;
    private ProgressBar progressBar;
    private static final String URL_KEY="url";
    private static final String TITLE_NAME_KEY="titleName";
    private static final String ON_PAUSE="onPause";
    private static final String ON_RESUME="onResume";
    private SetContainerFragment mFg;
    private final Handler handler=new Handler();
    private static WebFragment mWebFragment;
    public static Fragment newInstance( String url, String titleName){
        if(StringUtils.isEmpty(handleUrlAds(url))){
            Util.showToast(R.string.url_error);
            return null;
        }
        if(mWebFragment==null){
            mWebFragment=new WebFragment();
            Bundle bundle=new Bundle();
            bundle.putString(URL_KEY,url);
            bundle.putString(TITLE_NAME_KEY,titleName);
            mWebFragment.setArguments(bundle);
        }

        return mWebFragment;
    }
    public static Fragment newInstance( String url){
        if(StringUtils.isEmpty(handleUrlAds(url))){
            Util.showToast(R.string.url_error);
            return null;
        }
        if(mWebFragment==null){
            mWebFragment=new WebFragment();
            Bundle bundle=new Bundle();
            bundle.putString(URL_KEY,url);
            mWebFragment.setArguments(bundle);
        }
        return mWebFragment;
    }

    private static String handleUrlAds(String url){
        url = url.replaceAll(" ", "");
        if (!TextUtils.isEmpty(url)) {
            if (!(url.startsWith("http://") || url.startsWith("https://"))) {
                url = "http://" + url;
            }
            return url;
        } else {
            return "";
        }
    }



    @Override
    protected boolean isAttach() {
        return true;
    }

    @Override
    protected View getContentView() {
        mFg= (SetContainerFragment) getParentFragment();
        mView=getAttachView(R.layout.yanxiu_webview_layout);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        baseUrl=getArguments().getString(URL_KEY);
        titleName=getArguments().getString(TITLE_NAME_KEY);
        findView();
        return mView;
    }

    private void findView() {
        if(TextUtils.isEmpty(titleName)) {
            titleText.setText(R.string.privacy_policy_txt);
        } else {
            titleText.setText(titleName);
        }
        progressBar = (ProgressBar)mView.findViewById(R.id.loading_progress);

        mWebView = (WebView)mView.findViewById(R.id.webView);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setUserAgentString(CommonCoreUtil.createUA(getActivity()));
        mWebView.setVerticalScrollBarEnabled(true);
        mWebView.setHorizontalScrollBarEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new LetvWebViewClient());
        mWebView.setWebChromeClient(new LetvWebViewChromeClient());

    }

    @Override
    public void onReset() {
        destoryData();
    }

    private class LetvWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
            try {
                view.loadUrl(url);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }

        @Override
        public void onPageFinished (WebView view, String url) {
            super.onPageFinished(view, url);
            titleName = view.getTitle();
            titleText.setText(titleName);
        }
    }

    private class LetvWebViewChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (progressBar.getVisibility() != View.VISIBLE) {
                progressBar.setVisibility(View.VISIBLE);
            }
            progressBar.setProgress(newProgress);
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            }
        }
    }



    @Override
    protected void initLoadData() {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl(baseUrl);
            }
        },500);

    }

    @Override
    protected void setContentListener() {

    }

    @Override
    protected void destoryData() {
        finish();
    }

    private void finish() {
        releaseWebView();
        mFg.mIFgManager.popStack();
        mWebFragment=null;

    }

    private void releaseWebView() {
        try {
            if(getActivity()!=null&&getActivity().getWindow()!=null){
                getActivity().getWindow().clearFlags(
                        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }

            if (mWebView != null) {
                mWebView.getSettings().setBuiltInZoomControls(true);
                mWebView.stopLoading();
                mWebView.clearAnimation();
                mWebView.setVisibility(View.GONE);
                mWebView.removeAllViews();
                long timeout = ViewConfiguration.getZoomControlsTimeout();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mWebView!=null){
                            mWebView.destroy();
                        }

                    }
                },timeout);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        callHiddenWebViewMethod(ON_PAUSE);
    }


    @Override
    public void onResume() {
        super.onResume();
        callHiddenWebViewMethod(ON_RESUME);
    }

    private void callHiddenWebViewMethod(String name) {
        if (mWebView != null) {
            try {
                Method method = WebView.class.getMethod(name);
                method.invoke(mWebView);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView=null;
        mView=null;
        progressBar=null;
        mFg=null;
    }

    @Override
    protected IFgManager getFragmentManagerFromSubClass() {
        return null;
    }

    @Override
    protected int getFgContainerIDFromSubClass() {
        return 0;
    }
}
