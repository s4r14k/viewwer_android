package com.priscilla.viewwer.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.priscilla.viewwer.R;
import com.priscilla.viewwer.utils.GyroSensor;
import com.priscilla.viewwer.utils.MyDebug;

import static android.content.ContentValues.TAG;

public class BallView extends View {

    private static final String TAG = "BallView";

    public float mX, finalX;
    public float mY, finalY;
    public float mZ, finalZ;
    private Bitmap ball;
    private Bitmap redBall;
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private GyroSensor gyroSensor;
    private int n_panorama_pics = 0, max_panorama_pics_c = 24;
    private final static float panorama_pics_per_screen = 3.33333f;

    //construct new ball object
    public BallView(Context context, float x, float y, float z) {
        super(context);
        gyroSensor = new GyroSensor(context);
        //color hex is [transparency][red][green][blue]
        mPaint.setColor(0xFF00FF00); //not transparent. color is green

        this.mX = x;
        this.mY = y;
        this.mZ = z;

        Bitmap ballSrc = BitmapFactory.decodeResource(getResources(), R.drawable.green);
        ball = Bitmap.createScaledBitmap(ballSrc, 85, 85, true);

        Bitmap redBallSrc = BitmapFactory.decodeResource(getResources(), R.drawable.red);
        redBall = Bitmap.createScaledBitmap(redBallSrc, 85, 85, true);
    }

    //called by invalidate()
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mY > 190.0f && mY < 250.0f) {
            canvas.drawBitmap(ball, mX, mY, null);
        } else {
            canvas.drawBitmap(redBall, mX, mY, null);
        }
        setNextPanoramaPoint(mX, mY, mZ);
    }

    private void setNextPanoramaPoint(boolean repeat) {
        if( MyDebug.LOG )
            Log.d(TAG, "setNextPanoramaPoint");
        if( !repeat )
            n_panorama_pics++;
        if( MyDebug.LOG )
            Log.d(TAG, "n_panorama_pics is now: " + n_panorama_pics);
        if( n_panorama_pics == max_panorama_pics_c ) {
            if( MyDebug.LOG )
                Log.d(TAG, "reached max panorama limit");
            return;
        }
        float angle = (float) Math.toRadians(90) * n_panorama_pics;
        float x = (float) Math.sin(angle / panorama_pics_per_screen);
        float z = (float) -Math.cos(angle / panorama_pics_per_screen);
        setNextPanoramaPoint(x, 0.0f, z);



        if( n_panorama_pics == 1 ) {
            // also set target for right-to-left
            angle = - angle;
            x = (float) Math.sin(angle / panorama_pics_per_screen);
            z = (float) -Math.cos(angle / panorama_pics_per_screen);
            gyroSensor.addTarget(x, 0.0f, z);
        }

        finalX = x;
        finalY = z;
    }

    private void setNextPanoramaPoint(float x, float y, float z) {
        if( MyDebug.LOG )
            Log.d(TAG, "setNextPanoramaPoint : " + x + " , " + y + " , " + z);

        final float target_angle = 1.0f * 0.01745329252f;
        //final float target_angle = 0.5f * 0.01745329252f;
        final float upright_angle_tol = 2.0f * 0.017452406437f;
        //final float upright_angle_tol = 1.0f * 0.017452406437f;
        final float too_far_angle = 45.0f * 0.01745329252f;
        gyroSensor.setTarget(x, y, z, target_angle, upright_angle_tol, too_far_angle, new GyroSensor.TargetCallback() {
            @Override
            public void onAchieved(int indx) {
                if( MyDebug.LOG ) {
                    Log.d(TAG, "TargetCallback.onAchieved: " + indx);
                }
                // Disable the target callback so we avoid risk of multiple callbacks - but note we don't call
                // clearPanoramaPoint(), as we don't want to call drawPreview.clearGyroDirectionMarker()
                // at this stage (looks better to keep showing the target market on-screen whilst photo
                // is being taken, user more likely to keep the device still).
                // Also we still keep the target active (and don't call clearTarget() so we can monitor if
                // the target is still achieved or not (for panorama_pic_accepted).
                //gyroSensor.clearTarget();
                gyroSensor.disableTargetCallback();
            }

            @Override
            public void onTooFar() {
                if( MyDebug.LOG )
                    Log.d(TAG, "TargetCallback.onTooFar");

            }

        });
    }
}
