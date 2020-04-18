package com.priscilla.viewwer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import okhttp3.Cookie;
import okhttp3.Request;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.HttpAuthHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.priscilla.viewwer.Fragment.ProfilFragment;
import com.priscilla.viewwer.R;
import com.priscilla.viewwer.api.APIClient;
import com.priscilla.viewwer.api.APIInterface;
import com.priscilla.viewwer.utils.AddCookiesInterceptor;
import com.priscilla.viewwer.utils.ReceivedCookiesInterceptor;

import java.net.URL;
import java.util.HashSet;
import java.util.List;



public class WebViewVisitDetailActivity extends BaseActivity  {

    private final AppCompatActivity activity = WebViewVisitDetailActivity.this;
    Dialog _dialog;
    private Context _context;

    private Toolbar toolbar,toolbarActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_visit_detail);
        showLoadingView();

        View rootView = findViewById(android.R.id.content).getRootView();



        WebView  webView = (WebView) findViewById(R.id.webViewVisitDetail);
        webView.getSettings().setJavaScriptEnabled(true);
        Bundle b = getIntent().getExtras();

        String url = ""; // or other values
        if(b != null)
            url = b.getString("key");

        CookieManager cookieManager = CookieManager.getInstance();

        cookieManager.setCookie(url, getCurrentCookies());

        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                // hide element by class name
                webView.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('return')[0].style.display='none'; })()");
                hideLoadingView();
            }

        });

        if(b.getString("finishCreate") != null){
            toolbarActivityPanorama(rootView,R.id.ToolbarVisiteDisplay,R.string.title_profile,this);
        }else{
            new BaseActivity().toolbarActivity(rootView,R.id.ToolbarVisiteDisplay,R.string.title_search,this);
        }

        webView.loadUrl(url);


    }

    public void toolbarActivityPanorama(View view, int idTooblar, int Title, AppCompatActivity appCompatActivity) {
        toolbar = view.findViewById(idTooblar);
        appCompatActivity.setSupportActionBar(toolbar);

        toolbarActivity = (Toolbar) toolbar.findViewById(R.id.toolbarActivity);
        TextView title = (TextView) toolbar.findViewById(R.id.ToolbarTitle);
        title.setText(Title);
        LinearLayout backTo = (LinearLayout) toolbar.findViewById(R.id.linearLayoutBack);

        backTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Click","========= button clicked");

                Intent intent = new Intent(getApplicationContext(), BottomNavigationActivity.class);
                intent.putExtra("openProfil",true);
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);

            }
        });
    }
}
