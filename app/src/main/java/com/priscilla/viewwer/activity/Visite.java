package com.priscilla.viewwer.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.gson.Gson;
import com.priscilla.viewwer.R;
import com.priscilla.viewwer.adapter.PlaceAutoSuggestAdapter;
import com.priscilla.viewwer.api.APIClient;
import com.priscilla.viewwer.api.APIInterface;
import com.priscilla.viewwer.api.ApiBaseResponseCallBack;
import com.priscilla.viewwer.model.BaseResponse;
import com.priscilla.viewwer.model.ChangeSettingsRequest;
import com.priscilla.viewwer.model.HotSpot;
import com.priscilla.viewwer.model.Room;
import com.priscilla.viewwer.model.SceneModel;
import com.priscilla.viewwer.model.StartTaskRequest;
import com.priscilla.viewwer.model.Tour;
import com.priscilla.viewwer.model.TourBuildRequest;
import com.priscilla.viewwer.model.TourSettings;
import com.priscilla.viewwer.model.responseSearch;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.priscilla.viewwer.utils.App.getContext;
import static com.priscilla.viewwer.utils.Extensions.GetThumbName;
import static com.priscilla.viewwer.utils.Extensions.ToScenes;
import static com.priscilla.viewwer.utils.Extensions.ToThumbs;

public class Visite extends BaseActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{

    private AppCompatActivity appCompatActivity =Visite.this;

    private TextView textViewSaveTour;

    private Toolbar toolbar;

    public String UploadURL;
    public String tourId;

    public String UploadApiKey;
    private APIInterface _apiInterface;

    private Button buttonAddHotspot;
    private Button buttonFinaliseTour;

    private ArrayList<HotSpot> HotspotParamater;

    private TourSettings m_tourSettings = new TourSettings();

    private String[] Type = { "T1", "T2", "T3", "T4", "T5","T6","T7"};
    private int indexRoom = 0 ;

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
    private Spinner spin;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tour);
        textViewSaveTour=(TextView)findViewById(R.id.saveTour);
        buttonAddHotspot=(Button) findViewById(R.id.AddHotspot);

        HotspotParamater = (ArrayList<HotSpot>) getIntent().getSerializableExtra("hotpspotParameter");

        /*buttonAddHotspot.setOnClickListener(this);*/


        initView();
        initListener();

        View rootView = findViewById(android.R.id.content).getRootView();
        //Toolbar
        new BaseActivity().toolbarActivity(rootView, R.id.EditTourToolbar, R.string.panorama_back,this);


        this.tour = new Tour();

        EditTourInitProperty();

        initSpinner();

        ForSaleClicked();
        ForRentClicked();
        appartmentClicked();
        houseClicked();
        setEnergyClassA();
        ClickEnergy();
        clickGas();
    }

    private void initSpinner(){
        spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(Visite.this);


        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Type);
        aa.setDropDownViewResource(R.layout.list_spinner);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);


    }



    private void EditTourInitProperty() {
        ClassEnergyLastClicked = null;
        ClassGasLastClicked = null;

        city = findViewById(R.id.EditTourCity);
        city.setAdapter(new PlaceAutoSuggestAdapter(Visite.this,android.R.layout.simple_list_item_1));

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



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        tour.setType(Type[i]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    //================================================ Send to backOffice ===============================================================

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.EditTourFinish:
                Toast.makeText(getApplicationContext(),"sauver ",Toast.LENGTH_SHORT).show();
                SendResultInBackOffice();

                break;
            case R.id.AddHotspot:
                Toast.makeText(getApplicationContext(),"sauver la visite",Toast.LENGTH_SHORT).show();
        }
    }
    public void initView(){
        _apiInterface = APIClient.getinstance(getContext()).create(APIInterface.class);
        textViewSaveTour=(TextView)findViewById(R.id.saveTour);
        buttonAddHotspot=(Button) findViewById(R.id.AddHotspot);
        buttonFinaliseTour = (Button) findViewById(R.id.EditTourFinish);

    }

    public void initListener(){
        buttonFinaliseTour.setOnClickListener(this);
    }

    public void  SendResultInBackOffice(){
        List<byte[]> panoFile;
        List<byte[]> panoThumb;

        byte[] b_panoFile= null, b_panoThumb;
        ArrayList<Room> m_rooms = new ArrayList<Room>();

        ChangeSettingsRequest.Settings m_settings = new ChangeSettingsRequest().new Settings();

        m_settings.Hotspots = new ArrayList<>();


        m_settings.SceneTypes = new ArrayList<>();


        File imgFile = new  File(Environment.getExternalStorageDirectory() + File.separator +"viewwer/");
        if(imgFile.exists()){
            File[] files = imgFile.listFiles();
            int index = 0;
            for (File file:files
            ) {
                index ++;

                /***************************************************************************************************************
                 ** Settings ***************************************************************************************************/

                ChangeSettingsRequest.Settings.SceneTypeRequest scenes = new ChangeSettingsRequest().new Settings().new SceneTypeRequest();
                scenes.setSceneId(String.valueOf(index));
                scenes.setType("sceneType_"+index);
                m_settings.SceneTypes.add(scenes);

            }
        }else{
            Log.d("Image not exist",imgFile.getName() + "#######");
        }

        for (HotSpot hotspot: HotspotParamater
        ) {
            ChangeSettingsRequest.Settings.Hotspot hotspots = new ChangeSettingsRequest().new Settings().new Hotspot();
            //hotspots.setSceneId(String.valueOf(compteur));
            int i = Integer.parseInt(hotspot.getSceneId()) + 1;
            hotspots.setSceneId(Integer.toString(i));
            hotspots.setATV(hotspot.getATV());
            hotspots.setATH(hotspot.getATH());
            ChangeSettingsRequest.Settings.Hotspot.Actions.LoadScene loadScene = new ChangeSettingsRequest().new Settings().new Hotspot().new Actions().new LoadScene();
            loadScene.setSceneIds(Integer.toString(hotspot.getHotspotLeadTo()+1));
            ChangeSettingsRequest.Settings.Hotspot.Actions actions = new ChangeSettingsRequest().new Settings().new Hotspot().new Actions();
            actions.setLoadScene(loadScene);
            hotspots.setAction(actions);
            m_settings.Hotspots.add(hotspots);
        }
        /***************************************************************************************************************
         ** Tour Settings **********************************************************************************************/

        m_tourSettings = new TourSettings();

        m_tourSettings.Address = city.getText().toString() ;
        m_tourSettings.Area = Double.parseDouble(EditTourArea.getText().toString().isEmpty() ? "0" : EditTourArea.getText().toString()) ;
        m_tourSettings.TourName = EditTourDescription.getText().toString();
        m_tourSettings.EnergyClass = tour.getEnergyClass();
        m_tourSettings.GasClass = tour.getGasClass();
        m_tourSettings.IsHouse = tour.getHouse();
        m_tourSettings.Latitude = 0;
        m_tourSettings.Longitude = 0;
        m_tourSettings.Price = Double.parseDouble(EditTourPrice.getText().toString().isEmpty() ? "0" : EditTourPrice.getText().toString());
        m_tourSettings.PropertyType = tour.getType().replace("T","t0");
        m_tourSettings.SaleType = tour.getSaleType();

        Gson gson = new Gson();
        String json = gson.toJson(m_rooms);
        Log.d("Json"," #######" +json);

        Intent intentSavetour = new Intent(getApplicationContext(),ActivityCongrat.class);
        intentSavetour.putExtra("m_settings",m_settings);
        intentSavetour.putExtra("m_tourSettings",m_tourSettings);
        startActivity(intentSavetour);


    }

}


