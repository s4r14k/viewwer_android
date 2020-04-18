package com.priscilla.viewwer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.priscilla.viewwer.Fragment.SearchFragment;
import com.priscilla.viewwer.R;
import com.priscilla.viewwer.adapter.PlaceAutoSuggestAdapter;
import com.priscilla.viewwer.api.APIClient;
import com.priscilla.viewwer.api.APIInterface;
import com.priscilla.viewwer.api.ApiCallBack;
import com.priscilla.viewwer.api.ApiCallBackVisite;
import com.priscilla.viewwer.model.SearchRequest;
import com.priscilla.viewwer.model.UpvoteModel;
import com.priscilla.viewwer.model.responseSearch;
import com.priscilla.viewwer.utils.App;
import com.priscilla.viewwer.utils.InputValidation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class FiltreAnnonceActivity extends BaseActivity implements View.OnClickListener {
    private AppCompatActivity activity = FiltreAnnonceActivity.this;
    private Context context;
    private APIInterface _apiInterface;

    private responseSearch responseObject;
    ArrayList<responseSearch> listResp = new ArrayList<responseSearch>();


    public SearchFragment fragment;
    public Fragment previousFragment;
    private Bundle bundle;

    private InputValidation inputValidation;

    private AppCompatButton appCompatButtonLaunchSearch;

    private AppCompatButton appCompatButtonApartment;
    private AppCompatButton appCompatButtonHouse;
    private AppCompatButton appCompatButtonAll;

    private AppCompatButton appCompatButtonRent;
    private AppCompatButton appCompatButtonSale;
    private AppCompatButton appCompatButtonAllRent;

    private TextInputLayout textInputLayoutBudgetMin;
    private TextInputLayout textInputLayoutBudgetMax;
    private TextInputLayout textInputLayoutSurfaceMin;
    private TextInputLayout textInputLayoutSurfaceMax;
    private TextInputLayout textInputLayoutPieceMin;
    private TextInputLayout textInputLayoutPieceMax;

    private TextInputEditText textInputEditTextBudgetMin;
    private TextInputEditText textInputEditTextBudgetMax;
    private TextInputEditText textInputEditTextSurfaceMin;
    private TextInputEditText textInputEditTextSurfaceMax;
    private TextInputEditText textInputEditTextPieceMax;
    private TextInputEditText textInputEditTextPieceMin;
    private AutoCompleteTextView textInputEditTextLocation;



    private String placeType = "all";
    private String saleType = "all";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = context;
        setContentView(R.layout.activity_filtre_annonce);

        //deleteCookies();


//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        setTitle(getResources().getString(R.string.activity_filtre_annonce_title_toolBar));

        View rootView = findViewById(android.R.id.content).getRootView();
        //Toolbar
        new BaseActivity().toolbarActivity(rootView,R.id.FilterAnnonceToolbar,R.string.activity_filtre_annonce_title_toolBar,this);

        AutoCompleteTextView autoCompleteTextView=findViewById(R.id.autocomplete);
        autoCompleteTextView.setAdapter(new PlaceAutoSuggestAdapter(FiltreAnnonceActivity.this,android.R.layout.simple_list_item_1));
        initViews();
        initListeners();
        initObjects();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonLaunchSearch:
                onClickButtonLaunchSearch();
                break;
            case R.id.appCompatButtonApartment:
                placeType = "apartment";
                appCompatButtonApartment.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_cyan_button));
                appCompatButtonHouse.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_visite));
                appCompatButtonAll.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_visite));

                break;
            case R.id.appCompatButtonHouse:
                placeType = "house";
                appCompatButtonHouse.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_cyan_button));
                appCompatButtonApartment.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_visite));
                appCompatButtonAll.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_visite));

                break;
            case R.id.appCompatButtonAll:
                placeType = "all";
                appCompatButtonAll.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_cyan_button));
                appCompatButtonHouse.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_visite));
                appCompatButtonApartment.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_visite));

                break;

            case R.id.appCompatButtonRent:
                saleType = "rent";
                appCompatButtonRent.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_cyan_button));
                appCompatButtonSale.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_visite));
                appCompatButtonAllRent.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_visite));

                break;
            case R.id.appCompatButtonSale:
                saleType = "sale";
                appCompatButtonSale.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_cyan_button));
                appCompatButtonRent.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_visite));
                appCompatButtonAllRent.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_visite));

                break;
            case R.id.appCompatButtonAllRent:
                saleType = "all";
                appCompatButtonAllRent.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_cyan_button));
                appCompatButtonRent.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_visite));
                appCompatButtonSale.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_visite));

                break;
            default:
                break;
        }
    }

    private void initListeners() {
        appCompatButtonLaunchSearch.setOnClickListener(this);
        appCompatButtonApartment.setOnClickListener(this);
        appCompatButtonHouse.setOnClickListener(this);
        appCompatButtonAll.setOnClickListener(this);
        appCompatButtonRent.setOnClickListener(this);
        appCompatButtonSale.setOnClickListener(this);
        appCompatButtonAllRent.setOnClickListener(this);
    }
    private void initObjects() {
        // TODO : databaseHelper = new DatabaseHelper(activity);
        inputValidation = new InputValidation(activity);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }



    public void onClickButtonLaunchSearch(){

        String budgetMin = textInputEditTextBudgetMin.getText().toString().trim();
        String budgetMax = textInputEditTextBudgetMax.getText().toString().trim();
        String surfaceMax = textInputEditTextSurfaceMax.getText().toString().trim();
        String surfaceMin = textInputEditTextSurfaceMin.getText().toString().trim();
        String pieceMax = textInputEditTextPieceMax.getText().toString().trim();
        String pieceMin = textInputEditTextPieceMin.getText().toString().trim();
        String location = textInputEditTextLocation.getText().toString().trim();

        SearchRequest.Location nestedObject = new SearchRequest.Location();
        String task1 = null;
        String task = null;
        String place_id = "";
        if(!location.isEmpty())
        {
            try {
                task1 = new HttpGetRequest().execute("https://maps.googleapis.com/maps/api/place/autocomplete/json?"
                        +"input=" + location + "&key="+ BaseActivity.MAPS_GOOGLE_KEY).get();

                JSONObject jObj = new JSONObject(task1);
                JSONArray response = jObj.getJSONArray("predictions");

                for(int i=0;i<response.length();i++)
                {
                    JSONObject jb1 = response.getJSONObject(i);
                    place_id = jb1.getString("place_id");
                    Log.d(".......",place_id);
                }

                task = new HttpGetRequest().execute("https://maps.googleapis.com/maps/api/place/details/json?"
                        +"place_id=" + place_id + "&key="+ BaseActivity.MAPS_GOOGLE_KEY).get();

                JSONObject jObj2 = new JSONObject(task);
                JSONObject response2 = jObj2.getJSONObject("result").getJSONObject("geometry").getJSONObject("location");
                String latitude = response2.getString("lat");
                String longitude = response2.getString("lng");
                nestedObject = new SearchRequest.Location(true,Double.parseDouble(latitude),Double.parseDouble(longitude),place_id);

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            nestedObject = new SearchRequest.Location(true,0,0,place_id);

        }

        SearchRequest request = new SearchRequest();


        double resbudgetMin = (!inputValidation.isInputEditTextNotDouble(textInputEditTextBudgetMin) ? 0 : Double.parseDouble(budgetMin));
        double resbudgetMax =(!inputValidation.isInputEditTextNotDouble(textInputEditTextBudgetMax) ? 0 : Double.parseDouble(budgetMax));
        double ressurfaceMax =(!inputValidation.isInputEditTextNotDouble(textInputEditTextSurfaceMax) ? 0 : Double.parseDouble(surfaceMax));
        double ressurfaceMin = (!inputValidation.isInputEditTextNotDouble(textInputEditTextSurfaceMin) ? 0 : Double.parseDouble(surfaceMin));
        double respieceMax = (!inputValidation.isInputEditTextNotDouble(textInputEditTextPieceMax) ? 0 : Double.parseDouble(pieceMax));
        double respieceMin = (!inputValidation.isInputEditTextNotDouble(textInputEditTextPieceMin) ? 0 : Double.parseDouble(pieceMin));



        request.setBudgetFrom(resbudgetMin);
        request.setBudgetTo(resbudgetMax);
        request.setAreaFrom(ressurfaceMin);
        request.setAreaTo(ressurfaceMax);
        request.setRoomsFrom(respieceMax);
        request.setRoomsTo(respieceMin);

        request.setPlaceType(placeType);
        request.setSaleType(saleType);

        request.setLocation(nestedObject);

        ListeVisite(request,new ApiCallBackVisite() {
            @Override
            public void onResponse(ArrayList<responseSearch> resultLike) {
                //miverina
                Log.d("azo ve2",resultLike.size()+"size");
                for(responseSearch test : resultLike){
                    Log.d("liste3 response3", test.getLikedId()+"  "+test.getLiked()+ listResp.size()+ "jj" + test.getCreatedAt());
                    // Collections.sort(test.getCreatedAt());
                }
                Collections.sort(resultLike,
                        (o2, o1) -> o1.getCreatedAt().compareTo(o2.getCreatedAt()));
                Log.d("mipasser0 response3", "eto mande soa");

                Intent intent = new Intent(getBaseContext(), BottomNavigationActivity.class);
                bundle = new Bundle();
                Log.d("mipasser1 response3", "eto mande soa");

                bundle.putParcelableArrayList("key",listResp);
                Log.d("mipasser2 response3", "eto mande soa");

                intent.putExtras(bundle); //Put your id to your next Intent
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getBaseContext().startActivity(intent);
                finish();
            }
        });

//        // request
//        Call<ArrayList<responseSearch>> call = _apiInterface.SearchTours2(request);
//
//        call.enqueue(new Callback<ArrayList<responseSearch>>() {
//            @Override
//            public void onResponse(Call<ArrayList<responseSearch>> call, Response<ArrayList<responseSearch>> response) {
//                Log.d("Response code:", "===========" + response.code() + "=======" +response.isSuccessful());
////                Toast.makeText(getApplicationContext(), response.code(), Toast.LENGTH_SHORT).show();
//                if (response.isSuccessful()) {
//                    ArrayList<responseSearch> userObject = response.body();
//                    for (final responseSearch list : userObject) {
//                        BaseActivity.ListeLike(list.getEntityId(), new ApiCallBack() {
//                            public void onResponse(UpvoteModel resultLike) {
//                                if (resultLike.isLiked) {
//                                    // do something
//                                    list.setLikedId(resultLike.LikeId);
//                                    list.setLiked(true);
//                                    Toast.makeText(App.getContext(), list.getLiked().toString(), Toast.LENGTH_LONG).show();
//
//                                } else {
//                                    // do something
//                                    list.setLikedId(resultLike.LikeId);
//                                    list.setLiked(false);
//                                    Toast.makeText(App.getContext(), list.getLiked().toString(), Toast.LENGTH_LONG).show();
//
//                                }
//                            }
//
//                        });
//                    }
//                    final Intent intent = new Intent(getBaseContext(), BottomNavigationActivity.class);
//                    bundle = new Bundle();
//                    bundle.putParcelableArrayList("key",userObject);
//                    intent.putExtras(bundle); //Put your id to your next Intent
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    getBaseContext().startActivity(intent);
//                    finish();
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<responseSearch>> call, Throwable t)
//            {
//                // message
//                showNoInternetErrorDialog();
//            }
//        });

    }

    public class HttpGetRequest extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(String... params){
            String stringUrl = params[0];

            String result;
            String inputLine;
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);         //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();         //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());         //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();         //Check if the line we are reading is not null
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }         //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();         //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
            }
            catch(IOException e){
                e.printStackTrace();
                result = null;
            }
            return result;
        }   protected void onPostExecute(String result){
            super.onPostExecute(result);
        }
    }

    public void ListeVisite(SearchRequest request,final ApiCallBackVisite callback){
        // request
        Call<ArrayList<responseSearch>> call = _apiInterface.SearchTours2(request);

        call.enqueue(new Callback<ArrayList<responseSearch>>() {
            @Override
            public void onResponse(Call<ArrayList<responseSearch>> call, Response<ArrayList<responseSearch>> response) {
                Log.d("Response code:", "===========" + response.code() + "=======" +response.isSuccessful());
                if (response.isSuccessful()) {

                    ArrayList<responseSearch> userObject = response.body();
                    if(userObject.size() > 0) {
                        for (final responseSearch list2 : userObject) {
                            BaseActivity.ListeLike(list2.getEntityId(), new ApiCallBack() {
                                @Override
                                public void onResponse(UpvoteModel result) {

                                    responseObject = list2;
                                    responseObject.setLiked(result.getLiked());
                                    responseObject.setLikedId(result.getLikeId());
                                    listResp.add(responseObject);
                                    callback.onResponse(listResp);
                                }
                            });
                        }
                    }else{
                        callback.onResponse(listResp);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<responseSearch>> call, Throwable t)
            {
                // message
                showNoInternetErrorDialog();
            }
        });
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        appCompatButtonLaunchSearch = (AppCompatButton) findViewById(R.id.appCompatButtonLaunchSearch);

        textInputLayoutBudgetMin = (TextInputLayout) findViewById(R.id.textInputLayoutBudgetMin);
        textInputLayoutBudgetMax = (TextInputLayout) findViewById(R.id.textInputLayoutBudgetMax);
        textInputLayoutSurfaceMax = (TextInputLayout) findViewById(R.id.textInputLayoutSurfaceMax);
        textInputLayoutSurfaceMin = (TextInputLayout) findViewById(R.id.textInputLayoutSurfaceMin);
        textInputLayoutPieceMin = (TextInputLayout) findViewById(R.id.textInputLayoutPieceMin);
        textInputLayoutPieceMax = (TextInputLayout) findViewById(R.id.textInputLayoutPieceMax);

        textInputEditTextBudgetMin = (TextInputEditText) findViewById(R.id.textInputEditTextBudgetMin);
        textInputEditTextBudgetMax = (TextInputEditText) findViewById(R.id.textInputEditTextBudgetMax);


        textInputEditTextSurfaceMax = (TextInputEditText) findViewById(R.id.textInputEditTextSurfaceMax);
        textInputEditTextSurfaceMin = (TextInputEditText) findViewById(R.id.textInputEditTextSurfaceMin);

        textInputEditTextPieceMin = (TextInputEditText) findViewById(R.id.textInputEditTextPieceMin);
        textInputEditTextPieceMax = (TextInputEditText) findViewById(R.id.textInputEditTextPieceMax);

        textInputEditTextLocation = (AutoCompleteTextView) findViewById(R.id.autocomplete);

        appCompatButtonApartment = (AppCompatButton) findViewById(R.id.appCompatButtonApartment);
        appCompatButtonHouse = (AppCompatButton) findViewById(R.id.appCompatButtonHouse);
        appCompatButtonAll = (AppCompatButton) findViewById(R.id.appCompatButtonAll);
        appCompatButtonRent = (AppCompatButton) findViewById(R.id.appCompatButtonRent);
        appCompatButtonSale = (AppCompatButton) findViewById(R.id.appCompatButtonSale);
        appCompatButtonAllRent = (AppCompatButton) findViewById(R.id.appCompatButtonAllRent);


        // retrofit
        _apiInterface = APIClient.getinstance(context).create(APIInterface.class);
    }



}
