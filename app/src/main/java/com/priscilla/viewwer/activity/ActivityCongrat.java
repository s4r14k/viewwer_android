package com.priscilla.viewwer.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.priscilla.viewwer.R;
import com.priscilla.viewwer.api.APIClient;
import com.priscilla.viewwer.api.APIInterface;
import com.priscilla.viewwer.api.ApiBaseResponseCallBack;
import com.priscilla.viewwer.api.ApiCallBack;
import com.priscilla.viewwer.api.ApiCallBackVisite;
import com.priscilla.viewwer.model.BaseResponse;
import com.priscilla.viewwer.model.ChangeSettingsRequest;
import com.priscilla.viewwer.model.HotSpot;
import com.priscilla.viewwer.model.Room;
import com.priscilla.viewwer.model.SceneModel;
import com.priscilla.viewwer.model.StartTaskRequest;
import com.priscilla.viewwer.model.TourBuildRequest;
import com.priscilla.viewwer.model.TourSettings;
import com.priscilla.viewwer.model.UpvoteModel;
import com.priscilla.viewwer.model.responseSearch;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.priscilla.viewwer.utils.App.getContext;
import static com.priscilla.viewwer.utils.Extensions.GetThumbName;
import static com.priscilla.viewwer.utils.Extensions.ToScenes;
import static com.priscilla.viewwer.utils.Extensions.ToThumbs;
import static com.priscilla.viewwer.utils.Extensions.deleteDirectory;

public class ActivityCongrat extends BaseActivity {
    private TourSettings m_tourSettings = new TourSettings();
    private ChangeSettingsRequest.Settings m_settings = new ChangeSettingsRequest().new Settings();
    private ArrayList<Room> m_rooms = new ArrayList<Room>();
    private int indexRoom = 0 ;

    private APIInterface _apiInterface;

    private TextView textViewSaveTour;

    private Toolbar toolbar;

    public String UploadURL;
    public String tourId;

    public String UploadApiKey;

    private Button buttonAddHotspot;

    private TextView textView1,textView_2,textView_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congrat);
        initView();
        setRooms();
        m_settings = (ChangeSettingsRequest.Settings) getIntent().getSerializableExtra("m_settings");
        m_tourSettings = (TourSettings) getIntent().getSerializableExtra("m_tourSettings");
        BuildTour(m_rooms,m_settings,m_tourSettings);
    }

    public void initView(){
        textView1=(TextView) findViewById(R.id.text1);
        textView1.setText("votre visite sera accessible dans votre profil d'ici dans quelques instants");
        textView_2=(TextView)findViewById(R.id.text_2);
        textView_2.setText("Elle sera accessible dans le fil de recherche après validation par nos équipes");
        textView_3=(TextView)findViewById(R.id.text_3);
        textView_3.setText("Veuillez patientez pendant la génération de votre visite");
        _apiInterface = APIClient.getinstance(getContext()).create(APIInterface.class);
    }


    public void setRooms(){
        byte[] b_panoFile= null, b_panoThumb;
        File imgFile = new  File(Environment.getExternalStorageDirectory() + File.separator +"viewwer/");
        if(imgFile.exists()) {
            File[] files = imgFile.listFiles();
            int index = 0;

            for (File file : files
            ) {
                index++;
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                b_panoFile = stream.toByteArray();
                myBitmap.recycle();

                b_panoThumb = ResizeImageAndroid(b_panoFile, 150, 250, 100);

                /*File photo=new File(Environment.getExternalStorageDirectory() + File.separator +"viewwer" + File.separator +"thumb_"+file.getName());
                try {
                    FileOutputStream fos=new FileOutputStream(photo.getPath());
                    fos.write(b_panoThumb);
                    Log.d("panothumb",photo.getPath());
                    fos.close();
                }
                catch (java.io.IOException e) {
                    Log.e("PictureDemo", "Exception in photoCallback", e);
                }*/
                //
                Room rooms = new Room();
                rooms.setId(index);
                rooms.setSceneTypeId("sceneType_" + index);
                rooms.setSceneName("salon");
                rooms.setRoomName("RoomName");
                if (index == 1) {
                    rooms.setSceneTypeName("salon");
                } else {
                    rooms.setSceneTypeName("salon" + index);
                }

                rooms.setPanoRamaFile(b_panoFile);
                rooms.setPanoRamaThumbFile(b_panoThumb);
                rooms.setPanoRamaFilePath(file.getPath());

                m_rooms.add(rooms);
            }
        }
    }

    public static byte[] ResizeImageAndroid(byte[] imageData, float width, float height, int quality)
    {
        // Load the bitmap
        Bitmap originalImage = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

        float oldWidth = (float)originalImage.getWidth();
        float oldHeight = (float)originalImage.getHeight();
        float scaleFactor = 0f;

        if (oldWidth > oldHeight)
        {
            scaleFactor = width / oldWidth;
        }
        else
        {
            scaleFactor = height / oldHeight;
        }

        float newHeight = oldHeight * scaleFactor;
        float newWidth = oldWidth * scaleFactor;

        Bitmap resizedImage = Bitmap.createScaledBitmap(originalImage, (int)newWidth, (int)newHeight, false);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        resizedImage.compress(Bitmap.CompressFormat.JPEG, quality, stream);

        return stream.toByteArray();

    }



    public  void BuildTour(ArrayList<Room> rooms, ChangeSettingsRequest.Settings settings, TourSettings tourSettings)
    {
        if (tourId == null)
        {
            StartTourTask(new ApiBaseResponseCallBack() {
                @Override
                public void onResponse(BaseResponse tourTaskResult) {
                    tourId = tourTaskResult.TaskId;
                    UploadURL = tourTaskResult.Url;
                    UploadApiKey = tourTaskResult.ApiKey;
                    Log.d("tourId2", tourId + "####### voici le tourtaskResult1");
                    //request DossierID
                    if (tourId != null)
                    {

                        UploadImages(rooms,tourId,new ApiBaseResponseCallBack() {
                            @Override
                            public void onResponse(BaseResponse tourTaskResult) {
                                if(tourTaskResult.getStatus().equals("finish")){
                                    StartBuildingTour(tourId, rooms,new ApiBaseResponseCallBack() {
                                        @Override
                                        public void onResponse(BaseResponse tourTaskResult) {
                                            if(tourTaskResult.getStatus().equals("success")){
                                                UploadHotspots(tourId, settings, new ApiBaseResponseCallBack() {
                                                    @Override
                                                    public void onResponse(BaseResponse result) {
                                                        Log.d(" ca entre ici","#####continuer");
                                                        SaveTour(tourId, tourSettings);
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }

                            }
                        });
                    }
                }
            });
        }
        Log.d("tourTaskResult", tourId + " voici le tourtaskResult2");

    }


    public  Boolean UploadImages(ArrayList<Room> rooms, String TourId, ApiBaseResponseCallBack callBack)
    // public  Boolean UploadImages(List<Room> rooms, String TourId)
    {
        List<Boolean> results = new ArrayList<>();
        BaseResponse resultLike = new BaseResponse();

        for (Room item : rooms)
        {
            PostImage(UploadURL,item.PanoRamaFile,item.PanoRamaThumbFile,item.PanoRamaFilePath,item.getPanoRamaThumbPath(),new ApiBaseResponseCallBack() {
                @Override
                public void onResponse(BaseResponse resultLike) {
                    indexRoom ++;
                    Log.d("sary"," " + rooms.size() + " sy "+ indexRoom);
                    if(rooms.size() == indexRoom){
                        resultLike.setStatus("finish");
                    }

                    callBack.onResponse(resultLike);
                }
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        return true;
    }

    public void PostImage(String url, byte[] image, byte[] thumbImage,String imgPath,String getThumbPath, ApiBaseResponseCallBack callback)
    //  public void PostImage(String url, byte[] image, String imgPath)
    {
        BaseResponse contentObject = new BaseResponse();
        String name = (new File(imgPath)).getName();

        String nameThumb = GetThumbName(getThumbPath);
        Log.d("vita2 nameThumb", "########"+ nameThumb);
        Log.d("it's work", "########"+ nameThumb);
        _apiInterface = APIClient.PostMultipart(getContext(), url).create(APIInterface.class);

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), image);
        RequestBody requestFile2 =
                RequestBody.create(MediaType.parse("multipart/form-data"), thumbImage);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", name, requestFile);
        MultipartBody.Part thumb =
                MultipartBody.Part.createFormData("thumb_image", nameThumb, requestFile2);

        Call<BaseResponse> call = _apiInterface.PostMultipart(UploadApiKey, body,thumb);
        Log.d("vita", "######");
        call.enqueue(new Callback<BaseResponse>() {

            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse res = response.body();
                callback.onResponse(res);
                Log.d("Upload ok", "########");

            }

            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.d("Upload failed","##########");
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



    public void StartTourTask(final ApiBaseResponseCallBack callback)
    {

        StartTaskRequest task = new StartTaskRequest();

        task.Type = "apptour";

        Call<BaseResponse> call = _apiInterface.StartTourTask(task);
        call.enqueue(new Callback<BaseResponse>() {

            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse res = response.body();
                Log.d("StartTourTask","============="+response.message()+ response.code());
                callback.onResponse(res);

            }
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    public void StartBuildingTour(String taskId, List<Room> rooms,  ApiBaseResponseCallBack callback)
    {
        Log.d("build","StartBuildingTour ato");

        List<SceneModel> scenes = ToScenes(rooms);

        List<SceneModel>  thumbs = ToThumbs(rooms);

        TourBuildRequest tours = new TourBuildRequest();

        tours.Preset = "build";
        tours.Scenes= scenes;
        tours.Thumbnails = thumbs;

        _apiInterface = APIClient.getinstance(getContext()).create(APIInterface.class);

        Call<BaseResponse> call = _apiInterface.StartBuildingTour(taskId,tours);
        //request DossierID
        call.enqueue(new Callback<BaseResponse>() {

            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse res = response.body();
                Log.d("buildTour","============="+response.message()+ response.code());

                callback.onResponse(res);

            }
            public void onFailure(Call<BaseResponse> call, Throwable t) {


                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void UploadHotspots(String tourId,ChangeSettingsRequest.Settings settings, ApiBaseResponseCallBack callback){
        Log.d("UploadHotspots","######## continuer");
        ChangeSettingsRequest tours = new ChangeSettingsRequest();
        tours.Preset = "settings";
        tours.Settings = settings;

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Call<BaseResponse> call = _apiInterface.UploadHotspots(tourId,tours);
                        Log.d("Lancement","######## ");
                        //request DossierID
                        call.enqueue(new Callback<BaseResponse>() {
                            @Override
                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                BaseResponse res = response.body();
                                Log.d("UploadHotspots fait","#####continuer");
                                callback.onResponse(res);

                            }
                            public void onFailure(Call<BaseResponse> call, Throwable t) {
                                Log.d("UploadHotspots failed","##########");
                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                },
                30000);


    }

    public void SaveTour(String tourId,TourSettings toursettings) {
        CreateSaveTourTask(new ApiBaseResponseCallBack() {
            @Override
            public void onResponse(BaseResponse result) {
                String saveTaskId = result.TaskId;


                if (saveTaskId != "") {
                    toursettings.TourTaskId = tourId;
                    Log.d("tourSaveId", result.TaskId + "tourId" + toursettings.TourTaskId);

                    Call<BaseResponse> call = _apiInterface.SaveTour(saveTaskId, toursettings);
                    //request DossierID
                    call.enqueue(new Callback<BaseResponse>() {

                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            Log.d("SaveTour", "=============" + response.message() + response.code());

                            BaseResponse res = response.body();

                            (new Handler()).postDelayed(
                                    new Runnable() {
                                        public void run() {
                                            Call<ArrayList<responseSearch>> call = _apiInterface.GetMyTours();

                                            //request DossierID
                                            call.enqueue(new Callback<ArrayList<responseSearch>>() {

                                                @Override
                                                public void onResponse(Call<ArrayList<responseSearch>> call, Response<ArrayList<responseSearch>> response) {
                                                    ArrayList<responseSearch> objectResp = response.body();
                                                    deleteDirectory();
                                                    String WebViewUrl = BaseActivity.URL_API_SERVER +"/apptours/"+objectResp.get(0).getEntityId();
                                                    Log.d("WebViewUrl", "########## " +WebViewUrl);
                                                    Intent intent = new Intent(getApplicationContext(), WebViewVisitDetailActivity.class);
                                                    Bundle b = new Bundle();
                                                    b.putString("key", WebViewUrl);
                                                    b.putString("finishCreate", "true");//Your id
                                                    intent.putExtras(b); //Put your id to your next Intent
                                                    startActivity(intent);
                                                    finish();


                                                }
                                                public void onFailure(Call<ArrayList<responseSearch>> call, Throwable t) {
                                                    Log.d("Response code:", "=====" + t.getMessage());
                                                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    },
                                    20000);
                            Toast.makeText(getApplicationContext(), "Enregistrement terminer, felicitation", Toast.LENGTH_SHORT).show();

                        }

                        public void onFailure(Call<BaseResponse> call, Throwable t) {

                            Log.d("SaveTour failed", "=============");
                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        });
    }

    public void CreateSaveTourTask(final ApiBaseResponseCallBack callback)
    {
        StartTaskRequest task = new StartTaskRequest();

        task.Type = "apptour-save";
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Call<BaseResponse> call = _apiInterface.CreateSaveTourTask(task);
                        call.enqueue(new Callback<BaseResponse>() {

                            @Override
                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                BaseResponse res = response.body();
                                Log.d("CreateSaveTourTask","============="+response.message()+ response.code());
                                callback.onResponse(res);

                            }
                            public void onFailure(Call<BaseResponse> call, Throwable t) {
                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                },
                10000);

    }
}
