package com.priscilla.viewwer.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.priscilla.viewwer.R;
import com.priscilla.viewwer.api.APIClient;
import com.priscilla.viewwer.api.APIInterface;
import com.priscilla.viewwer.model.BaseResponse;
import com.priscilla.viewwer.model.RegisterUser;
import com.priscilla.viewwer.utils.InputValidation;

import java.util.regex.Pattern;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.priscilla.viewwer.utils.App.getContext;

public class CreateUserActivity extends BaseActivity implements View.OnClickListener{

    private static final Pattern password_controller=Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\ S+$).{8,}$");

    private AppCompatActivity appCompatActivity=CreateUserActivity.this;

    private AppCompatButton creerCompte= null;

    private TextView professionnel,particulier;

    private EditText editTextEmail,editTextFirstName,editTextLastName,editTextPhone;

    private TextInputEditText  inputEditTextPassword,inputEditTextPasswordConfirmed;
    private TextInputLayout  textInputPassword,textInputPasswordConfirmed;

    private EditText editTextCompanyName;

    private EditText editTextActivity;

    private EditText editTextPartener;

    private InputValidation inputValidation;

    private APIInterface _apiInterface;

    private TextView agenceImmobilier;
    private TextView Mandataire;
    private TextView PromoteurConstructeur;
    private TextView syndicGestionnaire;
    private TextView marchandDeBien;
    private TextView architecte;
    private TextView autre,annuler;

    private View view,viewProfessionnel,viewParticulier;

    private AlertDialog.Builder mbuilder;
    private AlertDialog dialog;

    private RelativeLayout layoutProfessionnel, layoutParticulier;

    //Particulier
    private EditText partTextMail,partTextFirstName,partTextLastName,partTextPhone,partenerCode;
    private TextInputEditText partinputEditTextPassword,partinputEditTextPasswordConfirm;

    private TextInputLayout parttextInputPassword,parttextInputPasswordConfirm;
    private Button partCreerCompte=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        View rootView = findViewById(android.R.id.content).getRootView();
        //Toolbar
        new BaseActivity().toolbarActivity(rootView,R.id.toolBarCreateCompte,R.string.activity_create_account_title,this);

        creerCompte=(AppCompatButton)findViewById(R.id.creerCompte);

        _apiInterface = APIClient.getClient().create(APIInterface.class);

        //contenu_type_inscriptioin.addView(viewProfessionnel);

        initView();
        initListener();
        initObjects();

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
                if (!inputValidation.isInputPasswordAccept(inputEditTextPassword, textInputPassword,"8 caracteres dont 1 maj, 1 min et 1 symblole")) {
                    return;
                }
            }
        });

        inputEditTextPasswordConfirmed.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // Toast.makeText(getApplicationContext(),"before text change",Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable arg0) {

                if (!inputValidation.isInputEditTextMatches(inputEditTextPassword, inputEditTextPasswordConfirmed,textInputPasswordConfirmed,"Mot de passe non identique")) {
                    return;
                }
            }
        });

        partinputEditTextPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // Toast.makeText(getApplicationContext(),"before text change",Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable arg0) {

                if (!inputValidation.isInputPasswordAccept(partinputEditTextPassword, parttextInputPassword,"8 caracteres dont 1 maj, 1 min et 1 symblole")) {
                    return;
                }
            }
        });

        partinputEditTextPasswordConfirm.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // Toast.makeText(getApplicationContext(),"before text change",Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable arg0) {

                if (!inputValidation.isInputEditTextMatches(partinputEditTextPassword, partinputEditTextPasswordConfirm,parttextInputPasswordConfirm,"Mot de passe non identique")) {
                    return;
                }
            }
        });


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.creerCompte:
                registerUser();
//                Toast.makeText(getApplicationContext(), "compte creer avec succes", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getApplicationContext(), UserLoginActivity.class));
                break;
            case R.id.partParticulier:
                layoutProfessionnel.setVisibility(View.GONE);
                layoutParticulier.setVisibility(View.VISIBLE);
                particulier.setTextColor(Color.WHITE);
                particulier.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.color_blue_dark));
                professionnel.setTextColor(Color.BLACK);
                professionnel.setBackgroundColor(Color.WHITE);
                break;
            case R.id.partProfessionnel:
                layoutParticulier.setVisibility(View.GONE);
                layoutProfessionnel.setVisibility(View.VISIBLE);
                professionnel.setTextColor(Color.WHITE);
                professionnel.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.color_blue_dark));
                particulier.setTextColor(Color.BLACK);
                particulier.setBackgroundColor(Color.WHITE);

                break;
            case R.id.textActivity:
                //Toast.makeText(getApplicationContext(), "activité", Toast.LENGTH_SHORT).show();
                dialog.show();
                break;
            case R.id.agenceImmobilier:
                Toast.makeText(getApplicationContext(), "Agence Immobilier", Toast.LENGTH_SHORT).show();
                editTextActivity.setText(agenceImmobilier.getText());
                dialog.hide();
                break;
            case  R.id.Mandataire:
                Toast.makeText(getApplicationContext(), "Mandataire", Toast.LENGTH_SHORT).show();
                editTextActivity.setText(Mandataire.getText());
                dialog.hide();
                break;
            case  R.id.PromoteurConstructeur:
                Toast.makeText(getApplicationContext(), "Promoteur Constructeur", Toast.LENGTH_SHORT).show();
                editTextActivity.setText(PromoteurConstructeur.getText());
                dialog.hide();
                break;
            case  R.id.syndicGestionnaire:
                Toast.makeText(getApplicationContext(), "Syndic et Gestionnaire", Toast.LENGTH_SHORT).show();
                editTextActivity.setText(syndicGestionnaire.getText());
                dialog.hide();
                break;
            case  R.id.marchandDeBien:
                Toast.makeText(getApplicationContext(), "Marchand De Bien", Toast.LENGTH_SHORT).show();
                editTextActivity.setText(marchandDeBien.getText());
                dialog.hide();
                break;
            case  R.id.architecte:
                Toast.makeText(getApplicationContext(), "Architecte", Toast.LENGTH_SHORT).show();
                editTextActivity.setText(architecte.getText());
                dialog.hide();
                break;
            case  R.id.autre:
                Toast.makeText(getApplicationContext(), "Entrer votre activité", Toast.LENGTH_SHORT).show();
                editTextActivity.setFocusable(editTextActivity.getFocusable());
                editTextActivity.setText("");
                dialog.hide();
                break;
            case R.id.partCreerCompte:
                registerUserParticular();
                break;
            case R.id.annuler:
                dialog.hide();
                break;
        }
    }
    public void initView(){

        editTextEmail = (EditText) findViewById(R.id.textEmail);
        inputEditTextPassword= (TextInputEditText) findViewById(R.id.inputEditTextPassword);
        textInputPassword= (TextInputLayout) findViewById(R.id.textInputPassword);
        textInputPasswordConfirmed= (TextInputLayout) findViewById(R.id.textInputPasswordConfirmed);
        inputEditTextPasswordConfirmed=(TextInputEditText) findViewById(R.id.inputEditTextPasswordConfirmed);
        annuler = (TextView) findViewById(R.id.annuler);


        editTextFirstName = (EditText) findViewById(R.id.textFirstName);
        editTextLastName = (EditText) findViewById(R.id.textLastName);
        editTextPhone=(EditText) findViewById(R.id.textPhone);
        editTextCompanyName=(EditText)findViewById(R.id.textCompanyName);
        editTextActivity=(EditText)findViewById(R.id.textActivity);
        editTextActivity.setFocusable(false);
        editTextPartener=(EditText)findViewById(R.id.textPartenerCode);
        creerCompte = (AppCompatButton) findViewById(R.id.creerCompte);

        view= getLayoutInflater().inflate(R.layout.activity_modal,null);

        //modal
        agenceImmobilier = (TextView) view.findViewById(R.id.agenceImmobilier);
        Mandataire = (TextView)view.findViewById(R.id.Mandataire);
        PromoteurConstructeur = (TextView)view.findViewById(R.id.PromoteurConstructeur);
        syndicGestionnaire = (TextView)view.findViewById(R.id.syndicGestionnaire);
        marchandDeBien = (TextView)view.findViewById(R.id.marchandDeBien);
        architecte = (TextView)view.findViewById(R.id.architecte);
        autre = (TextView)view.findViewById(R.id.autre);

        //for inflater
        mbuilder= new AlertDialog.Builder(CreateUserActivity.this);
        mbuilder.setView(view);
        dialog= mbuilder.create();

        //include
        //contenu_type_inscriptioin = (RelativeLayout) findViewById(R.id.contenu_create_user);
        layoutProfessionnel= findViewById(R.id.include_professionnel);
        layoutParticulier = findViewById(R.id.include_particulier);



        //chose professionnel/particular
        particulier=(TextView) findViewById(R.id.partParticulier);
        particulier.setTextColor(Color.BLACK);
        professionnel=(TextView)findViewById(R.id.partProfessionnel);
        professionnel.setTextColor(Color.WHITE);
        professionnel.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.color_blue_dark));


        //Particulier
        partTextMail=(EditText) findViewById(R.id.partTextEmail);
        partTextFirstName=(EditText)findViewById(R.id.partTextFirstName);
        partTextLastName=(EditText)findViewById(R.id.partTextLastName);
        partTextPhone=(EditText)findViewById(R.id.partTextPhone);
        partenerCode=(EditText)findViewById(R.id.partTextPartenerCode);
        parttextInputPassword=(TextInputLayout)findViewById(R.id.parttextInputPassword);
        parttextInputPasswordConfirm=(TextInputLayout)findViewById(R.id.parttextInputPasswordConfirm);
        partinputEditTextPassword=(TextInputEditText)findViewById(R.id.partInputEditTextPassword);
        partinputEditTextPasswordConfirm=(TextInputEditText)findViewById(R.id.partInputEditTextPasswordConfirmed);

        partCreerCompte=(Button)findViewById(R.id.partCreerCompte);


    }
    public void initListener(){
        partCreerCompte.setOnClickListener(this);
        creerCompte.setOnClickListener(this);
        professionnel.setOnClickListener(this);
        particulier.setOnClickListener(this);
        professionnel.setOnClickListener(this);
        editTextActivity.setOnClickListener(this);
        annuler.setOnClickListener(this);

       // editTextLastName.addTextChangedListener(mEmailEntryWatcher);


        agenceImmobilier.setOnClickListener(this);
        Mandataire.setOnClickListener(this);
        PromoteurConstructeur.setOnClickListener(this);
        syndicGestionnaire.setOnClickListener(this);
        marchandDeBien.setOnClickListener(this);
        architecte.setOnClickListener(this);
        autre.setOnClickListener(this);
    }
    private  void initObjects() {
        inputValidation = new InputValidation(appCompatActivity);

    }

    public void registerUser(){
        String email= editTextEmail.getText().toString();
       // String password=textInputPassword.getEditText().getText().toString().trim();
        String password=inputEditTextPassword.getText().toString().trim();
        String passwordConfirm=inputEditTextPasswordConfirmed.getText().toString().trim();
//        String password=textInputPassword.getText().getText().toString().trim();
//        String passwordConfirm=textInputConfirmPassword.getEditText().getText().toString().trim();
        String firstName=editTextFirstName.getText().toString();
        String lastName=editTextLastName.getText().toString();
        String phone=editTextPhone.getText().toString();
        boolean IsPro=true;
        String companyName=editTextCompanyName.getText().toString();
        String activity=editTextActivity.getText().toString();
        String codePartener=editTextPartener.getText().toString();

        if (!inputValidation.isInputEditTextFill(editTextCompanyName, "veuillez saisir le nom de votre societe")){
            return;
        }
        if (!inputValidation.isInputEditTextFill(editTextActivity, "veuillez entrer votre activite")){
            return;
        }
        if (!inputValidation.isInputEditTextFill(editTextFirstName, "veuillez saisir votre nom")){
            return;
        }

        if (!inputValidation.isInputEditTextFill(editTextLastName, "veuillez entrer votre prenom")){
            return;
        }

        if (!inputValidation.isInputEditTextFill(editTextEmail, getString(R.string.activity_user_error_input_invalid))){
            return;
        }

        if (!inputValidation.isInputEditTextFill(inputEditTextPasswordConfirmed, "veuillez retaper votre mot de passe")){
            return;
        }

        if (!inputValidation.isInputEditTextFill(inputEditTextPassword, "veuillez entrer votre mot de passe")){
            return;
        }

        if (!inputValidation.isInputEditTextEmail(editTextEmail, "Format e-mail invalide")) {
                        return;
        }



        editTextEmail.clearFocus();
        editTextEmail.setError(null);
//        textInputPassword.clearFocus();
//        textInputPassword.setError(null);
        editTextFirstName.clearFocus();
        editTextFirstName.setError(null);
        editTextLastName.clearFocus();
        editTextLastName.setError(null);
        editTextPhone.clearFocus();
        editTextPhone.setError(null);
        editTextCompanyName.clearFocus();
        editTextCompanyName.setError(null);
        editTextActivity.clearFocus();
        editTextActivity.setFocusable(false);
        editTextActivity.setError(null);
        editTextPartener.clearFocus();
        editTextPartener.setError(null);

        showLoadingView();

        Call<BaseResponse> call= _apiInterface.registerUser(companyName,activity,firstName,lastName,email,phone,password,IsPro,codePartener);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                Log.d("Response code:", "===========" + response.code() + "=======" +response.isSuccessful());

                if(response.isSuccessful()){

                    BaseResponse userObject = response.body();
                    hideLoadingView();

//                    Toast.makeText(getApplicationContext(), userObject.getStatus(), Toast.LENGTH_SHORT).show();
                    if (userObject.getStatus().equalsIgnoreCase("success")) {
                        Log.d("status","success");

                        // store current user in cache
                        showMessageDialogSucces("creation compte avec succes");
                        //  go to ListVisitsActivity
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }else {
                       String resultError =  userObject.getError();
                        switch (resultError)
                        {
                            case "bad_request":
                                Toast.makeText(getApplicationContext(),   "Format d'email incorrect", Toast.LENGTH_SHORT).show();
                                break;
                            case "internal":
                                Toast.makeText(getApplicationContext(),  "Invalide code partenaire", Toast.LENGTH_SHORT).show();
                                break;
                            case "user_exists":
                                Toast.makeText(getApplicationContext(),  "Compte existant", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(),  userObject.getErrorDescription(), Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                }else {
                    hideLoadingView();
                    showDefaultErrorDialog();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t)
            {
                hideLoadingView();
                // message
                showNoInternetErrorDialog();
            }
        });


    }

    public void registerUserParticular(){
        String mail= partTextMail.getText().toString();
        String password=partinputEditTextPassword.getText().toString().trim();
        String firstName= partTextFirstName.getText().toString();
        String lastName= partTextLastName.getText().toString();
        String phone=partTextPhone.getText().toString();
        String codePartner=partenerCode.getText().toString();



        if (!inputValidation.isInputEditTextFill(partTextFirstName, "veuillez saisir votre nom")){
            return;
        }if (!inputValidation.isInputEditTextFill(partTextLastName, "veuillez entrer votre prenom")){
            return;
        }
        if (!inputValidation.isInputEditTextFill(partTextMail, getString(R.string.activity_user_error_input_invalid))){
            return;
        }
        if (!inputValidation.isInputEditTextEmail(partTextMail, "Format e-mail invalide")){
            return;
        }
        if (!inputValidation.isInputEditTextFill(partinputEditTextPassword, "veuillez entrer votre mot de passe")){
            return;
        }
        if (!inputValidation.isInputEditTextFill(partinputEditTextPasswordConfirm, "veuillez retaper votre mot de passe")){
            return;
        }


        partTextMail.clearFocus();
        partTextMail.setError(null);
        inputEditTextPassword.clearFocus();
        inputEditTextPassword.setError(null);
        partTextFirstName.clearFocus();
        partTextFirstName.setError(null);
        partTextLastName.clearFocus();
        partTextLastName.setError(null);
        partTextPhone.clearFocus();
        partTextPhone.setError(null);
        partenerCode.clearFocus();
        partenerCode.setError(null);

        showLoadingView();


        Log.d("Response layout:", "===========" + mail + "=======" +password+"====="
                +firstName+"====="+lastName+"====="+phone+"===="+ codePartner);

        Call<BaseResponse> call=_apiInterface.registerUserParticular(mail,password,firstName,lastName,phone,false,codePartner);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Log.d("Response code:", "===========" + response.code() + "=======" +response.isSuccessful());
                if(response.isSuccessful()){

                    BaseResponse userObject = response.body();
                    hideLoadingView();

//                    Toast.makeText(getApplicationContext(), userObject.getStatus(), Toast.LENGTH_SHORT).show();
                    if (userObject.getStatus().equalsIgnoreCase("success")) {
                        Log.d("status","success");

                        // store current user in cache
                        showMessageDialogSucces("creation compte avec succes");
                        //  go to ListVisitsActivity
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }else {
                        String resultError =  userObject.getError();
                        switch (resultError)
                        {
                            case "bad_request":
                                Toast.makeText(getApplicationContext(),   "Format d'email incorrect", Toast.LENGTH_SHORT).show();
                                break;
                            case "internal":
                                Toast.makeText(getApplicationContext(),  "Invalide code partenaire", Toast.LENGTH_SHORT).show();
                                break;
                            case "user_exists":
                                Toast.makeText(getApplicationContext(),  "Compte existant", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(),  userObject.getErrorDescription(), Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                }else {
                    hideLoadingView();
                    showDefaultErrorDialog();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t)
            {
                hideLoadingView();
                // message
                showNoInternetErrorDialog();
            }
        });

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


