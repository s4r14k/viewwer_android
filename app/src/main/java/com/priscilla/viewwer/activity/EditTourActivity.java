package com.priscilla.viewwer.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.priscilla.viewwer.R;
import com.priscilla.viewwer.adapter.PlaceAutoSuggestAdapter;
import com.priscilla.viewwer.api.APIClient;
import com.priscilla.viewwer.api.APIInterface;
import com.priscilla.viewwer.model.Favorites;
import com.priscilla.viewwer.model.Tour;
import com.priscilla.viewwer.model.responseProfil;
import com.priscilla.viewwer.model.responseSearch;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.priscilla.viewwer.utils.App.getContext;

public class EditTourActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String[] Type = { "T1", "T2", "T3", "T4", "T5","T6","T7"};

    private Tour tour;
    private TextView ClassEnergyLastClicked;
    private TextView ClassGasLastClicked;
    private AutoCompleteTextView city;
    private TextView SaleTypeSale;
    private TextView SaleTypeRent;
    private TextView isAppartement;
    private TextView isHouse;
    private EditText EditTourArea;
    private TextView EditTourEnergyA;
    private TextView EditTourEnergyB;
    private TextView EditTourEnergyC;
    private TextView EditTourEnergyD;
    private TextView EditTourEnergyE;
    private TextView EditTourEnergyF;
    private TextView EditTourEnergyG;
    private TextView EditTourGasA;
    private TextView EditTourGasB;
    private TextView EditTourGasC;
    private TextView EditTourGasD;
    private TextView EditTourGasE;
    private TextView EditTourGasF;
    private TextView EditTourGasG;
    private EditText EditTourDescription;
    private EditText EditTourPrice;
    private Button EditTourFinish;
    private APIInterface _apiInterface;
    private Spinner spin;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tour);

        View rootView = findViewById(android.R.id.content).getRootView();
        //Toolbar
        new BaseActivity().toolbarActivity(rootView, R.id.EditTourToolbar, R.string.title_edit_profile,this);

        context = context;

        this.tour = new Tour();





        Bundle b = getIntent().getExtras();
        String EntityId = b.getString("id");


        EditTourInitProperty();

        initSpinner();

        GetTourList(EntityId);

        ForSaleClicked();
        ForRentClicked();
        appartmentClicked();
        houseClicked();
        setEnergyClassA();
        ClickEnergy();
        clickGas();

        finishClicked(EntityId);




    }

    private void initSpinner(){
        spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);


        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Type);
        aa.setDropDownViewResource(R.layout.list_spinner);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);


    }

    private void GetTourList(String Entityid){
        Log.d("eto","======EntityId "+ Entityid);

        _apiInterface = APIClient.getinstance(context).create(APIInterface.class);

        Call<responseSearch> call = _apiInterface.GetTour(Entityid);

        call.enqueue(new Callback<responseSearch>() {
            @Override
            public void onResponse(Call<responseSearch> call, Response<responseSearch> response) {
                Log.d("Response code:", "===========" + response.code() + "=======" +response.isSuccessful());
                if(response.isSuccessful()){
                    responseSearch tours = response.body();
                    Log.d("eto","======area"+ tours.getArea());
                    Log.d("eto","======area"+ tours.getPrice());
                    setAddress(tours.getAddress());
                    setSaleType(tours.getSaleType());
                    setIsHouse(tours.getHouse());
                    setArea(tours.getArea());
                    setDescription(tours.getDescription());
                    setPrice(tours.getPrice());
                    setEnergyClass(tours.getEnergyClass());
                    setGasClass(tours.getGasClass());
                    //get tour type for spinner
                    String replaceTourType = tours.getType().replace("t0","T");

                    for (int i = 0; i< Type.length;i++){
                        if(Type[i].equals(replaceTourType)){
                            spin.setSelection(i);
                        }
                    }
                }else{
                    Log.d("eto","======error"+ response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<responseSearch> call, Throwable t)
            {
                Log.d("Status response","respone etat "+t);
            }
        });
    }


    private void EditTourInitProperty() {
        ClassEnergyLastClicked = null;
        ClassGasLastClicked = null;

        city = findViewById(R.id.EditTourCity);
        city.setAdapter(new PlaceAutoSuggestAdapter(EditTourActivity.this,android.R.layout.simple_list_item_1));

        SaleTypeSale = (TextView) findViewById(R.id.EditTourForSale);
        SaleTypeRent = (TextView) findViewById(R.id.EditTourForRent);
        isAppartement = (TextView) findViewById(R.id.EditTourAppertment);
        isHouse = (TextView) findViewById(R.id.EditTourHouse);
        EditTourArea = (EditText) findViewById(R.id.EditTourArea);
        EditTourEnergyA = (TextView) findViewById(R.id.EditTourEnergyA);
        EditTourEnergyB = (TextView) findViewById(R.id.EditTourEnergyB);
        EditTourEnergyC = (TextView) findViewById(R.id.EditTourEnergyC);
        EditTourEnergyD = (TextView) findViewById(R.id.EditTourEnergyD);
        EditTourEnergyE = (TextView) findViewById(R.id.EditTourEnergyE);
        EditTourEnergyF = (TextView) findViewById(R.id.EditTourEnergyF);
        EditTourEnergyG = (TextView) findViewById(R.id.EditTourEnergyG);

        EditTourGasA = (TextView) findViewById(R.id.EditTourGasA);
        EditTourGasB = (TextView) findViewById(R.id.EditTourGasB);
        EditTourGasC = (TextView) findViewById(R.id.EditTourGasC);
        EditTourGasD = (TextView) findViewById(R.id.EditTourGasD);
        EditTourGasE = (TextView) findViewById(R.id.EditTourGasE);
        EditTourGasF = (TextView) findViewById(R.id.EditTourGasF);
        EditTourGasG = (TextView) findViewById(R.id.EditTourGasG);

        EditTourDescription = (EditText) findViewById(R.id.EditTourDescription);

        EditTourPrice = (EditText) findViewById(R.id.EditTourPrice);

        EditTourFinish = (Button) findViewById(R.id.EditTourFinish);



    }

    private void setAddress(String Address){
        if(!Address.equals(""))
            city.setText(Address);
    }

    private void setSaleType(String saleType){
        if(!saleType.equals("")){
            if(saleType.equals("sale")){
                BackgroundButtonTop(SaleTypeSale,SaleTypeRent);
                tour.setSaleType("sale");
            }else{
                BackgroundButtonTop(SaleTypeRent,SaleTypeSale);
                tour.setSaleType("rent");
            }
        }
    }
    private void setIsHouse(Boolean house){
        if(house == true){
            BackgroundButtonTop(isHouse,isAppartement);
            tour.setHouse(true);
        }
    }


    private void setArea(Double area){
        if(!area.toString().equals("")){
            EditTourArea.setText(area.toString());
        }
    }

    private void setDescription(String description){
        if(!description.equals(""))
            EditTourDescription.setText(description);
    }

    private void setPrice(Double price){
        if(!price.toString().equals(""))
            EditTourPrice.setText(price.toString());
    }

    private void setEnergyClass(String energy){
        switch (energy){
            case "A":
                classHouseChoose(EditTourEnergyA,"A",null);
                break;
            case "B":
                classHouseChoose(EditTourEnergyB,"B",null);
                break;
            case "C":
                classHouseChoose(EditTourEnergyC,"C",null);
                break;
            case "D":
                classHouseChoose(EditTourEnergyD,"D",null);
                break;
            case "E":
                classHouseChoose(EditTourEnergyE,"E",null);
                break;
            case "F":
                classHouseChoose(EditTourEnergyF,"F",null);
                break;
            case "G":
                classHouseChoose(EditTourEnergyG,"G",null);
                break;
                default:
                    tour.setEnergyClass("N");
        }
    }

    private void setGasClass(String gas){
        switch (gas){
            case "A":
                classHouseChoose(EditTourGasA,null,"A");
                break;
            case "B":
                classHouseChoose(EditTourGasB,null,"B");
                break;
            case "C":
                classHouseChoose(EditTourGasC,null,"C");
                break;
            case "D":
                classHouseChoose(EditTourGasD,null,"D");
                break;
            case "E":
                classHouseChoose(EditTourGasE,null,"E");
                break;
            case "F":
                classHouseChoose(EditTourGasF,null,"F");
                break;
            case "G":
                classHouseChoose(EditTourGasG,null,"G");
                break;
            default:
                tour.setEnergyClass("N");
        }
    }

    private void BackgroundButtonTop(TextView textView_cyan,TextView textView_grey){
        textView_cyan.setBackgroundResource(R.drawable.background_edit_tour_clicked);
        textView_cyan.setTextColor(ContextCompat.getColor(getContext(),R.color.color_white));
        textView_grey.setBackgroundResource(R.drawable.background_edit_tour_nonclicked);
        textView_grey.setTextColor(ContextCompat.getColor(getContext(),R.color.color_gray_dark));
    }

    private void ForSaleClicked(){
        SaleTypeSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackgroundButtonTop(SaleTypeSale,SaleTypeRent);
                tour.setSaleType("sale");
            }
        });
    }

    private void ForRentClicked(){
        SaleTypeRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackgroundButtonTop(SaleTypeRent,SaleTypeSale);
                tour.setSaleType("rent");
            }
        });
    }

    private void appartmentClicked(){
        isAppartement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackgroundButtonTop(isAppartement,isHouse);
                tour.setHouse(false);
            }
        });
    }

    private void houseClicked(){
        isHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackgroundButtonTop(isHouse,isAppartement);
                tour.setHouse(true);
            }
        });
    }

    private void classHouseChoose(TextView textView,String classEnergy,String classGas){
            if(classEnergy != null){
                if(ClassEnergyLastClicked == null) {
                    textView.getLayoutParams().width = 35;
                    textView.getLayoutParams().height = 35;
                    textView.requestLayout();
                    tour.setEnergyClass(classEnergy);
                    ClassEnergyLastClicked = textView;
                }else {
                    ClassEnergyLastClicked.getLayoutParams().height = 25;
                    ClassEnergyLastClicked.getLayoutParams().width = 25;
                    ClassEnergyLastClicked.requestLayout();

                    textView.getLayoutParams().width = 35;
                    textView.getLayoutParams().height = 35;
                    textView.requestLayout();
                    tour.setEnergyClass(classEnergy);

                    ClassEnergyLastClicked = textView;
                }
            }else if(classGas != null){
                if(ClassGasLastClicked == null) {
                    textView.getLayoutParams().width = 35;
                    textView.getLayoutParams().height = 35;
                    textView.requestLayout();
                    ClassGasLastClicked = textView;
                    tour.setGasClass(classGas);
                }else {
                    ClassGasLastClicked.getLayoutParams().height = 25;
                    ClassGasLastClicked.getLayoutParams().width = 25;
                    ClassGasLastClicked.requestLayout();

                    textView.getLayoutParams().width = 35;
                    textView.getLayoutParams().height = 35;
                    textView.requestLayout();
                    ClassGasLastClicked = textView;
                    tour.setGasClass(classGas);
                }
            }


    }

    private void setEnergyClassA(){
        classHouseChoose(EditTourEnergyA,"A",null);
    }

    private void ClickEnergy(){
        clickEnergieA();
        clickEnergieB();
        clickEnergieC();
        clickEnergieD();
        clickEnergieE();
        clickEnergieF();
        clickEnergieG();
    }

    private void clickEnergieA(){
        EditTourEnergyA.setClickable(true);
        EditTourEnergyA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classHouseChoose(EditTourEnergyA,"A",null);
            }
        });
    }

    private void clickEnergieB(){
        EditTourEnergyB.setClickable(true);
        EditTourEnergyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classHouseChoose(EditTourEnergyB,"B",null);
            }
        });
    }

    private void clickEnergieC(){
        EditTourEnergyC.setClickable(true);
        EditTourEnergyC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classHouseChoose(EditTourEnergyC,"C",null);
            }
        });
    }

    private void clickEnergieD(){
        EditTourEnergyD.setClickable(true);
        EditTourEnergyD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classHouseChoose(EditTourEnergyD,"D",null);
            }
        });
    }

    private void clickEnergieE(){
        EditTourEnergyE.setClickable(true);
        EditTourEnergyE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classHouseChoose(EditTourEnergyE,"E",null);
            }
        });
    }

    private void clickEnergieF(){
        EditTourEnergyF.setClickable(true);
        EditTourEnergyF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classHouseChoose(EditTourEnergyF,"F",null);
            }
        });
    }

    private void clickEnergieG(){
        EditTourEnergyG.setClickable(true);
        EditTourEnergyG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classHouseChoose(EditTourEnergyG,"G",null);
            }
        });
    }

    private void clickGas(){
        clickGasA();
        clickGasB();
        clickGasC();
        clickGasD();
        clickGasE();
        clickGasF();
        clickGasG();
    }

    private void clickGasA(){
        EditTourGasA.setClickable(true);
        EditTourGasA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classHouseChoose(EditTourGasA,null,"A");
            }
        });
    }

    private void clickGasB(){
        EditTourGasB.setClickable(true);
        EditTourGasB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classHouseChoose(EditTourGasB,null,"B");
            }
        });
    }

    private void clickGasC(){
        EditTourGasC.setClickable(true);
        EditTourGasC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classHouseChoose(EditTourGasC,null,"C");
            }
        });
    }

    private void clickGasD(){
        EditTourGasD.setClickable(true);
        EditTourGasD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classHouseChoose(EditTourGasD,null,"D");
            }
        });
    }

    private void clickGasE(){
        EditTourGasE.setClickable(true);
        EditTourGasE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classHouseChoose(EditTourGasE,null,"E");
            }
        });
    }

    private void clickGasF(){
        EditTourGasF.setClickable(true);
        EditTourGasF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classHouseChoose(EditTourGasF,null,"F");
            }
        });
    }

    private void clickGasG(){
        EditTourGasG.setClickable(true);
        EditTourGasG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classHouseChoose(EditTourGasG,null,"G");
            }
        });
    }



    private void finishClicked(final String Entityid){
        EditTourFinish.setClickable(true);
        EditTourFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double area;
                Double price;
                if(EditTourArea.getText().toString().equals("")){
                    area = 0.0;
                }else{
                    area = Double.parseDouble(EditTourArea.getText().toString());
                }

                if(EditTourPrice.getText().toString().equals("")){
                    price = 0.0;
                }else{
                    price = Double.parseDouble(EditTourPrice.getText().toString());
                }

                Call<Tour> call = _apiInterface.UpdateTour(Entityid,
                        city.getText().toString(),
                        tour.getSaleType(),
                        tour.getHouse(),
                        area,
                        tour.getEnergyClass(),
                        tour.getGasClass(),
                        EditTourDescription.getText().toString(),
                        price,
                        tour.getType().replace("T","t0"));


                call.enqueue(new Callback<Tour>() {
                    @Override
                    public void onResponse(Call<Tour> call, Response<Tour> response) {
                        Log.d("Response code:", "===========" + response.code() + "=======" +response.isSuccessful());
                        if(response.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Tour> call, Throwable t)
                    {
                        Log.d("Status response","respone etat "+t);
                    }
                });
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        tour.setType(Type[i]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
