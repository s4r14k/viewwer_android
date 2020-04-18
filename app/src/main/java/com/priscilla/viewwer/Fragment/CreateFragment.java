package com.priscilla.viewwer.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import pl.pawelkleczkowski.customgauge.CustomGauge;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.camerakit.CameraKitView;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.priscilla.viewwer.MainActivity;
import com.priscilla.viewwer.R;
import com.priscilla.viewwer.activity.BallView;
import com.priscilla.viewwer.activity.DegreeView;
import com.priscilla.viewwer.activity.PanoramaActivity;
import com.priscilla.viewwer.activity.PanoramaWebViewActivity;
import com.priscilla.viewwer.api.APIClient;
import com.priscilla.viewwer.api.APIInterface;
import com.priscilla.viewwer.api.ApiBaseResponseCallBack;
import com.priscilla.viewwer.model.BaseResponse;
import com.priscilla.viewwer.model.ChangeSettingsRequest;
import com.priscilla.viewwer.model.HotSpot;
import com.priscilla.viewwer.model.Room;

import com.priscilla.viewwer.model.SceneModel;
import com.priscilla.viewwer.model.StartTaskRequest;
import com.priscilla.viewwer.model.StitchedImage;
import com.priscilla.viewwer.model.TourBuildRequest;
import com.priscilla.viewwer.model.TourSettings;
import com.priscilla.viewwer.utils.MyDebug;
import com.priscilla.viewwer.utils.NativePanorama;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.priscilla.viewwer.utils.Extensions.ToScenes;
import static com.priscilla.viewwer.utils.Extensions.ToThumbs;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateFragment} interface
 * to handle interaction events.
 * Use the {@link CreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

@SuppressWarnings("ALL")
public class CreateFragment extends Fragment implements SensorEventListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    static {
        System.loadLibrary("native_lib");
    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public String UploadURL;
    public String tourId=null;

    public String UploadApiKey;

    private Button btnSendBo;
    private APIInterface _apiInterface;
    private static final String TAG = "MainActivity";
    private CameraKitView cameraKitView;
    // private GyroSensor gyroSensor;
    private BallView ballView;
    DegreeView mDegreeView = null;

    float[] acceleromterVector = new float[3];
    float[] magneticVector = new float[3];
    float[] resultMatrix = new float[9];
    float[] values = new float[3];

    private FrameLayout relativeLayout;

    private float initial_x = 110.0f;
    private float initial_y = 212.0f;

    Timer mTmr = null;
    TimerTask mTsk = null;
    Handler RedrawHandler = new Handler();

    private float[] mGyroData = new float[3];
    private float[] gravity = new float[3];
    private float[] linear_acceleration = new float[3];

    private SensorManager sensorManager;
    private OrientationEventListener orientationEventListener;

    private Sensor accelerometer;
    private Sensor magnetic;
    private Sensor orientation;

    private Sensor gyroscopeSensor;
    private Sensor rotationVectorSensor;
    private Sensor accelerometerSensor;
    private SensorEventListener gyroscopeEventListener;
    private SensorEventListener rotationVectorEventListener;
    private SensorEventListener acceleroemterEventListener;

    private final float[] rotationMatrix = new float[9];
    //    private MatVector imgs = new MatVector();
    private List<Bitmap> bitmapImgs = new ArrayList<Bitmap>();
    private long ti;
    private float angle = 0;
    private long somme = 0;
    private ProgressDialog ringProgressDialog;
    ProgressBar loading;

    private int current_orientation = 0;
    private boolean view_rotate_animation;

    TextView textView;
    TextView beginView;
    private long lastUpdate;
    private boolean color = false;

    private float azimuth;
    private float pitch;
    private float roll;

    private int nombre_photo = 0;
    private boolean isMultipleOf15 = false;
    private boolean takeone = true;
    private int quinze = 15;
    private static int pic_number_to_take = 4;
    private int clicked_once = 0;

    public static int x, y;
    public int x_init=70;
    public int y_init=60;
    private static int x_final=0, y_final=0, dx=0, dy=0;
    private TextView txtlist, xValue, yValue, zValue, notif, pitch_value_inside;

    private ImageView spot_left, spot_right;
    private Button startButton, stitchingButton;
    private int direction = 1;
    private double starting_pitch;
    private boolean started_pitch;

    // Status
    private boolean can_start = false, started = false;
    private TextView can_start_view;
    private boolean can_take_picture = false;
    private boolean taking_picture = false;

    /*** Debut Variables Integration design ***/
    private int counter = 1;
    private boolean isOk = false;

    private Handler mHandler = new Handler();

    android.graphics.PointF mBallPos, mBallSpd;
    int mScrWidth, mScrHeight;
    private float xMax, yMax, mBallPosZ, mBallSpdZ;

    private float rollV, pitchV;
    private int valueOfPitch = 0;

    Mat mRGBA, mRGBAT;

    private CustomGauge gauge;// Declare this variable in your activity
    private int circleProgess = 4;
    private ImageView patern_image;

    private float final_y = 100 + 82/2 - 5;
    private float final_x = 60 + 435;
    private int progression = 0;

    AlertDialog mainDialog;
    private TextureView textureView;

    private String priseVue;

    File myDir;
    File file;

    private List<Mat> listImage = new ArrayList<>();
    List<Bitmap> bmList = new ArrayList<>();

    /**
     * Android Hardware
     */
    private String cameraId;
    CameraDevice cameraDevice;
    CameraManager cameraManager;
    CameraCaptureSession cameraCaptureSession;
    CaptureRequest captureRequest;
    CaptureRequest.Builder captureRequestBuilder;

    private Size imageDimension;
    private ImageReader imageReader;

    Handler mBackgroundHandler;
    HandlerThread mBackgroundThread;

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    /**
     * COnfiguration d'orientation
     */
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private int imageDispo = -1; // index in list of the image that is available (using onPicture)
    private int imageWaitedFor = 1; // image waited for the Async Stitching Task
    private Mat finalResult; // the final result that will store the stitched panorama
    private int start_pitch_count = 5;

    private static final int ANGLE = 90;

    private BaseLoaderCallback mLoaderCallBack = new BaseLoaderCallback(getContext()) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                } break;
                default:
                {
                    super.onManagerConnected(status);
                }break;
            }
        }
    };

    private final int HANDLER_START_STITCHING = 1002;
    private final int HANDLER_SET_STITCHING_BUTTON_TEXT = 1003;
    private final double STITCH_IMAGE_SCALE = 0.5;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case HANDLER_START_STITCHING :
                {
                    new Thread()
                    {
                        public void run()
                        {
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    String[] source=getDirectoryFilelist();
                                    final String stitchingResultImagePath = new File(getApplicationContext().getExternalFilesDir(null).getAbsolutePath()) +"/result.jpg";
                                    if(NativePanorama.jniStitching(source, stitchingResultImagePath, STITCH_IMAGE_SCALE)==0)
                                    {
                                        handler.sendMessage(handler.obtainMessage(HANDLER_SET_STITCHING_BUTTON_TEXT,"Stitching success"));
                                    }
                                    else
                                    {
                                        handler.sendMessage(handler.obtainMessage(HANDLER_SET_STITCHING_BUTTON_TEXT,"Stitching error"));
                                    }
                                }
                            });
                        }
                    }.start();
                    break;
                }
                default: {
                    break;
                }
            }
            return false;
        }
    });

    private String[] getDirectoryFilelist() {
        String[] filelist;
        File sourceDirectory = new File(getApplicationContext().getExternalFilesDir(null).getAbsolutePath());
        int index=0;
        int folderCount=0;
        //except folders
        for(File file : sourceDirectory.listFiles())
        {
            if(file.isDirectory())
            {
                folderCount++;
            }
        }
        filelist=new String[sourceDirectory.listFiles().length-folderCount];
        for(File file : sourceDirectory.listFiles())
        {
            if(!file.isDirectory())
            {
                //showLOG("getFilelist file:"+file.getPath());
                filelist[index]=file.getPath();
                index++;
            }
        }
        return filelist;
    }


    public CreateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateFragment newInstance(String param1, String param2) {
        CreateFragment fragment = new CreateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("params");
            Toast.makeText(getContext(),mParam1,Toast.LENGTH_SHORT).show();
        }
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create, container, false);

//        Intent intent = new Intent(rootView.getContext(), CreateActivity.class);
//        startActivity(intent);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // gyroSensor = new GyroSensor(this);
        ballView = new BallView(getContext(),initial_x, initial_y, 1.0f);
        mDegreeView = new DegreeView(getContext(), "0", initial_x,initial_y, mBallPosZ,5);

        relativeLayout = (FrameLayout) rootView.findViewById(R.id.relativelayout);
        textView = (TextView) rootView.findViewById(R.id.textView);
        beginView = (TextView) rootView.findViewById(R.id.beginView);
        textureView = (TextureView) rootView.findViewById(R.id.textureView);

        gauge = rootView.findViewById(R.id.gauge2);
        listImage = new ArrayList<>();


        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // mGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        magnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        lastUpdate = System.currentTimeMillis();

        orientationEventListener = new OrientationEventListener(getContext()) {
            @Override
            public void onOrientationChanged(int orientation) {
                onOrientationChangedThis(orientation);
            }
        };

        gyroscopeEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float axisX = event.values[0]; // Pitch value
                float axisY = event.values[1]; // roll acceleration value in rad/s
                float axisZ = event.values[2];

                long diff = Calendar.getInstance().getTimeInMillis() - ti;

                if(started){
                    roll += (axisY * diff) / 1000;
                }
                pitch += (axisX * diff) / 1000;

                ti = Calendar.getInstance().getTimeInMillis(); // get the initial time

                // Doing the calculations
                dx = (int) ((final_x - initial_x)/15);
                Log.i("roll", String.valueOf(Math.toDegrees(roll)));
                if(! taking_picture){
                    if(started){
                        ballView.mX = initial_x + (float) Math.toDegrees(roll) * (-dx);
                        mDegreeView.mX = initial_x + (float) Math.toDegrees(roll) * (-dx);

                    }

                    if(Math.abs(Math.abs(Math.toDegrees(roll)) - 15) < 0.3 && Math.abs(Math.toDegrees(pitch)) < 3){
                        if(progression == 360){
                            // showProcessingDialog("Capturing and stitching...");
                            // started = false;
                            resetView();
                        }else{
                            //showProcessingDialog("Capturing...");

                            try {
                                takePictureEssai();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            resetView();
                            // progression += 15;
                            // gauge.setValue(progression);
                            nombre_photo += 1;
                        }
                        // taking_picture = true;
                    }

                    ballView.mY = initial_y - (float) Math.toDegrees(pitch) * 5;
                    mDegreeView.mY = initial_y - (float) Math.toDegrees(pitch) * 5;
                    valueOfPitch = (int) Math.toDegrees(pitch);
                    mDegreeView.mText = Integer.toString(valueOfPitch);

                    if(valueOfPitch >= -15 && valueOfPitch < 17) {
                        textView.setVisibility(View.GONE);
                    } else {
                        textView.setVisibility(View.VISIBLE);
                    }

                    if(valueOfPitch >= -5 && valueOfPitch < 5 && nombre_photo == 0) {
                        beginView.setVisibility(View.VISIBLE);

                        beginView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                beginView.setVisibility(View.GONE);

                                try {
                                    takePictureEssai();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }

                                started = true;
                            }
                        });

                    } else {
                        beginView.setVisibility(View.GONE);
                    }
                }

                if(!started && can_start && (int) Math.toDegrees(pitch) == 0){
                    // showProcessingDialog("Capturing...");
                    started = true;
                    taking_picture = true;
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        // Rotation vector listener
        rotationVectorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                float [] result = new float[9];
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
                SensorManager.getOrientation(rotationMatrix, result);

                if(!started_pitch){
                    pitch = (float) (-Math.PI/2 - result[1]);
                    started_pitch = true;
                }

//                mBallView.mY = patern_image.getY() + 82/2 - 5; // center the ball
//                mBallView.mX = 60 + 435;
                // Log.i("POSY", String.valueOf(patern_image.getY()));
                // Log.i("POSX", String.valueOf(patern_image.getX()));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        return rootView;
    }

    public void resetView() {
        ballView.mX = initial_x;
        taking_picture = false;
        roll = 0;
    }

    public void onOrientationChangedThis(int orientation) {

        if( MyDebug.LOG ) {
            Log.d("TAGCUR", "onOrientationChanged()");
            Log.d("TAGCUR", "orientation: " + orientation);
            Log.d("TAGCUR", "current_orientation: " + current_orientation);
        }

        if( orientation == OrientationEventListener.ORIENTATION_UNKNOWN ) {
            Log.d("TAGCUR", "current_orientation is off " );
            return;
        }

        int diff = Math.abs(orientation - current_orientation);

        if( diff > 180)  {
            diff = 360 - diff;
        }

        Log.d("TAGCUR", "Diff : " + diff);

        // only change orientation when sufficiently changed
        if( diff > 60 ) {
            orientation = (orientation + 45) / 90 * 90;
            orientation = orientation % 360;

            Log.d("TAGCUR", "The orientation" + orientation);
            Log.d("TAGCUR", "The current orientation" + current_orientation);

            if( orientation != current_orientation ) {
                current_orientation = orientation;
                if( MyDebug.LOG ) {
                    Log.d("TAGCUR", "current_orientation is now: " + current_orientation);
                }
                view_rotate_animation = true;

                Log.d("TAGCUR", "The rotation  ");

                int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();

                Log.d("TAGCUR", "The rotation  " + rotation);

                int degrees = 0;
                switch (rotation) {
                    case Surface.ROTATION_0: degrees = 0; break;
                    case Surface.ROTATION_90: degrees = 90; break;
                    case Surface.ROTATION_180: degrees = 180; break;
                    case Surface.ROTATION_270: degrees = 270; break;
                    default:
                        break;
                }
                // getRotation is anti-clockwise, but current_orientation is clockwise, so we add rather than subtract
                // relative_orientation is clockwise from landscape-left
                //int relative_orientation = (current_orientation + 360 - degrees) % 360;
                int relative_orientation = (current_orientation + degrees) % 360;
                if( MyDebug.LOG ) {
                    Log.d("TAGCUR", "    current_orientation = " + current_orientation);
                    Log.d("TAGCUR", "    degrees = " + degrees);
                    Log.d("TAGCUR", "    relative_orientation = " + relative_orientation);
                }

                final int ui_rotation = (360 - relative_orientation) % 360;

                view_rotate_animation = false;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Prise de vue")
                .setMessage("Allez dans la première pièce et placez-vous au centre de la pièce.")
                .setCancelable(false)
                .setPositiveButton("J'ai compris", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            takePicture();
                        } catch (CameraAccessException | IOException e) {
                            e.printStackTrace();
                        }

                        final String[] testItem = {"Buanderie", "Bureau", "Chambre 2", "Chambre 3", "Chambre 4", "Chambre Principale", "Couloir", "Couloir 2", "Cuisine", "Dressing", "Entrée", "Salle de bain", "Salle de bain 2", "Salon", "Terrasse"};
                        AlertDialog.Builder items = new AlertDialog.Builder(getContext());
                        items.setTitle("Dans quelle pièce êtes-vous")
                                .setItems(testItem, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // The 'which' argument contains the index position
                                        // of the selected item
                                        AlertDialog.Builder prise = new AlertDialog.Builder(getContext());
                                        priseVue = testItem[which];
                                        prise.setTitle("Prise de vue " + testItem[which])
                                                .setMessage("Tenez votre smartphone à deux mains et à la verticale. Faite un tour complet sur vous-même pour réaliser le scan de la pièce")
                                                .setCancelable(false)
                                                .setPositiveButton("Ok, je démarre le scan", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                });

                                        AlertDialog showPrise = prise.create();
                                        showPrise.show();
                                    }

                                });
                        AlertDialog showItem = items.create();
                        showItem.show();
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent panoramaIntent = new Intent(getContext(), PanoramaActivity.class);
                        startActivity(panoramaIntent);
                    }
                });
        //Creating dialog box
        mainDialog  = builder.create();
        mainDialog.show();
    }

    @Override
    public void onResume() {

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetic, SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.registerListener(rotationVectorEventListener, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(gyroscopeEventListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_GAME);
        orientationEventListener.enable();

        super.onResume();
        initGyroSensors();

        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, getContext(), mLoaderCallBack);
        } else {
            Log.d("OpenCV", "OpenCV library not found inside package");
            mLoaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

        startBackgroundThread();

        Log.d("textureView", "textureView : " + textureView.isAvailable());

        if (textureView.isAvailable()) {
            try {
                openCamera();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
        // gyroSensor.startRecording();

        // final boolean isRecord = gyroSensor.isRecording();

        // Log.d("Gyrosensor", "is recordin main = " + isRecord);

        relativeLayout.addView(ballView);
        relativeLayout.addView(mDegreeView);

        mTmr = new Timer();
        mTsk = new TimerTask() {
            public void run() {

                if (ballView.mY <= 0f) ballView.mY = 0f;
                if (ballView.mX <= 0f) ballView.mX = 0f;
                if (mDegreeView.mY <= 0f) mDegreeView.mY = 0f;
                if (mDegreeView.mX <= 0f) mDegreeView.mX = 0f;
                if (ballView.mY >= 512.0f) ballView.mY = 512.0f;

                //redraw ball. Must run in background thread to prevent thread lock.
                RedrawHandler.post(new Runnable() {
                    public void run() {
                        ballView.invalidate();
                        mDegreeView.invalidate();
                    }});
            }}; // TimerTask

        mTmr.schedule(mTsk,10,10); //start timer

        ti = Calendar.getInstance().getTimeInMillis();

        /*if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 101);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        }*/


    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    @Override
    public void onPause() {
        orientationEventListener.disable();
        super.onPause();

        try {
            stopBackgroundThread();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void initGyroSensors() {
        // gyroSensor.enableSensors();
    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        Double angle = Math.atan2(x,y) / (Math.PI/180);

        Log.d("ANGLE", "angle : " + angle);

        float accelationSquareRoot = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = event.timestamp;

        Log.d("TAGCUR", "acccelation : " + accelationSquareRoot);

        if (accelationSquareRoot >= 2) //
        {
            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;
            Toast.makeText(getContext(), "Device was shuffed", Toast.LENGTH_SHORT).show();
            if (color) {
                textView.setBackgroundColor(Color.GREEN);
            } else {
                textView.setBackgroundColor(Color.RED);
            }
            color = !color;
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            acceleromterVector=event.values;
            getAccelerometer(event);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticVector=event.values;
        }

        // Demander au sensorManager la matric de Rotation (resultMatric)
        SensorManager.getRotationMatrix(resultMatrix, null, acceleromterVector, magneticVector);

        // Demander au SensorManager le vecteur d'orientation associé (values)
        SensorManager.getOrientation(resultMatrix, values);

        final float alpha = 0.8f;

        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];


        float roll = linear_acceleration[0];
        float pitch = linear_acceleration[1];
        float yaw = linear_acceleration[2];

        Double angle = Math.atan2(roll,pitch) / (Math.PI/180);

        Log.d("ANGLER", "angle : " + angle);
    }

    private List<Mat> prepareData(List<Bitmap> bitmap) {
        // convert the captured Bitmap into an OpenCV Mat and store in this listImage list
        Mat mat = new Mat();
        for (Bitmap bm : bitmap) {
            Utils.bitmapToMat(bm, mat);
            listImage.add(mat);
        }
        return listImage;
    }

    private void addFileToBitmapList(byte[] data) throws IOException {
        // Decode byte array to Bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        // Rotate the picture to fit portrait mode
        Matrix matrix = new Matrix();
        //matrix.postRotate(90);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);

        // OpenCV
        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap, mat);
        listImage.add(mat);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(getApplicationContext(), "Permission de la Camera Nécessaire", Toast.LENGTH_LONG).show();
            }
        }
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
            try {
                openCamera();
            } catch (CameraAccessException e) {
                Log.e("opencam", "ERROR");
                e.printStackTrace();
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };

    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            cameraDevice = camera;
            try {
                createCameraPreview();
            } catch (CameraAccessException e) {
                e.printStackTrace();
                Log.e("opencam", "ERROR");
            }
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(CameraDevice camera, int i) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    private Range<Integer> getRange() {
        CameraCharacteristics chars = null;
        try {
            chars = cameraManager.getCameraCharacteristics(cameraId);
            Range<Integer>[] ranges = chars.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
            Range<Integer> result = null;
            for (Range<Integer> range : ranges) {
                int upper = range.getUpper();
                if (upper >= 10) {
                    if (result == null || upper < result.getUpper().intValue()) {
                        result = range;
                    }
                }
            }
            if (result == null) {
                result = ranges[0];
            }
            return result;
        } catch (CameraAccessException e) {
            e.printStackTrace();
            return null;
        }
    };

    private void createCameraPreview() throws CameraAccessException {
        SurfaceTexture texture = textureView.getSurfaceTexture();
        texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());

        Surface surface = new Surface(texture);
        captureRequestBuilder = cameraDevice.createCaptureRequest(cameraDevice.TEMPLATE_PREVIEW);
        captureRequestBuilder.addTarget(surface);

        captureRequestBuilder.set(CaptureRequest.COLOR_CORRECTION_ABERRATION_MODE, CameraMetadata.COLOR_CORRECTION_ABERRATION_MODE_HIGH_QUALITY);

        // captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);

        if(nombre_photo > 0) {
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_LOCK, true);
        }

        cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(CameraCaptureSession session) {
                if (cameraDevice == null) {
                    return;
                }
                // captureRequestBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, getRange());
                cameraCaptureSession = session;
                try {
                    updatePreview();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConfigureFailed(CameraCaptureSession session) {
                Toast.makeText(getApplicationContext(), "Configuration changed", Toast.LENGTH_LONG).show();
            }
        }, null);
    }

    private void updatePreview() throws CameraAccessException {
        if (cameraDevice == null) return;

        cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
    }

    private void openCamera() throws CameraAccessException {
        CameraManager cameraManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        // 0 for rear camera
        cameraId = cameraManager.getCameraIdList()[0];
        CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
        StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

        // Request some capabilities
        Integer supported_hardware_level = cameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
        Log.i("Characteristics", "Hardware level: " + Integer.valueOf(supported_hardware_level));

//        Range exposure_time_range = cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE);
//        Log.i("Characteristics", "Exposure time range: " + exposure_time_range.toString());

        imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        }
        cameraManager.openCamera(cameraId, stateCallback, null);
    }

    private void takePicture() throws CameraAccessException, IOException {
        if (cameraDevice == null) {
            return;
        }
        Log.i("MyApp", "Picture: taken");
        CameraManager manager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
        Size[] jpegSizes = null;

        jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);


        int width = 640;
        int height = 480;

        if (jpegSizes != null && jpegSizes.length > 0) {
            width = jpegSizes[0].getWidth();
            height = jpegSizes[0].getHeight();
        }

        ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
        List<Surface> outputSurfaces = new ArrayList<>(2);
        outputSurfaces.add(reader.getSurface());

        outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));

        final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
        captureBuilder.addTarget(reader.getSurface());
        captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_OFF);
        captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_OFF);
        captureRequestBuilder.set(CaptureRequest.SENSOR_EXPOSURE_TIME,Long.valueOf("22000000"));
        captureRequestBuilder.set(CaptureRequest.SENSOR_SENSITIVITY,200);
        captureRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, CameraMetadata.CONTROL_AE_PRECAPTURE_TRIGGER_CANCEL);

        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

        // timestamp
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        // Fichier et dossier
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/Viewwer");

        if(!directory.exists())
            directory.mkdir();

        file = new File(Environment.getExternalStorageDirectory() + "/Viewwer/pano_" + ts + ".jpg");

        Log.i("MyApp", sdCard.getAbsolutePath());
        ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {

            }
        };
        reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);

        final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
            @Override
            public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                super.onCaptureCompleted(session, request, result);
                //Toast.makeText(getApplicationContext(), "Image " + nombre_photo + "/" + pic_number_to_take + " Sauvé", Toast.LENGTH_SHORT).show();
                try {
                    createCameraPreview();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        };

        cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(CameraCaptureSession session) {
                try {
                    captureRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, CameraMetadata.CONTROL_AE_PRECAPTURE_TRIGGER_CANCEL);
                    session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConfigureFailed(CameraCaptureSession session) {

            }
        }, mBackgroundHandler);

    }

    protected void stopBackgroundThread() throws InterruptedException {
        mBackgroundThread.quitSafely();
        mBackgroundThread.join();
        mBackgroundThread = null;
    }



    // Fonctions helpers
    public void convertToDegrees(float[] vector){
        for (int i=0; i<vector.length; i++) vector[i] = Math.round(Math.toDegrees(vector[i]));
    }

    private void showProcessingDialog(String m) {
//        cam.stopPreview();
//        Context c = getApplicationContext();
        ringProgressDialog = ProgressDialog.show(getActivity(), "", m, true);
        ringProgressDialog.setCancelable(false);
    }

    private void closeProcessingDialog() {
        ringProgressDialog.dismiss();
    }


    // Asynchronous task

    class MyAsyncTask extends AsyncTask<Void, Integer, String> {

        Integer count = 0;
        @Override
        protected String doInBackground(Void... voids) {
            finalResult = new Mat();
            while(imageWaitedFor < (int)(ANGLE/15) + 1){
                if(imageDispo >= imageWaitedFor){ // if the image waited for is already available
                    if(imageWaitedFor == 1){
                        long[] temp = new long[2];
                        temp[0] = listImage.get(0).getNativeObjAddr();
                        temp[1] = listImage.get(1).getNativeObjAddr();
                        Log.i("AsyncStitch", "Stitching image 0 with image 1");
                        NativePanorama.processPanorama(temp, finalResult.getNativeObjAddr());
                        Log.i("AsyncStitch", "Completed");
                    }else{
                        long[] temp = new long[2];
                        temp[0] = finalResult.getNativeObjAddr();
                        temp[1] = listImage.get(imageWaitedFor).getNativeObjAddr();
                        Log.i("AsyncStitch", "Stitching result with image " + Integer.valueOf(imageWaitedFor));
                        NativePanorama.processPanorama(temp, finalResult.getNativeObjAddr());
                        Log.i("AsyncStitch", "Completed");
                    }
                    imageWaitedFor++;
                }
            }

            Context context = getApplicationContext();
            String p = context.getExternalFilesDir(null).getAbsolutePath();
            Log.i("AsyncStitch", "filepath: " + p);


            final String fileName = p+ "/stitchAsync.jpg";

            Imgcodecs.imwrite(fileName, finalResult);

            listImage.clear();

            return ("Return object");
        }
        @Override
        protected void onProgressUpdate(Integer... diff) {

        }

        // Surcharge de la méthode onPostExecute (s'exécute dans la Thread de l'IHM)
        @Override
        protected void onPostExecute(String message) {
            // Mettre à jour l'IHM
//            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            Log.i("AsyncThread", "Async task finished");
            closeProcessingDialog();
        }
    }

    private void takePictureEssai() throws FileNotFoundException {
//        showProcessingDialog("Capturing...");
        Bitmap image = textureView.getBitmap();
        String filename = getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/fetsy" + listImage.size() + ".jpg";

        Log.i("info_file", filename);

        FileOutputStream out = new FileOutputStream((filename));

        Log.i("MyApp", "Took picture");

        Context context = getActivity().getBaseContext();
        String p = context.getExternalFilesDir(null).getAbsolutePath();

        // Try to get half of the image
        //Bitmap image2 = Bitmap.createBitmap(image, 0,0,10,image.getHeight());
        //image2.compress(Bitmap.CompressFormat.JPEG, 100, out);

        image.compress(Bitmap.CompressFormat.JPEG, 100, out);

        Mat result = new Mat();
        Utils.bitmapToMat(image, result);
        Imgproc.cvtColor(result, result, Imgproc.COLOR_BGR2RGB);

        listImage.add(result);

        imageDispo = listImage.size() - 1;

        Log.i("IMAGESIZE", Integer.toString(listImage.size()));

        taking_picture = false; // finished
        roll = 0;

        progression += 15;
        gauge.setValue(progression);

        if (progression == 360) {
            handler.sendMessage(handler.obtainMessage(HANDLER_START_STITCHING, "Start stitching"));
        }

        if(progression == ANGLE) {
            // stitch();
            // resetView();
            // ballView.mY = initial_y;
            // started = false;
            // can_start = false;
        }else{
            // closeProcessingDialog();
        }

        if(started){
            // Toast.makeText(getApplicationContext(), "Taken picture " + Integer.toString(listImage.size()), Toast.LENGTH_SHORT).show();
        }
    }

    private void save(byte[] bytes) throws IOException {
        OutputStream outputStream = null;
        outputStream = new FileOutputStream(file);
        outputStream.write(bytes);
        outputStream.close();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    private void initViews(View view) {
        // retrofit
        _apiInterface = APIClient.getinstance(getContext()).create(APIInterface.class);
    }

    public void stitch(){
        Context context = getActivity().getBaseContext();
        String p = context.getExternalFilesDir(null).getAbsolutePath();
//        p = "/storage/sdcard0/a_stitching";
        // OpenCV
        try {
            // Create a long array to store all image address
            int elems = listImage.size();
            long[] tempobjadr = new long[elems];
            Log.i("LEN", Integer.toString(tempobjadr.length));

            for (int i = 0; i < elems; i++) {
                tempobjadr[i] = listImage.get(i).getNativeObjAddr();
            }
            // Create a Mat to store the final panorama image
            Mat result = new Mat();
            // Call the OpenCV C++ Code to perform stitching process
            NativePanorama.processPanorama(tempobjadr, result.getNativeObjAddr());

            NativePanorama.crop(result.getNativeObjAddr());

            // Second approach
//            int elems = listImage.size();
//            long[] tempobjadr = new long[elems];
//            Log.i("LEN", Integer.toString(tempobjadr.length));
//
//            long[] toBeStitched = new long[2];
//            toBeStitched[0] = listImage.get(0).getNativeObjAddr();
//            toBeStitched[1] = listImage.get(1).getNativeObjAddr();
//
//            Mat result = new Mat();
//            NativePanorama.processPanorama(toBeStitched, result.getNativeObjAddr());
//
//            for (int i = 2; i < elems; i++) {
//                toBeStitched[0] = result.getNativeObjAddr();
//                toBeStitched[1] = listImage.get(i).getNativeObjAddr();
//                NativePanorama.processPanorama(toBeStitched, result.getNativeObjAddr());
//            }

//            // Trying with 6 by 6
//            // Here we have a group of 2 by 2
//            // If we have 24 images, we must set it to be n_groups = 4 and n_in_group = 6
//            int n_groups = 2;
//            int n_in_group = 6;
//
//            long[][] allTempObjAdr = new long[n_groups][n_in_group];
//            Mat[] results = new Mat[n_groups];
//            long[] resultsAdr = new long[n_groups];
//
//            Log.i("ADR", Arrays.toString(tempobjadr));
//            for (int i = 0; i < n_groups; i++) {
//
//                for (int j = 0; j < n_in_group; j++) {
//                    Log.i("ADR", "a loop");
//                    allTempObjAdr[i][j] = tempobjadr[n_in_group*i + j];
//                }
//
//            }
//
//            // Initializing results
//            for (int i = 0; i < results.length; i++) {
//                results[i] = new Mat();
//            }
//
//            Log.i("ADR", "outside");
//            for(int k=0; k < n_groups; k++){
//                Log.i("ADR", "iteration");
//                NativePanorama.processPanorama(allTempObjAdr[k], results[k].getNativeObjAddr());
//            }
//
//            // Stitching all groups
//            for (int i = 0; i < n_groups; i++) {
//                resultsAdr[i] = results[i].getNativeObjAddr();
//            }
//
//            Mat result = new Mat();
//            NativePanorama.processPanorama(resultsAdr, result.getNativeObjAddr());

            // Not changing
            final String fileName = p+ "/stitch.jpg";

//            final String fileName = "";
            Imgcodecs.imwrite(fileName, result);

            listImage.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // closeProcessingDialog();
    }

}
