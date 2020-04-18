package com.priscilla.viewwer.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.camerakit.CameraKitView;
import com.priscilla.viewwer.R;
import com.priscilla.viewwer.utils.MyDebug;

import java.util.Timer;
import java.util.TimerTask;

public class CreateActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "MainActivity";
    private CameraKitView cameraKitView;
    // private GyroSensor gyroSensor;
    private BallView ballView;

    float[] acceleromterVector = new float[3];
    float[] magneticVector = new float[3];
    float[] resultMatrix = new float[9];
    float[] values = new float[3];

    private RelativeLayout relativeLayout;

    private float initial_x = 110.0f;
    private float initial_y = 2.0f;

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

    private int current_orientation = 0;
    private boolean view_rotate_animation;

    TextView textView;
    private long lastUpdate;
    private boolean color = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // gyroSensor = new GyroSensor(this);
        ballView = new BallView(this,initial_x, initial_y, 1.0f);

        cameraKitView = findViewById(R.id.camera);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout);
        textView = (TextView) findViewById(R.id.textView);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // mGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        magnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        lastUpdate = System.currentTimeMillis();

        orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                onOrientationChangedThis(orientation);
            }
        };
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

                int rotation = getWindowManager().getDefaultDisplay().getRotation();

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
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateActivity.this);
        builder.setTitle("Prise de vue")
                .setMessage("Allez dans la première pièce et placez-vous au centre de la pièce.")
                .setCancelable(false)
                .setPositiveButton("J'ai compris", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String[] testItem = {"Buanderie", "Bureau", "Chambre 2", "Chambre 3", "Chambre 4", "Chambre Principale", "Couloir", "Couloir 2", "Cuisine", "Dressing", "Entrée", "Salle de bain", "Salle de bain 2", "Salon", "Terrasse"};
                        AlertDialog.Builder items = new AlertDialog.Builder(CreateActivity.this);
                        items.setTitle("Dans quelle pièce êtes-vous")
                                .setItems(testItem, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // The 'which' argument contains the index position
                                        // of the selected item
                                        AlertDialog.Builder prise = new AlertDialog.Builder(CreateActivity.this);
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
                        Intent panoramaIntent = new Intent(CreateActivity.this, PanoramaActivity.class);
                        startActivity(panoramaIntent);
                    }
                });
        //Creating dialog box
        AlertDialog dialog  = builder.create();
        dialog.show();
    }

    @Override
    protected void onResume() {

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetic, SensorManager.SENSOR_DELAY_NORMAL);
        orientationEventListener.enable();

        super.onResume();
        cameraKitView.onResume();
        initGyroSensors();
        // gyroSensor.startRecording();

        // final boolean isRecord = gyroSensor.isRecording();

        // Log.d("Gyrosensor", "is recordin main = " + isRecord);

        relativeLayout.addView(ballView);
        // relativeLayout.addView(testBall);

        mTmr = new Timer();
        mTsk = new TimerTask() {
            public void run() {

                ballView.mX += - linear_acceleration[0];
                ballView.mY += linear_acceleration[2];
                ballView.mZ += linear_acceleration[1];

                if (ballView.mY <= 0f) ballView.mY = 0f;
                if (ballView.mY >= 712.0f) ballView.mY = 712.0f;

                //redraw ball. Must run in background thread to prevent thread lock.
                RedrawHandler.post(new Runnable() {
                    public void run() {
                        ballView.invalidate();
                    }});
            }}; // TimerTask

        mTmr.schedule(mTsk,10,10); //start timer

        cameraKitView.setGestureListener(new CameraKitView.GestureListener() {
            @Override
            public void onTap(CameraKitView cameraKitView, float v, float v1) {

            }

            @Override
            public void onLongTap(CameraKitView cameraKitView, float v, float v1) {

            }

            @Override
            public void onDoubleTap(CameraKitView cameraKitView, float v, float v1) {

            }

            @Override
            public void onPinch(CameraKitView cameraKitView, float v, float v1, float v2) {

            }
        });
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        orientationEventListener.disable();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
            Toast.makeText(this, "Device was shuffed", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}

