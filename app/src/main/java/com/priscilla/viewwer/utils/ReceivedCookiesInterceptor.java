package com.priscilla.viewwer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;
public class ReceivedCookiesInterceptor implements Interceptor {

    private Context context;
    public ReceivedCookiesInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        HashSet<String> cookies = (HashSet<String>) PreferenceManager.getDefaultSharedPreferences(context).getStringSet("PREF_COOKIES", new HashSet<String>());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {

            Log.d("value of my cookies",""+ cookies);
            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }




            SharedPreferences.Editor memes = PreferenceManager.getDefaultSharedPreferences(context).edit();
            memes.putStringSet("PREF_COOKIES", cookies).apply();
            memes.commit();
        }
        else{

            cookies.add("connect.sid=s%3AGP3D4hp5zzzcDdKc_Ss_anFYDe_FhY2t.bbS9zZBgf%2BP%2BfVBZXTZs%2FgPY0Kcp5znDbz5uqM%2BfAoA");
            SharedPreferences.Editor memes = PreferenceManager.getDefaultSharedPreferences(context).edit();
            memes.putStringSet("PREF_COOKIES", cookies).apply();
            memes.commit();
        }

        return originalResponse;
    }
}
