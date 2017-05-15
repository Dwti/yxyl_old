package com.yanxiu.gphone.studentold.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.common.core.utils.CommonCoreUtil;
import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.base.YanxiuBaseActivity;
import com.yanxiu.gphone.studentold.utils.Util;

import java.lang.reflect.Method;

public class WebViewActivity extends YanxiuBaseActivity implements OnClickListener {

	private View topView;
	private WebView mWebView;
	private String baseUrl;
	private String titleName;
	private View backView;

	private ProgressBar progressBar;

	private TextView titleView;

	private Handler handler=new Handler();

	public static void launch(Context context, String url) {
		Intent intent = new Intent(context, WebViewActivity.class);
		url = url.replaceAll(" ", "");
		if (!TextUtils.isEmpty(url)) {
			if (!(url.startsWith("http://") || url.startsWith("https://"))) {
				url = "http://" + url;
			}
			intent.putExtra("url", url);
			context.startActivity(intent);
		} else {
			Util.showToast(R.string.url_error);
		}
	}
	public static void launch(Context context, String url, String titleName) {
		Intent intent = new Intent(context, WebViewActivity.class);
		url = url.replaceAll(" ", "");
		if (!TextUtils.isEmpty(url)) {
			if (!(url.startsWith("http://") || url.startsWith("https://"))) {
				url = "http://" + url;
			}
			intent.putExtra("url", url);
			intent.putExtra("titleName", titleName);
			context.startActivity(intent);
		} else {
			Util.showToast(R.string.url_error);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yanxiu_webview_layout);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		baseUrl = getIntent().getStringExtra("url");
		titleName = getIntent().getStringExtra("titleName");

		findView();
	}

	private void findView() {
		topView = findViewById(R.id.private_police_top_layout);
		backView = topView.findViewById(R.id.pub_top_left);
		titleView = (TextView) topView.findViewById(R.id.pub_top_mid);
		if(TextUtils.isEmpty(titleName)) {
			titleView.setText(R.string.privacy_policy_txt);
		} else {
			titleView.setText(titleName);
		}
		progressBar = (ProgressBar) findViewById(R.id.loading_progress);
		backView.setOnClickListener(this);
		mWebView = (WebView) findViewById(R.id.webView);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings().setLoadWithOverviewMode(true);
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
			String title = view.getTitle();
			if (title.equals("- no title specified")){
				if(TextUtils.isEmpty(titleName)) {
					titleView.setText(R.string.privacy_policy_txt);
				} else {
					titleView.setText(titleName);
				}
			}else {
				titleView.setText(title);
			}

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
	public void onClick(View v) {
		if(v == backView){
			finishActivity();
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
		super.onPause();
		callHiddenWebViewMethod("onPause");
	}

	@Override
	protected void onResume() {
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
}
