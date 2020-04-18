package com.priscilla.viewwer.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Entity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.priscilla.viewwer.activity.ChangePasswordActivity;
import com.priscilla.viewwer.activity.EditProfilActivity;
import com.priscilla.viewwer.activity.EditTourActivity;
import com.priscilla.viewwer.activity.WebViewVisitDetailActivity;
import com.priscilla.viewwer.api.APIClient;
import com.priscilla.viewwer.api.APIInterface;
import com.priscilla.viewwer.model.Favorites;
import com.priscilla.viewwer.model.UpvoteModel;
import com.priscilla.viewwer.model.responseProfilEdit;
import com.priscilla.viewwer.model.responseSearch;
import com.priscilla.viewwer.utils.App;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.priscilla.viewwer.activity.BaseActivity.DislikeIcon;
import static com.priscilla.viewwer.activity.BaseActivity.LikeTour;
import static com.priscilla.viewwer.activity.BaseActivity.LikeTourRedIcon;
import static com.priscilla.viewwer.activity.BaseActivity.createFavorite;
import static com.priscilla.viewwer.activity.BaseActivity.deleteFavory;
import static com.priscilla.viewwer.activity.BaseActivity.switchIconFavorite;

public class ProfilRecyclerAdapter extends RecyclerView.Adapter<ProfilRecyclerAdapter.ViewHolder>  {

    private ArrayList<responseSearch> _tourList = new ArrayList<responseSearch>();
    private ViewHolder holders;
    private ArrayList<Boolean> isPublish;
    private ArrayList<Boolean> isFavorite;
    private Context context;
    private APIInterface _apiInterface;
    private ArrayList<Favorites> favorites_list = null;

    public ProfilRecyclerAdapter(ArrayList<responseSearch> tourList) {
        this.context = context;
        this._tourList = tourList;
        this.isPublish = new ArrayList<Boolean>();
        this.isFavorite = new ArrayList<Boolean>();
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.content_profil_list_visits, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final responseSearch positions = _tourList.get(position);
        final String WebViewUrl = positions.getUrl();

        isPublish.add(positions.getIsPublished());

        isTourFavorite(holder, _tourList,position);

        publishedUnpublishedTour(holder, _tourList,position);

        deleteTour(holder, _tourList,position);
        editTour(holder, _tourList,position);

        if(positions.getLiked()){

            holder.ProfilLikeIcon.setImageResource(R.drawable.ic_heart_red);
        }else{
            holder.ProfilLikeIcon.setImageResource(R.drawable.ic_like_cyan);
        }

        holder.ProfilLikeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String EntityId = positions.getEntityId();
                ImageView ImageIcon = view.findViewById(R.id.ProfilLikeIcon);
                Log.d("EntityId","========"+_tourList.get(position).getEntityId());

                final ImageView likeicon = view.findViewById(R.id.ProfilLikeIcon);
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

        holder.linearLayoutProfilShare.setOnClickListener(new View.OnClickListener() {
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
    }


    public  void isTourFavorite(final ViewHolder holder,
                                final ArrayList<responseSearch> _tourList,final int position){
        _apiInterface = APIClient.getinstance(context).create(APIInterface.class);

        Call<ArrayList<Favorites>> call = _apiInterface.GetListFavorites();

        call.enqueue(new Callback<ArrayList<Favorites>>() {
            @Override
            public void onResponse(Call<ArrayList<Favorites>> call, Response<ArrayList<Favorites>> response) {
                Log.d("Response code:", "===========" + response.code() + "=======" + response.isSuccessful());

                final responseSearch positions =  _tourList.get(position);

                if (response.isSuccessful()) {
                    favorites_list = response.body();

                    if(positions.getSaleType().equals("sale")){
                        String title = holder.imageView.getContext().getString(R.string.Appartement_for_sale);
                        holder.textView.setText(title+" "+positions.getType().replace("t0","T"));
                    }else{
                        String title = holder.imageView.getContext().getString(R.string.Appartement_for_rent);
                        holder.textView.setText(title+" "+positions.getType().replace("t0","T"));
                    }

                    holder.ProfilLikeRating.setText(positions.getCounterRating().toString());

                    ImageView img = (ImageView) holder.imageView;

                    String imageUrl = positions.getThumbnail();

                    Picasso.get().load(imageUrl).into(img);


                    //Tour Moderation status
                    switch (positions.getModerationStatus()){
                        case "pending":
                            holder.ProfilModerationStatus.setText(R.string.activity_Profil_Moderation_status_approved);
                            holder.ProfilModerationStatusContainer.setBackgroundColor(Color.parseColor("#f57c00"));
                            Log.d("Response code:", "===========Status pending" );
                            break;
                        case "rejected":
                            holder.ProfilModerationStatus.setText(R.string.activity_Profil_Moderation_status_rejected);
                            holder.ProfilModerationStatusContainer.setBackgroundColor(Color.parseColor("#d32f2f"));
                            Log.d("Response code:", "===========Status rejected" );
                            break;
                        case "approved":
                            holder.ProfilModerationStatus.setText(R.string.activity_Profil_Moderation_status_approved);
                            holder.ProfilModerationStatusContainer.setBackgroundColor(Color.parseColor("#388e3c"));
                            Log.d("Response code:", "===========Status completed" );
                            break;
                        default:
                            Log.d("Response code:", "===========Status approved" );
                            return;


                    }

                    //Tour ispublished
                    if (positions.getIsPublished()){
                        holder.ProfilIsPublishedIcon.setImageResource(R.drawable.ic_publish_cyan);
                    }

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Favorites>> call, Throwable t) {
                Log.d("Status response", "=========== " + t);

            }
        });
    }

    public void publishedUnpublishedTour(final ViewHolder holder,
                                         final ArrayList<responseSearch> _tourList,final int position){
        holder.ProfilIsPublishedIcon.setClickable(true);
        holder.ProfilIsPublishedIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _apiInterface = APIClient.getinstance(context).create(APIInterface.class);

                ImageView published = (ImageView) view.findViewById(R.id.ProfilIsPublishedIcon);
                Drawable drawable = view.getResources().getDrawable(R.drawable.ic_publish_cyan);

                /*Bitmap bitmap_1 = ((BitmapDrawable) drawable.hashCode()).getBitmap();
                Bitmap bitmap_2 = ((BitmapDrawable) published.getDrawable()).getBitmap();*/

                /*Log.d("bitmap","====="+compareDrawable(drawable,published.getDrawable()));*/


                if(isPublish.get(position) == true){

                    published.setImageResource(R.drawable.ic_unpublish_gray_dark);

                    Log.d("unpublish", "=========== " );

                    Call<responseProfilEdit> call = _apiInterface.PublishedTour(_tourList.get(position).getEntityId(),
                            false);

                    isPublish.set(position,false);

                    call.enqueue(new Callback<responseProfilEdit>() {
                        @Override
                        public void onResponse(Call<responseProfilEdit> call, Response<responseProfilEdit> response) {
                            Log.d("Response code:", "===========" + response.code() + "=======" + response.isSuccessful());

                            final responseSearch positions = _tourList.get(position);

                            if (response.isSuccessful()) {
                                Log.d("Status response", "=========== success");
                            }
                        }

                        @Override
                        public void onFailure(Call<responseProfilEdit> call, Throwable t) {
                            Log.d("Status response", "=========== " + t);

                        }
                    });
                }else{
                    Log.d("publish", "=========== " );

                    published.setImageResource(R.drawable.ic_publish_cyan);

                    Call<responseProfilEdit> call = _apiInterface.PublishedTour(_tourList.get(position).getEntityId(),
                            true);
                    isPublish.set(position,true);

                    call.enqueue(new Callback<responseProfilEdit>() {
                        @Override
                        public void onResponse(Call<responseProfilEdit> call, Response<responseProfilEdit> response) {
                            Log.d("Response code:", "===========" + response.code() + "=======" + response.isSuccessful());

                            final responseSearch positions = _tourList.get(position);

                            if (response.isSuccessful()) {
                                Log.d("Status response", "=========== success");
                            }
                        }

                        @Override
                        public void onFailure(Call<responseProfilEdit> call, Throwable t) {
                            Log.d("Status response", "=========== " + t);

                        }
                    });
                }
            }
        });
    }

    public void editTour(final ViewHolder holder,
                         final ArrayList<responseSearch> _tourList,final int position){
        holder.ProfilEditTourIcon.setClickable(true);
        holder.ProfilEditTourIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditTourActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", _tourList.get(position).getEntityId()); //Your id
                intent.putExtras(bundle); //Put your id to your next Intent
                view.getContext().startActivity(intent);
            }
        });
    }



    public void deleteTour(final ViewHolder holder,
                           final ArrayList<responseSearch> _tourList,final int position){
        holder.ProfilDeleteIcon.setClickable(true);
        holder.ProfilDeleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTourConfirmation(view.getContext(),position,view);
            }
        });

    }

    public void deleteTourConfirmation(Context context,final int position,final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setMessage(R.string.activity_Profil_delete_tour);
        builder.setPositiveButton(R.string.activity_Profil_delete_tour_yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        _apiInterface = APIClient.getinstance(view.getContext()).create(APIInterface.class);
                        /*Call<responseProfilEdit> call = _apiInterface.DeleteTour(_tourList.get(position).getEntityId());

                        call.enqueue(new Callback<responseProfilEdit>() {
                            @Override
                            public void onResponse(Call<responseProfilEdit> call, Response<responseProfilEdit> response) {
                                Log.d("Response code:", "===========" + response.code() + "=======" + response.isSuccessful());

                                if (response.isSuccessful()) {
                                    LinearLayout parent = (LinearLayout) view.findViewById(R.id.ProfilDeleteIcon).
                                    getParent().getParent().getParent().getParent();
                                    parent.setVisibility(View.GONE);
                                    Log.d("Status response", "=========== success");
                                }
                            }

                            @Override
                            public void onFailure(Call<responseProfilEdit> call, Throwable t) {
                                Log.d("Status response", "=========== " + t);

                            }
                        });*/



                    }
                });
        builder.setNegativeButton(R.string.activity_Profil_delete_tour_no,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

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
        public TextView ProfilModerationStatus;
        public LinearLayout ProfilModerationStatusContainer;
        public ImageView ProfilIsPublishedIcon;
        public ImageView ProfilDeleteIcon;
        public TextView ProfilLikeRating;
        public ImageView ProfilEditTourIcon;
        public LinearLayout linearLayoutProfilShare;
        public ImageView ProfilLikeIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.profilVisiteImage);
            this.textView = (TextView) itemView.findViewById(R.id.profileVisiteTitle);
            this.ProfilModerationStatus = (TextView) itemView.findViewById(R.id.ProfilModerationStatus);
            this.ProfilModerationStatusContainer = (LinearLayout) itemView.findViewById(R.id.ProfilModerationStatusContainer);
            this.ProfilIsPublishedIcon = (ImageView) itemView.findViewById(R.id.ProfilIsPublishedIcon);
            this.ProfilDeleteIcon =(ImageView) itemView.findViewById(R.id.ProfilDeleteIcon);
            this.ProfilLikeRating = (TextView) itemView.findViewById(R.id.ProfilLikeRating);
            this.ProfilEditTourIcon = (ImageView) itemView.findViewById(R.id.ProfilEditTourIcon);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayoutProfil);
            this.linearLayoutProfilShare = (LinearLayout) itemView.findViewById(R.id.linearLayoutProfilShare);
            this.ProfilLikeIcon = (ImageView) itemView.findViewById(R.id.ProfilLikeIcon);
        }
    }


}
