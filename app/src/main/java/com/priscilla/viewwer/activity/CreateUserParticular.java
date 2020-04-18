package com.priscilla.viewwer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.priscilla.viewwer.R;
import com.priscilla.viewwer.api.APIClient;
import com.priscilla.viewwer.api.APIInterface;
import com.priscilla.viewwer.model.RegisterUser;
import com.priscilla.viewwer.utils.InputValidation;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateUserParticular extends BaseActivity implements View.OnClickListener {


    private Button partCreerCompte=null;

    private EditText partTextMail,partTextFirstName,partTextLastName,partTextPhone,partenerCode;

    private TextInputEditText inputEditTextPassword,inputEditTextPasswordConfirm;

    private TextInputLayout textInputPassword,textInputPasswordConfirm;

    private APIInterface _apiInterface;

    private InputValidation inputValidation;

    private  AppCompatActivity appCompatActivity= CreateUserParticular.this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_particular);

        Toast.makeText(getApplicationContext(),"ici",Toast.LENGTH_LONG).show();

        initView();
        initListener();
        initObject();

        inputEditTextPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // Toast.makeText(getApplicationContext(),"before text change",Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                 Toast.makeText(getApplicationContext(),"after text change",Toast.LENGTH_LONG).show();

                if (!inputValidation.isInputPasswordAccept(inputEditTextPassword, textInputPassword,"8 caracteres dont 1 maj, 1 min et 1 symblole")) {
                    return;
                }
            }
        });

        inputEditTextPasswordConfirm.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // Toast.makeText(getApplicationContext(),"before text change",Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable arg0) {

                if (!inputValidation.isInputEditTextMatches(inputEditTextPassword, inputEditTextPasswordConfirm,textInputPasswordConfirm,"Mot de passe non identique")) {
                    return;
                }
            }
        });
    }

    public void initListener() {
        partCreerCompte.setOnClickListener(this);
        _apiInterface = APIClient.getClient().create(APIInterface.class);
    }
    public  void initObject(){
        inputValidation=new InputValidation(appCompatActivity);
    }

    public  void initView(){
        partTextMail=(EditText) findViewById(R.id.partTextEmail);
        partTextFirstName=(EditText)findViewById(R.id.partTextFirstName);
        partTextLastName=(EditText)findViewById(R.id.partTextLastName);
        partTextPhone=(EditText)findViewById(R.id.partTextPhone);
        partenerCode=(EditText)findViewById(R.id.partTextPartenerCode);
        textInputPassword=(TextInputLayout)findViewById(R.id.parttextInputPassword);
        textInputPasswordConfirm=(TextInputLayout)findViewById(R.id.parttextInputPasswordConfirm);
        inputEditTextPassword=(TextInputEditText)findViewById(R.id.partInputEditTextPassword);
        inputEditTextPasswordConfirm=(TextInputEditText)findViewById(R.id.partInputEditTextPasswordConfirmed);

        partCreerCompte=(Button)findViewById(R.id.partCreerCompte);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.partCreerCompte:
                registerUserParticular();
        }
    }
    public void registerUserParticular(){
        String mail= partTextMail.getText().toString();
        String password=inputEditTextPassword.getText().toString().trim();
        String confirmPassword=inputEditTextPasswordConfirm.getText().toString().trim();
        String firstName= partTextFirstName.getText().toString();
        String lastName= partTextLastName.getText().toString();
        String phone=partTextPhone.getText().toString();
        String codePartner=partenerCode.getText().toString();

        if (!inputValidation.isInputEditTextFill(partTextMail, getString(R.string.activity_user_error_input_invalid))){
            return;
        }

        if (!inputValidation.isInputEditTextFill(partTextFirstName, "veuillez saisir votre nom")){
            return;
        }if (!inputValidation.isInputEditTextFill(partTextLastName, "veuillez entrer votre prenom")){
            return;
        }



        partTextMail.clearFocus();
        partTextMail.setError(null);
        inputEditTextPassword.clearFocus();
        inputEditTextPassword.setError(null);
        inputEditTextPasswordConfirm.clearFocus();
        inputEditTextPasswordConfirm.setError(null);
        partTextFirstName.clearFocus();
        partTextFirstName.setError(null);
        partTextLastName.clearFocus();
        partTextLastName.setError(null);
        partTextPhone.clearFocus();
        partTextPhone.setError(null);
        partenerCode.clearFocus();
        partenerCode.setError(null);

        showLoadingView();

//        Call<RegisterUser> call=_apiInterface.registerUserParticular(mail,password,firstName,lastName,phone,codePartner);
//                    call.enqueue(new Callback<RegisterUser>() {
//                        @Override
//                        public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {
//                            Log.d("Response code:", "===========" + response.code() + "=======" +response.isSuccessful());
//                            if(response.isSuccessful()){
//                                RegisterUser userObject = response.body();
//                                hideLoadingView();
////                    Toast.makeText(getApplicationContext(), userObject.getStatus(), Toast.LENGTH_SHORT).show();
//                                if (userObject.getStatus().equalsIgnoreCase("success")) {
//                                    Log.d("status","success");
//                                    // store current user in cache
//                                    Toast.makeText(getApplicationContext(), userObject.getStatus(), Toast.LENGTH_SHORT).show();
////                        showMessageDialogSucces("creation compte avec succes");
//                                    //  go to ListVisitsActivity
//                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                                    finish();
//                                }else {
//                                    Log.d("status","failed");
//                                }
//                            }else {
//                    showDefaultErrorDialog();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RegisterUser> call, Throwable t)
//            {
//                // message
//                showNoInternetErrorDialog();
//            }
//        });

    }
    public void showMessageDialogSucces(String message){
        this.hideAlertDialog();
        this.hideLoadingView();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.icon_checked_green);
        builder.setTitle(R.string.activity_user_create_account);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, null);

        AlertDialog _alertDialog = builder.show();
    }
}
