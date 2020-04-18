package com.priscilla.viewwer.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.priscilla.viewwer.R;
import com.priscilla.viewwer.activity.BaseActivity;
import com.priscilla.viewwer.activity.EditProfilActivity;
import com.priscilla.viewwer.adapter.ProfilAdapter;
import com.priscilla.viewwer.adapter.ProfilRecyclerAdapter;
import com.priscilla.viewwer.adapter.VisitRecyclerAdapter;
import com.priscilla.viewwer.api.APIClient;
import com.priscilla.viewwer.api.APIInterface;
import com.priscilla.viewwer.api.ApiCallBack;
import com.priscilla.viewwer.api.ApiCallBackVisite;
import com.priscilla.viewwer.model.Favorites;
import com.priscilla.viewwer.model.Tour;
import com.priscilla.viewwer.model.UpvoteModel;
import com.priscilla.viewwer.model.responseProfil;
import com.priscilla.viewwer.model.responseSearch;
import com.priscilla.viewwer.utils.App;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ProfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private APIInterface _apiInterface;
    private ArrayList<Tour> tourList;
    private Context context;
    private ListView profilVisiteListe;
    private RecyclerView profilVisiteListeRecycler;
    private Button editProfil;

    private responseSearch responseObject = new responseSearch();
    ArrayList<responseSearch> listResp = new ArrayList<responseSearch>();

    private TextView ProfileName;

    public ProfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilFragment newInstance(String param1, String param2) {
        ProfilFragment fragment = new ProfilFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_profil, container, false);

        initViews();
        getActivity();

        //Tooblar
        new BaseActivity().toolbarFragment(rootView,R.id.ProfilToolbar,R.string.title_profile,((AppCompatActivity)getActivity()),getActivity());


        profilVisiteListeRecycler = (RecyclerView) rootView.findViewById(R.id.profilVisiteListeRecycler);
        ListeProfile(new ApiCallBackVisite() {
            public void onResponse(ArrayList<responseSearch> resultLike) {
                //miverina
                Log.d("azo ve",resultLike.size()+"size");
                for(responseSearch test : resultLike){
                    Log.d("liste3 response3", test.getLikedId()+"  "+test.getLiked()+ listResp.size()+ "jj" + test.getCreatedAt());
                    // Collections.sort(test.getCreatedAt());
                }
                Collections.sort(resultLike,
                        (o2, o1) -> o1.getCreatedAt().compareTo(o2.getCreatedAt()));

                ProfilRecyclerAdapter adapter = new ProfilRecyclerAdapter(listResp);

                //  simpleList.setAdapter(tourAdapter);
                profilVisiteListeRecycler.setHasFixedSize(true);
                profilVisiteListeRecycler.setLayoutManager(new LinearLayoutManager(context));
                profilVisiteListeRecycler.setAdapter(adapter);
            }
        });

        editProfil = (Button) rootView.findViewById(R.id.editProfil);
        editProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditActivity();
            }
        });

        //set Last Name and first Name of profil fragment
        getMyPofilName(rootView);

        return rootView;
    }

    public void getMyPofilName(final View rootView){
        Call<responseProfil> call = _apiInterface.GetMyProfil();

        //request DossierID

        call.enqueue(new Callback<responseProfil>() {

            @Override
            public void onResponse(Call<responseProfil> call, Response<responseProfil> response) {
                responseProfil profile = response.body();
                Log.d("Response code:", "=====" + response.isSuccessful()+ "code"+ response.code()+" message " + response.message());
                ProfileName = (TextView) rootView.findViewById(R.id.ProfileName);
                ProfileName.setText(profile.getFirstName()+" "+profile.getLastName());
            }
            public void onFailure(Call<responseProfil> call, Throwable t) {
                Log.d("Response code:", "=====" + t.getMessage());

                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        this.context = context;
        // retrofit
        _apiInterface = APIClient.getinstance(getContext()).create(APIInterface.class);
    }

    public void ListeProfile(final ApiCallBackVisite callback){

        Call<ArrayList<responseSearch>> call = _apiInterface.GetMyTours();

        //request DossierID
        call.enqueue(new Callback<ArrayList<responseSearch>>() {

            @Override
            public void onResponse(Call<ArrayList<responseSearch>> call, Response<ArrayList<responseSearch>> response) {
                ArrayList<responseSearch> objectResp = response.body();

                for (final responseSearch list2 : objectResp) {
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
            public void onFailure(Call<ArrayList<responseSearch>> call, Throwable t) {
                Log.d("Response code:", "=====" + t.getMessage());

                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void openEditActivity(){
        Intent intent = new Intent(getActivity().getApplication(), EditProfilActivity.class);
        startActivity(intent);

    }


}
