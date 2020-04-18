package com.priscilla.viewwer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.priscilla.viewwer.R;
import com.priscilla.viewwer.api.APIClient;
import com.priscilla.viewwer.api.APIInterface;
import com.priscilla.viewwer.model.responseProfilEdit;
import com.priscilla.viewwer.utils.InputValidation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity {
    private final AppCompatActivity activity = ChangePasswordActivity.this;

    private AppCompatButton PasswordChangedButton;

    private TextInputEditText textInputPasswordConfirmed;
    private TextInputEditText textInputNewPassword;
    private TextInputEditText textInputEditOldPassword;

    private TextInputLayout textInputPasswordConfirmedLayout;
    private TextInputLayout textInputNewPasswordLayout;
    private TextInputLayout textInputEditOldPasswordLayout;

    private InputValidation inputValidation;


    private APIInterface _apiInterface;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        View rootView = findViewById(android.R.id.content).getRootView();
        //Toolbar
        new BaseActivity().toolbarActivity(rootView,R.id.ChangePasswordToolbar,R.string.title_edit_profile,this);

        initView();
        initObjects();
        onClickChangedPassword();

        textInputNewPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // Toast.makeText(getApplicationContext(),"before text change",Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (!inputValidation.isInputPasswordAccept(textInputNewPassword, textInputNewPasswordLayout,"8 caracteres dont 1 maj, 1 min et 1 symblole")) {
                    return;
                }
            }
        });

        textInputPasswordConfirmed.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // Toast.makeText(getApplicationContext(),"before text change",Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable arg0) {

                if (!inputValidation.isInputEditTextMatches(textInputNewPassword, textInputPasswordConfirmed,textInputPasswordConfirmedLayout,"Mot de passe non identique")) {
                    return;
                }
            }
        });
    }

    private void initObjects() {
        // TODO : databaseHelper = new DatabaseHelper(activity);
        inputValidation = new InputValidation(activity);

    }

    public void initView(){
        PasswordChangedButton = (AppCompatButton) findViewById(R.id.PasswordChangedButton);

        textInputEditOldPassword = (TextInputEditText) findViewById(R.id.textInputEditOldPassword);
        textInputNewPassword = (TextInputEditText) findViewById(R.id.textInputNewPassword);
        textInputPasswordConfirmed = (TextInputEditText) findViewById(R.id.textInputPasswordConfirmed);

        textInputPasswordConfirmedLayout = (TextInputLayout) findViewById(R.id.textInputPasswordConfirmedLayout);
        textInputNewPasswordLayout = (TextInputLayout) findViewById(R.id.textInputNewPasswordLayout);
        textInputEditOldPasswordLayout = (TextInputLayout) findViewById(R.id.textInputEditOldPasswordLayout);

        _apiInterface = APIClient.getinstance(this).create(APIInterface.class);
    }

    public void onClickChangedPassword(){

        PasswordChangedButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                BounceAnimation(PasswordChangedButton, view.getContext());

                if (!inputValidation.isInputEditTextFilled(textInputEditOldPassword, textInputEditOldPasswordLayout, getString(R.string.activity_change_password_field_must_be_filled))) {
                    return;
                }

                if (!inputValidation.isInputEditTextFilled(textInputNewPassword, textInputNewPasswordLayout, getString(R.string.activity_change_password_field_must_be_filled))) {
                    return;
                }



                String oldPassword = textInputEditOldPassword.getText().toString();
                String newPassword = textInputNewPassword.getText().toString();
                String confirmed =  textInputPasswordConfirmed.getText().toString();

                if(!newPassword.equals(confirmed)){
                    showChangedPassworMessage(getString(R.string.activity_change_password_newPass_confirmPass_notEqual));
                }

                showLoadingView();

                Call<responseProfilEdit> call = _apiInterface.ChangePassword(oldPassword, newPassword);

                call.enqueue(new Callback<responseProfilEdit>() {
                    @Override
                    public void onResponse(Call<responseProfilEdit> call, Response<responseProfilEdit> response) {
                        Log.d("Response code:", "===========" + response.code() + "=======" +response.isSuccessful());
                        if (response.isSuccessful()) {
                            responseProfilEdit userObject = response.body();

                            // hide spinner
                            hideLoadingView();
                            Toast.makeText(getApplicationContext(), userObject.getStatus(), Toast.LENGTH_SHORT).show();

                            if (!userObject.getStatus().equals("failed")) {
                                // store current user in cache

                                // go to ListVisitsActivity

                                finish();

                            } else {
                                // error message
                                showChangedPassworMessage(getString(R.string.activity_change_password_old_password_error_serveur));
                            }
                        } else {
                            // message
                            showDefaultErrorDialog();
                        }

                    }

                    @Override
                    public void onFailure(Call<responseProfilEdit> call, Throwable t)
                    {
                        // message
                        Log.d("Erreur be mihitsy:", "===========" + t.getMessage());
                        showNoInternetErrorDialog();
                    }
                });
            }
        });
    }
}
