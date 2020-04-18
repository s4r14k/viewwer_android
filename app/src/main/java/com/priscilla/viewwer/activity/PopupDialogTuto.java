package com.priscilla.viewwer.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.priscilla.viewwer.R;

public class PopupDialogTuto extends Dialog {

    private AppCompatActivity appCompatActivity;

    private TextView prisedevue, hotspots, pratique, cancel;

    public PopupDialogTuto(Activity activity) {
        super(activity, R.style.Theme_AppCompat_Dialog);
        setContentView(R.layout.activity_popup);

        initView();
        //initListener();


    }

    private void initListener() {
//        prisedevue.setOnClickListener(this);
//        pratique.setOnClickListener(this);
//        hotspots.setOnClickListener(this);
    }

    private void initView() {
        this.prisedevue = findViewById(R.id.tuto_prise_de_vue);
        this.pratique = findViewById(R.id.tuto_pratique);
        this.hotspots = findViewById(R.id.tuto_hotspots);
        this.cancel = findViewById(R.id.tutp_annuler);


    }

    public void setCancel(TextView cancel) {
        this.cancel = cancel;
    }

    public TextView getPrisedevue(){return prisedevue;}

    public TextView getPratique(){return pratique;}

    public TextView getHotspots(){return hotspots;}

    public TextView getCancel(){
        return cancel;}

    public void build(){
        show();
    }

    public void exit(){
        hide();
    }


}
