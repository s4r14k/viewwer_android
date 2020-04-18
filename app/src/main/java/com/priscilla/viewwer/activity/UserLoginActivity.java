package com.priscilla.viewwer.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.NestedScrollView;
import okhttp3.Cookie;
import okhttp3.internal.http2.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.priscilla.viewwer.R;
import com.priscilla.viewwer.api.APIClient;
import com.priscilla.viewwer.api.APIInterface;
import com.priscilla.viewwer.model.User;
import com.priscilla.viewwer.model.responseUser;
import com.priscilla.viewwer.utils.InputValidation;

import java.lang.reflect.Array;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.util.Arrays;
import java.util.List;

public class UserLoginActivity extends BaseActivity implements View.OnClickListener {

    private final AppCompatActivity activity = UserLoginActivity.this;

    private Context _context;

    private NestedScrollView nestedScrollView;
    private LinearLayoutCompat linearLayoutCompat;

    private TextInputLayout textInputLayoutPseudo;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextPseudo;
    private TextInputEditText textInputEditTextPassword;

    private AppCompatButton appCompatButtonLogin;
    private AppCompatButton appCompatButtonLinkRegister;

    private InputValidation inputValidation;

    private APIInterface _apiInterface;

    private AppCompatButton appCompatButtonLinkFacebook;

    private AppCompatButton appCompatButtonLinkGoogle;

    private ImageView showAndHidePassword;

    private Boolean showPassword = false;

    private LoginButton ButtonLoginFacebook;

    private CallbackManager callbackManager;

    private GoogleSignInClient mGoogleSignInClient;

    private TextView UserLoginForgotPassword;

    private int RC_SIGN_IN = 0;
    private int FB_SIGN_IN = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        if(getCurrentCookies() != null && getCurrentCookies()!= ""){
            startActivity(new Intent(UserLoginActivity.this, BottomNavigationActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }

        initViews();
        showAndHidePassword();
        initListeners();
        initObjects();
    }

    // ===========================================================
    // Public Methods
    // ===========================================================

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonLogin:
                verifyFromDataBase();

                break;
            case R.id.appCompatButtonLinkRegister:
                // navigate to UserRegisterActivity
                startActivity(new Intent(getApplicationContext(), CreateUserActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.appCompatButtonLinkFacebook:
                // navigate to Connection facebook
                ButtonLoginFacebook.performClick();
                FB_SIGN_IN = 1;

                callbackManager = CallbackManager.Factory.create();

                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email","public_profile"));


                break;
            case R.id.appCompatButtonLinkGoogle:
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();

                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.UserLoginForgotPassword:
                Intent intent = new Intent(this, WebViewForgetPassword.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    // ===========================================================
    // Private Methods
    // ===========================================================

    /**
     * This method is to initialize views
     */
    private void initViews() {
        linearLayoutCompat = (LinearLayoutCompat) findViewById(R.id.userLoginActivityLinearLayoutCompat);

        UserLoginForgotPassword = (TextView) findViewById(R.id.UserLoginForgotPassword);

        textInputLayoutPseudo = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);

        textInputEditTextPseudo = (TextInputEditText) findViewById(R.id.textInputEditTextPseudo);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);

        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);
        appCompatButtonLinkRegister = (AppCompatButton) findViewById(R.id.appCompatButtonLinkRegister);

        showAndHidePassword = (ImageView) findViewById(R.id.showAndHidePassword);

        // retrofit
        _apiInterface = APIClient.getClient().create(APIInterface.class);

        appCompatButtonLinkFacebook = findViewById(R.id.appCompatButtonLinkFacebook);
        ButtonLoginFacebook = findViewById(R.id.ButtonLoginFacebook);

        appCompatButtonLinkGoogle = findViewById(R.id.appCompatButtonLinkGoogle);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(FB_SIGN_IN == 1){

            callbackManager.onActivityResult(requestCode, resultCode, data);
        }


        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                handleSignInResult(task);
            } catch (ApiException e) {
                appCompatButtonLinkGoogle.setText("error");
                Log.d("error","======= "+e);
            }
        }
    }

    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) throws ApiException {


        GoogleSignInAccount account = completedTask.getResult(ApiException.class);
        Log.d("info","======= info"+account);
        if (account != null) {
            String personName = account.getDisplayName();
            String personEmail = account.getEmail();
            String personId = account.getId();

            Log.d("info","======= name: "+personName+" email: "+personEmail+" id: "+personId);

            Call<responseUser> call = _apiInterface.ConnectToGoogle(personEmail, personId,personName,"google");
            call.enqueue(new Callback<responseUser>() {
                @Override
                public void onResponse(Call<responseUser> call, Response<responseUser> response) {
                    Log.d("Response code:", "===========" + response.code() + "=======" +response.isSuccessful());

                    if (response.isSuccessful()) {
                        responseUser userObject = response.body();

                        //hide loading
                        hideLoadingView();


                        Toast.makeText(getApplicationContext(), userObject.getStatus(), Toast.LENGTH_SHORT).show();

                        if (userObject.getStatus().equalsIgnoreCase("success") ) {

                            List<String> cookieList = response.headers().toMultimap().get("set-cookie");
                            saveCurrentCookies(cookieList.get(0));

                            java.net.CookieManager cookieManager = new CookieManager();
                            List<HttpCookie> Cookies =   cookieManager.getCookieStore().getCookies();

                            Toast.makeText(getApplicationContext(),"cookiesSize" + Cookies.size() ,Toast.LENGTH_LONG).show();

                            //  go to ListVisitsActivity
                            startActivity(new Intent(UserLoginActivity.this, BottomNavigationActivity.class));
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();

                        } else {
                            Log.d("status","failed");

                            // Log.d("erreur","Login incorrect");
                            // error message
                            showLoginMessageDialog(getString(R.string.activity_user_login_error_valid_pseudo_password));
                        }
                    } else {
                        // message
                        Log.d("status","======="+response.body());
                        showDefaultErrorDialog();
                    }
                }

                @Override
                public void onFailure(Call<responseUser> call, Throwable t)
                {
                    // message

                    showNoInternetErrorDialog();
                }
            });

        }

    }



    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken OldaccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken != null){
                //show loading
                /*showLoadingView();*/
                Call<responseUser> call = _apiInterface.ConnectToFacebook(currentAccessToken.getToken(), "facebook");
                call.enqueue(new Callback<responseUser>() {
                    @Override
                    public void onResponse(Call<responseUser> call, Response<responseUser> response) {
                        Log.d("Response code:", "===========" + response.code() + "=======" +response.isSuccessful());

                        if (response.isSuccessful()) {
                            responseUser userObject = response.body();

                            //hide loading
                            /*hideLoadingView();*/


                            Toast.makeText(getApplicationContext(), userObject.getStatus(), Toast.LENGTH_SHORT).show();

                            if (userObject.getStatus().equalsIgnoreCase("success") ) {

                                List<String> cookieList = response.headers().toMultimap().get("set-cookie");

                                if(getCurrentCookies().equals("")) {
                                    saveCurrentCookies(cookieList.get(0));
                                }

                                //  go to ListVisitsActivity
                                startActivity(new Intent(UserLoginActivity.this, BottomNavigationActivity.class));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();

                            } else {
                                Log.d("status","failed");

                                // Log.d("erreur","Login incorrect");
                                // error message
                                showLoginMessageDialog(getString(R.string.activity_user_login_error_valid_pseudo_password));
                            }
                        } else {
                            // message
                            Log.d("status","======="+response.body());
                            showDefaultErrorDialog();
                        }
                    }

                    @Override
                    public void onFailure(Call<responseUser> call, Throwable t)
                    {
                        // message

                        showNoInternetErrorDialog();
                    }
                });
            }

        }
    };

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        appCompatButtonLinkRegister.setOnClickListener(this);
        appCompatButtonLinkFacebook.setOnClickListener(this);
        appCompatButtonLinkGoogle.setOnClickListener(this);
        UserLoginForgotPassword.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        // TODO : databaseHelper = new DatabaseHelper(activity);
        inputValidation = new InputValidation(activity);

    }

    /**
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    private void verifyFromDataBase() {
        String login = textInputEditTextPseudo.getText().toString();
        String password = textInputEditTextPassword.getText().toString();

        if (!inputValidation.isInputEditTextFilled(textInputEditTextPseudo, textInputLayoutPseudo, getString(R.string.activity_user_login_error_message_pseudo))) {
            return;
        }

        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.activity_user_login_error_message_password))) {
            return;
        }


        // clear focus
        textInputEditTextPseudo.clearFocus();
        textInputEditTextPseudo.setError(null);
        textInputEditTextPassword.clearFocus();
        textInputEditTextPassword.setError(null);

        // show spinner
        showLoadingView();

        // request
        Call<responseUser> call = _apiInterface.loginUser(login, password);
        call.enqueue(new Callback<responseUser>() {
            @Override
            public void onResponse(Call<responseUser> call, Response<responseUser> response) {
                Log.d("Response code:", "===========" + response.code() + "=======" +response.isSuccessful());

                if (response.isSuccessful()) {
                    responseUser userObject = response.body();


                    // hide spinner
                    hideLoadingView();
//                    List<Cookie> cookies = httpClient.getCookieStore().getCookies();
//                    for (int i = 0; i < cookies.size(); i++) {
//                        cookie = cookies.get(i);
//                    }
                    Toast.makeText(getApplicationContext(), userObject.getStatus(), Toast.LENGTH_SHORT).show();

                    if (userObject.getStatus().equalsIgnoreCase("success") ) {
                        List<String> cookieList = response.headers().toMultimap().get("set-cookie");
                        saveCurrentCookies(cookieList.get(0));

                       //  go to ListVisitsActivity
                        startActivity(new Intent(UserLoginActivity.this, BottomNavigationActivity.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();

                    } else {
                        Log.d("status","failed");

                       // Log.d("erreur","Login incorrect");
                        // error message
                        showLoginMessageDialog(getString(R.string.activity_user_login_error_valid_pseudo_password));
                    }
                } else {
                    // message
                    showDefaultErrorDialog();
                }
            }

            @Override
            public void onFailure(Call<responseUser> call, Throwable t)
            {
                // message
                showNoInternetErrorDialog();
            }
        });
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextPseudo.setText(null);
        textInputEditTextPassword.setText(null);
    }

    private void showAndHidePassword(){
        showAndHidePassword.setClickable(true);
        showAndHidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!showPassword){
                    showAndHidePassword.setImageResource(R.drawable.ic_hide_password_dark);
                    TextInputEditText input = findViewById(R.id.textInputEditTextPassword);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    showPassword = true;
                }else{
                    showAndHidePassword.setImageResource(R.drawable.ic_show_password_dark);
                    TextInputEditText input = findViewById(R.id.textInputEditTextPassword);
                    input.setInputType(InputType.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
                    showPassword = false;
                }
            }
        });
    }

    // ===========================================================
    // Inner Classes/Interfaces
    // ===========================================================
}
