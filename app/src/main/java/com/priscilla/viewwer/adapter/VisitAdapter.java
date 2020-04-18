package com.priscilla.viewwer.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.priscilla.viewwer.R;
import com.priscilla.viewwer.model.Employe;
import com.priscilla.viewwer.model.Tour;
import com.priscilla.viewwer.model.responseTour;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VisitAdapter extends BaseAdapter {
    private LayoutInflater _inflater;
    private ArrayList<responseTour> _tourList = new ArrayList<responseTour>();

    public VisitAdapter(LayoutInflater inflater, ArrayList<responseTour> tourList){
        _inflater = inflater;
        _tourList = tourList;
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


        view = _inflater.inflate(R.layout.content_list_visits, null);

        // Set labels and values
        TextView description = (TextView) view.findViewById(R.id.textView);
        description.setText(_tourList.get(pos).getDescription());

        ImageView img = (ImageView) view.findViewById(R.id.img);
        String imageUrl = _tourList.get(pos).getThumbnail();
        Log.d("test",_tourList.get(pos).getDescription() + "description");
        //Loading image using Picasso
        Picasso.get().load(imageUrl).into(img);
       // img.setImageResource("DDD");

        return view;
    }
}
