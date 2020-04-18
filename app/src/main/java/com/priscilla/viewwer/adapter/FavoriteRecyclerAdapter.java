package com.priscilla.viewwer.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.priscilla.viewwer.R;
import com.priscilla.viewwer.activity.BaseActivity;
import com.priscilla.viewwer.api.APIClient;
import com.priscilla.viewwer.api.APIInterface;
import com.priscilla.viewwer.model.Favorites;
import com.priscilla.viewwer.model.Tour;
import com.priscilla.viewwer.model.UpvoteModel;
import com.priscilla.viewwer.model.responseSearch;
import com.priscilla.viewwer.model.responseTour;
import com.priscilla.viewwer.utils.App;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.priscilla.viewwer.activity.BaseActivity.DislikeIcon;
import static com.priscilla.viewwer.activity.BaseActivity.LikeTour;
import static com.priscilla.viewwer.activity.BaseActivity.LikeTourRedIcon;
import static com.priscilla.viewwer.activity.BaseActivity.deleteFavory;
import static com.priscilla.viewwer.utils.App.getContext;

public class FavoriteRecyclerAdapter extends RecyclerView.Adapter<FavoriteRecyclerAdapter.ViewHolder> {
    private ArrayList<responseSearch> _tourList = new ArrayList<>();
    private static APIInterface _apiInterface;

    private static ArrayList<Favorites> favorites_list;

    private Context context;

    public FavoriteRecyclerAdapter(ArrayList<responseSearch> tourList) {
        this._tourList = tourList;
        _apiInterface = APIClient.getinstance(context).create(APIInterface.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.content_favorite_list_visits, parent, false);

        FavoriteRecyclerAdapter.ViewHolder viewHolder = new FavoriteRecyclerAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final responseSearch positions = _tourList.get(position);

        isTourFavorite(holder, _tourList,position);
        unFaroriteTour(holder, _tourList,position);

        if(positions.getLiked()){
            holder.FavoriteLikeIcon.setImageResource(R.drawable.ic_heart_red);
            holder.FavoriteLikeRating.setTextColor(Color.RED);
        }else{
            holder.FavoriteLikeIcon.setImageResource(R.drawable.ic_like_cyan);
            holder.FavoriteLikeRating.setTextColor(ContextCompat.getColor(getContext(),R.color.color_cyan));


        }

        holder.FavoriteLikeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String EntityId = positions.getEntityId();
                ImageView ImageIcon = view.findViewById(R.id.FavoriteLikeIcon);
                Log.d("EntityId","========"+_tourList.get(position).getEntityId());

                final ImageView likeicon = view.findViewById(R.id.FavoriteLikeIcon);
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

        holder.linearLayoutFavoriteShare.setOnClickListener(new View.OnClickListener() {
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

    private Resources getResources() {
        return null;
    }


    public  void isTourFavorite(final FavoriteRecyclerAdapter.ViewHolder holder,
                                final ArrayList<responseSearch> _tourList, final int position){

        final responseSearch positions = _tourList.get(position);

        Log.d("Response code:", "===========tours "+ positions.getEntityId()+" is favorite "+positions.getFavorite());

        if(positions.getSaleType().equals("sale")){
            String title = holder.imageView.getContext().getString(R.string.Appartement_for_sale);
            holder.textView.setText(title+" "+positions.getType().replace("t0","T"));
        }else{
            String title = holder.imageView.getContext().getString(R.string.Appartement_for_rent);
            holder.textView.setText(title+" "+positions.getType().replace("t0","T"));
        }

        holder.FavoriteLikeRating.setText(positions.getCounterRating().toString());

        ImageView img = (ImageView) holder.imageView;

        String imageUrl = positions.getThumbnail();

        Picasso.get().load(imageUrl).into(img);
        holder.FavoriteFavoriteIcon.setImageResource(R.drawable.ic_star_orange);


    }

    public void unFaroriteTour(final FavoriteRecyclerAdapter.ViewHolder holder,
                               final ArrayList<responseSearch> _tourList, final int position){
        holder.FavoriteFavoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Call<ArrayList<Favorites>> call = _apiInterface.GetListFavorites();

                call.enqueue(new Callback<ArrayList<Favorites>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Favorites>> call, Response<ArrayList<Favorites>> response) {
                        Log.d("Response code:", "===========" + response.code() + "=======" + response.isSuccessful());

                        final responseSearch positions =  _tourList.get(position);


                        if (response.isSuccessful()) {
                            favorites_list = response.body();

                            for (Favorites list: favorites_list) {
                                if(list.getEntityId().equals(positions.getEntityId())){
                                    Log.d("Response code:", "===========Entire here" );

                                    deleteFavory(list.getFavoriteId(),_apiInterface);

                                    LinearLayout parent = (LinearLayout) view.findViewById(R.id.FavoriteFavoriteIcon).
                                            getParent().getParent();
                                    parent.setVisibility(View.GONE);
                                    Log.d("Status response", "=========== success");

                                    return;

                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Favorites>> call, Throwable t) {
                        Log.d("Status response", "=========== " + t);

                    }
                });
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


    public static  class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public RelativeLayout relativeLayout;
        public ImageView FavoriteFavoriteIcon;
        public TextView FavoriteLikeRating;
        public LinearLayout FavoriteContainerItem;
        public LinearLayout linearLayoutFavoriteShare;
        public ImageView FavoriteLikeIcon;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.img);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            textView.setTextColor(Color.BLACK);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
            this.FavoriteFavoriteIcon = (ImageView) itemView.findViewById(R.id.FavoriteFavoriteIcon);
            this.FavoriteLikeRating = (TextView) itemView.findViewById(R.id.FavoriteLikeRating2);
            this.FavoriteContainerItem = (LinearLayout) itemView.findViewById(R.id.FavoriteContainerItem);
            this.linearLayoutFavoriteShare = (LinearLayout) itemView.findViewById(R.id.linearLayoutFavoriteShare);
            this.FavoriteLikeIcon = (ImageView) itemView.findViewById(R.id.FavoriteLikeIcon);
        }
    }

}


