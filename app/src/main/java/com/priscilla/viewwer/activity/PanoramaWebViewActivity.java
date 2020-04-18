package com.priscilla.viewwer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.priscilla.viewwer.R;

public class PanoramaWebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panorama_web_view);
        final WebView webView = (WebView) findViewById(R.id.webViewPanorama);
        webView.setBackgroundResource(R.drawable.image);
        webView.loadUrl("file:///android_asset/Panorama.html");
       // make sure you cast the web view before using loadUrl() function :
    }
}
