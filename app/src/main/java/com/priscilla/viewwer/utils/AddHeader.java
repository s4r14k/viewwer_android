package com.priscilla.viewwer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.priscilla.viewwer.activity.BaseActivity.PREFS_COOKIES;

public class AddHeader implements Interceptor {
    private Context context;

    public AddHeader(Context context) {
        this.context = context;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(App.getContext());

        String myCookies =  sharedPreferences.getString(PREFS_COOKIES, "");



        Request request = chain.request()
                .newBuilder()
                .addHeader("Cookie", myCookies)
                .addHeader("apikey", myCookies)
                .build();
        Response response = chain.proceed(request);

        Log.d("mandalo eto azafady ",response.toString() + myCookies+"manomboka eto");
        return response;
    }
}
