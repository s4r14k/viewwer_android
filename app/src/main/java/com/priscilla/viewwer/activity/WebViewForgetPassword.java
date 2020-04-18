package com.priscilla.viewwer.activity;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.priscilla.viewwer.R;

import java.net.URL;

public class WebViewForgetPassword extends BaseActivity {

    private String devUrl = "https://dev.viewwer.com/auth/request-password-reset";
    private String prodUrl = "https://viewwer.com/auth/request-password-reset";
    private String Url = "/auth/request-password-reset";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_forget_password);

        showLoadingView();

        WebView webView = (WebView) findViewById(R.id.webViewForgotPassword);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('panel')[0].style.width='5%'; })()");
                hideLoadingView();
            }
        });

        webView.loadUrl(BaseActivity.URL_API_SERVER + Url);
    }

}
