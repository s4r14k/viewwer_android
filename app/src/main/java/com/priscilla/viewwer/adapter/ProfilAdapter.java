package com.priscilla.viewwer.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.priscilla.viewwer.R;
import com.priscilla.viewwer.activity.WebViewVisitDetailActivity;
import com.priscilla.viewwer.model.responseTour;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfilAdapter extends BaseAdapter {
    private LayoutInflater _inflater;
    private ArrayList<responseTour> _tourList = new ArrayList<responseTour>();

    public ProfilAdapter(LayoutInflater _inflater, ArrayList<responseTour> _tourList) {
        this._inflater = _inflater;
        this._tourList = _tourList;
    }

    @Override
    public int getCount() {
        return _tourList.size();
    }

    @Override
    public responseTour getItem(int position) {
        return _tourList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return _tourList.indexOf(_tourList.get(position));
    }
    @Override

    public View getView(int pos, View view, ViewGroup viewGroup) {
        view = _inflater.inflate(R.layout.content_profil_list_visits, null);

        // Set labels and values
        TextView profilVisiteTitle = (TextView) view.findViewById(R.id.profileVisiteTitle);
        profilVisiteTitle.setText(_tourList.get(pos).getDescription());

        ImageView img = (ImageView) view.findViewById(R.id.profilVisiteImage);
        String imageUrl = _tourList.get(pos).getThumbnail();
        Log.d("test","imageUrl");
        //Loading image using Picasso
        Picasso.get().load(imageUrl).into(img);
        // img.setImageResource("DDD");

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(view.getContext(), WebViewVisitDetailActivity.class);
                Bundle b = new Bundle();
             //   b.putString("key", WebViewUrl); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                view.getContext().startActivity(intent);
            }
        });

        return view;
    }
}
