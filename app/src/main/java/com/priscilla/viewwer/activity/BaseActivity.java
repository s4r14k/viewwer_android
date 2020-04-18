package com.priscilla.viewwer.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.priscilla.viewwer.MainActivity;
import com.priscilla.viewwer.R;
import com.priscilla.viewwer.api.APIClient;
import com.priscilla.viewwer.api.APIInterface;
import com.priscilla.viewwer.api.ApiCallBack;
import com.priscilla.viewwer.model.BaseResponse;
import com.priscilla.viewwer.model.Favorites;
import com.priscilla.viewwer.model.UpvoteModel;
import com.priscilla.viewwer.model.User;
import com.priscilla.viewwer.model.responseFavorite;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener
{

    // ===========================================================
    // Constants
    // ===========================================================
    public static final String PREFS_KEY_COOKIES = "PREFS_KEY_COOKIES";
    public static final String PREFS_COOKIES = "PREFS_COOKIES";

    private static  APIInterface _apiInterface;

    private static ArrayList<Favorites> favorites_list;


    public static int SPLASH_TIME_OUT = 2000;
    public static int LOADING_TIME_OUT = 100;


    //public static String URL_API_SERVER = "https://dev.viewwer.com";
    public static String MAPS_GOOGLE_KEY = "AIzaSyCZGkaBxVDN8MF_PzmH9C06iTIV2rSMOSc";
    public static String URL_API_SERVER = "https://dev.viewwer.com";
   // public static String URL_API_SERVER = "http://dummy.restapiexample.com/";

    private static Context context;

    private static ProgressDialog _progressDialog;
    private AlertDialog _alertDialog;
    private Dialog _dialog;

    private Typeface _font;

    private int mYear, mMonth, mDay;

    private BaseActivity baseActivity;

    private AlertDialog.Builder mbuilder;
    private AlertDialog dialog;
    private View view;


    // ===========================================================
    // Fields
    // ===========================================================


    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods from SuperClass
    // ===========================================================



    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //
    }*/


    // ===========================================================
    // Methods for Interfaces
    // ===========================================================

    // ===========================================================
    // Public Methods
    // ===========================================================


    // ===========================================================
    // Private Methods
    // ===========================================================


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.baseActivity = this;
    }

    protected void putStringPreference(Context context, String prefsName, String key, String value) {

        SharedPreferences preferences = context.getSharedPreferences(prefsName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(key, value);
        editor.commit();
    }

    protected String getStringPreference(Context context, String prefsName,
                                         String key) {

        SharedPreferences preferences = context.getSharedPreferences(
                prefsName, Activity.MODE_PRIVATE);
        String value = preferences.getString(key, "");
        return value;
    }

    public static void showLoadingView(Context context) {

        _progressDialog = new ProgressDialog(context, R.style.WidgetProgressBarSmall);
        _progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        _progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        _progressDialog.setCancelable(false);
        _progressDialog.show();
    }


    public  void  showLoadingView() {

        _progressDialog = new ProgressDialog(this, R.style.WidgetProgressBarSmall);
        _progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        _progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        _progressDialog.setCancelable(false);
        _progressDialog.show();
    }

    public void showDefaultErrorDialog() {
        this.hideAlertDialog();
        this.hideLoadingView();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_error_outline);
        builder.setTitle(R.string.default_error_title);
        builder.setMessage(R.string.default_error_message);
        builder.setPositiveButton(android.R.string.ok, null);

        _alertDialog = builder.show();

    }

    public void hideAlertDialog() {
        if (_alertDialog != null && _alertDialog.isShowing()) {
            _alertDialog.dismiss();
        }
        _alertDialog = null;
    }

    public static void  hideLoadingView() {
        if (_progressDialog != null && _progressDialog.isShowing()) {
            _progressDialog.dismiss();

        }
        _progressDialog = null;
    }

    public AlertDialog showNoInternetErrorDialog() {
        this.hideAlertDialog();
        this.hideLoadingView();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_action_network_wifi);
        builder.setTitle(R.string.internet_connexion_error_title);
        builder.setMessage(R.string.internet_connexion_error_message);
        builder.setPositiveButton(android.R.string.ok, null);

        _alertDialog = builder.show();

        return _alertDialog;
    }

    public void showLoginMessageDialog(String message) {
        this.hideAlertDialog();
        this.hideLoadingView();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_person_black);
        builder.setTitle(R.string.activity_user_error_title);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, null);

        _alertDialog = builder.show();
    }

    public void saveCurrentCookies(String cookies) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREFS_COOKIES, cookies);
        editor.apply();
    }

    public String  getCurrentCookies() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        return sharedPreferences.getString(PREFS_COOKIES, "");
    }

    // ===========================================================
    // Create favorite, delete favoris, switch icon favoris
    // ===========================================================
    public static void createFavorite(String EntityId, APIInterface _apiInterface){
        Call<responseFavorite> call = _apiInterface.CreateFavorite("AppTour",EntityId);
        call.enqueue(new Callback<responseFavorite>() {
            @Override
            public void onResponse(Call<responseFavorite> call, Response<responseFavorite> response) {
                Log.d("Response code:", "===========" + response.code() + "=======" +response.isSuccessful());

                if (response.isSuccessful()) {
                    responseFavorite userObject = response.body();
                    Log.d("Response code:", "=========== favoriteId" + userObject.getFavoriteId());
                    Log.d("Response code:", "===========Entity id" + userObject.getFavoriteId());
                }
            }

            @Override
            public void onFailure(Call<responseFavorite> call, Throwable t)
            {
                Log.d("Status response","=========== "+t);

            }
        });
    }

    public static void deleteFavory(String favoriteId,APIInterface _apiInterface){
        Call<responseFavorite> call = _apiInterface.DeleteFavorite(favoriteId);
        call.enqueue(new Callback<responseFavorite>() {
            @Override
            public void onResponse(Call<responseFavorite> call, Response<responseFavorite> response) {
                Log.d("Response code:", "===========" + response.code() + "=======" +response.isSuccessful());

                if (response.isSuccessful()) {
                    responseFavorite userObject = response.body();
                    Log.d("Response code:", "===========" + userObject.getStatus());
                }
            }

            @Override
            public void onFailure(Call<responseFavorite> call, Throwable t)
            {
                Log.d("Status response","=========== "+t);

            }
        });
    }

    public static void switchIconFavorite(String entityId,final ImageView favoriteIcon){
        final String EntityId = entityId;

        try {

            _apiInterface = APIClient.getinstance(context).create(APIInterface.class);

            Call<ArrayList<Favorites>> call = _apiInterface.GetListFavorites();

            call.enqueue(new Callback<ArrayList<Favorites>>() {
                @Override
                public void onResponse(Call<ArrayList<Favorites>> call, Response<ArrayList<Favorites>> response) {
                    Log.d("Response code:", "===========" + response.code() + "=======" +response.isSuccessful());

                    if (response.isSuccessful()) {
                        ArrayList<Favorites> favorites = response.body();

                        for (Favorites list : favorites){
                            if(list.getEntityId().equals(EntityId)){
                                Log.d("Response code:", "=========== déjà là");
                                favoriteIcon.setImageResource(R.drawable.ic_favorite_gray_dark);
                                deleteFavory(list.getFavoriteId(),_apiInterface);
                                return;
                            }
                        }

                        Log.d("Response code:", "===========Pas encore");

                        createFavorite(EntityId,_apiInterface);
                        favoriteIcon.setImageResource(R.drawable.ic_favorite_orange);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Favorites>> call, Throwable t)
                {
                    Log.d("Status response","=========== "+t);

                }
            });
        }catch (Exception e){
            Log.d("Status response","=========== "+e);
        }

    }

    protected void showChangedPassworMessage(String message) {
        this.hideAlertDialog();
        this.hideLoadingView();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_person_black);
        builder.setTitle(R.string.activity_change_password_Title_error);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, null);

        _alertDialog = builder.show();
    }

    public void initActionBarTitle(String title) {
         ActionBar  actionbar = getSupportActionBar();
        actionbar.setDisplayShowCustomEnabled(true);
       // actionBar.setCustomView(actionBarLayout);
        TextView textview = new TextView(BaseActivity.this);
        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textview.setLayoutParams(layoutparams);
        textview.setText(title);
        textview.setTypeface(_font);
        textview.setTextColor(Color.WHITE);
        textview.setGravity(Gravity.CENTER);
        textview.setTextSize(18);
        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar.setCustomView(textview);
    }


    private void showPopupTuto( Activity activity){
        //TextView textView = new TextView(activity);
        final CharSequence[] tuto= {"Tutoriel de Prise de Vue","Tutoriel Hotspots"};
        Intent intent = new Intent(activity,VideoTutoActivity.class);
        PopupDialogTuto popupDialogTuto = new PopupDialogTuto(activity);


        popupDialogTuto.getCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(activity,"annuler",Toast.LENGTH_SHORT).show();
                popupDialogTuto.dismiss();
            }
        });
        popupDialogTuto.getHotspots().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(activity,"hotspots",Toast.LENGTH_SHORT).show();

                CharSequence tutorial = tuto[1];
                intent.putExtra("tutorial",tutorial);
                activity.startActivity(intent);
                popupDialogTuto.dismiss();
            }
        });
        popupDialogTuto.getPratique().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(activity,"pratique",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(activity, CarousselActivity.class);
                activity.startActivity(intent);


//                CharSequence tutorial = tuto[2];
//                intent.putExtra("tutorial",tutorial);
//                activity.startActivity(intent);
                popupDialogTuto.dismiss();
            }
        });
        popupDialogTuto.getPrisedevue().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(activity,"prise de vue",Toast.LENGTH_SHORT).show();
                CharSequence tutorial = tuto[0];
                intent.putExtra("tutorial",tutorial);
                activity.startActivity(intent);
                popupDialogTuto.dismiss();
            }
        });


        popupDialogTuto.build();

    }
/**
    private void showTutoriel(final Context activityContext){
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(hgbv);
        myBuilder.setCancelable(true);
        final CharSequence[] tuto= {"Tutoriel de Prise de Vue","Tutoriel Hotspots","Bonne Pratique"};

        ImageView btuto = (ImageView) toolbar.findViewById(R.id.TutoButton);
        btuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupTuto popupTuto = new PopupTuto();
                popupTuto.showPopupWindow(v);
            }
        });



        myBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        myBuilder.setTitle("Tutoriels").setItems(tuto, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("string","======"+tuto[i].toString());
                Intent intent = new Intent(activityContext,VideoTutoActivity.class);
                CharSequence tutorial = tuto[i];
                intent.putExtra("tutorial",tutorial);
                activityContext.startActivity(intent);
            }
        });

        AlertDialog tutos = myBuilder.create();
        tutos.show();
    }
 */

    public void deleteCookies(Context context5){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context5);
//        SharedPreferences preferences = getSharedPreferences(PREFS_COOKIES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // editor.remove(PREFS_COOKIES);
        editor.clear();
        editor.commit();
        context5.startActivity(new Intent(context5,UserLoginActivity.class));
        finish();

    }

    public void logoutUser(final Context context4) {
        new AlertDialog.Builder(context4)
                .setTitle(R.string.app_name)
                .setMessage(R.string.logout)
                .setIcon(R.drawable.ic_power_black)
                .setCancelable(true)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();

                        BaseActivity.showLoadingView(context4);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideLoadingView();
                                deleteCookies(context4);
                            }
                        }, LOADING_TIME_OUT);
                    }
                })
                .setNegativeButton("Non", null)
                .show();
    }


    public void toolbarFragment(final View rootView, int idTooblar, int Title, final AppCompatActivity activity, final Context activities){
        Toolbar toolbar = rootView.findViewById(idTooblar);
        activity.setSupportActionBar(toolbar);



        TextView title = (TextView) toolbar.findViewById(R.id.ToolbarTitle);
        ImageView logout = (ImageView) toolbar.findViewById(R.id.ToolbarLogout);
        ImageView tuto = (ImageView) toolbar.findViewById(R.id.TutoButton);
        LinearLayout linearLayoutTuto = (LinearLayout) toolbar.findViewById(R.id.linearLayoutTutoButton);
        LinearLayout linearLayoutLogout = (LinearLayout) toolbar.findViewById(R.id.linearLayoutLogout);
        title.setText(Title);



        linearLayoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(activities != null){
                    logoutUser(activities);

                }else{
                    Log.d("Click","========= activities null");
                }
            }
        });
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if(activities != null){
//                    logoutUser(activities);
//
//                }else{
//                    Log.d("Click","========= activities null");
//
//                }
//
//            }
//        });
        linearLayoutTuto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(activities != null){
                    //showTutoriel(activities);

                    //ato ny atao le popup####
                    showPopupTuto(activity);

                }else{
                    Log.d("Click","========= activities null");
                }
            }
        });
//        tuto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(activities != null){
//                    showTutoriel(activities);
//
//                }else{
//                    Log.d("Click","========= activities null");
//                }
//            }
//        });
    }

    public  void toolbarActivity(View view,int idTooblar,int Title,final AppCompatActivity appCompatActivity){
        Toolbar toolbar = view.findViewById(idTooblar);
        appCompatActivity.setSupportActionBar(toolbar);

        TextView title = (TextView) toolbar.findViewById(R.id.ToolbarTitle);
        ImageView back = (ImageView) toolbar.findViewById(R.id.ToolbarBack);
        LinearLayout backTo = (LinearLayout) toolbar.findViewById(R.id.linearLayoutBack);

        title.setText(Title);
        backTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Click","========= button clicked");
                appCompatActivity.finish();
            }
        });
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("Click","========= button clicked");
//                appCompatActivity.finish();
//            }
//        });
    }

    public static void BounceAnimation(Button button,Context context){
        Animation bounceAnimation = AnimationUtils.loadAnimation(context, R.anim.bounce);
        button.startAnimation(bounceAnimation);
    }

    public static void deleteLike(String likeId,APIInterface _apiInterface){

        Log.d("Response mama likeId:", "===========" + likeId + "=======" +likeId);

        Call<BaseResponse> call = _apiInterface.Dislike(likeId);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Log.d("Response like deleted:", "===========" + response.code() + "=======" +response.isSuccessful());
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t)
            {
                Log.d("Status response","=========== "+t);
            }
        });
    }



    public static void createLike(String EntityId, APIInterface _apiInterface){
        Call<BaseResponse> call = _apiInterface.CreateLike(EntityId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Log.d("Response code:", "===========" + response.code() + "=======" +response.isSuccessful());
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t)
            {
                Log.d("Status response","=========== "+t);

            }
        });
    }

    public static void ListeLike(final String EntityId, final ApiCallBack callback){
        try {
            _apiInterface = APIClient.getinstance(context).create(APIInterface.class);

            Call<ArrayList<UpvoteModel>> call = _apiInterface.GetListLikes();

            call.enqueue(new Callback<ArrayList<UpvoteModel>>() {
                @Override
                public void onResponse(Call<ArrayList<UpvoteModel>> call, Response<ArrayList<UpvoteModel>> response) {
                    Log.d("Response UpvoteModel:", "===========" + response.code() + "=======" +response.isSuccessful());
                    if (response.isSuccessful()) {
                        ArrayList<UpvoteModel> likes = response.body();

                        if(likes.size() > 0) {
                            //   Log.d("listes likes",likes.toString())
                            UpvoteModel resResult = new UpvoteModel();
                            for (UpvoteModel list : likes){
                                if(list.getEntityId().trim().equals(EntityId)){
                                    resResult.setLikeId(list.getLikeId());
                                    resResult.setLiked(true);
                                    break;
                                }else{
                                    resResult.setLikeId(list.getLikeId());
                                    resResult.setLiked(false);
                                }
                            }
                            callback.onResponse(resResult);
                        }else{
                            //   Log.d("listes likes",likes.toString())
                            UpvoteModel resResult = new UpvoteModel();
                            resResult.setLikeId("");
                            resResult.setLiked(false);
                            //  Log.d("Response mivadika true:", "===========" + list.getEntityId() + "=======" +response.isSuccessful());
                            callback.onResponse(resResult);
                        }

                    }
                }

                @Override
                public void onFailure(Call<ArrayList<UpvoteModel>> call, Throwable t)
                {
                    Log.d("Status response","=========== "+t);

                }
            });


        }catch (Exception e){
            Log.d("Status response","=========== "+e);
        }
    }


    /**
     * Change counter like
     * @param model
     */
    public static void LikeTour(UpvoteModel model){
        Log.d("action model code2:", "===========" + model.getAction() + "=======" +model.getLikeId() + "=======" +model.getEntityType() );

        try {

            _apiInterface = APIClient.getinstance(context).create(APIInterface.class);

            Call<BaseResponse> call = _apiInterface.LikeTour(model);

            call.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    Log.d("valiny rating code3:", "===========" + response.code() + "=======" +response.isSuccessful());

                    if (response.isSuccessful()) {
                        BaseResponse result = response.body();

//                        if(result.getStatus().equals("success"))
//                        {
//                            likeIcon.setImageResource(R.drawable.ic_like_red);
//                        }

                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t)
                {
                    Log.d("Status response","=========== "+t);

                }
            });
        }catch (Exception e){
            Log.d("Status response","=========== "+e);
        }

    }

    public static void LikeTourRedIcon(String EntityId){

        try {

            _apiInterface = APIClient.getinstance(context).create(APIInterface.class);

            Call<BaseResponse> call = _apiInterface.CreateLike(EntityId);

            call.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    Log.d("Response code:", "===========" + response.code() + "=======" +response.isSuccessful());

                    if (response.isSuccessful()) {
                        BaseResponse result = response.body();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t)
                {
                    Log.d("Status response","=========== "+t);

                }
            });
        }catch (Exception e){
            Log.d("Status response","=========== "+e);
        }

    }

    public static void DislikeIcon(String LikeId){

        try {

            _apiInterface = APIClient.getinstance(context).create(APIInterface.class);

            Call<BaseResponse> call = _apiInterface.Dislike(LikeId);

            call.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    Log.d("Response code:", "===========" + response.code() + "=======" +response.isSuccessful());

                    if (response.isSuccessful()) {
                        BaseResponse result = response.body();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t)
                {
                    Log.d("Status response","=========== "+t);

                }
            });
        }catch (Exception e){
            Log.d("Status response","=========== "+e);
        }

    }

    @Override
    public void onClick(View v) {

    }
}
