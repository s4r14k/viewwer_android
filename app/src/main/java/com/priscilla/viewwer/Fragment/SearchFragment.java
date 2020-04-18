package com.priscilla.viewwer.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.priscilla.viewwer.R;
import com.priscilla.viewwer.activity.BaseActivity;
import com.priscilla.viewwer.activity.BottomNavigationActivity;
import com.priscilla.viewwer.activity.EditProfilActivity;
import com.priscilla.viewwer.activity.FiltreAnnonceActivity;
import com.priscilla.viewwer.adapter.VisitAdapter;
import com.priscilla.viewwer.adapter.VisitRecyclerAdapter;
import com.priscilla.viewwer.api.APIClient;
import com.priscilla.viewwer.api.APIInterface;
import com.priscilla.viewwer.api.ApiCallBack;
import com.priscilla.viewwer.api.ApiCallBackVisite;
import com.priscilla.viewwer.model.Employe;
import com.priscilla.viewwer.model.Tour;
import com.priscilla.viewwer.model.UpvoteModel;
import com.priscilla.viewwer.model.responseSearch;
import com.priscilla.viewwer.model.responseTour;
import com.priscilla.viewwer.utils.App;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private APIInterface _apiInterface;
    private ArrayList<Employe> employeList;
    private ArrayList<Tour> tourList;
    private Context context;
    private Button BtnFiltre;
    private LinearLayout linearLayoutContentFilterAndDeleteFilter;
    private Button appCompatButtonDeleteFilter;
    private LinearLayout linearLayoutDeleteFilter;

    private AlertDialog _alertDialog;

    VisitRecyclerAdapter adapter;
    //THE IMPORTANT VARIABLE
    private ArrayList<responseSearch> sampleObject;
    private ArrayList<UpvoteModel> arrayListModel;
    private responseSearch responseObject = new responseSearch();
    ArrayList<responseSearch> listResp = new ArrayList<responseSearch>();
    // ListView simpleList;
    RecyclerView simpleList;
    BaseActivity activity = (BaseActivity) getActivity();

    private Boolean isLiked2 = false;


    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        this.context = context;

        new BaseActivity().toolbarFragment(rootView,R.id.SearchToolbar,R.string.title_search,
                ((AppCompatActivity)getActivity()),getActivity());
        linearLayoutContentFilterAndDeleteFilter = (LinearLayout) rootView.findViewById(R.id.linearLayoutContentFilterAndDeleteFilter);
        linearLayoutDeleteFilter = (LinearLayout) rootView.findViewById(R.id.linearLayoutDeleteFilter);
        appCompatButtonDeleteFilter = (Button) rootView.findViewById(R.id.appCompatButtonDeleteFilter);
        initViews();
        onBtnDeleteFilter();
        // BaseActivity.initActionBarTitle(getResources().getString(R.string.activity_filtre_annonce_title_toolBar));

        //  simpleList = (ListView) rootView.findViewById(R.id.simpleListView);

        BaseActivity.showLoadingView(getContext());

        simpleList = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        BtnFiltre = (Button) rootView.findViewById(R.id.btnFiltre);
        BtnFiltre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity().getApplication(), FiltreAnnonceActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        Bundle b = getActivity().getIntent().getExtras();
        if(b != null){

            linearLayoutContentFilterAndDeleteFilter.setPadding(40,20,40,20);
            linearLayoutDeleteFilter.setVisibility(LinearLayout.VISIBLE);
            sampleObject = b.getParcelableArrayList("key");

            BtnFiltre.setText(getString(R.string.activity_list_visit_button_text_filter)+" "+sampleObject.size());
            String resultat = sampleObject.size() == 0 ? "Aucun resultat" : sampleObject.size() + " resultat";

            if(sampleObject.size() == 0){

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false);

                TextView title = new TextView(getContext());
                // You Can Customise your Title here
                title.setText(R.string.title_not_found_search);
                // title.setBackgroundColor(Color.DKGRAY);
                title.setPadding(10, 30, 10, 20);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.BLACK);
                title.setTextSize(18);
                title.setAllCaps(false);
                builder.setCustomTitle(title);

                TextView myMsg = new TextView(getContext());
                myMsg.setText(R.string.error_not_found_search);
                myMsg.setPadding(10, 10, 10, 10);
                myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                myMsg.setTextSize(18);
                builder.setView(myMsg);
                //  builder.se(Gravity.CENTER);
                // builder.setMessage(R.string.error_not_found_search);

                builder.setPositiveButton("Modifier criteres",  new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(getActivity().getApplication(), FiltreAnnonceActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("Annuler",  new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                Intent intent = new Intent(getActivity().getApplication(), BottomNavigationActivity.class);
                                startActivity(intent);
                            }
                });


                _alertDialog = builder.show();
                _alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLUE);
                _alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
                _alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
                _alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
                _alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(18);
                _alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(18);
            }
            for (final responseSearch list : sampleObject) {

            }

            for (final responseSearch res : sampleObject) {
                Log.d("result listeview bundle",res.getLiked() +"h"+ res.getEntityId() +" "+ sampleObject.size());

            }


            adapter = new VisitRecyclerAdapter(sampleObject,context);

            //  simpleList.setAdapter(tourAdapter);
            simpleList.setHasFixedSize(true);
            simpleList.setLayoutManager(new LinearLayoutManager(context));
            simpleList.setAdapter(adapter);
            BaseActivity.hideLoadingView();

        }else{
            ListeVisite(new ApiCallBackVisite() {
                public void onResponse(ArrayList<responseSearch> resultLike) {
                    //miverina
                    Log.d("azo ve",resultLike.size()+"size");
                    for(responseSearch test : resultLike){
                        Log.d("liste3 response3", test.getLikedId()+"  "+test.getLiked()+ listResp.size()+ "jj" + test.getCreatedAt());
                        // Collections.sort(test.getCreatedAt());
                    }
                    Collections.sort(resultLike,
                            (o2, o1) -> o1.getCreatedAt().compareTo(o2.getCreatedAt()));

                    adapter = new VisitRecyclerAdapter(listResp,context);
                    simpleList.setHasFixedSize(true);
                    simpleList.setLayoutManager(new LinearLayoutManager(context));
                    simpleList.setAdapter(adapter);
                }
            });
        }

        return rootView;

    }

    public void ListeVisite(final ApiCallBackVisite callback){
        Call<ArrayList<responseSearch>> call = _apiInterface.GetMyVisites();
        //request DossierID
        call.enqueue(new Callback<ArrayList<responseSearch>>() {

            @Override
            public void onResponse(Call<ArrayList<responseSearch>> call, final Response<ArrayList<responseSearch>> response) {
                ArrayList<responseSearch> objectResp = response.body();
                for (final responseSearch list : objectResp) {
                    BaseActivity.ListeLike(list.getEntityId(), new ApiCallBack() {
                        @Override
                        public void onResponse(UpvoteModel result) {

                            responseObject = list;
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
                BaseActivity.hideLoadingView();
            }
            public void onFailure(Call<ArrayList<responseSearch>> call, Throwable t) {
                Log.d("Response code:", "=====" + t.getMessage());
                //  Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                BaseActivity.hideLoadingView();
            }
        });
    }



    public void onBtnDeleteFilter() {
        appCompatButtonDeleteFilter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                linearLayoutContentFilterAndDeleteFilter.setPadding(100,20,100,20);
                linearLayoutDeleteFilter.setVisibility(LinearLayout.GONE);
                BtnFiltre.setText(getString(R.string.activity_list_visit_button_text_filter));

                ListeVisite(new ApiCallBackVisite() {
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

                        adapter = new VisitRecyclerAdapter(listResp,context);
                        simpleList.setHasFixedSize(true);
                        simpleList.setLayoutManager(new LinearLayoutManager(context));
                        simpleList.setAdapter(adapter);
                    }
                });

            }
        });
    }

    private void initViews() {
        // retrofit
        _apiInterface = APIClient.getinstance(getContext()).create(APIInterface.class);
    }
}
