package com.priscilla.viewwer.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.priscilla.viewwer.R;
import com.priscilla.viewwer.model.HotSpot;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;


@SuppressWarnings("MagicConstant")
public class PanoramaActivity extends BaseActivity implements View.OnClickListener {
    private SubsamplingScaleImageView mVrPanoramaView;
    private FrameLayout scrollviewPanorama;
    private ArrayList<HotSpot> hotspotTab;
    private static final int MAX_CLICK_DURATION = 200;
    private long startClickTime;
    private ImageView iv = null;
    private Button EditVisite,PreviousRoom,NextRoom,WatchTutorial,AddHotspot;
    private Toolbar toolbar,toolbarActivity;
    private TextView saveTour;
    private int c = 0, startX = 0,startY = 0;
    private File imgFile;
    private File[] files;
    private int compteurFile = 0;
    private HorizontalScrollView horizontalScrollView;
    private FrameLayout framlayout;
    private boolean isOnHotspotClick = false;

    private int imageHeight;
    private int imageWidth;


    Rect outRect = new Rect();
    int[] location = new int[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panorama);
        initView();
        initFile();
        clickButton();
        toolBar();
        onTouchImageView();

        mVrPanoramaView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageWidth = mVrPanoramaView.getWidth();
                imageHeight = mVrPanoramaView.getHeight();
            }
        });
    }

    public void toolbarActivityPanorama(View view, int idTooblar, int Title, AppCompatActivity appCompatActivity) {
        toolbar = view.findViewById(idTooblar);
        appCompatActivity.setSupportActionBar(toolbar);

        toolbarActivity = (Toolbar) toolbar.findViewById(R.id.toolbarActivity);
        saveTour = (TextView) toolbar.findViewById(R.id.saveTour);

        saveTour.setOnClickListener(this);

        TextView title = (TextView) toolbar.findViewById(R.id.ToolbarTitle);
        ImageView back = (ImageView) toolbar.findViewById(R.id.ToolbarBack);
        LinearLayout save = (LinearLayout) toolbar.findViewById(R.id.toobarPanorama) ;
        LinearLayout backTo = (LinearLayout) toolbar.findViewById(R.id.linearLayoutBack);
//        save.setOnClickListener(this::onClick);
        title.setText(Title);
        save.setVisibility(View.VISIBLE);
        backTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Click","========= button clicked");

                startActivity(new Intent(appCompatActivity, BottomNavigationActivity.class));

                appCompatActivity.finish();


            }
        });
    }

    public void toolBar(){
        View rootView = findViewById(android.R.id.content).getRootView();
        //Toolbar
        toolbarActivityPanorama(rootView,R.id.PanoramaToolbar,R.string.annuler,this);
    }



    public void onTouchImageView(){
        mVrPanoramaView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        startClickTime = Calendar.getInstance().getTimeInMillis();
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        isOnHotspotClick = is0nClickHotspot((int) event.getRawX(),(int) event.getRawY() );
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                        if(clickDuration < MAX_CLICK_DURATION) {
                            final int x = (int) (event.getRawX());
                            final int y = (int) (event.getRawY());
                            if(!isOnHotspotClick){
                                addHotspot();
                            }
                        }
                    }
                }
                return true;
            }
        });
    }

    private void addHotspot(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(PanoramaActivity.this);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PanoramaActivity.this, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Salon");
        arrayAdapter.add("Chambre");


        builderSingle.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String roomName = arrayAdapter.getItem(which);

                iv = new ImageView(getApplicationContext());

                HotSpot hotSpot = new HotSpot();

                switch (roomName){
                    case "Salle de bain":
                        iv.setImageResource(R.drawable.bathroom);
                        hotSpot.setHotspotLeadTo(which);
                        break;
                    case "Cuisine":
                        iv.setImageResource(R.drawable.kitchen);
                        hotSpot.setHotspotLeadTo(which);
                        break;
                    case "Chambre":
                        iv.setImageResource(R.drawable.bedroom);
                        hotSpot.setHotspotLeadTo(which);
                        break;
                    case "Salon":
                        iv.setImageResource(R.drawable.livingroom);
                        hotSpot.setHotspotLeadTo(which);
                        break;
                }
                iv.setLayoutParams(new FrameLayout.LayoutParams(100,100));
                iv.setY(startY - 90);
                iv.setX(startX - 30);

                hotSpot.setHotspot(iv);
                Log.d("scroll width","########"+ imageWidth);

                float width = Math.round(((startX  * 360) / imageWidth)* 100) / 100;

                if(width > 180){
                    hotSpot.setATH((width + 180) - 360);
                }else{
                    hotSpot.setATH(width + 180);
                }



                if(startY > (imageHeight/2)){
                    float difference = startY - (imageHeight/2);
                    hotSpot.setATV(Math.round((((difference * 30) / (imageHeight/2)) * 100)) / 100);
                }else{
                    float difference = (imageHeight/2) - startY;
                    hotSpot.setATV(Math.round(((( - difference * 30) / (imageHeight/2)) * 100)) / 100);
                }

                hotSpot.setImage(files[compteurFile].getName());
                hotSpot.setSceneId(Integer.toString(compteurFile));

                hotspotTab.add(hotSpot);

                framlayout.addView(iv);
            }
        });
        builderSingle.show();
    }

    public void initView(){
        EditVisite = (Button) findViewById(R.id.EditVisite);
        PreviousRoom = (Button) findViewById(R.id.PreviousRoom);
        NextRoom = (Button) findViewById(R.id.NextRoom);
        WatchTutorial = (Button) findViewById(R.id.WatchTutorial);
        AddHotspot = (Button) findViewById(R.id.AddHotspot);
        scrollviewPanorama = (FrameLayout) findViewById(R.id.scrollviewPanorama);
        mVrPanoramaView = (SubsamplingScaleImageView)findViewById(R.id.mVrPanoramaView);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        framlayout = (FrameLayout) findViewById(R.id.framlayout);
        hotspotTab = new ArrayList<HotSpot>();
    }

    public void clickButton(){
        //button
        WatchTutorial.setOnClickListener(this);
        PreviousRoom.setOnClickListener(this);
        NextRoom.setOnClickListener(this);
    }

    private boolean isViewInBounds(View view, int x, int y){
        view.getDrawingRect(outRect);
        view.getLocationOnScreen(location);
        outRect.offset(location[0], location[1]);
        return outRect.contains(x, y);
    }

    private void initFile(){
        imgFile = new  File(Environment.getExternalStorageDirectory() + File.separator +"viewwer");
        if(imgFile.exists()){
            files = imgFile.listFiles();
            if(files !=  null){

                String url = imgFile.getAbsolutePath() +"/"+files[0].getName();
                Log.d("enter here:","============="+url);
                mVrPanoramaView.setImage(ImageSource.uri(url));
            }else{
                Log.d("no image:","###");
            }
        }else{
            Log.d("enter here tsy misy:","=============");
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Aucune image enregistré");
            dialog.setMessage("Veuillez refaire la prise de photo");
            dialog.setNeutralButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getApplicationContext(), BottomNavigationActivity.class);
                    startActivity(intent);
                    finish();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    private boolean is0nClickHotspot(int x, int y){
        boolean isHotspot = false;
        int compteur = 0;
        int containerHotspotClick = 0;
        if(!hotspotTab.isEmpty()) {
            WatchTutorial.setVisibility(View.GONE);
            AddHotspot.setVisibility(View.GONE);
            for (HotSpot hotspot : hotspotTab
            ) {
                boolean  isClick = isViewInBounds(hotspot.getHotspot(), x, y);
                if(isClick){
                    containerHotspotClick = compteur;
                    isHotspot = true;
                }
                compteur ++;
            }
        }else{
            WatchTutorial.setVisibility(View.VISIBLE);
            AddHotspot.setVisibility(View.VISIBLE);
        }

        if(isHotspot){
            framlayout.removeView(hotspotTab.get(containerHotspotClick).getHotspot());
            hotspotTab.remove(containerHotspotClick);
        }

        return isHotspot;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveTour:
                ArrayList<String> mustAddImage = new ArrayList<>();
                if(!hotspotTab.isEmpty()){
                    for (File file:imgFile.listFiles()
                    ) {
                        int compteurTab = 1;
                        for (HotSpot hotspot:hotspotTab
                        ) {
                            if(compteurTab == hotspotTab.size()){
                                if(!hotspot.getImage().equals(file.getName())) {
                                    mustAddImage.add(file.getName());
                                }
                                compteurTab ++;
                            }
                        }
                    }
                    if(!mustAddImage.isEmpty()){
                        for (String fileName:mustAddImage
                        ) {
                            HotSpot hotSpot = new HotSpot();
                            hotSpot.setImage(fileName);
                            hotSpot.setSceneId(String.valueOf(hotspotTab.size()));
                            hotspotTab.add(hotSpot);
                        }
                    }
                }else{
                    for(File file:imgFile.listFiles()){

                        HotSpot hotSpot = new HotSpot();
                        hotSpot.setImage(file.getName());
                        hotSpot.setSceneId(String.valueOf(hotspotTab.size()));
                        hotspotTab.add(hotSpot);
                    }
                }

                for (HotSpot hotspot:hotspotTab
                ) {
                    Log.d("HotspotTab","########## image"+hotspot.getImage());
                    Log.d("HotspotTab","########## hotspot"+hotspot.getATH());
                    Log.d("HotspotTab","########## hotspot"+hotspot.getATV());
                    Log.d("HotspotTab","########## hotspot"+hotspot.getHotspotLeadTo());
                    Log.d("HotspotTab","########## hotspot"+hotspot.getSceneId());
                }

                Intent intentSavetour = new Intent(getApplicationContext(),Visite.class);
                intentSavetour.putExtra("hotpspotParameter",hotspotTab);
                startActivity(intentSavetour);
                finish();
                break;
            case R.id.WatchTutorial:
                Intent intent = new Intent(getApplicationContext(), VideoTutoActivity.class);
                intent.putExtra("tutorial","Tutoriel Hotspots");
                startActivity(intent);
                finish();
                break;
            case R.id.PreviousRoom:
                if(compteurFile > 0){
                    OnChangeImageRemoveHotspot(compteurFile);
                    compteurFile --;
                    String url = imgFile.getAbsolutePath() +"/"+files[compteurFile].getName();
                    mVrPanoramaView.setImage(ImageSource.uri(url));

                    OnChangeImageAddHotspot(compteurFile);

                }else{
                    return;
                }
                break;
            case R.id.NextRoom:
                if(compteurFile < (files.length - 1)){
                    OnChangeImageRemoveHotspot(compteurFile);
                    compteurFile ++;
                    String url = imgFile.getAbsolutePath() +"/"+files[compteurFile].getName();
                    mVrPanoramaView.setImage(ImageSource.uri(url));

                    OnChangeImageAddHotspot(compteurFile);
                }else{
                    compteurFile = files.length - 1;
                    return;
                }
                break;
        }
    }

    private void OnChangeImageRemoveHotspot(int compteurFile){

        if(!hotspotTab.isEmpty()){
            for (HotSpot hotspot:hotspotTab
            ) {
                if(hotspot.getHotspot() != null){
                    if(hotspot.getImage().equals(files[compteurFile].getName())){
                        framlayout.removeView(hotspot.getHotspot());
                    }
                }
            }
        }
    }

    private void OnChangeImageAddHotspot(int compteurFile){
        if(!hotspotTab.isEmpty()){
            for (HotSpot hotspot:hotspotTab
            ) {
                if(hotspot.getHotspot() != null){
                    if(hotspot.getImage().equals(files[compteurFile].getName())){
                        framlayout.addView(hotspot.getHotspot());
                    }
                }
            }
        }
    }
}







