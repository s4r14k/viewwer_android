package com.priscilla.viewwer.api;

import android.content.Context;

import com.priscilla.viewwer.activity.BaseActivity;
import com.priscilla.viewwer.utils.AddCookiesInterceptor;
import com.priscilla.viewwer.utils.ReceivedCookiesInterceptor;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.OkHttpClient;
import okhttp3.internal.JavaNetCookieJar;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private static Retrofit retrofit = null;
    private static Context context;

    // ===========================================================
    // Constructors
    // ===========================================================
    public APIClient(Context context) {
        this.context = context;
    }
    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods from SuperClass
    // ===========================================================

    // ===========================================================
    // Methods for Interfaces
    // ===========================================================

    // ===========================================================
    // Public Methods
    // ===========================================================

//    public static Retrofit getClient() {
//
//        // init cookie manager
//       // CookieHandler cookieHandler;
//        CookieManager cookieManager = new CookieManager();
//        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
//
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addNetworkInterceptor(interceptor)
//                .cookieJar(new JavaNetCookieJar(cookieManager))
//                .addInterceptor(interceptor)
//                .readTimeout(60, TimeUnit.SECONDS)
//                .writeTimeout(60, TimeUnit.SECONDS)
//                .connectTimeout(60, TimeUnit.SECONDS)
//                .build();
//
//        retrofit = new Retrofit.Builder()
//                .baseUrl(BaseActivity.URL_API_SERVER)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
//                .build();
//
//        return retrofit;
//    }

    public static Retrofit getClient() {

        // init cookie manager
        // CookieHandler cookieHandler;
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .addInterceptor(interceptor)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BaseActivity.URL_API_SERVER)
                .addConverterFactory(GsonConverterFactory.create())

                .client(client)
                .build();

        return retrofit;
    }

    public static Retrofit getinstance(Context context)
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new AddCookiesInterceptor(context)) // This is used to add ApplicationInterceptor.
                //  .addNetworkInterceptor(new CustomInterceptor()) //This is used to add NetworkInterceptor.
                .build();
        //Defining the Retrofit using Builder
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseActivity.URL_API_SERVER) //This is the onlt mandatory call on Builder object.
                .client(okHttpClient) //The Htttp client to be used for requests
                .addConverterFactory(GsonConverterFactory.create()) // Convertor library used to convert response into POJO
                .build();
        return retrofit;
//        OkHttpClient client = new OkHttpClient();
//
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        // CookieHandler cookieHandler;
//        CookieManager cookieManager = new CookieManager();
//        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
//        builder.addInterceptor(new AddCookiesInterceptor()); // VERY VERY IMPORTANT
//        //builder.addInterceptor(new ReceivedCookiesInterceptor(context)); // VERY VERY IMPORTANT
//        builder.addInterceptor(logging);
//       // builder.addHeader("Cookie", cookie);
//        builder.cookieJar(new JavaNetCookieJar(cookieManager));
//        client = builder.build();
//
//        if(retrofit==null)
//        {
//            retrofit = new Retrofit.Builder().
//                    baseUrl(BaseActivity.URL_API_SERVER).
//
//
//                  //  addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
//                    addConverterFactory(GsonConverterFactory.create()).
//                    client(client).
//                    build();
//        }
//        return retrofit;
    }

    public static Retrofit PostMultipart(Context context, String url)
    {
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
       // interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
             //   .addInterceptor(interceptor)
                .addInterceptor(new AddCookiesInterceptor(context)) // This is used to add ApplicationInterceptor.
                //  .addNetworkInterceptor(new CustomInterceptor()) //This is used to add NetworkInterceptor.
                .build();
        //Defining the Retrofit using Builder
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url+"/") //This is the onlt mandatory call on Builder object.
                .client(okHttpClient) //The Htttp client to be used for requests
                .addConverterFactory(GsonConverterFactory.create()) // Convertor library used to convert response into POJO
                .build();
        return retrofit;
    }

    public static Retrofit getSimpleUrl(Context context)
    {
        // init cookie manager
        // CookieHandler cookieHandler;
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        cookieManager.getCookieStore();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .addInterceptor(interceptor)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BaseActivity.URL_API_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }


    // ===========================================================
    // Private Methods
    // ===========================================================

    // ===========================================================
    // Inner Classes/Interfaces
    // ===========================================================

}
