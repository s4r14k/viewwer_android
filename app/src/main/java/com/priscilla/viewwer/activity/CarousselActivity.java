package com.priscilla.viewwer.activity;

import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;

import com.priscilla.viewwer.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

public class CarousselActivity extends AppCompatActivity {
    CarouselView carouselView;
    WebView webcaroussel;

    int[] sampleImages = {R.drawable.best_practice_1, R.drawable.best_practice_2, R.drawable.best_practice_3,
            R.drawable.best_practice_4, R.drawable.best_practice_5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caroussel);


        webcaroussel = (WebView) findViewById(R.id.webcaroussel);
        //this.addContentView(webcaroussel);
        //setContentView(webcaroussel);
        webcaroussel.loadUrl("https://icreadev.mg/caroussel/");


        //carouselView = (CarouselView) findViewById(R.id.carouselView);
        //carouselView.setPageCount(sampleImages.length);
        //carouselView.setImageListener(imageListener);


    }

//    ImageListener imageListener = new ImageListener() {
//        @Override
//        public void setImageForPosition(int position, ImageView imageView) {
//            imageView.setImageResource(sampleImages[position]);
//        }
//    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportFragmentManager().popBackStack();
    }
}
