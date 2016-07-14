package com.yanxiu.gphone.parent.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.common.core.utils.CommonCoreUtil;
import com.yanxiu.gphone.parent.R;
import com.yanxiu.gphone.parent.activity.base.TopViewBaseActivity;
import com.yanxiu.gphone.parent.utils.ParentUtils;
import com.yanxiu.gphone.parent.utils.PublicLoadUtils;

import java.lang.reflect.Method;

/**
 * Created by lidongming on 16/3/31.
 */
public class ParentWebViewActivity  extends TopViewBaseActivity {

    private WebView mWebView;
    private String baseUrl;

    private ProgressBar progressBar;

    private Handler handler=new Handler();

    public static void launch(Context context, String url) {
        Intent intent = new Intent(context, ParentWebViewActivity.class);
        url = url.replaceAll(" ", "");
        if (!TextUtils.isEmpty(url)) {
            if (!(url.startsWith("http://") || url.startsWith("https://"))) {
                url = "http://" + url;
            }
            intent.putExtra("url", url);
            context.startActivity(intent);
        } else {
            ParentUtils.showToast(R.string.url_error);
        }
    }
    public static void launch(Context context, String url, String titleName) {
        Intent intent = new Intent(context, ParentWebViewActivity.class);
        url = url.replaceAll(" ", "");
        if (!TextUtils.isEmpty(url)) {
            if (!(url.startsWith("http://") || url.startsWith("https://"))) {
                url = "http://" + url;
            }
            intent.putExtra("url", url);
            intent.putExtra("titleName", titleName);
            context.startActivity(intent);
        } else {
            ParentUtils.showToast(R.string.url_error);
        }
    }

    @Override
    protected View getContentView() {
        mPublicLayout = PublicLoadUtils.createPage(this, R.layout.parent_webview_layout);
        mPublicLayout.finish();
//        mView= LayoutInflater.from(this).inflate(R.layout.parent_feedback_layout,null);
        initView();
        initData();
        return mPublicLayout;
    }

    private void initData() {
        titleText.setText(this.getResources().getString(R.string.parent_clause_and_policy_txt));

    }


    private void initView(){
        baseUrl = getIntent().getStringExtra("url");
//        titleView = (TextView) topView.findViewById(R.id.pub_top_mid);
//        if(TextUtils.isEmpty(titleName)) {
//            titleView.setText(R.string.privacy_policy_txt);
//        } else {
//            titleView.setText(titleName);
//        }
        progressBar = (ProgressBar) mPublicLayout.findViewById(R.id.loading_progress);
        mWebView = (WebView) mPublicLayout.findViewById(R.id.webView);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setUserAgentString(CommonCoreUtil.createUA(this));
        mWebView.setVerticalScrollBarEnabled(true);
        mWebView.setHorizontalScrollBarEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new LetvWebViewClient());
        mWebView.setWebChromeClient(new LetvWebViewChromeClient());
        mWebView.loadUrl(baseUrl);
    }


    private class LetvWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                view.loadUrl(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished (WebView view, String url) {
            super.onPageFinished(view, url);
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
    public void onBackPressed() {
        finishActivity();
        super.onBackPressed();
    }

    private void finishActivity(){
        releaseWebView();
        finish();
    }


    private  void releaseWebView(){
        try {
            getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
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
                        mWebView.destroy();
                    }
                },timeout);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        callHiddenWebViewMethod("onPause");
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        callHiddenWebViewMethod("onResume");
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
    protected boolean isAttach() {
        return false;
    }


    @Override
    protected void setContentListener() {

    }

    @Override
    protected void destoryData() {

    }

    @Override
    protected void initLaunchIntentData() {

    }
}
