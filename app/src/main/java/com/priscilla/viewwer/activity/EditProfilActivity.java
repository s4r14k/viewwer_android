package com.priscilla.viewwer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.priscilla.viewwer.Fragment.ProfilFragment;
import com.priscilla.viewwer.R;
import com.priscilla.viewwer.adapter.VisitAdapter;
import com.priscilla.viewwer.api.APIClient;
import com.priscilla.viewwer.api.APIInterface;
import com.priscilla.viewwer.model.responseProfil;
import com.priscilla.viewwer.model.responseProfilEdit;
import com.priscilla.viewwer.model.responseTour;
import com.priscilla.viewwer.model.responseUser;
import com.priscilla.viewwer.utils.InputValidation;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfilActivity extends  BaseActivity {
    private AppCompatButton changePasswordProfilButton;
    private AppCompatButton editProfilMAJButton;
    private APIInterface _apiInterface;
    private Context context;
    private TextInputEditText textInputEditTextFirstname;
    private TextInputEditText textInputEditTextLastname;
    private TextInputEditText textInputEditTextPhone;

    private TextInputLayout textInputEditTextLastnameLayout;
    private TextInputLayout textInputEditTextFirstnameLayout;

    private ArrayList<responseProfil> _user = new ArrayList<>();
    private InputValidation inputValidation;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_edit);
        this.context = context;


        View rootView = findViewById(android.R.id.content).getRootView();

        //Toolbar
        new BaseActivity().toolbarActivity(rootView,R.id.EditProfilToolbar,R.string.title_edit_profile,this);
        initView();

        Call<responseProfil> call = _apiInterface.GetMyProfil();

        //request DossierID

        call.enqueue(new Callback<responseProfil>() {

            @Override
            public void onResponse(Call<responseProfil> call, Response<responseProfil> response) {
                responseProfil objectResp = response.body();
                Log.d("Response code eto:", "=====" + response.isSuccessful()+ "code"+ response.code()+" message " + response.message());

                textInputEditTextFirstname = (TextInputEditText) findViewById(R.id.textInputEditTextFirstname);
                textInputEditTextLastname = (TextInputEditText) findViewById(R.id.textInputEditTextLastname);
                textInputEditTextPhone = (TextInputEditText) findViewById(R.id.textInputEditTextPhone);

                textInputEditTextLastnameLayout = (TextInputLayout) findViewById(R.id.textInputEditTextLastnameLayout);
                textInputEditTextFirstnameLayout = (TextInputLayout) findViewById(R.id.textInputEditTextFirstnameLayout);

                textInputEditTextFirstname.setText(objectResp.getFirstName());
                textInputEditTextLastname.setText(objectResp.getLastName());
                textInputEditTextPhone.setText(objectResp.getPhone());

                //    tourList = objectResp.getResultat();
                // Contains list of UserFolder


            }

            public void onFailure(Call<responseProfil> call, Throwable t) {
                Log.d("Response code eto:", "=====" + t.getMessage());

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initView(){
        _apiInterface = APIClient.getinstance(context).create(APIInterface.class);
        changePasswordProfilButton = (AppCompatButton) findViewById(R.id.changePasswordProfilButton);
        editProfilMAJButton = (AppCompatButton) findViewById(R.id.editProfilMAJButton);
        initObjects();

        onClickChangedProfil();

        onClickChangePasswordButton();


    }

    private void initObjects() {
        // TODO : databaseHelper = new DatabaseHelper(activity);
        inputValidation = new InputValidation(this);

    }

    public void onClickChangePasswordButton(){
        changePasswordProfilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BounceAnimation(changePasswordProfilButton,view.getContext());
                Intent intent = new Intent(EditProfilActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onClickChangedProfil(){
        editProfilMAJButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BounceAnimation(editProfilMAJButton,view.getContext());
                if (!inputValidation.isInputEditTextFilled(textInputEditTextLastname, textInputEditTextLastnameLayout, getString(R.string.activity_change_password_field_must_be_filled))) {
                    return;
                }

                if (!inputValidation.isInputEditTextFilled(textInputEditTextFirstname, textInputEditTextFirstnameLayout, getString(R.string.activity_change_password_field_must_be_filled))) {
                    return;
                }
                String firstName = textInputEditTextFirstname.getText().toString();
                String lastName = textInputEditTextLastname.getText().toString();
                String phone =  textInputEditTextPhone.getText().toString();

                showLoadingView();

                Call<responseProfilEdit> call = _apiInterface.UpdateProfil(firstName,lastName,phone);
                call.enqueue(new Callback<responseProfilEdit>() {
                    @Override
                    public void onResponse(Call<responseProfilEdit> call, Response<responseProfilEdit> response) {
                        Log.d("Response code:", "===========" + response.code() + "=======" +response.isSuccessful());

                        if (response.isSuccessful()) {
                            responseProfilEdit userObject = response.body();

                            // hide spinner
                            hideLoadingView();
                            Toast.makeText(getApplicationContext(), userObject.getStatus(), Toast.LENGTH_SHORT).show();

                            if (userObject.getStatus() != "failed") {
                                // store current user in cache

                                // go to ListVisitsActivity;
                                finish();

                            } else {
                                // error message
                                showLoginMessageDialog(getString(R.string.activity_user_login_error_valid_pseudo_password));
                            }
                        } else {
                            // message
                            showDefaultErrorDialog();
                        }
                    }

                    @Override
                    public void onFailure(Call<responseProfilEdit> call, Throwable t)
                    {
                        Log.d("Status response","respone etat "+t);
                        // message
                        showNoInternetErrorDialog();
                    }
                });
            }
        });
    }
}
