package com.priscilla.viewwer.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.priscilla.viewwer.R;
import com.priscilla.viewwer.activity.BaseActivity;
import com.priscilla.viewwer.activity.BottomNavigationActivity;
import com.priscilla.viewwer.activity.EditProfilActivity;
import com.priscilla.viewwer.activity.WebViewVisitDetailActivity;
import com.priscilla.viewwer.api.APIClient;
import com.priscilla.viewwer.api.APIInterface;
import com.priscilla.viewwer.model.Favorites;
import com.priscilla.viewwer.model.Likes;
import com.priscilla.viewwer.model.UpvoteModel;
import com.priscilla.viewwer.model.responseSearch;
import com.priscilla.viewwer.model.responseTour;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;

import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.priscilla.viewwer.activity.BaseActivity.BounceAnimation;
import static com.priscilla.viewwer.activity.BaseActivity.DislikeIcon;
import static com.priscilla.viewwer.activity.BaseActivity.LikeTour;
import static com.priscilla.viewwer.activity.BaseActivity.LikeTourRedIcon;
import static com.priscilla.viewwer.activity.BaseActivity.switchIconFavorite;


public class VisitRecyclerAdapter extends RecyclerView.Adapter<VisitRecyclerAdapter.ViewHolder> {


    // RecyclerView recyclerView;
    private ArrayList<responseSearch> _tourList = new ArrayList<responseSearch>();
    private static APIInterface _apiInterface;

    private static ArrayList<Favorites> favorites_list;

    public VisitRecyclerAdapter(ArrayList<responseSearch> tourList, Context context) {
        this._tourList = tourList;
        this._apiInterface = APIClient.getinstance(context).create(APIInterface.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.content_list_visits, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // final _tourList myListData = listdata[position];
        final responseSearch positions = _tourList.get(position);
        Log.d("verification","======= position"+position);

        isTourFavorite(holder, _tourList, position);

        if(positions.getLiked()){

            holder.SearchLikeIcon.setImageResource(R.drawable.ic_heart_red);
            holder.SearchLikeRating.setTextColor(Color.RED);
        }else{
            holder.SearchLikeIcon.setImageResource(R.drawable.ic_like_cyan);
            holder.SearchLikeRating.setTextColor(Color.rgb(0,188,212));
        }

        final String WebViewUrl = positions.getUrl();

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(view.getContext(), WebViewVisitDetailActivity.class);
                Bundle b = new Bundle();
                b.putString("key", WebViewUrl); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                view.getContext().startActivity(intent);
            }
        });

        holder.SearchFavoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView visiteFavoriteicon = view.findViewById(R.id.SearchFavoriteIcon);
                Log.d("EntityId","========"+_tourList.get(position).getEntityId());
                switchIconFavorite(_tourList.get(position).getEntityId(), visiteFavoriteicon);
            }
        });

        holder.SearchLikeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String EntityId = positions.getEntityId();
                ImageView ImageIcon = view.findViewById(R.id.imageViewLikeIcon);
                Log.d("EntityId","========"+_tourList.get(position).getEntityId());

                final ImageView likeicon = view.findViewById(R.id.imageViewLikeIcon);
                final Bitmap bmap = ((BitmapDrawable)likeicon.getDrawable()).getBitmap();
                Drawable myDrawable = view.getResources().getDrawable(R.drawable.ic_heart_red);
                final Bitmap myIcon = ((BitmapDrawable) myDrawable).getBitmap();

                if(bmap.sameAs(myIcon))
                {
                    UpvoteModel dislike = new UpvoteModel();
                    dislike.setEntityType(positions.getEntityType());
                    dislike.setEntityId(positions.getEntityId());
                    dislike.setAction("dislike");
                    LikeTour(dislike);
                    DislikeIcon(positions.getLikedId());
                    positions.setLiked(false);
                    positions.setCounterRating(positions.getCounterRating() - 1);
                    notifyItemChanged(position);
                    notifyDataSetChanged();
                }else {
                    LikeTourRedIcon(positions.getEntityId());
                    UpvoteModel like = new UpvoteModel();
                    like.setEntityType(positions.getEntityType());
                    like.setEntityId(positions.getEntityId());
                    like.setAction("like");
                    LikeTour(like);
                    positions.setLiked(true);
                    positions.setCounterRating(positions.getCounterRating() + 1);
                    notifyItemChanged(position);
                    notifyDataSetChanged();
                }

                Log.d("EntityId","========"+ positions.getCounterRating());
            }
        });

        holder.linearLayoutShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( positions.Program != null) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    //  i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
                    i.putExtra(Intent.EXTRA_TEXT, positions.Program.getUrl());
                    view.getContext().startActivity(Intent.createChooser(i, positions.Program.getName()));
                }else{
                    Toast.makeText(view.getContext(),"Missing program",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void isTourFavorite(final VisitRecyclerAdapter.ViewHolder holder,
                               final ArrayList<responseSearch> _tourList, final int position) {


        final responseSearch positions = _tourList.get(position);

        ImageView img = (ImageView) holder.imageView;
        holder.SearchLikeRating.setText(positions.getCounterRating().toString());
        String imageUrl = positions.getThumbnail();
        Picasso.get().load(imageUrl).into(img);

        if(positions.getSaleType().equals("sale")){
            String title = holder.imageView.getContext().getString(R.string.Appartement_for_sale);
            holder.textView.setText(title+" "+positions.getType().replace("t0","T"));
        }else{
            String title = holder.imageView.getContext().getString(R.string.Appartement_for_rent);
            holder.textView.setText(title+" "+positions.getType().replace("t0","T"));
        }

        Call<ArrayList<Favorites>> call = _apiInterface.GetListFavorites();

        call.enqueue(new Callback<ArrayList<Favorites>>() {
            @Override
            public void onResponse(Call<ArrayList<Favorites>> call, Response<ArrayList<Favorites>> response) {

                if (response.isSuccessful()) {
                    ArrayList<Favorites> favorites = response.body();

                    for (Favorites list : favorites){
                        if(list.getEntityId().equals(positions.getEntityId())){

                            holder.SearchFavoriteIcon.setImageResource(R.drawable.ic_favorite_orange);
                            return;
                        }
                    }


                    holder.SearchFavoriteIcon.setImageResource(R.drawable.ic_favorite_gray_dark);

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Favorites>> call, Throwable t)
            {
                Log.d("Status response","=========== "+t);

            }
        });
    }

    public void isTourLike(final VisitRecyclerAdapter.ViewHolder holder,
                               final ArrayList<responseSearch> _tourList, final int position) {


        final responseSearch positions = _tourList.get(position);

        Call<ArrayList<UpvoteModel>> call = _apiInterface.GetListLikes();

        call.enqueue(new Callback<ArrayList<UpvoteModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UpvoteModel>> call, Response<ArrayList<UpvoteModel>> response) {

                if (response.isSuccessful()) {
                    ArrayList<UpvoteModel> likes = response.body();

                    for (UpvoteModel list : likes){
                        if(list.getEntityId().equals(positions.getEntityId())){

                            holder.SearchLikeIcon.setImageResource(R.drawable.ic_heart_red);
                            return;
                        }
                    }


                    holder.SearchLikeIcon.setImageResource(R.drawable.ic_like_cyan);

                }
            }

            @Override
            public void onFailure(Call<ArrayList<UpvoteModel>> call, Throwable t)
            {
                Log.d("Status response","=========== "+t);

            }
        });
    }

    @Override
    public int getItemCount() {
        return _tourList.size();
    }


    @Override
    public long getItemId(int position) {
        return _tourList.indexOf(_tourList.get(position));
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public RelativeLayout relativeLayout;
        public ImageView SearchFavoriteIcon;
        public TextView SearchLikeRating;
        public ImageView SearchLikeIcon;
        public LinearLayout linearLayoutShare;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.img);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            textView.setTextColor(Color.BLACK);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
            this.SearchFavoriteIcon = (ImageView) itemView.findViewById(R.id.SearchFavoriteIcon);
            this.SearchLikeRating = (TextView) itemView.findViewById(R.id.SearchLikeRating);
            this.SearchLikeIcon = (ImageView) itemView.findViewById(R.id.imageViewLikeIcon);
            this.linearLayoutShare = (LinearLayout) itemView.findViewById(R.id.linearLayoutShare);
        }
    }
}
