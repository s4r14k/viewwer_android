package com.priscilla.viewwer.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.priscilla.viewwer.R;
import com.priscilla.viewwer.activity.BaseActivity;
import com.priscilla.viewwer.adapter.FavoriteRecyclerAdapter;
import com.priscilla.viewwer.adapter.VisitRecyclerAdapter;
import com.priscilla.viewwer.api.APIClient;
import com.priscilla.viewwer.api.APIInterface;
import com.priscilla.viewwer.api.ApiCallBack;
import com.priscilla.viewwer.api.ApiCallBackVisite;
import com.priscilla.viewwer.model.Favorites;
import com.priscilla.viewwer.model.UpvoteModel;
import com.priscilla.viewwer.model.responseProfil;
import com.priscilla.viewwer.model.responseSearch;
import com.priscilla.viewwer.model.responseTour;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private APIInterface _apiInterface;

    private responseSearch responseObject = new responseSearch();
    ArrayList<responseSearch> listResp = new ArrayList<responseSearch>();

    RecyclerView simpleList;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        initViews();

        //Toobar
        new BaseActivity().toolbarFragment(rootView,R.id.FavoriteToolbar,R.string.title_favorites,((AppCompatActivity)getActivity()),getActivity());

        //  simpleList = (ListView) rootView.findViewById(R.id.simpleListView);
        simpleList = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        ListeFavorite(new ApiCallBackVisite() {
            @Override
            public void onResponse(ArrayList<responseSearch> resultLike) {
                //miverina
                Log.d("azo ve",resultLike.size()+"size");
                for(responseSearch test : resultLike){
                    Log.d("liste3 response3", test.getLikedId()+"  "+test.getLiked()+ listResp.size()+ "jj" + test.getCreatedAt());
                    // Collections.sort(test.getCreatedAt());
                }
                Collections.sort(resultLike,
                        (o2, o1) -> o1.getCreatedAt().compareTo(o2.getCreatedAt()));

                FavoriteRecyclerAdapter adapter = new FavoriteRecyclerAdapter(listResp);

                //  simpleList.setAdapter(tourAdapter);
                simpleList.setHasFixedSize(true);
                simpleList.setLayoutManager(new LinearLayoutManager(getContext()));
                simpleList.setAdapter(adapter);
            }
        });


        return rootView;
    }


    public void ListeFavorite(final ApiCallBackVisite callback){
        Call<ArrayList<responseSearch>> call = _apiInterface.SearchTours("all");
        //request DossierID
        call.enqueue(new Callback<ArrayList<responseSearch>>() {

            @Override
            public void onResponse(Call<ArrayList<responseSearch>> call, Response<ArrayList<responseSearch>> response) {

                final ArrayList<responseSearch> listFavorite = new ArrayList<>();
                final ArrayList<responseSearch> objectResp = response.body();

                Call<ArrayList<Favorites>> call_2 = _apiInterface.GetListFavorites();

                call_2.enqueue(new Callback<ArrayList<Favorites>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Favorites>> call, Response<ArrayList<Favorites>> response) {
                        Log.d("Response code:", "===========" + response.code() + "=======" +response.isSuccessful());

                        if (response.isSuccessful()) {
                            ArrayList<Favorites> favorites = response.body();

                            for (Favorites list : favorites){
                                for (responseSearch AllList: objectResp
                                ) {
                                    if(list.getEntityId().equals(AllList.getEntityId())){
                                        listFavorite.add(AllList);
                                    }
                                }

                            }

                            for (final responseSearch list2 : listFavorite) {
                                BaseActivity.ListeLike(list2.getEntityId(), new ApiCallBack() {
                                    @Override
                                    public void onResponse(UpvoteModel result) {

                                        responseObject = list2;
                                        responseObject.setLiked(result.getLiked());
                                        responseObject.setLikedId(result.getLikeId());
                                        listResp.add(responseObject);

                                        for(responseSearch test : listResp){
                                            Log.d("test2",test.getLiked()+ "ekaeka" + result.getLiked()+" "+result.getLikeId());
                                        }

                                        callback.onResponse(listResp);
                                    }
                                });
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Favorites>> call, Throwable t)
                    {
                        Log.d("Status response","=========== "+t);

                    }
                });
            }
            public void onFailure(Call<ArrayList<responseSearch>> call, Throwable t) {
                Log.d("Response code:", "=====" + t.getMessage());

                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {

        // retrofit
        _apiInterface = APIClient.getinstance(getContext()).create(APIInterface.class);
    }



}
